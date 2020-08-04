package fourth.task;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Period {
    private final Month month;
    private final BigDecimal allSalaryInDay;
    private final BigDecimal allSalaryInPeriod;
    private final LocalDate startPeriod;
    private final LocalDate endPeriod;
    private final int countOfWorkingDays;

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

    public static int calculateWorkingDays(LocalDate startDate, LocalDate endDate){
        int workingDays = 0;
        boolean isMinus = false;
        if (startDate.isAfter(endDate)){
            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
            isMinus = true;
        }
        while (!startDate.isAfter(endDate)){
            if (!startDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                    && !startDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                    && !Holidays.isHoliday(startDate))
                workingDays++;
            startDate = startDate.plusDays(1);
        }
        if (isMinus)
            return -workingDays;
        return workingDays;
    }

    public static LocalDate getDayBudgetEnd(Period period, BigDecimal budget){
        LocalDate end = period.getEndPeriod();
        while(budget.compareTo(period.getAllSalaryInDay()) >= 0){
            if (!end.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                    && !end.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                    && !Holidays.isHoliday(end))
                budget = budget.subtract(period.getAllSalaryInDay());
            end = end.minusDays(1);
        }
        while (end.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                || end.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                || Holidays.isHoliday(end))
            end = end.minusDays(1);
        return end;
    }

    private static List<Period> addPeriodWithOutRemove(List<Person> personList, BigDecimal budget){
        List<Period> periodList = new ArrayList<>();
        LocalDate startDate = personList.get(0).getBeginDate();
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        LocalDate tempDateToAdd;

        int i = 0;
        while(getAllPeriodsSum().compareTo(budget) < 0) {
            if (i < personList.size() && getAllPeriodsSum().compareTo(budget) < 0){
                tempDateToAdd = personList.get(i).getBeginDate();
                // Если есть работники, которых нужно добавить
                if (tempDateToAdd.equals(startDate)) {
                    i++;
                } else if (tempDateToAdd.isAfter(startDate) && tempDateToAdd.isBefore(endDate.plusDays(1))) {
                    periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, i + 1)), startDate, tempDateToAdd.minusDays(1)));
                    startDate = tempDateToAdd;
                    i++;
                } else {
                    periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, i + 1)), startDate, endDate));
                    startDate = endDate.plusDays(1);
                    endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                }
            }
            // Если работники работают, но деньги в бюджете еще есть (не все этапы закончены)
            else if (getAllPeriodsSum().compareTo(budget) < 0){
                periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, personList.size())), startDate, endDate));
                startDate = endDate.plusDays(1);
                endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            }

            // Тут смотреть бюджет
            if (getAllPeriodsSum().compareTo(budget) > 0){
                LocalDate endBudget = getDayBudgetEnd(periodList.get(periodList.size() - 1), getAllPeriodsSum().subtract(budget));
                Period tempPeriod = periodList.get(periodList.size() - 1);
                periodList.remove(periodList.size() - 1);
                periodList.add(new Period(tempPeriod.getMonth(), tempPeriod.getAllSalaryInDay(), tempPeriod.getStartPeriod(), endBudget));
            }
        }
        return periodList;
    }

    private static List<Period> addPeriodWithRemove(List<Person> personList, BigDecimal budget, List<Person> removePersonList){
        List<Period> periodList = new ArrayList<>();
        LocalDate startDate = personList.get(0).getBeginDate();
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        LocalDate tempDateToAdd;
        LocalDate tempDateToRemove;

        int i = 0;
        // Выполняется пока есть деньги в бюджете, по сравнению с суммой всех денег за периоды
        while(getAllPeriodsSum().compareTo(budget) < 0) {
            // Если всех работников добавили, но не все уволились с проекта
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
            }
            // Если есть работники, которых нужно добавить
            else if (i < personList.size() && getAllPeriodsSum().compareTo(budget) < 0){
                tempDateToAdd = personList.get(i).getBeginDate();
                tempDateToRemove = removePersonList.get(0).getEndDate();
                if (tempDateToAdd.isBefore(tempDateToRemove)) {
                    if (tempDateToAdd.equals(startDate)) {
                        i++;
                    } else if (tempDateToAdd.isAfter(startDate) && tempDateToAdd.isBefore(endDate.plusDays(1))) {
                        periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, i + 1)), startDate, tempDateToAdd.minusDays(1)));
                        startDate = tempDateToAdd;
                        i++;
                    } else {
                        periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, i + 1)), startDate, endDate));
                        startDate = endDate.plusDays(1);
                        endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                    }
                } else {
                    if (tempDateToRemove.equals(startDate)) {
                        personList.remove(removePersonList.get(0));
                        removePersonList.remove(0);
                        i--;
                    } else if (tempDateToRemove.isAfter(startDate) && tempDateToRemove.isBefore(endDate.plusDays(1))) {
                        periodList.add(new Period(tempDateToRemove.getMonth(), getSalaryFromList(personList.subList(0, i + 1)), startDate, tempDateToRemove.minusDays(1)));
                        personList.remove(removePersonList.get(0));
                        removePersonList.remove(0);
                        startDate = tempDateToRemove;
                        i--;
                    } else {
                        periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, i + 1)), startDate, endDate));
                        startDate = endDate.plusDays(1);
                        endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                    }
                }
            }
            // Если работники работают, но деньги в бюджете еще есть (не все этапы закончены)
            else if (getAllPeriodsSum().compareTo(budget) < 0){
                periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, personList.size())), startDate, endDate));
                startDate = endDate.plusDays(1);
                endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            }

            // Тут смотреть бюджет
            if (getAllPeriodsSum().compareTo(budget) > 0){
                LocalDate endBudget = getDayBudgetEnd(periodList.get(periodList.size() - 1), getAllPeriodsSum().subtract(budget));
                Period tempPeriod = periodList.get(periodList.size() - 1);
                periodList.remove(periodList.size() - 1);
                periodList.add(new Period(tempPeriod.getMonth(), tempPeriod.getAllSalaryInDay(), tempPeriod.getStartPeriod(), endBudget));
            }
        }
        return periodList;
    }

    public static List<Period> createPeriodsList(List<Person> personList, BigDecimal budget){
        Collections.sort(personList);
        List<Person> removePersonList = createRemoveList(personList);
        List<Period> periodList;

        if (removePersonList.isEmpty())
            periodList = addPeriodWithOutRemove(personList, budget);
        else
            periodList = addPeriodWithRemove(personList, budget, removePersonList);
        return periodList;
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
