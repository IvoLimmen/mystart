package org.limmen.mystart.server.servlet.ajax;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;
import org.json.JSONException;
import org.json.JSONWriter;
import org.limmen.mystart.Storage;
import org.limmen.mystart.server.servlet.AbstractServlet;
import org.limmen.mystart.server.servlet.model.ChartData;

public class AjaxServlet extends AbstractServlet {

  private static final long serialVersionUID = 1L;

  public AjaxServlet(Storage storage,
                     MultipartConfigElement multipartConfigElement,
                     Path temporaryDirectory, Properties properties) {
    super(storage,
          multipartConfigElement,
          temporaryDirectory, 
          properties);
  }

  private List<ChartData> mapToChartData(Map<String, Long> data) {
    List<ChartData> items = new ArrayList<>();

    ColorPicker colorPicker = new ColorPicker();

    data.forEach((key, value) -> {
      String color = colorPicker.next();
      items.add(new ChartData("EMPTY".equals(key) ? "Never" : key, color, "" + value, color));
    });

    return items;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doGet(req, res);
    Long userId = (Long) req.getSession().getAttribute(USER_ID);

    if (userId == null) {
      return;
    }
    if (exists(req, "stats")) {
      String key = req.getParameter("stats");

      switch (key) {
        case "source":
          streamChartData(res, mapToChartData(getStatsStorage().getSourceStatistics(userId)).stream());
          break;
        case "create_year":
          streamChartData(res, mapToChartData(getStatsStorage().getCreationStatistics(userId)).stream());
          break;
        case "visits":
          streamChartData(res, mapToChartData(getStatsStorage().getVisitStatistics(userId)).stream());
          break;
        case "protocol":
          streamChartData(res, mapToChartData(getStatsStorage().getProtocolStatistics(userId)).stream());
          break;
      }
    }
  }

  protected void streamChartData(HttpServletResponse res, Stream<ChartData> stream) throws IOException, JSONException {
    res.setHeader("Content-Type", "application/json");
    JSONWriter jsonWriter = new JSONWriter(res.getWriter());
    jsonWriter.array();
    stream.forEach(item -> {
      jsonWriter.object();
      jsonWriter.key("value").value(item.value);
      jsonWriter.key("highlight").value(item.highlight);
      jsonWriter.key("label").value(item.label);
      jsonWriter.key("color").value(item.color);
      jsonWriter.endObject();
    });
    jsonWriter.endArray();
    res.flushBuffer();
  }
}
