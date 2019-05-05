package eli.inevitable.enumerations

enum class Align(private val padder: Function2<String, Int, String>) {
    /**
     * Text originates from the LEFT;
     *
     * No whitespace padding is applied.
     */
    NONE({ line: String, margin: Int -> line }),
    /**
     * Text originates from the LEFT;
     *
     * Any additional whitespace will be appended to the right of the right-most character until the line length is equal to the margin.
     */
    LEFT({ line: String, margin: Int -> line.padEnd(margin) }),
    /**
     * Text originates from the CENTER;
     *
     * Any additional whitespace will be prepended/appended alternately to the left/right of the left-most/right-most characters respectively until the line length is equal to the margin.
     */
    CENTER({ line: String, margin: Int ->
               line.substring(
                   0, line.length / 2
                             ).padStart(margin / 2) + line.substring(
                   line.length / 2, line.length
                                                                    ).padEnd(margin / 2) + if((line.length - margin) % 2 == 1) " " else ""
           }),
    /**
     * Text originates from the RIGHT;
     *
     * Any additional whitespace will be prepended to the left of the left-most character until the line length is equal to the margin.
     */
    RIGHT({ line: String, margin: Int -> line.padStart(margin) }),
    /**
     * Text originates from the LEFT;
     *
     * TODO: Any additional whitespace will be injected sequentially between each of the "words", starting from the left, until the line length is equal to the margin.
     *
     * If the original line length is less than 61.8% of the margin or contains less than 2 words, then the padding will function as [Align.LEFT]
     */
    JUSTIFY_LEFT({ line: String, margin: Int -> line.padEnd(margin) }),
    /**
     * Text originates from the RIGHT;
     *
     * TODO: Any additional whitespace will be injected sequentially between each of the "words", starting from the right, until the line length is equal to the margin.
     *
     * If the original line length is less than 61.8% of the margin or contains less than 2 words, then the padding will function as [Align.RIGHT]
     */
    JUSTIFY_RIGHT({ line: String, margin: Int -> line.padStart(margin) });

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
    fun pad(line: String, margin: Int): String {
        if(line.length >= margin) return line

        return padder.invoke(line, margin)
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
    fun pad(lines: List<String>, margin: Int): List<String> {
        return lines.map { pad(it, margin) }
    }
}
