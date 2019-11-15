package read;


import jdbc.JdbcTest2;
import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReadCSV {
    public HttpServletRequest request=null;
    public  String readCSV(InputStream inputStream, user.Table table, user.User user, String fileName){
        List<String[]> list = new ArrayList<String[]>();
        String [] strings =null;
        CsvReader csvReader=null;

        JdbcTest2 jdbcTest2=new JdbcTest2();
        jdbcTest2.table=table;
        jdbcTest2.user=user;
        Date date=new Date();
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int uploadId=0;
//        jdbcTest2.uploadId=user.StaffNumber+"-"+simpleDateFormat.format(date);
        jdbcTest2.filrName=fileName;
        uploadId=jdbcTest2.insertDataConnection();

        String []attributes=table.attributes.split(",");
        int length=attributes.length-1;
        String []type=table.type.split(",");
        String []nullable=table.nullable.split(",");


        SimpleDateFormat simpleDateFormat1= new SimpleDateFormat("yyyy/MM/dd HH:mm");
        SimpleDateFormat simpleDateFormat2= new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat simpleDateFormat3= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat4=new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        String uploadResult="success";


        int flag=0;
        int read=0;


        int slength=0;

        try {
            csvReader = new CsvReader(new InputStreamReader(inputStream,"GBK")) ;
            //csvReader.readHeaders();
//            int length=0;
            int row=0;
            while (csvReader.readRecord()){
                row++;
                // 读一整行
                //System.out.println(csvReader.getRawRecord());
                // 读这行的某一列
//                System.out.println(csvReader.getRawRecord());
                strings=csvReader.getRawRecord().split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
//                System.out.println(strings.length+length);
                if(row==1){
                    //length=strings.length;
                    for(int i=0;i<strings.length;i++){
                        if(strings[i].equals("")){
                            uploadResult="错误：数据文件"+"表格栏位第1行"+(i+1)+"不能为空";
                            jdbcTest2.insertUpload(uploadResult,new java.sql.Date(date.getTime()));
                            return uploadResult;
                        }
                    }
                    if(length!=strings.length){
                        uploadResult="错误：数据文件"+"表格栏位数目不正确";
                        jdbcTest2.insertUpload(uploadResult,new java.sql.Date(date.getTime()));
                        return uploadResult;
                    }
                    continue;
                }

                String []strings1=new String[length];

                slength=strings.length;

                for(int i=0;i<slength;i++){
                    if(i>=length){
                        uploadResult="错误：数据文件"+(row)+"行"+(i+1)+"列为无效数据";
                        jdbcTest2.insertUpload(uploadResult,new java.sql.Date(date.getTime()));
                        return uploadResult;
                    }
                    String str=strings[i];
                    if(str != null && !str.equals("")) {
                        flag=1;
                        if(str.length()>=2){
                            if(str.indexOf("\"")==0) str = str.substring(1,str.length());
                            if(str.lastIndexOf("\"")==(str.length()-1)) str = str.substring(0,str.length()-1);
                            str = str.replaceAll("\"\"","\"");
                        }

                        if(type[i].equals("NUMBER")){
                            try {
                                String s2=str.replace(",","");
                                s2=s2.replace("(","-");
                                s2=s2.replace(")","");
                                Double.parseDouble(s2);
                                str=s2;
                            }catch (Exception e2){
                                uploadResult="错误：数据文件"+row+"行"+(i+1)+"列要求为数字";
                                jdbcTest2.insertUpload(uploadResult,new java.sql.Date(date.getTime()));
                                return uploadResult;
                            }
                        }else if(type[i].equals("DATE")){
                            Date date1=null;
                            try {
                                date1=simpleDateFormat1.parse(str);
                                str=simpleDateFormat4.format(date1);
                            }catch (ParseException p){
                                try {
                                    date1=simpleDateFormat2.parse(str);
                                    str=simpleDateFormat4.format(date1);
                                }catch (ParseException p2){
                                    try {
                                        date1=simpleDateFormat3.parse(str);
                                        str=simpleDateFormat4.format(date1);
                                    }catch (ParseException p3){
                                        uploadResult="错误：数据文件"+row+"行"+(i+1)+"列数据类型应为日期型";
                                        jdbcTest2.insertUpload(uploadResult,new java.sql.Date(date.getTime()));
                                        return uploadResult;
                                    }
                                }
                            }
                        }
                        strings1[i]=str;
                    }else{
                        if(nullable[i].equals("Y")){
                            strings[i]="";
                        }else{
                            if(uploadResult.equals("success")){
                                uploadResult="错误：数据文件"+(row)+"行"+(i+1)+"列不能为空";
                            }
                            if(i==slength-1&&flag==0){
                                jdbcTest2.insertUpload(uploadResult,new java.sql.Date(date.getTime()));
                                return uploadResult;
                            }
                        }
                    }
//                    System.out.println(strings1[i]);
                }
                flag=0;
//                for(int i=strings.length;i<length;i++){
//                    System.out.println(strings);
//                }
//                System.out.println();
                list.add(strings1);
                if(list.size()==3000){
                    if(read==0){
                        read=1;


                    }
                    jdbcTest2.insertData(list);
                    list.clear();
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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return uploadResult;
    }

    public static void main(String[] args) throws Exception {
        String filename = "C:\\Users\\bg.ding\\Desktop\\csv.csv";
        ReadCSV readCSV=new ReadCSV();
        user.Table table=new user.Table();
//        String tableString="{\"attributes\":\"PRICE_LIST,DESCRIPTION,CURRENCY_CODE,PRICE_LIST_START_DATE,PRICE_LIST_END_DATE,ACTIVE_FLAG,PRODUCT_ATTRIBUTE_CONTEXT,PRODUCT_ATTRIBUTE,ITEM_NO,IO_MASTER_ITEM,ITEM_DESCRIPTION,PRODUCT_UOM_CODE,LINE_TYPE,APPLICATION_METHOD,VALUE,PRICE_START_DATE,PRICE_END_DATE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,CHANGE_REASON,PROJECT_CODE,REGION_CODE,NOTEID\",\"database\":\"apps.UATA\",\"length\":\"240,240,240,240,7,240,240,240,240,240,240,240,240,240,22,240,240,240,7,240,7,240,240,240,22\"," +
//                "\"nullable\":\"N,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y\",\"tableId\":1,\"tablename\":\"MIS_DBG_TEST2\",\"type\":\"VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,DATE,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,NUMBER,DATE,DATE,VARCHAR2,DATE,VARCHAR2,DATE,VARCHAR2,VARCHAR2,VARCHAR2,NUMBER\"}";

        String tableString="{\"attributes\":\"ID3,NAME3,BIRTH3,SALARY3,REMARK3,UPLOADID\",\"database\":\"apps.UATA\",\"length\":\"22,240,7,22,200,240\",\"nullable\":\"N,Y,Y,Y,Y,Y\",\"ps\":\"3\",\"remark\":\"3\",\"tableId\":4,\"tablename\":\"MIS_DBG_3\",\"type\":\"NUMBER,VARCHAR2,DATE,NUMBER,VARCHAR2,VARCHAR2\"}";
        JSONObject jsonObject = JSONObject.parseObject(tableString);
        table=JSONObject.toJavaObject(jsonObject, user.Table.class);

        user.User user=new user.User();
        String userString ="{\"Name\":\"1 2\",\"Password\":\"123\",\"Role\":\"3;4;\",\"StaffNumber\":\"dsa\",\"createDate\":\"2018-10-26 10:37:37\",\"name\":\"1 2\",\"password\":\"123\",\"role\":\"3;4;\",\"staffNumber\":\"dsa\",\"updateDate\":\"2018-11-12 10:28:28\"}";
        jsonObject=JSONObject.parseObject(userString);
        user=JSONObject.toJavaObject(jsonObject, user.User.class);
        FileInputStream file =new FileInputStream(filename);
        System.out.println("-- 程序开始 --");
        long time_1 = System.currentTimeMillis();
        System.out.println(readCSV.readCSV(file,table,user,filename));
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
