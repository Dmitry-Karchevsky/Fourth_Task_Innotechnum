package fourth.task;

import java.math.BigDecimal;
import java.util.List;

public class ProjectMain {
    public static void main(String[] args) {
        List<Person> list = ReadFile.readPersonsInfoInList("src\\main\\resources\\PersonsInfo.txt");
        List<Stage> list2 = ReadFile.readStagesInfoInList("src\\main\\resources\\StagesInfo.txt");
        //System.out.println(list);
        List<Period> periodList = Period.createPeriodsList(list, new BigDecimal(1500000));

        System.out.println(periodList);
    }
}
