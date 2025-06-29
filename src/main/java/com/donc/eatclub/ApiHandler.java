package com.donc.eatclub;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.donc.eatclub.model.Deal;
import com.donc.eatclub.model.RestaurantData;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

  private static final DateTimeFormatter DTF_24HOUR = DateTimeFormatter.ofPattern("HH:mm");
  private static final DateTimeFormatter DTF_AMPM;

  static {
    DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
    builder.parseCaseInsensitive();
    builder.appendPattern("h:mma");
    DTF_AMPM = builder.toFormatter();
  }

  @Override
  public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context context) {
    Map<String, String> params = event.getQueryStringParameters();
    if (params == null || !params.containsKey("timeOfDay")) {
      APIGatewayV2HTTPResponse resp = new APIGatewayV2HTTPResponse();

      resp.setStatusCode(400);
      resp.setBody("Missing 'timeOfDay' query parameter");
      return resp;
    }
    context.getLogger().log("Received request with param timeOfDay=: " + params.get("timeOfDay"));

    RestaurantData data = GetData();

    Gson gson = new Gson();
    APIGatewayV2HTTPResponse resp = new APIGatewayV2HTTPResponse();
    resp.setStatusCode(200);
    resp.setBody(gson.toJson(new DealResponse(FindActiveDeals(params.get("timeOfDay"), data))));

    return resp;
  }

  private RestaurantData GetData() {
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

  private List<RestaurantDeal> FindActiveDeals(String timeOfDay, RestaurantData rd) {
    List<RestaurantDeal> matchingDeals = new ArrayList<>();
    for (var r : rd.restaurants()) {
      if (r.deals() != null) {
        for (var d : r.deals()) {
          boolean isActive = false;
          if (d.start() != null && d.end() != null) {
            isActive = IsDealActive(timeOfDay, d.start(), d.end());
          } else if (d.open() != null && d.close() != null) {
            isActive = IsDealActive(timeOfDay, d.open(), d.close());
          }
          if (isActive) {
            matchingDeals.add(new RestaurantDeal(
                r.objectId(),
                r.name(),
                r.address1(),
                r.suburb(),
                r.open(),
                r.close(),
                d.objectId(),
                d.discount(),
                d.dineIn(),
                d.lightning(),
                d.qtyLeft()));
          }
        }
      }
    }
    return matchingDeals;
  }

  private boolean IsDealActive(String timeString, String startTimeString, String endTimeString) {
    LocalTime time = LocalTime.parse(timeString, DTF_24HOUR);
    LocalTime startTime = LocalTime.parse(startTimeString, DTF_AMPM);
    LocalTime endTime = LocalTime.parse(endTimeString, DTF_AMPM);
    return time.isAfter(startTime) && time.isBefore(endTime);
  }

  record DealResponse(
      List<RestaurantDeal> deals
  ) {

  }

  record RestaurantDeal(
      String restaurantObjectId,
      String restaurantName,
      String restaurantAddress1,
      String restarantSuburb,
      String restaurantOpen,
      String restaurantClose,
      String dealObjectId,
      String discount,
      String dineIn,
      String lightning,
      String qtyLeft
  ) {

  }

}
