package generatorjava;

import java.awt.geom.Point2D;

public class ProjectionScratchpad {
    final public Coordinates fromCoordinates;
    final public Coordinates toCoordinates;
    final public Point2D.Double translatedPoint;
    final public TiltTranslationScratchpad tiltTranslationScratchpad;

    public ProjectionScratchpad() {
        this.fromCoordinates = new Coordinates();
        this.toCoordinates = new Coordinates();
        this.translatedPoint = new Point2D.Double();
        this.tiltTranslationScratchpad = new TiltTranslationScratchpad();
    }
}
