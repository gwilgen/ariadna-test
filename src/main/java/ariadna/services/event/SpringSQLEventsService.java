package ariadna.services.event;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

public class SpringSQLEventsService extends AbstractSQLEventsService {
	
	@Autowired
	DataSource dataSource;

	@Override
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
}
