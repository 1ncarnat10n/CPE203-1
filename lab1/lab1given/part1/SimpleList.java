import java.util.List;
import java.util.LinkedList;

class SimpleList {
    public static List<Integer> squareAll(List<Integer> values) {
        List<Integer> newValues = new LinkedList<Integer>();
        for (int value: values) {
            newValues.add((int)Math.pow(value, 2));
        }
        return newValues;
    }
}
