package hughjd.xyz.aperio.password

import java.security.SecureRandom

object PasswordGenerator {

    private val random = SecureRandom()

    enum class Chartype constructor(chars: List<Char>) {
        LETTERS("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toList()),
        NUMBERS("0123456789".toList()),
        SYMBOLS("!£$%^&*()_-+={}[];'#:@~<>?,./|¬`\"".toList());

        internal val charset: List<Char> = chars
    }

    /**
     * Generate a random password

     * There are more letters than anything else so to avoid a letter-heavy password we randomly
     * select a chartype for each character rather than combining all chartypes into one set,
     * which can and did produce 'alphanumeric' passwords consisting solely of letters
     */
    fun generatePassword(size: Int, types: Set<Chartype>): String {
        return arrayOfNulls<Char>(size).map { _ ->
            val randomCharset: Chartype = types.toTypedArray()[random.nextInt(types.size)]
            val randomIndex = random.nextInt(randomCharset.charset.size)
            randomCharset.charset[randomIndex].toString()
        }.joinToString("")
    }
}
