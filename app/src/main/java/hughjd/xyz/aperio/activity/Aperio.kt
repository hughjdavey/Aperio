package hughjd.xyz.aperio.activity

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import hughjd.xyz.aperio.password.PasswordDb
import timber.log.Timber

class Aperio : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Aperio.db =  Room.databaseBuilder(this, PasswordDb::class.java, "pdb").build()

        Timber.plant(Timber.DebugTree())

        val passwordListIntent = Intent(this, PasswordList::class.java)
        // these flags mean that pressing back button or PasswordList#finish does NOT bring user back here
        passwordListIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(passwordListIntent)
    }

    companion object {
        var db: PasswordDb? = null
    }
}
