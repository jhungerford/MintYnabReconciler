package dev.budget.reconciler

import io.dropwizard.Configuration
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.setup.Bootstrap
import scaldi.Injector

class TestApplication(implicit inj: Injector) extends ReconcilerApplication {
  override def initialize(bootstrap: Bootstrap[Configuration]) = {
    bootstrap.addBundle(new AssetsBundle("/test-web/js", "/web/js/test", "", "test-js"))
    bootstrap.addBundle(new AssetsBundle("/test-web/css", "/web/css/test", "", "test-css"))
    bootstrap.addBundle(new AssetsBundle("/test-html", "/test", "index.html", "test-html"))

    super.initialize(bootstrap)
  }
}
