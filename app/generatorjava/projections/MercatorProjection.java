package generatorjava.projections;

/**
 * Created by julian on 03/09/2014.
 */

import java.awt.geom.Point2D;


public class MercatorProjection extends CylindricalProjection {
    public MercatorProjection() {
        minLatitude = DTR *-85;
        maxLatitude = DTR * 85;
    }

    public Point2D.Double project(double lam, double phi, Point2D.Double out) {
        if (spherical)
        {
            out.x = scaleFactor * lam;
            out.y = scaleFactor * Math.log(Math.tan(ProjMath.QUARTERPI + 0.5 * phi));
        }
        else
        {
            out.x = scaleFactor * lam;
            out.y = -scaleFactor * Math.log(ProjMath.tsfn(phi, Math.sin(phi), e));
        }
        return out;
    }

    public Point2D.Double projectInverse(double x, double y, Point2D.Double out) {
        if (spherical) {
            out.y = ProjMath.HALFPI - 2. * Math.atan(Math.exp(-y / scaleFactor));
            out.x = x / scaleFactor;
        } else {
            out.y = ProjMath.phi2(Math.exp(-y / scaleFactor), e);
            out.x = x / scaleFactor;
        }
        return out;
    }

    public boolean hasInverse() {
        return true;
    }

    public boolean isRectilinear() {
        return true;
    }

    /**
     * Returns the ESPG code for this projection, or 0 if unknown.
     */
    public int getEPSGCode() {
        return 9804;
    }

    public String toString() {
        return "Mercator IB";
    }

    public int getProjParmsCode() { //fwp
        return 1;
    }
}

