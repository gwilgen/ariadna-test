package ariadna.services.populate;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ariadna.models.Event;
import ariadna.models.EventSource;
import ariadna.repositories.EventSourcesRepository;
import ariadna.repositories.EventsRepository;

public class JPAPopulateService extends AbstractPopulateService {

	@Autowired
	EventSourcesRepository eventSourcesRepository;
	
	@Autowired
	EventsRepository eventsRepository;
	
	
	@Override
	public List<EventSource> getEventSources() {
		return eventSourcesRepository.findAll();
	}

	@Override
	public Event createEvent(Integer sourceId, Timestamp timestamp, Double value) {
		return eventsRepository.save(new Event(sourceId, timestamp, value));
	}

}
