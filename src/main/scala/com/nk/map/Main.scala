package com.nk.map

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

object Main extends LazyLogging {


  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("alpakka")
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    Http().bindAndHandle(allRoutes, connection.host, connection.port.value) map { binding =>
      logger.info(s"Cel interface bound to ${binding.localAddress}")
    }

  }

}
