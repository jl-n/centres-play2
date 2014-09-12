package generatorjava;


public class TiltTranslation {
    final double [][] rotation;

    public TiltTranslation(double originLongitude, double originLatitude) {
        final double [][] latitudeRotation = yRotation(originLatitude);
        final double [][] longitudeRotation = zRotation(-originLongitude);
//        rotation = multiply(longitudeRotation, latitudeRotation);
        rotation = multiply(latitudeRotation, longitudeRotation);
    }

    private double [][] xRotation(double degrees) {
        double radians = Math.toRadians(degrees);
        final double s = Math.sin(radians);
        final double c = Math.cos(radians);

        final double [][] rotation = {{ 1, 0, 0},
                { 0, c,-s},
                { 0, s, c}};
        return rotation;
    }

    private double [][] yRotation(double degrees) {
        double radians = Math.toRadians(degrees);
        final double s = Math.sin(radians);
        final double c = Math.cos(radians);

        final double [][] rotation = {{ c, 0, s},
                { 0, 1, 0},
                {-s, 0, c}};
        return rotation;
    }

    private double [][] zRotation(double degrees) {
        double radians = Math.toRadians(degrees);
        final double s = Math.sin(radians);
        final double c = Math.cos(radians);

        final double [][] rotation = {{ c,-s, 0},
                { s, c, 0},
                { 0, 0, 1}};
        return rotation;
    }

    public Coordinates translate(Coordinates from, TiltTranslationScratchpad scratchpad, Coordinates toCoordinates) {

        // Degrees in & out

        // http://stackoverflow.com/questions/1185408/converting-from-longitude-latitude-to-cartesian-coordinates

        double fromLongitudeRadians = Math.toRadians(from.getLongitude());
        double fromLatitudeRadians = Math.toRadians(from.getLatitude());

        double cosFromLatitude = Math.cos(fromLatitudeRadians);

        scratchpad.from3D[0] = cosFromLatitude * Math.cos(fromLongitudeRadians);
        scratchpad.from3D[1] = cosFromLatitude * Math.sin(fromLongitudeRadians);
        scratchpad.from3D[2] = Math.sin(fromLatitudeRadians);

        multiply(rotation, scratchpad.from3D, scratchpad.to3D);

        toCoordinates.longitude = Math.toDegrees(Math.atan2(scratchpad.to3D[1], scratchpad.to3D[0]));
        toCoordinates.latitude = Math.toDegrees(Math.asin(scratchpad.to3D[2]));
        return toCoordinates;
    }

    // http://introcs.cs.princeton.edu/java/22library/Matrix.java.html

    // matrix-vector multiplication (y = A * x)
    private static double[] multiply(double[][] A, double[] x, double[] y) {
        int m = A.length;
        int n = A[0].length;
        if (x.length != n) throw new RuntimeException("Illegal matrix dimensions.");
        for (int i = 0; i < m; i++) {
            y[i] = 0D;
            for (int j = 0; j < n; j++)
                y[i] += (A[i][j] * x[j]);
        }
        return y;
    }

    // return C = A * B
    private static double[][] multiply(double[][] A, double[][] B) {
        int mA = A.length;
        int nA = A[0].length;
        int mB = B.length;
        int nB = A[0].length;
        if (nA != mB) throw new RuntimeException("Illegal matrix dimensions.");
        double[][] C = new double[mA][nB];
        for (int i = 0; i < mA; i++)
            for (int j = 0; j < nB; j++)
                for (int k = 0; k < nA; k++)
                    C[i][j] += (A[i][k] * B[k][j]);
        return C;
    }


}
