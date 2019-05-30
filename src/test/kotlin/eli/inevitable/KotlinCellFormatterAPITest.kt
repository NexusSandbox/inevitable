package eli.inevitable

import com.thedeanda.lorem.LoremIpsum
import eli.inevitable.enumerations.Align.CENTER
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KotlinCellFormatterAPITest {
    private val faker: LoremIpsum = LoremIpsum.getInstance()

    private val line1 = faker.getWords(5)
    private val line2 = faker.getWords(2)
    private val line3 = faker.getWords(3)

    @Test
    fun `Example vararg builder output`() {
        textCell(line1, line2, line3) {
            alignment(CENTER)
            padding(1, 1)
            paddingSpacer('*')
        }.println()
    }

    @Test
    fun `Example list builder output`() {
        textCell(listOf(line1, line2, line3)) {
            alignment(CENTER)
            padding(1, 1)
            paddingSpacer('*')
        }.println()
    }

    @Test
    fun `Example empty builder output`() {
        textCell {
            lines(line1, line2, line3)
            alignment(CENTER)
            padding(1, 1)
            paddingSpacer('*')
        }.println()
    }
}
