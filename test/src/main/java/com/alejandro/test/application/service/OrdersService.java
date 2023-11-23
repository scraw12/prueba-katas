package com.alejandro.test.application.service;

import com.alejandro.test.application.domain.PagerDTO;
import com.alejandro.test.application.model.order.Order;
import com.alejandro.test.application.model.order.OrderGroupByItemType;
import com.alejandro.test.application.repository.OrderRepository;
import com.alejandro.test.application.service.threads.SaveAllRunnable;
import com.alejandro.test.application.service.threads.WriteCSV;
import com.alejandro.test.infraestructure.config.APIConnection;
import com.google.gson.Gson;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class OrdersService {

    @Autowired
    private APIConnection apiConnection;

    @Autowired
    private OrderRepository orderRepository;

    public List<NameValuePair> setUpValuePair(Integer page) {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        NameValuePair param1NameValuePair = new BasicNameValuePair("page", String.valueOf(page));
        NameValuePair param2NameValuePair = new BasicNameValuePair("max-per-page", "1000");
        nameValuePairs.add(param1NameValuePair);
        nameValuePairs.add(param2NameValuePair);
        return nameValuePairs;
    }

    private PagerDTO getOrdersFromApi(Integer page) {

        try {
            HttpGet httpGet = new HttpGet(apiConnection.getUrl());
            httpGet.addHeader("accept", "application/vnd.orders.page-list+json");
            URI uri = new URIBuilder(String.format("%s/orders", httpGet.getUri())).addParameters(this.setUpValuePair(page))
                    .build();
            httpGet.setUri(uri);
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                String response = client.execute(httpGet, new BasicHttpClientResponseHandler());
                return new Gson().fromJson(response, PagerDTO.class);

            } catch (IOException e) {
                return new PagerDTO(e.getMessage());
            }
        } catch(URISyntaxException e) {
            return new PagerDTO(e.getMessage());
        }
    }

    public String synchronizeOrders() {
        Instant start = Instant.now();
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        Integer page = 1;
        PagerDTO pagerDTO = this.getOrdersFromApi(page++);
        do {
            if(pagerDTO.getErrorMessage() != null) {
                threadPool.shutdownNow();
                return new Gson().toJson(pagerDTO.getErrorMessage());
            }
            threadPool.execute(new Thread(new SaveAllRunnable(pagerDTO.getContent(), this.orderRepository)));
            pagerDTO = this.getOrdersFromApi(page++);
        } while (pagerDTO.getLinks().getNext() != null && !pagerDTO.getLinks().getNext().isEmpty());

        new SaveAllRunnable(pagerDTO.getContent(), this.orderRepository).convertAndSave();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println(String.format("Time synchronize with DB: %s", timeElapsed));

        try {
            threadPool.shutdown();
            if(threadPool.awaitTermination(2, TimeUnit.MINUTES)) {
                start = Instant.now();
                List<OrderGroupByItemType> totalOrdersGrouByItemType = this.orderRepository.getCountByItemType();
                System.out.println(String.format("%15s  %10s", "Item Type", "Total"));
                System.out.println(String.format("%15s  %10s", "---------", "-----"));
                Integer totalData = 0;
                for(OrderGroupByItemType order : totalOrdersGrouByItemType) {
                    System.out.println(String.format("%15s %10s", order.getItemType(), order.getTotal()));
                    totalData += Math.toIntExact(order.getTotal());
                }
                System.out.println(String.format("%15s  %10s", "Total Data", totalData));
                end = Instant.now();
                timeElapsed = Duration.between(start, end);
                System.out.println(String.format("Time Print Table: %s",timeElapsed));
                return new Gson().toJson(totalOrdersGrouByItemType);
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
            return new Gson().toJson(e.getMessage());
        }

        threadPool.shutdownNow();
        return new Gson().toJson("Unexpected error, please try again");
    }


    public File generateCSV() {
        Instant start = Instant.now();
        List<Order> orderList = orderRepository.findAllByOrderByIdAsc();
        Instant endDB =  Instant.now();
        Duration timeElapsed = Duration.between(start, endDB);
        System.out.println("End call DB: " + timeElapsed);
        try {
            File f = new File("src/test/csv/orders_sorted_by_id_ascendant.csv");
            FileWriter fw = new FileWriter(f, false);
            BufferedWriter bw = new BufferedWriter(fw);
            LineNumberReader lnr = new LineNumberReader(new FileReader(f));
            new WriteCSV(orderList, bw, lnr).generateCSV();

            lnr.close();
            bw.close();
            fw.close();

            Instant end = Instant.now();
            timeElapsed = Duration.between(start, end);
            System.out.println("End generate CSV: " + timeElapsed);

            return f;
        } catch(IOException e) {
            return null;
        }
    }

}
