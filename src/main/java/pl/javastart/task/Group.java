package pl.javastart.task;

public class Group {
    private String code;
    private String name;
    private Lecturer lecturer;
    private Student[] students = new Student[10];
    private int studentCount;

    public Group(String code, String name, Lecturer lecturer) {
        this.code = code;
        this.name = name;
        this.lecturer = lecturer;
    }

    public Student[] getStudents() {
        return students;
    }

    public void setStudents(Student[] students) {
        this.students = students;
    }

    public void incrementStudentCount() {
        studentCount++;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }
}

