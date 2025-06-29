package com.donc.eatclub.handlers;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;

public class TestLogger implements LambdaLogger {

  @Override
  public void log(String s) {}

  @Override
  public void log(byte[] bytes) {}

  @Override
  public void log(String message, LogLevel logLevel) {
    LambdaLogger.super.log(message, logLevel);
  }

  @Override
  public void log(byte[] message, LogLevel logLevel) {
    LambdaLogger.super.log(message, logLevel);
  }
}
