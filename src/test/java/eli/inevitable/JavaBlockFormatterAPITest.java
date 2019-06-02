package eli.inevitable;

import com.google.common.collect.Lists;
import com.thedeanda.lorem.LoremIpsum;
import eli.inevitable.enumerations.Align;
import eli.inevitable.formatters.BlockFormatter;
import eli.inevitable.formatters.CellFormatter;
import eli.inevitable.formatters.RowFormatter;
import eli.inevitable.formatters.TableFormatter;
import kotlin.text.Charsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JavaBlockFormatterAPITest {
    private LoremIpsum faker = LoremIpsum.getInstance();

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

    @Test
    public void testVarargBuilderOutput() {
        BlockFormatter.Builder.of(tableContent)
                .title(title)
                .caption(caption)
                .finish()
                .println(System.out, Charsets.UTF_8);
    }

    @Test
    public void testListBuilderOutput() {
        BlockFormatter.Builder.of(Lists.newArrayList(tableContent))
                .title(title)
                .caption(caption)
                .finish()
                .println(System.out, Charsets.UTF_8);
    }

    @Test
    public void testEmptyBuilderOutput() {
        BlockFormatter.Builder.of()
                .contents(tableContent)
                .title(title)
                .caption(caption)
                .finish()
                .println(System.out, Charsets.UTF_8);
    }
}
