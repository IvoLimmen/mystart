<%

%>
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>MyStart</title>
    <meta name="description" content="MyStart">
    <meta name="author" content="Ivo Limmen">    
  </head>
  <body>
    <h1>MyStart</h1>
    <form name="search">
      <input type="text" name="search"/>
      <button type="submit" name="search">Search</button>
    </form>
    <form name="import" action="/importServlet" method="post" enctype="multipart/form-data">
      <input type="file" name="file"/>
      <button type="submit" name="import">Import</button>
    </form>      
  </body>
</html>