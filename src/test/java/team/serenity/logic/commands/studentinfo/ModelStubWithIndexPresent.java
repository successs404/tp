package team.serenity.logic.commands.studentinfo;

import static team.serenity.testutil.TypicalStudent.AARON;
import static team.serenity.testutil.TypicalStudentInfo.AARON_PRESENT_INFO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import team.serenity.model.group.Group;
import team.serenity.model.group.GroupLessonKey;
import team.serenity.model.group.UniqueGroupList;
import team.serenity.model.group.lesson.Lesson;
import team.serenity.model.group.lesson.UniqueLessonList;
import team.serenity.model.group.studentinfo.StudentInfo;
import team.serenity.model.util.UniqueList;
import team.serenity.testutil.GroupBuilder;
import team.serenity.testutil.LessonBuilder;
import team.serenity.testutil.ModelStub;

/**
 * A Model stub containing a present student
 */
class ModelStubWithIndexPresent extends ModelStub {
    private Group uniqueGroup;
    private Lesson uniqueLesson;

    @Override
    public ObservableList<Group> getFilteredGroupList() {
        List<Group> grpList = new ArrayList<>();
        this.uniqueGroup = new GroupBuilder().withName("G01")
            .withStudents(AARON)
            .withLessons("1-1").build();
        grpList.add(uniqueGroup);
        UniqueList<Group> groupUniqueList = new UniqueGroupList();
        groupUniqueList.setElementsWithList(grpList);
        return groupUniqueList.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Lesson> getFilteredLessonList() {
        List<Lesson> lsnList = new ArrayList<>();
        this.uniqueLesson = new LessonBuilder()
            .withName("1-1")
            .withStudentInfos(AARON_PRESENT_INFO)
            .build();
        lsnList.add(uniqueLesson);
        UniqueList<Lesson> lessonUniqueList = new UniqueLessonList();
        lessonUniqueList.setElementsWithList(lsnList);
        return lessonUniqueList.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<StudentInfo> getObservableListOfStudentsInfoFromKey(GroupLessonKey key) {
        GroupLessonKey mapKey = new GroupLessonKey(uniqueGroup.getGroupName(), uniqueLesson.getLessonName());
        Map<GroupLessonKey, UniqueList<StudentInfo>> uniqueStudentInfoList = new HashMap<>();
        uniqueStudentInfoList.put(mapKey, uniqueLesson.getStudentsInfo());
        return uniqueStudentInfoList.get(key).asUnmodifiableObservableList();
    }

    @Override
    public void setListOfStudentsInfoToGroupLessonKey(GroupLessonKey key,
                                                      UniqueList<StudentInfo> newListOfStudentsInfo) {
    }

    @Override
    public void updateLessonList() {
        return;
    }

    @Override
    public void updateStudentsInfoList() {
        return;
    }

}
