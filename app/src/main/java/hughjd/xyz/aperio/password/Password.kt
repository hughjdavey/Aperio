package hughjd.xyz.aperio.password

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity
data class Password(@PrimaryKey(autoGenerate = true) val id: Long, val name: String, val password: String,
                    val email: String, val url: String, val username: String) : Serializable
