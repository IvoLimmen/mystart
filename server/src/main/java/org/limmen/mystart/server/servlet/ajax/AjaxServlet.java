package org.limmen.mystart.server.servlet.ajax;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import java.util.stream.Stream;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONWriter;
import org.limmen.mystart.Link;
import org.limmen.mystart.Storage;
import org.limmen.mystart.server.servlet.AbstractServlet;
import org.limmen.mystart.server.servlet.model.ChartData;

@Slf4j
public class AjaxServlet extends AbstractServlet {

  private static final List<String> COLORS = new ArrayList<>();
  private static final long serialVersionUID = 1L;

  static {
    COLORS.add("#f56954");
    COLORS.add("#00a65a");
    COLORS.add("#f39c12");
    COLORS.add("#00c0ef");
    COLORS.add("#3c8dbc");
    COLORS.add("#d2d6de");
  }

  public AjaxServlet(Storage storage,
                     MultipartConfigElement multipartConfigElement,
                     Path temporaryDirectory) {
    super(storage,
          multipartConfigElement,
          temporaryDirectory);
  }

  private List<ChartData> bySource(Collection<Link> links) {
    List<ChartData> items = new ArrayList<>();

    AtomicInteger counter = new AtomicInteger(0);
    links.stream()
        .collect(groupingBy(g -> g.getSource(), counting()))
        .forEach((key, count) -> {
          String color = COLORS.get(counter.getAndIncrement());
          items.add(new ChartData(key, color, "" + count, color));
        });

    return items;
  }

  private List<ChartData> byVisits(Collection<Link> links) {
    List<ChartData> items = new ArrayList<>();

    AtomicInteger counter = new AtomicInteger(0);
    links.stream()
        .collect(groupingBy(g -> g.getLastVisit() == null ? "Never" : g.getLastVisit().getYear() + "", counting()))
        .forEach((key, count) -> {
          String color = COLORS.get(counter.getAndIncrement());
          items.add(new ChartData(key, color, "" + count, color));
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
      Collection<Link> links = getLinkStorage().getAll(userId);
      Collection<String> labels = getLinkStorage().getAllLabels(userId);

      switch (key) {
        case "source":
          streamChartData(res, bySource(links).stream());
          break;
        case "create_year":
//          stats.putAll(
//              links.stream()
//                  .collect(groupingBy(g -> g.getCreationDate().getYear() + "", counting())));
          break;
        case "visits":
          streamChartData(res, byVisits(links).stream());
          break;
        case "labels":
//          labels.forEach(l -> {
//            stats.put(l, links.stream().filter(f -> f.getLabels().contains(l)).count());
//          });
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
