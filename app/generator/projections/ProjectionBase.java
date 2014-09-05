package generator.projections;

import java.awt.*;
import java.awt.geom.*;

// The superclass for all map projections
public class ProjectionBase {
//  public class Projection implements Cloneable {

    //The minimum latitude of the bounds of this projection in radians
    protected double minLatitude = -Math.PI/2; // -90 degrees

    // The minimum longitude of the bounds of this projection in radians
    protected double minLongitude = -Math.PI; // -180 degrees

    // The maximum latitude of the bounds of this projection in radians
    protected double maxLatitude = Math.PI/2;

    // The maximum longitude of the bounds of this projection in radians
    protected double maxLongitude = Math.PI;

    // The latitude of the centre of projection in radians
    protected double projectionLatitude = 0.0;

    // The longitude of the centre of projection in radians
    protected double projectionLongitude = 0.0;

    // Standard parallel 1 (for projections which use it) in radians
    protected double projectionLatitude1 = 0.0;

    // Standard parallel 2 (for projections which use it) in radians
    protected double projectionLatitude2 = 0.0;

    // The projection scale factor
    protected double scaleFactor = 1.0;

    // The false Easting of this projection in meters
    protected double falseEasting = 0;

    // The false Northing of this projection in meters
    protected double falseNorthing = 0;

    // The latitude of true scale. Only used by specific projections in radians
    protected double trueScaleLatitude = 0.0;

    // The equator radius in meters
    protected double a = 0;

    // The eccentricity
    protected double e = 0;

    /// The eccentricity squared
    protected double es = 0;

    // 1-(eccentricity squared)
    protected double one_es = 0;

    // 1/(1-(eccentricity squared))
    protected double rone_es = 0;

    // The ellipsoid used by this projection
    protected Ellipsoid ellipsoid;

    // True if this projection is using a sphere (es == 0)
    protected boolean spherical;

    // True if this projection is geocentric
    protected boolean geocentric;

    // The name of this projection
    protected String name = null;

    // Conversion factor from metres to whatever units the projection uses.
    protected double fromMetres = 1;

    // The total scale factor = Earth radius * units
    private double  totalScale = 0;

    // Landsat satellite number - fwp
    protected int landsatNumber = 1; // 1 to 5

    // Landsat satellite path number = fwp
    protected int landsatPath = 1; // 1 to 251


    // Some useful constants
    protected final static double EPS10 = 1e-10;
    protected final static double RTD = 180.0/Math.PI;
    protected final static double DTR = Math.PI/180.0;

    protected ProjectionBase() {
        setEllipsoid( Ellipsoid.SPHERE );
    }


    public Object clone() {
        try {
            ProjectionBase e = (ProjectionBase)super.clone();
            return e;
        }
        catch ( CloneNotSupportedException e ) {
            throw new InternalError();
        }
    }


    // The method which actually does the projection. This should be overridden//
    // for all projections.  x and y are in radians.
    public Point2D.Double project(double x, double y, Point2D.Double dst) {
        dst.x = x;
        dst.y = y;
        return dst;
    }

    // Project a lat/long point (in degrees), producing a result in metres //
    public Point2D.Double project( Point2D.Double src, Point2D.Double dst ) {
        double x = src.x*DTR;
        if ( projectionLongitude != 0 )
            x = ProjMath.normalizeLongitude( x-projectionLongitude );
        project(x, src.y*DTR, dst);
        dst.x = totalScale * dst.x + falseEasting;
        dst.y = totalScale * dst.y + falseNorthing;
        return dst;
    }


    // The method which actually does the inverse projection. This should
    // be overridden for all projections.//
    public Point2D.Double projectInverse(double x, double y, Point2D.Double dst) {
        dst.x = x;
        dst.y = y;
        return dst;
    }

    // Inverse-project a point (in metres), producing a lat/long result in degrees
    public Point2D.Double projectInverse(Point2D.Double src,
                                         Point2D.Double dst) {
        double x = (src.x - falseEasting) / totalScale;
        double y = (src.y - falseNorthing) / totalScale;
        projectInverse(x, y, dst);
        if (dst.x < -Math.PI)
            dst.x = -Math.PI;
        else if (dst.x > Math.PI)
            dst.x = Math.PI;
        if (projectionLongitude != 0)
            dst.x = ProjMath.normalizeLongitude(dst.x+projectionLongitude);
        dst.x *= RTD;
        dst.y *= RTD;
        return dst;
    }

    // Returns true if this projection is conformal
    public boolean isConformal() {//
        return false;
    }

    // Returns true if this projection is equal area
    public boolean isEqualArea() {//
        return false;
    }

    // Returns true if this projection has an inverse
    public boolean hasInverse() {//
        return false;
    }

    // Returns true if lat/long lines form a rectangular grid for this projection
    public boolean isRectilinear() {//
        return false;
    }

    // Returns true if latitude lines are parallel for this projection
    public boolean parallelsAreParallel() {//
        return isRectilinear();
    }

    // Returns true if the given lat/long point is Area Of Interest (AOI)
    // The lat/lon parameters are degrees //fwp
    public boolean inside(double x, double y)
    {
        if(minLongitude < maxLongitude) // Normal map
            return minLongitude <= x * DTR && x * DTR <= maxLongitude
                    && minLatitude <= y * DTR && y * DTR <= maxLatitude;
        else // check AOI which spans the 180 degree longitude
        { // check positive side
            if (minLongitude <= x * DTR && x <= 180.0
                    && minLatitude <= y * DTR && y * DTR <= maxLatitude)
                return true;
            // check negative side
            if (-180.0 <= x && x * DTR <= maxLongitude
                    && minLatitude <= y * DTR && y * DTR <= maxLatitude)
                return true;
        }
        return false; // not in either positive or negative side
    }

    // Set the name of this projection.
    public void setName( String name ) {//
        this.name = name;
    }

    public String getName() {//
        if ( name != null )
            return name;
        return toString();
    }

    public String toString() {  //
        return "None";
    }

    public void setMaxLatitude( double maxLatitude ) {// Added fwp
        this.maxLatitude = DTR * maxLatitude;
    }

    public double getMaxLatitude() { //
        return maxLatitude * RTD;
    }

    public void setMinLatitude( double minLatitude ) {// Added fwp
        this.minLatitude = DTR * minLatitude;
    }

    public double getMinLatitude() {//
        return minLatitude * RTD;
    }

    public void setMinLongitude( double minLongitude ) {//
        this.minLongitude = DTR * minLongitude;
    }

    public double getMinLongitude() {//
        return minLongitude * RTD;
    }

    public void setMaxLongitude( double maxLongitude ) {//
        this.maxLongitude = DTR * maxLongitude;
    }

    public double getMaxLongitude() {//
        return maxLongitude * RTD;
    }

    // Set the projection latitude in degrees.//
    public void setProjectionLatitude( double projectionLatitude ) {//
        this.projectionLatitude = DTR * projectionLatitude;
    }

    public double getProjectionLatitude() {//
        return projectionLatitude * RTD;
    }

    // Set the projection longitude in degrees.
    public void setProjectionLongitude( double projectionLongitude ) {//
        this.projectionLongitude = DTR * projectionLongitude;
    }

    public double getProjectionLongitude() {//
        return projectionLongitude * RTD;
    }

    // Set the latitude of true scale in degrees. This is only used by
    // certain projections.
    public void setTrueScaleLatitude( double trueScaleLatitude ) {//
        this.trueScaleLatitude = DTR * trueScaleLatitude;
    }

    public double getTrueScaleLatitude() {//
        return trueScaleLatitude * RTD;
    }

    // Set the projection latitude in degrees.
    public void setProjectionLatitude1( double projectionLatitude1 ) {//
        this.projectionLatitude1 = DTR * projectionLatitude1;
    }

    public double getProjectionLatitude1() {//
        return projectionLatitude1 * RTD;
    }

    // Set the projection latitude in degrees.
    public void setProjectionLatitude2( double projectionLatitude2 ) {//
        this.projectionLatitude2 = DTR * projectionLatitude2;
    }

    public double getProjectionLatitude2() {//
        return projectionLatitude2 * RTD;
    }

    // Set the false Northing in projected units.
    public void setFalseNorthing( double falseNorthing ) {//
        this.falseNorthing = falseNorthing;
    }

    public double getFalseNorthing() {//
        return falseNorthing;
    }

    // Set the false Easting in projected units.
    public void setFalseEasting( double falseEasting ) {//
        this.falseEasting = falseEasting;
    }

    public double getFalseEasting() {//
        return falseEasting;
    }

    // Set the projection scale factor. This is set to 1 by default.
    public void setScaleFactor( double scaleFactor ) {//
        this.scaleFactor = scaleFactor;
    }

    public double getScaleFactor() {//
        return scaleFactor;
    }

    public double getEquatorRadius() {//
        return a;
    }

    public void setLandsatNumber(int number) {  //fwp
        landsatNumber = number;
    }

    public int getLandsatNumber(){ //fwp
        return landsatNumber;
    }

    public void setLandsatPath(int number) { //fwp
        landsatPath = number;
    }

    public int getLandsatPath(){ //fwp
        return landsatPath;
    }

    // Set the conversion factor from metres to projected units. This is
    // set to 1 by default.
    public void setFromMetres( double fromMetres ) {//
        this.fromMetres = fromMetres;
    }

    public double getFromMetres() {//
        return fromMetres;
    }

    public void setEllipsoid( Ellipsoid ellipsoid ) {//
        this.ellipsoid = ellipsoid;
        a = ellipsoid.equatorRadius;
        e = ellipsoid.eccentricity;
        es = ellipsoid.eccentricity2;
    }

    public Ellipsoid getEllipsoid() {//
        return ellipsoid;
    }

    // Initialize the projection. This should be called after setting
    // parameters and before using the projection.
    // This is for performance reasons as initialization may be expensive.
    public void initialize() {//
        spherical = e == 0.0;
        one_es = 1-es;
        rone_es = 1.0/one_es;
        totalScale = a * fromMetres;
    }

    // This must be overridden by all projections. fwp
    public int getProjParmsCode() {
        return 0;
    }

    // This is only used by the azmuthal projections.
    // It specifies the extent of the visable map in degrees from the
    // intersection of the projection latitude and longitude. //fwp
    protected double	mapRadius = 180.0;

    public void setMapRadius(double mapRadius) {
        this.mapRadius = mapRadius;
    }
    public double getMapRadius() {
        return mapRadius;
    }

    // Altitude above the earth - used only be perspective projection
    protected double projectionAltitude = 6371008.7714; // sphere raidus meters

    public void setProjectionAltitude(double altitude) {
        this.projectionAltitude = projectionAltitude;
    }
    public double getProjectionAltitude() {
        return projectionAltitude;
    }


}

