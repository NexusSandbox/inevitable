package eli.inevitable.formatters

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LineWrapperTest {
    private val defaultMargin = 80

    @ParameterizedTest(name = "Empty text: \"{0}\"")
    @ValueSource(strings = [""])
    fun `An empty string is valid`(input: String) {
        input.wrapLines(defaultMargin)
    }

    @ParameterizedTest(name = "Text containing whitespace: \"{0}\"")
    @ValueSource(strings = [" ", "\t"])
    fun `A blank string is valid`(input: String) {
        input.wrapLines(defaultMargin)
    }

    @ParameterizedTest(name = "Text containing newline: \"{0}\"")
    @ValueSource(strings = ["\n", "\r\n"])
    fun `A line break is valid`(input: String) {
        input.wrapLines(defaultMargin)
    }

    @Test
    fun `Structured ASCII text is valid`() {
    }

    @Test
    fun `Multi-lined Structured ASCII text is valid`() {
    }

    @Test
    fun `Structured Unicode text is valid`() {
    }

    @Test
    fun `Non-printable character text is valid`() {
    }
}
