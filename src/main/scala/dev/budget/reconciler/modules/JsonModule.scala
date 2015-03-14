package dev.budget.reconciler.modules

import com.fasterxml.jackson.databind.ObjectMapper
import scaldi.Module

class JsonModule extends Module {
  bind [ObjectMapper] to new ObjectMapper
}
