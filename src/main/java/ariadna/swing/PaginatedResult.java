package ariadna.swing;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import ariadna.models.Event;

public class PaginatedResult extends JDialog {

	private static final long serialVersionUID = 1L;

	JButton next = new JButton("Siguiente");
	JButton prev = new JButton("Anterior");

	Function<Pageable, List<Event>> callback;
	int currentPage = 0;
	final int pageSize = 20;
	DefaultTableModel model = new DefaultTableModel(new Object[][] {}, new String[]{"id", "fuente_id", "timestamp", "valor"}); 
	
	public void update(Function<Pageable, List<Event>> callback) {
		this.callback = callback;
		currentPage = -1;
		navigate(getNextPage());
		if (0 == model.getRowCount()) {
			setVisible(false);
			JOptionPane.showMessageDialog(this,
			    "No se han encontrado registros para la búsqueda seleccionada",
			    "No hay resultados",
			    JOptionPane.WARNING_MESSAGE);
		}
	}
	
	protected void navigate(Pageable pageable) {
		setVisible(false);
		while (model.getRowCount() > 0) {
			model.removeRow(0);
		}
		callback.apply(pageable)
			.forEach(e -> model.addRow(new Object[] {e.getId(), e.getSourceId(), e.getTimestamp(), e.getValue()}));
		setTitle("Resultados de la búsqueda - página " + currentPage);
		next.setEnabled(pageSize == model.getRowCount());
		prev.setEnabled(0 < pageable.getPageNumber());
		setVisible(true);
	}
	
	public PaginatedResult(JFrame parent) {
		super(parent);
		setLayout(new GridLayout(2, 1));
		setBounds(440, 40, 800, 600);
		
		JTable t = new JTable(model);
		t.setPreferredSize(new Dimension(740, 480));
		t.setFillsViewportHeight(true);
		add(new JScrollPane(t));
		
		add(createPaginationButtons());
	}
	
	protected JPanel createPaginationButtons() {
		JPanel result = new JPanel();

		prev.addActionListener(e -> navigate(getPrevPage()));
		next.addActionListener(e -> navigate(getNextPage()));

		result.add(prev);
		result.add(next);
		
		return result;
	}
	
	protected Pageable getNextPage() {
		return PageRequest.of(++currentPage, pageSize);
	}
	
	protected Pageable getPrevPage() {
		return PageRequest.of(--currentPage, pageSize);
	}
}
