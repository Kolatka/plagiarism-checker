package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.SolverService;


public class MainWindowController implements Initializable {
	
	@FXML
	private Button searchButton;
	@FXML
	private Button parametersButton;
	@FXML
	private Button prepareButton;
	@FXML
	private Button textsButton;
	@FXML
	private Button secondTextButton;
	@FXML
	private Button saveParametersButton;
	@FXML
	private Button compareButton;
	@FXML
	private TextArea textArea;
	@FXML
	private TextArea textArea2;
	@FXML 
	private BorderPane mainPane;
	@FXML 
	private GridPane textPane;
	@FXML 
	private GridPane textPane2;
	@FXML 
	private GridPane parametersPane;
	@FXML 
	private GridPane slidePane;
	@FXML
	private CheckBox isSortingWordsCheckBox;
	@FXML
	private CheckBox isSortingSentencesCheckBox;
	@FXML
	private CheckBox isUsingFlexionsCheckBox;
	@FXML
	private CheckBox isUsingWordTypesCheckBox;
	@FXML
	private CheckBox isCheckingSynonymsCheckBox;
	@FXML
	private Pane pane1;
	@FXML
	private Pane pane2;
	private Stage stage;

	private Boolean isParametersClicked=false;
	private Boolean isSecondTextClicked=false;

	private SolverService solverService;
	
	
	public void initialize(URL location, ResourceBundle resources) {
		compareButton.setDisable(true);
		solverService = new SolverService();
		textArea.setWrapText(true);
		textArea2.setWrapText(true);
		changeParametersVisibility(false);
		changeSecondTextVisiblity(0.0);
		eventHandlers();

	}

	private String readFile(String path) throws IOException{
		  byte[] encoded = Files.readAllBytes(Paths.get(path));
		  return new String(encoded, StandardCharsets.UTF_8);
	}
	
	@FXML
	private void handleButtonAction(ActionEvent event) throws IOException {
		ConfirmBoxController cbc = new ConfirmBoxController();
		stage = (Stage) mainPane.getScene().getWindow();
	    if(event.getSource()==searchButton){   
	    	FileChooser fileChooser = new FileChooser();
	    	fileChooser.setTitle("Choose a file");
	    	File file = fileChooser.showOpenDialog(stage);
	    	if(file!=null) {
	    		String str = readFile(file.getAbsolutePath());
		    	if(secondTextAreaClicked && textArea2.isVisible()) textArea2.setText(str);
		    	else textArea.setText(str);
	    	}

		}else if(event.getSource()==parametersButton){  
			textPane.setHalignment(textPane, HPos.RIGHT);
			if(!isParametersClicked){
				Timeline timeline = new Timeline(
						new KeyFrame(Duration.ZERO, new KeyValue(textPane.maxWidthProperty(), textPane.getWidth())),
		    	        new KeyFrame(Duration.millis(300), new KeyValue(textPane.maxWidthProperty(), textPane.getWidth() - 180))
				);
		    	timeline.play();
		    	timeline.setOnFinished(e -> changeParametersVisibility(true));
			}else {
				Timeline timeline = new Timeline(
						new KeyFrame(Duration.ZERO, new KeyValue(textPane.maxWidthProperty(), textPane.getWidth())),
		    	        new KeyFrame(Duration.millis(1), new KeyValue(textPane.maxWidthProperty(), textPane.getWidth() + 180))
				);
		    	timeline.play();
		    	timeline.setOnFinished(e -> changeParametersVisibility(false));
			}
	    	
		}else if(event.getSource()==prepareButton){  
			if(cbc.showStage("Preparing can take a while. Are you sure?","Confirmation") == 1) {
				prepareButton();
			}
		}else if(event.getSource()==textsButton){   
			
			
			
		}else if(event.getSource()==secondTextButton){   
			textPane2.setHalignment(textPane, HPos.LEFT);
			if(!isSecondTextClicked){
				secondTextAreaClicked = true;
				Timeline timeline = new Timeline(
						new KeyFrame(Duration.ZERO, new KeyValue(textPane.maxWidthProperty(), textPane.getWidth())),
		    	        new KeyFrame(Duration.millis(500), new KeyValue(textPane.maxWidthProperty(),slidePane.getWidth()/2))
				);
		    	timeline.play();
		    	timeline.setOnFinished(e -> changeSecondTextVisiblity(slidePane.getWidth()));
			}else {
				Timeline timeline = new Timeline(
						new KeyFrame(Duration.ZERO, new KeyValue(textPane.maxWidthProperty(), textPane.getWidth())),
		    	        new KeyFrame(Duration.millis(1), new KeyValue(textPane.maxWidthProperty(),textPane.getWidth() + textPane2.getWidth()))
				);
		    	timeline.play();
		    	timeline.setOnFinished(e -> changeSecondTextVisiblity(0.0));
			}
		}else if(event.getSource()==saveParametersButton){ 
			solverService.loadParameters(isSortingWordsCheckBox.isSelected(), isSortingSentencesCheckBox.isSelected(), isUsingFlexionsCheckBox.isSelected(), isUsingWordTypesCheckBox.isSelected(), isCheckingSynonymsCheckBox.isSelected());
		}else if(event.getSource()==compareButton){ 
			if(cbc.showStage("Are you sure you want to compare texts?","Confirmation") == 1) {
				Task<Boolean> task = new Task<Boolean>() {
			        @Override
			        public Boolean call() {
						return solverService.compareTexts();
			        }
			    };
			    task.setOnSucceeded(e -> {
					try {
						ResultsWindowController resultsWindowController = new ResultsWindowController();
						resultsWindowController.showStage(solverService.getComparingStatistics(), solverService.getSimilarSentences());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			    });
			    new Thread(task).start();
			}
		}
	}
	
	private void prepareButton() {
		solverService.resetService();
		AlertBoxController alertBoxController = new AlertBoxController();
		Task<Boolean> task = new Task<Boolean>() {
	        @Override
	        public Boolean call() {
	        	if(textArea.getText().length()>0) {
	    			solverService.loadFirstText(textArea.getText());
	    			solverService.prepareText(0);
	    		}
	    		if(textArea2.getText().length()>0) {
	    			solverService.loadSecondText(textArea2.getText());
	    			solverService.prepareText(1);
	    		}
	            return true;
	        }
	    };
	    task.setOnSucceeded(e -> {
	    	try {
				alertBoxController.showStage("Success!", "Success");
				compareButton.setDisable(false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	    });
	    new Thread(task).start();
	}
	
	private void changeParametersVisibility(Boolean value) {
		if(value) {
			parametersPane.setMinWidth(100);
		}else {
			parametersPane.setMinWidth(0);
		}
		isSortingWordsCheckBox.setVisible(value);
		isSortingSentencesCheckBox.setVisible(value);
		isUsingFlexionsCheckBox.setVisible(value);
		isUsingWordTypesCheckBox.setVisible(value);
		isCheckingSynonymsCheckBox.setVisible(value);
		saveParametersButton.setVisible(value);
		pane1.setVisible(value);
		pane2.setVisible(value);
		isParametersClicked = value;
	}
	
	private void changeSecondTextVisiblity(Double width) {
		if(width>0){
			textPane2.setMinWidth(width/2);
			textArea2.setVisible(true);
			isSecondTextClicked = true;
		}else {
			textPane2.setMinWidth(width);
			textPane2.setMaxWidth(width);
			textArea2.setText("");
			textArea2.setVisible(false);
			isSecondTextClicked = false;
		}
	}
	
	
	void print(String s, TextArea ta){
		Platform.runLater( () -> ta.appendText(s + "\n") );
	}

	private Boolean secondTextAreaClicked=false;
	private void eventHandlers() {
		textArea.setOnMouseClicked(event -> {
		if (event.getButton().equals(MouseButton.PRIMARY)) {
						secondTextAreaClicked=false;
		}});
		textArea2.setOnMouseClicked(event -> {
		if (event.getButton().equals(MouseButton.PRIMARY)) {
						secondTextAreaClicked=true;
		}});
	}

	public void showStage(Parent root, Stage stage){
		 Scene scene = new Scene(root);
		 stage.setScene(scene);
		 stage.show();
	}
		
}
