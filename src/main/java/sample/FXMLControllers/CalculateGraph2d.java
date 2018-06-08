package sample.FXMLControllers;

import javafx.scene.chart.XYChart;

public class CalculateGraph2d {



    public static XYChart.Series getOutArea(double xMin, double xMax, double yMin, double yMax, double paramX, double paramY, double paramAnswer, double scan, boolean isUp) {
        XYChart.Series<Number, Number> outArea = new XYChart.Series<>();
        if (isUp) {
            for (double x = xMin; x <= xMax; x = x + 0.01) {
                outArea.getData().add(new XYChart.Data<>(x, yMax));
                boolean isTrue = false;
                for (double y = yMax; y >= yMin; y = y - 0.01) {
                    if (paramX * x + paramY * y < paramAnswer) {
                        outArea.getData().add(new XYChart.Data<>(x, y));
                        isTrue = true;
                        break;
                    }
                }
                if (!isTrue) {
                    outArea.getData().add(new XYChart.Data<>(x, yMin));
                }
            }
        } else {
            for (double x = xMin; x <= xMax; x = x + 0.01) {
                outArea.getData().add(new XYChart.Data<>(x, yMin));
                boolean isTrue = false;
                for (double y = yMin; y <= yMax; y = y + 0.01) {
                    if (paramX * x + paramY * y >= paramAnswer) {
                        outArea.getData().add(new XYChart.Data<>(x, y));
                        isTrue = true;
                        break;
                    }
                }
                if (!isTrue) {
                    outArea.getData().add(new XYChart.Data<>(x, yMax));
                }
            }
        }
        return outArea;
    }
}
