package ariadna.services.event;

import java.sql.Connection;
import java.sql.SQLException;

import ariadna.services.util.RawConnectionProvider;

public class RawSQLEventsService extends AbstractSQLEventsService {

	@Override
	public Connection getConnection() throws SQLException {
		return RawConnectionProvider.getConnection();
	}
	

}
