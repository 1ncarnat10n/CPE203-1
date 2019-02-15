import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class ExampleMap {
   public static List<String> highEnrollmentStudents( Map<String, List<Course>> courseListsByStudentName, int unitThreshold ) {
         List<String> overEnrolledStudents = new LinkedList<>();
         for (String studentName: courseListsByStudentName.keySet()) {
            int units = 0;
            for (Course course: courseListsByStudentName.get(studentName)) {
              units += course.getNumUnits(); 
            }
            if (units > unitThreshold) {
              overEnrolledStudents.add(studentName);
            }
         }
      return overEnrolledStudents;
   }
}
