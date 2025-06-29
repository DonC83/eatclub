package com.donc.eatclub.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.donc.eatclub.integration.DataFetcher;
import com.donc.eatclub.model.RestaurantData;
import com.google.gson.Gson;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class PeakTimeHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

  @Override
  public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context context) {
    RestaurantData data = new DataFetcher().GetData();

    List<TimeRange> timeRanges = data.restaurants().stream()
        .filter(r -> r.deals() != null)
        .flatMap(r -> r.deals().stream()
            .map(d -> {
              if (d.start() != null && d.end() != null) {
                return new TimeRange(d.start(), d.end());
              } else if (d.open() != null && d.close() != null) {
                return new TimeRange(d.open(), d.close());
              } else {
                return null;
              }
            })
            .filter(Objects::nonNull)
        )
        .toList();

    TimeRange peakTime = findPeakTimeRange(timeRanges);

    APIGatewayV2HTTPResponse resp = new APIGatewayV2HTTPResponse();
    resp.setStatusCode(200);
    Gson gson = new Gson();
    resp.setBody(gson.toJson(new PeakTimeResponse(peakTime.start, peakTime.end)));
    return resp;
  }

  record PeakTimeResponse(String peakTimeStart, String peakTimeEnd) { }

  record TimeRange(String start, String end) { }

  private TimeRange findPeakTimeRange(List<TimeRange> timeRanges) {
    DateTimeFormatterBuilder dtfBuilder = new DateTimeFormatterBuilder();
    dtfBuilder.parseCaseInsensitive();
    dtfBuilder.appendPattern("h:mma");
    DateTimeFormatter dtf = dtfBuilder.toFormatter();
    TreeMap<LocalTime, Integer> events = new TreeMap<>();

    for (TimeRange tr : timeRanges) {
      LocalTime start = LocalTime.parse(tr.start(), dtf);
      LocalTime end = LocalTime.parse(tr.end(), dtf);
      events.put(start, events.getOrDefault(start, 0) + 1);
      events.put(end, events.getOrDefault(end, 0) - 1);
    }

    int current = 0, max = 0;
    LocalTime peakStart = null, peakEnd = null;
    for (Map.Entry<LocalTime, Integer> entry : events.entrySet()) {
      current += entry.getValue();
      if (current > max) {
        max = current;
        peakStart = entry.getKey();
      } else if (current < max && peakStart != null) {
        peakEnd = entry.getKey();
        break;
      }
    }
    if (peakStart != null && peakEnd == null) {
      peakEnd = events.lastKey();
    }
    assert peakStart != null;
    return new TimeRange(peakStart.format(dtf), peakEnd.format(dtf));
  }
}
