package dev.budget.reconciler.api

import javax.ws.rs.core.MediaType
import javax.ws.rs.{Produces, GET, Path}

@Path("/v1/hello")
class HelloResource {

  @GET
  @Produces(Array(MediaType.TEXT_PLAIN))
  def getHello(): String = {
    "Hello scala resources!"
  }

}
