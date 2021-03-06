package fourth.task;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;

public class Person implements Comparable<Person>{
    private static int idCounter = 0;

    private final int id;
    private final String name;
    private final BigDecimal salaryInDay;
    private final LocalDate beginDate;
    private LocalDate endDate;

    public Person(String name, BigDecimal salaryInDay, LocalDate beginDate) {
        idCounter++;
        this.name = name;
        this.salaryInDay = salaryInDay;
        this.beginDate = beginDate;
        this.id = idCounter;
    }

    public Person(String name, BigDecimal salaryInDay, LocalDate beginDate, LocalDate endDate) {
        idCounter++;
        this.name = name;
        this.salaryInDay = salaryInDay;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.id = idCounter;
    }

    public BigDecimal getSalaryInDay() {
        return salaryInDay;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id &&
                Objects.equals(name, person.name) &&
                Objects.equals(salaryInDay, person.salaryInDay) &&
                Objects.equals(beginDate, person.beginDate) &&
                Objects.equals(endDate, person.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, salaryInDay, beginDate, endDate);
    }

    @Override
    public int compareTo(Person otherPerson) {
        return Comparators.BEGIN_DATE.compare(this, otherPerson);
    }

    public static class Comparators {

        public static Comparator<Person> BEGIN_DATE = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.beginDate.compareTo(o2.beginDate);
            }
        };
        public static Comparator<Person> END_DATE = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.endDate.compareTo(o2.endDate);
            }
        };
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", salaryInDay=" + salaryInDay +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                '}' + "\n";
    }
}
