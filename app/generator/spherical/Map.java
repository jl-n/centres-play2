package generator.spherical;

import generator.MapElementStyle;
import generator.Projection;
import generator.Scale;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: matt
 * Date: 03/09/2013
 * Time: 13:20
 * To change this template use File | Settings | File Templates.
 */
public class Map implements Draw {
    public void initialize(File indexPath, File dataPath){
        // Structure of MWDB index file records - 16 bytes
        int   recNum;
        short numPoints;
        short lineType;
        short maxLat;
        short minLat;
        short maxLon;
        short minLon;
        try
        {

            // Set up the index file
            FileChannel indexChannel = null;
            ByteBuffer indexBuffer = ByteBuffer.allocate(16);
            indexBuffer.order(ByteOrder.LITTLE_ENDIAN);

            // Structure of MWDB data file records - 4 bytes
            short lat;
            short lon;

            // Set up the coordinate data file.
            FileChannel dataChannel = null;
            ByteBuffer dataBuffer = ByteBuffer.allocate(4);
            dataBuffer.order(ByteOrder.LITTLE_ENDIAN);

            // Initialize the index file and get data from first record.
            indexChannel = new FileInputStream(indexPath).getChannel();
            // Get the first record which holds the number of lines and points
            // which are contained in the index and data files respectively.
            indexChannel.read(indexBuffer);
            indexBuffer.flip(); // flip because we are reading little-endian????
            // Record count does not include the first record which only contains
            // count of index records and count of total points in data file.
            int indexRecordCount = indexBuffer.getShort(4);

            // Initialize the coordiante data file.
            dataChannel = new FileInputStream(dataPath).getChannel();

            // Loop through the index records.
            for (int i = 1; i <= indexRecordCount; i++) {
                indexChannel.read(indexBuffer); // Get next index file record
                indexBuffer.flip();
                recNum    = indexBuffer.getInt(0);
                numPoints = indexBuffer.getShort(4);
                lineType  = indexBuffer.getShort(6);
                maxLat    = indexBuffer.getShort(8);
                minLat    = indexBuffer.getShort(10);
                maxLon    = indexBuffer.getShort(12);
                minLon    = indexBuffer.getShort(14);

                Line.Kind kind;

                switch (lineType) {
                    case 1: // coast lines
                        kind = Line.Kind.COAST;
                        break;
                    case 5: // islands
                        kind = Line.Kind.ISLAND;
                        break;
                    case 2: // international borders
                        kind = Line.Kind.INTERNATIONAL_BORDER;
                        break;
                    case 4: // internal political borders
                        kind = Line.Kind.POLITICAL_BORDER;
                        break;
                    case 6: // lakes
                        kind = Line.Kind.LAKE;
                        break;
                    case 7: // rivers
                        kind = Line.Kind.RIVER;
                        break;
                    default: // did I miss something?
                        kind = Line.Kind.COAST;
                        break;
                } // end of set kind
                Line line = new Line(kind, maxLat / 60.0, minLat / 60.0, maxLon / 60.0, minLon / 60.0);


                for (int j = 0; j < numPoints; j++) {
                    dataChannel.read(dataBuffer); // Get next data file record
                    dataBuffer.flip();
                    lat = dataBuffer.getShort(0);
                    lon = dataBuffer.getShort(2);
//                    line.addPoint(SphericalPoint.rotatedPoint(-80.8728, new SphericalPoint(lat / 60.0, lon / 60.0)));
                    line.addPoint(new Point(lat / 60.0, lon / 60.0));
                }


                // add line to its relevant list in the linesGroupedByKind collection
                if(!linesGroupedByKind.containsKey(kind)){
                    linesGroupedByKind.put(kind, new ArrayList<Line>());
                }

                linesGroupedByKind.get(kind).add(line);



            }  // END for i

            indexChannel.close();
            dataChannel.close();
        } // END try
        catch (Exception ioe)
        {
            ioe.printStackTrace(System.err); }
    }



    private void renderLineKind(Graphics2D display, Projection projection, int minX, int maxX, int minY, int maxY, Scale scale, Line.Kind kind, MapElementStyle style) {

        for( Line line : linesGroupedByKind.get(kind)) {
            line.render(display, projection, minX, maxX, minY, maxY, scale, linesGroupedByKind.get(kind).indexOf(line), style);
        }

    }

    public Integer size() {
        int count = 0;
        for (Line.Kind kind : linesGroupedByKind.keySet()){
            count += linesGroupedByKind.get(kind).size();
        }
        return count;
    }

    // Private



    private java.util.Map<Line.Kind, List<Line>> linesGroupedByKind = new HashMap<Line.Kind, List<Line>>();

    @Override
    public void render(Graphics2D display, Projection projection, int minX, int maxX, int minY, int maxY, Scale scale, MapElementStyle style) {
        Rectangle background = new Rectangle(minX, minY, maxX - minX, maxY - minY);
        display.setColor(style.sea.getColor());
        display.fill(background);




        // countries
        renderLineKind(display, projection, minX, maxX, minY, maxY, scale, Line.Kind.COAST, style);
        renderLineKind( display,  projection,  minX, maxX, minY, maxY, scale, Line.Kind.ISLAND, style);

        // water features
        renderLineKind( display,  projection,  minX, maxX, minY, maxY, scale, Line.Kind.LAKE, style);

        //
        renderLineKind( display,  projection,  minX, maxX, minY, maxY, scale, Line.Kind.INTERNATIONAL_BORDER, style);

    }
}
