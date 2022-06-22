package ariadna;

import ariadna.services.bootstrap.RawBootstrapService;
import ariadna.services.bootstrap.IBootstrapService;
import ariadna.services.event.RawSQLEventsService;
import ariadna.services.event.IEventsService;
import ariadna.services.populate.IPopulateService;
import ariadna.services.populate.RawSQLPopulateService;
import ariadna.swing.SwingFrame;

public class RawSwingApp {
	
	IEventsService		eventsService		= new RawSQLEventsService();
	IPopulateService	populateService		= new RawSQLPopulateService();
	IBootstrapService	bootstrapService	= new RawBootstrapService(populateService);

	public RawSwingApp() {
		bootstrapService.check(); // explicit check at startup
		new SwingFrame(eventsService, populateService).setVisible(true);
	}
	
	public static void main(String... args) {
		new RawSwingApp();
	}
}
