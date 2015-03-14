package dev.budget.reconciler.es

sealed case class ESIndex(
  name: String,
  esType: String,
  indexFileName: String)

object MintESIndex extends ESIndex("mint", "transaction", "es/mint_index.json")
object YnabESIndex extends ESIndex("ynab", "transaction", "es/ynab_index.json")
