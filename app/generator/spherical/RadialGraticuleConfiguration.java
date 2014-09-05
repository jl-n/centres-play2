package generator.spherical;

import java.awt.*;

/**
 * Created by julian on 04/09/2014.
 */
public class RadialGraticuleConfiguration {
    int maxLat = 90;
    int minLat = -90;
    int maxLon = 180;
    int minLon = -180;
    int latitudeStep = 15;
    int longitudeStep = 15;



    public RadialGraticuleConfiguration(int maxLat, int minLat, int maxLon, int minLon, int latitudeStep, int longitudeStep, int smoothFactor) {
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

    public Color lineColor = Color.BLACK;
}

