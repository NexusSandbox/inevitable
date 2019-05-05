package eli.inevitable

import com.google.common.flogger.FluentLogger
import kotlin.text.RegexOption.DOT_MATCHES_ALL

/**
 * Implements a linear-time algorithm to break a single [line of text][String] into multiple lines with a maximum defined width. Will favor breaking lines on word boundaries.
 *
 * @see <a href="https://xxyxyz.org/line-breaking/">Line Breaking Algorithms</a>
 */
class LineWrapper(private val line: String, private val margin: Int) {

    private val fracturePoints = fracturePointPattern.findAll(line).toList()

    fun fracture(): List<String> {

        logger.atInfo().log("Fracture Points: ${fracturePoints.size}")
        fracturePoints.forEach {
            logger.atInfo().log("Fracture -> [Range: ${it.range}; Value: \"${it.value}\"")
        }

        // Join words together for each line, then create list of lines
        return listOf(line)
    }

    companion object {
        private val logger = FluentLogger.forEnclosingClass()
        private val fracturePointPattern = Regex("""(^\s*|\s+)""", DOT_MATCHES_ALL)
    }
}

/**
 * Fractures the line of text into multiple lines of text with a length no longer than the specified margin.
 *
 * Each line is padded with whitespace to fill the margin.
 *
 * @param margin The maximum number of characters(including whitespace) allocated between each fracture-point.
 * @param tabSpaces The number of non-breaking whitespace characters used to replace any tab characters. Default: 4
 * @return A non-null but possibly empty [List] of [strings][String].
 */
fun String.wrapLines(margin: Int, tabSpaces: Int = 4): List<String> = listOf(this).wrapLines(margin,
                                                                                             tabSpaces)

/**
 * Fractures each line of text into multiple lines of text with a length no longer than the specified margin.
 *
 * Each line is padded with whitespace to fill the margin.
 *
 * @param margin The maximum number of characters(including whitespace) allocated between each fracture-point.
 * @param tabSpaces The number of non-breaking whitespace characters used to replace any tab characters. Default: 4
 * @return A non-null but possibly empty [List] of [strings][String].
 */
fun List<String>.wrapLines(margin: Int, tabSpaces: Int = 4): List<String> = this.map {
    it.replace("\t", " ".repeat(tabSpaces)).split(Regex("\r?\n", DOT_MATCHES_ALL))
}.flatten().flatMap { LineWrapper(it, margin).fracture() }
