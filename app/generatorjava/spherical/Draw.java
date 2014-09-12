package generatorjava.spherical;

import generatorjava.MapElementStyle;
import generatorjava.Projection;
import generatorjava.Scale;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: matt
 * Date: 03/09/2013
 * Time: 13:19
 * To change this template use File | Settings | File Templates.
 */
public interface Draw {
    void render(Graphics2D display, Projection projection, int minX, int maxX, int minY, int maxY, Scale scale, MapElementStyle style);
}
