package controllers

import java.awt.image.BufferedImage
import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import javax.imageio.ImageIO
import javax.imageio.stream.ImageInputStream

import generator.{GeneratedMap, Coordinate}
import generatorjava.{MapElementStyle, DrawMap}
import generator
import play.api._
import play.api.http.Writeable
import play.api.mvc._
import play.api.libs.json.Json


object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def make = Action {
    Ok(views.html.make())
  }

  def translate(mapCenter: Coordinate, location: Coordinate) = Action { request =>
    Ok("Got request [" + request + "]")
  }

  def drawMap(longitude: Double, latitude: Double, styleSelect: String) = Action {
    GeneratedMap.serveImage(longitude, latitude, styleSelect)
  }

}