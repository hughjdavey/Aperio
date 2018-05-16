package hughjd.xyz.aperio.password

import hughjd.xyz.aperio.password.PasswordGenerator.Chartype.LETTERS
import hughjd.xyz.aperio.password.PasswordGenerator.Chartype.NUMBERS
import hughjd.xyz.aperio.password.PasswordGenerator.Chartype.SYMBOLS
import hughjd.xyz.aperio.password.PasswordGenerator.generatePassword
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class PasswordTest {

    @Test(expected = IllegalArgumentException::class)
    fun passwordGenerationWithoutCharsets() {
        generatePassword(8, setOf())
    }

    @Test(expected = NegativeArraySizeException::class)
    fun passwordGenerationNegativeSize() {
        generatePassword(-8, setOf(LETTERS))
    }

    @Test
    fun passwordGenerationTest() {
        val lettersOnly = generatePassword(8, setOf(LETTERS))
        assertThat(lettersOnly.length, `is`(8))
        assertThat(lettersOnly.matches(Regex("^[a-zA-Z]+\$")), `is`(true))

        val numbersOnly = generatePassword(10, setOf(NUMBERS))
        assertThat(numbersOnly.length, `is`(10))
        assertThat(numbersOnly.matches(Regex("^[0-9]+\$")), `is`(true))

        val symbolsOnly = generatePassword(12, setOf(SYMBOLS))
        assertThat(symbolsOnly.length, `is`(12))
        assertThat(symbolsOnly.matches(Regex("^[!£\\\\$%^&*()_\\-+={}\\[\\];'#:@~<>?,./|¬`\"]+\$")), `is`(true))

        val lettersAndNumbers = generatePassword(14, setOf(LETTERS, NUMBERS))
        assertThat(lettersAndNumbers.length, `is`(14))
        assertThat(lettersAndNumbers.matches(Regex("^[a-zA-Z0-9]+\$")), `is`(true))

        val lettersAndSymbols = generatePassword(16, setOf(LETTERS, SYMBOLS))
        assertThat(lettersAndSymbols.length, `is`(16))
        assertThat(lettersAndSymbols.matches(Regex("^[a-zA-Z!£\\\\\$%^&*()_\\-+={}\\[\\];'#:@~<>?,./|¬`\"]+\$")), `is`(true))

        val numbersAndSymbols = generatePassword(18, setOf(NUMBERS, SYMBOLS))
        assertThat(numbersAndSymbols.length, `is`(18))
        assertThat(numbersAndSymbols.matches(Regex("^[0-9!£\\\\\$%^&*()_\\-+={}\\[\\];'#:@~<>?,./|¬`\"]+\$")), `is`(true))

        val lettersNumbersAndSymbols = generatePassword(20, setOf(LETTERS, NUMBERS, SYMBOLS))
        assertThat(lettersNumbersAndSymbols.length, `is`(20))
        assertThat(lettersNumbersAndSymbols.matches(Regex("^[a-zA-Z0-9!£\\\\\$%^&*()_\\-+={}\\[\\];'#:@~<>?,./|¬`\"]+\$")), `is`(true))

    }
}
