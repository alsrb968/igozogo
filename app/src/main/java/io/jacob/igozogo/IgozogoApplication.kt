package io.jacob.igozogo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class IgozogoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(TimberTree(getString(R.string.log_tag)))
    }

    private inner class TimberTree(private val tag: String) : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement): String {
            return "[${element.fileName}:${element.lineNumber}#${element.methodName}()]"
        }

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            super.log(priority, this.tag, "$tag $message", t)
        }
    }
}