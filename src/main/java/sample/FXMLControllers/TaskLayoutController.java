package sample.FXMLControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Entities.Task;
import sample.Main;

import java.io.InputStream;
import java.util.Scanner;

public class TaskLayoutController {

    public TableView taskView;
    public TableColumn taskColumn;
    public TextArea taskAreaField;
    private ObservableList<Task> tasks;
    private Stage dialogStage;
    private boolean okClicked;
    private Main main;
    private Task task;

    public TaskLayoutController() {
    }

    public void initialize() {
        tasks = FXCollections.observableArrayList();
        addTasks(tasks);
        taskColumn.setCellValueFactory(new PropertyValueFactory<>("nameTask"));
        taskView.setItems(tasks);
    }

    private void addTasks(ObservableList<Task> tasks) {
        tasks.add(new Task("Расчет минимальной\nсебестоимости фильтрата", "sample/Entities/Task1/full.txt",
                "sample/Entities/Task1/formal.txt", "Минимальная себестоимость фильтрата\nза смену равна ", " y.e. и достигается \nпри температурах на перегородках Т1 и Т2\nравных",
                -5, 0, -1, 5, 0.5f, 1, 1, "(x^2 + y - 11)^2 + (x + y^2 - 7)^2", 80, true));
        tasks.add(new Task("Расчет минимальных\nзатрат на очистку", "sample/Entities/Task2/full.txt",
                "sample/Entities/Task2/formal.txt", "Минимальная затраты на фильтрацию ", "рмрпморпм",
                -3, 3, -3, 6, -1, 1, 1, "2000*((x-y)^2 + (1-y)^2)", 100, false));
    }

    public void onClickChoose(ActionEvent actionEvent) {
        if(task != null){
            okClicked = true;
            main.getMainController().setTask(task);
            dialogStage.close();
        } else {
            main.getAlert("Не выбрана задача!", "Пожалуйста, выберете задачу!");
        }
    }

    public void onClickExit(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void onClickSelect(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            String result;
            final InputStream resourceF = this.getClass().getClassLoader().getResourceAsStream(((Task) taskView.getSelectionModel().getSelectedItem()).getPathFull());
            try (Scanner s = new Scanner(resourceF, "UTF-8").useDelimiter("\\A")) {
                result = s.hasNext() ? s.next() : "";
            }
            task = (Task) taskView.getSelectionModel().getSelectedItem();
            taskAreaField.setText(result);
        } else if(mouseEvent.getClickCount() == 1){
            task = (Task) taskView.getSelectionModel().getSelectedItem();
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
