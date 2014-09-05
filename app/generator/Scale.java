package generator;

public class Scale {
    int xDisplayAdjust;
    int yDisplayAdjust;
    double xProjAdjust;
    double yProjAdjust;
    double projToDisplayRatio;

    public Scale(int xDisplayAdjust, int yDisplayAdjust, double xProjAdjust, double yProjAdjust, double projToDisplayRatio) {
        this.xDisplayAdjust = xDisplayAdjust;
        this.yDisplayAdjust = yDisplayAdjust;
        this.xProjAdjust = xProjAdjust;
        this.yProjAdjust = yProjAdjust;
        this.projToDisplayRatio = projToDisplayRatio;
    }

    public int toDisplayX(double meters) {
        return xDisplayAdjust + (int)((meters + xProjAdjust) * projToDisplayRatio);
    }

    public int toDisplayY(double meters) {
        return yDisplayAdjust + (int)((meters - yProjAdjust) * -projToDisplayRatio);
    }

}
