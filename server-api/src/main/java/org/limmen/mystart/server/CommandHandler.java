package org.limmen.mystart.server;

import spark.Request;
import spark.Response;

public class CommandHandler {

  public static String handle(Request req, Response res) {
    return "test";
  }
}