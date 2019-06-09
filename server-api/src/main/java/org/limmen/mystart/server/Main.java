package org.limmen.mystart.server;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFiles;

public class Main {

  public static void main(String[] args) {

    port(8080);
    
    staticFiles.location("/public");
    staticFiles.expireTime(3600); // hour

    get("/command", (req, res) -> CommandHandler.handle(req, res));
  }
}
