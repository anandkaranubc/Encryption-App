package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

public class EncryptionExceptionTest {

    @Test
    public void testEncryptionInitialization() {
        // Replace the algorithm name with an invalid value to cause an exception
        try {
            Encryption.cipher = Cipher.getInstance("InvalidAlgorithm");
            Assertions.fail("Expected exception was not thrown");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            // The expected exception was thrown, so the test passed
        } catch (Exception e) {
            Assertions.fail("Unexpected exception occurred", e);
        }
    }
}
