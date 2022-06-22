package ariadna.services.populate;

import java.sql.Connection;
import java.sql.SQLException;

import ariadna.services.util.RawConnectionProvider;

public class RawSQLPopulateService extends AbstractSQLPopulateService {
	
	@Override
	public Connection getConnection() throws SQLException {
		return RawConnectionProvider.getConnection();
	}

}
