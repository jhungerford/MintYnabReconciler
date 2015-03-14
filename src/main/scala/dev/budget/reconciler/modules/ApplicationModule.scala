package dev.budget.reconciler.modules

import dev.budget.reconciler.ReconcilerApplication
import dev.budget.reconciler.api.{TransactionsResource, HelloResource}
import scaldi.Module

class ApplicationModule extends Module {
  bind [ReconcilerApplication] to new ReconcilerApplication

  bind [HelloResource] to new HelloResource
  bind [TransactionsResource] to new TransactionsResource
}
