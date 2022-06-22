package ariadna.models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "events")
public class Event {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	Long id;
	
	Integer sourceId;
	
	Timestamp timestamp;
	
	Double value;
	
	public Event() { id = -1L; }
	
	public Event(Integer sourceId, Timestamp timestamp, Double value) {
		this.sourceId = sourceId;
		this.timestamp = timestamp;
		this.value = value;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public Integer getSourceId() {
		return sourceId;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	
	public Double getValue() {
		return value;
	}
}
