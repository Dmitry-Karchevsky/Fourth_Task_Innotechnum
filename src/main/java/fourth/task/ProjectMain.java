package fourth.task;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectMain {
    public static void main(String[] args) {
        List<Person> list = ReadFile.readPersonsInfoInList("src\\main\\resources\\PersonsInfo.txt");
        List<Stage> list2 = ReadFile.readStagesInfoInList("src\\main\\resources\\StagesInfo.txt");
        System.out.println(list);
        List<Period> periodList = createPeriodsList(list);
        System.out.println(periodList);
    }

    public static BigDecimal getSalaryFromList(List<Person> personList){
        BigDecimal result = new BigDecimal(0);
        for (Person person : personList){
            result = result.add(person.getSalaryInDay());
        }
        return result;
    }

    public static List<Person> createRemoveList(List<Person> list){
        List<Person> removeList = new ArrayList<>();
        for (Person person : list){
            if (person.getEndDate() != null)
                removeList.add(person);
        }
        Collections.sort(removeList, Person.Comparators.END_DATE);
        return removeList;
    }

    public static List<Period> createPeriodsList(List<Person> personList){
        Collections.sort(personList);
        List<Period> periodList = new ArrayList<>();
        List<Person> removePersonList = createRemoveList(personList);
        LocalDate startDate = personList.get(0).getBeginDate();
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        LocalDate tempDateToAdd;
        LocalDate tempDateToRemove;

        int i = 1;
        while(i < personList.size() || !removePersonList.isEmpty()){
            tempDateToRemove = removePersonList.get(0).getEndDate();
            if (i == personList.size()){
                while (!removePersonList.isEmpty()){
                    tempDateToRemove = removePersonList.get(0).getEndDate();
                    if (tempDateToRemove.equals(startDate)) {
                        personList.remove(removePersonList.get(0));
                        removePersonList.remove(0);
                    }
                    else if (tempDateToRemove.isAfter(startDate) && tempDateToRemove.isBefore(endDate.plusDays(1))) {
                        periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, personList.size())), startDate, tempDateToRemove.minusDays(1)));
                        startDate = tempDateToRemove;
                        personList.remove(removePersonList.get(0));
                        removePersonList.remove(0);
                    } else {
                        periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, personList.size())), startDate, endDate));
                        startDate = endDate.plusDays(1);
                        endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                    }
                }
                break;
            }

            tempDateToAdd = personList.get(i).getBeginDate();

            if (tempDateToAdd.isBefore(tempDateToRemove)) {
                if (tempDateToAdd.equals(startDate)) {
                    i++;
                }
                else if (tempDateToAdd.isAfter(startDate) && tempDateToAdd.isBefore(endDate.plusDays(1))) {
                    periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, i)), startDate, tempDateToAdd.minusDays(1)));
                    startDate = tempDateToAdd;
                    i++;
                } else {
                    periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, i)), startDate, endDate));
                    startDate = endDate.plusDays(1);
                    endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                }
            }
            else {
                if (tempDateToRemove.equals(startDate)){
                    i--;
                    personList.remove(removePersonList.get(0));
                    removePersonList.remove(0);
                    continue;
                }
                if (tempDateToRemove.isAfter(startDate) && tempDateToRemove.isBefore(endDate.plusDays(1))) {
                    periodList.add(new Period(tempDateToRemove.getMonth(), getSalaryFromList(personList.subList(0, i)), startDate, tempDateToRemove.minusDays(1)));
                    personList.remove(removePersonList.get(0));
                    removePersonList.remove(0);
                    startDate = tempDateToRemove;
                    i--;
                }
                else {
                    periodList.add(new Period(tempDateToRemove.getMonth(), getSalaryFromList(personList.subList(0, i)), startDate, endDate));
                    startDate = endDate.plusDays(1);
                    endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                }
            }
        }
        periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, personList.size())), startDate));
        return periodList;
    }
}
