package com.witnovus.book.core

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.witnovus.book.work.BookWorkerFactory
import com.witnovus.book.utils.Constants
import com.witnovus.book.utils.Utils
import dagger.hilt.android.HiltAndroidApp
import io.paperdb.Paper
import javax.inject.Inject

/**
 * This is application class which initialize all the required modules, libraries,etc
 */
@HiltAndroidApp
class BookApplication : Application() {
    @Inject
    lateinit var bookWorkerFactory: BookWorkerFactory

    override fun onCreate() {
        super.onCreate()
        Paper.init(applicationContext)
        WorkManager.initialize(this, Configuration.Builder().setWorkerFactory(
            bookWorkerFactory
        ).build())
        if (Paper.book().contains(Constants.IS_BOOK_FETCHED_FROM_API)) {
            Utils.scheduleBookSyncWorker(applicationContext)
        }
    }
}
