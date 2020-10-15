package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedGroup.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalGroups.GROUP_A;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;

class JsonAdaptedGroupTest {

    private static final String VALID_NAME = GROUP_A.getName();
    private static final List<JsonAdaptedStudent> VALID_STUDENTS = GROUP_A.getStudents().asUnmodifiableObservableList()
        .stream()
        .map(JsonAdaptedStudent::new)
        .collect(Collectors.toList());
    private static final List<JsonAdaptedLesson> VALID_LESSONS = GROUP_A.getLessons().asUnmodifiableObservableList()
        .stream()
        .map(JsonAdaptedLesson::new)
        .collect(Collectors.toList());

    @Test
    public void toModelType_validGroupDetails_returnsGroup() throws Exception {
        JsonAdaptedGroup group = new JsonAdaptedGroup(GROUP_A);
        assertEquals(GROUP_A, group.toModelType());
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedGroup group = new JsonAdaptedGroup(null, VALID_STUDENTS, VALID_LESSONS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "name");
        assertThrows(IllegalValueException.class, expectedMessage, group::toModelType);
    }

    @Test
    public void toModelType_nullStudents_throwsIllegalValueException() {
        JsonAdaptedGroup group = new JsonAdaptedGroup(VALID_NAME, null, VALID_LESSONS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "students");
        assertThrows(IllegalValueException.class, expectedMessage, group::toModelType);
    }

    @Test
    public void toModelType_nullClasses_throwsIllegalValueException() {
        JsonAdaptedGroup group = new JsonAdaptedGroup(VALID_NAME, VALID_STUDENTS, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "lessons");
        assertThrows(IllegalValueException.class, expectedMessage, group::toModelType);
    }

}
