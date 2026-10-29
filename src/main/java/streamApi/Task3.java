package streamApi;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Task3 {

    public static void main(String[] args) {
        Task3 task = new Task3();
        task.test();
    }

    enum DayOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    static class WorkSheet {
        String name;
        List<DayOfWeek> workDays;

        public WorkSheet(String name, List<DayOfWeek> workDays) {
            this.name = name;
            this.workDays = workDays;
        }

        public String getName() {
            return name;
        }

        public List<DayOfWeek> getWorkDays() {
            return workDays;
        }
    }

    void test() {
        var denSheet = new WorkSheet("Денис", List.of(DayOfWeek.TUESDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SUNDAY));
        var benSheet = new WorkSheet("Бен", List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY));
        var lisSheet = new WorkSheet("Лиза", List.of(DayOfWeek.THURSDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        var sheets = List.of(denSheet, benSheet, lisSheet);
        // TODO: Необходимо вывести на экран дни в которые работало более 1 человека
        // sheets.stream()
        // .forEach(System.out::println);

        sheets.stream()
                .flatMap(sheet -> sheet.getWorkDays().stream().distinct())
                .collect(Collectors.groupingBy(
                        day -> day,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .forEach(System.out::println);


//        I think this version is easier to read
//        var res = sheets.stream()
//                .flatMap(sheet -> sheet.getWorkDays().stream().distinct())
//                .collect(Collectors.groupingBy(
//                        day -> day,
//                        Collectors.counting()
//                ));
//
//        res.entrySet().stream()
//                .filter(entry -> entry.getValue() > 1)
//                .map(Map.Entry::getKey)
//                .forEach(System.out::println);


    }
}
