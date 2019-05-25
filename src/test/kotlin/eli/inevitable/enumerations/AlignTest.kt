package eli.inevitable.enumerations

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AlignTest {

    @Test
    fun `Center alignment with odd length margin & even line length`() {
        val margin = 5
        val expectedLineLength = 2
        val lines = listOf("x".repeat(expectedLineLength))
        val actualLines = Align.CENTER.paddify(lines, margin)
        actualLines.forEach {
            assertEquals(margin, it.length, """Unexpected line length:
                    |   content="$it"
                """.trimMargin())
        }
    }

    @Test
    fun `Center alignment with even length margin & odd line length`() {
        val margin = 4
        val expectedLineLength = 3
        val lines = listOf("x".repeat(expectedLineLength))
        val actualLines = Align.CENTER.paddify(lines, margin)
        actualLines.forEach {
            assertEquals(margin, it.length, """Unexpected line length:
                    |   content="$it"
                """.trimMargin())
        }
    }

    @Test
    fun `Center alignment with even length margin & even line length`() {
        val margin = 4
        val expectedLineLength = 2
        val lines = listOf("x".repeat(expectedLineLength))
        val actualLines = Align.CENTER.paddify(lines, margin)
        actualLines.forEach {
            assertEquals(margin, it.length, """Unexpected line length:
                    |   content="$it"
                """.trimMargin())
        }
    }

    @Test
    fun `Center alignment with odd length margin & odd line length`() {
        val margin = 5
        val expectedLineLength = 5
        val lines = listOf("x".repeat(expectedLineLength))
        val actualLines = Align.CENTER.paddify(lines, margin)
        actualLines.forEach {
            assertEquals(margin, it.length, """Unexpected line length:
                    |   content="$it"
                """.trimMargin())
        }
    }
}
