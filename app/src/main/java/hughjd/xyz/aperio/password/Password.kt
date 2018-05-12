package hughjd.xyz.aperio.password

import java.io.Serializable

data class Password(val id: Long, val name: String, val password: String, val email: String, val url: String, val username: String) : Serializable
