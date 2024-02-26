package it.edu.iisgubbio.terzo.wordle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WordleIta extends Application {
	String vParole[] = new String[5], guessableWords[], vRightWord[], existingWord[] = new String[9423];;
	TextField[][] caselle = new TextField[6][5];
	String guess, color, rightWord, testoTF4 = "", lastAlfLetter = "",
			path = "src/it/edu/iisgubbio/terzo/wordle/parole/tutteLeParoleFacile.txt";
	TextField actual, actualTF;
	boolean parolaEsistente;
	static boolean rSelected = true;
	int HB = 0, TF = 0, actualHB, nParole = 1501;

	@FXML
	BorderPane borderPane;
	@FXML
	VBox vBox;
	@FXML
	Label lNumeroVittorie, lNumeroSconfitte;

	public void initialize() {
		
		creazioneTF();
		
		try {
			conteggioVS(2);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		(caselle[0][0]).setFocusTraversable(true);
		try {
			letturaFileParole();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start(Stage finestra) throws IOException {

		Scene scena = new Scene(FXMLLoader.load(WordleIta.class.getResource("WordleFxml.fxml")));

		finestra.setResizable(false);
		finestra.setScene(scena);
		finestra.setTitle("WORDLE");
		finestra.show();
		
		Alert sconfitta = new Alert(AlertType.INFORMATION, "Come giocare:\n"
				+ "Il gioco consiste nel trovare la parola segreta utilizzando al massimo 6 tentativi, ad ogni tentativo le lettere inserite si coloreranno:"
				+ "\nGIALLO: lettera contenuta nella parola segreta ma non nella posizione giusta;"
				+ "\nNERO: lettera non contenuta nella parola segreta;"
				+ "\nVERDE: lettera contenuta nella parola e inserita nella posizione giusta."
				+ "\nInserisci le lettere utilizzando la tastiera, se vuoi cambiare una delle lettere inserite basta utilizzare BACKSPACE che ti permetterà di tornare indietro.\nSe arrivi alla fine della parola(5 lettere) premi INVIO, se non succede niente significa che hai inserito una parola insesistente.\nLa migliore parola per iniziare è SARTI.");
		sconfitta.setTitle("INFO WORDLE");
		sconfitta.setHeaderText("INFO");
		sconfitta.showAndWait();
	}

	public void creazioneTF(){
		for (int i = 0; i < 6; i++) {
			HBox d = (HBox) vBox.getChildren().get(i);
			for (int g = 0; g < 5; g++) {
				caselle[i][g] = actualTF = (TextField) d.getChildren().get(g);
				if (g == 0 && i == 0) {
					actualTF.setDisable(false);
				} else {
					actualTF.setDisable(true);
				}
				actualTF.setText("");
				actualTF.setId(null);
			}
		}
	}
	
	// reset
	public void reset() {
		rightWord = guessableWords[(int) (Math.random() * nParole)];
		vRightWord = rightWord.split("");
		creazioneTF();
		parolaEsistente = false;
		HB = TF = 0;
	}

	public void resa() {
		Alert vittoria = new Alert(AlertType.CONFIRMATION, "sei sicuro di volrti arrende?");
		vittoria.setTitle("!");
		vittoria.setHeaderText("AVVISO");
		vittoria.showAndWait();
		if (vittoria.getResult().getText().equals("OK")) {
			sconfitta();
		} else {
			vittoria.close();
		}
	}

	public void vittoria() {		
		try {
			conteggioVS(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Alert vittoria = new Alert(AlertType.INFORMATION, "la parola era: " + rightWord);
		vittoria.setTitle("HAI VINTO!");
		vittoria.setHeaderText("VITTORIA!");
		vittoria.showAndWait();
		
		reset();
	}

	public void sconfitta() {		
		try {
			conteggioVS(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Alert sconfitta = new Alert(AlertType.INFORMATION, "la parola era: " + rightWord);
		sconfitta.setTitle("HAI PERSO!");
		sconfitta.setHeaderText("SCONFITTA!");
		sconfitta.showAndWait();

		reset();
	}

	public void conteggioVS(int x) throws IOException {
		FileReader lettura = new FileReader("src/it/edu/iisgubbio/terzo/wordle/gameData/conteggioVittorieESconfitte.txt");
		BufferedReader lettoreDiRighe = new BufferedReader(lettura);
		String vittoria = lettoreDiRighe.readLine();
		String sconfitta = lettoreDiRighe.readLine();
		lettoreDiRighe.close();
		lettura.close();

		String vittoriaV[] = vittoria.split(" "), sconfittaV[] = sconfitta.split(" ");

		FileWriter scrittura = new FileWriter("src/it/edu/iisgubbio/terzo/wordle/gameData/conteggioVittorieESconfitte.txt");
		if (x == 0) {
			vittoriaV[1] = "" + (Integer.parseInt(vittoriaV[1]) + 1);
			vittoria = vittoriaV[0] + " " + vittoriaV[1];
		} else if (x == 1) {
			sconfittaV[1] = "" + (Integer.parseInt(sconfittaV[1]) + 1);
			sconfitta = sconfittaV[0] + " " + sconfittaV[1];

		}
		lNumeroVittorie.setText(vittoriaV[1]);
		lNumeroSconfitte.setText(sconfittaV[1]);

		scrittura.write(vittoria + "\n" + sconfitta);
		scrittura.close();
	}

	public void letturaFileParole() throws IOException {
		guessableWords = new String[nParole];
		FileReader flussoCaratteri = new FileReader(path);
		BufferedReader lettoreDiRighe = new BufferedReader(flussoCaratteri);
		
		for (int c = 0; c < nParole; c++) {
			guessableWords[c] = lettoreDiRighe.readLine();
			
		}
		lettoreDiRighe.close();
		flussoCaratteri.close();
		rightWord = guessableWords[(int) (Math.random() * nParole)];
		vRightWord = rightWord.split("");
	}

	// quando viene premuto un tasto e quel tasto è una lettera alfabetica si scorre
	// di un TF a destra e la lettera viene inserita nel vettore della parola finale
	public void inserimentoCaratteri(KeyEvent event) {
		actual = caselle[HB][TF];
		if (actual.getText().matches("[A-Z]+")) {
			// se il caps lock è attivo
			actual.setText("");
			Alert maiusc = new Alert(AlertType.WARNING, "Probabilmente il blocco maiuscolo � attivo!");
			maiusc.setTitle("ERRORE");
			maiusc.setHeaderText("ATTENZIONE!");
			maiusc.showAndWait();

		} else if (actual.getText().matches("[a-z]+")) {
			// faccio questo cosicché se inserisco un carattere non alfabetico mi inserisce l'ultimo alfabetico inserito
			lastAlfLetter = (String) event.getCharacter();
			if (TF != 4) {
				vParole[TF++] = actual.getText();
				(caselle[HB][TF]).setDisable(false);
				actual.setDisable(true);
				lastAlfLetter = "";
			}
			if (actual.getText().length() > 1) {
				actual.setText(event.getCharacter());
			}
		} else {
			actual.setText(lastAlfLetter);
		}
	}

	// quando si scrive una parola completa(5 lettere) e si preme INVIO viene
	// chiamato il metodo che controlla l'esistenza della parola e poi wordle()
	public void parolaCompleta(KeyEvent event) {
		// l'input arriva solo dagli ultimi tf di ogni HBox
		actual = caselle[HB][TF];
		actualHB = HB;
		if (event.getCode() == KeyCode.ENTER) {
			lastAlfLetter = "";
			vParole[4] = actual.getText();
			parolaEsistente();
			if (HB != 5 && parolaEsistente) {
				(caselle[++HB][TF = 0]).setDisable(false);
				actual.setDisable(true);
				wordle();
			} else if (parolaEsistente) {
				wordle();
				actual.setDisable(true);
				if (!color.equals("ggggg")) {
					sconfitta();
				}
			}
		} else if (event.getCode() == KeyCode.BACK_SPACE) {
			cancel(event);
		}
	}

	public void cancel(KeyEvent event) {
		if (event.getCode() == KeyCode.BACK_SPACE) {
			lastAlfLetter = "";
			actual = caselle[HB][TF];
			if (TF != 0) {
				(caselle[HB][--TF]).setDisable(false);
				(caselle[HB][TF]).setText("");
				(caselle[HB][TF]).setFocusTraversable(true);
				vParole[TF + 1] = "";
				if (TF + 1 == 4) {
					actual.setText("");
				}
				actual.setDisable(true);
			}
		} else if(event.getCode() == KeyCode.ENTER) {
			Alert enterNonAl4 = new Alert(AlertType.WARNING, "la parola non � completa!");
			enterNonAl4.setTitle("ERRORE");
			enterNonAl4.setHeaderText("ATTENZIONE!");
			enterNonAl4.showAndWait();
		}
	}


	// controlla la parola inserita se è presente nel file delle parole inseribili
	public void parolaEsistente() {
		parolaEsistente = false;
		guess = "";
			
		for (int i = 0; i < 5; i++) {
			guess = guess + vParole[i];
		}
		
		try {
		FileReader flussoCaratteri = new FileReader("src/it/edu/iisgubbio/terzo/wordle/parole/tutteLeParoleInseribili.txt");
		BufferedReader lettoreDiRighe = new BufferedReader(flussoCaratteri);
		for (int c = 0; c < 9423 && !parolaEsistente; c++) {
			if(guess.equals(""+lettoreDiRighe.readLine())) {
				parolaEsistente = !parolaEsistente;
			}
		}
		lettoreDiRighe.close();
		flussoCaratteri.close();
		
		} catch (Exception e) {
			System.out.println("error: "+e);
		}
		
		if (!parolaEsistente) {
			Alert parolaInestistente = new Alert(AlertType.WARNING, "prova ad inserire un'altra parola");
			parolaInestistente.setTitle("ATTENZIONE");
			parolaInestistente.setHeaderText("PAROLA INESISTENTE");
			parolaInestistente.showAndWait();
		}
	}

	// metodo che controlla la parola inserite e setta l'ID ai TextField del colore opportuno
	public void wordle() {
		color = "";
		TextField tf;
		String lettereTrovate[] = new String[5];
		for (int k = 0; k < 5; k++) {
			if (vParole[k].equals(vRightWord[k])) {
				lettereTrovate[k] = "g";
				caselle[actualHB][k].setId("green");
			} else {
				lettereTrovate[k] = "b";
			}
		}
		
		for (int k = 0; k < 5; k++) {
			tf = caselle[actualHB][k];
			int cYB = 0;
			 
			for (int c = 0; c < 5 && !(lettereTrovate[k].equals("g")); c++) {
				if (vParole[k].equals(vRightWord[c]) && k != c && !(lettereTrovate[c].equals("g"))) {
					cYB++;
				}
			}

			if (cYB != 0) {
				lettereTrovate[k] = "y";		
				tf.setId("yellow");
			} else if (!(lettereTrovate[k].equals("g"))) {
				tf.setId("black");
			}
		}
		
		for (int i = 0; i < 5; i++) {
			color = color + lettereTrovate[i];
		}

		if (color.equals("ggggg")) {
			(caselle[HB][TF]).setDisable(true);
			vittoria();
		}
	}

	// apertura seconda finestra "Impostazioni"
	public void impostazioni() throws IOException {

		Stage scenaT = new Stage();
		Scene scena;

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Impostazioni.class.getResource("ImpostazioniFXML.fxml"));
			scena = new Scene(loader.load(getClass().getResource("ImpostazioniFXML.fxml").openStream()));
			Impostazioni imp = (Impostazioni) loader.getController();
			scenaT.setScene(scena);
			scenaT.setResizable(false);
			scenaT.setTitle("IMPOSTAZIONI");
			scenaT.showAndWait();

			if (imp.isFacile()) {
				path = "src/it/edu/iisgubbio/terzo/wordle/parole/tutteLeParoleFacile.txt";
				nParole = 1501;
				rSelected = true;
			} else {
				path = "src/it/edu/iisgubbio/terzo/wordle/parole/tutteLeParoleDifficile.txt";
				nParole = 7843;
				rSelected = false;
			}

			initialize();

		} catch (Exception e) {
			System.out.println("errore: " + e);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}