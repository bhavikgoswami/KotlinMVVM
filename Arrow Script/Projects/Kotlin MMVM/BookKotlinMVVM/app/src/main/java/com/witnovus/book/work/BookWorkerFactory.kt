package com.witnovus.book.work

import android.content.Context
import javax.inject.Inject
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.ListenableWorker
import com.witnovus.book.work.BookWorker

/**
 * This is WorkerFactory class to override the required methods
 */
class BookWorkerFactory @Inject constructor(var factory: BookWorker.Factory) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return if (workerClassName == BookWorker::class.java.name) {
            factory.create(appContext, workerParameters)
        } else null
    }
}