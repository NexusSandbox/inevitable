package eli.inevitable

import eli.inevitable.formatters.CellFormatter
import eli.inevitable.formatters.DivFormatter
import kotlin.text.RegexOption.DOT_MATCHES_ALL

/**
 * System agnostic character sequence for generating a line break.
 */
val LINE_BREAK = System.lineSeparator()
/**
 * Regex pattern for identifying and breaking up new lines.
 */
val LINE_BREAK_PATTERN = Regex("""(\r\n|\r|\n)""", DOT_MATCHES_ALL)

/**
 * Regex pattern for identifying and replacing tab characters.
 */
val TAB_PATTERN = Regex("""\t""", DOT_MATCHES_ALL)

/**
 * Regex pattern for identifying all non-printable characters (excluding space ' ').
 */
val NON_DISPLAY_PATTERN = Regex("""^[\S ]+$""", DOT_MATCHES_ALL)

/**
 * Extension function to determine the max width of a [collection of strings][Collection]
 */
fun Collection<String>.maxWidth(): Int = this.maxBy { it.length }?.length ?: 0

/**
 * Breaks apart a single [line of text][String] into multiple lines if there are any line
 * breaks, converts all tab characters into spaces, and trims any extra whitespace off the end
 * of each line.
 *
 * @param tabSpaces The number of spaces to use in place of a tab character.
 *
 * Default: 2
 * @return A [List] of sanitized [strings][String].
 */
fun String.sanitize(tabSpaces: Int = 2): List<String> = LINE_BREAK_PATTERN.split(this).map {
    TAB_PATTERN.replace(it.trimEnd(), " ".repeat(tabSpaces))
}

/**
 * @param token A [token][Char] to test.
 * @return `True` if the [token][Char] is able to be displayed to the screen; otherwise `false`.
 */
fun isPrintable(token: Char): Boolean {
    return NON_DISPLAY_PATTERN.matches(token.toString())
}

/**
 * @param things An [array of values][Int] to scan.
 * @return The maximum value of all values in the array. Null if the array is empty (no values)
 */
fun maxOf(vararg things: Int): Int? {
    return things.maxBy { it }
}

/**
 * Constructs a block of formatted text. This block may optionally be
 * padded with an arbitrary number of empty rows or columns of text.
 * @param contents An array of [lines of text][String] contained within the cell.
 * @param init An optional initialization lambda used to configure the formatting for the cell.
 * @return A finalized [CellFormatter]
 */
fun textCell(vararg contents: String,
             init: (CellFormatter.Builder.() -> CellFormatter.Builder)? = null): CellFormatter {
    return textCell(contents.toList(), init)
}

/**
 * Constructs a block of formatted text. This block may optionally be
 * padded with an arbitrary number of empty rows or columns of text.
 * @param contents A [list][List] of [lines of text][String] contained within the cell.
 * @param init An optional initialization lambda used to configure the formatting for the cell.
 * @return A finalized [CellFormatter]
 */
fun textCell(contents: List<String>,
             init: (CellFormatter.Builder.() -> CellFormatter.Builder)? = null): CellFormatter {
    val builder = CellFormatter.Builder(contents)
    return if(init == null) builder.finish() else builder.init().finish()
}

// /**
//  * Constructs a row of blocks of formatted text.
//  * @param contents An array of [cells][CellFormatter] contained within the row.
//  * @param init An optional initialization lambda used to configure the formatting for the row.
//  * @return A finalized [RowFormatter]
//  */
// fun textRow(vararg contents: CellFormatter,
//             init: (RowFormatter.Builder.() -> RowFormatter.Builder)? = null): RowFormatter {
//     return textRow(contents.toList(), init)
// }
//
// /**
//  * Constructs a row of blocks of formatted text.
//  * @param contents A [list][List] of [cells][CellFormatter] contained within the row.
//  * @param init An optional initialization lambda used to configure the formatting for the row.
//  * @return A finalized [RowFormatter]
//  */
// fun textRow(contents: List<CellFormatter>,
//             init: (RowFormatter.Builder.() -> RowFormatter.Builder)? = null): RowFormatter {
//     val builder = RowFormatter.Builder(contents)
//     return if(init == null) builder.finish() else builder.init().finish()
// }

/**
 * Constructs a divider of formatted text.
 * @param columnCount The total number of columns
 * @param init An optional initialization lambda used to configure the formatting for the divider.
 * @return A finalized [DivFormatter]
 */
fun textDivider(columnCount: Int,
                init: (DivFormatter.Builder.() -> DivFormatter.Builder)? = null): DivFormatter {
    val builder = DivFormatter.Builder(columnCount)
    return if(init == null) builder.finish() else builder.init().finish()
}

// /**
//  * Constructs a table of rows of formatted text.
//  * @param rows An array of [rows][RowFormatter] contained within the table.
//  * @param init An optional initialization lambda used to configure the formatting for the table.
//  * @return A finalized [TableFormatter]
//  */
// fun textTable(vararg rows: RowFormatter,
//               init: (TableFormatter.Builder.() -> TableFormatter.Builder)? = null): TableFormatter {
//     return textTable(rows.toList(), init)
// }
//
// /**
//  * Constructs a table of rows of formatted text.
//  * @param rows A [list][List] of [rows][RowFormatter] contained within the table.
//  * @param init An optional initialization lambda used to configure the formatting for the table.
//  * @return A finalized [TableFormatter]
//  */
// fun textTable(rows: List<RowFormatter>,
//               init: (TableFormatter.Builder.() -> TableFormatter.Builder)? = null): TableFormatter {
//     val builder = TableFormatter.Builder(rows)
//     return if(init == null) builder.finish() else builder.init().finish()
// }

// /**
//  * Constructs a bordered block of formatted text.
//  * @param contents An array of [formatters][TextFormatter] contained within the block.
//  * @param init An optional initialization lambda used to configure the formatting for the block.
//  * @return A finalized [BlockFormatter]
//  */
// fun textBlock(vararg contents: TextFormatter,
//               init: (BlockFormatter.Builder.() -> BlockFormatter.Builder)?): BlockFormatter {
//     return textBlock(contents.toList(), init)
// }
//
// /**
//  * Constructs a bordered block of formatted text.
//  * @param contents A [list][List] of [formatters][TextFormatter] contained within the block.
//  * @param init An optional initialization lambda used to configure the formatting for the block.
//  * @return A finalized [BlockFormatter]
//  */
// fun textBlock(contents: List<TextFormatter>,
//               init: (BlockFormatter.Builder.() -> BlockFormatter.Builder)?): BlockFormatter {
//     val builder = BlockFormatter.Builder(contents)
//     return if(init == null) builder.finish() else builder.init().finish()
// }
