'''
	Player MP3 - @author Davide Galante

	Player MP3 realizzato in Python con l'ausilio delle librerie
	Tkinter, Pygame , Random e Os.

'''
from random import randint
from tkinter import *
from tkinter import messagebox
from tkinter import filedialog
from pygame import *
from pygame.locals import *
import os
from os import listdir

def splash():
	'''
	Creo la 'schermata splash', ovvero quella
	che compare inizialmente e che scompare dopo pochi secondi
	'''
	splash = Tk()
	splash.config(border=0)
	centra_finestra(splash)

	altezza=splash.winfo_screenheight()
	larghezza=splash.winfo_screenwidth()

	spotify = "img\\icone\\logoSplash.png"

	img = PhotoImage(file=spotify)
	canvas = Canvas(splash, height=altezza*0.8, width=larghezza*0.8, bg="black",highlightthickness=0) #definisco la dimensione del canvas tale per cui stia nel mezzo della schermata
	canvas.create_image(larghezza*0.8/2, altezza*0.8/2, image=img) #definisco la dimensione dell'immagine tale per cui stia nel mezzo del canvas (quindi divido l'altezza e la lunghezza per due)
	canvas.pack()

	splash.after(2000, splash.destroy)
	splash.mainloop()

def centra_finestra(schermata):
	'''
	Funzione che centra la schermata
	'''
	schermata.overrideredirect(True) #per togliere i bordi esterni alla schermata
	larghezza = schermata.winfo_screenwidth()  # larghezza schermo in pixel
	altezza = schermata.winfo_screenheight() # altezza schermo in pixel
	schermata.geometry('%dx%d+%d+%d' % (larghezza*0.8, altezza*0.8, larghezza*0.1, altezza*0.1)) #definisco la dimensione e la posizione della schermata tale per cui stia nel mezzo del monitor

def play():
	'''
	Fa partire la musica
	'''
	global paused,canzoneSelezionata
	
	if paused:
		mixer.music.unpause()
		paused = False
	else:
		try:
			canzoneSelezionata = canzoni.curselection()
			canzoneSelezionata = int(canzoneSelezionata[0])
			canzone = playlistCanzoni[canzoneSelezionata]
			mixer.music.load(canzone)
			visualizza_copertina(canzone)
			mixer.music.play() 
			while canzoneSelezionata <= len(playlistCanzoni):
				canzoneSelezionata += 1
				canzone = playlistCanzoni[canzoneSelezionata]
				mixer.music.queue(canzone)
		except:
			pass

def stop():
	'''
	Ferma la musica e la resetta
	'''
	mixer.music.stop()


def pausa():
	'''
	Stoppa la musica e riprende al 'play'
	'''
	global paused
	
	paused = True
	mixer.music.pause()


def precedente():
	'''
	Fa partire la canzone precedente a quella selezionata.
	Se si è alla prima canzone e si prova a premere questo bottone, si riceve un errore
	'''
	global canzoneSelezionata
	
	try: #Questo try serve per dare errore nel caso in cui l'utente prema il tasto quando ancora nessuna canzone è selezionata
		if canzoneSelezionata > 0:
			canzoneSelezionata -= 1
			canzone = playlistCanzoni[canzoneSelezionata]
			canzoni.selection_clear(0,END)
			canzoni.selection_set(canzoneSelezionata)
			mixer.music.load(canzone)
			visualizza_copertina(canzone)
			mixer.music.play(-1) #loop
		else:
			messagebox.showerror("Errore","PRIMA CANZONE DELLA PLAYLIST!!!!!")
	except:
		messagebox.showerror("Errore","Operazione non valida")


def successivo():
	'''
	Fa partire la canzone successiva a quella selezionata.
	Se si è all'ultima canzone e si prova a premere questo bottone, si riceve un errore
	'''
	global canzoneSelezionata
	
	try:	#Questo try serve per dare errore nel caso in cui l'utente prema il tasto quando ancora nessuna canzone è selezionata
		try:
			canzoneSelezionata += 1
			canzone = playlistCanzoni[canzoneSelezionata]
			canzoni.selection_clear(0,END)
			canzoni.selection_set(canzoneSelezionata)
			mixer.music.load(canzone)
			visualizza_copertina(canzone)
			mixer.music.play(-1) #loop
		except:
			canzoneSelezionata -= 1
			messagebox.showerror("Errore","ULTIMA CANZONE DELLA PLAYLIST!!!!!!")
	except:
		messagebox.showerror("Errore","Operazione non valida")


def shuffle():
	'''
	Fa partire una canzone casuale presente nella playlist
	'''
	global canzoneSelezionata
	
	massimo = len(playlistCanzoni)-1
	canzoneSelezionata = randint(0,massimo)
	canzone = playlistCanzoni[canzoneSelezionata]
	canzoni.selection_clear(0,END)
	canzoni.selection_set(canzoneSelezionata)
	mixer.music.load(canzone)
	visualizza_copertina(canzone)
	mixer.music.play(-1) #loop


def volume(val):
	'''
	Gestisco il volume (da 0 a 100)
	'''
	volume = int(val)/100
	mixer.music.set_volume(volume)


def informazioni():
	'''
	Mostro il messagebox con le informazioni riguardanti l'app
	'''
	messagebox.showinfo('MP3 Player','App Creata da Davide Galante, 3°A Informatica!')


def riempi_lista():
	'''
	Inizializza la lista riempendola con tutte le
	canzoni presenti nella cartella 'mp3'
	'''
	indice = 0 
	os.chdir('mp3') #sposto temporaneamente la radice di lavoro in 'mp3'
	lista_canzoni = listdir('.') #lista contenente tutti i file nella cartella mp3
	for song in lista_canzoni:
		percorso_canzone = os.path.abspath(song) #metto in una variabile il percorso assoluto della canzone
		canzoni.insert(indice,song[:-4]) #nella listbox inserisco solo il nome della canzone
		playlistCanzoni.insert(indice,percorso_canzone) #nella playlist inserisco invece il percorso
		indice += 1
	os.chdir('..') #reimposto la radice di valoro sulla cartella pricipale


def nuovaImmagine():
	'''
	Consente di aggiungere un'immagine alla cartella contenente tutte
	le copertine associate alle canzoni.
	'''
	percorsoImmagine = filedialog.askopenfilename(initialdir=os.getcwd()) #impongo come cartella di default, quella dove sto lavorando
	if percorsoImmagine[-4:] == '.png':
		nomeImmagine = os.path.basename(percorsoImmagine)
		percorsoNuovo = 'img\\copertine\\'+nomeImmagine
		os.rename(percorsoImmagine,percorsoNuovo)
	else:
		messagebox.showerror("Errore","File con estesione non valida")


def togliImmagine():
	'''
	Permette di togliere una copertina dalla cartella contenente tutte
	le suddette copertine.
	'''
	percorsoImmagine = filedialog.askopenfilename(initialdir='img\\copertine') #impongo come cartella di default, quella dove ci sono le copertine
	if percorsoImmagine[-4:] == '.png':
		nomeImmagine = os.path.basename(percorsoImmagine)
		percorsoNuovo = 'img\\cestino_copertine\\'+nomeImmagine
		os.rename(percorsoImmagine,percorsoNuovo)
	else:
		messagebox.showerror("Errore","File con estesione non valida")


def cercaCanzone():
	'''
	Seleziono una canzone dal computer e ne trovo/visualizzo 
	la sua copertina tramite la funzione apposita
	'''
	global percorsoFile,icona
	
	percorsoFile = filedialog.askopenfilename(initialdir=os.getcwd()) #impongo come cartella di default, quella dove sto lavorando
	if percorsoFile[-4:] == '.mp3':
		aggiungiCanzone(percorsoFile)
	else:
		messagebox.showerror("Errore","File con estesione non valida")


def aggiungiCanzone(nomeFile):
	'''
	Consenste all'utente di aggiungere una canzone al player musicale
	'''
	nomeFile = os.path.basename(nomeFile)[:-4] #Seleziono solo il nome della canzone, senza l'estensione
	indice = 0
	canzoni.insert(indice,nomeFile)
	playlistCanzoni.insert(indice,percorsoFile)


def togliCanzone():
	'''
	Consente all'utente di togliere una canzone al player musicale
	'''
	canzoneSelezionata = canzoni.curselection()
	canzoneSelezionata = int(canzoneSelezionata[0])
	playlistCanzoni.pop(canzoneSelezionata)
	canzoni.delete(canzoneSelezionata)


def visualizza_copertina(nome):
	'''
	Visualizza la copertina della canzone in riproduzione
	'''
	global icona
	
	icona.destroy()
	try:
		
		imgFile = os.path.basename(nome)[:-4] #Seleziono la parte di percorso corrispondente solo al nome della canzone
		immagineAssociata = 'img\\copertine\\'+imgFile+'.png' #creo il percorso dell'immagine corrispondente alla canzone
		copertina = PhotoImage(file = immagineAssociata) #inserisco l'immagine in una variabile tramite PhotoImage di Tkinter
		icona = Label(frameAltoDestro, image = copertina) #successivamente la inserisco in una label e la mostro.
		icona.copertina = copertina
		icona.config(background=colore, activebackground= colore, border=0)
		icona.pack()
	except:
		copertinaVuota = PhotoImage(file = 'img\\copertine\\sconosciuto.png')
		icona = Label(frameAltoDestro, image = copertinaVuota) #Metto come immagine iniziale dell'app un punto interrogativo, che poi verrà sostituito con le varie copertine dei brani
		icona.copertinaVuota = copertinaVuota
		icona.pack()
#------------------------------ Inizio Main ------------------------------------
playlistCanzoni=[]
paused = False
mixer.init()
colore = '#111111'
splash()
finestra = Tk()
centra_finestra(finestra)
finestra.configure(background=colore)
finestra.iconbitmap("img\\icone\\Spotify.ico")
finestra.resizable(0, 0)
finestra.title("Mp3 Player")


'''
	Finestra Principale = frameAlto,frameDestro,frameAltoDestro,frameSinistro
	FrameAlto = banner
	FrameSinistro = listbox playlist, bottoni per aggiunta e rimozione canzoni
	FrameDestro = copertina brano, bottoni play/pause/stop, volume
'''

#-------------------------Inizio Frame Alto---------------------------------------
frameAlto = Frame(finestra)
frameAlto.pack(side = TOP)
frameAlto.config(background=colore)

imgBanner = PhotoImage(file = 'img\\icone\\banner.png')
banner = Label(frameAlto, image = imgBanner)
banner.config(borderwidth = 0)
banner.pack()
#------------------------- Fine Frame Alto---------------------------------------

#-------------------------Inizio Frame Destra---------------------------------------
frameDestro = Frame(finestra)
frameDestro.config(background=colore)
frameDestro.pack(side = RIGHT,padx=100)
frameAltoDestro = Frame(frameDestro)
frameAltoDestro.config(background=colore)
frameAltoDestro.pack(side = TOP)
btnFrame = Frame(frameDestro)
btnFrame.config(background=colore,borderwidth = 0)
btnFrame.pack(pady=15)

copertinaVuota = PhotoImage(file = 'img\\copertine\\sconosciuto.png')
icona = Label(frameAltoDestro, image = copertinaVuota) #Metto come immagine iniziale dell'app un punto interrogativo, che poi verrà sostituito con le varie copertine dei brani
icona.config(background=colore, activebackground = colore, border=0)
icona.pack()

#	Creo dei bottoni per far: partire, stoppare, mettere in pausa la musica,
	#far partire la canzone precedente e successiva e infine il bottone per 
#	la riproduziona casuale. 

imgPre = PhotoImage(file = 'img\\icone\\precedente.png')
btnPre = Button(btnFrame,image = imgPre, command = precedente)
btnPre.config(borderwidth = 0,activebackground= colore)
btnPre.grid(row=0, column=0, padx=10)

imgStop = PhotoImage(file = 'img\\icone\\stop.png')
btnStop = Button(btnFrame,image = imgStop, command = stop)
btnStop.config(borderwidth = 0,activebackground= colore)
btnStop.grid(row=0, column=1, padx=10)

imgPlay = PhotoImage(file = 'img\\icone\\play.png')
btnPlay = Button(btnFrame, image = imgPlay, command = play)
btnPlay.config(borderwidth = 0,activebackground= colore)
btnPlay.grid(row=0, column=2, padx=10)

imgPause = PhotoImage(file = 'img\\icone\\pause.png')
btnPausa = Button(btnFrame,image = imgPause, command = pausa)
btnPausa.config(borderwidth = 0,activebackground= colore)
btnPausa.grid(row=0, column=3, padx=10)

imgSucc = PhotoImage(file = 'img\\icone\\successivo.png')
btnSucc = Button(btnFrame,image = imgSucc, command = successivo)
btnSucc.config(borderwidth = 0,activebackground= colore)
btnSucc.grid(row=0, column=4, padx=10)

imgShuffle = PhotoImage(file = 'img\\icone\\shuffle.png')
btnShuffle= Button(btnFrame,image = imgShuffle, command = shuffle)
btnShuffle.config(borderwidth = 0, background=colore, activebackground= colore, border=0)
btnShuffle.grid(row=2, columnspan = 5, pady = 20)

#Gestisco il volume tramite il metodo Scale di Tkinter
volume = Scale(btnFrame,orient= "horizontal", command = volume)
volume.set(50)
volume.config(activebackground= colore,background=colore, highlightbackground=colore, showvalue=0) #highlightbackground cambia il colore del bordo per renderlo uguale allo sfondo
volume.grid(row=3, columnspan = 5)
#------------------------- Fine Frame Destra---------------------------------------


#-------------------------Inizio Frame Sinistra---------------------------------------
frameSinistro = Frame(finestra)
frameSinistro.pack(side = TOP, padx=100, pady = 60)
frameSinistro.config(background=colore)
canzoni = Listbox(frameSinistro)
canzoni.config(background=colore,selectbackground=colore,fg='#1DB354',width=60,highlightcolor=colore,highlightbackground=colore,borderwidth=0,justify=CENTER,activestyle='none')
riempi_lista()
canzoni.pack()

btnleftFrame = Frame(frameSinistro)
btnleftFrame.config(background=colore,borderwidth = 0)
btnleftFrame.pack(pady=15)



aggCanz = Label(btnleftFrame, text = 'Aggiungi o Rimuovi Canzoni: ')
aggCanz.config(background = colore,fg='#FF9908')
aggCanz.grid(row=0, columnspan = 2, padx=10)

imgAdd = PhotoImage(file = 'img\\icone\\add.png')
imgDel = PhotoImage(file = 'img\\icone\\del.png')

btnAggCanz = Button(btnleftFrame,borderwidth = 0,image=imgAdd,command=cercaCanzone)
btnAggCanz.grid(row=1, column=0, padx=10, pady=10)

btnDelCanz = Button(btnleftFrame,borderwidth = 0,image=imgDel,command = togliCanzone)
btnDelCanz.grid(row=1, column=1, padx=10)

aggImm = Label(btnleftFrame, text = 'Aggiungi o Rimuovi Copertine: ')
aggImm.config(background = colore,fg='#FF9908')
aggImm.grid(row=2, columnspan = 2, padx=10)

btnAggImm = Button(btnleftFrame,borderwidth = 0,image=imgAdd,command=nuovaImmagine)
btnAggImm.grid(row=3, column=0, padx=10, pady = 10)

btnDelCanz = Button(btnleftFrame,borderwidth = 0,image=imgDel,command=togliImmagine)
btnDelCanz.grid(row=3, column=1, padx=10)
#------------------------- Fine Frame Sinistra---------------------------------------


#creo menu a tendina
menuTendina = Menu(finestra)
finestra.config(menu=menuTendina)

#creo sottomenu per i file
menuFile = Menu(menuTendina, tearoff = 0)	#tearoff=0 toglie il tratteggio
menuTendina.add_cascade(label = "File", menu = menuFile)
canzone = menuFile.add_command(label='Apri nuovo file audio',command = cercaCanzone)
immagine = menuFile.add_command(label='Inserisci nuova immagine',command = nuovaImmagine)

#creo sottomenu per le informazioni dell'app
menuInfo = Menu(menuTendina, tearoff = 0)
menuTendina.add_cascade(label = "Informazioni", menu = menuInfo)
menuInfo.add_command(label="Informazioni sull'app", command = informazioni)

#creo sottomenu per le impostazioni dell'app
menuImpostazioni = Menu(menuTendina, tearoff = 0)	#tearoff=0 toglie il tratteggio
menuTendina.add_cascade(label = "Impostazioni", menu = menuImpostazioni)
menuImpostazioni.add_command(label='Esci', command = finestra.destroy)

#loop per far funzionare il tutto
finestra.mainloop()

