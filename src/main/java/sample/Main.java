package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.FXMLControllers.MainController;
import sample.FXMLControllers.RootLayoutController;
import sample.FXMLControllers.TaskLayoutController;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private MainController mainController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Оптимизация");
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        initRootLayout();
        showLayout();
        getPrimaryStage().getScene().getStylesheets().add(Main.class.getResource("View/Style.css").toExternalForm());

    }

    private void showLayout() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("View/Main.fxml"));
            AnchorPane lruPane = loader.load();

            rootLayout.setCenter(lruPane);

            mainController = loader.getController();
            mainController.setMain(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initRootLayout() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("View/RootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        RootLayoutController rootLayoutController = loader.getController();
        rootLayoutController.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }

    public MainController getMainController(){
        return mainController;
    }

    public boolean showTaskDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("View/TaskLayout.fxml"));
            AnchorPane pane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Выбор задачи");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);
            scene.getStylesheets().add(Main.class.getResource("View/Style.css").toExternalForm());

            TaskLayoutController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMain(this);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void getAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
