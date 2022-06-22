package ariadna.services.populate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

import ariadna.models.EventSource;

/**
 * Common behavior independent of db implementation
 * @author Guillermo
 *
 */
public abstract class AbstractPopulateService implements IPopulateService {
	
	@Override
	public int createRandomEvents() {
		int result = 0;
		for (EventSource es: getEventSources()) {
			while (true) {
				if (Math.random() > 0.8) { // eventually no more events will be created
					break;
				}
				createEvent(es.getId(), getRandomTimestamp(), getRandomValue());
				result++;
			}
		}
		return result;
	}
	
	/**
	 * Values will be between 1/1/2000 and 1/1/2020 (arbitrary decision)
	 * @return
	 */
	private Timestamp getRandomTimestamp() {
		Double rand = Math.random();
		final long offset = LocalDate.of(2000, 1, 1).toEpochDay() * 24 * 60 * 60 * 1000;
		final long range = LocalDate.of(2020, 1, 1).toEpochDay() * 24 * 60 * 60 * 1000 - offset;
		return new Timestamp(Double.valueOf(rand*range).longValue() + offset);
	}

	/**
	 * Values will be between -100 and 100 (arbitrary decision)
	 * @return
	 */
	private Double getRandomValue() {
		Double rand = Math.random();
		final long range = 200;
		assert (range > 1);
		final long offset = range / -2;
		return rand*range + offset;
	}
}
