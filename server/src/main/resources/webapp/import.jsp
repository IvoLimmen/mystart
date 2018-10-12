<!doctype html>
<html lang="en">
  <jsp:include page="parts/header.jsp" flush="true"/>
  <body>
    <h1>MyStart</h1>
    <form name="import" action="/import" method="post" enctype="multipart/form-data">
      <div>
        <label for="file">Upload a file for parsing</label><br/>
        <input type="file" name="file"/>
      </div>
      <div>
        <label for="url">Enter a URL to read from</label><br/>
        <input type="text" name="url"/>
      </div>
      <div>      
        <label for="skipDuplicates">Skip duplicates (or update labels)</label><br/>
        <input type="checkbox" name="skipDuplicates"/>
      </div>
      <div>
        <button type="submit" name="importButton">Import</button>
      </div>
    </form>      
  </body>
</html>