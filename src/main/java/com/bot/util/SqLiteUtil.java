/**
 * CREATE TABLE test_table(
 *		   column1 INTEGER  PRIMARY KEY,
 *		   column2 VARCHAR(255),
 *		   column3 DATETIME
 *		);
 */
package com.bot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class SqLiteUtil {

	
	private static Connection c;
	
	private static Logger logger = LoggerFactory.getLogger(SqLiteUtil.class);
	
	private static String url;
	static{
		url = Paths.get("./","bot.db").toAbsolutePath().toString();
	}

	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		SqLiteUtil.url = url;
	}

	/**
	 * 
	  * 打开数据库连接
	  *@return 
	  *@date 2016年12月15日 上午9:37:46
	  *@author zxn
	 */
	private synchronized static Connection getConnection(){
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:"+url);
	    } catch ( Exception e ) {
			logger.error( e.getClass().getName() + ": " + e.getMessage(),e );
	      	System.exit(0);
	    }
	   return c;
	}
	
	/**
	 * 
	  * 关闭数据库连接
	  *@date 2016年12月15日 上午9:38:15
	  *@author zxn
	 */
	private synchronized static  void closeConnection(){
		try {
			c.close();
		} catch (SQLException e) {
			logger.error("error:",e);
		}
	}
	
	/**
	 * 
	  * 执行查询返回map结果
	  *@param sql 要执行的sql语句，预处理型
	  *@param param 预处理中？的值
	  *@return 
	  *@date 2016年12月15日 上午9:38:35
	  *@author zxn
	 */
	public synchronized static List<Map<String, String>>  getRowValue(String sql,List<String> param){
		getConnection();
		printSQL(sql, param);
		List<Map<String, String>> list=new LinkedList<>();
		try {
			PreparedStatement pst=c.prepareStatement(sql);
			for(int i=0;i<param.size();i++){
				pst.setString(i+1, param.get(i));
			}
			ResultSet rs=pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
//		getColumnCount(); 返回 ResultSet 中的列数。 
//		getColumnName(int); 返回列序号为 int 的列名。 
			while(rs.next()){
				Map<String, String> map=new LinkedHashMap<>();
				for(int i=0;i<rsmd.getColumnCount();i++){
					map.put(rsmd.getColumnName(i+1), rs.getString(i+1));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			logger.error("error:",e);
		}finally {
			closeConnection();
		}
		return list;
	}
	public synchronized static List<Map<String, String>>  getRowValue(String sql){
		getConnection();
		printSQL(sql);
		List<Map<String, String>> list=new LinkedList<>();
		try {
			PreparedStatement pst=c.prepareStatement(sql);
			ResultSet rs=pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
//		getColumnCount(); 返回 ResultSet 中的列数。 
//		getColumnName(int); 返回列序号为 int 的列名。 
			while(rs.next()){
				Map<String, String> map=new LinkedHashMap<>();
				for(int i=0;i<rsmd.getColumnCount();i++){
					map.put(rsmd.getColumnName(i+1), rs.getString(i+1));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			logger.error("error:",e);
		}finally {
			closeConnection();
		}
		return list;
	}
	/**
	 * 
	  * 执行新增或修改语句
	  *@param sql 要执行的sql语句，预处理型
	  *@param param 预处理中？的值
	  *@return 
	  *@date 2016年12月15日 上午9:40:03
	  *@author zxn
	 */
	public synchronized static Integer updateValue(String sql,List<String> param){
		getConnection();
		printSQL(sql, param);
		Integer num=0;
		try {
			PreparedStatement pst=c.prepareStatement(sql);
			for(int i=0;i<param.size();i++){
				pst.setString(i+1, param.get(i));
			}
			num = pst.executeUpdate();
		} catch (SQLException e) {
			logger.error("error:",e);
		}finally {
			closeConnection();
		}
		return num;
	}
	
	/**
	 * 
	  * 执行建表语句
	  *@param sql 
	  *@date 2016年12月15日 上午9:40:48
	  *@author zxn
	 */
	public synchronized static Integer createTable(String sql){
		getConnection();
		printSQL(sql);
		Integer num=0;
		try {
			Statement st=c.createStatement();
			num=st.executeUpdate(sql);
		} catch (SQLException e) {
			logger.error("error:",e);
		}finally {
			closeConnection();
		}
		return num;
	}
	
	private static void printSQL(String sql,List<String> param){
		for(int i=0;i<param.size();i++){
			sql=sql.replaceFirst("\\?", param.get(i));
		}
		logger.debug(sql);
	}
	private static void printSQL(String sql){
		logger.debug(sql);
	}
}
