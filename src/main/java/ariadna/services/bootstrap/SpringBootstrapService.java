package ariadna.services.bootstrap;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ariadna.services.populate.IPopulateService;

@Service
public class SpringBootstrapService extends AbstractBootstrapService {
	
	Logger logger = LoggerFactory.getLogger(SpringBootstrapService.class);
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	IPopulateService populateService;
	
	@Override
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
	@Override
	public IPopulateService getPopulateService() {
		return populateService;
	}
	
	/**
	 * Spring will call this method after the instance binding
	 */
	@PostConstruct
	public void autocheck() {
		log("Performing autocheck");
		check();
	}

	@Override
	protected void log(String msg) {
		logger.debug(msg);
	}

}
