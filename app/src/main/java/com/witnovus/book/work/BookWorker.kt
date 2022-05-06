package com.witnovus.book.work

import android.content.Context
import androidx.work.*
import com.witnovus.book.repository.BookRepository
import com.witnovus.book.utils.Constants
import dagger.assisted.AssistedInject
import dagger.assisted.Assisted
import io.paperdb.Paper
import dagger.assisted.AssistedFactory
import java.util.concurrent.TimeUnit

/**
 * This is Book Worker class which extends Worker class  to sync Book Record on Server.
 */
class BookWorker @AssistedInject constructor(
    @param:Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val bookRepository: BookRepository
) : Worker(
    context, workerParams) {
    override fun doWork(): Result {

        //Call SyncBook Method to sync the book data on server
        val isSyncContinue = bookRepository.syncBook()

        //If isSyncContinue is true then we need to schedule worker again to sync next book record
        if (isSyncContinue) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            // Create Request to schedule after 5 seconds again
            val workRequest = OneTimeWorkRequest.Builder(BookWorker::class.java)
                .setInitialDelay(5, TimeUnit.SECONDS)
                .setConstraints(constraints).build()
            WorkManager.getInstance(context).enqueue(workRequest)

            //Store Worker Request ID in Shared Preference
            Paper.book().write(Constants.WORK_REQUEST_ID, workRequest.id.toString())
        }
        return Result.success()
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context?, workerParams: WorkerParameters?): BookWorker?
    }
}