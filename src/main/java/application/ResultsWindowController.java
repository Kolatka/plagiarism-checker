package application;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class ResultsWindowController implements Initializable {

	@FXML
	private Button statsButton;
	@FXML
	private Button sentencesButton;
	@FXML
	private TextArea resultsArea;
	@FXML 
	private BorderPane mainPane;

	private static String stats = "";
	private static String sentences = "";
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		resultsArea.appendText(stats);
	}
	
	@FXML
	private void handleButtonAction(ActionEvent event) {
		if(event.getSource()==statsButton){ 
			resultsArea.setText(stats);
	    }else if(event.getSource()==sentencesButton){ 
	    	resultsArea.setText(sentences);
	    }   
	}

	@FXML
	public void showStage(String stats, String sentences) throws IOException{
		this.stats = stats;
		this.sentences = sentences;
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/presentation/ResultsWindow.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Results");
		stage.setScene(scene);
		stage.showAndWait();
	}
	
	void print(String s, TextArea ta){
		Platform.runLater( () -> ta.appendText(s + "\n") );
	}

	public void showStage(Parent root, Stage stage){
		 Scene scene = new Scene(root);
		 stage.setScene(scene);
		 stage.show();
	}
		
}
