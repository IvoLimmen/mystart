<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="js/awesomplete.min.js"></script>
<script>         
  new Awesomplete('input[data-multiple]', {
    filter: function(text, input) {
      return Awesomplete.FILTER_CONTAINS(text, input.match(/[^,]*$/)[0]);
    },

    item: function(text, input) {
      return Awesomplete.ITEM(text, input.match(/[^,]*$/)[0]);
    },

    replace: function(text) {
      var before = this.input.value.match(/^.+,\s*|/)[0];
      this.input.value = before + text + ", ";
    },

    list: [<c:forEach items="${labels}" var="label" varStatus="status">
      '${label}'<c:if test="${!status.last}">,</c:if>
    </c:forEach>]
  });
</script>
