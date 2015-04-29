package controllers

import java.awt.image.BufferedImage
import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import javax.imageio.ImageIO
import javax.imageio.stream.ImageInputStream

import generator.Coordinate
import generatorjava.{MapElementStyle, DrawMap}
import response.Response

//import generator
import play.api._
import play.api.http.Writeable
import play.api.mvc._
import play.api.libs.json.Json


object Application extends Controller {

  def index = Action { request =>
    Console.print(request.body.asText)
    Ok(views.html.index() ) //Loads initial page, react client from now on communicates via ajax
  }

  def make = Action {
    Ok(views.html.make())
  }

  //Takes the center of the map and a coordinate and gives back the x,y location of that coordinate of the reprojected map
  def translate(mapCenter: Coordinate, location: Coordinate) = Action { request =>
    Ok("Got request [" + request + "]")
  }

  def drawMap(longitude: Double, latitude: Double, styleSelect: String) = Action {
    Response.serveImage(longitude, latitude, styleSelect)
  }

}