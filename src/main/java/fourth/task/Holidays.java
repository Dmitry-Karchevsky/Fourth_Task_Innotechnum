package fourth.task;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Holidays {
    // Коронавирус не считаем
    private static final Set<LocalDate> holidays = new HashSet<>(){{
        add(LocalDate.of(2020, 1, 1));
        add(LocalDate.of(2020, 1, 2));
        add(LocalDate.of(2020, 1, 3));
        add(LocalDate.of(2020, 1, 6));
        add(LocalDate.of(2020, 1, 7));
        add(LocalDate.of(2020, 1, 8));
        add(LocalDate.of(2020, 2, 24));
        add(LocalDate.of(2020, 3, 9));
        add(LocalDate.of(2020, 5, 1));
        add(LocalDate.of(2020, 5, 4));
        add(LocalDate.of(2020, 5, 5));
        add(LocalDate.of(2020, 5, 11));
        add(LocalDate.of(2020, 6, 12));
        add(LocalDate.of(2020, 6, 24)); //вроде выходной
        add(LocalDate.of(2020, 7, 1)); //вроде выходной
        add(LocalDate.of(2020, 11, 4));

        add(LocalDate.of(2021, 1, 1));
        add(LocalDate.of(2021, 1, 4));
        add(LocalDate.of(2021, 1, 5));
        add(LocalDate.of(2021, 1, 6));
        add(LocalDate.of(2021, 1, 7));
        add(LocalDate.of(2021, 1, 8));
        add(LocalDate.of(2021, 2, 23));
        add(LocalDate.of(2021, 3, 8));
        add(LocalDate.of(2021, 5, 3));
        add(LocalDate.of(2021, 5, 10));
        add(LocalDate.of(2021, 6, 14));
        add(LocalDate.of(2021, 11, 4));
    }};

    public static boolean isHoliday(LocalDate date) {
        return (holidays.contains(date));
    }
}
