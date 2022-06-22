package ariadna.services.event;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;

import ariadna.models.Event;

public interface IEventsService {	
	
	public List<Event> getEventsBetweenTimestamps(Timestamp from, Timestamp to, Pageable pageable);
	
	public List<Event> getEventsOfSource(Integer sourceId, Pageable pageable);
	
	public List<Event> getEventsBetweenValues(Double min, Double max, Pageable pageable);

	public long getNumberOfEvents();

}
