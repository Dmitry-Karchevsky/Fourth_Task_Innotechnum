package fourth.task;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Stage {
    private final String name;
    private final BigDecimal budget;
    private final LocalDate endStageDate;

    private static BigDecimal allStagesBudget = new BigDecimal(0);

    public Stage(String name, BigDecimal budget, LocalDate endStageDate) {
        this.name = name;
        this.budget = budget;
        this.endStageDate = endStageDate;
        allStagesBudget = allStagesBudget.add(budget);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public LocalDate getEndStageDate() {
        return endStageDate;
    }

    public static BigDecimal getAllStagesBudget() {
        return allStagesBudget;
    }

    @Override
    public String toString() {
        return "Stage{" +
                "name='" + name + '\'' +
                ", budget=" + budget +
                ", endStageDate=" + endStageDate +
                '}' + "\n";
    }
}
