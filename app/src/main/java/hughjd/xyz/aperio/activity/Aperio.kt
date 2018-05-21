package hughjd.xyz.aperio.activity

import android.app.Application
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import timber.log.Timber

open class Aperio : Application(), LifecycleDelegate {

    override fun onCreate() {
        super.onCreate()
        val lifeCycleHandler = ApplicationLifecycleHandler(this)
        registerActivityLifecycleCallbacks(lifeCycleHandler)
        registerComponentCallbacks(lifeCycleHandler)
    }

    override fun onAppForegrounded() {
        Timber.d("App placed in foreground - authentication needed")
    }

    override fun onAppBackgrounded() {
        Timber.d("App placed in background")
        authenticationNeeded = true
    }

    companion object {

        var authenticationNeeded = true

        fun showAuthenticationDialog(context: Context) {
            MaterialDialog.Builder(context)
                    .cancelable(false)
                    .title("Log in")
                    .inputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    .input("Enter password", "", { _, input -> onAuthenticate(input.toString(), context) })
                    .show()
        }

        private fun onAuthenticate(password: String, context: Context) {
            when (password) {
                "hello" -> {
                    Toast.makeText(context, "Authenticated", Toast.LENGTH_SHORT).show()
                    authenticationNeeded = false
                }
                else -> {
                    Toast.makeText(context, "Incorrect password", Toast.LENGTH_SHORT).show()
                    (context as AppCompatActivity).finish()
                }
            }
        }
    }
}
