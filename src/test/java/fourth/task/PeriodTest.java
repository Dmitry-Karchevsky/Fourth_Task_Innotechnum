package fourth.task;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class PeriodTest {

    @Test
    void calculateWorkingDays() {
        LocalDate dateStart1 = LocalDate.of(2020, 12, 31);
        LocalDate dateEnd1 = LocalDate.of(2020, 8, 12);

        LocalDate dateStart2 = LocalDate.of(2021, 1, 4);
        LocalDate dateEnd2 = LocalDate.of(2021, 6, 7);

        int days = Period.calculateWorkingDays(dateStart1, dateEnd1);
        int days2 = Period.calculateWorkingDays(dateStart2, dateEnd2);
        System.out.println(days);
        System.out.println(days2);
    }

    @Test
    void getDayBudgetEnd() {
        BigDecimal budget1 = new BigDecimal(9000);
        //BigDecimal budget1 = new BigDecimal(8900);
        BigDecimal budget = new BigDecimal(13000);
        //BigDecimal budget = new BigDecimal(13100);
        Period period = new Period(Month.APRIL, new BigDecimal(1000),
                LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30));
        LocalDate endDay = Period.getDayBudgetEnd(period, budget1);

        LocalDate start = period.getStartPeriod();
        while(budget.compareTo(period.getAllSalaryInDay()) > 0){
            if (!start.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                    && !start.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                    && !Holidays.isHoliday(start))
                budget = budget.subtract(period.getAllSalaryInDay());
            start = start.plusDays(1);
        }
        while (start.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                || start.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                || Holidays.isHoliday(start))
            start = start.plusDays(1);

        System.out.println(start);
        System.out.println(endDay);
    }

    @Test
    void createPeriodsList() {
    }

    @Test
    void getAllSalaryInDay() {
    }

    @Test
    void getCountOfWorkingDays() {
    }
}