package eli.inevitable

import com.thedeanda.lorem.LoremIpsum
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KotlinRowFormatterAPITest {
    private val faker: LoremIpsum = LoremIpsum.getInstance()

    private val cell1 = textCell(faker.getWords(5), faker.getWords(2))
    private val cell2 = textCell(faker.getWords(2))
    private val cell3 = textCell(faker.getWords(3), faker.getWords(2), faker.getWords(1))

    @Test
    fun `Example vararg builder output`() {
        textRow(cell1, cell2, cell3) {
            verticalDivider(':')
        }.println()
    }

    @Test
    fun `Example list builder output`() {
        textRow(listOf(cell1, cell2, cell3)) {
            verticalDivider(':')
        }.println()
    }

    @Test
    fun `Example empty builder output`() {
        textRow {
            cells(cell1, cell2, cell3)
            verticalDivider(':')
        }.println()
    }

    @Test
    fun `Example empty appending builder output`() {
        textRow {
            cells(cell1)
            cells(cell2)
            cells(cell3)
            verticalDivider(':')
        }.println()
    }
}
