package team.serenity.commons.util;

import static team.serenity.commons.core.Messages.MESSAGE_FILE_EMPTY;
import static team.serenity.commons.core.Messages.MESSAGE_INVALID_HEADER_COLUMNS;
import static team.serenity.commons.core.Messages.MESSAGE_NO_STUDENT_LIST;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import team.serenity.commons.core.sorter.LessonSorter;
import team.serenity.commons.core.sorter.StudentInfoSorter;
import team.serenity.commons.core.sorter.StudentSorter;
import team.serenity.logic.parser.exceptions.ParseException;
import team.serenity.model.group.Group;
import team.serenity.model.group.GroupLessonKey;
import team.serenity.model.group.lesson.Lesson;
import team.serenity.model.group.student.Student;
import team.serenity.model.group.student.StudentNumber;
import team.serenity.model.group.studentinfo.StudentInfo;
import team.serenity.model.group.studentinfo.UniqueStudentInfoList;
import team.serenity.model.util.UniqueList;

/**
 * Reads XLSX file that the tutor downloads from LUMINUS and writes JSON data to a new XLSX file.
 */
public class XlsxUtil {

    private String filePath;
    private Workbook workbook;
    private Sheet sheet;

    private DataFormatter formatter = new DataFormatter();

    /**
     * Creates a XlsxUtil object that manages XLSX files.
     *
     * @param filePath The path of the XLSX file that the tutor downloads from LUMINUS.
     */
    public XlsxUtil(String filePath) {
        try {
            this.filePath = filePath;
            this.workbook = new XSSFWorkbook(filePath);
            this.sheet = this.workbook.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a XlsxUtil object that exports a group as a XLSX file.
     */
    public XlsxUtil() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet();
    }

    /**
     * Creates a XlsxUtil object that manages XLSX files.
     *
     * @param filePath The path of the XLSX file that the tutor downloads from LUMINUS.
     * @param workbook The workbook of the XLSX file.
     */
    public XlsxUtil(String filePath, Workbook workbook) {
        this.filePath = filePath;
        this.workbook = workbook;
        this.sheet = this.workbook.getSheetAt(0);
    }

    /**
     * Reads XLSX file that the tutor downloads from LUMINUS.
     * The XLSX file stores a list of {@code Student} that are in a tutorial group.
     *
     * @return a set of students.
     */
    public Set<Student> readStudentsFromXlsx() {
        Set<Student> students = new HashSet<>();
        Iterator<Row> rowIterator = this.sheet.iterator();
        skipRowsToHeaderRow(rowIterator);
        readDetailsOfStudents(rowIterator, students);
        List<Student> studentList = new ArrayList<>(students);
        studentList.sort(new StudentSorter());
        return new LinkedHashSet<>(studentList);
    }

    /**
     * Checks the validity of the XLSX file.
     *
     * @throws ParseException a parsing exception.
     */
    public void checkValidityOfXlsx() throws ParseException {
        checkValidityXlsxEmpty();
        checkValidityXlsxContents(this.sheet.iterator());
    }

    private void checkValidityXlsxEmpty() throws ParseException {
        if (sheet.getLastRowNum() == -1) {
            throw new ParseException(MESSAGE_FILE_EMPTY);
        }
    }

    private void checkValidityXlsxContents(Iterator<Row> rowIterator) throws ParseException {
        if (skipRowsToHeaderRow(rowIterator) == null) {
            throw new ParseException(MESSAGE_INVALID_HEADER_COLUMNS);
        }
        if (!rowIterator.hasNext()) {
            throw new ParseException(MESSAGE_NO_STUDENT_LIST);
        }
    }

    private Row skipRowsToHeaderRow(Iterator<Row> rowIterator) {
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (isHeaderRow(row)) {
                return row;
            }
        }
        return null;
    }

    private boolean isHeaderRow(Row row) {
        return this.formatter.formatCellValue(row.getCell(0)).equals("Photo")
            && this.formatter.formatCellValue(row.getCell(1)).equals("Name")
            && this.formatter.formatCellValue(row.getCell(2)).equals("Student Number");
    }

    private void readDetailsOfStudents(Iterator<Row> rowIterator, Set<Student> students)
        throws IllegalArgumentException {
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            cellIterator.next();

            Cell nameCell = cellIterator.next();
            String name = this.formatter.formatCellValue(nameCell);

            Cell studentIdCell = cellIterator.next();
            String studentId = this.formatter.formatCellValue(studentIdCell);

            Student student = new Student(name, studentId);
            students.add(student);
        }
    }

    /**
     * Reads a set of the Lessons
     *
     * @param studentsInfo Set of StudentInfo.
     * @return Set of Lessons.
     */
    public Set<Lesson> readLessonsFromXlsx(Set<StudentInfo> studentsInfo) {
        Set<Lesson> lessons = new HashSet<>();
        Iterator<Row> rowIterator = this.sheet.iterator();
        Row headerRow = skipRowsToHeaderRow(rowIterator);
        readDetailsOfLessons(Objects.requireNonNull(headerRow), lessons, studentsInfo);
        List<Lesson> lessonList = new ArrayList<>(lessons);
        lessonList.sort(new LessonSorter());
        return new LinkedHashSet<>(lessonList);
    }

    private void readDetailsOfLessons(Row headerRow, Set<Lesson> lessons, Set<StudentInfo> studentsInfo) {
        for (Cell cell : headerRow) {
            if (this.formatter.formatCellValue(cell).startsWith("T")) {
                String lessonName = this.formatter.formatCellValue(cell);
                String formattedLessonName = formatLessonName(lessonName);
                UniqueList<StudentInfo> newStudentsInfo = new UniqueStudentInfoList();
                newStudentsInfo.setElementsWithList(new ArrayList<>(studentsInfo));
                lessons.add(new Lesson(formattedLessonName, newStudentsInfo));
            }
        }
    }

    private String formatLessonName(String lessonName) {
        String trimmedLessonName = lessonName.substring(1); // remove the first character "T" from the lessonName
        int lessonNumbering = Integer.parseInt(trimmedLessonName);
        int weekNumber = lessonNumbering / 2 + lessonNumbering % 2;
        int lessonNumber = 2 - lessonNumbering % 2;
        return String.format("%d-%d", weekNumber, lessonNumber);
    }

    /**
     * Creates a new set of StudentInfo from XLSX.
     *
     * @param students Set of Students.
     * @return Set of StudentInfo.
     */
    public Set<StudentInfo> readStudentsInfoFromXlsx(Set<Student> students) {
        Set<StudentInfo> studentsInfo = new HashSet<>();
        initialiseStudentInfo(students, studentsInfo);
        List<StudentInfo> studentInfoList = new ArrayList<>(studentsInfo);
        studentInfoList.sort(new StudentInfoSorter());
        return new LinkedHashSet<>(studentInfoList);
    }

    private void initialiseStudentInfo(Set<Student> students, Set<StudentInfo> studentsInfo) {
        for (Student student : students) {
            studentsInfo.add(new StudentInfo(student));
        }
    }

    /**
     * Write attendance data to XLSX file.
     *
     * @param group Group to export the attendance records.
     * @param studentInfoMap List of StudentInfo.
     */
    public void writeAttendanceToXlsx(Group group, Map<GroupLessonKey, UniqueList<StudentInfo>> studentInfoMap) {
        UniqueList<Student> studentList = group.getSortedStudents();
        UniqueList<Lesson> lessonList = group.getSortedLessons();
        List<List<Object>> data = new ArrayList<>();
        Map<StudentNumber, List<Integer>> studentDetailsMap = new HashMap<>();

        for (Lesson lesson : lessonList) {
            GroupLessonKey groupLessonKey = new GroupLessonKey(group.getGroupName(), lesson.getLessonName());
            for (StudentInfo studentInfo : studentInfoMap.get(groupLessonKey)) {
                Student student = studentInfo.getStudent();
                Optional<List<Integer>> attList = Optional.ofNullable(studentDetailsMap.get(student.getStudentNo()));
                List<Integer> newAttList = attList.orElseGet(ArrayList::new);
                newAttList.add(studentInfo.getAttendance().getIntegerAttendance());
                studentDetailsMap.put(student.getStudentNo(), newAttList);
            }
        }

        generateData(data, studentList, studentDetailsMap);
        writeDataToXlsx(data, String.format("%s_attendance.xlsx", group.getGroupName().toString()), group);
    }

    /**
     * Write participation score data to XLSX file.
     *
     * @param group Group to export the participation score records.
     * @param studentInfoMap List of StudentInfo.
     */
    public void writeScoreToXlsx(Group group, Map<GroupLessonKey, UniqueList<StudentInfo>> studentInfoMap) {
        UniqueList<Student> studentList = group.getSortedStudents();
        UniqueList<Lesson> lessonList = group.getSortedLessons();
        List<List<Object>> data = new ArrayList<>();
        Map<StudentNumber, List<Integer>> studentDetailsMap = new HashMap<>();

        for (Lesson lesson : lessonList) {
            GroupLessonKey groupLessonKey = new GroupLessonKey(group.getGroupName(), lesson.getLessonName());
            for (StudentInfo studentInfo : studentInfoMap.get(groupLessonKey)) {
                Student student = studentInfo.getStudent();
                Optional<List<Integer>> scoreList = Optional.ofNullable(studentDetailsMap.get(student.getStudentNo()));
                List<Integer> newScoreList = scoreList.orElseGet(ArrayList::new);
                newScoreList.add(studentInfo.getParticipation().getScore());
                studentDetailsMap.put(student.getStudentNo(), newScoreList);
            }
        }

        generateData(data, studentList, studentDetailsMap);
        writeDataToXlsx(data, String.format("%s_participation.xlsx", group.getGroupName().toString()), group);
    }

    private void generateData(List<List<Object>> data, UniqueList<Student> studentList,
        Map<StudentNumber, List<Integer>> studentDetailsMap) {
        for (Student student : studentList) {
            List<Object> studentDetails = new ArrayList<>();
            studentDetails.add(student.getStudentName().toString());
            studentDetails.add(student.getStudentNo().toString());
            List<Integer> lst = studentDetailsMap.get(student.getStudentNo());
            studentDetails.addAll(lst);
            data.add(studentDetails);
        }
    }

    private void writeDataToXlsx(List<List<Object>> data, String outputFileName, Group group) {
        int rowCount = 0;
        int columnCount = 0;

        Row row = sheet.createRow(rowCount);
        Cell cell = row.createCell(columnCount);

        addTitle(cell, group.getGroupName().toString());
        addHeaders(goToHeaderRow(rowCount), columnCount, group.getLessons());
        addContent(goToContentRow(rowCount), data);

        try {
            FileOutputStream outputStream = new FileOutputStream(outputFileName);
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addTitle(Cell titleCell, String groupName) {
        String title = String.format("CS2101 %s Attendance Sheet", groupName);
        titleCell.setCellValue(title);
    }

    private int goToHeaderRow(int rowCount) {
        return rowCount + 2;
    }

    private int goToContentRow(int rowCount) {
        return rowCount + 3;
    }
    private void addHeader(int columnCount, Row headerRow, String headerName) {
        Cell headerCell = headerRow.createCell(columnCount);
        headerCell.setCellValue(headerName);
    }

    private void addHeaders(int rowCount, int columnCount, UniqueList<Lesson> lessonList) {
        Row row = sheet.createRow(rowCount);
        addHeader(columnCount, row, "Name");
        columnCount++;
        addHeader(columnCount, row, "Student Number");
        columnCount++;
        for (Lesson lesson : lessonList) {
            addHeader(columnCount, row, lesson.getLessonName().toString());
            columnCount++;
        }
    }

    private void addContent(int rowCount, List<List<Object>> data) {
        for (List<Object> studentDetails : data) {
            Row row = sheet.createRow(rowCount);
            int columnCount = 0;
            for (Object field : studentDetails) {
                Cell cell = row.createCell(columnCount);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
                columnCount++;
            }
            rowCount++;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this // short circuit if same object
                || (obj instanceof XlsxUtil // instanceof handles nulls
                && this.filePath.equals(((XlsxUtil) obj).filePath));
    }
}

