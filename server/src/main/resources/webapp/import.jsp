<!doctype html>
<html lang="en">
  <jsp:include page="parts/header.jsp" flush="true"/>
  <body>
    <h1>MyStart</h1>
    <form name="import" action="/importServlet" method="post" enctype="multipart/form-data">
      <label for="file">Upload a file for parsing</label>
      <input type="file" name="file"/>
      <label for="url">Enter a URL to read from</label>
      <input type="text" name="url"/>
      <button type="submit" name="import">Import</button>
    </form>      
  </body>
</html>