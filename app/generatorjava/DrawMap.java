package generatorjava;

import generatorjava.spherical.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: matt
 * Date: 03/09/2013
 * Time: 15:19
 * To change this template use File | Settings | File Templates.
 */
public class DrawMap {

    private final static int LONGITUDE_GRATICULE_STEP = 15;
    private final static int LATITUDE_GRATICULE_STEP = 15;
    private final static int GRATICULE_SMOOTH_FACTOR = 1;


    public static void generate(DrawType drawType, double longitudeCenter, double latitudeCenter, String filePath, MapElementStyle style){
        if (filePath == null) filePath = "data/maps/output.svg";


        File outputFile = new File(filePath);


        MapConfiguration mapConfiguration = new MapConfiguration(85,-85,180,-180, drawType.getScale(), style);
        Map map = new Map();

//        File indexFile = drawType.getIndexFile();
//        File datFile = drawType.getDatFile();

        map.initialize(drawType.getIndexFile(), drawType.getDatFile());

        // Setup the Graticule
        GraticuleConfiguration graticuleConfiguration = new GraticuleConfiguration(mapConfiguration.maxLatitude,
                                                                                   mapConfiguration.minLatitude,
                                                                                   mapConfiguration.maxLongitude,
                                                                                   mapConfiguration.minLongitude,
                                                                                   LATITUDE_GRATICULE_STEP,
                                                                                   LONGITUDE_GRATICULE_STEP,
                                                                                   GRATICULE_SMOOTH_FACTOR);

        Graticule graticule = new Graticule(graticuleConfiguration);

        final List<Draw> mapElements = new ArrayList<Draw>();
        mapElements.add(map);
        mapElements.add(graticule);

        Projection projection = ProjectionFactory.getCenteredProjection(mapConfiguration, longitudeCenter, latitudeCenter);

        SvgGenerator.render(outputFile, mapElements, projection, mapConfiguration);

    }

    public static BufferedImage generateInMemory(DrawType drawType, double longitudeCenter, double latitudeCenter, MapElementStyle styles   ) throws Exception {
        MapConfiguration mapConfiguration = new MapConfiguration(85,-85,180,-180, drawType.getScale(), styles);
        Map map = new Map();

        map.initialize(drawType.getIndexFile(), drawType.getDatFile());

        GraticuleConfiguration graticuleConfiguration = new GraticuleConfiguration(mapConfiguration.maxLatitude,
                mapConfiguration.minLatitude,
                mapConfiguration.maxLongitude,
                mapConfiguration.minLongitude,
                LATITUDE_GRATICULE_STEP,
                LONGITUDE_GRATICULE_STEP,
                GRATICULE_SMOOTH_FACTOR);

        graticuleConfiguration.lineColor = styles.graticule.getColor();

        RadialGraticuleConfiguration radialGraticuleConfiguration = new RadialGraticuleConfiguration(mapConfiguration.maxLatitude,
                mapConfiguration.minLatitude,
                mapConfiguration.maxLongitude,
                mapConfiguration.minLongitude,
                LATITUDE_GRATICULE_STEP,
                LONGITUDE_GRATICULE_STEP,
                GRATICULE_SMOOTH_FACTOR);

        Graticule graticule = new Graticule(graticuleConfiguration);
        RadialGraticule radialgraticule = new RadialGraticule(radialGraticuleConfiguration);

        final List<Draw> mapElements = new ArrayList<Draw>();
        mapElements.add(map);
        mapElements.add(graticule); //Comment out to remove Lat Long on rendered map
        //mapElements.add(radialgraticule);

        Projection projection = ProjectionFactory.getCenteredProjection(mapConfiguration, longitudeCenter, latitudeCenter);

        return SvgGenerator.renderPNG( mapElements, projection, mapConfiguration);

    }


    public enum DrawType {
        SCREEN(1, "Low"), PRINT(5, "High");

        private int scale;
        private String dataFile;

        DrawType(int scale, String dataFile) {
            this.scale = scale;
            this.dataFile = dataFile;
        }

        public int getScale() {
            return scale;
        }

        public File getDatFile(){

            return new File("app/generatorjava/MapData/" + dataFile + "_DAT.LE");
        }

        public File getIndexFile(){
            return new File("app/generatorjava/MapData/" + dataFile + "_IND.LE");
        }
    }
}
