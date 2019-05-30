> "I am inevitable!"
>           - A mean purple titan

# Overview

Constructor library for structured monospaced text.

Current functionality:

- Multi-line text of uniform character width, and arbitrary padding
- Multi-line columnated-row text with vertical dividers
- Textual table with horizontal dividers, header, and footer
- Blocked text with border, title, and caption

Each of these elements can be combined to create structured text that can be printed to a console, or used to display arbitrarily sized text data to the screen.

*Note*: The display font must render with a monospaced font for the text to align as intended.

## Kotlin & Java Compatibility

This library was constructed using Kotlin, and as such used several idiomatic Kotlin structures for the API. However, it was also designed to emulate a standard Java builder pattern structure for the API. So each construct can be called using either approach.

### Target Compatibility

|        | Version |
|--------|---------|
| JVM    | `1.9`   |
| Kotlin | `1.3.x` |

## Usage Examples

*Note*: These examples can be composed with each other in arbitrary ways to render the structured text.

### Multi-line Uniform Text

#### Kotlin API

Let the pre-constructed line values be:
```kotlin
val line1 = faker.getWords(5)
val line2 = faker.getWords(2)
val line3 = faker.getWords(3)
```

Then, these values can be used in the following ways:

```kotlin
textCell(line1, line2, line3) {
    alignment(CENTER)
    padding(1, 1)
    paddingSpacer('*')
}.println()
```

*OR*

```kotlin
textCell(listOf(line1, line2, line3)) {
    alignment(CENTER)
    padding(1, 1)
    paddingSpacer('*')
}.println()
```

*OR*

```kotlin
textCell {
    lines(line1, line2, line3)
    alignment(CENTER)
    padding(1, 1)
    paddingSpacer('*')
}.println()
```

*OR*

```kotlin
textCell {
    lines(line1)
    lines(line2)
    lines(line3)
    alignment(CENTER)
    padding(1, 1)
    paddingSpacer('*')
}.println()
```

#### Sample Output
```
****************************************
*vivendo eos scripserit prompta commodo*
*            noluisse idque            *
*         reque volumus tibique        *
****************************************
```

### Columnated-Row Text

#### Kotlin API

Let the pre-constructed cell values be:
```kotlin
val cell1 = textCell(faker.getWords(5),
                     faker.getWords(2))
val cell2 = textCell(faker.getWords(2))
val cell3 = textCell(faker.getWords(3),
                     faker.getWords(2),
                     faker.getWords(1))
```

Then, these values can be used in the following ways:

```kotlin
textRow(cell1, cell2, cell3) {
    verticalDivider(':')
}.println()
```

*OR*

```kotlin
textRow(listOf(cell1, cell2, cell3)) {
    verticalDivider(':')
}.println()
```

*OR*

```kotlin
textRow {
    cells(cell1, cell2, cell3)
    verticalDivider(':')
}.println()
```

*OR*

```kotlin
textRow {
    cells(cell1)
    cells(cell2)
    cells(cell3)
    verticalDivider(':')
}.println()
```

#### Sample Output
```
porttitor eloquentiam fabellas sit mutat:laoreet justo:quem an ipsum
quem ultrices                           :             :veritus augue
                                        :             :salutatus    
```

### Textual Table

#### Kotlin API

Let the pre-constructed row values be:
```kotlin
val header = textRow(textCell(faker.getTitle(1)),
                     textCell(faker.getTitle(1)),
                     textCell(faker.getTitle(1)))
val row1 = textRow(textCell(faker.getWords(5),
                            faker.getWords(2)),
                   textCell(faker.getWords(2)),
                   textCell(faker.getWords(3),
                            faker.getWords(2),
                            faker.getWords(1)))
val row2 = textRow(textCell(faker.getWords(5)),
                   textCell(faker.getWords(2),
                            faker.getWords(2)),
                   textCell(faker.getWords(3),
                            faker.getWords(2),
                            faker.getWords(1)))
val row3 = textRow(textCell(faker.getWords(5),
                            faker.getWords(2),
                            faker.getWords(1)),
                   textCell(faker.getWords(2),
                            faker.getWords(2)),
                   textCell(faker.getWords(3)))
```

Then, these values can be used in the following ways:

```kotlin
textTable(row1, row2, row3) {
    header(header)
    headerDivider('|', '-')
}.println()
```

*OR*

```kotlin
textTable(listOf(row1, row2, row3)) {
    header(header)
    headerDivider('|', '-')
}.println()
```

*OR*

```kotlin
textTable {
    body(row1, row2, row3)
    header(header)
    headerDivider('|', '-')
}.println()
```

*OR*

```kotlin
textTable {
    body(row1)
    body(row2)
    body(row3)
    header(header)
    headerDivider('|', '-')
}.println()
```

#### Sample Output

```
In                                        |Mauris             |Omittam                     
------------------------------------------|-------------------|----------------------------
percipit bibendum purus augue cum         |dolores sea        |graecis voluptaria salutatus
iisque phasellus                          |                   |inciderint vix              
                                          |                   |mea                         
antiopam porta omittantur mazim erroribus |option himenaeos   |consul nobis contentiones   
                                          |aptent oporteat    |convenire dicam             
                                          |                   |decore                      
mi referrentur conclusionemque hac dapibus|iisque deterruisset|verear doming commune       
duo tractatos                             |simul alienum      |                            
autem                                     |                   |                            
```

### Blocked Text

Let the pre-constructed row values be:
```kotlin
val title = textCell(faker.getTitle(2)) {
    alignment(Align.CENTER)
}
val caption = textCell(faker.getWords(3))
val tableContent =
    textTable(textRow(textCell(faker.getWords(1)),
                      textCell(faker.getWords(1)),
                      textCell(faker.getWords(1))),
              textRow(textCell(faker.getWords(1)),
                      textCell(faker.getWords(1)),
                      textCell(faker.getWords(1)))) {
        header(textRow(textCell(faker.getWords(1)),
                       textCell(faker.getWords(1)),
                       textCell(faker.getWords(1))))
        headerDivider('|', '=')
    }
```

#### Kotlin API

```kotlin
textBlock(tableContent) {
    title(title)
    caption(caption)
}.println()
```

*OR*

```kotlin
textBlock(listOf(tableContent)) {
    title(title)
    caption(caption)
}.println()
```

*OR*

```kotlin
textBlock {
    contents(tableContent)
    title(title)
    caption(caption)
}.println()
```

#### Sample Output

```
        Omittam Theophrastus         
*************************************
*fugit   |ceteros    |docendi       *
*========|===========|==============*
*quot    |scelerisque|arcu          *
*facilisi|esse       |necessitatibus*
*************************************
dicam ultrices reformidans           
```

## Issues

[GitHub: Issues](https://github.com/NexusSandbox/inevitable/issues)

- Please include as much of the following as is relevant:
  - Title -
    - A concise description of the issue
  - Summary - 
    - A detailed description of the issue
    - An accurate example of the workflow that triggers the issue
      - Include code and/or screenshots as necessary
    - The applicable versions of the code and environment you are using
      - Version of `inevitable` consumed?
      - Version of the JVM/Kotlin?
      - Version of MacOS or Windows?

## Contributions

I welcome any contributions and suggestions in hopes that this library become as useful and helpful as possible. However, I would request that you abide by the following for any changes:

- Create an issue for the project with a description of what you would like to see changed.
- Create a branch or fork so you can implement and later merge in your changes.
- Create any unit tests to exercise your changes as appropriate to verify the functionality to a reasonable confidence level.
  - The unit tests should be simple and straightforward.
- Ensure all tests pass prior to creating a pull request.
- Document any new publicly exposed classes/methods to describe their intended purpose.


*Note*: Since this project is still rather new and small, once the pull request is created, I will get to reviewing it as soon as I am able.
- Please address any review comments to the best of your ability.

## License

MIT License