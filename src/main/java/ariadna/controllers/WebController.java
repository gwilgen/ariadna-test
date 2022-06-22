package ariadna.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ariadna.models.Event;
import ariadna.validations.ValidationException;

@Controller
@RequestMapping("web")
public class WebController extends AbstractController {
	
	@RequestMapping("timestamps")
	public String webTimestamps(@RequestParam String from, @RequestParam String to, Pageable pageable, Model model) {
		return webResult(() -> super.timestamps(from, to, pageable), pageable, model);
	}
	
	@RequestMapping("source")
	public String webSource(@RequestParam int id, Pageable pageable, Model model) {
		return webResult(() -> super.source(id, pageable), pageable, model);
	}
	
	@RequestMapping("values")
	public String webValues(@RequestParam String from, @RequestParam String to, Pageable pageable, Model model) {
		return webResult(() -> super.values(from, to, pageable), pageable, model);
	}
	
	private String webResult(Supplier<List<Event>> eventsSupplier, Pageable pageable, Model model) {
		try {
			model.addAttribute("events", eventsSupplier.get()); // beware! the supplier being called in the first statement is tricky...
			model.addAttribute("page", pageable.getPageNumber());
			return "results";
		} catch (ValidationException ve) {
			model.addAttribute("msg", ve.getMessage());
			return "error";
		}
	}
}
