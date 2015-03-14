package dev.budget.reconciler

import com.massrelevance.dropwizard.ScalaApplication
import dev.budget.reconciler.api.HelloResource
import dev.budget.reconciler.config.ReconcilerConfiguration
import dev.budget.reconciler.es.ManagedElasticSearch
import dev.budget.reconciler.health.ElasticSearchHealth
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.setup.{Bootstrap, Environment}
import scaldi.{Injectable, Injector}

class ReconcilerApplication(implicit inj: Injector) extends ScalaApplication[ReconcilerConfiguration] with Injectable {

  override def initialize(bootstrap: Bootstrap[ReconcilerConfiguration]) = {
    bootstrap.addBundle(new AssetsBundle("/web", "/web", "", "web"))
    bootstrap.addBundle(new AssetsBundle("/html", "", "index.html", "html"))
  }

  override def run(configuration: ReconcilerConfiguration, environment: Environment) = {
    environment.jersey.setUrlPattern("/api/*")
    environment.lifecycle().manage( inject [ManagedElasticSearch] )
    environment.healthChecks().register("ElasticSearch", inject [ElasticSearchHealth])

    environment.jersey.register( inject [HelloResource] )
  }
}
