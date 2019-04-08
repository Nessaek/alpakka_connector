package com.nk.map.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, onSuccess, path}
import akka.http.scaladsl.server.Route
import com.nk.map.CassandraService
import akka.http.scaladsl.server.Directives._
import com.nk.connector._
import io.circe.syntax._
import io.circe.generic.auto._

import scala.util.{Failure, Success}

object Routes {

val routes: Route =  pathPrefix("routes") {

  path("insert") {
    onComplete(CassandraService.insertAlpakkaEntries()) {stuff =>
      complete("asdf" + stuff)
    }
  } ~ path ("start_producer"){
    onSuccess(CassandraService.getAlpakkaEntries()){ alpakkaSeq =>
      onComplete(CassandraService.connectEntriesToProducer(alpakkaSeq)){done =>
       done match  {
          case Success(_) => complete("Producer Done");
          case Failure(err) => complete(err.toString);
        }

      }

    }
  } ~ path("start_consumer") {
    onSuccess(CassandraService.readFromProducer()) {messages =>
      complete("yo" + messages)

    }
  }

}



}
