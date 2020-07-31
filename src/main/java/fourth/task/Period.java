package fourth.task;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Period {
    private Month month;
    private BigDecimal allSalaryInDay;
    private BigDecimal allSalaryInPeriod;
    private LocalDate startPeriod;
    private LocalDate endPeriod;
    private int countOfWorkingDays;

    private static BigDecimal allPeriodsSum = new BigDecimal(0);

    public Period(Month month, BigDecimal allSalaryInDay, LocalDate startPeriod, LocalDate endPeriod) {
        this.month = month;
        this.allSalaryInDay = allSalaryInDay;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        countOfWorkingDays = calculateWorkingDays(this.startPeriod, this.endPeriod);
        allSalaryInPeriod = allSalaryInDay.multiply(new BigDecimal(countOfWorkingDays));
        allPeriodsSum = allPeriodsSum.add(allSalaryInPeriod);
    }

    public Period(Month month, BigDecimal allSalaryInDay, LocalDate startPeriod) {
        this.month = month;
        this.allSalaryInDay = allSalaryInDay;
        this.startPeriod = startPeriod;
    }

    public int calculateWorkingDays(LocalDate startDate, LocalDate endDate){
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

    private static BigDecimal getSalaryFromList(List<Person> personList){
        BigDecimal result = new BigDecimal(0);
        for (Person person : personList){
            result = result.add(person.getSalaryInDay());
        }
        return result;
    }

    private static List<Person> createRemoveList(List<Person> list){
        List<Person> removeList = new ArrayList<>();
        for (Person person : list){
            if (person.getEndDate() != null)
                removeList.add(person);
        }
        Collections.sort(removeList, Person.Comparators.END_DATE);
        return removeList;
    }

    private static LocalDate getDayBudgetEnd(Period period, BigDecimal budget){
        LocalDate end = period.getEndPeriod();
        while(budget.compareTo(period.getAllSalaryInDay()) > 0){
            budget = budget.subtract(period.getAllSalaryInDay());
            end = end.minusDays(1);
        }
        return end.plusDays(1);
    }

    public static List<Period> createPeriodsList(List<Person> personList, BigDecimal budget){
        Collections.sort(personList);
        List<Period> periodList = new ArrayList<>();
        List<Person> removePersonList = createRemoveList(personList);
        LocalDate startDate = personList.get(0).getBeginDate();
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        LocalDate tempDateToAdd;
        LocalDate tempDateToRemove;

        int i = 1;
        while(i < personList.size() || !removePersonList.isEmpty() || getAllPeriodsSum().compareTo(budget) < 0) {
            System.out.println(getAllPeriodsSum());
            if (i == personList.size() && !removePersonList.isEmpty() && getAllPeriodsSum().compareTo(budget) < 0) {
                tempDateToRemove = removePersonList.get(0).getEndDate();
                if (tempDateToRemove.equals(startDate)) {
                    personList.remove(removePersonList.get(0));
                    removePersonList.remove(0);
                    i--;
                } else if (tempDateToRemove.isAfter(startDate) && tempDateToRemove.isBefore(endDate.plusDays(1))) {
                    periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, personList.size())), startDate, tempDateToRemove.minusDays(1)));
                    startDate = tempDateToRemove;
                    personList.remove(removePersonList.get(0));
                    removePersonList.remove(0);
                    i--;
                } else {
                    periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, personList.size())), startDate, endDate));
                    startDate = endDate.plusDays(1);
                    endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                }
            } else if (i < personList.size() && getAllPeriodsSum().compareTo(budget) < 0){
                tempDateToAdd = personList.get(i).getBeginDate();
                tempDateToRemove = removePersonList.get(0).getEndDate();
                if (tempDateToAdd.isBefore(tempDateToRemove)) {
                    if (tempDateToAdd.equals(startDate)) {
                        i++;
                    } else if (tempDateToAdd.isAfter(startDate) && tempDateToAdd.isBefore(endDate.plusDays(1))) {
                        periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, i)), startDate, tempDateToAdd.minusDays(1)));
                        startDate = tempDateToAdd;
                        i++;
                    } else {
                        periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, i)), startDate, endDate));
                        startDate = endDate.plusDays(1);
                        endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                    }
                } else {
                    if (tempDateToRemove.equals(startDate)) {
                        personList.remove(removePersonList.get(0));
                        removePersonList.remove(0);
                        i--;
                    } else if (tempDateToRemove.isAfter(startDate) && tempDateToRemove.isBefore(endDate.plusDays(1))) {
                        periodList.add(new Period(tempDateToRemove.getMonth(), getSalaryFromList(personList.subList(0, i)), startDate, tempDateToRemove.minusDays(1)));
                        personList.remove(removePersonList.get(0));
                        removePersonList.remove(0);
                        startDate = tempDateToRemove;
                        i--;
                    } else {
                        periodList.add(new Period(tempDateToRemove.getMonth(), getSalaryFromList(personList.subList(0, i)), startDate, endDate));
                        startDate = endDate.plusDays(1);
                        endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                    }
                }
            }
            else if (getAllPeriodsSum().compareTo(budget) < 0){
                periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, personList.size())), startDate, endDate));
                startDate = endDate.plusDays(1);
                endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            }

            // тут смотреть бюджет
            if (getAllPeriodsSum().compareTo(budget) > 0){
                LocalDate endBudget = getDayBudgetEnd(periodList.get(periodList.size() - 1), getAllPeriodsSum().subtract(budget));
                Period tempPeriod = periodList.get(periodList.size() - 1);
                periodList.remove(periodList.size() - 1);
                periodList.add(new Period(tempPeriod.getMonth(), tempPeriod.getAllSalaryInDay(), tempPeriod.getStartPeriod(), endBudget));
            }
        }

        return periodList;
    }

    public Month getMonth() {
        return month;
    }

    public BigDecimal getAllSalaryInDay() {
        return allSalaryInDay;
    }

    public LocalDate getStartPeriod() {
        return startPeriod;
    }

    public LocalDate getEndPeriod() {
        return endPeriod;
    }

    public int getCountOfWorkingDays() {
        return countOfWorkingDays;
    }

    public BigDecimal getAllSalaryInPeriod() {
        return allSalaryInPeriod;
    }

    public static BigDecimal getAllPeriodsSum() {
        return allPeriodsSum;
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
