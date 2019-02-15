import java.time.LocalTime;

class CourseSection {
    private final String prefix;
    private final String number;
    private final int enrollment;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public CourseSection(final String prefix, final String number, final int enrollment, final LocalTime startTime, final LocalTime endTime) {
        this.prefix = prefix;
        this.number = number;
        this.enrollment = enrollment;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other.getClass() != getClass()) {
            return false;
        }
        CourseSection test = (CourseSection)other;
        return isEqual(prefix, test.prefix) && isEqual(number, test.number) && enrollment == test.enrollment && isEqual(startTime, test.startTime) && isEqual(endTime, test.endTime);
    }

    public int hashCode() {
        int hash = 1301081;
        if (prefix != null) {
            hash += prefix.hashCode();
        }
        if (number != null) {
            hash += number.hashCode();
        }
        hash += enrollment;
        if (startTime != null) {
            hash += startTime.hashCode();
        }
        if (endTime != null) {
            hash += endTime.hashCode();
        }
        return hash;
    }

    private boolean isEqual(Object o1, Object o2) {
        return o1 == o2 || (o1 != null && o2 != null && o1.equals(o2));
    }
}
