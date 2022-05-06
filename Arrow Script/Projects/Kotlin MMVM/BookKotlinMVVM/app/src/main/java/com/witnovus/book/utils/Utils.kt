package com.witnovus.book.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import androidx.work.*
import com.witnovus.book.work.BookWorker
import io.paperdb.Paper
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import com.witnovus.book.R
/**
 * This Utils class contains all the common method which are used multiple times in application.
 */
class Utils {
    companion object {
        private var dialog: Dialog? = null

        /**
         * This method checks the Internet Connectivity
         *
         * @param context : Context
         * @return boolean value based on internet connectivity status
         */
        fun checkInternetConnection(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                activeNetworkInfo != null && activeNetworkInfo.isConnected
            }
        }

        /**
         * This method shows progress dialog
         *
         * @param context : Context
         */
        fun showProgressDialog(context: Context) {
            dialog = Dialog(context)
            val inflate = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null)
            dialog?.setContentView(inflate)
            dialog?.setCancelable(false)
            dialog?.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            dialog?.show()
        }

        /**
         * This method hides the progress dialog if its showing
         *
         */
        fun hideProgressDialog() {
            if (dialog != null && dialog!!.isShowing) dialog!!.dismiss()
        }

        /**
         * This method checks weather worker is already running or not
         *
         * @param uuid    : Worker Request Id
         * @param context : Context
         * @return running : boolean value based on status
         */
        private fun isWorkScheduled(uuid: String?, context: Context?): Boolean {
            val instance = WorkManager.getInstance(context!!)
            val workUUID = UUID.fromString(uuid)
            val statuses = instance.getWorkInfoById(workUUID)
            var running = false
            var workInfo: WorkInfo? = null
            try {
                workInfo = statuses.get()
            } catch (e: ExecutionException) {
                Log.d("Exception", "ExecutionException in isWorkScheduled: $e")
            } catch (e: InterruptedException) {
                Log.d("Exception", "InterruptedException in isWorkScheduled: $e")
            }
            if (workInfo != null) {
                val state = workInfo.state
                running =
                    running || state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED
            }
            return running
        }

        /**
         * This method schedule the worker class if there is no any running worker class
         *
         * @param context : Context
         */
        @JvmStatic
        fun scheduleBookSyncWorker(context: Context?) {
            var isWorkerRunning = false

            // check worker request id is stored in shared preference or not
            if (Paper.book().contains(Constants.WORK_REQUEST_ID)) {
                val workRequestId = Paper.book().read<String>(Constants.WORK_REQUEST_ID)
                if (workRequestId != null) {
                    isWorkerRunning = isWorkScheduled(workRequestId, context)
                }
            }
            if (!isWorkerRunning) {
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                // Create Request to schedule after 5 seconds again
                val workRequest = OneTimeWorkRequest.Builder(BookWorker::class.java)
                    .setInitialDelay(5, TimeUnit.SECONDS)
                    .setConstraints(constraints).build()
                WorkManager.getInstance(context!!).enqueue(workRequest)

                //Store Worker Request ID in Shared Preference
                Paper.book().write(Constants.WORK_REQUEST_ID, workRequest.id.toString())
            }
        }
    }
}