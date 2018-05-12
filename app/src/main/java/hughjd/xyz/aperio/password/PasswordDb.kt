package hughjd.xyz.aperio.password

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(Password::class), version = 1, exportSchema = false)
abstract class PasswordDb : RoomDatabase() {

    abstract fun passwordDao(): PasswordDao
}
