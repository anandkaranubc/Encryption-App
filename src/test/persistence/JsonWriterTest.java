package persistence;

import model.Encryption;
import model.EncryptionList;
import model.Progress;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.EncryptionApp;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    Progress progress;

    @BeforeEach
    void runBefore() {
        EncryptionList.getDataNames().clear();
        EncryptionApp.setVariables(null, null);
        EncryptionList.getEncryptedCiphers().clear();
        progress = new Progress();
    }

    @Test
    void testWriterInvalidFile() {
        try {
            Progress progress = new Progress();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            EncryptionApp.setVariables("username", "password");
            Progress progress1 = new Progress();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWorkroom.json");
            writer.open();
            writer.write(progress1);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyWorkroom.json");
            progress1 = reader.read();
            assertEquals("username", progress1.getUsername());
            assertEquals("password", progress1.getPassword());
            assertEquals(0, progress1.numDataNames());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            EncryptionApp.setVariables("username", "password");

            Encryption encryption = Encryption.getInstance();
            encryption.passEncryption("safari", "Safari");
            encryption.passEncryption("mozilla", "Mozilla");

            Progress progress = new Progress();
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWorkroom.json");
            writer.open();
            writer.write(progress);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralWorkroom.json");
            progress = reader.read();
            assertEquals("username", progress.getUsername());
            assertEquals("password", progress.getPassword());
            List<String> dataNames = progress.getDataNames();
            assertEquals(2, dataNames.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}