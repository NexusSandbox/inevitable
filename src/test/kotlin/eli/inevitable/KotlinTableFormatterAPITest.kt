package eli.inevitable

import com.thedeanda.lorem.LoremIpsum
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KotlinTableFormatterAPITest {
    private val faker: LoremIpsum = LoremIpsum.getInstance()

    private val header =
        textRow(textCell(faker.getTitle(1)), textCell(faker.getTitle(1)), textCell(faker.getTitle(1)))
    private val row1 =
        textRow(textCell(faker.getWords(5), faker.getWords(2)), textCell(faker.getWords(2)), textCell(faker.getWords(3), faker.getWords(2), faker.getWords(1)))
    private val row2 =
        textRow(textCell(faker.getWords(5)), textCell(faker.getWords(2), faker.getWords(2)), textCell(faker.getWords(3), faker.getWords(2), faker.getWords(1)))
    private val row3 =
        textRow(textCell(faker.getWords(5), faker.getWords(2), faker.getWords(1)), textCell(faker.getWords(2), faker.getWords(2)), textCell(faker.getWords(3)))

    @Test
    fun `Example vararg builder output`() {
        textTable(row1, row2, row3) {
            header(header)
            headerDivider('|', '=')
        }.println()
    }

    @Test
    fun `Example list builder output`() {
        textTable(listOf(row1, row2, row3)) {
            header(header)
            headerDivider('|', '=')
        }.println()
    }

    @Test
    fun `Example empty builder output`() {
        textTable {
            body(row1, row2, row3)
            header(header)
            headerDivider('|', '=')
        }.println()
    }

    @Test
    fun `Example empty appending builder output`() {
        textTable {
            body(row1)
            body(row2)
            body(row3)
            header(header)
            headerDivider('|', '=')
        }.println()
    }
}
