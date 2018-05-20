package sample.FXMLControllers;

import javafx.scene.chart.XYChart;

public class CalculateGraph2d {

    private double xMin, xMax, yMin, yMax, paramX, paramY, paramAnswer, scan;
    private XYChart.Data<Number, Number> calculateX, calculateY;
    XYChart.Series<Number, Number> outArea = new XYChart.Series<>();

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
        outArea();
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

    public void outArea(){
        for(double x = xMin; x <= xMax; x = x + scan){
            outArea.getData().add(new XYChart.Data<>(x, yMax));
            for (double y = yMin; y <= yMax; y = y + scan){
                if(paramX*x+paramY*y>=paramAnswer){
                    outArea.getData().add(new XYChart.Data<>(x,y));
                    break;
                }
            }
        }
        outArea.getData().add(new XYChart.Data<>(xMax, yMax));
    }

    public XYChart.Data<Number, Number> getCalculateX() {
        return calculateX;
    }

    public XYChart.Data<Number, Number> getCalculateY() {
        return calculateY;
    }

    public XYChart.Series getOutArea() {
        return outArea;
    }
}
