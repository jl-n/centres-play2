package generator.projections;

/**
 * Created with IntelliJ IDEA.
 * User: matt
 * Date: 03/09/2013
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */
public class ProjMath {
    public final static double HALFPI = Math.PI/2.0;
    public final static double QUARTERPI = Math.PI/4.0;
    public final static double TWOPI = Math.PI*2.0;
    public final static double RTD = 180.0/Math.PI;
    public final static double DTR = Math.PI/180.0;

// Degree versions of trigonometric functions

//	public static double asind(double v) {
//		return Math.asin(v) * RTD;
//	}

    public static double asin(double v) {
        if (Math.abs(v) > 1.)
            return v < 0.0 ? -Math.PI/2 : Math.PI/2;
        return Math.asin(v);
    }

    public static double acos(double v) {
        if (Math.abs(v) > 1.)
            return v < 0.0 ? Math.PI : 0.0;
        return Math.acos(v);
    }

    public static double distance(double dx, double dy) {
        return Math.sqrt(dx*dx+dy*dy);
    }

    public static double normalizeLatitude(double angle) {
        if (Double.isInfinite(angle) || Double.isNaN(angle))
            throw new ProjectionException("Infinite latitude");
        while (angle > ProjMath.HALFPI)
            angle -= Math.PI;
        while (angle < -ProjMath.HALFPI)
            angle += Math.PI;
        return angle;
    }


    public static double normalizeLongitude(double angle) {
        if (Double.isInfinite(angle) || Double.isNaN(angle))
            throw new ProjectionException("Infinite longitude");
        while (angle > Math.PI)
            angle -= TWOPI;
        while (angle < -Math.PI)
            angle += TWOPI;
        return angle;
    }

    public static double greatCircleDistance(double lon1, double lat1,
                                             double lon2, double lat2 ) {
        double dlat = Math.sin((lat2-lat1)/2);
        double dlon = Math.sin((lon2-lon1)/2);
        double r = Math.sqrt(dlat*dlat + Math.cos(lat1)*Math.cos(lat2)*dlon*dlon);
        return 2.0 * Math.asin(r);
    }

    public static double tsfn(double phi, double sinphi, double e) {
        sinphi *= e;
        return (Math.tan (.5 * (ProjMath.HALFPI - phi)) /
                Math.pow((1. - sinphi) / (1. + sinphi), .5 * e));
    }

    public static double msfn(double sinphi, double cosphi, double es) {
        return cosphi / Math.sqrt(1.0 - es * sinphi * sinphi);
    }

    private final static int N_ITER = 15;

    public static double phi2(double ts, double e) {
        double eccnth, phi, con, dphi;
        int i;

        eccnth = .5 * e;
        phi = ProjMath.HALFPI - 2. * Math.atan(ts);
        i = N_ITER;
        do {
            con = e * Math.sin(phi);
            dphi = ProjMath.HALFPI - 2. * Math.atan(ts * Math.pow((1. - con) / (1. + con), eccnth)) - phi;
            phi += dphi;
        } while (Math.abs(dphi) > 1e-10 && --i != 0);
        if (i <= 0)
            throw new ProjectionException();
        return phi;
    }

    private final static double C00 = 1.0;
    private final static double C02 = .25;
    private final static double C04 = .046875;
    private final static double C06 = .01953125;
    private final static double C08 = .01068115234375;
    private final static double C22 = .75;
    private final static double C44 = .46875;
    private final static double C46 = .01302083333333333333;
    private final static double C48 = .00712076822916666666;
    private final static double C66 = .36458333333333333333;
    private final static double C68 = .00569661458333333333;
    private final static double C88 = .3076171875;
    private final static int MAX_ITER = 10;

    public static double[] enfn(double es) {
        double t;
        double[] en = new double[5];
        en[0] = C00 - es * (C02 + es * (C04 + es * (C06 + es * C08)));
        en[1] = es * (C22 - es * (C04 + es * (C06 + es * C08)));
        en[2] = (t = es * es) * (C44 - es * (C46 + es * C48));
        en[3] = (t *= es) * (C66 - es * C68);
        en[4] = t * es * C88;
        return en;
    }

    public static double mlfn(double phi, double sphi, double cphi, double[] en) {
        cphi *= sphi;
        sphi *= sphi;
        return en[0] * phi - cphi * (en[1] + sphi*(en[2] + sphi*(en[3] + sphi*en[4])));
    }

    public static double inv_mlfn(double arg, double es, double[] en) {
        double s, t, phi, k = 1./(1.-es);

        phi = arg;
        for (int i = MAX_ITER; i != 0; i--) {
            s = Math.sin(phi);
            t = 1. - es * s * s;
            phi -= t = (mlfn(phi, s, Math.cos(phi), en) - arg) * (t * Math.sqrt(t)) * k;
            if (Math.abs(t) < 1e-11)
                return phi;
        }
        return phi;
    }


    private final static double P00 = .33333333333333333333;
    private final static double P01 = .17222222222222222222;
    private final static double P02 = .10257936507936507936;
    private final static double P10 = .06388888888888888888;
    private final static double P11 = .06640211640211640211;
    private final static double P20 = .01641501294219154443;

    public static double[] authset(double es) {
        double t;
        double[] APA = new double[3];
        APA[0] = es * P00;
        t = es * es;
        APA[0] += t * P01;
        APA[1] = t * P10;
        t *= es;
        APA[0] += t * P02;
        APA[1] += t * P11;
        APA[2] = t * P20;
        return APA;
    }

    public static double authlat(double beta, double []APA) {
        double t = beta+beta;
        return(beta + APA[0] * Math.sin(t) + APA[1] * Math.sin(t+t) + APA[2] * Math.sin(t+t+t));
    }

    public static double qsfn(double sinphi, double e, double one_es) {
        double con;

        if (e >= 1.0e-7) {
            con = e * sinphi;
            return (one_es * (sinphi / (1. - con * con) -
                    (.5 / e) * Math.log ((1. - con) / (1. + con))));
        } else
            return (sinphi + sinphi);
    }
}
