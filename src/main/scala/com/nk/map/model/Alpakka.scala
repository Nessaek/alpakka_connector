package com.nk.map.model

import io.circe.{Decoder, Encoder}

import io.circe.generic.semiauto._


object Alpakka {
  implicit val jsonDecoder: Decoder[Alpakka] = deriveDecoder[Alpakka]
  implicit val jsonEncoder: Encoder[Alpakka] = deriveEncoder[Alpakka]
}

case class Alpakka(id:Int,name:String,animal_type:String)
