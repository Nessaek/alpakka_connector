package com.nk.map

import com.datastax.driver.core.Cluster

object Connector {




    implicit val session = new Cluster
    .Builder()
      .addContactPoints("localhost")
      .withPort(9042)
      .build()
      .connect()


}
