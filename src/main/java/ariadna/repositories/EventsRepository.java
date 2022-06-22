package ariadna.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import ariadna.models.Event;

public interface EventsRepository extends PagingAndSortingRepository<Event, Long> {

	public List<Event> findAllByTimestampBetweenOrderByTimestamp(Timestamp from, Timestamp to, Pageable pageable);
	
	public List<Event> findBySourceIdOrderByTimestamp(Integer sourceId, Pageable pageable);
		
	public List<Event> findAllByValueBetweenOrderByTimestamp(Double min, Double max, Pageable pageable);

}
