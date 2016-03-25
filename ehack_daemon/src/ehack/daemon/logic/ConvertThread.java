package ehack.daemon.logic;

import java.sql.*;
import org.apache.log4j.Logger;


import ehack.daemon.pool.ConnectionPool;

public class ConvertThread implements Runnable {
	private static Logger logger = Logger.getLogger(ConvertThread.class.getName());
	
	/* Connection Pool 구현 전까지 static으로한다. */
	private static Connection conn = null;
	
	private static ConnectionPool Connection;

	
	public ConvertThread(ConnectionPool connection) {
		Connection = connection;
		logger.debug(Connection.toString());
	}


	@Override
	public void run() {
		logger.info(this.getClass().getName() + "를 시작합니다.");

		while(true) {

			
			//로직부분
			try {
				//Something to do

			} catch(Exception e) {
				logger.error("오류가 발생하였습니다. ::", e);
	        } finally {
				try { Connection.returnConn(conn);} catch (Exception e) {}
	            
	        }
			
			//시간 대기
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				logger.error("오류가 발생하였습니다. ::", e);
			}

		}		
	}

}
