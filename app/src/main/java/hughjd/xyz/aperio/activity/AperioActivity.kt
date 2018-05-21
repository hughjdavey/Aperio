package hughjd.xyz.aperio.activity

import android.support.v7.app.AppCompatActivity

/**
 * All app activities can override this class to get an authentication check in onResume
 */
open class AperioActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()

        if (Aperio.authenticationNeeded) {
            Aperio.showAuthenticationDialog(this)
        }
    }
}
