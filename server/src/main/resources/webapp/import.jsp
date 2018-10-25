<!doctype html>
<html lang="en">
  <jsp:include page="parts/head.jsp" flush="true"/>
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
        <label for="importHomepageAsExtra">Import homepage as extra link (GitHub only)</label><br/>
        <input type="checkbox" name="importHomepageAsExtra"/>
      </div>
      <div>
        <label for="importLanguageAsLabel">Import language as label (GitHub only)</label><br/>
        <input type="checkbox" name="importLanguageAsLabel"/>
      </div>
      <div>
        <button type="submit" name="importButton">Import</button>
      </div>
    </form>      
  </body>
</html>