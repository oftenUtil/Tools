package main.java.com.jbutton.commons.hive;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 连接Hive并查询数据
 * 支持Kerberos
 *
 * @author lee
 */
public class HiveTest {
    public static void main(String[] args) {
        String jdbcUrl = args[0];
        String userName = args[1];
        String passWord = args[2];
        passWord = null;
        String tableName = args[3];
        String auth = args[4];
        String path = args[5];
        String kerberosName = args[6];
        Connection conn = null;
        /** 使用Hadoop安全登录 **/
        if (StringUtils.equalsIgnoreCase(auth, "yes")) {
            org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
            conf.set("hadoop.security.authentication", "Kerberos");
            System.setProperty("java.security.krb5.conf", "/etc/krb5.conf");
            if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
                // 默认：这里不设置的话，win默认会到 C盘下读取krb5.init
                System.setProperty("java.security.krb5.conf", "C:/Windows/krbconf/bms/krb5.ini");
            }
            try {
                UserGroupInformation.setConfiguration(conf);
                //UserGroupInformation.loginUserFromKeytab("hive/node021.talaxy.lw@TALAXY.LW", path);
                UserGroupInformation.loginUserFromKeytab(kerberosName, path);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            //conn = DriverManager.getConnection(jdbcUrl,userName,passWord);
            conn = DriverManager.getConnection(jdbcUrl);
            System.out.println("jdbcUrl为:" + jdbcUrl);
            System.out.println("获取连接:" + conn);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        String sql = "select count(*) from " + tableName;
        System.out.println("sql:" + sql);
        ResultSet res = null;
        try {
            Statement statement = conn.createStatement();
            res = statement.executeQuery(sql);
            System.out.println("执行" + sql + "运行结果:");
            while (res.next()) {
                System.out.println(res.getString(1));
            }
            queryData(statement, tableName, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean queryData(Statement statement, String tableName, ResultSet res) throws Exception {
        String sql = "SELECT * FROM " + tableName + " LIMIT 5";
        System.out.println("Running:" + sql);
        try {
            res = statement.executeQuery(sql);
            System.out.println("执行“+sql+运行结果:");
            while (res.next()) {
                System.out.println(res.getString(1) + "," + res.getString(2) + "," + res.getString(3));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
