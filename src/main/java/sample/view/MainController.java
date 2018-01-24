package sample.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

public class MainController {

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

    private LineChart<Number,Number> chart2dLine;
    private NumberAxis xAxis = new NumberAxis();
    private NumberAxis yAxis = new NumberAxis();

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
    private JavaFXChartFactory factory;
    private AWTChart chart;

    public MainController() {
    }

    public void initialize(){

    }

    public void clearFields(){
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
        minV.setText("Min значение объема фильрата: ");
        minOut.setText("Min значение расхода: ");
    }

    private AWTChart getDemoChart(JavaFXChartFactory factory, String toolkit, int xMin, int xMax, int yMin, int yMax) {
        // -------------------------------
        // Define a function to plot
        Mapper mapper = new Mapper() {
            @Override
            public double f(double x, double y) {
                return Math.pow(x*x+y-11,2)+Math.pow(x+y*y-7,2) ;
            }
        };

        // Define range and precision for the function to plot
        Range rangeX = new Range(xMin, xMax);
        Range rangeY = new Range(yMin, yMax);
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
        AWTChart chart = (AWTChart) factory.newChart(quality, toolkit);
        chart.getScene().getGraph().add(surface);
        return chart;
    }

    public TextField getxMinField() {
        return xMinField;
    }

    public TextField getxMaxField() {
        return xMaxField;
    }

    public TextField getyMinField() {
        return yMinField;
    }

    public TextField getyMaxField() {
        return yMaxField;
    }

    public TextField getScanField() {
        return scanField;
    }

    public void onCllickStart(ActionEvent actionEvent) {

        try {
            int xMin = Integer.parseInt(xMinField.getText());
            int xMax = Integer.parseInt(xMaxField.getText());
            int yMin = Integer.parseInt(yMinField.getText());
            int yMax = Integer.parseInt(yMaxField.getText());
            factory = new JavaFXChartFactory();
            chart = getDemoChart(factory, "offscreen", xMin, xMax, yMin, yMax);
            imageView = factory.bindImageView(chart);

            anchor2dPane.getChildren().removeAll();
            Pane3d.getChildren().removeAll();
            chart2dLine = new LineChart<Number, Number>(xAxis, yAxis);

            XYChart.Series series1 = new XYChart.Series();
            XYChart.Series series2 = new XYChart.Series();
            XYChart.Series series3 = new XYChart.Series();
            XYChart.Series series4 = new XYChart.Series();
            XYChart.Series series5 = new XYChart.Series();
            XYChart.Series seriesAnswer = new XYChart.Series();
            //vertical line max x
            series1.getData().add(new XYChart.Data(xMax, yMax));
            series1.getData().add(new XYChart.Data(xMax, yMin));
            //horizontal line min x
            series2.getData().add(new XYChart.Data(xMin, yMin));
            series2.getData().add(new XYChart.Data(xMin, yMax));

            series3.getData().add(new XYChart.Data(xMin, yMin));
            series3.getData().add(new XYChart.Data(xMax, yMin));

            series4.getData().add(new XYChart.Data(xMin, yMax));
            series4.getData().add(new XYChart.Data(xMax, yMax));

            for (int a = yMax; a > yMin; a--) {
                if (Double.parseDouble(paramX.getText()) * xMin + Double.parseDouble(paramY.getText()) * a <= Double.parseDouble(paramAnswer.getText())) {
                    series5.getData().add(new XYChart.Data<Number, Number>(xMin, a));
                    break;
                }
            }

            for (int a = yMax; a > yMin; a--) {
                if (Double.parseDouble(paramX.getText()) * xMax + Double.parseDouble(paramY.getText()) * a <= Double.parseDouble(paramAnswer.getText())) {
                    series5.getData().add(new XYChart.Data<Number, Number>(xMax, a));
                    break;
                }
            }

            double[] optXY = optim(xMin, xMax, yMin, xMax, Double.valueOf(scanField.getText()));
            seriesAnswer.getData().add(new XYChart.Data<Number, Number>(optXY[0], optXY[1]));

            chart2dLine.getData().add(series1);
            chart2dLine.getData().add(series2);
            chart2dLine.getData().add(series3);
            chart2dLine.getData().add(series4);
            chart2dLine.getData().add(series5);
            chart2dLine.getData().add(seriesAnswer);
            chart2dLine.setLegendVisible(false);
            anchor2dPane.getChildren().add(chart2dLine);
            Pane3d.getChildren().add(imageView);
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Bad format in cell.");
            alert.setContentText("Check all cells!\n");
            alert.showAndWait();

            xMinField.clear();
            xMaxField.clear();
            yMaxField.clear();
            yMinField.clear();
            scanField.clear();

        }
    }

    public double[] optim(int xMin, int xMax, int yMin, int yMax, double scan){
        double v, rashod;
        double[] optXY = {0, 0};
        //функция, вычисляется первое значение для сравнения с поледующими для нахождения минимума
        double minimumV = Math.rint(Math.pow((xMax -yMax - 11), 2)+ Math.pow((xMax+yMax-7), 2));
        //перебор, scan - шаг
        for(double i = xMin; i < xMax; i=i+scan){
            for(double a = yMin; a < yMax; a=a+scan){
                if(0.5 * i + a <= 1){
                    v = Math.pow((i*i +a - 11), 2)+ Math.pow((i+a*a-7), 2);
                    if(minimumV >v){
                        minimumV = v;
                        optXY[0]=i;
                        optXY[1]=a;
                        System.out.println("T1 = " + i);
                        System.out.println("T2 = " + a);
                        System.out.println("V = " + minimumV);
                        rashod = minimumV * 8 * 10;
                        System.out.println("Rashod = " + String.format("%.1f",rashod));
                        minV.setText("Min значение объема фильрата: " + String.format("%.3f", minimumV));
                        minOut.setText("Min значение расхода: "+ String.format("%.1f",rashod));
                    }
                }
            }
        }

        return optXY;
    }
}
