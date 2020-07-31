package fourth.task;

import java.util.List;

public class ProjectMain {
    public static void main(String[] args) {
        List<Person> personList = ReadFile.readPersonsInfoInList("src\\main\\resources\\PersonsInfo.txt");
        List<Stage> stagesList = ReadFile.readStagesInfoInList("src\\main\\resources\\StagesInfo.txt");

        List<Period> periodList = Period.createPeriodsList(personList, Stage.getAllStagesBudget());

        WriteInFile.writeAboutMonths("src\\main\\resources\\OutInfoAboutMonth.txt", periodList);
        WriteInFile.writeAboutStages("src\\main\\resources\\OutInfoAboutStages.txt", stagesList, periodList);
    }
}
