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
                .append("\t")
                .append(money)
                .append("\t")
                .append(allMoney)
                .append("\t")
                .append(days)
                .append("\n");
        return stringBuilder.toString();
    }

    public static void writeAboutMonths(String fileName, List<Period> listPeriod, BigDecimal budget) {
        try(FileWriter writer = new FileWriter(fileName, false))
        {
            StringBuilder stringBuilder = new StringBuilder();
            BigDecimal sum = new BigDecimal(0);
            BigDecimal monthSum;
            int workingDaysSum;
            int i = 0;
            while(i != listPeriod.size()){
                LocalDate tempDate;
                monthSum = new BigDecimal(0);
                workingDaysSum = 0;
                tempDate = listPeriod.get(i).getStartPeriod();
                //ПОСМОТРЕТЬ УСЛОВИЕ
                while (i != listPeriod.size() && tempDate.getMonth().equals(listPeriod.get(i).getMonth())) {
                    workingDaysSum += listPeriod.get(i).getCountOfWorkingDays();
                    monthSum = listPeriod.get(i).getAllSalaryInPeriod();
                    i++;
                }

                sum = sum.add(monthSum);
                //stringBuilder.append(infoNewMonth(tempDate, monthSum, sum, workingDaysSum));
                budget = budget.subtract(sum);
            }
            writer.write(stringBuilder.toString());
            writer.flush();
        } catch (IOException e) {
            System.out.printf("Недопустимое имя файла для вывода: %s\n", fileName);
        }
    }

    public static void writeAboutStages(String fileName, List<String> list) {
        try(FileWriter writer = new FileWriter(fileName, false))
        {

            writer.flush();
        } catch (IOException e) {
            System.out.printf("Недопустимое имя файла для вывода: %s\n", fileName);
        }
    }
}
