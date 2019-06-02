package eli.inevitable;

import com.google.common.collect.Lists;
import com.thedeanda.lorem.LoremIpsum;
import eli.inevitable.formatters.CellFormatter;
import eli.inevitable.formatters.RowFormatter;
import kotlin.text.Charsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JavaRowFormatterAPITest {
    private LoremIpsum faker = LoremIpsum.getInstance();

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

    @Test
    public void testVarargBuilderOutput() {
        RowFormatter.Builder.of(cell1, cell2, cell3)
                .verticalDivider(':')
                .finish()
                .println(System.out, Charsets.UTF_8);
    }

    @Test
    public void testListBuilderOutput() {
        RowFormatter.Builder.of(Lists.newArrayList(cell1, cell2, cell3))
                .verticalDivider(':')
                .finish()
                .println(System.out, Charsets.UTF_8);
    }

    @Test
    public void testEmptyBuilderOutput() {
        RowFormatter.Builder.of()
                .cells(cell1, cell2, cell3)
                .verticalDivider(':')
                .finish()
                .println(System.out, Charsets.UTF_8);
    }

    @Test
    public void testAppendingBuilderOutput() {
        RowFormatter.Builder.of()
                .cells(cell1)
                .cells(cell2)
                .cells(cell3)
                .verticalDivider(':')
                .finish()
                .println(System.out, Charsets.UTF_8);
    }
}
