package org.limmen.mystart;

import java.util.Map;

public interface StatsStorage {

  Map<String, Long> getCreationStatistics(Long userId);

  Map<String, Long> getProtocolStatistics(Long userId);

  Map<String, Long> getSourceStatistics(Long userId);

  Map<String, Long> getVisitStatistics(Long userId);
}
