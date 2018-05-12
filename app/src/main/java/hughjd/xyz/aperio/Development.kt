package hughjd.xyz.aperio

import hughjd.xyz.aperio.password.Password
import hughjd.xyz.aperio.password.PasswordGenerator.Chartype.*
import hughjd.xyz.aperio.password.PasswordGenerator.generatePassword
import java.util.*

object Development {

    private val random = Random()

    fun testPasswords(): List<Password> {
        return listOf(
                Password(0L, "Amazon", randomPassword(), randomEmail(), randomUrl(), randomUsername()),
                Password(0L, "Netflix", randomPassword(), randomEmail(), randomUrl(), randomUsername()),
                Password(0L, "PayPal", randomPassword(), randomEmail(), randomUrl(), randomUsername()),
                Password(0L, "Santander", randomPassword(), randomEmail(), randomUrl(), randomUsername()),
                Password(0L, "Lloyds", randomPassword(), randomEmail(), randomUrl(), randomUsername()),
                Password(0L, "Gmail", randomPassword(), randomEmail(), randomUrl(), randomUsername()),
                Password(0L, "Hotmail", randomPassword(), randomEmail(), randomUrl(), randomUsername()),
                Password(0L, "Twitch", randomPassword(), randomEmail(), randomUrl(), randomUsername()),
                Password(0L, "Ticketmaster", randomPassword(), randomEmail(), randomUrl(), randomUsername()),
                Password(0L, "Ubuntu Forums", randomPassword(), randomEmail(), randomUrl(), randomUsername()),
                Password(0L, "Reddit", randomPassword(), randomEmail(), randomUrl(), randomUsername()),
                Password(0L, "Ebay", randomPassword(), randomEmail(), randomUrl(), randomUsername()),
                Password(0L, "Nespresso Club", randomPassword(), randomEmail(), randomUrl(), randomUsername()),
                Password(0L, "Nuclear Launch Code", randomPassword(), randomEmail(), randomUrl(), randomUsername()),
                Password(0L, "PSN", randomPassword(), randomEmail(), randomUrl(), randomUsername())
        )
    }

    private fun randomPassword(): String {
        return generatePassword(randomInt(8, 16), setOf(LETTERS, NUMBERS, SYMBOLS))
    }

    private fun randomEmail(): String {
        return generatePassword(randomInt(5, 8), setOf(LETTERS)) + '@' + randomEmailProvider() + randomTLD()
    }

    private fun randomUsername(): String {
        val base = generatePassword(randomInt(4, 8), setOf(LETTERS)) + '_' + generatePassword(randomInt(1, 4), setOf(NUMBERS))
        return base[0].toUpperCase() + base.substring(1).toLowerCase()
    }

    private fun randomUrl(): String {
        return "https://" + generatePassword(randomInt(5, 8), setOf(LETTERS)).toLowerCase() + randomTLD() + '/'
    }

    private fun randomEmailProvider() = listOf("gmail", "hotmail", "yahoo", "aol", "protonmail")[random.nextInt(5)]

    private fun randomTLD() = listOf(".com", ".org", ".net", ".io", ".co.uk")[random.nextInt(5)]

    private fun randomInt(min: Int, max: Int) = random.nextInt(max + 1 - min) + min

    private fun spaces(n: Int): String {
        val str = StringBuilder()
        for (i in 0..n) {
            str.append(" ")
        }
        return str.toString()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        for (i in 1..10) {
            printJustified(randomPassword())
            printJustified(randomEmail())
            printJustified(randomUsername())
            printJustified(randomUrl())
            println()
        }
    }

    private fun printJustified(str: String) {
        print(str + spaces(25 - str.length))
    }
}
