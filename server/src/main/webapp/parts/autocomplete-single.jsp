<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="js/awesomplete.min.js"></script>
<script>         
  new Awesomplete('input[data-autocomplete]', {    
    list: [<c:forEach items="${labels}" var="label" varStatus="status">
      '${label}'<c:if test="${!status.last}">,</c:if>
    </c:forEach>]
  });
</script>
