package ariadna.services.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbcp.DelegatingConnection;

public class RawConnectionProvider {

	static Connection conn;
	
	static {
		try { // copied from application.properties
			conn = DriverManager.getConnection("jdbc:h2:file:./data/demo", "sa", "password");
		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to the database", e);
		}
	}
	
	private RawConnectionProvider() {}
	
	public static Connection getConnection() throws SQLException {
		return new DelegatingConnection(conn) {
			@Override
			public void close() {
				// Do nothing (to reuse the connection later)
			}
		};
	}
}
