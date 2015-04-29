package response

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

import controllers.Application._
import generator.Coordinate
import generatorjava.{Coordinates, DrawMap, MapElementStyle, TiltTranslation}
import play.api.Logger

/**
 * Created by julian on 07/04/2015.
 */
object Response {

  def serveImage(longitude: Double, latitude: Double, styleSelect: String) = {
    val styles :Map[String, MapElementStyle] =  Map(
      "bright" -> new MapElementStyle("#FFC907" , "#FFC907", "#4789FF",   "#4789FF", "#000000"),
      "monochrome" -> new MapElementStyle("#E6E6E6" , "#E6E6E6", "#008FFF",   "#008FFF", "#000000"),
      "orange" -> new MapElementStyle("#FFB80D" , "#FFB80D", "#E8600C",   "#E8600C", "#000000"),
      "green" -> new MapElementStyle("#267F4C" , "#267F4C", "#39BF72",   "#39BF72", "#000000")
    )

    Logger debug styleSelect //debug

    def getStyle(styleSelect: String): MapElementStyle = {
      styles get styleSelect match {
        case None => new MapElementStyle("#FFC907" , "#FFC907", "#4789FF",   "#4789FF", "#000000") //If invalid selection, return default style
        case Some(s) => s //Return a MapElementStyle
      }
    }

    //val style: MapElementStyle = new MapElementStyle("#E6E6E6" , "#E6E6E6", "#0000FF",   "#0000FF", "#000000")

    val image: BufferedImage = DrawMap.generateInMemory(DrawMap.DrawType.SCREEN, longitude, latitude, getStyle(styleSelect))

    //val is: ImageInputStream = ImageIO.createImageInputStream(image)
    val baos: ByteArrayOutputStream = new ByteArrayOutputStream()
    ImageIO.write(image, "png", baos)
    Ok(baos.toByteArray).as("image/png")
  }

  def serveJson(mapCenter: Coordinate, location: Coordinate) = {
    ???
  }

  def translate(mapCenter: Coordinate, location: Coordinate) = {
    val tilt = new TiltTranslation(mapCenter.longitude, mapCenter.latitude)
    val coordinates = new Coordinates(location.longitude, location.latitude)
    //tilt.translate(coordinates, new TiltTranslation())
  }
}
