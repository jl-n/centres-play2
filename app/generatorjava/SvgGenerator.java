package generatorjava;

import generatorjava.spherical.Draw;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;


import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;


public class SvgGenerator {

    public static void render(File file, List<Draw> drawings, Projection projection, MapConfiguration mapConfiguration) {
        try {
            Writer out = new FileWriter(file);
            render(out, drawings, projection, mapConfiguration.width, mapConfiguration.height, mapConfiguration.xAdjust, mapConfiguration.yAdjust, mapConfiguration.getScaledProjectionRatio());
        } catch (Exception ioe) {
            ioe.printStackTrace(System.err);
        }
    }

    public static void render(List<Draw> drawings, Projection projection, int width, int height, double xAdjust, double yAdjust, double projToDisplayRatio) {
        try {
            Writer out = new OutputStreamWriter(System.out, "UTF-8");
            render(out, drawings, projection, width, height, xAdjust, yAdjust, projToDisplayRatio);
        } catch (Exception ioe) {
            ioe.printStackTrace(System.err);
        }
    }

    public static void render(Writer writer, List<Draw> drawings, Projection projection, int width, int height, double xAdjust, double yAdjust, double projToDisplayRatio) throws Exception {
        MapElementStyle style = new MapElementStyle("#E6E6E6" , "#000000", "#0000FF",   "#0000FF", "#000000");


                // Get a DOMImplementation.
        DOMImplementation domImpl =
                SVGDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document.
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);

        // Create an instance of the SVG Generator.
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        final Point2D.Double maxLatLon = new Point2D.Double();
        final Point2D.Double minLatLon = new Point2D.Double();

        projection.northwestmost(maxLatLon);
        projection.southeastmost(minLatLon);

        final double maxLonMeters = maxLatLon.x;
        final double maxLatMeters = maxLatLon.y;
        final double minLonMeters = minLatLon.x;
        final double minLatMeters = minLatLon.y;

        Scale scale = new Scale(width / 2, height / 2, xAdjust, yAdjust, projToDisplayRatio);

        final int maxX = scale.toDisplayX(maxLonMeters);
        final int minY = scale.toDisplayY(maxLatMeters);
        final int minX = scale.toDisplayX(minLonMeters);
        final int maxY = scale.toDisplayY(minLatMeters );

        for (Draw drawing : drawings) {
            drawing.render(svgGenerator, projection, minX, maxX, minY, maxY, scale, style);
        }

        boolean useCSS = true; // we want to use CSS style attributes
        svgGenerator.stream(writer, useCSS);
        writer.flush();


    }

    public static BufferedImage renderPNG(List<Draw> drawings, Projection projection, MapConfiguration mapConfiguration) throws Exception {

        BufferedImage bImg = new BufferedImage(mapConfiguration.width, mapConfiguration.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D cg = bImg.createGraphics();
        final Point2D.Double maxLatLon = new Point2D.Double();
        final Point2D.Double minLatLon = new Point2D.Double();

        projection.northwestmost(maxLatLon);
        projection.southeastmost(minLatLon);

        final double maxLonMeters = maxLatLon.x;
        final double maxLatMeters = maxLatLon.y;
        final double minLonMeters = minLatLon.x;
        final double minLatMeters = minLatLon.y;

        Scale scale = new Scale(mapConfiguration.width / 2, mapConfiguration.height / 2, mapConfiguration.xAdjust, mapConfiguration.yAdjust, mapConfiguration.getScaledProjectionRatio());

        final int maxX = scale.toDisplayX(maxLonMeters);
        final int minY = scale.toDisplayY(maxLatMeters);
        final int minX = scale.toDisplayX(minLonMeters);
        final int maxY = scale.toDisplayY(minLatMeters );

        for (Draw drawing : drawings) {
            drawing.render(cg, projection, minX, maxX, minY, maxY, scale, mapConfiguration.mapElementStyle);
        }

        return bImg;

    }
}
