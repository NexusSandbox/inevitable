package eli.inevitable.formatters

import eli.inevitable.enumerations.Align
import eli.inevitable.formatters.TextFormatter.Companion.LINE_FORMAT
import eli.inevitable.formatters.TextFormatter.Companion.logger
import eli.inevitable.maxOf
import eli.inevitable.templates.AbstractBuilderTemplate

/**
 * A generalized formatter that constructs a block of formatted text. This block may be filled with an arbitrary number of [contents][TextFormatter], may be padded and/or framed with a border, and given a [title][CellFormatter] and/or a [caption][CellFormatter].
 */
class BlockFormatter private constructor(): TextFormatter {

    class Builder: AbstractBuilderTemplate<BlockFormatter> {

        constructor(formatter: BlockFormatter): super(BlockFormatter()) {
            buildable.run {
                title = formatter.title
                block.addAll(formatter.block)
                caption = formatter.caption
                width = formatter.width
                height = formatter.height
            }
        }

        constructor(): super(BlockFormatter())

        constructor(vararg contents: TextFormatter): this(contents.toList())

        constructor(contents: List<TextFormatter>): this() {
            buildable.block.addAll(contents)
        }

        companion object {
            @JvmStatic
            fun of(original: BlockFormatter): Builder = Builder(original)

            @JvmStatic
            fun of(): Builder = Builder()

            @JvmStatic
            fun of(vararg contents: TextFormatter): Builder = Builder(contents.toList())

            @JvmStatic
            fun of(contents: List<TextFormatter>): Builder = Builder(contents)
        }

        /**
         * Resets the contents and dimensions of the [block][BlockFormatter] but retains formatting options.
         * @return This [Builder]
         */
        fun resetContents(): Builder {
            checkFinalizedStatus()
            buildable.run {
                block.clear()
                lines.clear()
                width = 0
                height = 0
            }

            return this
        }

        /**
         * @param contents The [array of content elements][TextFormatter] to populate the body of the block. This will append to any prior content already specified.
         * @return This [Builder]
         */
        fun contents(vararg contents: TextFormatter): Builder {
            return contents(contents.toList())
        }

        /**
         * @param contents The [list of content elements][TextFormatter] to populate the body of the block. This will append to any prior content already specified.
         * @return This [Builder]
         */
        fun contents(contents: List<TextFormatter>): Builder {
            checkFinalizedStatus()
            buildable.block.addAll(contents)

            return this
        }

        /**
         * @param title The [block of text][CellFormatter] to span above the block body.
         *
         * Default: None
         * @return This [Builder]
         */
        fun title(title: CellFormatter): Builder {
            checkFinalizedStatus()
            buildable.title = title

            return this
        }

        /**
         * @param caption The [block of text][CellFormatter] to span below the block body.
         *
         * Default: None
         * @return This [Builder]
         */
        fun caption(caption: CellFormatter): Builder {
            checkFinalizedStatus()
            buildable.caption = caption

            return this
        }

        /**
         * @param alignment The [content][TextFormatter] [alignment][Align] within the body.
         *
         * Default: [Align.CENTER]
         * @return This [Builder]
         */
        fun alignment(alignment: Align): Builder {
            checkFinalizedStatus()
            buildable.alignment = alignment

            return this
        }

        /**
         * @param token The west-east [border character][Char] used to surround the body content.
         *
         * Default: '*'
         * @return This [Builder]
         */
        fun verticalBorder(token: Char): Builder {
            checkFinalizedStatus()
            buildable.verticalBorderToken = token

            return this
        }

        /**
         * @param token The north-south [border character][Char] used to surround the body content.
         *
         * Default: '*'
         * @return This [Builder]
         */
        fun horizontalBorder(token: Char): Builder {
            checkFinalizedStatus()
            buildable.horizontalBorderToken = token

            return this
        }

        /**
         * @param verticalPadding The number of empty rows to space between the horizontal border and the body content.
         *
         * Default: 0
         * @param horizontalPadding The number of empty columns to space between the vertical border and the body content.
         *
         * Default: 0
         * @return This [Builder]
         */
        fun padding(verticalPadding: Int, horizontalPadding: Int): Builder {
            checkFinalizedStatus()
            with(buildable) {
                verticalPaddingCount = verticalPadding
                horizontalPaddingCount = horizontalPadding
            }

            return this
        }

        /**
         * @param token The displayable [token][Char] used to pad the spacing between the body content and the borders.
         *
         * Default: ' '
         * @return This [Builder]
         */
        fun paddingSpacer(token: Char): Builder {
            checkFinalizedStatus()
            buildable.paddingSpacerToken = token

            return this
        }

        /**
         * @param token The displayable [token][Char] used to fill the spacing between the body content and the cell padding.
         *
         * Default: ' '
         * @return This [Builder]
         */
        fun fillingSpacer(token: Char): Builder {
            checkFinalizedStatus()
            buildable.fillingSpacerToken = token

            return this
        }

        /**
         * @param lineCount The positive total number of lines for the block body. If not set it will size to best fit the body content. Cannot be set to a value smaller than the contents of the block body.
         * @return This [Builder]
         */
        fun contentHeight(lineCount: Int): Builder {
            checkFinalizedStatus()
            buildable.run {
                contentHeight = kotlin.comparisons.maxOf(lineCount, block.sumBy { it.getHeight() })
            }

            return this
        }

        /**
         * @param maxLineWidth The maximum character width for the block body. If not set it will size to best fit the body content. Cannot be set to a value smaller than the contents of the block body.
         * @return This [Builder]
         */
        fun contentWidth(maxLineWidth: Int): Builder {
            checkFinalizedStatus()
            buildable.run {
                contentWidth =
                    maxOf(maxLineWidth, block.maxBy { it.getWidth() }?.getWidth() ?: 0) ?: 0
            }

            return this
        }

        override fun validate(): Builder {
            buildable.run {
                require(block.isNotEmpty()) {
                    "Unexpected empty body content size."
                }
                require(block.all { it.getWidth() == 0 }.not()) {
                    "Unexpected 0 body content width."
                }
            }

            return this
        }

        override fun build(): BlockFormatter = buildable.run {
            val maxWidthBody =
                maxOf(contentWidth, block.maxBy { it.getWidth() }?.getWidth() ?: 0) ?: 0
            width =
                maxOf(title?.getWidth() ?: 0, caption?.getWidth()
                                              ?: 0, maxWidthBody + 2 * (1 + horizontalPaddingCount))
                ?: 0
            height =
                maxOf(contentHeight, 2 * (1 + verticalPaddingCount) + block.sumBy { it.getHeight() } + (title?.getHeight()
                                                                                                        ?: 0) + (caption?.getHeight()
                                                                                                                 ?: 0))
                ?: 0
            logger.atInfo().log("Block Content Dimensions: ($width, $height)")

            val horizontalBorderLine =
                horizontalBorderToken.toString()
                    .repeat(width)
            val verticalPaddingLine =
                String.format(LINE_FORMAT, verticalBorderToken, paddingSpacerToken.toString().repeat(width - 2), verticalBorderToken)
            val horizontalPaddingText =
                paddingSpacerToken.toString()
                    .repeat(horizontalPaddingCount)

            lines.addAll(title?.let {
                CellFormatter.Builder(it)
                    .width(width)
                    .finish()
                    .getLines()
            } ?: listOf())
            logger.atInfo().log("Has Block Title: ${title != null}")

            lines.add(horizontalBorderLine)
            repeat(verticalPaddingCount) { lines.add(verticalPaddingLine) }
            logger.atInfo().log("Block Padding Count: ($horizontalPaddingCount, $verticalPaddingCount)")

            val bodyWidth = width - 2 * (1 + horizontalPaddingCount)
            val body = block.flatMap {
                when(it) {
                    is CellFormatter -> CellFormatter.Builder(it).width(bodyWidth).finish().getLines()
                    is DivFormatter  -> DivFormatter.Builder(it).columnWidths(bodyWidth).finish().getLines()
                    else             -> it.getLines()
                }
            }
                .map { alignment.paddify(it, bodyWidth, fillingSpacerToken) }
                .map { String.format(LINE_FORMAT, verticalBorderToken + horizontalPaddingText, it, horizontalPaddingText + verticalBorderToken) }
            lines.addAll(body)
            logger.atInfo().log("Block Content Element Count: ${block.size}")

            val fillerHeight = contentHeight - body.size
            if(fillerHeight > 0) {
                val verticalFillerLine =
                    String.format(LINE_FORMAT, verticalBorderToken + horizontalPaddingText, fillingSpacerToken.toString().repeat(bodyWidth), horizontalPaddingText, verticalBorderToken)
                repeat(fillerHeight) { lines.add(verticalFillerLine) }
                logger.atInfo().log("Added filler lines to body: $fillerHeight")
            }

            repeat(verticalPaddingCount) { lines.add(verticalPaddingLine) }
            lines.add(horizontalBorderLine)
            lines.addAll(caption?.let {
                CellFormatter.Builder(it)
                    .width(width)
                    .finish()
                    .getLines()
            } ?: listOf())
            logger.atInfo().log("Has Block Caption: ${caption != null}")

            this
        }
    }

    private var title: CellFormatter? = null
    private val block = mutableListOf<TextFormatter>()
    private var caption: CellFormatter? = null

    private var alignment = Align.CENTER
    private var verticalPaddingCount = 0
    private var horizontalPaddingCount = 0
    private var verticalBorderToken = '*'
    private var horizontalBorderToken = '*'
    private var fillingSpacerToken = ' '
    private var paddingSpacerToken = ' '

    private var contentHeight = 0
    private var height = 0
    private var contentWidth = 0
    private var width = 0
    private val lines = mutableListOf<String>()

    override fun getHeight() = height

    override fun getWidth() = width

    override fun getLines() = lines.toList()
}
