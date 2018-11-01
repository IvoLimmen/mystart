<section class="menu">
  <h1>MyStart</h1>
  <a href='javascript:(function(){var a=window,b=document,c=encodeURIComponent,d=a.open("https://limmen.org/link?edit&url="+c(b.location)+"&title="+c(b.title),"popup","left="+((a.screenX||a.screenLeft)+10)+",top="+((a.screenY||a.screenTop)+10)+",height=510px,width=550px,resizable=1,alwaysRaised=1");a.setTimeout(function(){d.focus()},300)})();'>Bookmarklet</a>
  <a href="import.jsp">Import new links</a>
  <a href="check.jsp">Check links</a>
  <a href="stats.jsp">Statistics</a>
  <a href="/link?delall">Delete current selection</a>
  <a href="/link?edit">Add a new link</a>
  <form name="search" action="/home">
    <input type="text" name="search"/>
    <button type="submit" name="searchButton">Search</button>
  </form>
</section>