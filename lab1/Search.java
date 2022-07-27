public class Search {
    public static Object Linear_Search (Object[] data, Object target) {
        for (Object i : data) {
            if (i.equals(target)) {
                return i;
            }
        }
        return null;
    }
}
