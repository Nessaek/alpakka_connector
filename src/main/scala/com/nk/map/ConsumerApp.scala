package com.nk.map

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.scaladsl.Sink
import akka.stream.{ActorMaterializer, Materializer}
import org.apache.kafka.common.serialization.StringDeserializer

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object ConsumerApp {
  def main(args: Array[String]): Unit = {
    println("Hello from hBaseWriter")

    implicit val system: ActorSystem = ActorSystem("consumer-sample")
    implicit val materializer: Materializer = ActorMaterializer()

    val consumerSettings =
      ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
        .withBootstrapServers("localhost:9092")

    val done = Consumer
      .plainSource(consumerSettings, Subscriptions.topics("Awesome-Topic"))
      .runWith(Sink.foreach(println))

    implicit val ec: ExecutionContextExecutor = system.dispatcher
    done onComplete  {
      case Success(_) => println("Done"); system.terminate()
      case Failure(err) => println(err.toString); system.terminate()
    }
  }
}
