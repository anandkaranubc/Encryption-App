package persistence;

import model.EncryptionList;
import model.Progress;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Progress progress = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyProgress.json");
        try {
            Progress progress = reader.read();
            assertEquals("us", progress.getUsername());
            assertEquals("pa", progress.getPassword());
            assertEquals(0, progress.numDataNames());
            assertEquals(0, progress.numPasses());
        } catch (IOException e) {
            fail("Couldn't read from file");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralWorkRoom.json");
        try {
            Progress progress = reader.read();
            assertEquals("us", progress.getUsername());
            assertEquals("pa", progress.getPassword());
            List<String> dataNames = progress.getDataNames();
            assertEquals(2, dataNames.size());
            assertEquals(2, EncryptionList.getEncryptedCiphers().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}