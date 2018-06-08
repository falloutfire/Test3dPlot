package sample.FXMLControllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import sample.Main;

public class RootLayoutController {

    private Main main;

    public void handleClear(ActionEvent actionEvent) {
        main.getMainController().clearFields();
    }

    public void handleClose(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void handleAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Optimizatior");
        alert.setHeaderText("О программе");
        alert.setContentText("Автор: Лихачев Илья\nГруппа 455");

        alert.showAndWait();
    }

    public void setMainApp(Main main) {
        this.main = main;
    }
}
