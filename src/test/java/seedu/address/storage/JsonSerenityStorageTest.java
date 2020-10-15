package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalGroups.GROUP_A;
import static seedu.address.testutil.TypicalGroups.GROUP_B;
import static seedu.address.testutil.TypicalGroups.GROUP_C;
import static seedu.address.testutil.TypicalGroups.getTypicalSerenity;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataConversionException;
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
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readSerenity("notJsonFormatSerenity.json"));
    }

    @Test
    public void readSerenity_invalidGroupSerenity_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readSerenity("invalidGroupSerenity.json"));
    }

    @Test
    public void readSerenity_invalidAndValidGroupSerenity_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readSerenity("invalidAndValidGroupSerenity.json"));
    }

    @Test
    public void readAndSaveSerenity_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempSerenity.json");
        Serenity original = getTypicalSerenity();
        JsonSerenityStorage jsonSerenityStorage = new JsonSerenityStorage(filePath);

        // Save in new file and read back
        jsonSerenityStorage.saveSerenity(original, filePath);
        ReadOnlySerenity readBack = jsonSerenityStorage.readSerenity(filePath).get();
        assertEquals(original, new Serenity(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addGroup(GROUP_A);
        original.removeGroup(GROUP_C);
        jsonSerenityStorage.saveSerenity(original, filePath);
        readBack = jsonSerenityStorage.readSerenity(filePath).get();
        assertEquals(original, new Serenity(readBack));

        // Save and read without specifying file path
        original.addGroup(GROUP_B);
        jsonSerenityStorage.saveSerenity(original); // file path not specified
        readBack = jsonSerenityStorage.readSerenity().get(); // file path not specified
        assertEquals(original, new Serenity(readBack));

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
