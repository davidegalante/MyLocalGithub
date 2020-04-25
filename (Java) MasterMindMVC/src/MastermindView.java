import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

/**
 * Classe View di MasterMind
 * @author Davide Galante, Gian Maria Cavalli, Leonid Vlas
 * @version 2.0 (Con GUI)
 */

@SuppressWarnings("serial")
public class MastermindView extends JFrame{

	/** classe model di mastermind */
	private MastermindModel model;

	/** matrice che memorizza i tentativi fatti (come stringhe) */
	String[][] tentativo = new String[8][4]; 

	/** false finche primo bottone della riga non ancora premuto  */
	boolean coloreUno = false; 	
	/** false finche secondo bottone della riga non ancora premuto  */
	boolean coloreDue = false;	
	/** false finche terzo bottone della riga non ancora premuto  */
	boolean coloreTre = false;
	/** false finche quarto bottone della riga non ancora premuto  */
	boolean coloreQuattro = false;

	/** memorizza la riga in cui devono essere posizionati i colori */
	int riga_att = 7;
	/** memorizza il colore attualmente selezionato */
	String colore_att = "G";
	/**memorizza il numero di tentativi totali fatti al momento della vittoria*/
	int tentativi_tot;
	/** casella che mostra il colore attualmente in uso */
	JButton mostraColoreAtt = new JButton("Colore Attuale");

	/** definisce la zona suggerimenti */
	JButton[][] esito = new JButton[8][4]; 
	/** definisce la zona centrale coi bottoni */
	JButton[][] tab_array = new JButton[8][4];
	/** colori selezionabili */
	JButton giallo,arancione,celeste,verde,rosso,nero;
	/** tendine del menu a tendina */
	JMenuItem help,newGame,visualizzaSoluzione,credits;


	/**
	 * Chiama metodo che crea grafica
	 */
	public MastermindView()  {
		this.model = new MastermindModel(this);
		//Creo SplashScreen del gioco
		JWindow window = new JWindow();
		window.getContentPane().add(new JLabel(new ImageIcon(getClass().getClassLoader().getResource(("immagini/SplashScreen.jpg"))), SwingConstants.CENTER));
		window.setSize(433,311);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		window.setVisible(false);
		
		creaGui();
	}

	/**
	 * Crea grafica di gioco
	 */
	public void creaGui() {
		setTitle("MasterMind");
		setSize(400,440);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JMenuBar menuBar =new JMenuBar();  
		JMenu menu = new JMenu("Menu");  
		help=new JMenuItem("Come si gioca?"); 
		newGame=new JMenuItem("Nuova Partita"); 
		visualizzaSoluzione=new JMenuItem("Visualizza Soluzione"); 
		credits=new JMenuItem("Credits"); 
		help.addActionListener(new MastermindController(model,this));
		newGame.addActionListener(new MastermindController(model,this));
		visualizzaSoluzione.addActionListener(new MastermindController(model,this));
		credits.addActionListener(new MastermindController(model,this));
		menu.add(help);
		menu.add(newGame);
		menu.add(visualizzaSoluzione);
		menu.add(credits);
		menuBar.add(menu);  
		setJMenuBar(menuBar);  

		//Creo il pannello in cui inserisco tutta la grafica
		JPanel pane = new JPanel();
		pane.addMouseWheelListener(new MastermindController(model,this));
		pane.setLayout(new BorderLayout());

		/*--GRIGLIA COLORI--*/
		Box box_colori = new Box(BoxLayout.PAGE_AXIS);

		//Creo 1 pulsante per ogni colore
		giallo = new JButton("Giallo");
		arancione = new JButton("Arancione");
		celeste = new JButton("Celeste");
		verde = new JButton("Verde");
		rosso = new JButton("Rosso");
		nero = new JButton("Nero");
		nero.setForeground(Color.white);

		//Creo 1 listener per ogni pulsante
		giallo.addActionListener(new MastermindController(model, this));
		arancione.addActionListener(new MastermindController(model, this));
		celeste.addActionListener(new MastermindController(model, this));
		verde.addActionListener(new MastermindController(model, this));
		rosso.addActionListener(new MastermindController(model, this));
		nero.addActionListener(new MastermindController(model, this));

		giallo.setBackground(Color.yellow);
		arancione.setBackground(Color.orange);
		celeste.setBackground(Color.cyan);
		verde.setBackground(Color.green);
		rosso.setBackground(Color.red);
		nero.setBackground(Color.black);

		mostraColoreAtt.setEnabled(false);
		mostraColoreAtt.setBackground(Color.yellow); //di default, il colore preimpostato è giallo
		box_colori.add(mostraColoreAtt);

		JLabel spessore = new JLabel("***********************");
		box_colori.add(spessore);
		box_colori.add(giallo);
		box_colori.add(arancione);
		box_colori.add(celeste);
		box_colori.add(verde);
		box_colori.add(rosso);
		box_colori.add(nero);
		/*--FINE GRIGLIA COLORI--*/

		JPanel tabellone = new JPanel(); //Creo un JPanel in cui inserisco tutte le caselle di gioco (come JButton)
		tabellone.setPreferredSize(new Dimension(200,400));

		for (int rig = 0; rig < 8; rig++){
			tabellone.add(new JLabel(""+(1+rig)));
			for (int col = 0; col < 4; col++){
				JButton casella = new JButton();
				casella.setActionCommand(""+col);
				casella.addActionListener(new MastermindController(model, this));
				casella.setPreferredSize(new Dimension(40,40));
				casella.setBackground(Color.gray); //Il grigio è il colore predefinito all'inizio del gioco
				tab_array[rig][col] = casella;
				tabellone.add(tab_array[rig][col]);
			}
		}

		/*--ZONA SUGGERIMENTI--*/
		JPanel suggerimenti_panel = new JPanel(); //Stesso procedimento fatto per il tabellone
		for (int rig = 0; rig < 8; rig++){
			for (int col = 0; col < 4; col++){
				JButton casellaEsito = new JButton();
				casellaEsito.setPreferredSize(new Dimension(25,17));
				casellaEsito.setEnabled(false);
				casellaEsito.setBackground(Color.gray); 
				esito[rig][col] = casellaEsito;
				suggerimenti_panel.add(esito[rig][col]);
			}

		}

		pane.add(box_colori, BorderLayout.EAST);			//aggiungo la griglia colori a EAST
		pane.add(tabellone, BorderLayout.WEST);				//aggiungo il tabellone a WEST
		pane.add(suggerimenti_panel, BorderLayout.CENTER); 	//aggiungo la zona suggerimenti nel CENTER
		getContentPane().add(pane); 						//aggiungo il pannello al mio frame
		setResizable(false);
		setVisible(true);
	}
}
