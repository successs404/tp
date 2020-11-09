package team.serenity.model.group.student;

import static team.serenity.commons.util.AppUtil.checkArgument;
import static team.serenity.commons.util.CollectionUtil.requireAllNonNull;

/**
 * Represents a tutorial Group in Serenity.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Student {

    public static final String STUDENT_NAME_ERROR = "Name cannot be empty";
    public static final String STUDENT_ID_ERROR = "Student no cannot be empty "
        + "and must follow the format 'AXXXXXXXX' "
        + "where X is alphanumeric";
    private StudentName studentName;
    private StudentNumber studentNo;

    /**
     * Constructs a {@code Student}.
     *
     * @param studentName A valid name.
     * @param studentNo A valid student no.
     */
    public Student(String studentName, String studentNo) {
        requireAllNonNull(studentName, studentNo);
        checkArgument(isValidName(studentName), STUDENT_NAME_ERROR);
        checkArgument(isValidStudentId(studentNo), STUDENT_ID_ERROR);
        this.studentName = new StudentName(studentName);
        this.studentNo = new StudentNumber(studentNo);
    }

    /**
     * Constructs a {@code Student}.
     *
     * @param name A valid student name.
     * @param number A valid student number.
     */
    public Student(StudentName name, StudentNumber number) {
        requireAllNonNull(name, number);
        this.studentName = name;
        this.studentNo = number;
    }

    /**
     * Returns whether the StudentName is valid.
     *
     * @param s The StudentName to check.
     * @return Whether the StudentName is valid.
     */
    public static boolean isValidName(String s) {
        return StudentName.isValidName(s);
    }

    /**
     * Checks whether String s is a valid student no.
     * @param s Student no.
     * @return Whether String is valid.
     */
    public static boolean isValidStudentId(String s) {
        return StudentNumber.isValidName(s);
    }

    /**
     * Returns the StudentName.
     *
     * @return The name of the student.
     */
    public StudentName getStudentName() {
        return this.studentName;
    }

    /**
     * Returns the StudentNo.
     *
     * @return The matriculation number of the student.
     */
    public StudentNumber getStudentNo() {
        return this.studentNo;
    }

    /**
     * Returns true if both student have the same student number.
     * This defines a weaker notion of equality between two students.
     *
     * @param other The other student to compare with.
     * @return Whether the student is the same.
     */
    public boolean isSameStudent(Student other) {
        return this.studentNo.equals(other.studentNo);
    }

    @Override
    public String toString() {
        return this.studentName + " " + this.studentNo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof Student) {
            Student other = (Student) obj;
            return other.getStudentName().equals(getStudentName()) && other.getStudentNo().equals(getStudentNo());
        } else {
            return false;
        }
    }
}
