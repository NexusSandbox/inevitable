package eli.inevitable.formatters

import eli.inevitable.textDivider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DivFormatterTest {
    @Test
    fun `An empty divider is not valid`() {
        val actualException =
            assertThrows<IllegalArgumentException>("Expected exception not thrown.") {
                DivFormatter.Builder()
                    .finish()
            }
        assertEquals("Unable to generate a divider with no columns.", actualException.message, "Unexpected exception message.")
    }

    @Test
    fun `A divider can be populated with a single column`() {
        val columnWidth = 5
        val divider = textDivider(1) {
            columnWidths(5)
        }
        assertEquals(1, divider.getTotalColumns(), """Unexpected number of columns:
            |   content=${divider.getLines()}
        """.trimMargin())
        assertEquals(columnWidth, divider.getWidth(), """Unexpected total width:
            |   content=${divider.getLines()}
        """.trimMargin())
    }

    @Test
    fun `A divider can be populated with multiple columns`() {
        val columnWidth1 = 1
        val columnWidth2 = 2
        val columnWidth3 = 3
        val divider = textDivider(3) {
            columnWidths(columnWidth1, columnWidth2, columnWidth3)
        }
        assertEquals(3, divider.getTotalColumns(), """Unexpected number of columns:
            |   content=${divider.getLines()}
        """.trimMargin())
        assertEquals(2 + columnWidth1 + columnWidth2 + columnWidth3, divider.getWidth(), """Unexpected total width:
            |   content=${divider.getLines()}
        """.trimMargin())
    }
}
