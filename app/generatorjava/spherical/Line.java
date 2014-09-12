package generatorjava.spherical;

import generatorjava.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: matt
 * Date: 03/09/2013
 * Time: 13:22
 * To change this template use File | Settings | File Templates.
 */
public class Line {
    public Line(Kind kind, double maximumLatitude, double minimumLatitude, double maximumLongitude,  double minimumLongitude) {
        this.kind = kind;
        this.maximumLatitude = maximumLatitude;
        this.minimumLatitude = minimumLatitude;
        this.maximumLongitude = maximumLongitude;
        this.minimumLongitude = minimumLongitude;
    }
    public enum Kind {COAST, ISLAND, INTERNATIONAL_BORDER, POLITICAL_BORDER, LAKE, RIVER};

    public Kind kind;

    public double maximumLatitude;
    public double minimumLatitude;

    public double maximumLongitude;
    public double minimumLongitude;

    public List<Point> points = new ArrayList<Point>();

    public void addPoint(Point point) {
        points.add(point);
    }

    private static final boolean FILL = true;
    private static final boolean DO_NOT_FILL = false;

    public void render(Graphics2D display, Projection projection, int minX, int maxX, int minY, int maxY, Scale scale, int rand, MapElementStyle style) {
        ProjectionScratchpad scratchpad = new ProjectionScratchpad();

        if (visible(projection, minimumLongitude, maximumLongitude, minimumLatitude, maximumLatitude, scratchpad)) {

            final int size = points.size();
            int xPoints[] = new int[size];
            int yPoints[] = new int[size];
            int pointCount = 0;
            Point2D.Double pLatLon = new Point2D.Double();
            Point2D.Double cLatLon = new Point2D.Double();
            final BasicStroke basicStroke = new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

            Color[] colors = {new Color(0.47909194F, 0.20827466F, 0.77949196F),
                    new Color(0.84811723F, 0.025471032F, 0.09075558F),
                    new Color(0.9245226F, 0.5628461F, 0.9931298F),
                    new Color(0.433658F, 0.9567596F, 0.10179591F),
                    new Color(0.6697183F, 0.75373596F, 0.3925653F),
                    new Color(0.08614588F, 0.39165688F, 0.7116461F),
                    new Color(0.42070258F, 0.9925472F, 0.8404415F),
                    new Color(0.618439F, 0.7158707F, 0.78904647F),
                    new Color(0.064377725F, 0.55042773F, 0.9650085F),
                    new Color(0.34515464F, 0.74442476F, 0.4052155F),
                    new Color(0.8156863f, 0.3882353f, 0.2f),
                    new Color(0.5568628f, 0.60784316f, 0.8039216f),
                    new Color(0.29870206F, 0.89790034F, 0.29145354F),
                    new Color(0.32849622F, 0.11401802F, 0.1360842F),
                    new Color(0.18578005F, 0.55032516F, 0.4081183F),
                    new Color(0.59002924F, 0.4818495F, 0.58000755F),
                    new Color(0.74806255F, 0.22351414F, 0.060172915F),
                    new Color(0.043587565F, 0.03768587F, 0.45256478F),
                    new Color(0.46001625F, 0.37016958F, 0.45583898F),
                    new Color(0.34117648f, 0.05490196f, 0.9411765f),
                    new Color(0.56654364F, 0.5701058F, 0.39157867F),
                    new Color(0.8888126F, 0.6271956F, 0.25881195F),
                    new Color(0.17961067F, 0.25213158F, 0.58205086F)};

             for (Point point : points) {

                if (kind == Kind.RIVER)  {

                    break;
                }

                final double lambda = point.lambda;
                final double phi = point.phi;
                // Now we look for points which are inside of the AOI.
                 if (true || projection.inside(phi, lambda, scratchpad))
                {
                    cLatLon.y = lambda;
                    cLatLon.x = phi;
                    projection.project(cLatLon, pLatLon, scratchpad);//forward proj
                    xPoints[pointCount] = scale.toDisplayX(pLatLon.x);
                    yPoints[pointCount] = scale.toDisplayY(pLatLon.y);
                    pointCount = pointCount + 1;
                }

            }
            if (pointCount > 1) {
                Color color = Color.BLACK;
                switch (kind) {
                    case COAST:
                        color = Color.BLACK;
                        break;
                    case ISLAND:
                        color = style.country.getColor();
                        break;
                    case INTERNATIONAL_BORDER:
                        color = style.border.getColor();
                        break;
                    case POLITICAL_BORDER:
                        color = style.border.getColor();
                        break;
                    case LAKE:
                        color = style.lake.getColor();
                        break;
                    case RIVER:
                        color = style.lake.getColor();
                        break;
                }

                if (kind != Kind.COAST && kind != Kind.ISLAND && kind != Kind.LAKE && kind != Kind.INTERNATIONAL_BORDER)
                {
                    display.setStroke(basicStroke);

                    display.setColor(color);
                    drawPolys(display, minX, maxX, minY, maxY, xPoints, yPoints, pointCount, DO_NOT_FILL);


                }  else if (kind == Kind.LAKE){

                    display.setColor(color);

                    drawPolys(display, minX, maxX, minY, maxY, xPoints, yPoints, pointCount, FILL);
                }  else {

                    display.setColor(colors[rand % colors.length]);
                    rand++;

                    display.setColor(color);

                    drawPolys(display, minX, maxX, minY, maxY, xPoints, yPoints, pointCount, FILL);

                    if (kind == Kind.INTERNATIONAL_BORDER){
                        display.setColor(color);
                        drawPolys(display, minX, maxX, minY, maxY, xPoints, yPoints, pointCount, DO_NOT_FILL);
                    }
                }
            }
        }
    }


    private static void drawPolys(Graphics2D display, int minX, int maxX, int minY, int maxY, int[] xPoints, int[] yPoints, int pointCount, Boolean fill) {
        if (pointCount > 0 /* && pointCount == 413 || pointCount == 1017 /* && pointCount == 9282 */) {

            int minYPoint = Integer.MAX_VALUE;
            int maxYPoint = Integer.MIN_VALUE;

            int xPointsA[] = new int[pointCount];
            int yPointsA[] = new int[pointCount];


            int xPointsB[] = new int[pointCount+2];
            int yPointsB[] = new int[pointCount+2];

            final int midX = (minX + maxX) / 2;
            final int midY = (minY + maxY) / 2;

            xPointsA[0] = xPoints[0];
            yPointsA[0] = yPoints[0];
            int pointCountA = 1;

            for (int i = 1; i < pointCount; i++) {
                int xDeltaA = xPoints[i-1] - xPoints[i];
                int yDeltaA = yPoints[i-1] - yPoints[i];
                if (xDeltaA > midX || xDeltaA < -midX || yDeltaA > midY || yDeltaA < -midY) {
                    xPointsA[pointCountA - 1] = xPointsA[pointCountA - 1] < midX ? minX : maxX;
                    if (minYPoint > yPoints[i] ) {
                        minYPoint = yPoints[i];
                    }
                    if (maxYPoint < yPoints[i] ) {
                        maxYPoint = yPoints[i];
                    }
                    xPointsB[0] = xPoints[i] < midX ? minX : maxX;
                    yPointsB[0] = yPoints[i];
                    int pointCountB = 1;
                    for (i = i+1; i <pointCount; i++) {
                        int xDeltaB = xPoints[i - 1] - xPoints[i];
                        int yDeltaB = yPoints[i - 1] - yPoints[i];
                        if (xDeltaB > midX || xDeltaB < -midX|| yDeltaB > midY || yDeltaB < -midY) {
                            xPointsB[pointCountB - 1] = xPointsB[pointCountB - 1] < midX ? minX : maxX;
                            drawOrFillPoly(display, fill, xPointsB, yPointsB, pointCountB);
                            break;
                        } else {
                            xPointsB[pointCountB] = xPoints[i];
                            yPointsB[pointCountB] = yPoints[i];
                            pointCountB ++;
                        }
                    }
                    if (i >= pointCount) {
                        for (int j = pointCountB; j<pointCountA + pointCountB; j++) {
                            xPointsB[j] = xPointsA[j - pointCountB];
                            yPointsB[j] = yPointsA[j - pointCountB];
                        }

                        int yPole = yPoints[0] < midY ? minY : maxY;
                        xPointsB[pointCountA + pointCountB] = xPointsB[pointCountA + pointCountB - 1];
                        yPointsB[pointCountA + pointCountB] = yPole;
                        xPointsB[pointCountA + pointCountB + 1] = xPointsB[0];
                        yPointsB[pointCountA + pointCountB + 1] = yPole;

                        drawOrFillPoly(display, fill, xPointsB, yPointsB, pointCountA + pointCountB + 2);
                        return;
                    }
                    if (i < pointCount) {
                        if (minYPoint > yPoints[i] ) {
                            minYPoint = yPoints[i];
                        }
                        if (maxYPoint < yPoints[i] ) {
                            maxYPoint = yPoints[i];
                        }
                        xPointsA[pointCountA] = xPoints[i] < midX ? minX : maxX;
                        yPointsA[pointCountA] = yPoints[i];
                        pointCountA ++;
                    }
                } else {
                    if (i < pointCount) {
                        if (minYPoint > yPoints[i] ) {
                            minYPoint = yPoints[i];
                        }
                        if (maxYPoint < yPoints[i] ) {
                            maxYPoint = yPoints[i];
                        }
                        xPointsA[pointCountA] = xPoints[i];
                        yPointsA[pointCountA] = yPoints[i];
                        pointCountA ++;
                    }
                }
            }
            drawOrFillPoly(display, fill, xPointsA, yPointsA, pointCountA);
        }
    }

    private static void drawOrFillPoly(Graphics2D display, Boolean fill, int[] xPointsA, int[] yPointsA, int pointCountA) {
        if (fill) {
//                for (int j=0; j<pointCountA-1; j++) {
//                    float omega = (float)(pointCount-pointCountA+j)/pointCount;
//                    display.setColor(new Color(omega, omega, omega));
//                    display.drawLine(xPointsA[j], yPointsA[j], xPointsA[j+1], yPointsA[j+1]);
//                }
//                display.drawPolyline(xPointsA, yPointsA, pointCountA);
            display.fillPolygon(xPointsA, yPointsA, pointCountA);
        } else {
            display.drawPolyline(xPointsA, yPointsA, pointCountA);
        }
    }

    private boolean visible(Projection projection,
                            double minLon, double maxLon,
                            double minLat, double maxLat,
                            ProjectionScratchpad scratchpad)
    {
        return (projection.inside(maxLon, maxLat, scratchpad)
                || projection.inside(maxLon, minLat, scratchpad)
                || projection.inside(minLon, minLat, scratchpad)
                || projection.inside(minLon, maxLat, scratchpad));
    }

}