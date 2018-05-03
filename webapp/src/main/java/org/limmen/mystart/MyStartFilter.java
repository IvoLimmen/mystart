package org.limmen.mystart;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import org.apache.wicket.protocol.http.WicketFilter;

public class MyStartFilter extends WicketFilter {      

   @Override
   public void init(boolean isServlet, FilterConfig filterConfig) throws ServletException {
      super.init(isServlet, filterConfig);
   }   
}
