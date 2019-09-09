package application;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * @author Kolatka
 *
 */
public class MyApp extends Application {
	
	private Stage stage;

	@Override
	public void start(Stage primaryStage) throws Exception {

		MainWindowController mwc = new MainWindowController();
		Parent root = FXMLLoader.load(getClass().getResource("/presentation/MainWindow.fxml"));
		mwc.showStage(root, primaryStage);
		primaryStage.setTitle("Plagiarism-Checker");
		primaryStage.setOnCloseRequest(e->{
			e.consume();	
			primaryStage.close();
			Platform.exit();
		});
	}

	
	public static void main(String[] args) {
		launch(args);
	}

	
}

