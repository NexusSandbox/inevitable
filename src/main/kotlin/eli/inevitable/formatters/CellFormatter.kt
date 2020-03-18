package eli.inevitable.formatters

import eli.inevitable.enumerations.Align
import eli.inevitable.formatters.TextFormatter.Companion.LINE_FORMAT
import eli.inevitable.formatters.TextFormatter.Companion.logger
import eli.inevitable.isPrintable
import eli.inevitable.maxWidth
import eli.inevitable.sanitize
import eli.inevitable.templates.AbstractBuilderTemplate

/**
 * A generalized formatter that constructs a block of formatted text. This block may optionally be padded with an arbitrary number of empty rows of text.
 */
class CellFormatter private constructor(): TextFormatter {

    class Builder: AbstractBuilderTemplate<CellFormatter> {

        constructor(formatter: CellFormatter): super(CellFormatter()) {
            buildable.run {
                rawLines.addAll(formatter.rawLines)
                alignment = formatter.alignment
                fillingSpacerToken = formatter.fillingSpacerToken
                paddingSpacerToken = formatter.paddingSpacerToken
                verticalPaddingCount = formatter.verticalPaddingCount
                horizontalPaddingCount = formatter.horizontalPaddingCount
                height = formatter.height
                width = formatter.width
            }
        }

        constructor(): super(CellFormatter())

        constructor(vararg contents: String): this(contents.toList())

        constructor(contents: List<String>): this() {
            lines(contents)
        }

        companion object {
            @JvmStatic
            fun of(original: CellFormatter): Builder = Builder(original)

            @JvmStatic
            fun of(): Builder = Builder()

            @JvmStatic
            fun of(vararg contents: String): Builder = Builder(contents.toList())

            @JvmStatic
            fun of(contents: List<String>): Builder = Builder(contents)
        }

        /**
         * Resets the contents and dimensions of the [cell][CellFormatter] but retains formatting options.
         * @return This [Builder]
         */
        fun resetContents(): Builder {
            checkFinalizedStatus()
            buildable.run {
                textLines.clear()
                rawLines.clear()
                width = 0
                height = 0
            }

            return this
        }

        /**
         * @param lines The [array of text lines][String] to populate the rows of the cell. This will append to any prior lines already specified.
         * @return This [Builder]
         */
        fun lines(vararg lines: String): Builder {
            return lines(lines.toList())
        }

        /**
         * @param lines The [list of text lines][String] to populate the rows of the cell. This will append to any prior lines already specified.
         * @return This [Builder]
         */
        fun lines(lines: List<String>): Builder {
            checkFinalizedStatus()
            buildable.run {
                rawLines.addAll(lines.flatMap { it.sanitize() })
                width = maxOf(width, rawLines.maxWidth() + 2 * horizontalPaddingCount)
                height = maxOf(height, rawLines.size + 2 * verticalPaddingCount)
            }

            return this
        }

        /**
         * @param alignment Configures how to align the text within the cell.
         *
         * Default: [Align.LEFT]
         * @return This [Builder]
         */
        fun alignment(alignment: Align): Builder {
            checkFinalizedStatus()
            buildable.alignment = alignment

            return this
        }

        /**
         * @param token The displayable [token][Char] used to fill the spacing between the raw-text and the cell padding.
         *
         * Default: ' '.
         * @return This [Builder]
         */
        fun fillingSpacer(token: Char): Builder {
            checkFinalizedStatus()
            buildable.fillingSpacerToken = token

            return this
        }

        /**
         * @param token The displayable [token][Char] used to pad the spacing between the block text and the cell borders. Default: ' '.
         * @return This [Builder]
         */
        fun paddingSpacer(token: Char): Builder {
            checkFinalizedStatus()
            buildable.paddingSpacerToken = token

            return this
        }

        /**
         * @param verticalPadding The number of empty rows to space between horizontal cell borders.
         * @param horizontalPadding The number of empty columns to space between vertical cell borders.
         * @return This [Builder]
         */
        fun padding(verticalPadding: Int, horizontalPadding: Int): Builder {
            checkFinalizedStatus()
            buildable.run {
                verticalPaddingCount = verticalPadding
                horizontalPaddingCount = horizontalPadding
            }

            return this
        }

        /**
         * @param lineCount The positive total number of lines for the cell. If not set, will size to best fit the contents of the cell. Cannot be set to a value smaller than the contents of the cell including padding.
         * @return This [Builder]
         */
        fun height(lineCount: Int): Builder {
            checkFinalizedStatus()
            buildable.run {
                height = maxOf(lineCount, rawLines.size + 2 * verticalPaddingCount)
            }

            return this
        }

        /**
         * @param maxLineWidth The positive total character width of the cell. If not set, will size to best fit the contents of the cell. Cannot be set to a value smaller than the contents of the cell including padding.
         * @return This [Builder]
         */
        fun width(maxLineWidth: Int): Builder {
            checkFinalizedStatus()
            buildable.run {
                width = maxOf(maxLineWidth, rawLines.maxWidth() + 2 * horizontalPaddingCount)
            }

            return this
        }

        /**
         * @return The total number of lines in the cell. This includes any specified vertical padding.
         */
        fun getTotalHeight(): Int = buildable.run {
            maxOf(height, getContentHeight() + 2 * verticalPaddingCount)
        }

        /**
         * @return The number of lines of the content in the cell. This does not include any specified vertical padding.
         */
        fun getContentHeight(): Int = buildable.rawLines.size

        /**
         * @return The maximum character width of the cell. This includes any specified horizontal padding.
         */
        fun getTotalWidth(): Int = buildable.run {
            maxOf(width, getContentWidth() + 2 * horizontalPaddingCount)
        }

        /**
         * @return The maximum character width of the content in the cell. This does not include any specified horizontal padding.
         */
        fun getContentWidth(): Int = buildable.rawLines.maxWidth()

        override fun validate(): Builder {
            buildable.run {
                // Verify spacer tokens are valid
                require(isPrintable(fillingSpacerToken)) {
                    "Filling spacer token is not a printable character: 0x${Integer.toHexString(fillingSpacerToken.toInt()).toUpperCase()}"
                }
                require(isPrintable(paddingSpacerToken)) {
                    "Padding spacer token is not a printable character: 0x${Integer.toHexString(paddingSpacerToken.toInt()).toUpperCase()}"
                }
            }

            return this
        }

        override fun build(): CellFormatter = buildable.apply {
            height = getTotalHeight()
            width = getTotalWidth()
            logger.atInfo().log("Cell dimensions: ($width, $height)")

            // Create padding for vertical pad lines
            val linePadding =
                paddingSpacerToken.toString()
                    .repeat(width)

            // Create horizontal padding
            val horizontalPaddingText =
                paddingSpacerToken.toString()
                    .repeat(horizontalPaddingCount)

            // Create vertical padding
            repeat(verticalPaddingCount) { textLines.add(linePadding) }
            logger.atInfo().log("Cell Padding Count: ($horizontalPaddingCount, $verticalPaddingCount)")

            // Align content with filler
            textLines.addAll(alignment.paddify(rawLines, getTotalWidth() - 2 * buildable.horizontalPaddingCount, fillingSpacerToken).map {
                String.format(LINE_FORMAT, horizontalPaddingText, it, horizontalPaddingText)
            }.also {
                logger.atInfo().log("Cell Content: $it")
            })

            // Fill in any vertical spacing between content and padding
            val lineFillerCount = getTotalHeight() - getContentHeight() - 2 * verticalPaddingCount
            val lineFiller =
                String.format(LINE_FORMAT, horizontalPaddingText, fillingSpacerToken.toString().repeat(getTotalWidth() - 2 * horizontalPaddingCount), horizontalPaddingText)
            repeat(lineFillerCount) { textLines.add(lineFiller) }
            logger.atInfo().log("Cell Filler Count: $lineFillerCount")

            // Create padding for vertical pad lines
            repeat(verticalPaddingCount) { textLines.add(linePadding) }
        }
    }

    private val rawLines = mutableListOf<String>()

    private var alignment = Align.LEFT
    private var fillingSpacerToken = ' '
    private var paddingSpacerToken = ' '

    private var verticalPaddingCount = 0
    private var horizontalPaddingCount = 0

    private var height = 0
    private var width = 0

    private val textLines = mutableListOf<String>()

    /**
     * @return The character [alignment][Align] for the cell's text.
     */
    fun getAlignment() = alignment

    override fun getHeight() = height

    override fun getWidth() = width

    override fun getLines() = textLines.toList()
}
