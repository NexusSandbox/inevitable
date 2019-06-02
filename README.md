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

### `CellFormatter`: Multi-line Uniform Text

#### Kotlin API

<details>
    <summary>Let the pre-constructed line values be:</summary>

```kotlin
val line1 = faker.getWords(5)
val line2 = faker.getWords(2)
val line3 = faker.getWords(3)
```

</details>

<details>
    <summary>Then, these values can be used in the following ways:</summary>

- Initialize with varargs
```kotlin
textCell(line1, line2, line3) {
    alignment(CENTER)
    padding(1, 1)
    paddingSpacer('*')
}.println()
```
- Initialize with a pre-constructed list
```kotlin
textCell(listOf(line1, line2, line3)) {
    alignment(CENTER)
    padding(1, 1)
    paddingSpacer('*')
}.println()
```
- Initialize dynamically
```kotlin
textCell {
    lines(line1, line2, line3)
    alignment(CENTER)
    padding(1, 1)
    paddingSpacer('*')
}.println()
```
- Initialize dynamically by appending line-by-line
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

</details>

#### Java API

<details>
    <summary>Let the pre-constructed line values be:</summary>

```java
private String line1 = faker.getWords(5);
private String line2 = faker.getWords(2);
private String line3 = faker.getWords(3);
```

</details>

<details>
    <summary>Then, these values can be used in the following ways:</summary>

- Initialize with varargs
```java
CellFormatter.Builder.of(line1, line2, line3)
        .alignment(Align.CENTER)
        .padding(1, 1)
        .paddingSpacer('*')
        .finish()
        .println(System.out, Charsets.UTF_8);
```
- Initialize with a pre-constructed list
```java
CellFormatter.Builder.of(Lists.newArrayList(line1, line2, line3))
        .alignment(Align.CENTER)
        .padding(1, 1)
        .paddingSpacer('*')
        .finish()
        .println(System.out, Charsets.UTF_8);
```
- Initialize dynamically
```java
CellFormatter.Builder.of()
        .lines(line1, line2, line3)
        .alignment(Align.CENTER)
        .padding(1, 1)
        .paddingSpacer('*')
        .finish()
        .println(System.out, Charsets.UTF_8);
```

</details>

#### Sample Output
```
****************************************
*vivendo eos scripserit prompta commodo*
*            noluisse idque            *
*         reque volumus tibique        *
****************************************
```

### `RowFormatter`: Columnated-Row Text

#### Kotlin API

<details>
    <summary>Let the pre-constructed cell values be:</summary>

```kotlin
val cell1 = textCell(faker.getWords(5),
                     faker.getWords(2))
val cell2 = textCell(faker.getWords(2))
val cell3 = textCell(faker.getWords(3),
                     faker.getWords(2),
                     faker.getWords(1))
```

</details>

<details>
    <summary>Then, these values can be used in the following ways:</summary>

- Initialize with varargs
```kotlin
textRow(cell1, cell2, cell3) {
    verticalDivider(':')
}.println()
```
- Initialize with a pre-constructed list
```kotlin
textRow(listOf(cell1, cell2, cell3)) {
    verticalDivider(':')
}.println()
```
- Initialize dynamically
```kotlin
textRow {
    cells(cell1, cell2, cell3)
    verticalDivider(':')
}.println()
```
- Initialize dynamically by appending line-by-line
```kotlin
textRow {
    cells(cell1)
    cells(cell2)
    cells(cell3)
    verticalDivider(':')
}.println()
```

</details>

#### Java API

<details>
    <summary>Let the pre-constructed cell values be:</summary>

```java
private CellFormatter cell1 = CellFormatter.Builder.of(
        faker.getWords(5),
        faker.getWords(2))
        .finish();
private CellFormatter cell2 = CellFormatter.Builder.of(
        faker.getWords(2))
        .finish();
private CellFormatter cell3 = CellFormatter.Builder.of(
        faker.getWords(3),
        faker.getWords(2),
        faker.getWords(1))
        .finish();
```

</details>

<details>
    <summary>Then, these values can be used in the following ways:</summary>

- Initialize with varargs
```java
RowFormatter.Builder.of(cell1, cell2, cell3)
        .verticalDivider(':')
        .finish()
        .println(System.out, Charsets.UTF_8);
```
- Initialize with a pre-constructed list
```java
RowFormatter.Builder.of(Lists.newArrayList(cell1, cell2, cell3))
        .verticalDivider(':')
        .finish()
        .println(System.out, Charsets.UTF_8);
```
- Initialize dynamically
```java
RowFormatter.Builder.of()
        .cells(cell1, cell2, cell3)
        .verticalDivider(':')
        .finish()
        .println(System.out, Charsets.UTF_8);
```
- Initialize dynamically by appending line-by-line
```java
RowFormatter.Builder.of()
        .cells(cell1)
        .cells(cell2)
        .cells(cell3)
        .verticalDivider(':')
        .finish()
        .println(System.out, Charsets.UTF_8);
```

</details>

#### Sample Output
```
porttitor eloquentiam fabellas sit mutat:laoreet justo:quem an ipsum
quem ultrices                           :             :veritus augue
                                        :             :salutatus    
```

### `TableFormatter`: Textual Table

#### Kotlin API

<details>
    <summary>Let the pre-constructed row values be:</summary>

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

</details>

<details>
    <summary>Then, these values can be used in the following ways:</summary>

- Initialize with varargs
```kotlin
textTable(row1, row2, row3) {
    header(header)
    headerDivider('|', '-')
}.println()
```
- Initialize with a pre-constructed list
```kotlin
textTable(listOf(row1, row2, row3)) {
    header(header)
    headerDivider('|', '-')
}.println()
```
- Initialize dynamically
```kotlin
textTable {
    body(row1, row2, row3)
    header(header)
    headerDivider('|', '-')
}.println()
```
- Initialize dynamically by appending line-by-line
```kotlin
textTable {
    body(row1)
    body(row2)
    body(row3)
    header(header)
    headerDivider('|', '-')
}.println()
```

</details>

#### Java API

<details>
    <summary>Let the pre-constructed row values be:</summary>

```java
private RowFormatter header =
        RowFormatter.Builder.of(
                CellFormatter.Builder.of(faker.getTitle(1)).finish(),
                CellFormatter.Builder.of(faker.getTitle(1)).finish(),
                CellFormatter.Builder.of(faker.getTitle(1)).finish())
                .finish();
private RowFormatter row1 =
        RowFormatter.Builder.of(
                CellFormatter.Builder.of(faker.getWords(5),
                        faker.getWords(2))
                        .finish(),
                CellFormatter.Builder.of(faker.getWords(2))
                        .finish(),
                CellFormatter.Builder.of(faker.getWords(3),
                        faker.getWords(2),
                        faker.getWords(1))
                        .finish())
                .finish();
private RowFormatter row2 =
        RowFormatter.Builder.of(
                CellFormatter.Builder.of(faker.getWords(5))
                        .finish(),
                CellFormatter.Builder.of(faker.getWords(2),
                        faker.getWords(2))
                        .finish(),
                CellFormatter.Builder.of(faker.getWords(3),
                        faker.getWords(2),
                        faker.getWords(1))
                        .finish())
                .finish();
private RowFormatter row3 =
        RowFormatter.Builder.of(
                CellFormatter.Builder.of(faker.getWords(5),
                        faker.getWords(2),
                        faker.getWords(1))
                        .finish(),
                CellFormatter.Builder.of(faker.getWords(2),
                        faker.getWords(2))
                        .finish(),
                CellFormatter.Builder.of(faker.getWords(3))
                        .finish())
                .finish();
```

</details>

<details>
    <summary>Then, these values can be used in the following ways:</summary>

- Initialize with varargs
```java
TableFormatter.Builder.of(row1, row2, row3)
        .header(header)
        .headerDivider('|', '=')
        .finish()
        .println(System.out, Charsets.UTF_8);
```
- Initialize with a pre-constructed list
```java
TableFormatter.Builder.of(Lists.newArrayList(row1, row2, row3))
        .header(header)
        .headerDivider('|', '=')
        .finish()
        .println(System.out, Charsets.UTF_8);
```
- Initialize dynamically
```java
TableFormatter.Builder.of()
        .body(row1, row2, row3)
        .header(header)
        .headerDivider('|', '=')
        .finish()
        .println(System.out, Charsets.UTF_8);
```
- Initialize dynamically by appending line-by-line
```java
TableFormatter.Builder.of()
        .body(row1)
        .body(row2)
        .body(row3)
        .header(header)
        .headerDivider('|', '=')
        .finish()
        .println(System.out, Charsets.UTF_8);
```

</details>

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

### `BlockFormatter`: Blocked Text

#### Kotlin API

<details>
    <summary>Let the pre-constructed element values be:</summary>

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

</details>

<details>
    <summary>Then, these values can be used in the following ways:</summary>

- Initialize with varargs
```kotlin
textBlock(tableContent) {
    title(title)
    caption(caption)
}.println()
```
- Initialize with a pre-constructed list
```kotlin
textBlock(listOf(tableContent)) {
    title(title)
    caption(caption)
}.println()
```
- Initialize dynamically
```kotlin
textBlock {
    contents(tableContent)
    title(title)
    caption(caption)
}.println()
```

</details>

#### Java API

<details>
    <summary>Let the pre-constructed element values be:</summary>

```java
private CellFormatter title = CellFormatter.Builder.of(faker.getTitle(2))
        .alignment(Align.CENTER)
        .finish();
private CellFormatter caption = CellFormatter.Builder.of(faker.getWords(3))
        .finish();
private TableFormatter tableContent =
        TableFormatter.Builder.of(
                RowFormatter.Builder.of(
                        CellFormatter.Builder.of(faker.getWords(1))
                                .finish(),
                        CellFormatter.Builder.of(faker.getWords(1))
                                .finish(),
                        CellFormatter.Builder.of(faker.getWords(1))
                                .finish())
                        .finish(),
                RowFormatter.Builder.of(
                        CellFormatter.Builder.of(faker.getWords(1))
                                .finish(),
                        CellFormatter.Builder.of(faker.getWords(1))
                                .finish(),
                        CellFormatter.Builder.of(faker.getWords(1))
                                .finish())
                        .finish())
                .header(RowFormatter.Builder.of(
                        CellFormatter.Builder.of(faker.getWords(1))
                                .finish(),
                        CellFormatter.Builder.of(faker.getWords(1))
                                .finish(),
                        CellFormatter.Builder.of(faker.getWords(1))
                                .finish())
                        .finish())
                .headerDivider('|', '=')
                .finish();
```

</details>

<details>
    <summary>Then, these values can be used in the following ways:</summary>

- Initialize with varargs
```java
        BlockFormatter.Builder.of(tableContent)
                .title(title)
                .caption(caption)
                .finish()
                .println(System.out, Charsets.UTF_8);
```
- Initialize with a pre-constructed list
```java
        BlockFormatter.Builder.of(Lists.newArrayList(tableContent))
                .title(title)
                .caption(caption)
                .finish()
                .println(System.out, Charsets.UTF_8);
```
- Initialize dynamically
```java
        BlockFormatter.Builder.of()
                .contents(tableContent)
                .title(title)
                .caption(caption)
                .finish()
                .println(System.out, Charsets.UTF_8);
```

</details>

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