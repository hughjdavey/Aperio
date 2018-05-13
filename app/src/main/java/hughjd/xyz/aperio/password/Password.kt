package hughjd.xyz.aperio.password

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity
data class Password(@PrimaryKey(autoGenerate = true) val id: Long, val name: String, val password: String,
                    val email: String, val url: String, val username: String) : Serializable {

    companion object {
        val SORT_ALPHA: java.util.Comparator<Password> = Comparator { p1, p2 -> p1.name.compareTo(p2.name) }

        val SORT_ZETA: java.util.Comparator<Password> = Comparator { p1, p2 -> p1.name.compareTo(p2.name) * -1 }

        val SORT_NEW: java.util.Comparator<Password> = Comparator { p1, p2 -> java.lang.Long.compare(p1.id, p2.id) * -1 }

        val SORT_OLD: java.util.Comparator<Password> = Comparator { p1, p2 -> java.lang.Long.compare(p1.id, p2.id) }
    }
}
