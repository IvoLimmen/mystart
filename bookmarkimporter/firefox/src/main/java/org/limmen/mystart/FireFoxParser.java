package org.limmen.mystart;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FireFoxParser extends AbstractParser implements Parser {

   public FireFoxParser() {
      setSource("FireFox");
   }

   @Override
   public String getName() {
      return "FireFox";
   }

   @Override
   public boolean canParse(ParseContext context) {
      return context.getFileName() != null && context.getFileName().endsWith("places.sqlite");
   }

   @Override
  public Set<Link> parse(ParseContext context) throws IOException {

      List<LinkDto> list = new ArrayList<>();

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + context.getTemporaryFileName())) {
         Statement statement = connection.createStatement();
         statement.setQueryTimeout(30);

         try (ResultSet rs = statement.executeQuery(
             "select b.parent, b.title, p.url, b.dateAdded, p.last_visit_date, a.content from moz_bookmarks b "
             + "left join moz_places p on b.fk = p.id	"
             + "left join moz_items_annos a on b.id = a.item_id "
             + "where b.type = 1 and b.guid not in (\"menu________\",\"toolbar_____\",\"tags________\",\"unfiled_____\")")) {

            while (rs.next()) {

               LinkDto dto = new LinkDto();
               dto.setUrl(rs.getString("url"));
               dto.setTitle(rs.getString("title"));
               dto.setDescription(rs.getString("content"));
               dto.setDateAdded(convertEpochToTimestamp(rs.getLong("dateAdded")));
               dto.setDateVisited(convertEpochToTimestamp(rs.getLong("last_visit_date")));
               dto.setParent(rs.getInt("parent"));
               
               if (dto.isValid()) {
                  list.add(dto);
               }
            }
         }

         list.forEach(l -> {
            try {
               l.addLabels(getLabels(statement, l.getParent()));
            }
            catch (SQLException ex) {
               // ignore
            }
         });
      }
      catch (SQLException e) {
         throw new IOException(e);
      }

      list.forEach(l -> {
         addLink(l.getTitle(), l.getUrl(), l.getLabels(), l.getDescription(), l.getDateAdded(), l.getDateVisited());
      });

      return getLinks();
   }

   private Collection<String> getLabels(Statement statement, Integer id) throws SQLException {
      List<String> list = new ArrayList<>();

      String title = null;
      Integer type = null;
      Integer parent = null;

      try (ResultSet rs = statement.executeQuery("select * from moz_bookmarks where id = " + id)) {
         while (rs.next()) {
            title = rs.getString("title");
            type = rs.getInt("type");
            parent = rs.getInt("parent");
         }
      }

      if (type != null && type == 2) {
         list.add(title);
         if (parent != null && parent > 3) {
            list.addAll(getLabels(statement, parent));
         }
      }

      return list;
   }

}
