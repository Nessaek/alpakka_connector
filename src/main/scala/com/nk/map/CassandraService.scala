package com.nk.map

import akka.actor.ActorSystem
import akka.kafka.{ConsumerSettings, ProducerMessage, ProducerSettings, Subscriptions}
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.stream.{ActorMaterializer, Materializer}
import akka.{Done, NotUsed}
import akka.stream.alpakka.cassandra.scaladsl.CassandraSource
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import com.datastax.driver.core._

import scala.collection.immutable
import scala.concurrent.Future
import scala.concurrent.{Await, ExecutionContext}
import immutable.Seq
import com.nk.connector._
import com.nk.map.model.Alpakka
import io.circe.syntax._

import scala.concurrent.duration._
import akka.stream.alpakka.cassandra.scaladsl._
import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success}

object CassandraService extends LazyLogging{


  def selectFromCassandra()(): Future[Seq[Alpakka]] = {

    val statement: Statement = new SimpleStatement(s"SELECT * FROM animals.alpakka")

    val rows: Seq[Row] = Await.result(CassandraSource(statement).runWith(Sink.seq), 2.seconds)

    val stuff: Seq[Alpakka] = rows.map { row =>
      Alpakka(row.getInt("id"),
        row.getString("animal_type"),
        row.getString("name"))
    }
    Future(stuff)
  }

  def generateAlpakkaEntries(): Source[Alpakka, NotUsed] = {


 Source.fromIterator(() => Iterator.continually(Alpakka(Random.nextInt(10000000),Random.alphanumeric.take(6).mkString(""), Random.alphanumeric.take(6)
   .mkString("")))).take(10000)

  }

  def createCassandraSink(): Sink[Alpakka, Future[Done]] = {

    val preparedStatement = session.prepare(s"INSERT INTO animals.alpakka(id,animal_type,name) VALUES (?,?,?)")
    val statementBinder: (Alpakka, PreparedStatement) => BoundStatement = (al, ps) => ps.bind(al.id: java.lang.Integer, al.animal_type: java.lang.String, al.name: java.lang.String)

    val cassandraSink: Sink[Alpakka, Future[Done]] = CassandraSink[Alpakka](parallelism = 2, preparedStatement, statementBinder)
    cassandraSink

  }

  def insertAlpakkaEntries() = {

    generateAlpakkaEntries()
      .toMat(createCassandraSink())(Keep.right)
      .run()

  }


  def getAlpakkaEntries(): Future[Seq[Alpakka]] = {

    val preparedStatement = new SimpleStatement(s"SELECT * FROM animals.alpakka")
    val rows: Seq[Row] = Await.result(CassandraSource(preparedStatement).runWith(Sink.seq),2.second)

    Future(rows.map(row => Alpakka(row.getInt("id"),row.getString("name"),row.getString("animal_type"))))

  }

  def connectEntriesToProducer(seq: Seq[Alpakka]) = {
    implicit val system: ActorSystem = ActorSystem("producer-sample")
    implicit val materializer: Materializer = ActorMaterializer()

    val producerSettings = ProducerSettings(system, new StringSerializer, new StringSerializer)
      .withBootstrapServers("localhost:9092")

    val alpakkaSource: Source[Alpakka, NotUsed] = Source(seq)

alpakkaSource.map(item => new ProducerRecord[String, String]("alpakkas-rock", item.asJson.noSpaces))
      .runWith(Producer.plainSink(producerSettings))

  }

  def readFromProducer(): Future[Done] = {

    val consumerSettings =
      ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
        .withBootstrapServers("localhost:9092")

          



    Consumer.committableSource(consumerSettings, Subscriptions.topics("alpakkas-rock"))
      .runWith(Sink.foreach(print))


  }






}
