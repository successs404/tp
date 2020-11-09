package team.serenity.storage;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import team.serenity.commons.exceptions.IllegalValueException;
import team.serenity.model.group.lesson.Lesson;
import team.serenity.model.group.lesson.LessonName;
import team.serenity.model.group.studentinfo.StudentInfo;
import team.serenity.model.group.studentinfo.UniqueStudentInfoList;
import team.serenity.model.util.UniqueList;

/**
 * Jackson-friendly version of {@link Lesson}.
 */
class JsonAdaptedLesson {


    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Lesson's %s field is missing!";

    private final List<JsonAdaptedStudentInfo> studentInfos = new ArrayList<>();
    private final String name;

    /**
     * Constructs a {@code JsonAdaptedLesson} with the given {@code name}.
     *
     * @param name The lesson name.
     * @param studentInfos The studentinfos that the lesson is managing.
     */
    @JsonCreator
    public JsonAdaptedLesson(@JsonProperty("name") String name,
        @JsonProperty("studentInfos") List<JsonAdaptedStudentInfo> studentInfos) {
        this.studentInfos.addAll(studentInfos);
        this.name = name;
    }

    /**
     * Converts a given {@code Lesson} into this Lesson for Jackson use.
     *
     * @param source The lesson to manage.
     */
    public JsonAdaptedLesson(Lesson source) {
        requireNonNull(source);
        this.name = source.getLessonName().toString();
        this.studentInfos.addAll(source.getStudentsInfo()
            .asUnmodifiableObservableList().stream()
            .map(JsonAdaptedStudentInfo::new).collect(
            Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted Lesson object into the model's {@code Lesson} object.
     *
     * @return A new Lesson object.
     * @throws IllegalValueException Thrown if there were any data constraints violated in the adapted question.
     */
    public Lesson toModelType() throws IllegalValueException {
        if (this.name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                LessonName.class.getSimpleName()));
        }

        if (!LessonName.isValidName(this.name)) {
            throw new IllegalValueException(LessonName.MESSAGE_CONSTRAINTS);
        }

        UniqueList<StudentInfo> studentInfos = new UniqueStudentInfoList();
        for (JsonAdaptedStudentInfo jsonStudentInfo : this.studentInfos) {
            StudentInfo studentInfo = jsonStudentInfo.toModelType();
            studentInfos.add(studentInfo);
        }
        return new Lesson(name, studentInfos);
    }
}
