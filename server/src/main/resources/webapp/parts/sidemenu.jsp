<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul class="sidebar-menu" data-widget="tree">
  <li class="header">Menu</li>
  <li><a href="/link?edit">Add a new link</a></li>
  <li><a href="/link?delall">Delete current selection</a></li>
  <li><a href="/nav?page=import">Import links</a></li>
  <li><a href="/nav?page=check">Check links</a></li>
  <li><a href="/nav?page=stats">Statistics</a></li>
  <li><a href='javascript:(function(){var a=window,b=document,c=encodeURIComponent,d=a.open("https://limmen.org/link?edit&url="+c(b.location)+"&title="+c(b.title),"popup","left="+((a.screenX||a.screenLeft)+10)+",top="+((a.screenY||a.screenTop)+10)+",height=510px,width=550px,resizable=1,alwaysRaised=1");a.setTimeout(function(){d.focus()},300)})();'>Bookmarklet</a></li>
  <li><a href="/home?show=labels">Labels</a></li>
</ul>
