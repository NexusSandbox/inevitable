package eli.inevitable.formatters

import com.thedeanda.lorem.LoremIpsum
import eli.inevitable.textCell
import eli.inevitable.textRow
import eli.inevitable.textTable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TableFormatterTest {
    private val faker: LoremIpsum = LoremIpsum.getInstance()

    @Test
    fun `An empty table is valid`() {
        val lines = textTable().getLines()
        assert(lines.isEmpty()) {
            "Unexpected non-empty table contents."
        }
    }

    @Test
    fun `A table with a single row is valid`() {
        val content = faker.getWords(1)
        val lines = textRow(textCell(content)).getLines()
        assertEquals(1, lines.size, """Unexpected number of lines:
                |   lines=$lines
            """.trimMargin())
        assertEquals(content.length, lines.first().length, """Unexpected length of line:
                         |  content=${lines.first()}
                     """.trimMargin())
    }

    @Test
    fun `A table with multiple rows is valid`() {
        val content =
            listOf(textRow(textCell(faker.getWords(1))), textRow(textCell(faker.getWords(1))), textRow(textCell(faker.getWords(1))))
        val formatter = textTable(content)
        val lines = formatter.getLines()
        assertEquals(content.size, lines.size, """Unexpected number of lines:
            |   lines=$lines
        """.trimMargin())
        val tableWidth = formatter.getWidth()
        for(i in 0 until content.size) {
            assertEquals(tableWidth, lines[i].length, """Unexpected length of content line:
                |   content[$i]=${lines[i]}
            """.trimMargin())
        }
    }

    @Test
    fun `A table with cells with multiple rows is valid`() {
        val content =
            listOf(textRow(textCell(faker.getWords(1), faker.getWords(1), faker.getWords(1)), textCell(faker.getWords(1)), textCell(faker.getWords(1), faker.getWords(1))))
        val formatter = textTable(content)
        val lines = formatter.getLines()
        assertEquals(3, lines.size, """Unexpected number of lines:
            |   lines=$lines
        """.trimMargin())
        val tableWidth = formatter.getWidth()
        for(i in 0 until content.size) {
            assertEquals(tableWidth, lines[i].length, """Unexpected length of content line:
                |   content[$i]=${lines[i]}
            """.trimMargin())
        }
    }

    @Test
    fun `A table with cells with a header is valid`() {
        val header =
            textRow(textCell(faker.getTitle(1)), textCell(faker.getTitle(1)), textCell(faker.getTitle(1)))
        val content1 =
            textRow(textCell(faker.getWords(1)), textCell(faker.getWords(2)), textCell(faker.getWords(3)))
        val content2 =
            textRow(textCell(faker.getWords(1)), textCell(faker.getWords(2)), textCell(faker.getWords(3)))
        val formatter = textTable(content1, content2) {
            header(header)
            headerDivider('|', '=')
        }
        val lines = formatter.getLines()
        assertEquals(4, lines.size, """Unexpected number of lines:
            |   lines=$lines
        """.trimMargin())
        val tableWidth = formatter.getWidth()
        for(i in 0 until lines.size) {
            assertEquals(tableWidth, lines[i].length, """Unexpected length of content line:
                |    content[$i]=${lines[i]}
            """.trimMargin())
        }
    }

    @Test
    fun `A table with cells with a footer is valid`() {
        val footer =
            textRow(textCell(faker.getTitle(1)), textCell(faker.getTitle(1)), textCell(faker.getTitle(1)))
        val content1 =
            textRow(textCell(faker.getWords(1)), textCell(faker.getWords(2)), textCell(faker.getWords(3)))
        val content2 =
            textRow(textCell(faker.getWords(1)), textCell(faker.getWords(2)), textCell(faker.getWords(3)))
        val formatter = textTable(content1, content2) {
            footer(footer)
            footerDivider('|', '=')
        }
        val lines = formatter.getLines()
        assertEquals(4, lines.size, """Unexpected number of lines:
            |   lines=$lines
        """.trimMargin())
        val tableWidth = formatter.getWidth()
        for(i in 0 until lines.size) {
            assertEquals(tableWidth, lines[i].length, """Unexpected length of content line:
                |    content[$i]=${lines[i]}
            """.trimMargin())
        }
    }

    @Test
    fun `A table with multiple row with varying cell sizes is valid`() {
        val row1 =
            textRow(textCell(faker.getWords(5), faker.getWords(2)), textCell(faker.getWords(2)))
        val row2 =
            textRow(textCell(faker.getWords(5)), textCell(faker.getWords(1), faker.getWords(2)))
        val formatter = textTable(row1, row2)
        val lines = formatter.getLines()
        assertEquals(4, lines.size, """Unexpected number of lines:
            |   lines=$lines
        """.trimMargin())
        val tableWidth = formatter.getWidth()
        for(i in 0 until lines.size) {
            assertEquals(tableWidth, lines[i].length, """Unexpected length of content line:
                |    content[$i]=${lines[i]}
            """.trimMargin())
        }
    }
}
