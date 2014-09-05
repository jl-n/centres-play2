package generator;

import java.awt.*;


public class MapElementStyle {
    public MapElementStyle(String countryHex, String borderHex, String lakeHex, String seaHex, String graticuleHex){
        this.border = new Style(borderHex);
        this.country = new Style(countryHex);
        this.lake = new Style(lakeHex);
        this.sea = new Style(seaHex);
        this.graticule = new Style(graticuleHex);
    }

    public Style country;
    public Style border;
    public Style lake;
    public Style sea;
    public Style graticule;

    public class Style{
        public Style(String hexValue){
            this.hexValue = hexValue;
        }
        private String hexValue;
        public Color getColor(){
            return Color.decode(hexValue);
        }

    }
}
