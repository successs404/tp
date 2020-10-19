package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.ReadOnlySerenity;
import seedu.address.model.Serenity;

public class JsonSerenityStorageTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerenityStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readSerenity_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readSerenity(null));
    }

    private java.util.Optional<ReadOnlySerenity> readSerenity(String filePath) throws Exception {
        return new JsonSerenityStorage(Paths.get(filePath)).readSerenity(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
            ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
            : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readSerenity("NonExistentFile.json").isPresent());
    }

    @Test
    public void saveSerenity_nullSerenity_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveSerenity(null, "SomeFile.json"));
    }

    /**
     * Saves {@code serenity} at the specified {@code filePath}.
     */
    private void saveSerenity(ReadOnlySerenity serenity, String filePath) {
        try {
            new JsonSerenityStorage(Paths.get(filePath))
                .saveSerenity(serenity, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveSerenity_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveSerenity(new Serenity(), null));
    }
}
