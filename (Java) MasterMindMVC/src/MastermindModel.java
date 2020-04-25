import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;

/**
 * Classe Model di MasterMind
 * @author Davide Galante, Gian Maria Cavalli, Leonid Vlas
 * @version 2.0 (Con GUI)
 */
public class MastermindModel {
	/** contiene la combinazione di 4 colori generata casualmente */
	static String[] soluzione = new String[4];

	/** contiene i colori già usati in una riga */
	String[] coloriUsati = new String[4];

	/** contiene tutti i colori disponibili */
	static String[] colori = {"Giallo","Arancione","Celeste","Verde","Rosso","Nero"};

	/** Variabile che diventa true quando il gioco è terminato */
	boolean giocoFinito = false;

	/** contiene i colori (della classe Color) disponibili nel gioco */
	Color[] coloriDisponibili = {Color.yellow,Color.orange,Color.cyan,Color.green,Color.red,Color.black};

	/** Classe View di MasterMind */
	MastermindView view;

	/**
	 *  Richiama metodo che crea combinazione casuale di 4 colori
	 * @param view classe View di MasterMind
	 */
	public MastermindModel(MastermindView view){	
		this.view = view;
		generaCombinazione();
	}

	/**
	 *Metodo che si occupa di creare una combinazione di 4 colori.
	 *(Il colore è rappresentato da una stringa che contiene il suo nome)
	 *La combinazione generata viene quindi salvata nell'array soluzione.
	 */
	public static void generaCombinazione(){
		ArrayList<String> colors = new ArrayList<String>();
		colors.add("Giallo");
		colors.add("Arancione");
		colors.add("Celeste");
		colors.add("Verde");
		colors.add("Rosso");
		colors.add("Nero");
		Collections.shuffle(colors);

		//Genero l'array di 4 colori random
		for (int i=0; i<4; i++){
			soluzione[i]=colors.get(i);
		}
	}

	/**
	 * Comincia una nuova partita, settando tutti i parametri a quelli predefiniti.
	 */
	public void nuova_partita(){
		giocoFinito = false;
		view.colore_att = "giallo"; //risetta il colore attuale al giallo
		view.mostraColoreAtt.setBackground(Color.yellow); //ricolora di giallo la casella "colore attuale"
		view.riga_att = 7; //risetta la prima riga in basso come la riga su cui posizionare i colori
		for (int rig = 0; rig < 8; rig++){
			for (int col = 0; col < 4; col++){
				view.tab_array[rig][col].setBackground(Color.gray); //cancello tutti i colori sul tabellone
				view.tentativo[rig][col] = ""; //azzero tutti i tentativi memorizzati
				view.esito[rig][col].setBackground(Color.gray); //cancello tutti i colori nei suggerimenti
			}
		}
		view.coloreUno = false;		//azzero le variabili booleane che gestiscono il passaggio di riga
		view.coloreDue = false;
		view.coloreTre = false;
		view.coloreQuattro = false;
		generaCombinazione(); 		//calcolo una nuova soluzione

	}

	/**
	 * Verifica l'esattezza dei colori messi nel tentativo e gestisce la vincita o la sconfitta.
	 */
	public void verifica(){
		int cont_sug = 0; //incrementa di 1 ogni volta che viene colorato di bianco o di nero un rettangolo dei "suggerimenti"

		//quando una colonna è indovinata vengono settati a true, se alla fine sono tutti true mi da partita vinta
		boolean indovinato1 = false; 
		boolean indovinato2 = false;
		boolean indovinato3 = false;
		boolean indovinato4 = false;


		//controlla prima se le 4 posizioni sono esatte
		if (view.tentativo[view.riga_att][0].equals(soluzione[0])){
			view.esito[view.riga_att][cont_sug].setBackground(Color.black);
			cont_sug = cont_sug + 1;
			indovinato1 = true; //questa posizione è indovinata
		}
		if (view.tentativo[view.riga_att][1].equals(soluzione[1])){
			view.esito[view.riga_att][cont_sug].setBackground(Color.black);
			cont_sug = cont_sug + 1;
			indovinato2 = true;
		}
		if (view.tentativo[view.riga_att][2].equals(soluzione[2])){
			view.esito[view.riga_att][cont_sug].setBackground(Color.black);
			cont_sug = cont_sug + 1;
			indovinato3 = true;
		}
		if (view.tentativo[view.riga_att][3].equals(soluzione[3])){
			view.esito[view.riga_att][cont_sug].setBackground(Color.black);
			cont_sug = cont_sug + 1;
			indovinato4 = true;
		}

		//PRIMA COLONNA CONFRONTATA CON TUTTE LE COLONNE DELLA SOLUZIONE
		if (view.tentativo[view.riga_att][0].equals(soluzione[1])){
			view.esito[view.riga_att][cont_sug].setBackground(Color.white);
			cont_sug = cont_sug + 1;
		}
		else if (view.tentativo[view.riga_att][0].equals(soluzione[2])){
			view.esito[view.riga_att][cont_sug].setBackground(Color.white);
			cont_sug = cont_sug + 1;
		}
		else if (view.tentativo[view.riga_att][0].equals(soluzione[3])){
			view.esito[view.riga_att][cont_sug].setBackground(Color.white);
			cont_sug = cont_sug + 1;
		}

		//SECONDA COLONNA
		if (view.tentativo[view.riga_att][1].equals(soluzione[0])){
			view.esito[view.riga_att][cont_sug].setBackground(Color.white);
			cont_sug = cont_sug + 1;
		}
		else if (view.tentativo[view.riga_att][1].equals(soluzione[2])){
			view.esito[view.riga_att][cont_sug].setBackground(Color.white);
			cont_sug = cont_sug + 1;
		}
		else if (view.tentativo[view.riga_att][1].equals(soluzione[3])){
			view.esito[view.riga_att][cont_sug].setBackground(Color.white);
			cont_sug = cont_sug + 1;
		}

		//TERZA COLONNA
		if (view.tentativo[view.riga_att][2].equals(soluzione[0])){
			view.esito[view.riga_att][cont_sug].setBackground(Color.white);
			cont_sug = cont_sug + 1;
		}
		else if (view.tentativo[view.riga_att][2].equals(soluzione[1])){
			view.esito[view.riga_att][cont_sug].setBackground(Color.white);
			cont_sug = cont_sug + 1;
		}
		else if (view.tentativo[view.riga_att][2].equals(soluzione[3])){
			view.esito[view.riga_att][cont_sug].setBackground(Color.white);
			cont_sug = cont_sug + 1;
		}


		//QUARTA COLONNA
		if (view.tentativo[view.riga_att][3].equals(soluzione[0])){
			view.esito[view.riga_att][cont_sug].setBackground(Color.white);
			cont_sug = cont_sug + 1;
		}
		else if (view.tentativo[view.riga_att][3].equals(soluzione[1])){
			view.esito[view.riga_att][cont_sug].setBackground(Color.white);
			cont_sug = cont_sug + 1;
		}
		else if (view.tentativo[view.riga_att][3].equals(soluzione[2])){
			view.esito[view.riga_att][cont_sug].setBackground(Color.white);
			cont_sug = cont_sug + 1;
		}

		//se ho vinto compare la dialog box che me lo segnala
		if (indovinato1 && indovinato2 && indovinato3 && indovinato4) {
			giocoFinito=true;
			view.tentativi_tot = -view.riga_att + 8; //memorizzo il numero totale di tentativi fatti

			String text = "HAI VINTO!\nTENTATIVI: "+(view.tentativi_tot);
			text += "\nPremere OK per iniziare una nuova partita.";
			int result = JOptionPane.showConfirmDialog(null, text, "COMPLIMENTI",JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);

			if(JOptionPane.OK_OPTION==result) 
			{
				nuova_partita(); //viene avviata una nuova partita
			}
		} else {	//Se non ho vinto passo alla riga successiva
			if(view.riga_att != 0){ //se mi trovo nella prima riga non aumento
				view.riga_att -= 1;
			}
			//se è già l'ottavo tentativo avviso che la partita è stata persa
			else {
				giocoFinito = true;

				String text = "HAI PERSO!\nL'esatta sequenza era: ";
				for (int i = 0; i < 4; i++){
					text += soluzione[i]+" "; //mostro la soluzione esatta
				}
				text += "\nPremere OK per iniziare una nuova partita.";

				int result = JOptionPane.showConfirmDialog(null, text, "MI DISPIACE", JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
				if(JOptionPane.OK_OPTION==result){
					nuova_partita(); //avvio una nuova partita
				}
			}
		}
	}
}
