package ariadna.controllers;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import ariadna.models.Event;
import ariadna.services.event.IEventsService;
import ariadna.validations.DomainValidations;
import ariadna.validations.ValidationException;

public abstract class AbstractController {

	@Autowired
	IEventsService eventsService;

	public List<Event> timestamps(String from, String to, Pageable pageable) {
		if (!DomainValidations.isValidTimestamp(from)) {
			throw new ValidationException("El campo desde es incorrecto (formato yyyy-mm-dd hh:MM:ss)");
		}
		if (!DomainValidations.isValidTimestamp(to)) {
			throw new ValidationException("El campo hasta es incorrecto (formato yyyy-mm-dd hh:MM:ss)");
		}
		return eventsService.getEventsBetweenTimestamps(Timestamp.valueOf(from), Timestamp.valueOf(to), pageable);
	}
	
	public List<Event> source(int id, Pageable pageable) {
		return eventsService.getEventsOfSource(id, pageable);
	}
	
	public List<Event> values(String from, String to, Pageable pageable) {
		if (!DomainValidations.isValidValue(from)) {
			throw new ValidationException("El campo desde es incorrecto (sólo números, y de usar separador decimal, que sea .)");
		}
		if (!DomainValidations.isValidValue(to)) {
			throw new ValidationException("El campo hasta es incorrecto (sólo números, y de usar separador decimal, que sea .)");
		}
		return eventsService.getEventsBetweenValues(Double.valueOf(from), Double.valueOf(to), pageable);
	}
	
}
