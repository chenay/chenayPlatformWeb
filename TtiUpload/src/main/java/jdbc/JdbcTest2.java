package jdbc;

import user.*;


import oracle.jdbc.driver.OracleDriver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by 10412 on 2016/12/27.
 * JDBC的六大步骤
 * JAVA连接Oracle的三种方式
 */
public class JdbcTest2 //implements Runnable
{
    Connection connect = null;
    Statement statement = null;
    static ResultSet resultSet = null;

    public String url="10.34.0.131:1528/UATA";
    public String userName="apps";
    public String password="ucweb9638a";




    public Boolean Connection(String url,String user,String password){

        try {
            //第一步：注册驱动
            //第一种方式：类加载(常用)
            //Class.forName("oracle.jdbc.OracleDriver");

            //第二种方式：利用Driver对象
            Driver driver = new OracleDriver();
            DriverManager.deregisterDriver(driver);


            //第二步：获取连接
            //第一种方式：利用DriverManager（常用）
            connect = DriverManager.getConnection("jdbc:oracle:thin:@//"+url, user, password);

            //jdbc:oracle:thin:@//10.34.0.131:1528/UATA", "apps", "ucweb9638a

            //第二种方式：直接使用Driver
//            Properties pro = new Properties();
//            pro.put("user", "apps ");//你的oracle数据库用户名
//            pro.put("password", "ucweb9638a");//用户名密码
//            connect = driver.connect("jdbc:oracle:thin:@ 10.34.0.131:1528:UATA", pro);

            //测试connect正确与否
            System.out.println(connect);}
        catch (SQLException e1) {
            e1.printStackTrace();
            return false;
        }
        return true;
    }


    public List<Table> selectTable(User user)  {


        this.Connection(this.url,this.userName,this.password);


        List<Table> tables=new ArrayList<Table>();

        if(user.Role==null||user.Role.equals("")){
            return tables;
        }



        try {

            PreparedStatement pre = connect.prepareStatement("select  alias from TTI_DB.MIS_UPL_DATABASE where status=  'yes'  ");
            //System.out.println("s:"+user.StaffNumber);
            if(user.StaffNumber!=null){
                pre = connect.prepareStatement("select  alias from TTI_DB.MIS_UPL_DATABASE  ");
            }
            ArrayList<String> aliases=new ArrayList<String>();
            resultSet=pre.executeQuery();
            while (resultSet.next()){
                aliases.add(resultSet.getString("alias"));
            }
            List<String> tableIds=new ArrayList<String>();
            pre =connect.prepareStatement("select tableId,database from TTI_DB.MIS_UPL_TABLE ");
            resultSet=pre.executeQuery();
            while(resultSet.next()){
                if(aliases.contains(resultSet.getString("database"))){
                    tableIds.add(resultSet.getString("tableId"));
                }
            }

            String []roles=user.Role.split(";");
            for(int i=0;i<roles.length;i++){
                if(roles[i].equals("")){
                    continue;
                }
                if(tableIds.contains(roles[i])){



                    PreparedStatement preState = connect.prepareStatement("select  * from TTI_DB.MIS_UPL_TABLE where " +
                            "tableId=?");
                    preState.setInt(1,Integer.parseInt(roles[i]));

                    resultSet = preState.executeQuery();
                    while (resultSet.next())
                    {
                        Table table=new Table();
                        table.setTablename(resultSet.getString("tablename"));
                        table.setAttributes(resultSet.getString("attributes"));
                        table.setType(resultSet.getString("type"));
                        table.setNullable(resultSet.getString("nullable"));
                        table.setRemark(resultSet.getString("remark"));
                        table.setDatabase(resultSet.getString("database"));
                        table.setLength(resultSet.getString("length"));
                        table.setPs(resultSet.getString("ps"));
                        table.setTableId(resultSet.getInt("tableId"));
                        tables.add(table);
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        this.close();
        return tables;
    }



//    public String url2="";
//    public String userName2="";
//    public String password2="";

    public int insertDataConnection(){
        try{
            tablename=table.tablename;
            attributes=table.attributes.split(",");
            type=table.type.split(",");
            nullable=table.nullable.split(",");
            database=table.database;

            length=type.length-1;

            for(int i=0;i<length;i++){
                s+=",?";
            }



            this.Connection(this.url,this.userName,this.password);

            PreparedStatement pre = connect.prepareStatement("select  * from TTI_DB.MIS_UPL_DATABASE where alias=?");
            pre.setString(1,database);
            resultSet=pre.executeQuery();
            String url="";
            String userName="";
            String password="";
            while (resultSet.next()){
                url=resultSet.getString("url");
                userName=resultSet.getString("userName");
                password=JdbcTest2.decode(resultSet.getString("password"));
            }

            pre=connect.prepareStatement("select tti_db.MIS_upl_upload_log_SEQ.Nextval as uploadId  from dual");
            resultSet=pre.executeQuery();
            while (resultSet.next()){
                uploadId=resultSet.getInt("uploadId");
            }

            this.close();
            this.Connection(url,userName,password);

            ps = connect.prepareStatement("insert into "+tablename+" values ("+s+")");
            connect.setAutoCommit(false);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return uploadId;
    }


    public List<String[]> list=new ArrayList<>();
    public Table table=new Table();
    public String tablename=null;
    String []attributes=null;
    String [] type=null;
    String [] nullable=null;
    String database=null;
    String s="?";
    int length=0;
    PreparedStatement ps=null;

    public User user= new User();
    public String filrName=null;
    public int uploadId=0;
    public int uploadNumber=0;
//    public void setList3(List<String[]> list3){
//        this.list3=list3;
//    }
//    public void run(){
//        insertData(list3,table3,uploadId3);
//    }
    Date date=null;
    SimpleDateFormat sf3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
    public void insertData(List<String[]> list){
        try {
            int size=list.size();

            for(int i = 0;i<size;i++){
                String []s1=list.get(i);
                int flag=0;
                for(int j=0;j<length;j++){
                    if(s1[j]!=null&&!s1[j].equals("")&&!s1[j].equals(" ")){
                        flag=1;
                        if(type[j].equals("VARCHAR2")){
                            ps.setString(j+1,s1[j]);
                        }else if(type[j].equals("NUMBER")){
                            BigDecimal bg=new BigDecimal(Double.parseDouble(s1[j]));
                            ps.setDouble(j+1,bg.setScale(8,BigDecimal.ROUND_HALF_UP).doubleValue());
                        }else if(type[j].equals("DATE")){
                            try {
                                date = sf3.parse(s1[j]);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //System.out.println(date.toString()+"   "+date.getTime());
                            ps.setDate(j+1,new java.sql.Date(date.getTime()));
                        }
                    }else if(nullable[j].equals("Y")){
                        ps.setString(j+1,"");
                    }
                }
                ps.setInt(length+1,uploadId);
                //积攒SQL


                if(flag==1){
                    ps.addBatch();
                    uploadNumber++;
                }
                //当积攒到一定程度,就执行一次,并且清空记录
                if((i+1) % 1000==0){
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }
            //总条数不是批量值整数倍,则还需要在执行一次
            if(length % 1000 != 0){
                ps.executeBatch();
                ps.clearBatch();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public String insertData2(List<String[]> list){
        try {





            connect.setAutoCommit(false);



            //Date date = new Date(new java.util.Date().getTime());



            int size=list.size();
            for(int i = 0;i<size;i++){




                String []s1=list.get(i);

                int flag=0;
                for(int j=0;j<length;j++){



                   // System.out.println(s1[j]);

                    if(s1[j]!=null&&!s1[j].equals("")&&!s1[j].equals(" ")){
                        flag=1;
                        if(type[j].equals("VARCHAR2")){
                            ps.setString(j+1,s1[j]);
                        }else if(type[j].equals("NUMBER")){
                            try {
                                String s2=s1[j].replace(",","");
                                s2=s2.replace("(","-");
                                s2=s2.replace(")","");
                                Double d= Double.parseDouble(s2);
                                BigDecimal bigDecimal=new BigDecimal(d);
                                double f1=bigDecimal.setScale(9,RoundingMode.HALF_UP).doubleValue();

//                                System.out.println(d+"   "+f1);

                                ps.setDouble(j+1,f1);




                            }catch (Exception e2){
                                int row=i+2;
                                connect.rollback();
                                return "第"+row+"行的"+attributes[j]+":"+s1[j]+"要求为数字";
                            }
//                            }

                        }else if(type[j].equals("DATE")){
                            SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                            Date date=null;
                            try {
                                //使用SimpleDateFormat的parse()方法生成Date
                                date = sf.parse(s1[j]);
                                //打印Date
//                                System.out.println(date);
                            } catch (ParseException e) {
                                //e.printStackTrace();
                            }
                            if(date==null){
                                SimpleDateFormat sf3 = new SimpleDateFormat("yyyy/MM/dd,HH:mm:ss");
                                try {
                                    date = sf3.parse(s1[j]);
                                } catch (ParseException e) {
//                                    int row=i+2;
//                                    return "第"+row+"行的"+attributes[j]+":"+s1[j]+"要求为日期型";
                                }
                            }
                            if(date==null){
                                SimpleDateFormat sf3 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                                try {
                                    date = sf3.parse(s1[j]);
                                } catch (ParseException e) {
//                                    int row=i+2;
//                                    return "第"+row+"行的"+attributes[j]+":"+s1[j]+"要求为日期型";
                                }
                            }

                            if(date==null){
                                SimpleDateFormat sf3 = new SimpleDateFormat("yyyy/MM/dd");
                                try {
                                    date = sf3.parse(s1[j]);
                                } catch (ParseException e) {
                                    int row=i+2;
                                    connect.rollback();
                                    e.printStackTrace();
                                    return "第"+row+"行的"+attributes[j]+":"+s1[j]+"要求为日期型";
                                }
                            }
                            //System.out.println(date.toString()+"   "+date.getTime());
                            ps.setDate(j+1,new java.sql.Date(date.getTime()));
                        }
                    }else if(nullable[j].equals("Y")){
                        ps.setString(j+1,null);
                    }else{
                        int row=i+2;
                        connect.rollback();
                        return "第"+row+"行的"+attributes[j]+"不能为空";
                    }

                }

                if(flag==0){
                    break;
                }
                ps.setInt(length+1,uploadId);


                //积攒SQL
                ps.addBatch();

                //当积攒到一定程度,就执行一次,并且清空记录
                if((i+1) % 1000==0){
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }
            //总条数不是批量值整数倍,则还需要在执行一次
            if(length % 1000 != 0){
                ps.executeBatch();
                ps.clearBatch();
            }
            //connect.commit();
//            this.close();
            return "success";

        }catch (SQLException e){
            try {
                connect.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            System.out.println();
            e.printStackTrace();
            return "提交数据库时出现未知错误";
        }
    }



    public void insertUpload( String uploadResult,java.sql.Date date){
        try {
            this.Connection(this.url,this.userName,this.password);
            PreparedStatement ps1 = connect.prepareStatement("insert into TTi_DB.MIS_UPL_UPLOAD_LOG values (?,?,?,?,?,?,?)");
            ps1.setString(1,filrName);
            ps1.setString(2,user.StaffNumber);
            //new java.sql.Date(new Date().getTime())
            ps1.setDate(3,date);
            ps1.setString(4,String.valueOf(table.tableId));
            ps1.setString(5,uploadResult);
            ps1.setString(6,"");
            ps1.setInt(7,uploadId);
            ps1.executeUpdate();
            this.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public void commit(){
        try {
            connect.commit();
            this.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rollback(){
        try {
            connect.rollback();
            this.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String insert2(List<String[]> list,Table table,User user,String fileName)  {


        try {
            String tablename=table.tablename;
            String []attributes=table.attributes.split(",");
            String [] type=table.type.split(",");
            String [] nullable=table.nullable.split(",");
            String database=table.database;


            this.Connection(this.url,this.userName,this.password);

            PreparedStatement pre = connect.prepareStatement("select  * from TTI_DB.MIS_UPL_DATABASE where alias=?");
            pre.setString(1,database);
            resultSet=pre.executeQuery();
            String url="";
            String userName="";
            String password="";
            while (resultSet.next()){
                url=resultSet.getString("url");
                userName=resultSet.getString("userName");
                password=JdbcTest2.decode(resultSet.getString("password"));
            }

            PreparedStatement ps1= connect.prepareStatement("SELECT Max(id) as \"id\" FROM MIS_DBG_NOTE");
            resultSet=ps1.executeQuery();
            int n=0;
            while (resultSet.next()){
                n=resultSet.getInt("id");
            }

            this.close();
            this.Connection(url,userName,password);
            connect.setAutoCommit(false);

            String s="?";

            int length=type.length-1;
            for(int i=0;i<length;i++){
                s+=",?";
            }

            int size=list.size();
            long beginTime = System.currentTimeMillis();
            PreparedStatement ps = connect.prepareStatement("insert into "+tablename+" values ("+s+")");
            //Date date = new Date(new java.util.Date().getTime());




            for(int i = 0;i<size;i++){

                String []s1=list.get(i);


                int flag=0;
                for(int j=0;j<length;j++){

//                    if(i==362){
//                        System.out.println(j+"  "+s1[j]);
//                    }
                    if(s1[j]!=null&&!s1[j].equals("")&&!s1[j].equals(" ")){
                        flag=1;
                        if(type[j].equals("VARCHAR2")){
                            ps.setString(j+1,s1[j]);
                        }else if(type[j].equals("NUMBER")){
//                            try {
//                                ps.setInt(j+1,Integer.parseInt(s1[j].split("\\.")[0].replace(",","")));
//                            }catch (Exception e){
//                                int row=i+2;
//                                System.out.println(s1[j]);
//                                return "第"+row+"行的"+attributes[j]+":"+s1[j]+"要求为整数型";
                                try {
                                    String s2=s1[j].replace(",","");
                                    s2=s2.replace("(","-");
                                    s2=s2.replace(")","");
                                    Double d= Double.parseDouble(s2);
                                    ps.setDouble(j+1,d);
                                }catch (Exception e2){
                                    int row=i+2;
                                    return "第"+row+"行的"+attributes[j]+":"+s1[j]+"要求为数字";
                                }
//                            }

                        }else if(type[j].equals("DATE")){

                            //System.out.println(s1[j]);

                            SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                            Date date=null;
                            try {
                                //使用SimpleDateFormat的parse()方法生成Date
                                date = sf.parse(s1[j]);
                                //打印Date
//                                System.out.println(date);
                            } catch (ParseException e) {
                                //e.printStackTrace();
                            }
//                            if(date==null){
//                                SimpleDateFormat sf2 = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
//                                try {
//                                    date = sf2.parse(s1[j]);
//                                } catch (ParseException e) {
//                                    //e.printStackTrace();
//                                }
//                                //打印Date
//                            }
                            if(date==null){
                                SimpleDateFormat sf3 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                                try {
                                    date = sf3.parse(s1[j]);
                                } catch (ParseException e) {
//                                    int row=i+2;
//                                    return "第"+row+"行的"+attributes[j]+":"+s1[j]+"要求为日期型";
                                }
                            }
                            if(date==null){
                                SimpleDateFormat sf3 = new SimpleDateFormat("yyyy/MM/dd,HH:mm:ss");
                                try {
                                    date = sf3.parse(s1[j]);
                                } catch (ParseException e) {
//                                    int row=i+2;
//                                    return "第"+row+"行的"+attributes[j]+":"+s1[j]+"要求为日期型";
                                }
                            }
                            if(date==null){
                                SimpleDateFormat sf3 = new SimpleDateFormat("yyyy/MM/dd");
                                try {
                                    date = sf3.parse(s1[j]);
                                } catch (ParseException e) {
                                    int row=i+2;
                                    return "第"+row+"行的"+attributes[j]+":"+s1[j]+"要求为日期型";
                                }
                            }
                           //System.out.println(date.toString()+"   "+date.getTime());
                            ps.setDate(j+1,new java.sql.Date(date.getTime()));
                        }
                    }else if(nullable[j].equals("Y")){
                        ps.setString(j+1,null);
                    }else{
                        int row=i+2;
                        return "第"+row+"行的"+attributes[j]+"不能为空";
                    }

                }

                if(flag==0){
                    break;
                }
                ps.setInt(length+1,n+1);


                //积攒SQL
                ps.addBatch();

                //当积攒到一定程度,就执行一次,并且清空记录
                if((i+1) % 300==0){
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }
            //总条数不是批量值整数倍,则还需要在执行一次
            if(length % 300 != 0){
                ps.executeBatch();
                ps.clearBatch();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Time : "+(endTime - beginTime));
            connect.commit();
            this.close();


            this.Connection(this.url,this.userName,this.password);


            ps1 = connect.prepareStatement("insert into MIS_DBG_NOTE values (?,?,?,?,?,?)");
            ps1.setInt(1,n+1);
            ps1.setString(2,fileName);
            ps1.setString(3,user.StaffNumber);

            SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);


            Date date=null;
            try {
                date=sf.parse(new Date().toString());
            }catch (ParseException e){
                e.printStackTrace();
            }

            ps1.setDate(4,new java.sql.Date(date.getTime()));




            ps1.setString(5,table.tablename);
            ps1.setString(6,table.database);
            ps1.executeUpdate();
            this.close();



            return "success "+"a;"+((n+1)*3-2);

        }catch (SQLException e){
            try {
                connect.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return "提交数据库时出现未知错误";
        }

    }



    public  String insert3(Table table)  {

        this.Connection(this.url,this.userName,this.password);
        try {
//            PreparedStatement preState = connect.prepareStatement("insert into MIS_DBG values (?,?,?)");
//            preState.setInt(1, 2);//1是指sql语句中第一个？,  2是指第一个？的values值
//            preState.setString(2, "hhh");
//            preState.setString(3, "xxx");
//            preState.executeUpdate();        //执行查询语句

            int n=0;
            PreparedStatement ps = connect.prepareStatement("select tti_db.MIS_upl_table_SEQ.Nextval as tableId from dual");
            resultSet=ps.executeQuery();
            while (resultSet.next()){
                n=resultSet.getInt("tableId");
            }

            ps = connect.prepareStatement("insert into TTI_DB.MIS_UPL_TABLE values (?,?,?,?,?,?,?,?,?)");
            //Date date = new Date(new java.util.Date().getTime());
            ps.setString(1,table.tablename);
            ps.setString(2,table.attributes);
            ps.setString(3,table.type);
            ps.setString(5,table.nullable);
            ps.setString(4,table.remark);
            ps.setString(6,table.database);
            ps.setString(7,table.length);
            ps.setString(8,table.ps);
            ps.setInt(9,n);
            ps.executeUpdate();
            this.close();
            return "success";
        }catch (SQLException e){
            e.printStackTrace();
            this.close();
            return "此上传功能名称已存在，不允许重复创建！";
        }
    }
    public  Table selecttable(String tablename,Database database1){
        Table table=new Table();
        table.tablename=tablename;
        table.database=database1.getAlias();
        table.attributes="";
        table.type="";
        table.nullable="";
        table.length="";

        try{
            this.Connection(database1.getUrl(),database1.getUserName(),database1.getPassword());

            PreparedStatement ps = connect.prepareStatement("SELECT table_name, column_name, data_type,nullable,data_length\n" +
                    "  FROM all_tab_cols\n" +
                    " WHERE table_name = ? order by column_id");
            ps.setString(1,tablename);
            resultSet = ps.executeQuery();


            while (resultSet.next()){
                table.attributes=table.attributes+resultSet.getString("column_name")+",";
                table.type=table.type+resultSet.getString("data_type")+",";
                table.nullable=table.nullable+resultSet.getString("nullable")+",";
                table.length=table.length+resultSet.getString("data_length")+",";
            }
            this.close();
            if(!table.attributes.equals("")){
                table.attributes=table.attributes.substring(0,table.attributes.length()-1);
                table.type=table.type.substring(0,table.type.length()-1);
                table.nullable=table.nullable.substring(0,table.nullable.length()-1);
                table.length=table.length.substring(0,table.length.length()-1);
                try {
                    this.Connection(this.url,this.userName,this.password);
                    PreparedStatement ps2 = connect.prepareStatement("SELECT remark,ps,tableId\n" +
                            "  FROM TTI_DB.MIS_UPL_TABLE\n" +
                            " WHERE tablename = ? and database=?");
                    ps2.setString(1,tablename);
                    ps2.setString(2,database1.getAlias());
                    resultSet=ps2.executeQuery();
                    if(resultSet==null){
                        table.remark=null;
                        table.ps=null;
                    }else {
                        while (resultSet.next()){
                            table.remark=resultSet.getString("remark");
                            table.ps=resultSet.getString("ps");
                            table.tableId=resultSet.getInt("tableId");
                        }
                    }
                    this.close();
                }catch (SQLException e2){
                    e2.printStackTrace();
                }

                return  table;
            }
            else {
                return null;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public  List<Table> selectFuncionTable(Database database){
        this.Connection(this.url,this.userName,this.password);

        List<Table> tables=new ArrayList<Table>();
        try {
            PreparedStatement ps=connect.prepareStatement("select * from TTI_DB.MIS_UPL_TABLE where database=?");
            ps.setString(1,database.getAlias());
            resultSet=ps.executeQuery();
            while (resultSet.next()){
                Table table=new Table(resultSet.getString("tablename"),resultSet.getString("attributes"),
                        resultSet.getString("type"),resultSet.getString("nullable"),
                        resultSet.getString("remark"), resultSet.getString("length"),resultSet.getString("ps"),
                        resultSet.getString("database"),resultSet.getInt("tableId"));
                tables.add(table);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        this.close();
        return tables;
    }


    public  String update (Table table){
        this.Connection(this.url,this.userName,this.password);
        try {
            PreparedStatement ps = connect.prepareStatement("UPDATE TTI_DB.MIS_UPL_TABLE SET remark=?,ps=?,attributes=?,type=?,length=?,nullable=? where tableId=?");
            ps.setString(1,table.remark);
            ps.setString(2,table.ps);
            ps.setString(3,table.attributes);
            ps.setString(4,table.type);
            ps.setString(5,table.length);
            ps.setString(6,table.nullable);
            ps.setInt(7,table.tableId);
            resultSet = ps.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
            return "此上传功能名称已存在，不允许重复创建！";
        }
        this.close();
        return "success";
    }

    public  void delete(Table table){
        this.Connection(this.url,this.userName,this.password);
        try{
            PreparedStatement ps = connect.prepareStatement("delete  from TTI_DB.MIS_UPL_TABLE where tableId=?");
            ps.setInt(1,table.tableId);

            ps.executeUpdate();


            ps=connect.prepareStatement("select * from TTI_DB.MIS_UPL_USER");
            resultSet=ps.executeQuery();
            while (resultSet.next()){
                String staffNumber=resultSet.getString("staffNumber");
                if(resultSet.getString("role")==null|| resultSet.getString("role").equals("admin")){
                    continue;
                }



                String[] roles=resultSet.getString("role").split(";");
                String role="";

                for(int i=0;i<roles.length;i++){
                    if(roles[i].equals("")){
                        continue;
                    }
//                    String [] r=roles[i].split(",");
                    if(!roles[i].equals(String.valueOf(table.tableId))){
                        role=role+roles[i]+";";// alias  table remark
                    }
                }
                PreparedStatement ps2=connect.prepareStatement("update TTI_DB.MIS_UPL_USER set role=? where staffNumber=?");
                ps2.setString(1,role);
                ps2.setString(2,staffNumber);
                ps2.executeUpdate();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        this.close();
    }


    public  String insertUser(User user){
        this.Connection(this.url,this.userName,this.password);
        try{
            PreparedStatement ps = connect.prepareStatement("insert into TTI_DB.MIS_UPL_USER(staffnumber,name,role,password) values (?,?,?,?)");
            ps.setString(1,user.StaffNumber);
            ps.setString(2,user.Name);
            ps.setString(3,user.Role);
            ps.setString(4,JdbcTest2.encode(user.Password));
            ps.executeUpdate();
            this.close();
            return "success";
        }catch (SQLException e){
            e.printStackTrace();
            this.close();
            return "此工号已存在";
        }
    }

    public  List<User> selectUser(User user){
        this.Connection(this.url,this.userName,this.password);

        List<User> users = new ArrayList<User>();
        try {
            PreparedStatement ps = connect.prepareStatement("select * from TTI_DB.MIS_UPL_USER where staffNumber=?");

            if(user.StaffNumber.equals("")||user.StaffNumber==null){
                ps = connect.prepareStatement("select * from TTI_DB.MIS_UPL_USER ");
                resultSet=ps.executeQuery();
                while (resultSet.next()){
                    User user1=new User();
                    user1.StaffNumber=resultSet.getString("staffNumber");
                    user1.Name=resultSet.getString("name");
                    user1.Role=resultSet.getString("role");
                    user1.Password=JdbcTest2.decode(resultSet.getString("password"));
                    user1.createDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(resultSet.getDate("createDate").getTime()));
                    user1.updateDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(resultSet.getDate("updateDate").getTime()));

                    if(user1.Role!=null&&user1.Role.equals("admin")){
                        continue;
                    }
                    users.add(user1);

                }
            }else {
                ps.setString(1,user.StaffNumber);
                resultSet=ps.executeQuery();
                while (resultSet.next()){
                    User user1=new User();
                    user1.StaffNumber=resultSet.getString("staffNumber");
                    user1.Name=resultSet.getString("name");
                    user1.Role=resultSet.getString("role");
                    user1.Password=JdbcTest2.decode(resultSet.getString("password"));
                    user1.createDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(resultSet.getDate("createDate").getTime()));
                    user1.updateDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(resultSet.getDate("updateDate").getTime()));
                    users.add(user1);

                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        this.close();
        return users;
    }



    public  User selectLoginUser(User user){
        this.Connection(this.url,this.userName,this.password);
        User user1 = new User();
        try {
            System.out.println(user.StaffNumber+JdbcTest2.encode(user.Password));
            PreparedStatement ps = connect.prepareStatement("select * from TTI_DB.MIS_UPL_USER where staffNumber=? and password=?");
            ps.setString(1,user.StaffNumber);
            ps.setString(2,JdbcTest2.encode(user.Password));
            resultSet=ps.executeQuery();

            while (resultSet.next()){
                user1.StaffNumber=resultSet.getString("staffNumber");
                user1.Name=resultSet.getString("name");
                user1.Role=resultSet.getString("role");
                System.out.println(user1.StaffNumber+user1.Role);
                user1.Password=JdbcTest2.decode(resultSet.getString("password"));
                user1.createDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(resultSet.getDate("createDate").getTime()));

                user1.updateDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(resultSet.getDate("updateDate").getTime()));

            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        if(user1.StaffNumber!=null){
            this.close();
            return user1;
        }else {
            this.close();
            return null;
        }

    }

    public  String updateUser(User user){
        this.Connection(this.url,this.userName,this.password);
        try{
            PreparedStatement ps = connect.prepareStatement("update TTI_DB.MIS_UPL_USER set name=?,role=?,password=?,updateDate=? where staffnumber=?");
            ps.setString(5,user.StaffNumber);
            ps.setString(1,user.Name);
            ps.setString(2,user.Role);
            ps.setString(3,JdbcTest2.encode(user.Password));
            SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            Date date=null;
            try {
                date=sf.parse(new Date().toString());
            }catch (ParseException e){
                e.printStackTrace();
            }
            ps.setDate(4,new java.sql.Date(date.getTime()));
            ps.executeUpdate();
            this.close();
            return "success";
        }catch (SQLException e){
            e.printStackTrace();
            this.close();
            return "error";
        }
    }


    public  String deleteUser(User user){
        this.Connection(this.url,this.userName,this.password);
        try{
            PreparedStatement ps = connect.prepareStatement("delete  from TTI_DB.MIS_UPL_USER s where staffnumber=?");
            ps.setString(1,user.StaffNumber);
            ps.executeUpdate();
            this.close();
            return "success";
        }catch (SQLException e){
            e.printStackTrace();
            this.close();
            return "error";
        }
    }


    public  String selectRole(){
        this.Connection(this.url,this.userName,this.password);
        try {
            PreparedStatement ps2 = connect.prepareStatement("select tablename from TTI_DB.MIS_UPL_TABLE ");
            resultSet=ps2.executeQuery();
            String role="";
            while (resultSet.next()){
                role=role+resultSet.getString("tablename")+",";
            }
            int j=role.lastIndexOf(",");
            if(j!=-1){
                role=role.substring(0,j);
            }
            this.close();
            return role;
        }catch (SQLException e){
            e.printStackTrace();
            this.close();
            return null;
        }
    }

    //返回已连接数据库
    public  ArrayList<Database> selectDatabase(){
        this.Connection(this.url,this.userName,this.password);
        ArrayList<Database> arrayList=new ArrayList<Database>();
        try {
            PreparedStatement ps = connect.prepareStatement("select * from TTI_DB.MIS_UPL_DATABASE ");
            resultSet=ps.executeQuery();

            while (resultSet.next()){
                Database database=new Database();
                database.setUrl(resultSet.getString("url"));
                database.setUserName(resultSet.getString("userName"));
                database.setPassword(JdbcTest2.decode(resultSet.getString("password")));
                database.setAlias(resultSet.getString("alias"));
                database.setStatus(resultSet.getString("status"));
                arrayList.add(database);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        this.close();
        return arrayList;
    }


    public  String insertDatabase(Database database){

        if(!this.Connection(database.getUrl(),database.getUserName(),database.getPassword())){
            this.close();
            return "连接信息错误";
        }else {
            this.close();
            try {
                this.Connection(this.url,this.userName,this.password);
                PreparedStatement ps = connect.prepareStatement("select  count(*) as n from TTI_DB.MIS_UPL_DATABASE where url=? and username =?");
                ps.setString(1,database.getUrl());
                ps.setString(2,database.getUserName());
                resultSet=ps.executeQuery();
                while (resultSet.next()){
                    if(resultSet.getInt("n")!=0){
                        return "此连接已存在";
                    }
                }
                ps = connect.prepareStatement("insert into TTI_DB.MIS_UPL_DATABASE values (?,?,?,?,?)");
                ps.setString(1,database.getUrl());
                ps.setString(2,database.getUserName());
                ps.setString(3,JdbcTest2.encode(database.getPassword()));
                ps.setString(4,database.getAlias());
                ps.setString(5,"yes");
                ps.executeUpdate();
                this.close();
                return "连接成功";
            }catch (SQLException e){
                e.printStackTrace();
                return "此数据库别名已存在，请勿重复";
            }
        }
    }


    public  String updateDatabasePassword(Database database){

        if(!this.Connection(database.getUrl(),database.getUserName(),database.getPassword())){
            this.close();
            return "密码错误";
        }else {
            this.close();
            try {
                this.Connection(this.url,this.userName,this.password);
                PreparedStatement ps = connect.prepareStatement("update  TTI_DB.MIS_UPL_DATABASE set password=? where alias=?");
                ps.setString(1,JdbcTest2.encode(database.getPassword()));
                ps.setString(2,database.getAlias());
                ps.executeUpdate();
                this.close();
                return "success";
            }catch (SQLException e){
                e.printStackTrace();
                return "error";
            }
        }
    }

    public  String deleteDatabase(Database database){
        this.Connection(this.url,this.userName,this.password);
        try {
            PreparedStatement ps = connect.prepareStatement("delete from TTI_DB.MIS_UPL_DATABASE where alias=?");
            ps.setString(1,database.getAlias());
            ps.executeUpdate();


            List<String> tableIds=new ArrayList<String>();
            ps=connect.prepareStatement("select  tableId from TTI_DB.MIS_UPL_TABLE where database=?");
            ps.setString(1,database.getAlias());
            resultSet=ps.executeQuery();
            while (resultSet.next()){
                tableIds.add(resultSet.getString("tableId"));
            }


            ps=connect.prepareStatement("delete from MIS_DBG_Table where database=?");
            ps.setString(1,database.getAlias());
            ps.executeUpdate();

            ps=connect.prepareStatement("select * from TTI_DB.MIS_UPL_USER");
            resultSet=ps.executeQuery();
            while (resultSet.next()){
                String staffNumber=resultSet.getString("staffNumber");
                if(resultSet.getString("role")==null|| resultSet.getString("role").equals("admin")){
                    continue;
                }
                String[] roles=resultSet.getString("role").split(";");
                String role="";
                for(int i=0;i<roles.length;i++){
                    if(!tableIds.contains(roles[i])){
                        role=role+roles[i]+";";// alias  table
                    }
                }
                PreparedStatement ps2=connect.prepareStatement("update TTI_DB.MIS_UPL_USER set role=? where staffNumber=?");
                ps2.setString(1,role);
                ps2.setString(2,staffNumber);
                ps2.executeUpdate();
            }
            this.close();
            return "删除成功";
        }catch (SQLException e){
            e.printStackTrace();
            this.close();
            return "删除失败";
        }
    }

    public  String updateDatabase(Database database){
        this.Connection(this.url,this.userName,this.password);
        try {
            PreparedStatement ps = connect.prepareStatement("update  TTI_DB.MIS_UPL_DATABASE set status = ? where alias=?");
            ps.setString(1,database.getStatus());
            ps.setString(2,database.getAlias());
            ps.executeUpdate();
            this.close();
            return "修改成功";
        }catch (SQLException e){
            e.printStackTrace();
            this.close();
            return "修改失败";
        }

    }


    public  void close() {
        try {
            if (resultSet!=null) resultSet.close();
            if (statement!=null) statement.close();
            if (connect!=null) connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static final String key0 = "FECOI()*&<MNCXZPKL";
    private static final Charset charset = Charset.forName("UTF-8");
    private static byte[] keyBytes = key0.getBytes(charset);

    public static String encode(String enc){
        byte[] b = enc.getBytes(charset);
        for(int i=0,size=b.length;i<size;i++){
            for(byte keyBytes0:keyBytes){
                b[i] = (byte) (b[i]^keyBytes0);
            }
        }
        return new String(b);
    }

    public static String decode(String dec){
        byte[] e = dec.getBytes(charset);
        byte[] dee = e;
        for(int i=0,size=e.length;i<size;i++){
            for(byte keyBytes0:keyBytes){
                e[i] = (byte) (dee[i]^keyBytes0);
            }
        }
        return new String(e);
    }



    public int pageCount =10;//定义分页大小
    public int count=0;//上传总条数
    public int allPage=0;//总页数
    public int page=0;//当前页数

    public List<String> title=new ArrayList<String>();//表格标题

    public List<List<String>> uploadCount(int uploadId){
        this.Connection(url,userName,password);
        try{
            ps = connect.prepareStatement(" select database,tablename from TTI_DB.MIS_UPL_TABLE where TABLEID =(select UPLOAD_TABLE_ID from  TTi_DB.MIS_UPL_UPLOAD_LOG where UPLOAD_ID=?)");
            ps.setInt(1,uploadId);
            resultSet=ps.executeQuery();
            while (resultSet.next()){
                database=resultSet.getString("database");
                tablename=resultSet.getString("tablename");
            }
            ps = connect.prepareStatement("select url,username,password from TTI_DB.MIS_UPL_DATABASE " +
                    "where alias=?");
            ps.setString(1,database);
            resultSet=ps.executeQuery();
            while (resultSet.next()){
                url=resultSet.getString("url");
                userName=resultSet.getString("username");
                password=JdbcTest2.decode(resultSet.getString("password"));
            }
            this.close();
            this.Connection(url,userName,password);
            ps=connect.prepareStatement("select  count (*) as count from "+tablename +" where uploadId=?");
            ps.setInt(1,uploadId);
            resultSet=ps.executeQuery();
            while (resultSet.next()){
                count=resultSet.getInt("count");
            }
            if(count%pageCount==0){
                allPage=count/pageCount;
            }else {
                allPage=count/pageCount+1;
            }
            ps=connect.prepareStatement("select column_name from all_tab_cols where table_name =? order  by column_id");
            ps.setString(1,tablename);
            resultSet=ps.executeQuery();

            while (resultSet.next()){
//                System.out.println(resultSet.getString("column_name"));
                title.add(resultSet.getString("column_name").replace("_"," "));
            }
            return showUploadResult(uploadId,1,url,userName,password,title,tablename);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public  List<List<String>> showUploadResult(int uploadId,int page,String url,String userName,String password,List<String> title,String tableName){
        this.Connection(url,userName,password);
        List<List<String>> list=new ArrayList<List<String>>();
        try {
            ps=connect.prepareStatement("select * from (\n" +
                    "SELECT X.*,ROWNUM RNUM   FROM "+tableName+" X WHERE UPLOADID =?\n " +
                    ")WHERE RNUM>=? AND RNUM<=?");

            //ps.setString(1,tablename);
            ps.setInt(1,uploadId);
            ps.setInt(2,pageCount*(page-1)+1);
            ps.setInt(3,pageCount*page);
            resultSet=ps.executeQuery();
            int list1Size=title.size()-1;
            while (resultSet.next()){
                List<String> list2=new ArrayList<String>();
                for(int i=0;i<list1Size;i++){
                    list2.add(resultSet.getString(title.get(i).replace(" ","_")));
                }
                list.add(list2);
            }
            this.close();

        }catch (SQLException e){
            e.printStackTrace();
            list.clear();
            return list;
        }
        return list;
    }



    public  Integer recallUpload(int uploadId){
        int n=0;
        this.Connection(this.url,this.userName,this.password);
        String database="";
        String tablename="";
        String url="";
        String username="";
        String password="";
        try {
            PreparedStatement ps = connect.prepareStatement("select upload_table_id from  TTi_DB.MIS_UPL_UPLOAD_LOG where upload_id=?");
            ps.setInt(1,uploadId);
            resultSet=ps.executeQuery();
            String upload_table_id="";
            while (resultSet.next()){
                upload_table_id=resultSet.getString("upload_table_id");
            }
            ps = connect.prepareStatement("select database,tablename from  TTI_DB.MIS_UPL_TABLE where tableid=?");
            ps.setString(1,upload_table_id);
            resultSet=ps.executeQuery();
            while (resultSet.next()){
                database=resultSet.getString("database");
                tablename=resultSet.getString("tablename");
            }
            ps=connect.prepareStatement("update  TTi_DB.MIS_UPL_UPLOAD_LOG set upload_is_recall=? where  upload_id =?");
            ps.setString(1,"yes");
            ps.setInt(2,uploadId);
            ps.executeUpdate();
            ps=connect.prepareStatement("select  url,username,password from  TTI_DB.MIS_UPL_DATABASE where alias=?");
            ps.setString(1,database);
            resultSet=ps.executeQuery();
            while (resultSet.next()){
                url=resultSet.getString("url");
                username=resultSet.getString("username");
                password=JdbcTest2.decode(resultSet.getString("password"));
            }
            this.close();
            this.Connection(url,username,password);
            ps=connect.prepareStatement("delete from "+tablename+" where uploadId=?");
            ps.setInt(1,uploadId);
            n=ps.executeUpdate();
            this.close();

        }catch (SQLException e){
            e.printStackTrace();
            return n;
        }
        return n;
    }


    public  Timestamp StrTransSqlDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDate;
        Timestamp transdate = null;
        try {
            nowDate = simpleDateFormat.parse(date);


            transdate = new Timestamp(nowDate.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return transdate;
    }




    public static void main(String[] args) {
        JdbcTest2 jdbcTest2=new JdbcTest2();
        User user=new User();
        user.Name="dsa";
        System.out.println(jdbcTest2.selectTable(user));
        jdbcTest2.close();
//        Table table = new Table();
//        table.tablename="MIS_DBG";
//        table.remark="hhhhh";
//        JdbcTest2.Connection("10.34.0.131:1528/UATA","apps","uat1289669");
//        //JdbcTest2.update(table);
//        System.out.println(JdbcTest2.selecttable("MIS_DBG"));
//        JdbcTest2.close();
//        User user=new User();
//        user.StaffNumber="432";
//
//        user.Password="4325";
//        JdbcTest2.Connection("10.34.0.131:1528/UATA","apps","uat1289669");
//        System.out.println(JdbcTest2.selectUser(user).get(0).createDate);
//        System.out.println(JdbcTest2.recallUpload("54"));
    }
}
