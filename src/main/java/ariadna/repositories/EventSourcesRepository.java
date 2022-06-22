package ariadna.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ariadna.models.EventSource;

public interface EventSourcesRepository extends CrudRepository<EventSource, Integer> {
	
	List<EventSource> findAll();
}
