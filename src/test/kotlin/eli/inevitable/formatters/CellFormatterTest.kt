package eli.inevitable.formatters

import com.thedeanda.lorem.LoremIpsum
import eli.inevitable.textCell
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CellFormatterTest {
    private val faker: LoremIpsum = LoremIpsum.getInstance()

    @Test
    fun `An empty cell is valid`() {
        val lines = textCell().getLines()
        assert(lines.isEmpty()) {
            "Unexpected non-empty cell contents"
        }
    }

    @Test
    fun `A cell with single word is valid`() {
        val content = faker.getWords(1)
        val lines = textCell(content).getLines()
        assertEquals(1, lines.size, """Unexpected number of lines:
            |   lines=$lines
            """.trimMargin())
        assertEquals(content.length, lines.first().length, """Unexpected length of line:
            |   content=${lines.first()}
            """.trimMargin())
    }

    @Test
    fun `A cell with multiple lines is valid`() {
        val content = listOf<String>(faker.getWords(1), faker.getWords(1), faker.getWords(1))
        val formatter = textCell(content)
        val lines = formatter.getLines()
        assertEquals(content.size, lines.size, """Unexpected number of lines:
            |   lines=$lines
            """.trimMargin())
        val cellWidth = formatter.getWidth()
        for(i in 0 until content.size) {
            assertEquals(cellWidth, lines[i].length, """Unexpected length of content line:
                |   content=${lines[i]}
                """.trimMargin())
        }
    }

    @Test
    fun `A cell with vertically filled lines is valid`() {
        val contentHeight = 5
        val content = listOf<String>(faker.getWords(1), faker.getWords(1), faker.getWords(1))
        val formatter = textCell(content) {
            maxHeight(contentHeight)
        }
        val lines = formatter.getLines()
        assertEquals(contentHeight, lines.size, """Unexpected number of lines:
            |   lines=$lines
            """.trimMargin())
        val cellWidth = formatter.getWidth()
        for(i in 0 until content.size) {
            assertEquals(cellWidth, lines[i].length, """Unexpected length of content line:
                |   content=${lines[i]}
                """.trimMargin())
        }
        for(i in content.size until contentHeight) {
            assertEquals(cellWidth, lines[i].length, """Unexpected length of filled line:
                |   line=${lines[i]}
                """.trimMargin())
        }
    }

    @Test
    fun `A cell copy is immutable`() {
        val formatter1 = textCell("Testy")
        val formatter2 =
            CellFormatter.Builder(formatter1)
                .lines("McTesterson")
                .finish()
        assert(formatter1.getLines() != formatter2.getLines()) { "Copy constructor should create a new independent instance." }
    }
}
