package hughjd.xyz.aperio.activity

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.res.Configuration
import android.os.Bundle

class ApplicationLifecycleHandler(private val lifecycleDelegate: LifecycleDelegate) : Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    private var appInForeground = false

    override fun onTrimMemory(level: Int) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            appInForeground = false
            lifecycleDelegate.onAppBackgrounded()
        }
    }

    override fun onActivityResumed(activity: Activity?) {
        if (!appInForeground) {
            appInForeground = true
            lifecycleDelegate.onAppForegrounded()
        }
    }

    override fun onActivityPaused(activity: Activity?) { }

    override fun onActivityStarted(activity: Activity?) { }

    override fun onActivityDestroyed(activity: Activity?) { }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) { }

    override fun onActivityStopped(activity: Activity?) { }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) { }

    override fun onLowMemory() { }

    override fun onConfigurationChanged(newConfig: Configuration?) { }
}