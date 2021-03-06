package team.serenity.testutil;

import static team.serenity.testutil.TypicalStudent.AARON;
import static team.serenity.testutil.TypicalStudent.BENJAMIN;
import static team.serenity.testutil.TypicalStudent.CATHERINE;
import static team.serenity.testutil.TypicalStudent.DAVID;
import static team.serenity.testutil.TypicalStudent.ELFIE;
import static team.serenity.testutil.TypicalStudent.GEORGE;
import static team.serenity.testutil.TypicalStudent.HELENE;

import java.util.ArrayList;
import java.util.Arrays;

import team.serenity.model.group.studentinfo.Attendance;
import team.serenity.model.group.studentinfo.StudentInfo;
import team.serenity.model.group.studentinfo.UniqueStudentInfoList;
import team.serenity.model.managers.StudentInfoManager;
import team.serenity.model.util.UniqueList;

public class TypicalStudentInfo {

    public static final int ORIGINAL_SCORE = 3;
    public static final int VALID_ADD_SCORE = 1;
    public static final int VALID_SUB_SCORE = 1;
    public static final int SCORE_OUT_OF_RANGE = 6;
    public static final int SUB_TILL_SCORE_OUT_OF_RANGE = 4;

    public static final StudentInfo AARON_ABSENT_INFO = new StudentInfoBuilder().withStudent(AARON)
            .withAttendance(false).withParticipation(0).build();

    public static final StudentInfo AARON_PRESENT_INFO = new StudentInfoBuilder().withStudent(AARON)
            .withAttendance(true).withParticipation(ORIGINAL_SCORE).build();

    public static final StudentInfo AARON_FLAGGED_INFO = new StudentInfoBuilder().withStudent(AARON)
            .withAttendance(new Attendance(false, true)).withParticipation(0).build();

    public static final StudentInfo BENJAMIN_ABSENT_INFO = new StudentInfoBuilder().withStudent(BENJAMIN)
            .withAttendance(false).withParticipation(0).build();

    public static final StudentInfo BENJAMIN_PRESENT_INFO = new StudentInfoBuilder().withStudent(BENJAMIN)
            .withAttendance(true).withParticipation(3).build();

    public static final StudentInfo BENJAMIN_FLAGGED_INFO = new StudentInfoBuilder().withStudent(BENJAMIN)
            .withAttendance(new Attendance(false, true)).withParticipation(0).build();

    public static final StudentInfo CATHERINE_ABSENT_INFO = new StudentInfoBuilder().withStudent(CATHERINE)
            .withAttendance(false).withParticipation(0).build();

    public static final StudentInfo CATHERINE_PRESENT_INFO = new StudentInfoBuilder().withStudent(CATHERINE)
            .withAttendance(true).withParticipation(3).build();

    public static final StudentInfo CATHERINE_FLAGGED_INFO = new StudentInfoBuilder().withStudent(CATHERINE)
            .withAttendance(new Attendance(false, true)).withParticipation(0).build();

    public static final StudentInfo DAVID_INFO = new StudentInfoBuilder().withStudent(DAVID)
            .withAttendance(false).withParticipation(0).build();

    public static final StudentInfo ELFIE_INFO = new StudentInfoBuilder().withStudent(ELFIE)
            .withAttendance(false).withParticipation(0).build();

    public static final StudentInfo GEORGE_INFO = new StudentInfoBuilder().withStudent(GEORGE)
            .withAttendance(false).withParticipation(0).build();

    public static final StudentInfo HELENE_INFO = new StudentInfoBuilder().withStudent(HELENE)
            .withAttendance(false).withParticipation(0).build();

    public TypicalStudentInfo() {
    } //prevents instantiation

    public static UniqueList<StudentInfo> getTypicalStudentInfo() {
        UniqueList<StudentInfo> list = new UniqueStudentInfoList();
        list.setElementsWithList(new ArrayList<>(Arrays.asList(HELENE_INFO, GEORGE_INFO)));
        return list;
    }

    public static StudentInfoManager getTypicalStudentInfoManager() {
        StudentInfoManager studentInfoManager = new StudentInfoManager();
        studentInfoManager.setListOfStudentsInfoToGroupLessonKey(new GroupLessonKeyBuilder().build(),
                getTypicalStudentInfo());
        return studentInfoManager;
    }
}
