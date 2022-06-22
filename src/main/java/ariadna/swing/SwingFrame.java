package ariadna.swing;

import java.awt.GridLayout;
import java.sql.Timestamp;
import java.util.List;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.springframework.data.domain.Pageable;

import ariadna.models.Event;
import ariadna.services.event.IEventsService;
import ariadna.services.populate.IPopulateService;
import ariadna.validations.DomainValidations;

public class SwingFrame extends JFrame {
		
	private static final long serialVersionUID = 1L;
	
	JLabel numEvents = new JLabel("Inicializando...");
	JComboBox<String> combo = new JComboBox<>();

	IEventsService eventsService;
	IPopulateService populateService;
	
	PaginatedResult results = new PaginatedResult(this);
	
	public SwingFrame(IEventsService eventsService, IPopulateService populateService) {
		this.eventsService = eventsService;
		this.populateService = populateService;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 20, 1024, 800);
		setTitle("Ariadna Test - Spring App");
		setLayout(new GridLayout(5, 1));
		add(numEvents);
		updateNumEvents();
		add(createFormBetweenTimestamps());
		add(createFormByEventSource());
		add(createFormBetweenValues());
		add(createPopulateRandomEvents());
	}
	
	protected JPanel createFormBetweenTimestamps() {
		JPanel result = new JPanel(new GridLayout(3, 2));
		result.setBorder(new TitledBorder("Eventos entre intervalo de timestamps"));

		JLabel fromLabel = new JLabel("Desde:");
		JTextField fromTimestamp = new JTextField(20);
		result.add(fromLabel);
		result.add(fromTimestamp);
		
		JLabel toLabel = new JLabel("Hasta:");
		JTextField toTimestamp = new JTextField(20);
		result.add(toLabel);
		result.add(toTimestamp);
		
		JButton submit = new JButton("Enviar");
		submit.addActionListener(l -> {
			if (!checkValidTimestamp(fromTimestamp, "El campo desde es incorrecto (formato yyyy-mm-dd hh:MM:ss)")) {
				return;
			}
			if (!checkValidTimestamp(toTimestamp, "El campo hasta es incorrecto (formato yyyy-mm-dd hh:MM:ss)")) {
				return;
			}
			showResults(p -> eventsService.getEventsBetweenTimestamps(
				Timestamp.valueOf(fromTimestamp.getText()),
				Timestamp.valueOf(toTimestamp.getText()),
				p
			));
		});
		result.add(new JLabel("")); // dummy 
		result.add(submit);
		
		return result;
	}
	
	protected boolean checkValidTimestamp(JTextField f, String msg) {
		return showDialogOnValidationFail(f, msg, DomainValidations::isValidTimestamp);
	}
	
	protected boolean checkValidValue(JTextField f, String msg) {
		return showDialogOnValidationFail(f, msg, DomainValidations::isValidValue);
	}
	
	/**
	 * Common validation behavior
	 * @param f
	 * @param msg
	 * @param callback
	 * @return
	 */
	private boolean showDialogOnValidationFail(JTextField f, String msg, Function<String, Boolean> callback) {
		if (!callback.apply(f.getText())) {
			f.grabFocus();
			JOptionPane.showMessageDialog(this,
			    msg,
			    "Error de validación",
			    JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	protected JPanel createFormByEventSource() {
		JPanel result = new JPanel(new GridLayout(2, 1));
		result.setBorder(new TitledBorder("Eventos de un origen"));

		result.add(new JLabel("Seleccionar origen"));
		populateService.getEventSources().forEach(es -> combo.addItem(es.getName()));
		result.add(combo);
		
		JButton submit = new JButton("Enviar");
		// Warning! selectedIndex and actual id *might* not mean to just add +1, this should NOT go to production!
		submit.addActionListener(e -> showResults(p -> eventsService.getEventsOfSource(combo.getSelectedIndex() + 1, p)));

		result.add(new JLabel("")); // dummy 
		result.add(submit);

		return result;
	}

	protected JPanel createFormBetweenValues() {
		JPanel result = new JPanel(new GridLayout(3, 2));
		
		result.setBorder(new TitledBorder("Eventos entre intervalo de valores"));
		
		JLabel fromLabel = new JLabel("Desde:");
		JTextField fromTimestamp = new JTextField(20);
		result.add(fromLabel);
		result.add(fromTimestamp);
		
		JLabel toLabel = new JLabel("Hasta:");
		JTextField toTimestamp = new JTextField(20);
		result.add(toLabel);
		result.add(toTimestamp);
		
		JButton submit = new JButton("Enviar");
		submit.addActionListener(l -> {
			if (!checkValidValue(fromTimestamp, "El campo desde es incorrecto (sólo números, y de usar separador decimal, que sea .)")) {
				return;
			}
			if (!checkValidValue(toTimestamp, "El campo hasta es incorrecto (sólo números, y de usar separador decimal, que sea .)")) {
				return;
			}
			showResults(p -> eventsService.getEventsBetweenValues(
				Double.valueOf(fromTimestamp.getText()),
				Double.valueOf(toTimestamp.getText()),
				p
			));
		});
		
		result.add(new JLabel("")); // dummy 
		result.add(submit);

		return result;
	}

	protected JPanel createPopulateRandomEvents() {
		JPanel result = new JPanel(new GridLayout(2, 1));
		result.add(new JLabel("Simular la creación de eventos"));
		JButton btn = new JButton("Crear");
		btn.addActionListener(e -> {
			JOptionPane.showMessageDialog(this,
			    String.format("Se han creado %s registros aleatorios", populateService.createRandomEvents()),
			    "Resultado",
			    JOptionPane.INFORMATION_MESSAGE);
			updateNumEvents();
		});
		result.add(btn);
		return result;
	}
	
	protected void showResults(Function<Pageable, List<Event>> callback) {
		results.update(callback);
	}
	
	protected void updateNumEvents() {
		numEvents.setText(String.format("La base de datos contiene %s registros de eventos", eventsService.getNumberOfEvents()));
	}

}
