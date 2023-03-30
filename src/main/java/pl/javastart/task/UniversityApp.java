package pl.javastart.task;

import java.util.Arrays;

public class UniversityApp {
    private Lecturer[] lecturers = new Lecturer[10];
    private Group[] groups = new Group[10];
    private Grade[] grades = new Grade[10];

    private int countLecturers = 0;
    private int countGroups = 0;
    private int countGrades = 0;

    /**
     * Tworzy prowadzącego zajęcia.
     * W przypadku gdy prowadzący z zadanym id już istnieje, wyświetlany jest komunikat:
     * "Prowadzący z id [id_prowadzacego] już istnieje"
     *
     * @param id        - unikalny identyfikator prowadzącego
     * @param degree    - stopień naukowy prowadzącego
     * @param firstName - imię prowadzącego
     * @param lastName  - nazwisko prowadzącego
     */
    public void createLecturer(int id, String degree, String firstName, String lastName) {
        Lecturer lecturer = checkLecturerExists(id);
        if (lecturer != null) {
            System.out.printf("Prowadzący z id %d już istnieje\n", id);
            return;
        }
        lecturer = new Lecturer(id, degree, firstName, lastName);
        checkLecturersCapacity();
        lecturers[countLecturers] = lecturer;
        countLecturers++;
        System.out.printf("Dodano prowadzącego: %s %s\n", lecturer.getFirstName(), lecturer.getLastName());
    }

    private Lecturer checkLecturerExists(int id) {
        for (int i = 0; i < countLecturers; i++) {
            if (lecturers[i].getId() == id) {
                return lecturers[i];
            }
        }
        return null;
    }

    private void checkLecturersCapacity() {
        if (countLecturers == lecturers.length) {
            lecturers = Arrays.copyOf(lecturers, lecturers.length * 2);
        }
    }

    /**
         * Tworzy grupę zajęciową.
         * W przypadku gdy grupa z zadanym kodem już istnieje, wyświetla się komunikat:
         * "Grupa [kod grupy] już istnieje"
         * W przypadku gdy prowadzący ze wskazanym id nie istnieje wyświetla się komunikat:
         * "Prowadzący o id [id prowadzacego] nie istnieje"
         *
         * @param code       - unikalny kod grupy
         * @param name       - nazwa przedmiotu (np. "Podstawy programowania")
         * @param lecturerId - identyfikator prowadzącego. Musi zostać wcześniej utworzony za pomocą metody {@link #createLecturer(int, String, String, String)}
         */
    public void createGroup(String code, String name, int lecturerId) {
        Lecturer lecturer = checkLecturerExists(lecturerId);
        if (lecturer == null) {
            System.out.printf("Prowadzący o id %d nie istnieje\n", lecturerId);
            return;
        }

        if (checkGroupExists(code) != null) {
            System.out.printf("Grupa %s już istnieje\n", code);
            return;
        }
        Group group = new Group(code, name, lecturer);
        checkGroupsCapacity();
        groups[countGroups] = group;
        countGroups++;
        System.out.printf("Dodano grupę: %s\n", group.getName());
    }

    private void checkGroupsCapacity() {
        if (countGroups == groups.length) {
            groups = Arrays.copyOf(groups, groups.length * 2);
        }
    }

    private Group checkGroupExists(String code) {
        for (int i = 0; i < countGroups; i++) {
            if (groups[i].getCode().equals(code)) {
                return groups[i];
            }
        }
        return null;
    }

    /**
         * Dodaje studenta do grupy zajęciowej.
         * W przypadku gdy grupa zajęciowa nie istnieje wyświetlany jest komunikat:
         * "Grupa [kod grupy] nie istnieje
         *
         * @param index     - unikalny numer indeksu studenta
         * @param groupCode - kod grupy utworzonej wcześniej za pomocą {@link #createGroup(String, String, int)}
         * @param firstName - imię studenta
         * @param lastName  - nazwisko studenta
         */
    public void addStudentToGroup(int index, String groupCode, String firstName, String lastName) {
        Group group = checkGroupExists(groupCode);
        if (group == null) {
            System.out.printf("Grupa %s nie istnieje", groupCode);
            return;
        }

        Student student = checkStudentExists(index, group);
        if (student != null) {
            System.out.printf("Student o indeksie %d jest już w grupie %s\n",
                    index, groupCode);
            return;
        }
        student = new Student(index, group, firstName, lastName);
        if (group.getStudentCount() == group.getStudents().length) {
            group.setStudents(Arrays.copyOf(group.getStudents(), group.getStudents().length * 2));
        }
        group.getStudents()[group.getStudentCount()] = student;
        group.incrementStudentCount();
        System.out.printf("Dodano studenta: %s %s do grupy: %s\n", firstName, lastName, group.getName());
    }

        /**
         * Wyświetla informacje o grupie w zadanym formacie.
         * Oczekiwany format:
         * Kod: [kod_grupy]
         * Nazwa: [nazwa przedmiotu]
         * Prowadzący: [stopień naukowy] [imię] [nazwisko]
         * Uczestnicy:
         * [nr indeksu] [imie] [nazwisko]
         * [nr indeksu] [imie] [nazwisko]
         * [nr indeksu] [imie] [nazwisko]
         * W przypadku gdy grupa nie istnieje, wyświetlany jest komunikat w postaci: "Grupa [kod] nie znaleziona"
         *
         * @param groupCode - kod grupy, dla której wyświetlić informacje
         */
    public void printGroupInfo(String groupCode) {
        Group group = checkGroupExists(groupCode);
        if (group == null) {
            System.out.printf("Grupa %s nie istnieje", groupCode);
            return;
        }
        System.out.printf("Kod: %s\n", group.getCode());
        System.out.printf("Nazwa: %s\n", group.getName());
        System.out.printf("Prowadzący: %s %s %s\n", group.getLecturer().getDegree(),
                group.getLecturer().getFirstName(), group.getLecturer().getLastName());
        System.out.println("Uczestnicy:");
        for (int i = 0; i < group.getStudentCount(); i++) {
            System.out.printf("%s %s %s\n", group.getStudents()[i].getIndex(), group.getStudents()[i].getFirstName(),
                    group.getStudents()[i].getLastName());
        }
    }

        /**
         * Dodaje ocenę końcową dla wskazanego studenta i grupy.
         * Student musi być wcześniej zapisany do grupy za pomocą {@link #addStudentToGroup(int, String, String, String)}
         * W przypadku, gdy grupa o wskazanym kodzie nie istnieje, wyświetlany jest komunikat postaci:
         * "Grupa pp-2022 nie istnieje"
         * W przypadku gdy student nie jest zapisany do grupy, wyświetlany jest komunikat w
         * postaci: "Student o indeksie 179128 nie jest zapisany do grupy pp-2022"
         * W przypadku gdy ocena końcowa już istnieje, wyświetlany jest komunikat w postaci:
         * "Student o indeksie 179128 ma już wystawioną ocenę dla grupy pp-2022"
         *
         * @param studentIndex - numer indeksu studenta
         * @param groupCode    - kod grupy
         * @param grade        - ocena
         */
    public void addGrade(int studentIndex, String groupCode, double grade) {
        Group group = checkGroupExists(groupCode);
        if (group == null) {
            System.out.printf("Grupa %s nie istnieje", groupCode);
            return;
        }

        Student student = checkStudentExists(studentIndex, group);
        if (student == null) {
            System.out.printf("Student o indeksie %d nie jest zapisany do grupy %s", studentIndex, groupCode);
            return;
        }

        Grade grade1 = new Grade(student, group, grade);
        checkGradeExists(group, student);

        if (checkGradeExists(group, student)) {
            System.out.printf("Student o indeksie %d ma już wystawioną ocenę dla grupy %s\n", studentIndex,
                    groupCode);
            return;
        }
        checkGradesCapacity();
        grades[countGrades] = grade1;
        countGrades++;
        System.out.printf("Dodano ocenę %.1f dla studenta o indeksie %d w grupie %s\n", grade, studentIndex,
                groupCode);
    }

    private void checkGradesCapacity() {
        if (countGrades == grades.length) {
            grades = Arrays.copyOf(grades, grades.length * 2);
        }
    }

    private boolean checkGradeExists(Group group, Student student) {
        for (int i = 0; i < countGrades; i++) {
            if (grades[i].getStudent() == student && grades[i].getGroup() == group && grades[i].getGrade() != 0) {
                return true;
            }
        }
        return false;
    }

    private Student checkStudentExists(int studentIndex, Group group) {
        for (int i = 0; i < group.getStudentCount(); i++) {
            if (group.getStudents()[i].getIndex() == studentIndex) {
                return group.getStudents()[i];
            }
        }
        return null;
    }

    /**
         * Wyświetla wszystkie oceny studenta.
         * Przykładowy wydruk:
         * Podstawy programowania: 5.0
         * Programowanie obiektowe: 5.5
         *
         * @param index - numer indesku studenta dla którego wyświetlić oceny
         */
    public void printGradesForStudent(int index) {
        for (int i = 0; i < countGrades; i++) {
            if (grades[i].getStudent().getIndex() == index) {
                System.out.printf("%s: %.1f\n", grades[i].getGroup().getName(), grades[i].getGrade());
            }
        }
    }

        /**
         * Wyświetla oceny studentów dla wskazanej grupy.
         * Przykładowy wydruk:
         * 179128 Marcin Abacki: 5.0
         * 179234 Dawid Donald: 4.5
         * 189521 Anna Kowalska: 5.5
         *
         * @param groupCode - kod grupy, dla której wyświetlić oceny
         */
    public void printGradesForGroup(String groupCode) {
        Group group = checkGroupExists(groupCode);
        if (group != null) {
            for (int i = 0; i < countGrades; i++) {
                if (grades[i].getGroup().getCode().equals(groupCode)) {
                    System.out.printf("%d %s %s: %.1f\n", grades[i].getStudent().getIndex(),
                            grades[i].getStudent().getFirstName(), grades[i].getStudent().getLastName(),
                            grades[i].getGrade());
                }
            }
        } else {
            System.out.printf("Grupa %s nie istnieje\n", groupCode);
        }
    }

        /**
         * Wyświetla wszystkich studentów. Każdy student powinien zostać wyświetlony tylko raz.
         * Każdy student drukowany jest w nowej linii w formacie [nr_indesku] [imie] [nazwisko]
         * Przykładowy wydruk:
         * 179128 Marcin Abacki
         * 179234 Dawid Donald
         * 189521 Anna Kowalska
         */
    public void printAllStudents() {
        int[] indexes = new int[100];
        int indexCount = 0;
        for (int i = 0; i < countGroups; i++) {
            for (int j = 0; j < groups[i].getStudentCount(); j++) {
                boolean indexExists = false;
                for (int k = 0; k < indexCount; k++) {
                    if (indexes[k] == groups[i].getStudents()[j].getIndex()) {
                        indexExists = true;
                        break;
                    }
                }
                if (!indexExists) {
                    indexes[indexCount] = groups[i].getStudents()[j].getIndex();
                    indexCount++;
                    System.out.printf("%d %s %s\n", groups[i].getStudents()[j].getIndex(),
                            groups[i].getStudents()[j].getFirstName(), groups[i].getStudents()[j].getLastName());
                }
            }
        }
    }
}

