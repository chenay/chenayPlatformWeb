package read;


import jdbc.JdbcTest2;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;

public class ReadXLS {
    public HttpServletRequest request=null;
    public  String readXLS(InputStream inputStream, user.Table table, user.User user, String fileName){
        Workbook wb=null;
        JdbcTest2 jdbcTest2=new JdbcTest2();
        jdbcTest2.table=table;
        jdbcTest2.user=user;
        Date date=new Date();
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int uploadId=0;
//        jdbcTest2.uploadId=user.StaffNumber+"-"+simpleDateFormat.format(date);
        uploadId=jdbcTest2.insertDataConnection();
        jdbcTest2.filrName=fileName;

        String uploadResult="success";

        int read=0;

        int flag=0;
        String []attributes=table.attributes.split(",");
        int length=attributes.length-1;
        String []type=table.type.split(",");
        String []nullable=table.nullable.split(",");

        try {
            wb=new HSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = null;
        sheet=wb.getSheetAt(0);     //读取sheet 0
        int firstRowIndex = sheet.getFirstRowNum();
        int lastRowIndex = sheet.getLastRowNum();
//        System.out.println(firstRowIndex+" "+lastRowIndex);
        List<String[]> list=new ArrayList<String[]>();
        String[] strings=null;
        int column=0;
        if(sheet.getRow(0)!=null) {
            column=sheet.getRow(0).getLastCellNum();
//            System.out.println(column);
        }
        Row row = sheet.getRow(0);
        for (int cIndex = 0; cIndex < column; cIndex++) {   //遍历列
            Cell cell = row.getCell(cIndex);
            if (cell == null||cell.toString().equals("")) {
                uploadResult="错误：数据文件"+"表格栏位第1行"+(cIndex+1)+"不能为空";
                jdbcTest2.insertUpload(uploadResult,new java.sql.Date(date.getTime()));
                return uploadResult;
            }
        }
        if(column!=length){
            uploadResult="错误：数据文件"+"表格栏位数目不正确";
            jdbcTest2.insertUpload(uploadResult,new java.sql.Date(date.getTime()));
            return uploadResult;
        }

        for(int rIndex = firstRowIndex+1; rIndex <= lastRowIndex; rIndex++) {   //遍历行
            row = sheet.getRow(rIndex);
            if (row != null) {
                strings = new String[column];
                //int firstCellIndex = row.getFirstCellNum();
                int lastCellIndex = row.getLastCellNum();
//                System.out.println(firstCellIndex+"   "+lastCellIndex);
                for (int cIndex = 0; cIndex < lastCellIndex; cIndex++) {
                    //遍历列、
                    if(cIndex+1>column){
                        uploadResult="错误：数据文件"+(rIndex+1)+"行"+(cIndex+1)+"列为无效数据";
                        jdbcTest2.insertUpload(uploadResult,new java.sql.Date(date.getTime()));
                        return uploadResult;
                    }
                    Cell cell = row.getCell(cIndex);
                    if (cell != null&&!cell.toString().equals("")) {
                        flag=1;
                        if(type[cIndex].equals("VARCHAR2")){
                            strings[cIndex]=cell.toString();
                        }else if(type[cIndex].equals("NUMBER") && (cell.getCellType() == NUMERIC.getCode()) && !DateUtil.isCellDateFormatted(cell)){
                            strings[cIndex]=cell.toString();
                        }else if(type[cIndex].equals("DATE")&&DateUtil.isCellDateFormatted(cell)){
                            strings[cIndex] = cell.getDateCellValue().toString();
                        }else {
                            uploadResult="错误：数据文件"+(rIndex+1)+"行"+(cIndex+1)+"列数据类型应为"+type[cIndex];
                            jdbcTest2.insertUpload(uploadResult,new java.sql.Date(date.getTime()));
                            return uploadResult;
                        }
//                        if(cell.getCellType()==NUMERIC){
//                            if (DateUtil.isCellDateFormatted(cell)) {
//                                // 转换为日期格式YYYY-mm-dd
//                                strings[cIndex] = cell.getDateCellValue().toString();
//                            }else {
//                                strings[cIndex]=cell.toString();
//                            }
//                        }
//                        else {
//                            strings[cIndex]=cell.toString();
//                        }
                    }else {
                        if(nullable[cIndex].equals("Y")){
                            strings[cIndex]="";
                        }else {
                            if(uploadResult.equals("success")){
                                uploadResult="错误：数据文件"+(rIndex+1)+"行"+(cIndex+1)+"列不能为空";
                            }
                            if(cIndex==lastCellIndex-1&&flag==0){
                                jdbcTest2.insertUpload(uploadResult,new java.sql.Date(date.getTime()));
                                return uploadResult;
                            }
                        }
                    }
//                    System.out.println(strings[cIndex]);
                }
                flag=0;
                list.add(strings);

                if(list.size()==3000){
                    if(read==0){
                        read=1;

                    }
                    jdbcTest2.insertData(list);
                    list.clear();
                }
            }
        }
        if(read==0){

        }
        jdbcTest2.insertData(list);
        list.clear();
        HttpSession session=request.getSession();
        if(session.getAttribute("cancel")==null){
            jdbcTest2.commit();
            uploadResult=uploadResult+","+jdbcTest2.uploadNumber;
        }else {
            jdbcTest2.rollback();
            uploadResult="failure";
            session.removeAttribute("cancel");
        }
//        jdbcTest2.commit();
        jdbcTest2.insertUpload(uploadResult,new java.sql.Date(date.getTime()));
        if(uploadResult.contains("success")){
            System.out.println(uploadId);
            return "success"+","+uploadId+","+jdbcTest2.uploadNumber;
        }
        return uploadResult;
    }
    public static void main(String[] args) throws Exception {

        ReadXLS readXLS=new ReadXLS();
        user.Table table=new user.Table();
        String tableString="{\"attributes\":\"PRICE_LIST,DESCRIPTION,CURRENCY_CODE,PRICE_LIST_START_DATE,PRICE_LIST_END_DATE,ACTIVE_FLAG,PRODUCT_ATTRIBUTE_CONTEXT,PRODUCT_ATTRIBUTE,ITEM_NO,IO_MASTER_ITEM,ITEM_DESCRIPTION,PRODUCT_UOM_CODE,LINE_TYPE,APPLICATION_METHOD,VALUE,PRICE_START_DATE,PRICE_END_DATE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,CHANGE_REASON,PROJECT_CODE,REGION_CODE,NOTEID\",\"database\":\"apps.UATA\",\"length\":\"240,240,240,240,7,240,240,240,240,240,240,240,240,240,22,240,240,240,7,240,7,240,240,240,22\"," +
                "\"nullable\":\"N,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y\",\"tableId\":1,\"tablename\":\"MIS_DBG_TEST2\",\"type\":\"VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,DATE,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,NUMBER,DATE,DATE,VARCHAR2,DATE,VARCHAR2,DATE,VARCHAR2,VARCHAR2,VARCHAR2,NUMBER\"}";

//        String tableString="{\"attributes\":\"ID3,NAME3,BIRTH3,SALARY3,REMARK3,UPLOADID\",\"database\":\"apps.UATA\",\"length\":\"22,240,7,22,200,240\",\"nullable\":\"N,Y,Y,Y,Y,Y\",\"ps\":\"3\",\"remark\":\"3\",\"tableId\":4,\"tablename\":\"MIS_DBG_3\",\"type\":\"NUMBER,VARCHAR2,DATE,NUMBER,VARCHAR2,VARCHAR2\"}";
        JSONObject jsonObject = JSONObject.parseObject(tableString);
        table=JSONObject.toJavaObject(jsonObject, user.Table.class);

        user.User user=new user.User();
        String userString ="{\"Name\":\"1 2\",\"Password\":\"123\",\"Role\":\"3;4;\",\"StaffNumber\":\"dsa\",\"createDate\":\"2018-10-26 10:37:37\",\"name\":\"1 2\",\"password\":\"123\",\"role\":\"3;4;\",\"staffNumber\":\"dsa\",\"updateDate\":\"2018-11-12 10:28:28\"}";
        jsonObject=JSONObject.parseObject(userString);
        user=JSONObject.toJavaObject(jsonObject, user.User.class);

        String filename = "C:\\Users\\bg.ding\\Desktop\\Price_list_All2.xls";
        FileInputStream file =new FileInputStream(filename);
        System.out.println("-- 程序开始 --");
        long time_1 = System.currentTimeMillis();
        System.out.println(readXLS.readXLS(file,table,user,filename));
//        for(int i=0;i<list.size();i++){
//            for(int j=0;j<list.get(i).length;j++){
//                System.out.println(list.get(i)[j]+",");
//            }
//        }
        long time_2 = System.currentTimeMillis();
        System.out.println("-- 程序结束 --");
        System.out.println("-- 耗时 --"+(time_2 - time_1)+"ms");
    }
}
