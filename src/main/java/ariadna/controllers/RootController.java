package ariadna.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ariadna.services.event.IEventsService;
import ariadna.services.populate.IPopulateService;

@Controller
public class RootController {

	
	@Autowired
	IPopulateService populateService;
	
	@Autowired
	IEventsService eventsService;
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("numEvents", eventsService.getNumberOfEvents());
		model.addAttribute("eventSources", populateService.getEventSources());
		return "index";
	}
}
