package eli.inevitable;

import com.google.common.collect.Lists;
import com.thedeanda.lorem.LoremIpsum;
import eli.inevitable.enumerations.Align;
import eli.inevitable.formatters.CellFormatter;
import kotlin.text.Charsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JavaCellFormatterAPITest {
    private LoremIpsum faker = LoremIpsum.getInstance();

    private String line1 = faker.getWords(5);
    private String line2 = faker.getWords(2);
    private String line3 = faker.getWords(3);

    @Test
    public void testVarargBuilderOutput() {
        CellFormatter.Builder.of(line1, line2, line3)
                .alignment(Align.CENTER)
                .padding(1, 1)
                .paddingSpacer('*')
                .finish()
                .println(System.out, Charsets.UTF_8);
    }

    @Test
    public void testListBuilderOutput() {
        CellFormatter.Builder.of(Lists.newArrayList(line1, line2, line3))
                .alignment(Align.CENTER)
                .padding(1, 1)
                .paddingSpacer('*')
                .finish()
                .println(System.out, Charsets.UTF_8);
    }

    @Test
    public void testEmptyBuilderOutput() {
        CellFormatter.Builder.of()
                .lines(line1, line2, line3)
                .alignment(Align.CENTER)
                .padding(1, 1)
                .paddingSpacer('*')
                .finish()
                .println(System.out, Charsets.UTF_8);
    }
}
