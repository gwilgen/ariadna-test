package ariadna.services.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;

import ariadna.models.Event;
import ariadna.services.util.ConnectionProvider;

public abstract class AbstractSQLEventsService implements IEventsService, ConnectionProvider {
	
	@Override
	public List<Event> getEventsBetweenTimestamps(Timestamp from, Timestamp to, Pageable pageable) {
		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement("select id, source_id, timestamp, value from events where timestamp < ? and timestamp > ? order by timestamp limit ? offset ?")) {
				stmt.setTimestamp(1, from);
				stmt.setTimestamp(2, to);
				stmt.setInt(3, pageable.getPageSize());
				stmt.setInt(4, pageable.getPageSize()*pageable.getPageNumber());
				return executeAndMapToEventsList(stmt);
			}
		} catch (Exception e) {
			throw new RuntimeException("Could not perform the query", e);
		}
	}
	
	@Override
	public List<Event> getEventsOfSource(Integer sourceId, Pageable pageable) {
		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement("select id, source_id, timestamp, value from events where source_id = ? order by timestamp limit ? offset ?")) {
				stmt.setInt(1, sourceId);
				stmt.setInt(2, pageable.getPageSize());
				stmt.setInt(3, pageable.getPageSize()*pageable.getPageNumber());
				return executeAndMapToEventsList(stmt);
			}
		} catch (Exception e) {
			throw new RuntimeException("Could not perform the query", e);
		}
	}
	
	@Override
	public List<Event> getEventsBetweenValues(Double min, Double max, Pageable pageable) {
		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement("select id, source_id, timestamp, value from events where value > ? and value < ? order by timestamp limit ? offset ?")) {
				stmt.setDouble(1, min);
				stmt.setDouble(2, max);
				stmt.setInt(3, pageable.getPageSize());
				stmt.setInt(4, pageable.getPageSize()*pageable.getPageNumber());
				return executeAndMapToEventsList(stmt);
			}
		} catch (Exception e) {
			throw new RuntimeException("Could not perform the query", e);
		}
	}
	
	private List<Event> executeAndMapToEventsList(PreparedStatement stmt) throws SQLException {
		try (ResultSet rs = stmt.executeQuery()) {
			List<Event> result = new ArrayList<>();
			while (rs.next()) {
				Event e = new Event(rs.getInt("source_id"), rs.getTimestamp("timestamp"), rs.getDouble("value"));
				e.setId(rs.getLong("id"));
				result.add(e);
			}
			return result;
		}
	}
	
	@Override
	public long getNumberOfEvents() {
		try (Connection conn = getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement("select count(*) from events")) {
				try (ResultSet rs = stmt.executeQuery()) {
					rs.next();
					return rs.getLong(1);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Could not perform the query", e);
		}
	}
	
}
