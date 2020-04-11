package eli.inevitable.formatters

import eli.inevitable.maxOf
import eli.inevitable.maxWidth
import eli.inevitable.templates.AbstractBuilderTemplate
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * A generalized formatter that constructs a table of formatted text. This table will align each of the columns to the maximum character width, and each of the rows to the maximum line count for that row.
 */
class TableFormatter private constructor(): TextFormatter {
    override val logger = LogManager.getLogger(javaClass)!!

    class Builder: AbstractBuilderTemplate<TableFormatter> {

        constructor(formatter: TableFormatter): super(TableFormatter()) {
            buildable.run {
                header = formatter.header
                headerVerticalDividerToken = formatter.headerVerticalDividerToken
                headerHorizontalDividerToken = formatter.headerHorizontalDividerToken
                bodyContents.addAll(formatter.bodyContents)
                footerVerticalDividerToken = formatter.footerVerticalDividerToken
                footerHorizontalDividerToken = formatter.headerHorizontalDividerToken
                footer = formatter.footer
            }
        }

        constructor(): super(TableFormatter())

        constructor(vararg contents: RowFormatter): this(contents.toList())

        constructor(contents: List<RowFormatter>): this() {
            buildable.bodyContents.addAll(contents)
        }

        companion object {
            @JvmStatic
            fun of(original: TableFormatter): Builder = Builder(original)

            @JvmStatic
            fun of(): Builder = Builder()

            @JvmStatic
            fun of(vararg contents: RowFormatter): Builder = Builder(contents.toList())

            @JvmStatic
            fun of(contents: List<RowFormatter>): Builder = Builder(contents)
        }

        /**
         * Resets the contents and dimensions of the [table][TableFormatter] but retains formatting options.
         * @return This [Builder]
         */
        fun resetContents(): Builder {
            checkFinalizedStatus()
            buildable.run {
                lines.clear()
                bodyContents.clear()
                columnWidths.clear()
                width = 0
                height = 0
            }

            return this
        }

        /**
         * @param contents The [array of rows][RowFormatter] to populate the rows of the table. This will append to any prior rows already specified.
         *
         * All rows must have the same number of columns as any other row in the table.
         * @return This [Builder]
         */
        fun body(vararg contents: RowFormatter): Builder {
            return body(contents.toList())
        }

        /**
         * @param contents The [list of rows][RowFormatter] to populate the rows of the table. This will append to any prior rows already specified.
         *
         * All rows must have the same number of columns as any other row in the table.
         * @return This [Builder]
         */
        fun body(contents: List<RowFormatter>): Builder {
            checkFinalizedStatus()
            buildable.bodyContents.addAll(contents)

            return this
        }

        /**
         * @param row The [header row][RowFormatter] for the table.
         *
         * Must have the same number of columns as any other row in the table.
         * @return This [Builder]
         */
        fun header(row: RowFormatter): Builder {
            checkFinalizedStatus()
            buildable.header = row

            return this
        }

        /**
         * @param verticalToken The [vertical token][Char] for the header divider.
         * @param horizontalToken The [horizontal token][Char] for the header divider.
         *
         * @return This [Builder]
         */
        fun headerDivider(verticalToken: Char, horizontalToken: Char): Builder {
            checkFinalizedStatus()
            buildable.run {
                headerVerticalDividerToken = verticalToken
                headerHorizontalDividerToken = horizontalToken
            }

            return this
        }

        /**
         * @param row The [footer row][RowFormatter] for the table.
         *
         * Must have the same number of columns as any other row in the table.
         * @return This [Builder]
         */
        fun footer(row: RowFormatter): Builder {
            checkFinalizedStatus()
            buildable.footer = row

            return this
        }

        /**
         * @param verticalToken The [vertical token][Char] for the footer divider.
         * @param horizontalToken The [horizontal token][Char] for the footer divider.
         *
         * @return This [Builder]
         */
        fun footerDivider(verticalToken: Char, horizontalToken: Char): Builder {
            checkFinalizedStatus()
            buildable.run {
                footerVerticalDividerToken = verticalToken
                footerHorizontalDividerToken = horizontalToken
            }

            return this
        }

        /**
         * @return The total number of lines in the table. This includes any header or footer rows.
         */
        fun getTotalHeight(): Int = buildable.run {
            bodyContents.sumBy { it.getHeight() } + (header?.getHeight()
                                                     ?: 0) + (headerHorizontalDividerToken?.run { 1 }
                                                              ?: 0) + (footer?.getHeight()
                                                                       ?: 0) + (footerHorizontalDividerToken?.run { 1 }
                                                                                ?: 0)
        }

        /**
         * @return The maximum character width of the table.
         */
        fun getTotalWidth(): Int = buildable.run {
            maxOf(bodyContents.maxBy { it.getWidth() }?.getWidth() ?: 0, header?.getWidth()
                                                                         ?: 0, footer?.getWidth()
                                                                               ?: 0) ?: 0
        }

        override fun validate(): AbstractBuilderTemplate<TableFormatter> {
            buildable.run {
                val columnCount =
                    if(bodyContents.isNotEmpty()) bodyContents.first().getTotalColumns() else header?.getTotalColumns()
                                                                                              ?: footer?.getTotalColumns()
                                                                                              ?: 0

                bodyContents.forEachIndexed { index, rowFormatter ->
                    require(rowFormatter.getTotalColumns() == columnCount) {
                        "Unexpected total body contents columns for < row=$index >: ${rowFormatter.getTotalColumns()}"
                    }
                }
                require((header == null) || (header?.getTotalColumns() == columnCount)) {
                    "Unexpected total header columns: ${header?.getTotalColumns()}"
                }
                require((footer == null) || (footer?.getTotalColumns() == columnCount)) {
                    "Unexpected total footer columns: ${footer?.getTotalColumns()}"
                }
            }

            return this
        }

        override fun build(): TableFormatter = buildable.run {
            val initialColumnWidths =
                if(bodyContents.isNotEmpty()) bodyContents.first().getColumnWidths() else header?.getColumnWidths()
                                                                                          ?: footer?.getColumnWidths()
                                                                                          ?: listOf()
            columnWidths.addAll(initialColumnWidths.mapIndexed { index, colWidth ->
                maxOf(colWidth, bodyContents.maxBy { it.getColumnWidths()[index] }?.getColumnWidths()?.get(index)
                                ?: 0, header?.getColumnWidths()?.get(index)
                                      ?: 0, footer?.getColumnWidths()?.get(index) ?: 0) ?: 0
            })
            logger.atInfo().log("Table Column Widths: $columnWidths")

            lines.addAll(header?.let {
                RowFormatter.Builder(it)
                    .columnWidths(columnWidths)
                    .finish()
                    .getLines()
            } ?: listOf())
            logger.atInfo().log("Has Table Header: ${header != null}")

            lines.addAll(if(headerHorizontalDividerToken != null) DivFormatter.Builder(columnWidths).horizontalDivider(headerHorizontalDividerToken as Char).verticalDivider(headerVerticalDividerToken as Char).finish().getLines() else listOf())
            logger.atInfo().log("Has Table Header Divider: ${headerHorizontalDividerToken != null}")

            lines.addAll(bodyContents.flatMap {
                RowFormatter.Builder(it)
                    .columnWidths(columnWidths)
                    .finish()
                    .getLines()
            })
            logger.atInfo().log("Table Row Count: ${bodyContents.size}")

            lines.addAll(if(footerHorizontalDividerToken != null) DivFormatter.Builder(columnWidths).horizontalDivider(footerHorizontalDividerToken as Char).verticalDivider(footerVerticalDividerToken as Char).finish().getLines() else listOf())
            logger.atInfo().log("Has Table Footer Divider: ${footerHorizontalDividerToken != null}")

            lines.addAll(footer?.let {
                RowFormatter.Builder(it)
                    .columnWidths(columnWidths)
                    .finish()
                    .getLines()
            } ?: listOf())
            logger.atInfo().log("Has Table Footer: ${footer != null}")

            width = lines.maxWidth()
            height = lines.size
            logger.atInfo().log("Table Dimensions: ($width, $height)")

            this
        }
    }

    private var header: RowFormatter? = null
    private var headerVerticalDividerToken: Char? = null
    private var headerHorizontalDividerToken: Char? = null
    private val bodyContents = mutableListOf<RowFormatter>()
    private var footerVerticalDividerToken: Char? = null
    private var footerHorizontalDividerToken: Char? = null
    private var footer: RowFormatter? = null
    private val columnWidths = mutableListOf<Int>()

    private var height = 0
    private var width = 0
    private val lines = mutableListOf<String>()

    /**
     * @return The total number of columns specified in the table.
     */
    fun getTotalColumns() = columnWidths.size

    /**
     * @return The [list of column widths][List] for each column in the table.
     */
    fun getColumnWidths() = columnWidths.toList()

    override fun getHeight() = height

    override fun getWidth() = width

    override fun getLines() = lines.toList()
}
