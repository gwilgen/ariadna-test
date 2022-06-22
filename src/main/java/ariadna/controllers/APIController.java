package ariadna.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import ariadna.models.Event;
import ariadna.services.populate.IPopulateService;

@Controller
@RequestMapping("api")
public class APIController extends AbstractController {
	
	ObjectMapper om = new ObjectMapper();
	
	@Autowired
	IPopulateService populateService;

	@RequestMapping(value = "timestamps", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, ?> apiTimestamps(@RequestParam String from, @RequestParam String to, Pageable pageable) {
		return apiResult(super.timestamps(from, to, pageable));
	}
	
	@RequestMapping(value = "source", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, ?> apiSource(@RequestParam int id, Pageable pageable) {
		return apiResult(super.source(id, pageable));
	}
	
	@RequestMapping(value = "values", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, ?> apiValues(@RequestParam String from, @RequestParam String to, Pageable pageable) {
		return apiResult(super.values(from, to, pageable));
	}
	
	@RequestMapping(value = "createEvents", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Integer> createEvents() {
		Map<String, Integer> result = new HashMap<>();
		result.put("count", populateService.createRandomEvents());
		return result;
	}
	
	private Map<String, Object> apiResult(List<Event> events) {
		Map<String, Object> result = new HashMap<>();
		result.put("events", events.stream().map(e -> om.convertValue(e, Map.class)).collect(Collectors.toList()));
		return result;
	}
}
