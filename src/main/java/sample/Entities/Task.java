package sample.Entities;

public class Task {

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

    public Task(String nameTask, String pathFull, String pathFormal, String minV, String minOut, float xFrom, float xTo,
                float yFrom, float yTo, float xParam, float yParam, float answerParam, String func, float coef, boolean isUp) {
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

    public void setPathFull(String pathFull) {
        this.pathFull = pathFull;
    }

    public String getPathFormal() {
        return pathFormal;
    }

    public void setPathFormal(String pathFormal) {
        this.pathFormal = pathFormal;
    }

    public String getMinV() {
        return minV;
    }

    public void setMinV(String minV) {
        this.minV = minV;
    }

    public String getMinOut() {
        return minOut;
    }

    public void setMinOut(String minOut) {
        this.minOut = minOut;
    }

    public float getxFrom() {
        return xFrom;
    }

    public void setxFrom(float xFrom) {
        this.xFrom = xFrom;
    }

    public float getxTo() {
        return xTo;
    }

    public void setxTo(float xTo) {
        this.xTo = xTo;
    }

    public float getyFrom() {
        return yFrom;
    }

    public void setyFrom(float yFrom) {
        this.yFrom = yFrom;
    }

    public float getyTo() {
        return yTo;
    }

    public void setyTo(float yTo) {
        this.yTo = yTo;
    }

    public float getxParam() {
        return xParam;
    }

    public void setxParam(float xParam) {
        this.xParam = xParam;
    }

    public float getyParam() {
        return yParam;
    }

    public void setyParam(float yParam) {
        this.yParam = yParam;
    }

    public float getAnswerParam() {
        return answerParam;
    }

    public void setAnswerParam(float answerParam) {
        this.answerParam = answerParam;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public float getCoef() {
        return coef;
    }

    public void setCoef(float coef) {
        this.coef = coef;
    }

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }
}