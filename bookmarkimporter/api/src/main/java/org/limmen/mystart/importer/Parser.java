package org.limmen.mystart.importer;

import java.io.IOException;
import java.util.Set;
import org.limmen.mystart.Link;

public interface Parser {

  String getName();

  boolean canParse(ParseContext context) throws IOException;

  Set<Link> parse(ParseContext context) throws IOException;
}
