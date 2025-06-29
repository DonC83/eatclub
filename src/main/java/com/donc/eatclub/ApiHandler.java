package com.donc.eatclub;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import java.util.Map;

public class ApiHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

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
    return null;
  }


}
