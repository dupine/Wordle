package it.edu.iisgubbio.terzo.wordle;

import java.io.FileWriter;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioButton;

public class Impostazioni {
	@FXML
	RadioButton rModFacile, rModDifficile;
	
	public void initialize() {
		if(WordleIta.rSelected) {
			rModFacile.setSelected(true);
		} else {
			rModDifficile.setSelected(true);
		}
	}
	
	public boolean isFacile() {
		return rModFacile.isSelected();
	}
	
	public void reset() throws IOException {
		FileWriter scrittura = new FileWriter("src/it/edu/iisgubbio/terzo/wordle/gameData/conteggioVittorieESconfitte.txt");
		scrittura.write("vittorie: 0" + "\n" + "scofnitte: 0");
		scrittura.close();
	}
	
	public void info() {
		Alert sconfitta = new Alert(AlertType.INFORMATION, "Come giocare:\n"
				+ "Il gioco consiste nel trovare la parola segreta utilizzando al massimo 6 tentativi, ad ogni tentativo le lettere inserite si coloreranno:"
				+ "\nGIALLO: lettera contenuta nella parola segreta ma non nella posizione giusta;"
				+ "\nNERO: lettere non contenuta nella parola segreta;"
				+ "\nVERDE: lettera contenuta nella parola e inserita nella posizione giusta."
				+ "\nInserisci le lettere utilizzando la tastiera, se vuoi cambiare una delle lettere inserite basta utilizzare BACK SPACE che ti permette di tornare indietro.\nSe arrivi alla fine della parola(5 lettere) premi INVIO, se non succede niente significa che hai inserito una parola insesistente.\nLa migliore parola per iniziare è SARTI.");
		sconfitta.setTitle("INFO WORDLE");
		sconfitta.setHeaderText("INFO");
		sconfitta.showAndWait();
	}
}