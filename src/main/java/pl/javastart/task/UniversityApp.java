package pl.javastart.task;

public class UniversityApp {
    private Lecturer[] lecturers = new Lecturer[100];
    private Group[] groups = new Group[100];
    private int countLecturers = 0;
    private int countGroups = 0;

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
        for (int i = 0; i < countLecturers; i++) {
            if (lecturers[i].getId() == id) {
                System.out.printf("Prowadzący z id %d już istnieje\n", id);
                return;
            }
        }
        Lecturer lecturer = new Lecturer(id, degree, firstName, lastName);
        lecturers[countLecturers] = lecturer;
        countLecturers++;
        System.out.printf("Dodano prowadzącego: %s %s\n", lecturer.getFirstName(), lecturer.getLastName());
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
        boolean lecturerExists = false;
        Lecturer lecturer = null;
        for (int i = 0; i < countLecturers; i++) {
            if (lecturers[i].getId() == lecturerId) {
                lecturer = lecturers[i];
                lecturerExists = true;
                break;
            }
        }
        if (!lecturerExists) {
            System.out.printf("Prowadzący o id %d nie istnieje\n", lecturerId);
        }

        for (int i = 0; i < countGroups; i++) {
            if (groups[i].getCode().equals(code)) {
                System.out.printf("Grupa %s już istnieje\n", code);
                return;
            }
        }
        Group group = new Group(code, name, lecturer);
        groups[countGroups] = group;
        countGroups++;
        System.out.printf("Dodano grupę: %s\n", group.getName());
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
        boolean groupExists = false;
        Group group = null;
        for (int i = 0; i < countGroups; i++) {
            if (groups[i].getCode().equals(groupCode)) {
                group = groups[i];
                groupExists = true;
                break;
            }
        }
        if (!groupExists) {
            System.out.printf("Grupa %s nie istnieje", groupCode);
            return;
        }
        for (int i = 0; i < group.getStudentCount(); i++) {
            if (group.students[i].getIndex() == index) {
                System.out.printf("Student %s %s został już wcześniej dodany do grupy: %s\n",
                        firstName, lastName, group.getName());
                return;
            }
        }
        Student student = new Student(index, group, firstName, lastName);
        group.students[group.getStudentCount()] = student;
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
        boolean groupExists = false;
        Group group = null;
        for (int i = 0; i < countGroups; i++) {
            if (groups[i].getCode().equals(groupCode)) {
                group = groups[i];
                groupExists = true;
                break;
            }
        }
        if (!groupExists) {
            System.out.printf("Grupa %s nie znaleziona\n", groupCode);
            return;
        }
        System.out.printf("Kod: %s\n", group.getCode());
        System.out.printf("Nazwa: %s\n", group.getName());
        System.out.printf("Prowadzący: %s %s %s\n", group.getLecturer().getDegree(),
                group.getLecturer().getFirstName(), group.getLecturer().getLastName());
        System.out.println("Uczestnicy:");
        for (int i = 0; i < group.getStudentCount(); i++) {
            System.out.printf("%s %s %s\n", group.students[i].getIndex(), group.students[i].getFirstName(),
                    group.students[i].getLastName());
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
        boolean groupExists = false;
        Group group = null;
        for (int i = 0; i < countGroups; i++) {
            if (groups[i].getCode().equals(groupCode)) {
                group = groups[i];
                groupExists = true;
                break;
            }
        }
        if (!groupExists) {
            System.out.printf("Grupa %s nie istnieje\n", groupCode);
            return;
        }

        boolean studentExists = false;
        Student student = null;
        for (int i = 0; i < group.getStudentCount(); i++) {
            if (group.students[i].getIndex() == studentIndex) {
                studentExists = true;
                student = group.students[i];
                break;
            }
        }
        if (!studentExists) {
            System.out.printf("Student o indeksie %d nie jest zapisany do grupy %s", studentIndex, groupCode);
            return;
        }

        if (student.getGrade() != 0) {
            System.out.printf("Student o indeksie %d ma już wystawioną ocenę dla grupy %s\n", studentIndex,
                    groupCode);
            return;
        }

        student.setGrade(grade);
        System.out.printf("Dodano ocenę %.1f dla studenta o indeksie %d w grupie %s\n", grade, studentIndex,
                groupCode);
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
        for (int i = 0; i < countGroups; i++) {
            for (int j = 0; j < groups[i].getStudentCount(); j++) {
                if (groups[i].students[j].getIndex() == index) {
                    System.out.printf("%s: %.1f\n", groups[i].getName(), groups[i].students[j].getGrade());
                }
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
        boolean groupExists = false;
        Group group = null;
        for (int i = 0; i < countGroups; i++) {
            if (groups[i].getCode().equals(groupCode)) {
                group = groups[i];
                groupExists = true;
                break;
            }
        }
        if (groupExists) {
            for (int i = 0; i < group.getStudentCount(); i++) {
                System.out.printf("%d %s %s: %.1f\n", group.students[i].getIndex(), group.students[i].getFirstName(),
                        group.students[i].getLastName(), group.students[i].getGrade());
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
                    if (indexes[k] == groups[i].students[j].getIndex()) {
                        indexExists = true;
                        break;
                    }
                }
                if (!indexExists) {
                    indexes[indexCount] = groups[i].students[j].getIndex();
                    indexCount++;
                    System.out.printf("%d %s %s\n", groups[i].students[j].getIndex(),
                            groups[i].students[j].getFirstName(), groups[i].students[j].getLastName());
                }
            }
        }
    }
}

