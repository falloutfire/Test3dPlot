package sample.view;

import javafx.scene.chart.XYChart;

public class CalculateGraph2d {

    private double xMin, xMax, yMin, yMax, paramX, paramY, paramAnswer, scan;
    private XYChart.Data<Number, Number> calculateX, calculateY;

    public CalculateGraph2d(double xMin, double xMax, double yMin, double yMax, double paramX, double paramY, double paramAnswer, double scan) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.paramX = paramX;
        this.paramY = paramY;
        this.paramAnswer = paramAnswer;
        this.scan = scan;
        calcX();
        calcY();
    }

    private void calcX(){
        boolean isTrue = false;

        for(double a = xMax; a>= xMin; a = a - scan){
            if (paramX * a + paramY * yMax <= paramAnswer) {
                calculateX = new XYChart.Data<Number, Number>(a, yMax);
                isTrue = true;
                break;
            }
        }

        if(!isTrue){
            for (double a = yMax; a >= yMin; a = a - scan) {
                if (paramX * xMin + paramY * a <= paramAnswer) {
                    calculateX = new XYChart.Data<Number, Number>(xMin, a);
                    break;
                }
            }
        }
    }

    private void calcY(){
        boolean isTrue = false;

        for (double a = yMax; a >= yMin; a = a - scan) {
            if (paramX * xMax + paramY * a <= paramAnswer) {
                calculateY = new XYChart.Data<Number, Number>(xMax, a);
                isTrue = true;
                break;
            }
        }

        if(!isTrue){
            for(double a = xMax; a>= xMin; a = a - scan){
                if (paramX * a + paramY * yMin <= paramAnswer) {
                    calculateY = new XYChart.Data<Number, Number>(a, yMin);
                    break;
                }
            }
        }
    }

    public XYChart.Data<Number, Number> getCalculateX() {
        return calculateX;
    }

    public XYChart.Data<Number, Number> getCalculateY() {
        return calculateY;
    }
}
