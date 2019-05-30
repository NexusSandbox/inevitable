package eli.inevitable.formatters

import com.thedeanda.lorem.LoremIpsum
import eli.inevitable.maxOf
import eli.inevitable.textBlock
import eli.inevitable.textCell
import eli.inevitable.textDivider
import eli.inevitable.textRow
import eli.inevitable.textTable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlockFormatterTest {
    private val faker: LoremIpsum = LoremIpsum.getInstance()

    @Test
    fun `An empty block is not valid`() {
        val actualException =
            assertThrows<IllegalArgumentException>("Expected exception not thrown.") {
                textBlock()
            }
        assertEquals("Unexpected empty body content size.", actualException.message, "Unexpected exception message.")
    }

    @Test
    fun `A block can be populated with a single CellFormatter element`() {
        val content = faker.getWords(1)
        val block = textBlock(textCell(content))
        val lines = block.getLines()
        assertEquals(3, block.getHeight(), """Unexpected number of lines:
            |    content=$lines
        """.trimMargin())
        val blockWidth = block.getWidth()
        assertEquals(content.length + 2, blockWidth, """Unexpected total widths:
            |    content=$lines
        """.trimMargin())
        for(i in 0 until lines.size) {
            assertEquals(blockWidth, lines[i].length, """Unexpected length of content line:
                |    content[$i]=${lines[i]}
            """.trimMargin())
        }
    }

    @Test
    fun `A block can be populated with a multiple TextFormatter elements`() {
        val content1 = textCell(faker.getWords(1))
        val content2 = textRow(textCell(faker.getWords(1)), textCell(faker.getWords(1)))
        val content3 = textDivider(1)
        val content4 = textTable(textRow(textCell(faker.getWords(1)), textCell(faker.getWords(1))))
        val block = textBlock(content1, content2, content3, content4)
        val lines = block.getLines()
        assertEquals(6, block.getHeight(), """Unexpected number of lines:
            |    content=$lines
        """.trimMargin())
        val blockWidth = block.getWidth()
        assertEquals((maxOf(content1.getWidth(), content2.getWidth(), content3.getWidth(), content4.getWidth())
                      ?: 0) + 2, blockWidth, """Unexpected total widths:
            |    content=$lines
        """.trimMargin())
        for(i in 0 until lines.size) {
            assertEquals(blockWidth, lines[i].length, """Unexpected length of content line:
                |    content[$i]=${lines[i]}
            """.trimMargin())
        }
    }

    @Test
    fun `A block can have a title`() {
        val title = faker.getWords(1)
        val content = faker.getWords(1)
        val block = textBlock(textCell(content)) {
            title(textCell(title))
        }
        val lines = block.getLines()
        assertEquals(4, block.getHeight(), """Unexpected number of lines:
            |    content=$lines
        """.trimMargin())
        val blockWidth = block.getWidth()
        assertEquals(maxOf(content.length + 2, title.length), blockWidth, """Unexpected total widths:
            |    content=$lines
        """.trimMargin())
        for(i in 0 until lines.size) {
            assertEquals(blockWidth, lines[i].length, """Unexpected length of content line:
                |    content[$i]=${lines[i]}
            """.trimMargin())
        }
    }

    @Test
    fun `A block can have a caption`() {
        val caption = faker.getWords(1)
        val content = faker.getWords(1)
        val block = textBlock(textCell(content)) {
            caption(textCell(caption))
        }
        val lines = block.getLines()
        assertEquals(4, block.getHeight(), """Unexpected number of lines:
            |    content=$lines
        """.trimMargin())
        val blockWidth = block.getWidth()
        assertEquals(maxOf(content.length + 2, caption.length), blockWidth, """Unexpected total widths:
            |    content=$lines
        """.trimMargin())
        for(i in 0 until lines.size) {
            assertEquals(blockWidth, lines[i].length, """Unexpected length of content line:
                |    content[$i]=${lines[i]}
            """.trimMargin())
        }
    }

    @Test
    fun `A block can have both a title and a caption`() {
        val title = faker.getWords(1)
        val caption = faker.getWords(1)
        val content = faker.getWords(1)
        val block = textBlock(textCell(content)) {
            title(textCell(title))
            caption(textCell(caption))
        }
        val lines = block.getLines()
        assertEquals(5, block.getHeight(), """Unexpected number of lines:
            |    content=$lines
        """.trimMargin())
        val blockWidth = block.getWidth()
        assertEquals(maxOf(content.length + 2, caption.length, title.length), blockWidth, """Unexpected total widths:
            |    content=$lines
        """.trimMargin())
        for(i in 0 until lines.size) {
            assertEquals(blockWidth, lines[i].length, """Unexpected length of content line:
                |    content[$i]=${lines[i]}
            """.trimMargin())
        }
    }
}
