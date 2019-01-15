package core.framework.util;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

/**
 * @author neo
 */
public class IOUtilsTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void writeBytes() throws IOException {
        File tempFile = temporaryFolder.newFile("tempfile.txt");

        IOUtils.write(tempFile, new byte[10]);
        Assert.assertTrue(tempFile.exists());
        Assert.assertEquals(10, tempFile.length());
    }

    @Test
    public void readSingleLineText() {
        String input = "single line";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        String result = IOUtils.text(inputStream);

        assertEquals(input, result);
    }

    @Test
    public void readMultipleLinesText() {
        String input = "multiple\nline\ntext";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        String result = IOUtils.text(inputStream);

        assertEquals(input, result);
    }

    @Test
    public void throwExceptionIfFileNotExists() {
        exception.expect(RuntimeIOException.class);
        exception.expectMessage("java.io.FileNotFoundException");

        IOUtils.text(new File("not/exsit.file"));
    }

    @Test
    public void readAllFromReader() {
        String text = "text\ntext";
        StringReader reader = new StringReader(text);
        String readText = IOUtils.text(reader);

        Assert.assertEquals(text, readText);
    }
}
