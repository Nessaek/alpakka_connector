package com.nk

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.datastax.driver.core.Cluster

import scala.concurrent.ExecutionContext

package object connector {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

    implicit val session = Cluster
    .builder
      .addContactPoints("localhost")
      .withPort(9042)
      .build()
      .connect()


}
