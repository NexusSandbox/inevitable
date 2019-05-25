package eli.inevitable

import com.thedeanda.lorem.LoremIpsum
import eli.inevitable.enumerations.Align.CENTER
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KotlinCellFormatterAPITest {
    private val faker: LoremIpsum = LoremIpsum.getInstance()

    @Test
    fun `Example vararg builder output`() {
        val line1 = faker.getWords(5)
        val line2 = faker.getWords(2)
        val line3 = faker.getWords(3)
        textCell(line1, line2, line3) {
            alignment(CENTER)
            padding(1, 1)
            paddingSpacer('*')
        }.println()
    }

    @Test
    fun `Example list builder output`() {
        val line1 = faker.getWords(5)
        val line2 = faker.getWords(2)
        val line3 = faker.getWords(3)
        textCell(listOf(line1, line2, line3)) {
            alignment(CENTER)
            padding(1, 1)
            paddingSpacer('*')
        }.println()
    }

    @Test
    fun `Example empty builder output`() {
        val line1 = faker.getWords(5)
        val line2 = faker.getWords(2)
        val line3 = faker.getWords(3)
        textCell {
            lines(line1, line2, line3)
            alignment(CENTER)
            padding(1, 1)
            paddingSpacer('*')
        }.println()
    }

    @Test
    fun `Example empty appending builder output`() {
        textCell {
            lines(faker.getWords(5))
            lines(faker.getWords(2))
            lines(faker.getWords(3))
            alignment(CENTER)
            padding(1, 1)
            paddingSpacer('*')
        }.println()
    }
}
