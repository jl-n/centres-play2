package generatorjava;

import generatorjava.projections.ProjectionBase;

import java.awt.geom.Point2D;


public class Projection {
    final private ProjectionBase jmplp;
    final private TiltTranslation tilt;
    public Projection(ProjectionBase jmplp, Double longitude, Double latitude) {
        this.jmplp = jmplp;
        this.tilt = new TiltTranslation(longitude, latitude);
    }

    public boolean inside(double x, double y, ProjectionScratchpad scratchpad) {
        scratchpad.fromCoordinates.longitude = x;
        scratchpad.fromCoordinates.latitude = y;
        tilt.translate(scratchpad.fromCoordinates, scratchpad.tiltTranslationScratchpad, scratchpad.toCoordinates);
        return jmplp.inside(scratchpad.toCoordinates.getLongitude(), scratchpad.toCoordinates.getLatitude());
    }

    public Point2D.Double project(Point2D.Double src, Point2D.Double dst, ProjectionScratchpad scratchpad) {
        scratchpad.fromCoordinates.longitude = src.getX();
        scratchpad.fromCoordinates.latitude = src.getY();
        tilt.translate(scratchpad.fromCoordinates, scratchpad.tiltTranslationScratchpad, scratchpad.toCoordinates);
        scratchpad.translatedPoint.setLocation(scratchpad.toCoordinates.getLongitude(), scratchpad.toCoordinates.getLatitude());
        return jmplp.project(scratchpad.translatedPoint, dst);
    }

    public Point2D.Double northwestmost(Point2D.Double result)  {
        final Point2D.Double northwestmost = new Point2D.Double();
        northwestmost.setLocation(jmplp.getMaxLongitude(), jmplp.getMaxLatitude());
        return jmplp.project(northwestmost, result);
    }

    public Point2D.Double southeastmost(Point2D.Double result)  {
        final Point2D.Double southeastmost = new Point2D.Double();
        southeastmost.setLocation(jmplp.getMinLongitude(), jmplp.getMinLatitude());
        return jmplp.project(southeastmost, result);
    }

}
