package generator;

public class MapConfiguration {

    public MapConfiguration(){

    }

    public MapConfiguration(int maxLatitude, int minLatitude, int maxLongitude, int minLongitude, double scale, MapElementStyle styles){
        this.maxLatitude = maxLatitude;
        this.minLatitude = minLatitude;
        this.maxLongitude = maxLongitude;
        this.minLongitude = minLongitude;
        this.scale = scale;

        this.mapElementStyle = styles;
    }

    int maxLatitude = 85;
    int minLatitude = -85;
    int maxLongitude = 180;
    int minLongitude = -180;
    double scale = 100;


    int width = new Double(941 ).intValue();
    int height = new Double(594 ).intValue();
    double xAdjust = 0.0; //offset the image
    double yAdjust = 0.0;

    double projToDisplayRatio = 0.2E-4;  //original value 1.862052817063924E-5

    public MapElementStyle mapElementStyle;

    public double getScaledProjectionRatio(){
        return projToDisplayRatio*scale;
    }
}
