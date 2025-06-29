package com.donc.eatclub.integration;

import com.donc.eatclub.model.RestaurantData;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class DataFetcher {

  public RestaurantData GetData() {
    try (HttpClient c = HttpClient.newHttpClient()) {
      HttpRequest req = HttpRequest.newBuilder()
          .uri(URI.create("https://eccdn.com.au/misc/challengedata.json"))
          .GET().build();
      HttpResponse<String> resp = c.send(req, BodyHandlers.ofString());
      Gson gson = new Gson();
      return gson.fromJson(resp.body(), RestaurantData.class);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to fetch restaurant data", e);
    }
  }

}
