package ehack.daemon.server;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ehack.daemon.logic.ConvertThread;
import ehack.daemon.pool.ConnectionPool;

public class ServerMain {
	private static Logger logger = Logger.getLogger(ServerMain.class.getName());
	private static ConnectionPool Connection;
	
	public static void main(String[] args)  {
		logger.info("########## Jung2 Daemon이 시작됩니다. ##########");
		
		try {
			Connection = new ConnectionPool();
			logger.debug(Connection.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ConvertThread ct = new ConvertThread(Connection);
		
		Thread t1 = new Thread(ct);
		
		t1.start();
	}

}
