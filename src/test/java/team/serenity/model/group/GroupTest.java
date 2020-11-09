package team.serenity.model.group;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static team.serenity.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import team.serenity.model.group.lesson.Lesson;
import team.serenity.model.group.lesson.UniqueLessonList;
import team.serenity.model.group.student.Student;
import team.serenity.model.group.student.UniqueStudentList;
import team.serenity.model.group.studentinfo.StudentInfo;
import team.serenity.model.group.studentinfo.UniqueStudentInfoList;
import team.serenity.model.util.UniqueList;

class GroupTest {

    @Test
    public void constructor_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Group((GroupName) null, null));
        assertThrows(NullPointerException.class, () -> new Group(null, (String) null));
        assertThrows(NullPointerException.class, () -> new Group(null, (UniqueList<Student>) null));
        assertThrows(NullPointerException.class, () -> new Group((String) null, null, null));
        assertThrows(NullPointerException.class, () -> new Group((GroupName) null, null, null));
    }

    @Test
    void getName() {
        GroupName expectedGroup = new GroupName("G01");
        assertEquals(new Group("G01", new UniqueStudentList(),
            new UniqueLessonList()).getGroupName(), expectedGroup); //same
    }

    @Test
    void getStudents() {
        UniqueList<Student> students = new UniqueStudentList();
        List<Student> expectedList = students.asUnmodifiableObservableList();
        assertEquals(new Group("G01", students, new UniqueLessonList()).getStudents(), students);
        assertEquals(new Group("G01", students, new UniqueLessonList())
            .getStudentsAsUnmodifiableObservableList(), expectedList);
    }

    @Test
    void getLessons() {
        UniqueList<Lesson> lessons = new UniqueLessonList();
        List<Lesson> expectedList = lessons.asUnmodifiableObservableList();
        assertEquals(new Group("G01", new UniqueStudentList(), lessons).getLessons(), lessons);
        assertEquals(new Group("G01", new UniqueStudentList(), lessons)
            .getLessonsAsUnmodifiableObservableList(), expectedList);
    }

    @Test
    void testEquals() {
        UniqueList<Student> students = new UniqueStudentList();
        Student aaron = new Student("Aaron Tan", "A0123456A");
        students.add(aaron);

        UniqueList<Lesson> lessons = new UniqueLessonList();

        UniqueList<StudentInfo> studentInfos = new UniqueStudentInfoList();
        studentInfos.add(new StudentInfo(aaron));
        lessons.add(new Lesson("1-1", studentInfos));

        Group group = new Group("G01", students,
            lessons);
        Group sameGroup = new Group("G01", students, lessons);
        Group differentStudentList = new Group("G01", new UniqueStudentList(),
            lessons);
        Group differentLessonList = new Group("G01", students,
            new UniqueLessonList());
        Group differentName = new Group("G02", students,
            lessons);

        assertEquals(group, group); //same object
        assertEquals(group, sameGroup); //different object, same contents

        //assertFalse(group.equals(differentLessonList)); //same group name, different lesson list
        assertNotEquals(group, differentStudentList); //same group name, different student list
        assertNotEquals(group, differentName); //different group name
    }

    @Test
    void testHashCode() {
        GroupName expectedGroup = new GroupName("G01");
        Group group = new Group("G01", new UniqueStudentList(),
            new UniqueLessonList());
        assertEquals(group.hashCode(), (expectedGroup.hashCode()));
    }

    @Test
    void testToString() {
        GroupName expectedGroup = new GroupName("G01");
        Group group = new Group("G01",
            new UniqueStudentList(),
            new UniqueLessonList());
        String expectedValue = String.format("Group %s", expectedGroup);
        assertEquals(group.toString(), expectedValue);
    }
}
