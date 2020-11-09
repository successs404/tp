package team.serenity.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import team.serenity.commons.core.GuiSettings;
import team.serenity.model.group.Group;
import team.serenity.model.group.GroupLessonKey;
import team.serenity.model.group.GroupName;
import team.serenity.model.group.lesson.Lesson;
import team.serenity.model.group.lesson.LessonName;
import team.serenity.model.group.question.Question;
import team.serenity.model.group.student.Student;
import team.serenity.model.group.studentinfo.StudentInfo;
import team.serenity.model.managers.GroupManager;
import team.serenity.model.managers.ReadOnlyQuestionManager;
import team.serenity.model.managers.ReadOnlySerenity;
import team.serenity.model.userprefs.ReadOnlyUserPrefs;
import team.serenity.model.util.UniqueList;

/**
 * The API of the Model component.
 */
public interface Model {

    /**
     * {@code Predicate} that always evaluate to true.
     */
    Predicate<Group> PREDICATE_SHOW_ALL_GROUPS = unused -> true;

    /**
     * {@code Predicate} that always evaluate to true.
     */
    Predicate<Lesson> PREDICATE_SHOW_ALL_LESSONS = unused -> true;

    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<Question> PREDICATE_SHOW_ALL_QUESTIONS = unused -> true;

    // ========== UserPrefs ==========

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     * @param userPrefs The user prefs data.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     *
     * @param guiSettings The gui settings involved.
     */
    void setGuiSettings(GuiSettings guiSettings);

    // ========== Serenity ==========

    /**
     * Returns serenity.
     */
    ReadOnlySerenity getSerenity();

    /**
     * Returns the GroupManager.
     */
    GroupManager getGroupManager();

    /**
     * Returns the user prefs' serenity file path.
     */
    Path getSerenityFilePath();

    // ========== GroupManager ==========

    /**
     * Returns an unmodifiable view of the filtered group list.
     */
    ObservableList<Group> getFilteredGroupList();

    ObservableList<Group> getListOfGroups();

    /**
     * Returns true if at least one group exists in serenity.
     *
     * @return Whether any group exists.
     */
    boolean isEmpty();

    /**
     * Deletes the given group. The group must exist in serenity.
     *
     * @param target The group to delete.
     */
    void deleteGroup(Group target);

    /**
     * Adds the given group. {@code group} must not already exist in serenity.
     *
     * @param group The group to add.
     */
    void addGroup(Group group);

    /**
     * Exports attendance data of the given group as XLSX file.
     *
     * @param group The group to export attendance.
     */
    void exportAttendance(Group group);

    /**
     * Exports participation data of the given group as XLSX file.
     *
     * @param group The group to export participation score.
     */
    void exportParticipation(Group group);

    /**
     * Returns true if a group with a GroupName that is the same as {@code toCheck} exists in the
     * GroupManager.
     *
     * @param toCheck the given group name.
     * @return true if the given group name already exist in the GroupManager.
     */
    boolean hasGroupName(GroupName toCheck);

    /**
     * Updates the filter of the filtered group list to filter by the given {@code predicate}.
     *
     * @param predicate The group predicate.
     */
    void updateFilteredGroupList(Predicate<Group> predicate);

    /**
     * Get all student info objects from all groups.
     *
     * @return The observable list of StudentInfo.
     */
    ObservableList<StudentInfo> getAllStudentInfo();

    // ========== LessonManager ==========

    /**
     * Returns an unmodifiable view of the lesson list.
     *
     * @return The observable list of Lessons.
     */
    ObservableList<Lesson> getLessonList();

    /**
     * Returns an unmodifiable view of the filtered lesson list.
     *
     * @return The observable list of Lessons.
     */
    ObservableList<Lesson> getFilteredLessonList();

    /**
     * Returns true if a lesson with {@code LessonName} in group {@code GroupName}
     * is the same as {@code lessonName} exists in the LessonManager.
     *
     * @param groupName the given group to check against.
     * @param lessonName the given lesson name to check for.
     * @return true if the given lesson name already exists in the group in the LessonManager.
     */
    boolean ifTargetGroupHasLessonName(GroupName groupName, LessonName lessonName);

    /**
     * Deletes the given group's lesson. The group's lesson must exist in serenity.
     *
     * @param targetGroup The group containing the lesson to delete.
     * @param targetLesson The lesson to delete.
     */
    void deleteLesson(Group targetGroup, Lesson targetLesson);

    /**
     * Updates the lesson list to filter when changing to another group of interest.
     */
    void updateLessonList();

    /**
     * Updates the filter of the filtered lesson list to filter by the given {@code predicate}.
     *
     * @param predicate The lesson predicate.
     */
    void updateFilteredLessonList(Predicate<Lesson> predicate);

    // ========== StudentManager ==========

    /**
     * Returns an unmodifiable view of the student list.
     *
     * @return The observable list of Students.
     */
    ObservableList<Student> getStudentList();

    /**
     * Returns true if a question that is the same as {@code toCheck} exists in the
     * StudentManager.
     *
     * @param toCheck the given student.
     * @return true if the given student already exist in the StudentManager.
     */
    boolean hasStudent(Student toCheck);

    /**
     * Removes a Student from a Group.
     *
     * @param student The student to delete.
     * @param predicate The group predicate.
     */
    void deleteStudentFromGroup(Student student, Predicate<Group> predicate);

    /**
     * Adds a Student to a Group
     *
     * @param student The student to add.
     * @param predicate The group predicate.
     */
    void addStudentToGroup(Student student, Predicate<Group> predicate);

    /**
     * Updates the student list when changing to another group of interest.
     */
    void updateStudentsList();

    // ========== StudentInfoManager ==========

    /**
     * Returns an unmodifiable view of the student info list
     *
     * @return The observable list of StudentInfos.
     */
    ObservableList<StudentInfo> getStudentsInfoList();

    /**
     * Returns an unmodifiable view of the student info list from the specified {@code GroupLessonKey}.
     *
     * @param key The GroupLessonKey.
     * @return The observable list of StudentInfos.
     */
    ObservableList<StudentInfo> getObservableListOfStudentsInfoFromKey(GroupLessonKey key);

    /**
     * Replaces listOfStudentsInfo stored at {@code key} with {@code newListOfStudentsInfo}.
     * @param key the given target key.
     * @param newListOfStudentsInfo the given edited list of studentInfo.
     */
    void setListOfStudentsInfoToGroupLessonKey(GroupLessonKey key, UniqueList<StudentInfo> newListOfStudentsInfo);

    /**
     * Updates the student info list to filter when changing to another lesson of interest.
     */
    void updateStudentsInfoList();

    // ========== QuestionManager ==========

    /**
     * Returns the questionManager.
     *
     * @return The QuestionManager.
     */
    ReadOnlyQuestionManager getQuestionManager();

    /**
     * Replaces questionManager data with the data in {@code questionManager}.
     *
     * @param questionManager The ReadOnlyQuestionManager.
     */
    void setQuestionManager(ReadOnlyQuestionManager questionManager);

    /**
     * Returns true if a question that is the same as {@code toCheck} exists in the
     * QuestionManager.
     *
     * @param toCheck the given question.
     * @return true if the given question already exist in the QuestionManager.
     */
    boolean hasQuestion(Question toCheck);

    /**
     * Deletes the given question.
     * The question must exist in the QuestionManager.
     *
     * @param toDelete the given question.
     */
    void deleteQuestion(Question toDelete);

    /**
     * Adds the given question.
     * {@code toAdd} must not already exist in the QuestionManager.
     *
     * @param toAdd the given question.
     */
    void addQuestion(Question toAdd);

    /**
     * Replaces the given question {@code target} with {@code edited}.
     * {@code target} must exist in the QuestionManager.
     * {@code edited} must not be the same as another existing question in the QuestionManager.
     *
     * @param target the given target question.
     * @param edited the given edited question.
     */
    void setQuestion(Question target, Question edited);

    /**
     * Returns an unmodifiable view of the filtered question list
     *
     * @return The observable list of Questions.
     */
    ObservableList<Question> getFilteredQuestionList();

    /**
     * Updates the filter of the filtered question list to filter by the given {@code predicate}.
     *
     * @param predicate the given predicate.
     */
    void updateFilteredQuestionList(Predicate<Question> predicate);

}
