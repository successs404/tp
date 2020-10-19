package seedu.address.storage;

import static seedu.address.testutil.TypicalGroups.GROUP_A;

import java.util.List;
import java.util.stream.Collectors;

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

}
