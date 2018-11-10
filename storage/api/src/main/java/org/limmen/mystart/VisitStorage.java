package org.limmen.mystart;

import java.time.LocalDateTime;
import java.util.Set;

public interface VisitStorage {

  Set<LocalDateTime> getLast20Visists(Long linkId);

  void visit(Long linkId);
}
