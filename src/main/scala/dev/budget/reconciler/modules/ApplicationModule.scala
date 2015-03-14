package dev.budget.reconciler.modules

import dev.budget.reconciler.ReconcilerApplication
import dev.budget.reconciler.api.HelloResource
import scaldi.Module

class ApplicationModule extends Module {
  bind [ReconcilerApplication] to new ReconcilerApplication
  bind [HelloResource] to new HelloResource

}
