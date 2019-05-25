package eli.inevitable.enumerations

enum class Align(private val padder: Function3<String, Int, Char, String>) {
    /**
     * Text originates from the LEFT;
     *
     * No whitespace padding is applied.
     */
    NONE({ line: String, margin: Int, token: Char -> line }),
    /**
     * Text originates from the LEFT;
     *
     * Any additional whitespace will be appended to the right of the right-most character until the line length is equal to the margin.
     */
    LEFT({ line: String, margin: Int, token: Char -> line.padEnd(margin, token) }),
    /**
     * Text originates from the CENTER;
     *
     * Any additional whitespace will be prepended/appended alternately to the left/right of the left-most/right-most characters respectively until the line length is equal to the margin.
     */
    CENTER({ line: String, margin: Int, token: Char ->
               val lineLengthMiddle = line.length / 2
               val marginLengthMiddle = margin / 2
               val adjuster = (margin % 2)
               line.substring(0, lineLengthMiddle).padStart(marginLengthMiddle, token) + line.substring(lineLengthMiddle, line.length).padEnd(marginLengthMiddle + adjuster, token)
           }),
    /**
     * Text originates from the RIGHT;
     *
     * Any additional whitespace will be prepended to the left of the left-most character until the line length is equal to the margin.
     */
    RIGHT({ line: String, margin: Int, token: Char -> line.padStart(margin, token) }),
    /**
     * Text originates from the LEFT;
     *
     * TODO: Any additional whitespace will be injected sequentially between each of the "words", starting from the left, until the line length is equal to the margin.
     *
     * If the original line length is less than 61.8% of the margin or contains less than 2 words, then the padding will function as [Align.LEFT]
     */
    JUSTIFY_LEFT({ line: String, margin: Int, token: Char -> line.padEnd(margin, token) }),
    /**
     * Text originates from the RIGHT;
     *
     * TODO: Any additional whitespace will be injected sequentially between each of the "words", starting from the right, until the line length is equal to the margin.
     *
     * If the original line length is less than 61.8% of the margin or contains less than 2 words, then the padding will function as [Align.RIGHT]
     */
    JUSTIFY_RIGHT({ line: String, margin: Int, token: Char -> line.padStart(margin, token) });

    /**
     * Fills in whitespace to expand the line of text to fill the defined margin.
     *
     * If the line of text already meets or exceeds the margin, then the line returned unchanged.
     *
     * The enumerated type will determine how the whitespace is applied to the text.
     * @param line A single [line of text][String] that does not include any non-printable, or return characters.
     * @param margin The total number of characters to expend the line to.
     * @return The original or an expanded [line of characters][String] of length at least equal to the margin.
     */
    fun paddify(line: String, margin: Int, fillerToken: Char = ' '): String {
        if(line.length >= margin) return line

        return padder.invoke(line, margin, fillerToken)
    }

    /**
     * Fills in whitespace to expand each line of text to fill the defined margin.
     *
     * If the line of text already meets or exceeds the margin, then the line returned unchanged.
     *
     * The enumerated type will determine how the whitespace is applied to the text.
     * @param lines A [List] of [lines of text][String] that does not include any non-printable, or return characters.
     * @param margin The total number of characters to expend the line to.
     * @return A [List] of [original or expanded lines of characters][String] of length at least equal to the margin.
     */
    fun paddify(lines: List<String>, margin: Int, fillerToken: Char = ' '): List<String> {
        return lines.map { paddify(it, margin, fillerToken) }
    }
}
