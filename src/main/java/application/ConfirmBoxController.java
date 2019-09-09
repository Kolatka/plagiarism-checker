package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConfirmBoxController implements Initializable {

	@FXML
	private Button yesButton;
	@FXML
	private Button noButton;
	@FXML 
	private BorderPane mainPane;
	@FXML
	private Label textLabel;
	
	static int result = -1;
	static String text = "";
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		textLabel.setText(text);
	}
	
	@FXML
	private void handleButtonAction(ActionEvent event) throws IOException{
		Stage stage = (Stage) mainPane.getScene().getWindow();

		if(event.getSource()==yesButton){ 
			result = 1;
			stage.close();
	    }else if(event.getSource()==noButton){     
	    	result = 0;
	    	stage.close();
	    }

	}

	@FXML
	public int showStage(String message, String title) throws IOException{
		this.text = message;
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/presentation/ConfirmBox.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle(title);
		stage.setScene(scene);
		stage.showAndWait();
		return this.result;
	}
}
