package dev.budget.reconciler.modules

import dev.budget.reconciler.ReconcilerApplication
import dev.budget.reconciler.api.TransactionsResource
import scaldi.Module

class ApplicationModule extends Module {
  bind [ReconcilerApplication] to new ReconcilerApplication

  bind [TransactionsResource] to new TransactionsResource
}
