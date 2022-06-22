package ariadna.services.bootstrap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import ariadna.services.populate.IPopulateService;
import ariadna.services.util.ConnectionProvider;

public abstract class AbstractBootstrapService implements IBootstrapService, ConnectionProvider {

	protected abstract IPopulateService getPopulateService();
	
	@Override
	public void check() {
		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement("select count(*) from events")) {
				try (ResultSet rs = stmt.executeQuery()) {
					rs.next(); // If it fails, it launches an exception
					if (0 == rs.getInt(1)) {
						createRandomEvents();
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Could not initialize the database", e);
		}
	}
	
	private void createRandomEvents() {
		log("Creating random events...");
		int num = getPopulateService().createRandomEvents();
		log(String.format("Created %s random events", num));
	}
	
	protected abstract void log(String msg);
}
