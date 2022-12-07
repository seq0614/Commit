<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/head.jsp" %> 

  <body>
    <!---------------------------- header 영역 --------------------------------------->
	<jsp:include page="/WEB-INF/include/header.jsp"></jsp:include>
    <!-------------------------------- main 부분 ------------------------------->
    <main>

      <section class="carousel-wrapper">
        <h1 class="sr-only">carousel</h1>
        <div class="carousel-prev"><i class="bi bi-chevron-left"></i></div>
        <div class="carousel-next"><i class="bi bi-chevron-right"></i></div>
        <div class="carousel__items">
			<div class="carousel__item" style="background-color: #7a31b6;">
			    <img class="carousel__item--image" src="/assets/main/antec.jpg" alt="main banner image kakao" />
			</div>
			<div class="carousel__item" style="background-color: #becec1;">
			    <img class="carousel__item--image" src="/assets/main/zotac.jpg" alt="main banner image kakao" />
			</div>
			<div class="carousel__item" style="background-color: #840607;">
			    <img class="carousel__item--image" src="/assets/main/msi.jpg" alt="main banner image kakao" />
			</div>
		</div>
      </section>

      <!-- bestPc 게임용 -->
      <section class="section">
        <div class="pc">
          <div class="pc__desc">
            <h1 class="pc__title">Best Gaming Pc</h1>
            <a href="/pro/list?PRO_GROUP=PC">
              <span class="pc__more">더보기</span>
            </a>
          </div>
          <ul class="pc__items">
            <c:forEach var="row" items="${gamingPC}">
              <li>
                <a class="pc__item" href="/pro/detail?PRO_IDX=${row.PRO_IDX}">
                  <img class="pc__item--image" src="/uploadImg/${row.MAIN_IMG}" alt="computer" />
                  <div class="circle">
                    <span><i class="bi bi-lightning-charge-fill"></i></span>
                  </div>
                  <span class="pc__item--title">${row.NAME}</span>
                  <span class="pc__item--price">판매가 :
                    <fmt:formatNumber type="number" value="${row.PRICE}" />원
                  </span>
                </a>
              </li>
            </c:forEach>
          </ul>
        </div>
      </section>

      <!-- Best Pc 사무용 -->
      <section class="section pc-office">
        <div class="pc">
          <div class="pc__desc">
            <h1 class="pc__title">Best Office Pc</h1>
            <a href="/pro/list?PRO_GROUP=PC">
              <span class="pc__more">더보기</span>
            </a>
          </div>
          <ul class="pc__items">
            <c:forEach var="row" items="${officePC}">
              <li>
                <a class="pc__item" href="/pro/detail?PRO_IDX=${row.PRO_IDX}">
                  <img class="pc__item--image" src="/uploadImg/${row.MAIN_IMG}" alt="computer" />
                  <div class="circle">
                    <span><i class="bi bi-lightning-charge-fill"></i></span>
                  </div>
                  <span class="pc__item--title">${row.NAME}</span>
                  <span class="pc__item--price">판매가 :
                    <fmt:formatNumber type="number" value="${row.PRICE}" />원
                  </span>
                </a>
              </li>
            </c:forEach>
          </ul>
        </div>
      </section>
    </main>
	<jsp:include page="/WEB-INF/include/footer.jsp"></jsp:include>
    <script src="/js/carousel.js"></script>
    <script src="/js/dropdown.js"></script>
  </body>

  </html>