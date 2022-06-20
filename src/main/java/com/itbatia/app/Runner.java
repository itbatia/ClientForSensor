package com.itbatia.app;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Scanner;

public class Runner {

    public static void main(String[] args) {

        List<Double> temperature;
        HttpStatus statusOfResponse;
        String sensorName;
        String countMeasurement;
        String userChoice;

        RequestsToRestApi request = new RequestsToRestApi();
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("Введите новое название датчика для регистрации:");
            sensorName = scanner.nextLine();
            statusOfResponse = request.registerSensor(sensorName);
        } while (statusOfResponse==null);


        System.out.println("Укажите количество измерений этого датчика, которые будут отправлены на сервер:");
        countMeasurement = scanner.nextLine();
        while(!countMeasurement.matches("\\d+")) {
            System.out.println("Ведены неверные данные! Введите число:");
            countMeasurement = scanner.nextLine();
        }
        request.sendMeasurements(Integer.parseInt(countMeasurement), sensorName);


        System.out.println("\nПолучить эти измерения от сервера?\n1 - Да\n2 - Нет");
        userChoice = scanner.nextLine();

        if (userChoice.equals("1")) {
            temperature = request.getAllMeasurements();
            temperature.forEach(System.out::println);

            System.out.println("\nПоказать диаграмму изменения температур?\n1 - Да\n2 - Нет");
            userChoice = scanner.nextLine();
            if (userChoice.equals("1")) {
                request.drawChart(temperature);
            } else if (userChoice.equals("2")){
                System.out.println("Программа завершена.");
            } else {
                System.out.println("Ведены некорректные данные! Программа завершила работу.");
            }
        } else if (userChoice.equals("2")){
            System.out.println("Программа завершена.");
        } else {
            System.out.println("Ведены некорректные данные! Программа завершила работу.");
        }
    }
}
