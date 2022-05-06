package com.witnovus.book.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.witnovus.book.utils.Constants
import com.witnovus.book.utils.Utils
import io.paperdb.Paper

/**
 * This is device boot receiver class and set worker when device restarted.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            if (Paper.book().contains(Constants.IS_BOOK_FETCHED_FROM_API)) {
                if (Utils.checkInternetConnection(context)) {
                    Utils.scheduleBookSyncWorker(context)
                }
            }
        }
    }
}