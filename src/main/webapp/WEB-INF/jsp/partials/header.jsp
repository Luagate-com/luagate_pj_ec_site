<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="header">
  <div class="header-left">
    <a class="logo" href="${pageContext.request.contextPath}/goods">NORTH CRAFT</a>
  </div>
  <div class="header-right">
    <!-- sessionScope.userId の有無でログイン状態を判定する。 -->
    <c:choose>
      <c:when test="${not empty sessionScope.userId}">
        <a class="button-outline" href="${pageContext.request.contextPath}/mypage">マイページ</a>
      </c:when>
      <c:otherwise>
        <a class="button-outline" href="${pageContext.request.contextPath}/login">ログイン</a>
      </c:otherwise>
    </c:choose>
    <a class="cart" href="${pageContext.request.contextPath}/cart" aria-label="カート">
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M7 6h15l-2 9H8L6 3H2" stroke="#727270" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        <circle cx="9" cy="20" r="1.5" fill="#727270"/>
        <circle cx="18" cy="20" r="1.5" fill="#727270"/>
      </svg>
    </a>
  </div>
</header>
