package sample.Entities;

public class Task {

    private boolean isMin;
    private String nameTask;
    private String pathFull;
    private String pathFormal;
    private String minV;
    private String minOut;
    private double xFrom;
    private double xTo;
    private double yFrom;
    private double yTo;
    private double xParam;
    private double yParam;
    private double answerParam;
    private String func;
    private float coef;
    private boolean isUp;
    private String variables;

    public Task(boolean isMin, String nameTask, String pathFull, String pathFormal, String minV, String minOut, double xFrom, double xTo,
                double yFrom, double yTo, double xParam, double yParam, double answerParam, String func, float coef, boolean isUp, String variables) {
        this.isMin = isMin;
        this.nameTask = nameTask;
        this.pathFull = pathFull;
        this.pathFormal = pathFormal;
        this.minV = minV;
        this.minOut = minOut;
        this.xFrom = xFrom;
        this.xTo = xTo;
        this.yFrom = yFrom;
        this.yTo = yTo;
        this.xParam = xParam;
        this.yParam = yParam;
        this.answerParam = answerParam;
        this.func = func;
        this.coef = coef;
        this.isUp = isUp;
        this.variables = variables;
    }

    public boolean isMin() {
        return isMin;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getPathFull() {
        return pathFull;
    }

    public String getPathFormal() {
        return pathFormal;
    }

    public String getMinV() {
        return minV;
    }

    public String getMinOut() {
        return minOut;
    }

    public double getxFrom() {
        return xFrom;
    }

    public double getxTo() {
        return xTo;
    }

    public double getyFrom() {
        return yFrom;
    }

    public double getyTo() {
        return yTo;
    }

    public double getxParam() {
        return xParam;
    }

    public double getyParam() {
        return yParam;
    }

    public double getAnswerParam() {
        return answerParam;
    }

    public String getFunc() {
        return func;
    }

    public float getCoef() {
        return coef;
    }

    public boolean isUp() {
        return isUp;
    }

    public String getVariables() {
        return variables;
    }
}