package com.donc.eatclub.integration;

import com.donc.eatclub.model.RestaurantData;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;

public class DataFetcher {

  private static final Map<String, RestaurantData> dataMap = new HashMap<>();

  public RestaurantData GetData() {
    RestaurantData data;
    if (!dataMap.containsKey("restaurantData")) {
      try (HttpClient c = HttpClient.newHttpClient()) {
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("https://eccdn.com.au/misc/challengedata.json"))
            .GET().build();
        HttpResponse<String> resp = c.send(req, BodyHandlers.ofString());
        Gson gson = new Gson();
        data = gson.fromJson(resp.body(), RestaurantData.class);
        dataMap.put("restaurantData", data);
        return data;
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException("Failed to fetch restaurant data", e);
      }
    } else {
      return dataMap.get("restaurantData");
    }
  }

}
