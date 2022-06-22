package ariadna.services.bootstrap;

public interface IBootstrapService {
	
	/**
	 * Run at startup to check if the database exists and has the tables created (and with some content)
	 */
	public void check();

}
