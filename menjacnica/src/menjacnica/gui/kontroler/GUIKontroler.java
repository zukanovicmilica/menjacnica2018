package menjacnica.gui.kontroler;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import menjacnica.Menjacnica;
import menjacnica.MenjacnicaInterface;
import menjacnica.Valuta;
import menjacnica.gui.DodajKursGUI;
import menjacnica.gui.IzvrsiZamenuGUI;
import menjacnica.gui.MenjacnicaGUI;
import menjacnica.gui.ObrisiKursGUI;
import menjacnica.gui.models.MenjacnicaTableModel;

public class GUIKontroler {
	public static MenjacnicaInterface sistem = new Menjacnica();
	public static MenjacnicaGUI mg;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIKontroler.mg = new MenjacnicaGUI();
					mg.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void sacuvajUFajl() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(mg);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				sistem.sacuvajUFajl(file.getAbsolutePath());
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(mg, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void ucitajIzFajla(JTable table, JPanel contentPane) {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(contentPane);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				sistem.ucitajIzFajla(file.getAbsolutePath());
				prikaziSveValute(table);
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(contentPane, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void prikaziSveValute(JTable table) {
		MenjacnicaTableModel model = (MenjacnicaTableModel) (table.getModel());
		model.staviSveValuteUModel(sistem.vratiKursnuListu());

	}

	public static void prikaziDodajKursGUI() {
		DodajKursGUI prozor = new DodajKursGUI();
		prozor.setLocationRelativeTo(mg);
		prozor.setVisible(true);
	}

	public static void prikaziObrisiKursGUI(JTable table) {

		if (table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (table.getModel());
			ObrisiKursGUI prozor = new ObrisiKursGUI(model.vratiValutu(table.getSelectedRow()));
			prozor.setLocationRelativeTo(mg);
			prozor.setVisible(true);
		}
	}

	public static void prikaziIzvrsiZamenuGUI(JTable table) {
		if (table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (table.getModel());
			IzvrsiZamenuGUI prozor = new IzvrsiZamenuGUI(model.vratiValutu(table.getSelectedRow()));
			prozor.setLocationRelativeTo(mg);
			prozor.setVisible(true);
		}
	}

	public static void obrisiValutu(Valuta valuta) {
		try {
			sistem.obrisiValutu(valuta);

			prikaziSveValute(mg.getTable());
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(mg, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void unesiKurs(String naziv, String skraceni, int spinner, double prodajni, double kupovni,
			double srednji) {
		try {
			Valuta valuta = new Valuta();

			// Punjenje podataka o valuti
			valuta.setNaziv(naziv);
			valuta.setSkraceniNaziv(skraceni);
			valuta.setSifra(spinner);
			valuta.setProdajni(prodajni);
			valuta.setKupovni(kupovni);
			valuta.setSrednji(srednji);

			// Dodavanje valute u kursnu listu
			sistem.dodajValutu(valuta);

			// Osvezavanje glavnog prozora
			prikaziSveValute(mg.getTable());

		} catch (Exception e1) {
			JOptionPane.showMessageDialog(mg, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static String izvrsiZamenu(Valuta valuta, boolean selected, double parseDouble) {
		try {
			double konacniIznos = sistem.izvrsiTransakciju(valuta, selected, parseDouble);

			return "" + konacniIznos;

		} catch (Exception e1) {
			JOptionPane.showMessageDialog(mg, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
}
