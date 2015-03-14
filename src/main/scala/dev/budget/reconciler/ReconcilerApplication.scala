package dev.budget.reconciler

import com.massrelevance.dropwizard.ScalaApplication
import dev.budget.reconciler.api.{TransactionsResource, HelloResource}
import dev.budget.reconciler.es.ManagedElasticSearch
import dev.budget.reconciler.health.ElasticSearchHealth
import io.dropwizard.Configuration
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.setup.{Bootstrap, Environment}
import scaldi.{Injectable, Injector}

class ReconcilerApplication(implicit inj: Injector) extends ScalaApplication[Configuration] with Injectable {

  override def initialize(bootstrap: Bootstrap[Configuration]) = {
    bootstrap.addBundle(new AssetsBundle("/web", "/web", "", "web"))
    bootstrap.addBundle(new AssetsBundle("/html", "", "index.html", "html"))
  }

  override def run(configuration: Configuration, environment: Environment) = {
    environment.jersey.setUrlPattern("/api/*")
    environment.lifecycle().manage( inject [ManagedElasticSearch] )
    environment.healthChecks().register("ElasticSearch", inject [ElasticSearchHealth])

    environment.jersey.register( inject [HelloResource] )
    environment.jersey.register( inject [TransactionsResource] )
  }
}
