import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * Classe Controller di MasterMind
 * @author Davide Galante, Gian Maria Cavalli, Leonid Vlas
 * @version 2.0 (Con GUI)
 */

public class MastermindController implements ActionListener,MouseWheelListener{

	/** classe model di mastermind */
	private MastermindModel model;
	/** classe view di mastermind */
	private MastermindView view;
	/** indice del colore nell'array, per selezione con rotellina */
	int indiceColore;

	/**
	 * 
	 * @param model classe model di mastermind
	 * @param view classe view di mastermind
	 */
	public MastermindController(MastermindModel model, MastermindView view) {
		this.model = model;
		this.view = view;
	}

	@SuppressWarnings("static-access")
	public void actionPerformed(ActionEvent e){
		Object bottone_premuto = e.getSource();
		if (bottone_premuto == view.giallo) {
			view.colore_att = "Giallo";
			view.mostraColoreAtt.setBackground(Color.yellow);
		}
		else if (bottone_premuto == view.arancione) {
			view.colore_att = "Arancione";
			view.mostraColoreAtt.setBackground(Color.orange);
		}
		else if (bottone_premuto == view.celeste) {
			view.colore_att = "Celeste";
			view.mostraColoreAtt.setBackground(Color.cyan);
		}
		else if (bottone_premuto == view.verde) {
			view.colore_att = "Verde";
			view.mostraColoreAtt.setBackground(Color.green);
		}
		else if (bottone_premuto == view.rosso) {
			view.colore_att = "Rosso";
			view.mostraColoreAtt.setBackground(Color.red);
		}
		else if (bottone_premuto == view.nero) {
			view.colore_att = "Nero";
			view.mostraColoreAtt.setBackground(Color.black);
		}
		else if(bottone_premuto == view.help) {
			JOptionPane.showMessageDialog(view,
					"Inserisci una combinazione di 4 colori ed indovina quella generata casualmente"
							+ " dal computer!\n"
							+ "Seleziona uno dei colori premendo su uno di quelli presenti a sinistra (o con la rotellina del mouse),\n"
							+ "e successivamente premi su uno dei bottoni presenti nella parte centrale per immetterlo!\n"
							+ "Una volta completata una riga, ottieni dei suggerimenti sul tuo tentativo!\n\n"
							+ "Suggerimenti:\n"
							+ "Quadratino bianco = Colore presente nella sequenza generata casualmente, ma in posizione errata!\n"
							+ "Quadratino nero = Colore presente nella sequenza generata casualmente e nella stessa posizione!\n"
							+ "Quadratino grigio = Colore non presente nella sequenza generata casualmente!\n\n"
							+ "Per guardare la soluzione, selezionare dal menu a tendina la voce 'Visualizza Soluzione'\n\n"
							+ "Buon Divertimento!",
							"Come giocare",
							JOptionPane.PLAIN_MESSAGE);
		}

		else if(bottone_premuto == view.newGame) {
			model.nuova_partita();
		}
		else if(bottone_premuto == view.visualizzaSoluzione) {
			String combinazioneVincente = "";
			for (int i = 0; i < 4; i++){
				combinazioneVincente += model.soluzione[i]+" "; //e mostro la soluzione esatta
			}

			JOptionPane.showMessageDialog(view,
					"La soluzione è: \n"
							+ combinazioneVincente,
							"Soluzione",
							JOptionPane.PLAIN_MESSAGE);
		}
		else if(bottone_premuto == view.credits) {
			JOptionPane.showMessageDialog(view,
					"Applicazione sviluppata da:\n"
							+ "Davide Galante\n"
							+ "Gian Maria Cavalli\n"
							+ "Leonid Vlas",
							"Credits",
							JOptionPane.PLAIN_MESSAGE);
		}
		else{
			JButton campo = (JButton)bottone_premuto; 			//creo un bottone oggetto con le caratteristiche del bottone premuto
			int temp_col = Integer.parseInt(campo.getActionCommand()); 	//i bottoni hanno come "action command" il numero della colonna, salvo quel valore come intero
			if(!Arrays.asList(model.coloriUsati).contains(view.colore_att)) {
				if (model.giocoFinito == false) {
					//cambio a true il valore corrispondente al booleano della colonna cliccata
					if (temp_col == 0) {view.coloreUno = true;} 				
					if (temp_col == 1) {view.coloreDue = true;}
					if (temp_col == 2) {view.coloreTre = true;}
					if (temp_col == 3) {view.coloreQuattro = true;}
					model.coloriUsati[temp_col] = (view.colore_att);
					view.tab_array[view.riga_att][temp_col].setBackground(view.mostraColoreAtt.getBackground()); //coloro la casella corrente del colore scelto
					view.tentativo[view.riga_att][temp_col] = (""+view.colore_att); //memorizzo il colore scelto nella matrice tentativo
				}
			}
			else {
				JOptionPane.showMessageDialog(view,
						"Errore:\n"
								+ "Colore già inserito.",
								"Errore",
								JOptionPane.ERROR_MESSAGE);
			}

			if (view.coloreUno&&view.coloreDue&&view.coloreTre&&view.coloreQuattro){ //se tutta la riga è stata colorata
				model.coloriUsati = new String[4];
				model.verifica();
				//Azzero i valori che tengono conto delle colonne utilizzate (nessuna colonna è considerata premuta)
				view.coloreUno = false;
				view.coloreDue = false;
				view.coloreTre = false;
				view.coloreQuattro = false;
			}
		}
	}

	/**
	 * Ascoltatore rotellina del mouse
	 * Se vado su con la rotellina, upOrDown (variabile di controllo dello spostamento della rotellina) vale 2,
	 * Altrimenti, upOrDown vale 1.
	 * @param e evento della rotellina del mouse
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		int upOrDown = 0;
		if(e.getWheelRotation() > 0) {
			upOrDown = 2;
		}
		else if(e.getWheelRotation() < 0) {
			upOrDown = 1;
		}
		cambiaColore(upOrDown);

	}


	/**
	 * Cambia il colore selezionato in base al movimento della rotellina
	 * @param upOrDown intero che corrisponde alla direzione dello scorrimento della rotellina
	 */
	@SuppressWarnings("static-access")
	private void cambiaColore(int upOrDown) {
		if (upOrDown == 2) 
			indiceColore++;
		else
			indiceColore--;
		if (indiceColore == model.coloriDisponibili.length) {
			indiceColore = 0;
		}else if (indiceColore<0) {
			indiceColore = model.coloriDisponibili.length - 1;
		}
		view.mostraColoreAtt.setBackground(model.coloriDisponibili[indiceColore]);
		view.colore_att = model.colori[indiceColore];
	}
}
