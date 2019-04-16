package sample.FXMLControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
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
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MainController {

    public TextArea resultTextArea;
    public Label isUpLabel;
    public ComboBox<String> methodBox;
    public TextArea mathModelArea;
    public TextField paramX;
    public TextField paramY;
    public TextField paramAnswer;
    public AnchorPane Pane3d;
    public AnchorPane anchor2dPane;
    public TextField xMinField;
    public TextField xMaxField;
    public TextField yMinField;
    public TextField yMaxField;
    public TextField scanField;
    public TextField k1Field;
    public TextField k2Field;
    public TextField nField;

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


            if (methodBox.getSelectionModel().getSelectedItem() != null) {
                if (xMax <= 100 && xMin >= -100 && yMax <= 100 && yMin >= -100 && scan >= 0.1 && isTrueArea(xMin, xMax, yMin, yMax, parX, parY, parAns, task.isUp())) {
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

                    LineChart<Number, Number> chart2dLine = new LineChart<>(xAxis, yAxis);
                    XYChart.Series<Number, Number> seriesAnswer = new XYChart.Series<>();

                    XYChart.Series<Number, Number> seriesArea = CalculateGraph2d.getOutArea(xMin, xMax, yMin, yMax, parX, parY, parAns, scan, task.isUp());

                    float[] optXY = new float[2];
                    if (methodBox.getSelectionModel().getSelectedItem().equals("Метод полного перебора")) {
                        optXY = optim(task.isMin(), xMin, xMax, yMin, yMax, scan, parX, parY, parAns, task.isUp(), task.getCoef());
                    } else if (methodBox.getSelectionModel().getSelectedItem().equals("Метод Бокса")) {
                        double eps = Double.parseDouble(scanField.getText());
                        //максимальное количество шагов расчета целевой функции
                        //и ограниченрий второго рода
                        int K1 = Integer.parseInt(k1Field.getText());
                        int K2 = Integer.parseInt(k2Field.getText());
                        int n = Integer.parseInt(nField.getText());
                        optXY = calculateBox(xMin, yMin, xMax, yMax, n, K1, K2, parX, parY, parAns, eps, task.isUp());
                    }

                    if (isTrue) {
                        chart2dLine.getData().clear();
                        seriesAnswer.getData().add(new XYChart.Data<>(optXY[0], optXY[1]));

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

                        if ((yMinField.getText().length() >= 4 && yMin != Math.round(yMin)) || (yMaxField.getText().length() >= 4 && yMax != Math.round(yMax))) {
                            anchor2dPane.getChildren().add(imageView2d);
                            imageView2d.setX(44);
                            imageView2d.setY(16);
                            imageView2d.setFitHeight(345);
                            imageView2d.setFitWidth(342);

                            prepareGraph(chart2dLine);
                        } else {
                            anchor2dPane.getChildren().add(imageView2d);
                            imageView2d.setX(36);
                            imageView2d.setY(16);
                            imageView2d.setFitHeight(345);
                            imageView2d.setFitWidth(350);

                            prepareGraph(chart2dLine);
                        }
                    } else {
                        anchor2dPane.getChildren().clear();
                        Pane3d.getChildren().clear();
                        Main.getAlert("Ошибка условий ограничения", "Проверьте все ячейки на наличие ошибок!\n");
                    }
                } else {
                    Main.getAlert("Неверно введены значения", "Проверьте все ячейки на наличие ошибок!\n" +
                            "Ограничения должны лежать в пределах от -100 до 100\nШаг должен быть больше 0.01");
                }
            } else {
                Main.getAlert("Не выбран метод расчета решения", "Выберете метод расчета решения задачи!");
            }

        } catch (NumberFormatException e) {
            Main.getAlert("Неверный формат чисел", "Проверьте все ячейки на наличие ошибок!\n");
            throw e;
        }
    }

    private void prepareGraph(LineChart<Number, Number> chart2dLine) {
        chart2dLine.setAlternativeRowFillVisible(false);
        chart2dLine.setAlternativeColumnFillVisible(false);

        chart2dLine.setMaxSize(400, 400);
        chart2dLine.setMinSize(400, 400);

        anchor2dPane.getChildren().add(chart2dLine);
        Pane3d.getChildren().add(imageView);
    }

    private float[] optim(boolean isMin, float xMin, float xMax, float yMin, float yMax, float scan, float parX, float parY, float parAns, boolean isUp, float coef) {
        float v, rashod;
        float[] optXY = new float[2];
        DecimalFormat df = new DecimalFormat("#.0");
        if (isMin) {
            float minimumV = Float.MAX_VALUE;
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
        methodsArray.add("Метод Бокса");
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

    Result Goal_func(Variable var, Task task) {
        Result res = new Result();
        Expression e = new ExpressionBuilder(task.getFunc())
                .variables("x", "y")
                .build()
                .setVariable("x", var.v1)
                .setVariable("y", var.v2);
        res.z = (float) e.evaluate();
        return res;
    }


    public void onClickClalcBox(ActionEvent actionEvent) {
    }

    private float[] calculateBox(double g1, double g2, double h1, double h2, int n, int K1, int K2, float parX, float parY, float parAns, double eps, boolean isMax) {
        float[] optXY = new float[2];

        //рассчитываем количество вершин комплекса
        int N;
        if (n <= 5) N = 2 * n;
        else N = n + 1;

        //обнуляем счетчики шагов
        int k1, k2;
        k1 = 0;
        k2 = 0;

        ArrayList<Variable> fix = new ArrayList<>();
        ArrayList<Variable> non_fix = new ArrayList<>();

        Res_of_dir ogr2;
        int t, f;
        t = f = 0;

        ArrayList<Variable> arr_var = new ArrayList<>();
        Random r = new Random();

        while (t == 0 && k2 < K2) {
            for (int i = 0; i < N; ++i) {
                arr_var.add(new Variable(g1 + (r.nextInt(10)) * (h1 - g1) / 10, g2 + (r.nextInt(10)) * (h2 - g2) / 10));
            }

            //проверка ограничений второго рода в каждой вершине комплекса
            for (int i = 0; i < N; ++i) {
                ogr2 = Dir(arr_var.get(i), parX, parY, parAns, isMax);
                if (ogr2.ogr) {
                    fix.add(t, arr_var.get(i));
                    t = t + 1;
                } else {
                    non_fix.add(arr_var.get(i));
                    f = f + 1;
                }
            }

            if (t == 0) {
                k2 = k2 + 1;
                f = 0;
            }
        }

        if (k2 == K2) {
            Main.getAlert("Ошибка!", "Превышено количество шагов расчета ограничений второго рода!\nУвеличьте это значение!");
            return null;
        }

        // подтягиваем незафиксированные вершины, пока не зафиксируем все
        // вершины комплекса
        Variable sum = new Variable();
        sum.v1 = sum.v2 = 0;

        ArrayList<Variable> jump = new ArrayList<>();
        Res_of_dir ogr22;
        k2 = 0;

        for (int i = 0; i < f && k2 < K2; i++) {
            sum.v1 = sum.v2 = 0;

            for (int j = 0; j < t; j++) {
                sum.v1 = sum.v1 + fix.get(j).v1;
                sum.v2 = sum.v2 + fix.get(j).v2;
            }
            jump.add(new Variable(0.5 * (non_fix.get(i).v1 + sum.v1 / t), 0.5 * (non_fix.get(i).v2 + sum.v2 / t)));
            ogr22 = Dir(jump.get(i), parX, parY, parAns, isMax);

            if (ogr22.ogr) {
                fix.add(t, jump.get(i));
                t = t + 1;
            } else {
                non_fix.get(i).v1 = jump.get(i).v1;
                non_fix.get(i).v2 = jump.get(i).v2;
                i = i - 1;
                k2 = k2 + 1;
            }
        }

        if (k2 == K2) {
            Main.getAlert("Ошибка!", "Превышено количество шагов расчета ограничений второго рода!\nУвеличьте это значение!");
            return null;
        }

        Result[] goal = new Result[N];

        for (int i = 0; i < fix.size(); i++) {
            goal[i] = Goal_func(fix.get(i), task);
        }

        //выбираем максимальное и минимальное значение целевой функции
        double max, min;

        while (k1 < K1) {
            int min_i = 0;
            int max_i = 0;
            max = goal[0].z;

            for (int j = 0; j < fix.size(); j++) {
                if (max < goal[j].z) {
                    max = goal[j].z;
                    max_i = j;
                }
            }

            min = goal[0].z;
            for (int j = 0; j < fix.size(); j++) {
                if (min > goal[j].z) {
                    min = goal[j].z;
                    min_i = j;
                }
            }

            int good_index = 0, bad_index = 0;
            double good_func, bad_func;

            //если тип экстремума - минимум, то наилучшим будет миним.
            //значение целевой функции
            //TODO
            if (isMax) {
                bad_index = max_i;
                good_index = min_i;
            } else {

                bad_index = min_i;
                good_index = max_i;
            }


            good_func = goal[good_index].z;
            bad_func = goal[bad_index].z;

            // рассчитываем координаты центра комплекса с отброшенной наихудшей вершиной
            sum.v1 = sum.v2 = 0;

            //for (int i = 0; i < N; ++i) {
            for (int i = 0; i < fix.size(); i++) {
                sum.v1 = sum.v1 + fix.get(i).v1;
                sum.v2 = sum.v2 + fix.get(i).v2;
            }

            Variable center = new Variable();

            center.v1 = (sum.v1 - fix.get(bad_index).v1) / (N - 1);
            center.v2 = (sum.v2 - fix.get(bad_index).v2) / (N - 1);

            // проверяем условие останова
            double B;
            B = (Math.abs(center.v1 - fix.get(bad_index).v1) + Math.abs(center.v1 - fix.get(good_index).v1) +
                    Math.abs(center.v2 - fix.get(bad_index).v2) + Math.abs(center.v2 - fix.get(good_index).v2)) / (2 * n);
            k1 = k1 + 1;

            //если условие останова выполняется, выводим результат
            if (B < eps) {
                isTrue = true;
                /*minimumV = v;
                optXY[0] = i;
                optXY[1] = a;*/
                double rashod = good_func * task.getCoef();
                DecimalFormat df = new DecimalFormat("#.0");
                resultTextArea.setText(task.getMinV() + String.format("%.0f", rashod) + task.getMinOut() +
                        Float.valueOf(df.format(fix.get(good_index).v1).replace(",", ".")) + " и " + Float.valueOf(df.format(fix.get(good_index).v2).replace(",", ".")) + task.getVariables());

                /*tbFunc.Text = Math.Round(good_func, 3).ToString();
                tbvar1.Text = Math.Round(fix[good_index].v1, 3).ToString();
                tbvar2.Text = Math.Round(fix[good_index].v2, 3).ToString();
                tbAbrid.Text = Math.Round(direction.res, 3).ToString();
                tbStep.Text = k1.ToString();*/
                optXY[0] = (float) fix.get(good_index).v1;
                optXY[1] = (float) fix.get(good_index).v2;
                return optXY;
            }
            //иначе продолжаем расчет
            //взамен наихудшей вершины рассчитываем координаты новой
            Variable new_var = new Variable();
            new_var.v1 = 2.3 * center.v1 - 1.3 * fix.get(bad_index).v1;
            new_var.v2 = 2.3 * center.v2 - 1.3 * fix.get(bad_index).v2;

            // проверяем ограничения первого рода
            while (new_var.v1 < g1) new_var.v1 = g1 + 1;
            while (new_var.v1 > h1) new_var.v1 = h1 - 1;

            while (new_var.v2 < g2) new_var.v2 = g2 + 1;
            while (new_var.v2 > h2) new_var.v2 = h2 - 1;

            //проверяем ограничения второго рода
            Res_of_dir ogr222;
            ogr222 = Dir(new_var, parX, parY, parAns, isMax);

            Result new_res;
            k2 = 0;

            while (k2 > K2) {
                if (ogr222.ogr) break;
                else {
                    new_var.v1 = (new_var.v1 + center.v1) / 2;
                    new_var.v2 = (new_var.v2 + center.v2) / 2;
                    ogr222 = Dir(new_var, parX, parY, parAns, isMax);
                    k2++;
                }
            }

            new_res = Goal_func(new_var, task);
            while (new_res.z > bad_func) {
                new_var.v1 = (new_var.v1 + fix.get(good_index).v1) / 2;
                new_var.v2 = (new_var.v2 + fix.get(good_index).v2) / 2;
                new_res = Goal_func(new_var, task);
            }

            fix.set(bad_index, new_var);
            goal[bad_index] = new_res;
        }
        Main.getAlert("Превышено количество шагов расчета целевой функции!", "Увеличьте это значение или погрешность расчета целевой функции!");
        return null;
    }


    private Res_of_dir Dir(Variable var, double parX, double parY, double parAns, boolean isMax) {
        Res_of_dir dir = new Res_of_dir();
        dir.res = parX * var.v1 + parY * var.v2;
        if (isMax) {
            dir.ogr = dir.res < parAns;
        } else {
            dir.ogr = dir.res > parAns;
        }

        return dir;
    }
}

class Variable {
    public double v1;
    public double v2;

    public Variable(double v1, double v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public Variable() {
    }
}

class Res_of_dir {
    public double res;
    public boolean ogr;

    public Res_of_dir(double res, boolean ogr) {
        this.res = res;
        this.ogr = ogr;
    }

    public Res_of_dir() {
    }
}

class Result {
    public double z;
}