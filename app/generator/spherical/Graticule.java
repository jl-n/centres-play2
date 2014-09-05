package generator.spherical;

import generator.*;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created with IntelliJ IDEA.
 * User: matt
 * Date: 03/09/2013
 * Time: 13:26
 * To change this template use File | Settings | File Templates.
 */
public class Graticule implements Draw {
    final private int mapMinLon;
    final private int mapMaxLon;
    final private int mapMinLat;
    final private int mapMaxLat;
    final private int latStep;
    final private int lonStep;
    final private int smoothFactor;

    public Graticule( int mapMinLon, int mapMaxLon,
                               int mapMinLat, int mapMaxLat,
                               int latStep,   int lonStep,
                               int smoothFactor ) {
        this.mapMinLon = mapMinLon;
        this.mapMaxLon = mapMaxLon;
        this.mapMinLat = mapMinLat;
        this.mapMaxLat = mapMaxLat;
        this.latStep = latStep;
        this.lonStep = lonStep;
        this.smoothFactor = smoothFactor;
    }

    public Graticule(GraticuleConfiguration configuration){
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
    public void render(Graphics2D display, Projection projection, int minX, int maxX, int minY, int maxY, Scale scale, MapElementStyle style)
    {
        final int midX = (minX + maxX) / 2;
        final int midY = (minY + maxY) / 2;

        double           cLat;            // Cartographic lat comming in
        double           cLon;            // Cartographic lon comming in
        Point2D.Double   pLatLon = new Point2D.Double();// Projected Lat/lon
        Point2D.Double   cLatLon = new Point2D.Double();// Cartesian Lat/Lon
        double           mapY;            // Map/screen view y coordinate
        double           mapX;            // Map/screen view x coordinate
        int lastX = 0;
        int lastY = 0;
        int thisX = 0;
        int thisY = 0;
        int lat = 0;
        int lon = 0;

        ProjectionScratchpad scratchpad = new ProjectionScratchpad();

        final BasicStroke basicStroke = new BasicStroke(1F, BasicStroke.JOIN_BEVEL, BasicStroke.CAP_BUTT);
        for (lon = (mapMinLon / lonStep) * lonStep ; lon <= mapMaxLon;  lon = lon + lonStep)
        {
            // Calculate the bottom point of each vertical longitude graticule line.
            cLon = lon / SECONDS_PER_DEGREE; // Convert seconds to degrees
            cLatLon.x = cLon;
            for (lat = (mapMinLat / latStep) * latStep; lat <= mapMaxLat; lat = lat + (latStep / smoothFactor))
            {
                cLat = lat / SECONDS_PER_DEGREE; // Convert seconds to degrees
                cLatLon.y = cLat;
                projection.project(cLatLon, pLatLon, scratchpad); // Do the forward projection
                mapX = pLatLon.x; // Get the projected values in meters
                mapY = pLatLon.y;
                thisX = scale.toDisplayX(mapX);
                thisY = scale.toDisplayY(mapY);


                if (lat != (mapMinLat / latStep) * latStep &&  lastX - thisX < midX && thisX - lastX < midX && lastY - thisY < midY && thisY - lastY < midY) {
                    display.setColor(Color.BLACK);
                    display.setStroke(basicStroke);
                    display.drawLine(lastX, lastY, thisX, thisY);
                }
                lastX = thisX;
                lastY = thisY;
            } // END For lat
            // Put mapMinLat back to initial value
        } // END For lon graticule

        // Draw the latitude lines of the graticule
        for (lat = (mapMinLat / latStep) * latStep; lat <= mapMaxLat;  lat = lat + latStep)
        {
            // Calculate the left point of each horizontal latitude graticule line.
            cLat = lat / SECONDS_PER_DEGREE; // Convert seconds to degrees
            cLatLon.y = cLat;
            for (lon = (mapMinLon / lonStep) * lonStep; lon <= mapMaxLon; lon = lon + (lonStep / smoothFactor))
            {
                cLon = lon / SECONDS_PER_DEGREE; // Convert seconds to degrees
                cLatLon.x = cLon;
                projection.project(cLatLon, pLatLon, scratchpad);       // Do the forward projection
                mapX = pLatLon.x;                    // Get the projected values in meters
                mapY = pLatLon.y;
                thisX = scale.toDisplayX(mapX);
                thisY = scale.toDisplayY(mapY);

                if (lon != (mapMinLon / lonStep) * lonStep && lastX - thisX < midX && thisX - lastX < midX && lastY - thisY < midY && thisY - lastY < midY) {
                    display.setColor(Color.BLACK);
                    display.setStroke(basicStroke);
                    display.drawLine(lastX, lastY, thisX, thisY);
                }
                lastX = thisX;
                lastY = thisY;
            } // END For lat

        } // END For lat graticule
    }
}
