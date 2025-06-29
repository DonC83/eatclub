package com.donc.eatclub.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.amazonaws.services.lambda.runtime.tests.annotations.Events;
import com.amazonaws.services.lambda.runtime.tests.annotations.HandlerParams;
import com.amazonaws.services.lambda.runtime.tests.annotations.Responses;
import org.junit.jupiter.params.ParameterizedTest;

public class TestActiveDealHandler {

  @ParameterizedTest
  @HandlerParams(
      events = @Events(folder = "events/", type = APIGatewayV2HTTPEvent.class),
      responses = @Responses(folder = "responses/", type = APIGatewayV2HTTPResponse.class)

  )
  void testApiGatewayEvents(APIGatewayV2HTTPEvent event, APIGatewayV2HTTPResponse expected) {
    Context context = new TestContext();
    ActiveDealHandler handler = new ActiveDealHandler();

    APIGatewayV2HTTPResponse response = handler.handleRequest(event, context);

    assertEquals(expected.getBody(), response.getBody());
    assertEquals(expected.getStatusCode(), response.getStatusCode());
  }

}
