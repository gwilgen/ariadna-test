package ariadna.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "event_sources")
public class EventSource {

	// For simplification purposes, the id is hardcoded in the code
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	Integer id;
	
	String name;
	
	public EventSource() { id = -1; }
	
	public EventSource(Integer id, String name) {
		this.id = id; // For simplification purposes
		this.name = name;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
}
