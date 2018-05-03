package org.limmen.mystart;

import java.io.IOException;
import java.util.List;

public interface Parser {

   String getName();
   
   boolean canParse(ParseContext context) throws IOException;
   
   List<Link> parse(ParseContext context) throws IOException;
}
