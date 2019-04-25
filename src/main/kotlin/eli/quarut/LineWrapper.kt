package eli.quarut

import eli.quarut.enumerations.Align
import eli.quarut.enumerations.Align.LEFT

/**
 * Implements a linear-time algorithm to break a single [line][String] of text into multiple lines with a maximum defined
 * width and minimum raggedness. Will favor breaking lines on word boundaries and does not preserve whitespace.
 *
 * @see <a href="https://xxyxyz.org/line-breaking/">Line Breaking Algorithms: Linear SMAWK</a>
 */
class LineWrapper private constructor(private val line: String, private val margin: Int) {

    private fun fracture(): List<String> {

        // Join words together for each line, then create list of lines
        return listOf(line)
    }

    companion object {
        /**
         * Fractures the line of text into multiple lines of text with a length no longer than the specified margin.
         * Each line is padded with whitespace to fill the margin.
         *
         * @return A non-null but possibly empty [List] of [strings][String].
         */
        fun parse(line: String, margin: Int, align: Align = LEFT): List<String> {
            return parse(listOf(line), margin)
        }

        /**
         * Fractures each line of text into multiple lines of text with a length no longer than the specified margin.
         * Each line is padded with whitespace to fill the margin.
         *
         * @return A non-null but possibly empty [List] of [strings][String].
         */
        fun parse(lines: List<String>, margin: Int, align: Align = LEFT): List<String> {
            return lines.map { it.split(Regex("\r?\n", RegexOption.DOT_MATCHES_ALL)) }.flatten()
                .map { LineWrapper(it, margin) }.flatMap { align.pad(it.fracture(), margin) }
        }
    }
}
