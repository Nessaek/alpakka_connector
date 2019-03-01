package com.nk.map

import java.util.Properties

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object ProducerApp {
  def main(args: Array[String]): Unit = {
    println("Hello from producer")


    implicit val system: ActorSystem = ActorSystem()
    implicit val materializer: Materializer = ActorMaterializer()

    val producerSettings = ProducerSettings(system, new StringSerializer, new StringSerializer)
      .withBootstrapServers("localhost:9092")

    val done: Future[Done] =
      Source(1 to 100)
        .map(value => new ProducerRecord[String, String]("Alpakkas-rock", "msg " + value))
        .runWith(Producer.plainSink(producerSettings))

    implicit val ec: ExecutionContextExecutor = system.dispatcher

    done onComplete  {
      case Success(_) => println("Done"); system.terminate()
      case Failure(err) => println(err.toString); system.terminate()
    }
  }
}