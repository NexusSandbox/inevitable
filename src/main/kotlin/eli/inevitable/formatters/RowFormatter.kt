package eli.inevitable.formatters

import eli.inevitable.formatters.TextFormatter.Companion.logger
import eli.inevitable.isPrintable
import eli.inevitable.templates.AbstractBuilderTemplate

/**
 * A generalized formatter that constructs a border-less row of formatted columized text.
 * This row may contain multiple lines of text.
 */
class RowFormatter private constructor(): TextFormatter {

    class Builder: AbstractBuilderTemplate<RowFormatter> {

        constructor(formatter: RowFormatter): super(RowFormatter()) {
            buildable.run {
                rawCells.addAll(formatter.rawCells)
                verticalDividerToken = formatter.verticalDividerToken
                columnWidths.addAll(formatter.columnWidths)
                height = formatter.height
                width = formatter.width
            }
        }

        constructor(): super(RowFormatter())

        constructor(vararg contents: CellFormatter): this(contents.toList())

        constructor(contents: List<CellFormatter>): this() {
            cells(contents)
        }

        companion object {
            @JvmStatic
            fun of(original: RowFormatter): Builder = Builder(original)

            @JvmStatic
            fun of(): Builder = Builder()

            @JvmStatic
            fun of(vararg contents: CellFormatter): Builder = Builder(contents.toList())

            @JvmStatic
            fun of(contents: List<CellFormatter>): Builder = Builder(contents)
        }

        /**
         * Resets the contents and dimensions of the [row][RowFormatter] but retains formatting options.
         * @return This [Builder]
         */
        fun resetContents(): Builder {
            checkFinalizedStatus()
            buildable.run {
                textLines.clear()
                columnWidths.clear()
                rawCells.clear()
                width = 0
                height = 0
            }

            return this
        }

        /**
         * @param cells The [array of cells][CellFormatter] to populate the rows. This will append to any prior cells already specified. This will also automatically append to the column widths according to the width of each cell.
         * @return This [Builder]
         */
        fun cells(vararg cells: CellFormatter): Builder {
            return cells(cells.toList())
        }

        /**
         * @param cells The [list of cells][CellFormatter] to populate the rows. This will append to any prior cells already specified. This will also automatically append to the column widths according to the width of each cell.
         * @return This [Builder]
         */
        fun cells(cells: List<CellFormatter>): Builder {
            checkFinalizedStatus()
            buildable.rawCells.addAll(cells)
            buildable.columnWidths.addAll(cells.map { it.getWidth() })

            return this
        }

        /**
         * @param widths The array of character widths for each column. This will reset any prior widths already specified. (Must match the total number of columns)
         * @return This [Builder]
         */
        fun columnWidths(vararg widths: Int): Builder {
            return columnWidths(widths.toList())
        }

        /**
         * @param widths The [list of character widths][List] for each column. This will reset any prior widths already specified. (Must match the total number of columns)
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
         * @param token The [displayable character][Char] used for separating the row's vertical borders.
         * @return This [Builder]
         */
        fun verticalDivider(token: Char): Builder {
            checkFinalizedStatus()
            buildable.verticalDividerToken = token

            return this
        }

        /**
         * @return The total number of lines in the row. This includes any specified vertical padding.
         */
        fun getTotalHeight(): Int = buildable.run {
            maxOf(height, rawCells.maxBy { it.getHeight() }?.getHeight() ?: 0)
        }

        /**
         * @return The maximum character width of the row. This includes any specified horizontal padding.
         */
        fun getTotalWidth(): Int = buildable.run {
            maxOf(width, columnWidths.sumBy { it } + (verticalDividerToken.let { columnWidths.size - 1 }))
        }

        override fun validate(): AbstractBuilderTemplate<RowFormatter> {
            buildable.run {
                require(isPrintable(verticalDividerToken)) {
                    "Vertical divider token is not a printable character: 0x${Integer.toHexString(verticalDividerToken.toInt()).toUpperCase()}"
                }

                require(rawCells.isNotEmpty()) { "Unable to generate a row with no columns." }
                require(rawCells.size == columnWidths.size) { "Unable to align cell columns with defined column widths" }
                rawCells.forEachIndexed { index, cellFormatter ->
                    if(columnWidths[index] > 0) require(cellFormatter.getWidth() <= columnWidths[index]) {
                        "Unable to fit cell with < index=$index > and < width=${cellFormatter.getWidth()} > into column with < width=${columnWidths[index]} >."
                    }
                }
            }

            return this
        }

        override fun build(): RowFormatter = buildable.apply {
            height = getTotalHeight()
            width = getTotalWidth()
            logger.info("Row dimensions: ($width, $height)")

            // Resize all the cells of the row
            val cells = rawCells.mapIndexed { index, cell ->
                if(columnWidths[index] == 0) columnWidths[index] = cell.getWidth()
                CellFormatter.Builder(cell)
                    .width(columnWidths[index])
                    .height(height)
                    .finish()
            }
            logger.info("Row Cell Count: ${cells.size}")

            // Join row of all cells together on same lines
            for(i in 0 until getTotalHeight()) {
                verticalDividerToken.let { textLines.add(cells.joinToString(verticalDividerToken.toString()) { it.getLines()[i] }) }
            }
        }
    }

    private val rawCells = mutableListOf<CellFormatter>()
    private var verticalDividerToken: Char = '|'

    private val columnWidths = mutableListOf<Int>()
    private var height = 0
    private var width = 0

    private val textLines = mutableListOf<String>()

    /**
     * @return The total number of columns specified in the row.
     */
    fun getTotalColumns() = columnWidths.size

    /**
     * @return The [list of column widths][List] for each column.
     */
    fun getColumnWidths() = columnWidths.toList()

    override fun getHeight() = height

    override fun getWidth() = width

    override fun getLines() = textLines.toList()
}
