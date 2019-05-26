package eli.inevitable

import com.thedeanda.lorem.LoremIpsum
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KotlinRowFormatterAPITest {
    private val faker: LoremIpsum = LoremIpsum.getInstance()

    @Test
    fun `Example vararg builder output`() {
        val cell1 = textCell(faker.getWords(5), faker.getWords(2))
        val cell2 = textCell(faker.getWords(2))
        val cell3 = textCell(faker.getWords(3), faker.getWords(2), faker.getWords(1))
        textRow(cell1, cell2, cell3) {
            verticalDivider(':')
        }.println()
    }

    @Test
    fun `Example list builder output`() {
        val cell1 = textCell(faker.getWords(5), faker.getWords(2))
        val cell2 = textCell(faker.getWords(2))
        val cell3 = textCell(faker.getWords(3), faker.getWords(2), faker.getWords(1))
        textRow(listOf(cell1, cell2, cell3)) {
            verticalDivider(':')
        }.println()
    }

    @Test
    fun `Example empty builder output`() {
        val cell1 = textCell(faker.getWords(5), faker.getWords(2))
        val cell2 = textCell(faker.getWords(2))
        val cell3 = textCell(faker.getWords(3), faker.getWords(2), faker.getWords(1))
        textRow {
            cells(cell1, cell2, cell3)
            verticalDivider(':')
        }.println()
    }

    @Test
    fun `Example empty appending builder output`() {
        textRow {
            cells(textCell(faker.getWords(5), faker.getWords(2)))
            cells(textCell(faker.getWords(2)))
            cells(textCell(faker.getWords(3), faker.getWords(2), faker.getWords(1)))
            verticalDivider(':')
        }.println()
    }
}
