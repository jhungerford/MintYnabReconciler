package dev.budget.reconciler.finagle.service

import com.twitter.finagle.Service
import com.twitter.util.Future
import dev.budget.reconciler.model.MintTransaction

class MintUploadService extends Service[List[MintTransaction], Int] {
  override def apply(request: List[MintTransaction]): Future[Int] = {
    Future.value(0)
  }
}
