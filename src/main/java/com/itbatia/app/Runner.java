package com.itbatia.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Runner {

    public static void main(String[] args) {

        RequestsToRestApi request = new RequestsToRestApi();
        Scanner scanner = new Scanner(System.in);
        List<Double> temperature = new ArrayList<>();

        System.out.println("Введите новое название датчика для регистрации:");
        String sensorName = scanner.nextLine();
        request.registerSensor(sensorName);

        System.out.println("Укажите количество измерений этого датчика, которые будут отправлены на сервер:");
        request.sendMeasurements(scanner.nextInt(), sensorName);

        System.out.println("\nПолучить эти измерения от сервера?\n1 - Да\n2 - Нет");
        if (scanner.nextInt() == 1) {
            temperature = request.getAllMeasurements();
            temperature.forEach(System.out::println);

            System.out.println("\nПоказать диаграмму изменения температур?\n1 - Да\n2 - Нет");
            if (scanner.nextInt() == 1) {
                request.drawChart(temperature);
            }
        }
    }
}
