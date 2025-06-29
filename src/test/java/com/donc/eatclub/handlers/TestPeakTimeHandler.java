package com.donc.eatclub.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import org.junit.jupiter.api.Test;

public class TestPeakTimeHandler {

  @Test
  void testApiGatewayEvents() {
    Context context = new TestContext();
    PeakTimeHandler handler = new PeakTimeHandler();

    APIGatewayV2HTTPResponse response = handler.handleRequest(new APIGatewayV2HTTPEvent(), context);

    assertEquals(200, response.getStatusCode());
    assertEquals("{\"peakTimeStart\":\"5:00pm\",\"peakTimeEnd\":\"9:00pm\"}", response.getBody());
  }

}
