package generatorjava;

import generatorjava.projections.Ellipsoid;
import generatorjava.projections.MercatorProjection;
import generatorjava.projections.ProjectionBase;

public class ProjectionFactory {
    public static Projection getCenteredProjection(MapConfiguration mapConfiguration, double projectionLongitude, double projectionLatitude){
        return getMercatorCenteredProjection(mapConfiguration, projectionLongitude, projectionLatitude);
    }

    private static Projection getMercatorCenteredProjection(MapConfiguration mapConfiguration, double projectionLongitude, double projectionLatitude){
        ProjectionBase projection = new MercatorProjection();
        projection.setEllipsoid(Ellipsoid.SPHERE);
        projection.setMaxLatitude(mapConfiguration.maxLatitude);
        projection.setMinLatitude(mapConfiguration.minLatitude);
        projection.setMaxLongitude(mapConfiguration.maxLongitude);
        projection.setMinLongitude(mapConfiguration.minLongitude);
        projection.setProjectionLongitude(0D);
        projection.initialize();

        return new Projection(projection, projectionLongitude, projectionLatitude);
    }

}
