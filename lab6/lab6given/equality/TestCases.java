import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class TestCases
{
    @Test
    public void testExercise1()
    {
        final CourseSection one = new CourseSection("CSC", "203", 35,
            LocalTime.of(9, 40), LocalTime.of(11, 0));
        final CourseSection two = new CourseSection("CSC", "203", 35,
            LocalTime.of(9, 40), LocalTime.of(11, 0));

        assertTrue(one.equals(two));
        assertTrue(two.equals(one));
    }

    @Test
    public void testExercise2()
    {
        final CourseSection one = new CourseSection("CSC", "203", 35,
            LocalTime.of(9, 10), LocalTime.of(10, 0));
        final CourseSection two = new CourseSection("CSC", "203", 35,
            LocalTime.of(1, 10), LocalTime.of(2, 0));

        assertFalse(one.equals(two));
        assertFalse(two.equals(one));
    }

    @Test
    public void testExercise3()
    {
        final CourseSection one = new CourseSection("CSC", "203", 35,
            LocalTime.of(9, 40), LocalTime.of(11, 0));
        final CourseSection two = new CourseSection("CSC", "203", 35,
            LocalTime.of(9, 40), LocalTime.of(11, 0));

        assertEquals(one.hashCode(), two.hashCode());
    }

    @Test
    public void testExercise4()
    {
        final CourseSection one = new CourseSection("CSC", "203", 35,
            LocalTime.of(9, 10), LocalTime.of(10, 0));
        final CourseSection two = new CourseSection("CSC", "203", 34,
            LocalTime.of(9, 10), LocalTime.of(10, 0));

        assertNotEquals(one.hashCode(), two.hashCode());
    }
    @Test
    public void testExercise5()
    {
        final CourseSection one = new CourseSection(null, null, 0,
            null, null);
        final CourseSection two = new CourseSection(null, null, 0,
            null, null);

//        assertTrue(one.equals(two));
    }

    @Test
    public void testStudent0(){
        List<CourseSection> currentCourse = new LinkedList<CourseSection>();
        final Student one = new Student("John", "Doe", 3, currentCourse);
        final Student two = new Student("John", "Doe", 3, currentCourse);

        assertTrue(one.equals(two));
    }
}
