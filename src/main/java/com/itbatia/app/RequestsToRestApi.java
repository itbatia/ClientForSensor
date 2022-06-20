package com.itbatia.app;

import com.itbatia.app.dto.AllMeasurements;
import com.itbatia.app.dto.Measurement;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RequestsToRestApi {

    private final RestTemplate restTemplate = new RestTemplate();

    public void registerSensor(String sensorName) {

        final String url = "http://localhost:8080/sensors/registration";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("name", sensorName);

        makePostRequestWithJSONData(url, jsonData);

        System.out.println("Ваш датчик успешно зарегистрирован!\n");
    }

    public void sendMeasurements(int count, String sensorName) {

        final String url = "http://localhost:8080/measurements/add";

        Map<String, Object> jsonData = new HashMap<>();
        for (int i = 1; i <= count; i++) {
            jsonData.put("value", new Random().nextDouble(45.0));
            jsonData.put("raining", new Random().nextBoolean());
            jsonData.put("sensor", Map.of("name", sensorName));

            makePostRequestWithJSONData(url, jsonData);
            System.out.println("Измерение " + i + " успешно отправлено на сервер!");
        }
    }

    public List<Double> getAllMeasurements() {
        String url = "http://localhost:8080/measurements";
        AllMeasurements jsonResponse = restTemplate.getForObject(url, AllMeasurements.class);

        if (jsonResponse == null) {
            return Collections.emptyList();
        }
        return jsonResponse.getMeasurements().stream().map(Measurement::getValue).collect(Collectors.toList());
    }

    private void makePostRequestWithJSONData(String url, Map<String, Object> jsonData) {

        // Чтобы принимающая сторона знала, что я посылаю JSON:
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // В тело запроса кладём мапу jsonData:
        HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);

        try {
            restTemplate.postForObject(url, request, String.class);
        } catch (HttpClientErrorException e) {
            System.out.println("Ошибка! " + e.getMessage());
        }
    }

    public void drawChart(List<Double> temperatures) {
        double[] xData = IntStream.range(0, temperatures.size()).asDoubleStream().toArray();
        double[] yData = temperatures.stream().mapToDouble(x -> x).toArray();

        XYChart chart = QuickChart.getChart("Temperatures", "X", "Y", "temperature",
                xData, yData);

        new SwingWrapper(chart).displayChart();
    }
}
