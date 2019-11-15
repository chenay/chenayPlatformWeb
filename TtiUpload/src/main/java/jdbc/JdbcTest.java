package jdbc;

import oracle.jdbc.driver.OracleDriver;
import java.sql.*;

/**
 * Created by 10412 on 2016/12/27.
 * JDBC的六大步骤
 * JAVA连接Oracle的三种方式
 */
public class JdbcTest
{
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;


    public void Connection(){
        try {
            //第一步：注册驱动
            //第一种方式：类加载(常用)
            //Class.forName("oracle.jdbc.OracleDriver");

            //第二种方式：利用Driver对象
            Driver driver = new OracleDriver();
            DriverManager.deregisterDriver(driver);


            //第二步：获取连接
            //第一种方式：利用DriverManager（常用）
            connect = DriverManager.getConnection("jdbc:oracle:thin:@//10.34.0.131:1528/UATA", "apps", "ucweb9638a");

            //第二种方式：直接使用Driver
//            Properties pro = new Properties();
//            pro.put("user", "apps ");//你的oracle数据库用户名
//            pro.put("password", "ucweb9638a");//用户名密码
//            connect = driver.connect("jdbc:oracle:thin:@ 10.34.0.131:1528:UATA", pro);

            //测试connect正确与否
            System.out.println(connect);}
            catch (SQLException e1) {
            e1.printStackTrace();
        }

    }

    public void close() {
        try {
            if (resultSet!=null) resultSet.close();
            if (statement!=null) statement.close();
            if (connect!=null) connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
