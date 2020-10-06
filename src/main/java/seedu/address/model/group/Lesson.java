package seedu.address.model.group;

import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Set;

/**
 * Represents a tutorial class in serenity. Guarantees: details are present and not null, field values are validated,
 * immutable.
 */
public class Lesson {

    public static final String NAME_CONSTRAINT = "Class name cannot be empty";
    public static final String STUDENTS_INFO_CONSTRAINT = "Students information cannot be empty";
    private final String name;
    private final Set<StudentInfo> studentsInfo;

    /**
     * Constructs a {@code Class}.
     *
     * @param name A valid name.
     */
    public Lesson(String name, Set<StudentInfo> studentsInfo) {
        requireAllNonNull(name, studentsInfo);
        checkArgument(isValidName(name), NAME_CONSTRAINT);
        checkArgument(isValidStudentInfo(studentsInfo), STUDENTS_INFO_CONSTRAINT);
        this.name = name;
        this.studentsInfo = studentsInfo;
    }

    boolean isValidName(String name) {
        return name.length() > 0;
    }

    public Set<StudentInfo> getStudentsInfo() {
        return Collections.unmodifiableSet(studentsInfo);
    }

    boolean isValidStudentInfo(Set<StudentInfo> studentsInfo) {
        return studentsInfo.size() > 0;
    }


    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Lesson)) {
            return false;
        }

        Lesson otherClass = (Lesson) obj;
        return otherClass.getName().equals(getName()) && otherClass.getStudentsInfo()
            .containsAll(getStudentsInfo());
    }
}

