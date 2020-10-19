package seedu.address.storage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonSerializableSerenityTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableSerenityTest");
    private static final Path TYPICAL_GROUPS_FILE = TEST_DATA_FOLDER.resolve("typicalGroupsSerenity.json");
    private static final Path INVALID_GROUP_FILE = TEST_DATA_FOLDER.resolve("invalidGroupSerenity.json");
    private static final Path DUPLICATE_GROUP_FILE = TEST_DATA_FOLDER.resolve("duplicateGroupSerenity.json");

}
