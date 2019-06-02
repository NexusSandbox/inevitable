package eli.inevitable;

import com.google.common.collect.Lists;
import com.thedeanda.lorem.LoremIpsum;
import eli.inevitable.formatters.CellFormatter;
import eli.inevitable.formatters.RowFormatter;
import eli.inevitable.formatters.TableFormatter;
import kotlin.text.Charsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JavaTableFormatterAPITest {
    private LoremIpsum faker = LoremIpsum.getInstance();

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

    @Test
    public void testVarargBuilderOutput() {
        TableFormatter.Builder.of(row1, row2, row3)
                .header(header)
                .headerDivider('|', '=')
                .finish()
                .println(System.out, Charsets.UTF_8);
    }

    @Test
    public void testListBuilderOutput() {
        TableFormatter.Builder.of(Lists.newArrayList(row1, row2, row3))
                .header(header)
                .headerDivider('|', '=')
                .finish()
                .println(System.out, Charsets.UTF_8);
    }

    @Test
    public void testEmptyBuilderOutput() {
        TableFormatter.Builder.of()
                .body(row1, row2, row3)
                .header(header)
                .headerDivider('|', '=')
                .finish()
                .println(System.out, Charsets.UTF_8);
    }

    @Test
    public void testAppendingBuilderOutput() {
        TableFormatter.Builder.of()
                .body(row1)
                .body(row2)
                .body(row3)
                .header(header)
                .headerDivider('|', '=')
                .finish()
                .println(System.out, Charsets.UTF_8);
    }

}
