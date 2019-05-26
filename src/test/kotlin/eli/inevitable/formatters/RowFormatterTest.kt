package eli.inevitable.formatters

import com.thedeanda.lorem.LoremIpsum
import eli.inevitable.maxWidth
import eli.inevitable.textCell
import eli.inevitable.textRow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RowFormatterTest {
    private val faker: LoremIpsum = LoremIpsum.getInstance()

    @Test
    fun `An empty row is not valid`() {
        val actualException =
            assertThrows<IllegalArgumentException>("Expected exception not thrown.") {
                RowFormatter.Builder()
                    .finish()
            }
        assertEquals("Unable to generate a row with no columns.", actualException.message, "Unexpected exception message.")
    }

    @Test
    fun `A row can be populated with a single cell with a single row`() {
        val content = faker.getWords(1)
        val row = textRow(textCell(content))
        assertEquals(1, row.getTotalColumns(), """Unexpected number of columns:
            |   content=${row.getLines()}
            """.trimMargin())
        assertEquals(1, row.getHeight(), """Unexpected number of lines:
            |   content=${row.getLines()}
            """.trimMargin())
        assertEquals(content.length, row.getWidth(), """Unexpected total width:
            |   content=${row.getLines()}
            """.trimMargin())
    }

    @Test
    fun `A row can be populated with multiple cells with a single row`() {
        val content1 = faker.getWords(1)
        val content2 = faker.getWords(2)
        val content3 = faker.getWords(3)
        val row = textRow(textCell(content1), textCell(content2), textCell(content3))
        assertEquals(3, row.getTotalColumns(), """Unexpected number of columns:
            |   content=${row.getLines()}
            """.trimMargin())
        assertEquals(1, row.getHeight(), """Unexpected number of lines:
            |   content=${row.getLines()}
            """.trimMargin())
        assertEquals(2 + content1.length + content2.length + content3.length, row.getWidth(), """Unexpected total width:
            |   content=${row.getLines()}
            """.trimMargin())
    }

    @Test
    fun `A row can be populated with multiple cells with multiple rows`() {
        val content1 =
            faker.getWords(1)
                .split(" ")
        val content2 =
            faker.getWords(2)
                .split(" ")
        val content3 =
            faker.getWords(3)
                .split(" ")
        val row = textRow(textCell(content1), textCell(content2), textCell(content3))
        assertEquals(3, row.getTotalColumns(), """Unexpected number of columns:
            |   content=${row.getLines()}
            """.trimMargin())
        assertEquals(3, row.getHeight(), """Unexpected number of lines:
            |   content=${row.getLines()}
            """.trimMargin())
        assertEquals(2 + content1.maxWidth() + content2.maxWidth() + content3.maxWidth(), row.getWidth(), """Unexpected total width:
            |   content=${row.getLines()}
            """.trimMargin())
    }
}
