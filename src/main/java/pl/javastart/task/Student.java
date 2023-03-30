package pl.javastart.task;

public class Student {
    private int index;
    private Group group;
    private String firstName;
    private String lastName;

    public Student(int index, Group group, String firstName, String lastName) {
        this.index = index;
        this.group = group;
        this.firstName = firstName;
        this.lastName = lastName;
    }



    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}


