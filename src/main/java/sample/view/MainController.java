package sample.view;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.contour.DefaultContourColoringPolicy;
import org.jzy3d.contour.MapperContourPictureGenerator;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainController {

    public ImageView img2d;
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

    private ImageView imageView2d;
    private JavaFXChartFactory factory2d;
    private AWTChart chart2dtest;

    public MainController() {
    }

    public void initialize() {

    }

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

    private AWTChart getDemoChart(JavaFXChartFactory factory, String toolkit, double xMin, double xMax, double yMin, double yMax) {
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
        AWTChart chart = (AWTChart) factory.newChart(quality, toolkit);
        chart.getScene().getGraph().add(surface);

        return chart;
    }

    private BufferedImage get2dChart(double xMin, double xMax, double yMin, double yMax) {
        Mapper mapper = new Mapper() {
            @Override
            public double f(double x, double y) {
                return Math.pow(x * x + y - 11, 2) + Math.pow(x + y * y - 7, 2);
            }
        };

        Range xrange = new Range((float) xMin , (float) xMax );
        Range yrange = new Range((float) yMin , (float) yMax );
        int steps = 100;

        // Create the object to represent the function over the given range.
        final Shape surface = (Shape) Builder.buildOrthonormal(new OrthonormalGrid(xrange, steps, yrange, steps), mapper);
        ColorMapper myColorMapper = new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f));
        surface.setColorMapper(myColorMapper);
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(true);
        surface.setWireframeColor(Color.BLACK);

        MapperContourPictureGenerator contour = new MapperContourPictureGenerator(mapper, xrange, yrange);

        BufferedImage bufferedImage = contour.getFilledContourImage(new DefaultContourColoringPolicy(myColorMapper), 400, 400, 30);

        return bufferedImage;
    }

    private AWTChart getChart2d(JavaFXChartFactory factory, String toolkit, double xMin, double xMax, double yMin, double yMax) {

        Mapper mapper = new Mapper() {
            @Override
            public double f(double x, double y) {
                return Math.pow(x * x + y - 11, 2) + Math.pow(x + y * y - 7, 2);
            }
        };

        // Define range and precision for the function to plot
        Range xrange = new Range((float) xMin, (float) xMax);
        Range yrange = new Range((float) yMin, (float) yMax);
        int steps = 80;

        // Create the object to represent the function over the given range.
        final Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(xrange, steps, yrange, steps), mapper);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(false);

        Quality quality = Quality.Advanced;

        AWTChart chart = (AWTChart) factory.newChart(quality, toolkit);
        chart.getScene().getGraph().add(surface);
        chart.getView().getCamera().setScreenGridDisplayed(true);
        chart.getAWTView().setViewPoint(new Coord3d(0, 10, 0), true);
        return chart;
    }


    public void onClickStart(ActionEvent actionEvent) throws IOException {

        try {
            double xMin = Double.parseDouble(xMinField.getText());
            double xMax = Double.parseDouble(xMaxField.getText());
            double yMin = Double.parseDouble(yMinField.getText());
            double yMax = Double.parseDouble(yMaxField.getText());
            double scan = Double.parseDouble(scanField.getText());
            double parX = Double.parseDouble(paramX.getText());
            double parY = Double.parseDouble(paramY.getText());
            double parAns = Double.parseDouble(paramAnswer.getText());
            factory = new JavaFXChartFactory();
            chart = getDemoChart(factory, "offscreen", xMin, xMax, yMin, yMax);
            imageView = factory.bindImageView(chart);

            BufferedImage bufferedImage = get2dChart(xMin, xMax, yMin, yMax);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView2d = new ImageView(image);

            anchor2dPane.getChildren().removeAll();
            Pane3d.getChildren().removeAll();
            LineChart<Number, Number> chart2dLine = new LineChart<Number, Number>(xAxis, yAxis);

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

            CalculateGraph2d calc2d = new CalculateGraph2d(xMin, xMax, yMin, yMax, parX, parY, parAns, scan);
            series5.getData().add(calc2d.getCalculateX());
            series5.getData().add(calc2d.getCalculateY());

            double[] optXY = new double[2];
            optXY = optim(xMin, xMax, yMin, yMax, scan, parX, parY, parAns);
            seriesAnswer.getData().add(new XYChart.Data<Number, Number>(optXY[0], optXY[1]));

            chart2dLine.getData().add(series1);
            chart2dLine.getData().add(series2);
            chart2dLine.getData().add(series3);
            chart2dLine.getData().add(series4);
            chart2dLine.getData().add(series5);
            chart2dLine.getData().add(seriesAnswer);
            chart2dLine.setLegendVisible(false);
            chart2dLine.setMaxWidth(400);

            final String resourceF = getClass().getResource("back.png").toExternalForm();
            File f = new File(resourceF.substring(6));
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "PNG", f);

            final String resourceCss = getClass().getResource("StylePlot.css").toExternalForm();
            chart2dLine.getStylesheets().add(resourceCss);

            anchor2dPane.getChildren().add(imageView2d);
            imageView2d.setX(36);
            imageView2d.setY(16);
            imageView2d.setFitHeight(345);
            imageView2d.setFitWidth(350);

            chart2dLine.setAlternativeRowFillVisible(false);
            chart2dLine.setAlternativeColumnFillVisible(false);

            //chart2dLine.setLayoutX(5);
            //chart2dLine.setLayoutY(5);
            chart2dLine.setMaxSize(400, 400);
            chart2dLine.setMinSize(400, 400);

            anchor2dPane.getChildren().add(chart2dLine);

            Pane3d.getChildren().add(imageView);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Неправильный формат чисел");
            alert.setContentText("Проверьте все ячейки на наличие ошибок!\n");
            alert.showAndWait();
        }
    }

    public double[] optim(double xMin, double xMax, double yMin, double yMax, double scan, double parX, double parY, double parAns) {
        double v, rashod;
        double[] optXY = new double[2];
        //функция, вычисляется первое значение для сравнения с поледующими для нахождения минимума
        double minimumV = Math.pow((xMax * xMax + yMax - 11), 2) + Math.pow((xMax + yMax * yMax - 7), 2);
        System.out.println("T1 = " + xMax);
        System.out.println("T2 = " + yMax);
        System.out.println("V = " + minimumV);
        System.out.println();
        //перебор, scan - шаг
        for (double i = xMin; i <= xMax; i = i + scan) {
            for (double a = yMax; a >= yMin; a = a - scan) {
                if (parX * i + parY * a < parAns) {
                    v = Math.pow((i * i + a - 11), 2) + Math.pow((i + a * a - 7), 2);
                    if (minimumV >= v) {
                        minimumV = v;
                        optXY[0] = i;
                        optXY[1] = a;
                        System.out.println("T1 = " + i);
                        System.out.println("T2 = " + a);
                        System.out.println("V = " + minimumV);
                        rashod = minimumV * 8 * 10;
                        System.out.println("Rashod = " + String.format("%.1f", rashod));
                        minV.setText("Min значение объема фильтрата: " + String.format("%.3f", minimumV));
                        minOut.setText("Min значение расхода: " + String.format("%.1f", rashod));
                    }
                }
            }
        }

        return optXY;
    }
}
