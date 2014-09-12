package generatorjava.spherical;

import generatorjava.*;
import play.Logger;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by julian on 04/09/2014.
 */
public class RadialGraticule implements Draw {

    final private int mapMinLon;
    final private int mapMaxLon;
    final private int mapMinLat;
    final private int mapMaxLat;
    final private int latStep;
    final private int lonStep;
    final private int smoothFactor;


    ///Takes in each parameter as an object
    public RadialGraticule(RadialGraticuleConfiguration configuration) {
        this.mapMinLat = configuration.mapMinLat;
        this.mapMaxLat = configuration.mapMaxLat;
        this.mapMinLon = configuration.mapMinLon;
        this.mapMaxLon = configuration.mapMaxLon;
        this.latStep = configuration.latStep;
        this.lonStep = configuration.lonStep;
        this.smoothFactor = configuration.smoothFactor;
    }

    static final private int SECONDS_PER_DEGREE = 3600;

    @Override
    public void render(Graphics2D display, Projection projection, int minX, int maxX, int minY, int maxY, Scale scale, MapElementStyle style) {

        double cLat;            // Cartographic lat coming in
        double cLon;            // Cartographic lon coming in
        Point2D.Double pLatLon = new Point2D.Double();// Projected Lat/lon
        Point2D.Double cLatLon = new Point2D.Double();// Cartesian Lat/Lon
        cLatLon.x = 0;
        cLatLon.y = 0;
        double mapY;            // Map/screen view y coordinate
        double mapX;            // Map/screen view x coordinate
        int lastX = 0;
        int lastY = 0;
        int thisX = 0;
        int thisY = 0;
        int lat = 0;
        int lon = 0;

        double smoothFactor = 0.07;

        ProjectionScratchpad scratchpad = new ProjectionScratchpad();

        final BasicStroke basicStroke = new BasicStroke(1F, BasicStroke.JOIN_BEVEL, BasicStroke.CAP_BUTT);

        //cLatLon.x = cLon;
        projection.project(cLatLon, pLatLon, scratchpad);       // Do the forward projection
        mapX = pLatLon.x;                    // Get the projected values in meters
        mapY = pLatLon.y;
        final int midX = scale.toDisplayX(mapX);
        final int midY = scale.toDisplayY(mapY);


        // Calculate the left point of each horizontal latitude graticule line.
        cLat = lat / SECONDS_PER_DEGREE; // Convert seconds to degrees
        cLatLon.y = cLat;


        int maxNumCircles = 100;

        for (int numOfCircles = 1; numOfCircles != maxNumCircles; numOfCircles++) {

            //Logger.debug(Double.toString(numOfCircles) + "   :numofcircles"); //debug

            for (double step = 0; step <= 2 * Math.PI + smoothFactor; step = step + smoothFactor) {
                //cLon = (lon / SECONDS_PER_DEGREE) * Math.PI / 180; // Convert seconds to degrees then to pi radians

                //double radianStep = cLon / (2 * Math.PI);

                thisX = (int) (midX + (Math.log(numOfCircles * .5) * 200) * (Math.cos(step)));
                thisY = (int) (midY + (Math.log(numOfCircles * .5) * 200) * (Math.sin(step)));

                //Logger.debug(Integer.toString(thisX) + "   :thisX"); //debug
                //Logger.debug(Integer.toString(thisY) + "   :thisY"); //debug
                Logger.debug(Double.toString(step) + "   :step"); //debug


                //if the longitude is not equal to the min long step and the difference in X from previous iter is less than middle of canvas and the same with Y draw the graticule

                //Previous statement:   lon != (mapMinLon / lonStep) * lonStep && lastX - thisX < midX && thisX - lastX < midX && lastY - thisY < midY && thisY - lastY < midY
                if (step != 0) {
                    display.setColor(Color.BLACK);
                    display.setStroke(basicStroke);
                    display.drawLine(lastX, lastY, thisX, thisY);
                }

                //Make last X Y values equal current X Y values for next iteration
                lastX = thisX;
                lastY = thisY;
            } // END For lat
        }


    }

}

