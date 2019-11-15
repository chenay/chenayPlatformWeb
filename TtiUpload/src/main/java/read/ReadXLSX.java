package read;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import jdbc.JdbcTest2;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * 自定义解析处理器
 * See org.xml.sax.helpers.DefaultHandler javadocs
 *        2016-12-21   下午3:20:34
 * ECP  com.use.prefecture.employees.util.change.card  CardChangeXxlsAnalyze
 */
public class ReadXLSX{

    private static StylesTable stylesTable;

    /**
     * 处理一个sheet
     * @param filename
     * @throws Exception
     */
    public  HttpServletRequest request=null;
    public String processOneSheet(File filename, user.Table table, user.User user, String fileName ) throws Exception {



        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader( pkg );
        stylesTable = r.getStylesTable();
        SharedStringsTable sst = r.getSharedStringsTable();

        XMLReader parser = fetchSheetParser(sst,table,user,fileName,request);
        SheetHandler handler = new SheetHandler(sst,table,user,fileName,request);
        parser.setContentHandler(handler);
        // Seems to either be rId# or rSheet#
        InputStream sheet2 = r.getSheet("rId1");
        InputSource sheetSource = new InputSource(sheet2);

        try{
            parser.parse(sheetSource);

        }catch (Exception e){
            e.printStackTrace();
        }


        sheet2.close();



       return handler.getExcelMap();
    }

    /**
     * 处理所有sheet
     * @param filename
     * @return
     * @throws Exception
     */
//    public String processAllSheets(File filename) throws Exception {
//
//        OPCPackage pkg = OPCPackage.open(filename);
//        XSSFReader r = new XSSFReader( pkg );
//        stylesTable = r.getStylesTable();
//        SharedStringsTable sst = r.getSharedStringsTable();
//
//        //XMLReader parser = fetchSheetParser(sst);
//
//        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
//        SheetHandler handler = new SheetHandler(sst);
//        parser.setContentHandler(handler);
//
//        Iterator<InputStream> sheets = r.getSheetsData();
//        while(sheets.hasNext()) {
//            InputStream sheet = sheets.next();
//            InputSource sheetSource = new InputSource(sheet);
//            parser.parse(sheetSource);
//            sheet.close();
//        }
//
//        return handler.getExcelMap();
//    }

    /**
     * 获取解析器
     * @param sst
     * @return
     * @throws SAXException
     */
    public XMLReader fetchSheetParser(SharedStringsTable sst, user.Table table, user.User user, String fileName, HttpServletRequest request) throws SAXException {
        XMLReader parser =
                XMLReaderFactory.createXMLReader(
                        "org.apache.xerces.parsers.SAXParser"
                );
        ContentHandler handler = new SheetHandler(sst,table,user,fileName,request);
        parser.setContentHandler(handler);
        return parser;
    }



    private static class SheetHandler extends DefaultHandler {


        public void endUpload(){
            if(num==0){
                uploadId=jdbcTest2.insertDataConnection();
            }
            jdbcTest2.insertUpload(uploadResult,new java.sql.Date(date.getTime()));
            String [] strings=new String[1];
            strings[2]="";
        }

        int col=0;

        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;

        private List<String> rowlist = new ArrayList<String>();
        private int curRow = 0;
        private int curCol = 0;

        //定义前一个元素和当前元素的位置，用来计算其中空的单元格数量，如A6和A8等
        private String preRef = null, ref = null;

        private List<String> refNames=new ArrayList<>();

        //定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格
        private String maxRef = null;

        private CellDataType nextDataType = CellDataType.VARCHAR2;
        private final DataFormatter formatter = new DataFormatter();
        private short formatIndex;
        private String formatString;

        public  HttpServletRequest request=null;

        //用一个enum表示单元格可能的数据类型
        enum CellDataType{
            BOOL, ERROR, FORMULA, INLINESTR, VARCHAR2, NUMBER, DATE, NULL
        }

        private SheetHandler(SharedStringsTable sst, user.Table table, user.User user, String fileName, HttpServletRequest request) {
            this.sst = sst;
            this.request=request;
            jdbcTest2.table=table;
            date=new Date();
            simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            jdbcTest2.user=user;
//            uploadId=user.StaffNumber+"-"+simpleDateFormat.format(date);
//            jdbcTest2.uploadId=user.StaffNumber+"-"+simpleDateFormat.format(date);
            jdbcTest2.filrName=fileName;
            this.table=table;


        }
        /**
         * 解析一个element的开始时触发事件
         */

        int c=1;

        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {

            // c => cell
//            System.out.println(uri+"  "+localName+"   "+name+"   "+attributes);

//            if(!uploadResult.equals("success")){
//                System.out.println(uploadResult);
//            }
//            System.out.println(c);
            if(name.equals("c")) {
                //前一个单元格的位置
                if(preRef == null){
                    preRef = attributes.getValue("r");
                }else{
                    preRef = ref;
                }
                //当前单元格的位置

                ref = attributes.getValue("r");

                refNames=new ArrayList<>();

                this.setNextDataType(attributes);

                // Figure out if the value is an index in the SST
                String cellType = attributes.getValue("t");
                if(cellType != null && cellType.equals("s")) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }

            }
            // Clear contents cache
            lastContents = "";
        }

        /**
         * 根据element属性设置数据类型
         * @param attributes
         */
        public void setNextDataType(Attributes attributes){

            nextDataType = CellDataType.NUMBER;
            formatIndex = -1;
            formatString = null;
            String cellType = attributes.getValue("t");
            String cellStyleStr = attributes.getValue("s");
            if ("b".equals(cellType)){
                nextDataType = CellDataType.BOOL;
            }else if ("e".equals(cellType)){
                nextDataType = CellDataType.ERROR;
            }else if ("inlineStr".equals(cellType)){
                nextDataType = CellDataType.INLINESTR;
            }else if ("s".equals(cellType)){
                nextDataType = CellDataType.VARCHAR2;
            }else if ("str".equals(cellType)){
                nextDataType = CellDataType.FORMULA;
            }
            else if (cellStyleStr != null){

                int styleIndex = Integer.parseInt(cellStyleStr);


                XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
                formatIndex = style.getDataFormat();
                formatString = style.getDataFormatString();

                if (formatString.contains("yy")){
                    nextDataType = CellDataType.DATE;
                    //full format is "yyyy-MM-dd hh:mm:ss.";

                    formatString = "EEE MMM dd HH:mm:ss zzz yyyy";
//                    formatString = "yyyy/MM/dd,hh:mm:ss";
                }
//                if (formatString == null){
//                    nextDataType = CellDataType.NULL;
//                    formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
//                }
            }
        }

        /**
         * 解析一个element元素结束时触发事件
         */
        public void endElement(String uri, String localName, String name)
                throws SAXException {
            // Process the last contents as required.
            // Do now, as characters() may be called more than once


            //System.out.println(name+ref);

            if(nextIsString) {
                int idx = Integer.parseInt(lastContents);
               lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();

                nextIsString = false;
            }

            // v => contents of a cell
            // Output after we've seen the string contents
            if(curRow>0){
                refNames.add(name);
            }
            if (name.equals("v")) {
                //System.out.println("ppp"+lastContents+"ooo");
                String value = this.getDataValue(lastContents.trim(), "");
                if(curRow==0){
                    rowlist.add(curCol,value);
                    if(curCol!=countNullCell(ref,"A1")&&uploadResult.equals("success")){

                        uploadResult="错误：数据文件表格栏位第1行"+(curCol+1)+"不能为空";

                        System.out.println(uploadResult);

                        curCol=countNullCell(ref,"A1");

                        endUpload();

                    }
                    curCol++;
                    //excelList[curCol]=value;
                }
                else {
                    //rowlist.set(countNullCell(ref,"A"), value);
                    int i=countNullCell(ref,"A");

//                    if(i+1-curCol==col){
//                        System.out.println("hhh");
//                        endUpload();
//                    }
                    if(value.equals("")){

                    }else{
                        if(!type[i].equals("VARCHAR2")&&!type[i].equals(nextDataType+"")&&uploadResult.equals("success")){
                            char []chars=ref.toCharArray();
                            System.out.println(ref);
                            String row="";
                            for(int j=0;j<chars.length;j++){
                                try{
                                    row+=Integer.parseInt(chars[j]+"");
                                }catch (NumberFormatException e){
                                    continue;
                                }
                            }
                            uploadResult="错误：数据文件"+row+"行"+(curCol+1) +"列类型应为"+type[i]+",但是数据值为"+nextDataType;
                            System.out.println( uploadResult);
                            endUpload();
                        }

                        if(curCol!=i&&uploadResult.equals("success")){
                            if(nullable[curCol].equals("Y")){
                                curCol=i;
                            }else if(uploadResult.equals("success")) {
                                char []chars=ref.toCharArray();
                                System.out.println(ref);
                                String row="";
                                for(int j=0;j<chars.length;j++){
                                    try{
                                        row+=Integer.parseInt(chars[j]+"");
                                    }catch (NumberFormatException e){
                                        continue;
                                    }
                                }
                                uploadResult="错误：数据文件"+row+"行"+(curCol+1) +"列不能为空";
                                System.out.println(uploadResult);
                                endUpload();
                            }
                        }
                        if(curCol>=length&&uploadResult.equals("success")){
                            char []chars=ref.toCharArray();
                            System.out.println(ref);
                            String row="";
                            for(int j=0;j<chars.length;j++){
                                try{
                                    row+=Integer.parseInt(chars[j]+"");
                                }catch (NumberFormatException e){
                                    continue;
                                }
                            }
                            uploadResult="错误：数据文件"+row+"行"+(curCol+1) +"列数据无效";
                            System.out.println( uploadResult);
                            endUpload();
                        }
                    }







                    curCol++;


                    excelList[i]=value;


                    //System.out.println(countNullCell(ref,"A")+"    "+excelList[countNullCell(ref,"A")]);
                }


//                if()
                //System.out.println(ref+countNullCell(ref,"A")+"  "+nextDataType);

            }else {

                //如果标签名称为 row，这说明已到行尾，调用 optRows() 方法
                if (name.equals("row")) {
                    //System.out.println(curRow);
                    //System.out.println(curRow);
                    //默认第一行为表头，以该行单元格数目为最大数目
                    if(curRow == 0){
                        attributes=table.attributes.split(",");
                        length=attributes.length-1;
                        type=table.type.split(",");
                        nullable=table.nullable.split(",");

                        maxRef = ref;
                        col=countNullCell(maxRef,"A1")+1;
                        excelList=new String[length];

                       // System.out.println(rowlist.size()+"  "+length+"  "+col);
                        if((rowlist.size()!=length)&&uploadResult.equals("success")){

                            uploadResult="错误：数据文件"+"表格栏位数目不正确";
                            System.out.println(uploadResult);
                            endUpload();

//                            if(rowlist.size()==col){
//
//                            }else {
//                                uploadResult="表格栏位不能有空";
//                                System.out.println("表格栏位不能有空");
//                            }

                        }

                        for(int i=0;i<col;i++){
                            excelList[i]=rowlist.get(i);
                        }
                        //allNull=1;
                    }

//                    if(allNull==0){
//                       // endUpload();
//                        System.out.println(curRow);
//                    }

                    optRows(sheetIndex,curRow,excelList);
//                    allNull=0;
                    //System.out.println(curRow+ref);

                    curRow++;



                    //一行的末尾重置一些数据
//                    rowlist.clear();
//                    for(int i=0;i<col;i++){
//                        rowlist.add("");
//                    }
                    excelList=new String[col];
                    for(int i=0;i<col;i++){
                        excelList[i]="";
                    }
                    curCol = 0;
                    preRef = null;
                    ref = null;
                }
            }
        }
        private int sheetIndex = -1;
        //private int totalCurCol=1;
        private int rowIndex=0;
        private List<String[]> excelMap=new ArrayList<String[]>();;
        String[] excelList=null;




        public user.Table table=new user.Table();
        String [] attributes=null;
        String [] type=null;
        String [] nullable=null;
        int length=0;
//        public User.User user=new User.User();
//        public String fileName=new String();
        //public String message=new String();
        public JdbcTest2 jdbcTest2=new JdbcTest2();
        public int num=0;
        public String uploadResult="success";
        Date date=null;
        SimpleDateFormat simpleDateFormat =null;
        int uploadId=0;


        public void optRows(int sheetIndex, int curRow, String[] excelList){



//            System.out.println(c);
//            c++;
//            int size=rowlist.size();
//            String[] excelList=new String[size];

          //  System.out.println(curRow);
            excelMap.add(excelList);
           // System.out.println(excelList[14]);

//            jdbcTest2.insertData(excelMap,table,300);
//            excelMap.clear();

            if(excelMap.size()==3000){

              //System.out.println(excelMap.get(0)[14]+"   "+excelMap.get(1)[14]);

                if(num==0){

                    uploadId=jdbcTest2.insertDataConnection();
                    excelMap.remove(0);



                    num=1;
                }

                //jdbcTest2.insertData(excelMap)

                jdbcTest2.insertData(excelMap);
                excelMap.clear();
//                jdbcTest2.insertData();


            }
            //System.out.println(i);
        }
        /*
         * @param
         */
        public String getExcelMap() {


            if(uploadResult.equals("success")){
                if(num==0){
                    uploadId=jdbcTest2.insertDataConnection();
                    excelMap.remove(0);
                    num=1;
                }
                jdbcTest2.insertData(excelMap);
                excelMap.clear();
                HttpSession session=request.getSession();
                if(session.getAttribute("cancel")==null){
                    jdbcTest2.commit();
                    uploadResult=uploadResult+","+jdbcTest2.uploadNumber;
                }else {
                    jdbcTest2.rollback();
                    uploadResult="failure";
                    session.removeAttribute("cancel");
                }
                jdbcTest2.insertUpload(uploadResult,new java.sql.Date(date.getTime()));
            }
            if(uploadResult.contains("success")){
                System.out.println(uploadId);
                return "success"+","+uploadId+","+jdbcTest2.uploadNumber;
            }
            return uploadResult;
        }
        /**
         * 根据数据类型获取数据
         * @param value
         * @param thisStr
         * @return
         */
        public String getDataValue(String value, String thisStr)

        {
            switch (nextDataType)
            {
                //这几个的顺序不能随便交换，交换了很可能会导致数据错误
                case BOOL:
                    char first = value.charAt(0);
                    thisStr = first == '0' ? "FALSE" : "TRUE";
                    break;
                case ERROR:
                    thisStr = "\"ERROR:" + value.toString() + '"';
                    break;
                case FORMULA:
                    thisStr = '"' + value.toString() + '"';
                    break;
                case INLINESTR:
                    XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
                    thisStr = rtsi.toString();
                    rtsi = null;
                    break;
                case VARCHAR2:
                    thisStr = value.toString();
                    break;
                case NUMBER:
//                    if (formatString != null){
//
//                        thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString).trim();
//                    }else{
//                        thisStr = value;
//                    }
                    thisStr = value;
                    //thisStr = thisStr.replace("_", "").trim();

//                    System.out.println(formatString+"    "+value);
                    break;
                case DATE:
                    try{
//                        System.out.println(formatString+" "+formatIndex+" "+value.toString());
                        thisStr = formatter.formatRawCellContents(Double.parseDouble(value), 22, formatString);
                    }catch(NumberFormatException ex){
                        thisStr = value.toString();
                    }
//                    System.out.println(thisStr);
                    break;
                default:
                    thisStr = "";
                    break;
            }
            return thisStr;
        }

        /**
         * 获取element的文本数据
         */
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            lastContents += new String(ch, start, length);
        }

        /**
         * 计算两个单元格之间的单元格数目(同一行)
         * @param ref
         * @param preRef
         * @return
         */
        public int countNullCell(String ref, String preRef){
            //excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
            String xfd = ref.replaceAll("\\d+", "");
            String xfd_1 = preRef.replaceAll("\\d+", "");

            xfd = fillChar(xfd, 3, '@', true);
            xfd_1 = fillChar(xfd_1, 3, '@', true);

            char[] letter = xfd.toCharArray();
            char[] letter_1 = xfd_1.toCharArray();
            int res = (letter[0]-letter_1[0])*26*26 + (letter[1]-letter_1[1])*26 + (letter[2]-letter_1[2]);
            return res;
        }

        /**
         * 字符串的填充
         * @param str
         * @param len
         * @param let
         * @param isPre
         * @return
         */
        String fillChar(String str, int len, char let, boolean isPre){
            int len_1 = str.length();
            if(len_1 <len){
                if(isPre){
                    for(int i=0;i<(len-len_1);i++){
                        str = let+str;
                    }
                }else{
                    for(int i=0;i<(len-len_1);i++){
                        str = str+let;
                    }
                }
            }
            return str;
        }
    }







    public static void main(String[] args) throws Exception {
        ReadXLSX example = new ReadXLSX();
        user.Table table=new user.Table();
//        String tableString="{\"attributes\":\"PRICE_LIST,DESCRIPTION,CURRENCY_CODE,PRICE_LIST_START_DATE,PRICE_LIST_END_DATE,ACTIVE_FLAG,PRODUCT_ATTRIBUTE_CONTEXT,PRODUCT_ATTRIBUTE,ITEM_NO,IO_MASTER_ITEM,ITEM_DESCRIPTION,PRODUCT_UOM_CODE,LINE_TYPE,APPLICATION_METHOD,VALUE,PRICE_START_DATE,PRICE_END_DATE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,CHANGE_REASON,PROJECT_CODE,REGION_CODE,NOTEID\",\"database\":\"apps.UATA\",\"length\":\"240,240,240,240,7,240,240,240,240,240,240,240,240,240,22,240,240,240,7,240,7,240,240,240,22\"," +
//                "\"nullable\":\"N,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y\",\"tableId\":1,\"tablename\":\"MIS_DBG_TEST2\",\"type\":\"VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,DATE,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,NUMBER,DATE,DATE,VARCHAR2,DATE,VARCHAR2,DATE,VARCHAR2,VARCHAR2,VARCHAR2,NUMBER\"}";


        String tableString="{\"attributes\":\"ID3,NAME3,BIRTH3,SALARY3,REMARK3,UPLOADID\",\"database\":\"apps.UATA\",\"length\":\"22,240,7,22,200,240\",\"nullable\":\"Y,Y,Y,Y,Y,Y\",\"ps\":\"3\",\"remark\":\"3\",\"tableId\":4,\"tablename\":\"MIS_DBG_3\",\"type\":\"NUMBER,VARCHAR2,DATE,NUMBER,VARCHAR2,VARCHAR2\"}";
        JSONObject jsonObject = JSONObject.parseObject(tableString);
        table=JSONObject.toJavaObject(jsonObject, user.Table.class);

        user.User user=new user.User();
        String userString ="{\"Name\":\"1 2\",\"Password\":\"123\",\"Role\":\"3;4;\",\"StaffNumber\":\"dsa\",\"createDate\":\"2018-10-26 10:37:37\",\"name\":\"1 2\",\"password\":\"123\",\"role\":\"3;4;\",\"staffNumber\":\"dsa\",\"updateDate\":\"2018-11-12 10:28:28\"}";
        jsonObject=JSONObject.parseObject(userString);
        user=JSONObject.toJavaObject(jsonObject, user.User.class);

        String filename = "C:\\Users\\bg.ding\\Desktop\\hhhh.xlsx";
        File file =new File(filename);
        System.out.println("-- 程序开始 --");
        long time_1 = System.currentTimeMillis();
       // System.out.println(example.processOneSheet(file,table,user,filename));
        long time_2 = System.currentTimeMillis();

        System.out.println("-- 程序结束 --");
        System.out.println("-- 耗时 --"+(time_2 - time_1)+"ms");
    }
}

