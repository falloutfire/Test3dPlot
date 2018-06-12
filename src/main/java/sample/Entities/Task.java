package sample.Entities;

public class Task {

    private boolean isMin;
    private String nameTask;
    private String pathFull;
    private String pathFormal;
    private String minV;
    private String minOut;
    private float xFrom;
    private float xTo;
    private float yFrom;
    private float yTo;
    private float xParam;
    private float yParam;
    private float answerParam;
    private String func;
    private float coef;
    private boolean isUp;
    private String variables;

    public Task(boolean isMin, String nameTask, String pathFull, String pathFormal, String minV, String minOut, float xFrom, float xTo,
                float yFrom, float yTo, float xParam, float yParam, float answerParam, String func, float coef, boolean isUp, String variables) {
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

    public float getxFrom() {
        return xFrom;
    }

    public float getxTo() {
        return xTo;
    }

    public float getyFrom() {
        return yFrom;
    }

    public float getyTo() {
        return yTo;
    }

    public float getxParam() {
        return xParam;
    }

    public float getyParam() {
        return yParam;
    }

    public float getAnswerParam() {
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

    public void setUp(boolean up) {
        isUp = up;
    }

    public String getVariables() {
        return variables;
    }

    public boolean isMin() {
        return isMin;
    }
}