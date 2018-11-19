package org.limmen.mystart.postgres;

import java.util.Map;
import java.util.function.Function;
import org.limmen.mystart.StatsStorage;

public class DbStatsStorage extends DbAbstractStorage implements StatsStorage {

  public DbStatsStorage(String user, String password, String url) {
    super(user, password, url);
  }

  private static final Function<Result, Map.Entry<String, Long>> MAPPER = res -> {
    String key = res.string("key");
    if (key == null) {
      key = "EMPTY";
    }
    return Map.entry(key, res.lng("value"));
  };

  @Override
  public Map<String, Long> getCreationStatistics(Long userId) {
    String sql = "select extract(year from creation_date) as key, count(id) as value "
        + "from links where user_id = ? group by key order by key";
    return executeStatisticsSql(sql, stmt -> {
                              stmt.setLong(1, userId);
                            }, MAPPER);
  }

  @Override
  public Map<String, Long> getProtocolStatistics(Long userId) {
    String sql = "select case when strpos(url, 'https:') = 1 then 'secure' else 'unsecure' end as key, count(id) as value "
        + "from links where user_id = ? group by key order by key";
    return executeStatisticsSql(sql, stmt -> {
                              stmt.setLong(1, userId);
                            }, MAPPER);
  }

  @Override
  public Map<String, Long> getSourceStatistics(Long userId) {
    String sql = "select source as key, count(id) as value "
        + "from links where user_id = ? group by key order by key";
    return executeStatisticsSql(sql, stmt -> {
                              stmt.setLong(1, userId);
                            }, MAPPER);
  }

  @Override
  public Map<String, Long> getVisitStatistics(Long userId) {
    String sql = "select extract(year from last_visit) as key, count(id) as value "
        + "from links where user_id = ? group by key order by key";
    return executeStatisticsSql(sql, stmt -> {
                              stmt.setLong(1, userId);
                            }, MAPPER);
  }
}
