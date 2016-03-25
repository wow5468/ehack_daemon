package ehack.daemon.pool;

import java.sql.*;
import java.util.*;

import org.apache.log4j.Logger;

public class ConnectionPool {
	private static Logger logger = Logger.getLogger(ConnectionPool.class.getName());

	private Hashtable<Connection, Boolean> ht = new Hashtable<Connection, Boolean>();
	private static String jdbcDriver = "org.postgresql.Driver";
	private static String jdbcUrl = "jdbc:postgresql://dororocinstance.c6efxm6vekfm.ap-northeast-1.rds.amazonaws.com:5432/jung2";
	private static String jdbcId = "jung2";
	private static String jdbcPw = "jung2qwer1234";
	private static int increment = 3;
	private static int initCreateCnt = 5;
	
	public ConnectionPool() throws SQLException, ClassNotFoundException {
		logger.info("ConnectionPool 초기화");

		Class.forName(jdbcDriver);
		for(int i=0; i<initCreateCnt; i++) {
			Connection conn = DriverManager.getConnection(jdbcUrl, jdbcId, jdbcPw);
			ht.put(conn, Boolean.FALSE);
			logger.info("i::"+i+", conn::"+conn.toString());
		}
	}
	
	public Connection getConnection() {
		Connection conn = null;
		
		Enumeration<Connection> enKeys = ht.keys();
		while(enKeys.hasMoreElements()) {
			conn = enKeys.nextElement();
			if(!ht.get(conn).booleanValue()) {
				ht.put(conn, Boolean.TRUE);
				return conn;
			}
		}
		
		try {
			incrementConn();
		} catch(SQLException e) {
			logger.error("오류 발생::", e);
		} catch(ClassNotFoundException e) {
			logger.error("오류 발생::", e);
		}
		
		return getConnection();
		
	}
	
	public void returnConn(Connection conn) {
		Enumeration<Connection> enKeys = ht.keys();
		while(enKeys.hasMoreElements()) {
			Connection findConn = enKeys.nextElement();
			if(findConn == conn) {
				ht.put(conn, Boolean.FALSE);
				//logger.debug("returned::"+conn.toString());
				break;
			}
		}
		
		try {
			removeConn();
		} catch(SQLException e) {
			logger.error("오류발생::", e);
		}
	}
	
	public void removeAll() throws SQLException {
		Enumeration<Connection> enKeys = ht.keys();
		while(enKeys.hasMoreElements()) {
			Connection conn = enKeys.nextElement();
			ht.remove(conn);
			conn.close();
		}
		logger.info(ht.toString());
	}
	
	private void incrementConn() throws SQLException, ClassNotFoundException {
		Class.forName(jdbcDriver);
		for(int i=0; i<increment; i++) {
			Connection conn = DriverManager.getConnection(jdbcUrl, jdbcId, jdbcPw);
			ht.put(conn, Boolean.FALSE);
			logger.info("i::"+i+", conn::"+conn.toString());
		}
	}
	
	private void removeConn() throws SQLException {
		int cnt = 0;
		
		Enumeration<Connection> enKeys = ht.keys();
		while(enKeys.hasMoreElements()) {
			Connection conn = enKeys.nextElement();
			if(ht.get(conn) == false) {
				cnt++;
				if(cnt>5) {
					logger.info("remove::"+conn.toString());
					ht.remove(conn);
					conn.close();
					
				}
			}
		}
	}
}
