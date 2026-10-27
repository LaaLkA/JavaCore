package task2;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Task2 {

    public static <T> Map<T, Integer> countOccurrences(T[] arr) {

        Objects.requireNonNull(arr, "Array must not be null");

        Map<T, Integer> res = new LinkedHashMap<>();
        for (T elem: arr) {
            res.merge(elem, 1, Integer::sum);
        }

        return res;
    }
}
