package fourth.task;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static Person createPerson(String[] info){
        if (info.length == 3)
            return new Person(info[0].trim(), new BigDecimal(info[1].trim()).setScale(2, RoundingMode.HALF_UP), LocalDate.parse(info[2].trim(), formatter));
        else
            return new Person(info[0].trim(), new BigDecimal(info[1].trim()).setScale(2, RoundingMode.HALF_UP), LocalDate.parse(info[2].trim(), formatter), LocalDate.parse(info[3].trim(), formatter));
    }

    private static Stage createStage(String[] info){
        return new Stage (info[0].trim(), new BigDecimal(info[1].trim()), LocalDate.parse(info[2].trim(), formatter));
    }

    public static List<Person> readPersonsInfoInList(String fileName){
        List<Person> list = new ArrayList<>();
        String[] info;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))){
            String line = fileReader.readLine();
            while (line != null) {
                info = line.split(";");
                list.add(createPerson(info));
                line = fileReader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.printf("Файл для считывания данных: %s не найден\n", fileName);
        } catch (IOException e) {
            System.out.printf("Ошибка при чтении данных из файла: %s\n", fileName);
        }
        return list;
    }

    public static List<Stage> readStagesInfoInList(String fileName){
        List<Stage> list = new ArrayList<>();
        String[] info;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))){
            String line = fileReader.readLine();
            while (line != null) {
                info = line.split(";");
                list.add(createStage(info));
                line = fileReader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.printf("Файл для считывания данных: %s не найден\n", fileName);
        } catch (IOException e) {
            System.out.printf("Ошибка при чтении данных из файла: %s\n", fileName);
        }
        return list;
    }

    /*public static List readInfoInList(String fileName, Class className){
        List list = new ArrayList<>();
        String[] info;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))){
            String line = fileReader.readLine();
            while (line != null) {
                info = line.split(";");
                if (className.equals(Person.class))
                    list.add(createPerson(info));
                else
                    list.add(createStage(info));
                line = fileReader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.printf("Файл для считывания данных: %s не найден\n", fileName);
            return null;
        } catch (IOException e) {
            System.out.printf("Ошибка при чтении данных из файла: %s\n", fileName);
            return null;
        }
        return list;
    }*/
}
