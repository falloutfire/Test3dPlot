package sample.FXMLControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.colors.colormaps.ColorMapRainbowNoBorder;
import org.jzy3d.contour.DefaultContourColoringPolicy;
import org.jzy3d.contour.MapperContourPictureGenerator;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import sample.FXMLControllers.CalculateGraph2d;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainController {

    @FXML
    private ComboBox taskBox;
    @FXML
    private ComboBox methodBox;
    @FXML
    private TextArea mathModelArea;
    @FXML
    private Label minOut;
    @FXML
    private Label minV;
    @FXML
    private TextField paramX;
    @FXML
    private TextField paramY;
    @FXML
    private TextField paramAnswer;
    @FXML
    private AnchorPane Pane3d;
    @FXML
    private AnchorPane anchor2dPane;
    @FXML
    private TextField xMinField;
    @FXML
    private TextField xMaxField;
    @FXML
    private TextField yMinField;
    @FXML
    private TextField yMaxField;
    @FXML
    private TextField scanField;

    private ImageView imageView;
    private ImageView imageView2d;
    private JavaFXChartFactory factory;
    private AWTChart chart;

    private ObservableList<String> methodsArray = FXCollections.observableArrayList();
    private ObservableList<String> taskArray = FXCollections.observableArrayList();

    private boolean isTrue;

    public MainController() {
    }

    public void initialize() {
        addingsArrays();
        methodBox.setItems(methodsArray);
        taskBox.setItems(taskArray);
        taskBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(taskArray.get(0))){
                xMinField.setText("-5");
                xMaxField.setText("0");
                yMinField.setText("-1");
                yMaxField.setText("5");
                paramX.setText("0.5");
                paramY.setText("1");
                paramAnswer.setText("1");
                scanField.setText("0.1");
                mathModelArea.setText("V(T1,T2)=α*G*(T1^2+β*T2-µ*∆ρ1)^N+γ*(β1*T1+T2^2-µ1*∆ρ2)^N\n" +
                        "Pr_смены(T1,T2)= t*Pr*V(T1,T2)\n" + "Pr_смены(T1,T2)→ min\n\n" +
                        "∆ρ1=11 кПа\n" + "∆ρ2=7 кПа\n" + "N=2 \n" + "G=1 м^3/ч\n" + "α, β, µ, γ, β1, µ1 = 1");
            }
        });

    }

    //очистка полей
    public void clearFields() {
        scanField.clear();
        xMaxField.clear();
        xMinField.clear();
        yMaxField.clear();
        yMinField.clear();
        paramY.clear();
        paramX.clear();
        paramAnswer.clear();
        anchor2dPane.getChildren().clear();
        Pane3d.getChildren().clear();
        minV.setText("Min значение объема фильтрата: ");
        minOut.setText("Min значение расхода: ");
    }

    //3д решение
    private AWTChart getDemoChart(JavaFXChartFactory factory, double xMin, double xMax, double yMin, double yMax) {
        // -------------------------------
        // Define a function to plot
        Mapper mapper = new Mapper() {
            @Override
            public double f(double x, double y) {
                return Math.pow(x * x + y - 11, 2) + Math.pow(x + y * y - 7, 2);
            }
        };

        // Define range and precision for the function to plot
        Range rangeX = new Range((float) xMin, (float) xMax);
        Range rangeY = new Range((float) yMin, (float) yMax);
        int steps = 80;

        // Create the object to represent the function over the given range.
        final Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(rangeX, steps, rangeY, steps), mapper);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(false);

        // -------------------------------
        // Create a chart
        Quality quality = Quality.Advanced;
        //quality.setSmoothPolygon(true);
        //quality.setAnimated(true);

        // let factory bind mouse and keyboard controllers to JavaFX node
        AWTChart chart = (AWTChart) factory.newChart(quality, "offscreen");
        chart.getScene().getGraph().add(surface);

        return chart;
    }

    //создание картинки для 2д решения
    private BufferedImage get2dChart(double xMin, double xMax, double yMin, double yMax) {
        Mapper mapper = new Mapper() {
            @Override
            public double f(double x, double y) {
                return Math.pow(x * x + y - 11, 2) + Math.pow(x + y * y - 7, 2);
            }
        };

        Range xrange = new Range((float) xMin, (float) xMax);
        Range yrange = new Range((float) yMin, (float) yMax);
        int steps = 200;

        // Create the object to represent the function over the given range.
        final Shape surface = (Shape) Builder.buildOrthonormal(new OrthonormalGrid(xrange, steps, yrange, steps), mapper);
        ColorMapper myColorMapper = new ColorMapper(new ColorMapRainbowNoBorder(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f));
        surface.setColorMapper(myColorMapper);
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(true);
        surface.setWireframeColor(Color.BLACK);

        MapperContourPictureGenerator contour = new MapperContourPictureGenerator(mapper, xrange, yrange);

        BufferedImage bufferedImage = contour.getFilledContourImage(new DefaultContourColoringPolicy(myColorMapper), 400, 400, 30);

        return bufferedImage;
    }

    //обработка нажатия на кнопку "Решение"
    public void onClickStart(ActionEvent actionEvent) throws IOException {

        try {
            isTrue = false;
            float xMin = Float.parseFloat(xMinField.getText());
            float xMax = Float.parseFloat(xMaxField.getText());
            float yMin = Float.parseFloat(yMinField.getText());
            float yMax = Float.parseFloat(yMaxField.getText());
            float scan = Float.parseFloat(scanField.getText());
            float parX = Float.parseFloat(paramX.getText());
            float parY = Float.parseFloat(paramY.getText());
            float parAns = Float.parseFloat(paramAnswer.getText());

            if(!(methodBox.getSelectionModel().getSelectedItem() == null)){
                if (xMax <= 10 && xMin >= -10 && yMax <= 10 && yMin >= -10 && isTrueArea(xMin, yMin, parX, parY, parAns)) {
                    factory = new JavaFXChartFactory();
                    chart = getDemoChart(factory, xMin, xMax, yMin, yMax);
                    imageView = factory.bindImageView(chart);

                    BufferedImage bufferedImage = get2dChart(xMin, xMax, yMin, yMax);
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    imageView2d = new ImageView(image);

                    anchor2dPane.getChildren().clear();
                    Pane3d.getChildren().clear();

                    NumberAxis xAxis = new NumberAxis(xMin, xMax, 1);
                    NumberAxis yAxis = new NumberAxis(yMin, yMax, 1);

                    LineChart<Number, Number> chart2dLine = new LineChart<Number, Number>(xAxis, yAxis);

                    XYChart.Series seriesDiag = new XYChart.Series();
                    XYChart.Series seriesAnswer = new XYChart.Series();


                    CalculateGraph2d calc2d = new CalculateGraph2d(xMin, xMax, yMin, yMax, parX, parY, parAns, scan);
                    XYChart.Series seriesArea = calc2d.getOutArea();
                    seriesDiag.getData().add(calc2d.getCalculateX());
                    seriesDiag.getData().add(calc2d.getCalculateY());

                    float[] optXY;
                    optXY = optim(xMin, xMax, yMin, yMax, scan, parX, parY, parAns);
                    seriesAnswer.getData().add(new XYChart.Data<Number, Number>(optXY[0], optXY[1]));

                    if (isTrue) {
                        chart2dLine.getData().add(seriesDiag);
                        chart2dLine.getData().add(seriesAnswer);
                        chart2dLine.getData().add(seriesArea);
                        chart2dLine.setLegendVisible(false);
                        chart2dLine.setMaxWidth(400);

                        Node lineDiag = seriesDiag.getNode().lookup(".chart-series-line");
                        lineDiag.setStyle("-fx-stroke: #FF0000;" +
                                "-fx-stroke-width: 2px;\n" +
                                "-fx-effect: null;");

                        Node lineArea = seriesArea.getNode().lookup(".chart-series-line");
                        lineArea.setStyle("-fx-stroke: #000000;" +
                                "-fx-stroke-width: 1px;" +
                                "-fx-effect: null;");

                        Node lineAnswer = seriesAnswer.getNode().lookup(".chart-series-line");
                        lineAnswer.setStyle("-fx-stroke-width: 7px;");

                        chart2dLine.setCreateSymbols(false);

                        final String resourceF = getClass().getResource("back.png").toExternalForm();
                        File f = new File(resourceF.substring(6));
                        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "PNG", f);

                        //final String resourceCss = getClass().getResource("StylePlot.css").toExternalForm();
                        //chart2dLine.getStylesheets().add(resourceCss);
                        Node stylePlotNode = chart2dLine.lookup(".chart-plot-background");
                        stylePlotNode.setStyle("-fx-background-color: transparent;");

                        anchor2dPane.getChildren().add(imageView2d);
                        imageView2d.setX(36);
                        imageView2d.setY(16);
                        imageView2d.setFitHeight(345);
                        imageView2d.setFitWidth(350);

                        chart2dLine.setAlternativeRowFillVisible(false);
                        chart2dLine.setAlternativeColumnFillVisible(false);

                        chart2dLine.setMaxSize(400, 400);
                        chart2dLine.setMinSize(400, 400);

                        anchor2dPane.getChildren().add(chart2dLine);

                        Pane3d.getChildren().add(imageView);
                    } else {
                        anchor2dPane.getChildren().clear();
                        Pane3d.getChildren().clear();
                        getAlert("Ошибка условий ограничения", "Проверьте все ячейки на наличие ошибок!\n");
                    }
                } else {
                    getAlert("Неправильный формат чисел", "Проверьте все ячейки на наличие ошибок!\n");
                }
            } else {
                getAlert("Не выбран метод расчета решения", "Выберете метод расчета решения задачи!");
            }

        } catch (NumberFormatException e) {
            getAlert("Неправильный формат чисел", "Проверьте все ячейки на наличие ошибок!\n");
        }
    }

    //расчет решения 
    private float[] optim(float xMin, float xMax, float yMin, float yMax, float scan, float parX, float parY, float parAns) {
        float v, rashod;
        float[] optXY = new float[2];
        //функция, вычисляется первое значение для сравнения с поледующими для нахождения минимума
        float minimumV = (float) (Math.pow((xMax * xMax + yMax - 11), 2) + Math.pow((xMax + yMax * yMax - 7), 2));
        System.out.println("T1 = " + xMax);
        System.out.println("T2 = " + yMax);
        System.out.println("V = " + minimumV);
        System.out.println();
        //перебор, scan - шаг
        for (float i = xMin; i <= xMax; i = i + scan) {
            for (float a = yMax; a >= yMin; a = a - scan) {
                if (parX * i + parY * a < parAns) {
                    isTrue = true;
                    v = (float) (Math.pow((i * i + a - 11), 2) + Math.pow((i + a * a - 7), 2));
                    if (minimumV >= v) {
                        minimumV = v;
                        optXY[0] = i;
                        optXY[1] = a;
                        System.out.println("T1 = " + i);
                        System.out.println("T2 = " + a);
                        System.out.println("V = " + minimumV);
                        rashod = minimumV * 8 * 10;
                        System.out.println("Rashod = " + String.format("%.1f", rashod));
                        minV.setText("Минимальное значение\nобъема фильтрата: " + String.format("%.3f", minimumV));
                        minOut.setText("Минимальная себестоимость\nфильтрата за смену: " + String.format("%.1f", rashod));
                    }
                }
            }
        }

        return optXY;
    }

    private void getAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean isTrueArea(double xMin, double yMin, double parX, double parY, double parAns) {
        return parAns >= xMin * parX + yMin * parY;
    }

    private void addingsArrays(){
        methodsArray.add("Метод полного перебора");
        taskArray.add("Расчет минимального значения фильтрата");
    }

}
