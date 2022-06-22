package ariadna.services.populate;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

public class SpringSQLPopulateService extends AbstractSQLPopulateService {
	
	@Autowired
	DataSource dataSource;
	
	@Override
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

}
