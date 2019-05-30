package eli.inevitable

import com.thedeanda.lorem.LoremIpsum
import eli.inevitable.enumerations.Align
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KotlinBlockFormatterAPITest {
    private val faker: LoremIpsum = LoremIpsum.getInstance()

    private val title = textCell(faker.getTitle(2)) {
        alignment(Align.CENTER)
    }
    private val caption = textCell(faker.getWords(3))
    private val tableContent =
        textTable(textRow(textCell(faker.getWords(1)), textCell(faker.getWords(1)), textCell(faker.getWords(1))), textRow(textCell(faker.getWords(1)), textCell(faker.getWords(1)), textCell(faker.getWords(1)))) {
            header(textRow(textCell(faker.getWords(1)), textCell(faker.getWords(1)), textCell(faker.getWords(1))))
            headerDivider('|', '=')
        }

    @Test
    fun `Example vararg builder output`() {
        textBlock(tableContent) {
            title(title)
            caption(caption)
        }.println()
    }

    @Test
    fun `Example list builder output`() {
        textBlock(listOf(tableContent)) {
            title(title)
            caption(caption)
        }.println()
    }

    @Test
    fun `Example empty builder output`() {
        textBlock {
            contents(tableContent)
            title(title)
            caption(caption)
        }.println()
    }
}
