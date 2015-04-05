package dev.budget.reconciler.modules

import dev.budget.reconciler.TestApplication
import scaldi.Module

class TestApplicationModule extends Module {
  bind [TestApplication] to new TestApplication()
}
