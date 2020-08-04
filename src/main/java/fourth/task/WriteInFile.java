package fourth.task;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class WriteInFile {
    private static String infoNewMonth(LocalDate dateInfo, BigDecimal money, BigDecimal allMoney, int days){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dateInfo.getMonth())
                .append(".")
                .append(dateInfo.getYear())
                .append("\t|\t")
                .append(money)
                .append("\t|\t")
                .append(allMoney)
                .append("\t|\t")
                .append(days)
                .append("\n");
        return stringBuilder.toString();
    }

    private static String infoNewStage(Stage stageInfo,  LocalDate endDate){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(stageInfo.getName())
                .append("\t|\t")
                .append(stageInfo.getBudget())
                .append("\t|\t")
                .append(stageInfo.getEndStageDate())
                .append("\t|\t")
                .append(endDate)
                .append("\t|\t")
                .append(Period.calculateWorkingDays(stageInfo.getEndStageDate(), endDate))
                .append("\n");
        return stringBuilder.toString();
    }

    public static void writeAboutMonths(String fileName, List<Period> listPeriod) {
        try(FileWriter writer = new FileWriter(fileName, false))
        {
            StringBuilder stringBuilder = new StringBuilder("Месяц\t|\tСумма\t|\tСумма накопительно\t|\tКол-во рабочих дней\n");
            BigDecimal sum = new BigDecimal(0);
            BigDecimal monthSum;
            int workingDaysSum;
            int i = 0;
            while(i != listPeriod.size()){
                LocalDate tempDate;
                monthSum = new BigDecimal(0);
                workingDaysSum = 0;
                tempDate = listPeriod.get(i).getStartPeriod();
                while (i != listPeriod.size() && tempDate.getMonth().equals(listPeriod.get(i).getMonth())) {
                    workingDaysSum += listPeriod.get(i).getCountOfWorkingDays();
                    monthSum = monthSum.add(listPeriod.get(i).getAllSalaryInPeriod());
                    i++;
                }
                sum = sum.add(monthSum);
                stringBuilder.append(infoNewMonth(tempDate, monthSum, sum, workingDaysSum));
            }
            writer.write(stringBuilder.toString());
            writer.flush();
        } catch (IOException e) {
            System.out.printf("Недопустимое имя файла для вывода: %s\n", fileName);
        }
    }

    public static void writeAboutStages(String fileName, List<Stage> listStage, List<Period> listPeriod) {
        try(FileWriter writer = new FileWriter(fileName, false))
        {
            StringBuilder stringBuilder = new StringBuilder("Название\t|\tСумма этапа\t|\tПлановый срок завершения\t|\tФактический срок\t|\tРасхождение\n");
            BigDecimal sum = new BigDecimal(0);
            BigDecimal budget = listStage.get(0).getBudget();
            int stageIndex = 0;
            int peridIndex = 0;
            while (stageIndex != listStage.size()){
                if (budget.compareTo(sum) <= 0){
                    stringBuilder.append(infoNewStage(listStage.get(stageIndex), Period.getDayBudgetEnd(listPeriod.get(peridIndex - 1), sum.subtract(budget))));
                    stageIndex++;
                    if (stageIndex != listStage.size())
                        budget = budget.add(listStage.get(stageIndex).getBudget());
                }
                if (peridIndex != listPeriod.size())
                    sum = sum.add(listPeriod.get(peridIndex).getAllSalaryInPeriod());
                peridIndex++;
            }
            writer.write(stringBuilder.toString());
            writer.flush();
        } catch (IOException e) {
            System.out.printf("Недопустимое имя файла для вывода: %s\n", fileName);
        }
    }
}
