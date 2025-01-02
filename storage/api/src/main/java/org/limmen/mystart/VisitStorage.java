package org.limmen.mystart;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitStorage {

  List<LocalDateTime> getLast20Visists(Long linkId);

  void visit(Long linkId);
}
