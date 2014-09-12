package generatorjava.spherical;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: matt
 * Date: 03/09/2013
 * Time: 14:47
 * To change this template use File | Settings | File Templates.
 */
public class GraticuleConfiguration {

    int maxLat = 90;
    int minLat = -90;
    int maxLon = 180;
    int minLon = -180;
    int latitudeStep = 15;
    int longitudeStep = 15;



    public GraticuleConfiguration(int maxLat, int minLat, int maxLon, int minLon, int latitudeStep, int longitudeStep, int smoothFactor) {
        this.maxLat = maxLat;
        this.minLat = minLat;
        this.maxLon = maxLon;
        this.minLon = minLon;
        this.latitudeStep = latitudeStep;
        this.longitudeStep = longitudeStep;
        this.smoothFactor = smoothFactor;

    }

    public final int mapMinLat = minLat * 60 * 60;
    public final int mapMaxLat = maxLat * 60 * 60;
    public final int mapMaxLon = maxLon * 60 * 60;
    public final int mapMinLon = minLon * 60 * 60;
    public final int latStep = latitudeStep * 60 * 60;
    public final int lonStep = longitudeStep * 60 * 60;
    public int smoothFactor = 5;

    public Color lineColor = Color.RED;
}
