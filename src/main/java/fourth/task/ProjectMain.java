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

        for (int i = 1; i < personList.size(); i++) {
            //if (personList.get(i).getEndDate() == null){
                LocalDate tempDateToAdd = personList.get(i).getBeginDate();

                if (tempDateToAdd.equals(startDate))
                    continue;
                if (tempDateToAdd.isAfter(startDate) && tempDateToAdd.isBefore(endDate.plusDays(1))){
                    periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, i)), startDate, tempDateToAdd.minusDays(1)));
                    startDate = tempDateToAdd;
                }
                else{
                    periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, i)), startDate, endDate));
                    startDate = endDate.plusDays(1);
                    endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                    i--;
                }
            //}
        }
        periodList.add(new Period(startDate.getMonth(), getSalaryFromList(personList.subList(0, personList.size())), startDate));
        return periodList;
    }
}
