package fourth.task;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Stage {
    private String name;
    private BigDecimal budget;
    private LocalDate endStageDate;

    public Stage(String name, BigDecimal budget, LocalDate endStageDate) {
        this.name = name;
        this.budget = budget;
        this.endStageDate = endStageDate;
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
