<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/head.jsp" %>

    <body>
      <div class="my-qna__wrapper--wrapper">
        <div class="my-qna__wrapper--header">
          <h1>${qnaDetail.TITLE}</h1>
          <span>${qnaDetail.MEM_ID}님 | ${qnaDetail.REG_DATE}</span>
        </div>
        <div class="my-qna__wrapper--main">
          <div class="my-qna__wrapper--main-items">
            <div class="my-qna__wrapper--main-item">${qnaDetail.PRO_IDX}</div>
            <div class="my-qna__wrapper--main-item my-qna__wrapper--main-item-img">
              <img src="/uploadImg/${qnaDetail.MAIN_IMG}" alt="my qna item image" />
            </div>
            <div class="my-qna__wrapper--main-item">
              ${qnaDetail.NAME}
            </div>
            <div class="my-qna__wrapper--main-item">
              <fmt:formatNumber type="number" value="${qnaDetail.PRICE}" /> 원
            </div>
          </div>
          <div class="my-qna__wrapper--main-wrapper">
           <textarea class="my-qna__wrapper—main-content" readonly>${qnaDetail.CONTENT}</textarea>
            <div class="my-qna__wrapper--main-buttons">
              <button class="btn-secondary my-qna__button-resive" data-qna_idx="${qnaDetail.QNA_IDX}">수정</button><button
                class="btn-secondary my-qna__button-delete">삭제</button>
            </div>
          </div>
        </div>
        <div class="my-qna__wrapper-close">
          <button class="btn-secondary my-qna__wrapper-close-button">닫기</button>
        </div>
      </div>
      <script src="https://code.jquery.com/jquery-latest.min.js"></script>
      <script>
        document
          .querySelector('.my-qna__wrapper-close-button')
          .addEventListener('click', () => {
            window.close();
          });

        //qna 수정
        const updateButton = document.querySelector('.my-qna__button-resive');
        //qna 삭제
        const deleteButton = document.querySelector('.my-qna__button-delete');

        const content = document.querySelector('.my-qna__wrapper—main-content');

        updateButton.addEventListener('click', function () {
          const length = content.innerHTML.length;
          content.readOnly = false;
          content.focus();
          content.setSelectionRange(length, length);
          updateButton.innerText = '저장';
          updateButton.classList.add('save-button');

          const saveButton = document.querySelector('.save-button');//저장버튼을 클릭했을때
          saveButton.addEventListener('click', function () {
            const updateContent = content.value;
            const qna_idx = saveButton.dataset.qna_idx;
            const updateQna = { CONTENT: updateContent, QNA_IDX: qna_idx }

            $.ajax({
              type: 'post',
              url: '/qna/update',
              data: JSON.stringify(updateQna),
              contentType: 'application/json; charset=utf-8',
              success: function () {
                alert('게시글 수정이 완료 되었습니다.');
                window.location.reload();
                updateButton.innerText = '수정';
                updateButton.classList.remove('save-button');
              },
              error: function (error) {
                alert('게시글 수정에 실패하였습니다.');
              }
            });
          })
        });

        deleteButton.addEventListener('click', function () {
          const answer = confirm('게시물을 삭제하시겠습니까?');
          if (answer === true) {
            const qna_idx = updateButton.dataset.qna_idx;
            const deleteQna = {REF : qna_idx}
            $.ajax({
              type: 'post',
              url: '/qna/delete',
              data: JSON.stringify(deleteQna),
              contentType: 'application/json; charset=utf-8',
              success: function () {
                alert('게시글 삭제가 완료 되었습니다.');
                opener.location.reload();
                window.close();
              },
              error: function (error) {
                alert('게시글 삭제에 실패하였습니다.');
              }
            });
          } else {
            return false;
          }
        });
      </script>
    </body>

    </html>