package dev.budget.reconciler

import io.dropwizard.Configuration
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.setup.Bootstrap
import scaldi.Injector

class TestApplication(implicit inj: Injector) extends ReconcilerApplication {
  override def initialize(bootstrap: Bootstrap[Configuration]) = {
    super.initialize(bootstrap)

    bootstrap.addBundle(new AssetsBundle("/test-web", "/test-web", "", "test-web"))
    bootstrap.addBundle(new AssetsBundle("/test-html", "/test", "index.html", "test-html"))
  }
}
