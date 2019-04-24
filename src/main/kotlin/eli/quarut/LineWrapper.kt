package eli.quarut

/**
 * Implements a linear-time algorithm to break a single [line][String] of text into multiple lines with a maximum defined
 * width and minimum raggedness. Will favor breaking lines on word boundaries and does not retain surrounding whitespace
 * between words.
 *
 * @see <a href="https://xxyxyz.org/line-breaking/">Line Breaking Algorithms</a>
 */
class LineWrapper private constructor(line: String, margin: Int) {

    private fun fracture(): List<String> {
        return listOf()
    }

    companion object {
        /**
         * Fractures the line of text into multiple lines of text with a length no longer than the specified margin.
         * Each line is padded with whitespace to fill the margin.
         *
         * @return A non-null but possibly empty [List] of [strings][String].
         */
        fun parse(line: String, margin: Int): List<String> {
            return parse(listOf(line), margin)
        }

        /**
         * Fractures each line of text into multiple lines of text with a length no longer than the specified margin.
         * Each line is padded with whitespace to fill the margin.
         *
         * @return A non-null but possibly empty [List] of [strings][String].
         */
        fun parse(lines: List<String>, margin: Int): List<String> {
            return lines.map { it.split(Regex("[\r\n]+", RegexOption.DOT_MATCHES_ALL)) }
                .flatten()
                .map { LineWrapper(it, margin) }
                .flatMap { it.fracture() }
        }
    }
}