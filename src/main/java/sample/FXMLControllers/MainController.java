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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
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
import sample.Entities.Task;
import sample.Main;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MainController {

    @FXML
    private TextArea resultTextArea;
    @FXML
    private Label isUpLabel;
    @FXML
    private ComboBox<String> methodBox;
    @FXML
    private TextArea mathModelArea;
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

    private Main main;
    private Task task;

    private ImageView imageView;
    private ImageView imageView2d;
    private JavaFXChartFactory factory;
    private AWTChart chart;

    private ObservableList<String> methodsArray = FXCollections.observableArrayList();

    private boolean isTrue;

    public MainController() {
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void initialize() {
        addingsArrays();
        methodBox.setItems(methodsArray);
        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?");
        scanField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) scanField.setText(oldValue);
        });
    }

    //очистка полей
    void clearFields() {
        anchor2dPane.getChildren().clear();
        Pane3d.getChildren().clear();
        resultTextArea.clear();
    }

    //3д решение
    private AWTChart getChart(JavaFXChartFactory factory, double xMin, double xMax, double yMin, double yMax, Task task) {
        // -------------------------------
        // Define a function to plot
        Mapper mapper = new Mapper() {
            @Override
            public double f(double x, double y) {
                Expression e = new ExpressionBuilder(task.getFunc())
                        .variables("x", "y")
                        .build()
                        .setVariable("x", x)
                        .setVariable("y", y);
                double result = e.evaluate();
                return result;
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
    private BufferedImage get2dChart(double xMin, double xMax, double yMin, double yMax, Task task) {
        Mapper mapper = new Mapper() {
            @Override
            public double f(double x, double y) {
                Expression e = new ExpressionBuilder(task.getFunc())
                        .variables("x", "y")
                        .build()
                        .setVariable("x", x)
                        .setVariable("y", y);
                return e.evaluate();
            }
        };

        Range xrange = new Range((float) xMin, (float) xMax);
        Range yrange = new Range((float) yMin, (float) yMax);
        int steps = 200;

        // Create the object to represent the function over the given range.
        final Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(xrange, steps, yrange, steps), mapper);
        ColorMapper myColorMapper = new ColorMapper(new ColorMapRainbowNoBorder(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f));
        surface.setColorMapper(myColorMapper);
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(true);
        surface.setWireframeColor(Color.BLACK);

        MapperContourPictureGenerator contour = new MapperContourPictureGenerator(mapper, xrange, yrange);

        return contour.getFilledContourImage(new DefaultContourColoringPolicy(myColorMapper), 400, 400, 30);
    }

    //обработка нажатия на кнопку "Решение"
    public void onClickStart(ActionEvent actionEvent) {

        try {
            isTrue = false;
            float xMin = Float.parseFloat(xMinField.getText().replace(",", "."));
            float xMax = Float.parseFloat(xMaxField.getText().replace(",", "."));
            float yMin = Float.parseFloat(yMinField.getText().replace(",", "."));
            float yMax = Float.parseFloat(yMaxField.getText().replace(",", "."));
            float scan = Float.parseFloat(scanField.getText());
            float parX = Float.parseFloat(paramX.getText());
            float parY = Float.parseFloat(paramY.getText());
            float parAns = Float.parseFloat(paramAnswer.getText());

            if (!(methodBox.getSelectionModel().getSelectedItem() == null)) {
                if (xMax <= 10 && xMin >= -10 && yMax <= 10 && yMin >= -10 && scan >= 0.1 && isTrueArea(xMin, xMax, yMin, yMax, parX, parY, parAns, task.isUp())) {
                    factory = new JavaFXChartFactory();
                    chart = getChart(factory, xMin, xMax, yMin, yMax, task);
                    imageView = factory.bindImageView(chart);

                    BufferedImage bufferedImage = get2dChart(xMin, xMax, yMin, yMax, task);
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    imageView2d = new ImageView(image);

                    anchor2dPane.getChildren().clear();
                    Pane3d.getChildren().clear();

                    NumberAxis xAxis = new NumberAxis(xMin, xMax, 1);
                    NumberAxis yAxis = new NumberAxis(yMin, yMax, 1);

                    LineChart<Number, Number> chart2dLine = new LineChart<Number, Number>(xAxis, yAxis);
                    XYChart.Series<Number, Number> seriesAnswer = new XYChart.Series<>();

                    XYChart.Series<Number, Number> seriesArea = CalculateGraph2d.getOutArea(xMin, xMax, yMin, yMax, parX, parY, parAns, scan, task.isUp());

                    float[] optXY;
                    optXY = optim(task.isMin(), xMin, xMax, yMin, yMax, scan, parX, parY, parAns, task.isUp(), task.getCoef());


                    if (isTrue) {
                        chart2dLine.getData().clear();
                        seriesAnswer.getData().add(new XYChart.Data<Number, Number>(optXY[0], optXY[1]));

                        chart2dLine.getData().add(seriesArea);
                        chart2dLine.getData().add(seriesAnswer);
                        chart2dLine.setLegendVisible(false);
                        chart2dLine.setCreateSymbols(false);
                        chart2dLine.setMaxWidth(400);

                        Node lineArea = seriesArea.getNode().lookup(".chart-series-line");
                        lineArea.setStyle("-fx-stroke: #2c2c2c;" +
                                "-fx-stroke-width: 3px;" +
                                "-fx-effect: null;");
                        Node lineAnswer = seriesAnswer.getNode().lookup(".chart-series-line");
                        lineAnswer.setStyle("-fx-stroke: #03c40c;" +
                                "-fx-stroke-width: 9px;");
                        Node stylePlotNode = chart2dLine.lookup(".chart-plot-background");
                        stylePlotNode.setStyle("-fx-background-color: transparent;");

                        anchor2dPane.getChildren().add(imageView2d);
                        imageView2d.setX(38);
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
                        main.getAlert("Ошибка условий ограничения", "Проверьте все ячейки на наличие ошибок!\n");
                    }
                } else {
                    main.getAlert("Неверно введены значения", "Проверьте все ячейки на наличие ошибок!\n" +
                            "Ограничения должны лежать в пределах от -10 до 10\nШаг должен быть больше 0.01");
                }
            } else {
                main.getAlert("Не выбран метод расчета решения", "Выберете метод расчета решения задачи!");
            }

        } catch (NumberFormatException e) {
            main.getAlert("Неправильный формат чисел", "Проверьте все ячейки на наличие ошибок!\n");
            throw e;
        }
    }

    //расчет решения 
    private float[] optim(boolean isMin, float xMin, float xMax, float yMin, float yMax, float scan, float parX, float parY, float parAns, boolean isUp, float coef) {
        float v, rashod;
        float[] optXY = new float[2];
        DecimalFormat df = new DecimalFormat("#.0");
        if (isMin) {
            float minimumV = Float.MAX_VALUE;
            //перебор, scan - шаг
            if (isUp) {
                for (float i = xMin; i <= xMax; i = i + scan) {
                    for (float a = yMin; a <= yMax; a = a + scan) {
                        if (parX * i + parY * a <= parAns) {
                            isTrue = true;
                            Expression e = new ExpressionBuilder(task.getFunc())
                                    .variables("x", "y")
                                    .build()
                                    .setVariable("x", i)
                                    .setVariable("y", a);
                            v = (float) e.evaluate();
                            if (minimumV >= v) {
                                minimumV = v;
                                optXY[0] = i;
                                optXY[1] = a;
                                rashod = minimumV * coef;
                                resultTextArea.setText(task.getMinV() + String.format("%.0f", rashod) + task.getMinOut() +
                                        Float.valueOf(df.format(optXY[0]).replace(",", ".")) + " и " + Float.valueOf(df.format(optXY[1]).replace(",", ".")) + task.getVariables());
                            }
                        }
                    }
                }
            } else {
                for (float i = xMin; i <= xMax; i = i + scan) {
                    for (float a = yMin; a <= yMax; a = a + scan) {
                        if (parX * i + parY * a >= parAns) {
                            isTrue = true;
                            Expression e = new ExpressionBuilder(task.getFunc())
                                    .variables("x", "y")
                                    .build()
                                    .setVariable("x", i)
                                    .setVariable("y", a);
                            v = (float) e.evaluate();
                            if (minimumV >= v) {
                                minimumV = v;
                                optXY[0] = i;
                                optXY[1] = a;
                                rashod = minimumV * coef;
                                resultTextArea.setText(task.getMinV() + String.format("%.0f", rashod) + task.getMinOut() +
                                        Float.valueOf(df.format(optXY[0]).replace(",", ".")) + " и " + Float.valueOf(df.format(optXY[1]).replace(",", ".")) + task.getVariables());
                            }
                        }
                    }
                }
            }
        } else {
            //перебор, scan - шаг
            float minimumV = 0;
            if (isUp) {
                for (float i = xMin; i <= xMax; i = i + scan) {
                    for (float a = yMin; a <= yMax; a = a + scan) {
                        if (parX * i + parY * a <= parAns) {
                            isTrue = true;
                            Expression e = new ExpressionBuilder(task.getFunc())
                                    .variables("x", "y")
                                    .build()
                                    .setVariable("x", i)
                                    .setVariable("y", a);
                            v = (float) e.evaluate();
                            if (minimumV < v) {
                                minimumV = v;
                                optXY[0] = i;
                                optXY[1] = a;
                                rashod = minimumV * coef;
                                resultTextArea.setText(task.getMinV() + String.format("%.0f", rashod) + task.getMinOut() +
                                        Float.valueOf(df.format(optXY[0]).replace(",", ".")) + " и " + Float.valueOf(df.format(optXY[1]).replace(",", ".")) + task.getVariables());
                            }
                        }
                    }
                }
            } else {
                for (float i = xMin; i <= xMax; i = i + scan) {
                    for (float a = yMin; a <= yMax; a = a + scan) {
                        if (parX * i + parY * a >= parAns) {
                            isTrue = true;
                            Expression e = new ExpressionBuilder(task.getFunc())
                                    .variables("x", "y")
                                    .build()
                                    .setVariable("x", i)
                                    .setVariable("y", a);
                            v = (float) e.evaluate();
                            if (minimumV < v) {
                                minimumV = v;
                                optXY[0] = i;
                                optXY[1] = a;
                                rashod = minimumV * coef;
                                resultTextArea.setText(task.getMinV() + String.format("%.0f", rashod) + task.getMinOut() +
                                        Float.valueOf(df.format(optXY[0]).replace(",", ".")) + " и " + Float.valueOf(df.format(optXY[1]).replace(",", ".")) + task.getVariables());
                            }
                        }
                    }
                }
            }
        }
        return optXY;
    }

    private boolean isTrueArea(double xMin, double xMax, double yMin, double yMax, double parX, double parY, double parAns, boolean isUp) {
        if (!isUp) {
            return parAns <= xMax * parX + yMax * parY || parAns <= xMin * parX + yMax * parY;
        } else {
            return parAns >= xMax * parX + yMin * parY || parAns >= xMin * parX + yMin * parY;
        }
    }

    private void addingsArrays() {
        methodsArray.add("Метод полного перебора");
    }

    public void onClickChooseTask(ActionEvent actionEvent) {
        boolean isChoose = main.showTaskDialog();
        if (isChoose) {
            xMinField.setText(String.valueOf(task.getxFrom()));
            xMaxField.setText(String.valueOf(task.getxTo()));
            yMinField.setText(String.valueOf(task.getyFrom()));
            yMaxField.setText(String.valueOf(task.getyTo()));
            paramX.setText(String.valueOf(task.getxParam()));
            paramY.setText(String.valueOf(task.getyParam()));
            paramAnswer.setText(String.valueOf(task.getAnswerParam()));
            scanField.setText("1");
            if (task.isUp()) {
                isUpLabel.setText("≤");
            } else {
                isUpLabel.setText("≥");
            }
            String result;
            final InputStream resourceF = this.getClass().getClassLoader().getResourceAsStream(task.getPathFormal());
            try (Scanner s = new Scanner(resourceF, "UTF-8").useDelimiter("\\A")) {
                result = s.hasNext() ? s.next() : "";
            }
            mathModelArea.setText(result);
            resultTextArea.clear();
        }
    }

    void setTask(Task task) {
        this.task = task;
    }
}
