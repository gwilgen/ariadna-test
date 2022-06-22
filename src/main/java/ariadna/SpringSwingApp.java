package ariadna;

import java.awt.EventQueue;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import ariadna.services.event.IEventsService;
import ariadna.services.populate.IPopulateService;
import ariadna.swing.SwingFrame;

@SpringBootApplication
public class SpringSwingApp {

	public static void main(String... args) {
		ApplicationContext ctx = new SpringApplicationBuilder(SpringSwingApp.class)
			.headless(false)
			.run(args);
		EventQueue.invokeLater(() -> {
			new SwingFrame(ctx.getBean(IEventsService.class), ctx.getBean(IPopulateService.class))
				.setVisible(true);

		});
	}
	
}
