package ariadna.services.event;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import ariadna.models.Event;
import ariadna.repositories.EventSourcesRepository;
import ariadna.repositories.EventsRepository;

public class JPAEventsService implements IEventsService {
	
	@Autowired
	EventSourcesRepository eventSourceRepository;
	
	@Autowired
	EventsRepository eventsRepository;

	@Override
	public List<Event> getEventsBetweenTimestamps(Timestamp from, Timestamp to, Pageable pageable) {
		return eventsRepository.findAllByTimestampBetweenOrderByTimestamp(from, to, pageable);
	}
	
	@Override
	public List<Event> getEventsOfSource(Integer sourceId, Pageable pageable) {
		return eventsRepository.findBySourceIdOrderByTimestamp(sourceId, pageable);
	}
	
	@Override
	public List<Event> getEventsBetweenValues(Double min, Double max, Pageable pageable) {
		return eventsRepository.findAllByValueBetweenOrderByTimestamp(min, max, pageable);
	}

	@Override
	public long getNumberOfEvents() {
		return eventsRepository.count();
	}
	
}
