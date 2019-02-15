class BetterLoop {
    public static boolean contains(int[] values, int v) {
        for (int value: values) {
            if (value == v) {
                return true;
            }
        }
        return false;
   }
}
