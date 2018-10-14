package org.limmen.mystart;

import java.io.IOException;
import java.util.Set;

public interface Parser {

  String getName();

  boolean canParse(ParseContext context) throws IOException;

  Set<Link> parse(ParseContext context) throws IOException;
}
