package eli.inevitable.formatters

import eli.inevitable.isPrintable
import eli.inevitable.templates.AbstractBuilderTemplate
import org.apache.logging.log4j.LogManager

/**
 * A generalized formatter that constructs a border-less divider row of formatted text. This row
 * will only contain a single line of text.
 */
class DivFormatter private constructor(): TextFormatter {
    override val logger = LogManager.getLogger(javaClass)!!

    class Builder: AbstractBuilderTemplate<DivFormatter> {
        constructor(formatter: DivFormatter): super(DivFormatter()) {
            buildable.run {
                verticalDividerToken = formatter.verticalDividerToken
                horizontalDividerToken = formatter.horizontalDividerToken
                columnWidths.addAll(formatter.columnWidths)
                width = formatter.width
            }
        }

        constructor(): super(DivFormatter())

        constructor(vararg columnWidths: Int): this(columnWidths.toList())

        constructor(columnWidths: List<Int>): this() {
            columnWidths(columnWidths)
        }

        companion object {
            @JvmStatic
            fun of(original: DivFormatter): Builder = Builder(original)

            @JvmStatic
            fun of(): Builder = Builder()

            @JvmStatic
            fun of(vararg columnWidths: Int): Builder = Builder(columnWidths.toList())

            @JvmStatic
            fun of(columnWidths: List<Int>): Builder = Builder(columnWidths)
        }

        /**
         * Resets the contents and dimensions of the [divider][DivFormatter] but retains formatting options.
         * @return This [Builder]
         */
        fun resetContents(): Builder {
            checkFinalizedStatus()
            buildable.run {
                columnWidths.clear()
                line = ""
                width = 0
            }

            return this
        }

        /**
         * @param widths The array of character widths for each column. This will reset any prior widths already specified.
         * @return This [Builder]
         */
        fun columnWidths(vararg widths: Int): Builder {
            return columnWidths(widths.toList())
        }

        /**
         * @param widths The [list of character widths][List] for each column. This will reset any prior widths already specified.
         * @return This [Builder]
         */
        fun columnWidths(widths: List<Int>): Builder {
            checkFinalizedStatus()
            buildable.run {
                columnWidths.clear()
                columnWidths.addAll(widths)
            }

            return this
        }

        /**
         * @param token The [displayable character][Char] used for separating each column.
         * @return This [Builder]
         */
        fun verticalDivider(token: Char): Builder {
            checkFinalizedStatus()
            buildable.verticalDividerToken = token

            return this
        }

        /**
         * @param token The [displayable character][Char] used for separating each row.
         * @return This [Builder]
         */
        fun horizontalDivider(token: Char): Builder {
            checkFinalizedStatus()
            buildable.horizontalDividerToken = token

            return this
        }

        /**
         * @return The maximum character width of the divider.
         */
        fun getTotalWidth(): Int = buildable.run {
            maxOf(width, columnWidths.sumBy { it } + columnWidths.size - 1)
        }

        override fun validate(): AbstractBuilderTemplate<DivFormatter> {
            buildable.run {
                require(isPrintable(verticalDividerToken)) {
                    "Vertical divider token is not a printable character: 0x${Integer.toHexString(verticalDividerToken.toInt()).toUpperCase()}"
                }
                require(isPrintable(horizontalDividerToken)) {
                    "Horizontal divider token is not a printable character: 0x${Integer.toHexString(horizontalDividerToken.toInt()).toUpperCase()}"
                }

                require(columnWidths.size > 0) {
                    "Unable to generate a divider with no columns."
                }
                require(columnWidths.all { it > 0 }) { "Invalid non-positive < columnWidth=$columnWidths >." }
            }

            return this
        }

        override fun build(): DivFormatter = buildable.apply {
            width = getTotalWidth()
            logger.atInfo().log("Divider width: $width")

            line = columnWidths.joinToString(verticalDividerToken.toString()) {
                horizontalDividerToken.toString()
                    .repeat(it)
            }
            logger.atInfo().log("Divider Content: \"$line\"")
        }
    }

    private var verticalDividerToken: Char = '|'
    private var horizontalDividerToken: Char = '-'

    private var columnWidths = mutableListOf<Int>()
    private var width = 0

    private var line = ""

    /**
     * @return The total number of columns specified in the divider.
     */
    fun getTotalColumns() = columnWidths.size

    /**
     * @return The [list of column widths][List] for each column.
     */
    fun getColumnWidths() = columnWidths.toList()

    override fun getHeight() = 1

    override fun getWidth() = width

    override fun getLines() = listOf(line)
}
