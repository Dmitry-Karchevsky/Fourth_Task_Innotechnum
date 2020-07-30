package fourth.task;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public class Period {
    private Month month;
    private BigDecimal allSalaryInDay;
    private LocalDate startPeriod;
    private LocalDate endPeriod;
    private int countOfWorkingDays;

    public Period(Month month, BigDecimal allSalaryInDay, LocalDate startPeriod, LocalDate endPeriod) {
        this.month = month;
        this.allSalaryInDay = allSalaryInDay;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        countOfWorkingDays = calculateWorkingDays(this.startPeriod, this.endPeriod);
    }

    public Period(Month month, BigDecimal allSalaryInDay, LocalDate startPeriod) {
        this.month = month;
        this.allSalaryInDay = allSalaryInDay;
        this.startPeriod = startPeriod;
    }

    private int calculateWorkingDays(LocalDate startDate, LocalDate endDate){
        int workingDays = 0;
        while (!startDate.isAfter(endDate)){
            if (!startDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                    && !startDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                    && !Holidays.isHoliday(startDate))
                workingDays++;
            startDate = startDate.plusDays(1);
        }
        return workingDays;
    }

    @Override
    public String toString() {
        return "Period{" +
                "month=" + month +
                ", allSalaryInDay=" + allSalaryInDay +
                ", startPeriod=" + startPeriod +
                ", endPeriod=" + endPeriod +
                ", countOfWorkingDays=" + countOfWorkingDays +
                '}' + " \n";
    }
}
