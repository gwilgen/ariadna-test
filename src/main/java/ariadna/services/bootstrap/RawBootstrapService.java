package ariadna.services.bootstrap;

import java.sql.Connection;
import java.sql.SQLException;

import ariadna.services.populate.IPopulateService;
import ariadna.services.util.RawConnectionProvider;

public class RawBootstrapService extends AbstractBootstrapService {
	
	IPopulateService populateService;
	
	public RawBootstrapService(IPopulateService populateService) {
		this.populateService = populateService;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		return RawConnectionProvider.getConnection();
	}
	
	@Override
	protected IPopulateService getPopulateService() {
		return populateService;
	}

	@Override
	protected void log(String msg) {
		System.out.print(String.format("[%s] %s", getClass().getSimpleName(), msg));
		
	}
}
