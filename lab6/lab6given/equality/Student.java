import java.util.List;
import java.util.Objects;

class Student {
    private final String surname;
    private final String givenName;
    private final int age;
    private final List<CourseSection> currentCourses;

    public Student(final String surname, final String givenName, final int age, final List<CourseSection> currentCourses) {
        this.surname = surname;
        this.givenName = givenName;
        this.age = age;
        this.currentCourses = currentCourses;
    }

    public boolean equals(Object other) {
        boolean nullsEqual = true;
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other.getClass() != getClass()) {
            return false;
        }

        Student test = (Student)other;
        return isEqual(surname, test.surname) && isEqual(givenName, test.givenName) && age == ((Student) other).age && isEqual(currentCourses, test.currentCourses);
    }

    public int hashCode() {
        return Objects.hash(surname, givenName, age, currentCourses);
    }

    private boolean isEqual(Object o1, Object o2) {
        return o1 == o2 || (o1 != null && o2 != null && o1.equals(o2));
    }
}
