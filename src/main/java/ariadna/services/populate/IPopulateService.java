package ariadna.services.populate;

import java.sql.Timestamp;
import java.util.List;

import ariadna.models.Event;
import ariadna.models.EventSource;

public interface IPopulateService {
	
	// Not used, initialized via schema.sql
	// public EventSource createEventSource(EventSource es);
	
	public Event createEvent(Integer sourceId, Timestamp timestamp, Double value);
	
	public List<EventSource> getEventSources();
	
	public int createRandomEvents();

}
