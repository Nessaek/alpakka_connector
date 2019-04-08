package com.nk.map

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.nk.map.routes.Routes
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends LazyLogging {


  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("alpakka")
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val allRoutes = Routes.routes

    Http().bindAndHandle(allRoutes, "localhost", 8081) map { binding =>
      logger.info(s"Alpakka test interface bound to ${binding.localAddress}")
    }

  }

}


