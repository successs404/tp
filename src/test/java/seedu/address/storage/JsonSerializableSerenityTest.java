package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.Serenity;
import seedu.address.testutil.TypicalGroups;

public class JsonSerializableSerenityTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableSerenityTest");
    private static final Path TYPICAL_GROUPS_FILE = TEST_DATA_FOLDER.resolve("typicalGroupsSerenity.json");
    private static final Path INVALID_GROUP_FILE = TEST_DATA_FOLDER.resolve("invalidGroupSerenity.json");
    private static final Path DUPLICATE_GROUP_FILE = TEST_DATA_FOLDER.resolve("duplicateGroupSerenity.json");

    @Test
    public void toModelType_typicalGroupsFile_success() throws Exception {
        JsonSerializableSerenity dataFromFile = JsonUtil.readJsonFile(TYPICAL_GROUPS_FILE,
            JsonSerializableSerenity.class).get();
        Serenity serenityFromFile = dataFromFile.toModelType();
        Serenity typicalGroupsSerenity = TypicalGroups.getTypicalSerenity();
        assertEquals(serenityFromFile, typicalGroupsSerenity);
    }

    @Test
    public void toModelType_invalidGroupsFile_throwsIllegalValueException() throws Exception {
        JsonSerializableSerenity dataFromFile = JsonUtil.readJsonFile(INVALID_GROUP_FILE,
            JsonSerializableSerenity.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateGroups_throwsIllegalValueException() throws Exception {
        JsonSerializableSerenity dataFromFile = JsonUtil.readJsonFile(DUPLICATE_GROUP_FILE,
            JsonSerializableSerenity.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableSerenity.MESSAGE_DUPLICATE_GROUP,
            dataFromFile::toModelType);
    }

}
