const plusButton = document.querySelector('.product-add__plus');
const minusButton = document.querySelector('.product-add__minus');
const productQuantity = document.querySelector('.product-quantity');
const productPrice = document.querySelector('.product-price__price');
const addToCart = document.querySelector('.add-to__cart');
const addToBuy = document.querySelector('.add-to__buy');
const productNumber = document.querySelector('.product-number');
const qnaItemTitle = document.querySelectorAll('.qna-item__title');
const qnaWrite = document.querySelector('.qna_write');
const mem_id = document.querySelector('.mem_id');
const admin = document.querySelector('.admin');

qnaWrite.addEventListener('click', () => {
   window.open(
      '/qna/write?PRO_IDX='+productNumber.value,
      'Child',
      'width = 670, height = 800'
   );
})

let initialQuantity = 1;
const regax = /[^0-9]/g;

productQuantity.value = initialQuantity;

const calcPrice = () => {
  console.log(productPrice.innerHTML);
  let price = parseInt(productPrice.innerHTML.replace(regax, ''));
  let productAddTotalPrice = document.querySelector(
    '.product-add__total-price'
  );
  let totalPrice = (price * initialQuantity).toLocaleString();
  productAddTotalPrice.innerHTML = `${totalPrice} 원`;
  let productTotalPrice = document.querySelector('.product-total__total-price');
  productTotalPrice.innerHTML = `${totalPrice} 원`;
};

const onClickPlusButton = () => {
  initialQuantity++;
  productQuantity.value = initialQuantity;
  calcPrice();
};

const onClickMinusButton = () => {
  if (initialQuantity > 1) {
    initialQuantity--;
    productQuantity.value = initialQuantity;
    calcPrice();
  }
};

plusButton.addEventListener('click', onClickPlusButton);

minusButton.addEventListener('click', onClickMinusButton);

addToCart.addEventListener('click', async () => {
   const productNum = parseInt(productNumber.value);
   const obj = { AMOUNT: initialQuantity, PRO_IDX: productNum };
   const objJson = JSON.stringify(obj);
   $.ajax({
      type: 'POST',
      url: '/basket/add',
      data: objJson,
      contentType: 'application/json',
      beforeSend: function(xmlHttpRequest) {
         xmlHttpRequest.setRequestHeader('AJAX', 'true');
      },
      success: function() {
         let answer = confirm(
            '장바구니에 추가되었습니다. 장바구니로 이동하시겠습니까?'
         );
         if (answer === true) {
            location.href = '/basket/main';
         }
      },
      error: function(error) {
         if (error.status == 400) {
            alert('로그인이 필요합니다');
            location.href = '/member/login';
         }
      },
   });
});

addToBuy.addEventListener('click', () => {
  const productNum = parseInt(productNumber.value);
  const data_pro = productNum;
  const data_amount = initialQuantity;
  sendData('/pro/order', { PRO_IDX: data_pro, AMOUNT: data_amount }, 'get');
});

function sendData(path, parameters, method) {
  const form = document.createElement('form');
  form.method = method;
  form.action = path;
  document.body.appendChild(form);

  for (const key in parameters) {
    const formField = document.createElement('input');
    formField.type = 'hidden';
    formField.name = key;
    formField.value = parameters[key];

    form.appendChild(formField);
  }
  form.submit();
}

for (const qnaTitle of qnaItemTitle) {
  qnaTitle.addEventListener('click', () => {
    const wrapper = document.querySelector(
      '.product-info__footer-qna--main-content'
    );
    wrapper.classList.toggle('open');
  });
}

const renderDetailPage = async () => {
  const postData = { AMOUNT: initialQuantity, PRO_IDX: parseInt(productNumber.value) };
  let reviewCurrentPage = 1;
  let qnaCurrentPage = 1;
  try {
    const res = await axios.post('/pro/detail', JSON.stringify(postData), {
         headers: { 'Content-Type': `application/json` },
      });
    const data = res.data;
    console.log(data);
    const { image, qna, review, detail } = data;
    const detailMainImageWrapper = document.querySelector(
      '.product-detail__img'
    );
    const detailMainImage = document.createElement('img');
    detailMainImage.src = '/uploadImg/'+detail.MAIN_IMG;
    detailMainImageWrapper.appendChild(detailMainImage);
    // 디테일 메인 가격
    productPrice.textContent = `${detail.PRICE.toLocaleString()} 원`;
    // 디테일 이름
    const detailTitle = document.querySelector('.product-detail__title');
    detailTitle.textContent = detail.NAME;
    //디테일 서브이름
    const detailSubTitle = document.querySelector('.product-sub-title');
    detailSubTitle.textContent = detail.NAME;
    //상품추가 토탈 가격
    const detailAddTotalPrice = document.querySelector(
      '.product-add__total-price'
    );
    detailAddTotalPrice.textContent = `${detail.PRICE.toLocaleString()} 원`;
    //디데일 토탈 가격
    const detailTotalPrice = document.querySelector(
      '.product-total__total-price'
    );
    detailTotalPrice.textContent = `${detail.PRICE.toLocaleString()} 원`;
    //디테일 상세정보 이미지
    const detailDetailImageWrapper = document.querySelector(
      '.product-info__footer-image'
    );
    image.map((image) => {
      const detailDetailImage = document.createElement('img');
      detailDetailImage.src = `/uploadImg/${image.STORED_NAME}`;
      detailDetailImageWrapper.appendChild(detailDetailImage);
    });
    // 디테일 상세정보 리뷰
    const detailReviewWrapper = document.querySelector(
      '.product-info__footer-review'
    );
    const renderReview = (reviewData) => {
      const reviewWrapperTitle = document.createElement('h1');
      reviewWrapperTitle.classList.add('product-info__footer-review--title');
      reviewWrapperTitle.textContent = 'Review';
      detailReviewWrapper.appendChild(reviewWrapperTitle);
      if(reviewData.length === 0){
		reviewWrapperTitle.style.display = 'none';
	  }
      reviewData.map((item) => {
        const reviewItemWrapper = document.createElement('div');
        reviewItemWrapper.classList.add('product-info__footer-review--item');
        const reviewItemTitle = document.createElement('div');
        reviewItemTitle.classList.add('review--item__title');
        const title = document.createElement('h3');
        title.textContent = item.MEM_ID;
        const titleCenter = document.createElement('span');
        titleCenter.textContent = '|';
        const titleDate = document.createElement('p');
        titleDate.textContent = item.REG_DATE;
        const reviewidx = document.createElement('span');
      reviewidx.innerHTML = item.REVIEW_IDX;
      reviewidx.classList.add('review_idx');
      reviewidx.classList.add('disable');
        reviewItemTitle.appendChild(reviewidx);
        reviewItemTitle.appendChild(title);
        reviewItemTitle.appendChild(titleCenter);
        reviewItemTitle.appendChild(titleDate);

        const reviewItemMain = document.createElement('div');
        reviewItemMain.classList.add('review--item__main');
        const reviewTitle = document.createElement('h1');
        reviewTitle.textContent = item.TITLE;
        const reviewContent = document.createElement('span');
        reviewContent.textContent = item.CONTENT;
        const reviewDeleteButton = document.createElement('div');
        reviewDeleteButton.classList.add('review--item__delete');
        const reviewDeleteButtonIcon = document.createElement('i');
        reviewDeleteButtonIcon.classList.add('bi-trash3');
        reviewDeleteButtonIcon.classList.add('bi');
        if (mem_id.innerHTML != item.MEM_ID) {
               reviewDeleteButtonIcon.classList.add('disable');
            }
        reviewDeleteButton.appendChild(reviewDeleteButtonIcon);
        reviewItemMain.appendChild(reviewTitle);
        reviewItemMain.appendChild(reviewContent);
        reviewItemMain.appendChild(reviewDeleteButton);

        reviewItemWrapper.appendChild(reviewItemTitle);
        reviewItemWrapper.appendChild(reviewItemMain);
        detailReviewWrapper.appendChild(reviewItemWrapper);
      });
      $(".disable").css("display", "none");
         const deleteReview = document.querySelectorAll('.bi-trash3');
         const reviewidx = document.querySelectorAll('.review_idx');
         for (let i = 0; i < deleteReview.length; i++) {
            deleteReview[i].addEventListener('click', () => {
               let answer = confirm('삭제하시겠습니까?');
               if (answer === true) {
               const data = { REVIEW_IDX: reviewidx[i].innerHTML };
               axios.post('/review/delete', JSON.stringify(data), {
                  headers: { 'Content-Type': `application/json` },
               });
               location.reload();
               }
            })
         }
    };
    if (reviewCurrentPage === 1) {
      detailReviewWrapper.innerHTML = '';
      const data = [...review].slice(0, 7);
      renderReview(data);
    }
    renderPageList([...review], reviewCurrentPage, renderReview);
    const reviewPaginationItem = document.querySelectorAll('.review-page-item');
    reviewPaginationItem[0].classList.add('is-active');
    for (const pageItem of reviewPaginationItem) {
      pageItem.addEventListener('click', () => {
        reviewPaginationItem.forEach((v) => v.classList.remove('is-active'));
        pageItem.classList.add('is-active');
        reviewCurrentPage = parseInt(pageItem.childNodes[0].innerHTML);
        console.log(reviewCurrentPage);
        if (reviewCurrentPage === 1) {
          detailReviewWrapper.innerHTML = '';
          const data = [...review].slice(
            reviewCurrentPage - 1,
            reviewCurrentPage * 7
          );
          renderReview(data);
        } else {
          console.log(reviewCurrentPage);
          console.log((reviewCurrentPage - 1) * 7, reviewCurrentPage * 7);
          const data2 = [...review].slice(
            (reviewCurrentPage - 1) * 7,
            reviewCurrentPage * 7
          );
          console.log(data2);
          detailReviewWrapper.innerHTML = '';
          renderReview(data2);
        }
      });
    }
    const detailQnaWrapper = document.querySelector(
      '.product-info__footer-qna'
    );
    console.log(qna);
    const qnaQnaList = [...qna].filter((v) => v.RE_STEP === 0);
    const qnaComment = [...qna].filter((comment) => comment.RE_STEP === 1);
    qnaComment.map((comment) => {
      qnaQnaList.map((qna) => {
        if (comment.REF === qna.REF) {
          qna.comment = comment;
        }
      });
    });
    console.log(qnaQnaList);

    const renderQna = (qna) => {
      console.log(qna);
      const renderQnaWrapper = document.querySelector(
        '.product-info__footer-qna'
      );
      const renderQnaTitle = document.createElement('h1');
      renderQnaTitle.classList.add('product-info__footer-qna--title');
      renderQnaTitle.textContent = 'Q&A';
      renderQnaWrapper.appendChild(renderQnaTitle);
      const renderQnaHeader = document.createElement('div');
      renderQnaHeader.classList.add('product-info__footer-qna--header');
      const addQnaHeaderItem = (content) => {
        const headerItem = document.createElement('div');
        headerItem.classList.add('product-info__footer-qna--header-item');
        headerItem.textContent = content;
        renderQnaHeader.appendChild(headerItem);
      };
      addQnaHeaderItem('ID');
      addQnaHeaderItem('카테고리');
      addQnaHeaderItem('제목');
      addQnaHeaderItem('작성일자');
      renderQnaWrapper.appendChild(renderQnaHeader);
      const qnaWrapper = document.createElement('div');
      qnaWrapper.classList.add('.product-info__footer-main-wrapper');
      renderQnaWrapper.appendChild(qnaWrapper);
      qna.map((item, index) => {
        const qnaMainWrapper = document.createElement('div');
        qnaMainWrapper.classList.add('product-info__footer-qna-main');
        qnaWrapper.appendChild(qnaMainWrapper);
        const qnaMainItems = document.createElement('div');
        qnaMainItems.classList.add('product-info__footer-qna--main-items');
        qnaMainWrapper.appendChild(qnaMainItems);
        for (let i = 0; i < 5; i++) {
          const qnaMainItem = document.createElement('div');
          qnaMainItem.classList.add('product-info__footer-qna--main-item');
          if (i === 0) {
            qnaMainItem.textContent = item.MEM_ID;
          } else if (i === 1) {
            if (item.QNA_TYPE === 'D') {
              qnaMainItem.textContent = '배송';
            } else if (item.QNA_TYPE === 'E') {
              qnaMainItem.textContent = '기타';
            } else if (item.QNA_TYPE === 'P') {
              qnaMainItem.textContent = '상품';
            }
          } else if (i === 2) {
            qnaMainItem.textContent = item.TITLE;
            qnaMainItem.classList.add('qna-item__title');
            qnaMainItem.addEventListener('click', () => {
              const qnaMainContent = document.querySelectorAll(
                '.product-info__footer-qna--main-content'
              );
              qnaMainContent[index].classList.toggle('open');
            });
          } else if (i === 3) {
            qnaMainItem.textContent = item.REG_DATE;
          } else if(i ===4){
         qnaMainItem.textContent = item.QNA_IDX;
         qnaMainItem.classList.add('root_idx');
            qnaMainItem.classList.add('disable');
      }
          qnaMainItems.appendChild(qnaMainItem);
        }
        $(".disable").css("display", "none");
        const qnaMainContent = document.createElement('div');
        qnaMainContent.classList.add('product-info__footer-qna--main-content');
        const qnaMainContentTitle = document.createElement('span');
        qnaMainContentTitle.textContent = item.CONTENT;
        qnaMainContent.appendChild(qnaMainContentTitle);
        if(admin.innerHTML == 'Y'){
   
        const qnaMainContentButton = document.createElement('button');
        qnaMainContentButton.type = 'button';
        qnaMainContentButton.classList.add('btn-secondary');
        qnaMainContentButton.classList.add('qna-content-add');
        qnaMainContentButton.textContent = '답변 달기';
        qnaMainContent.appendChild(qnaMainContentButton);
      }
        
        
        qnaMainWrapper.appendChild(qnaMainContent);
        if (item.comment !== undefined) {
          const qnaCommentWrapper = document.createElement('div');
          qnaCommentWrapper.classList.add(
            'product-info__footer-qna--commnet-items'
          );
          for (let i = 0; i < 4; i++) {
            const qnaCommentItem = document.createElement('div');
            qnaCommentItem.classList.add(
              'product-info__footer-qna--comment-item'
            );
            switch (i) {
              case 0:
                qnaCommentItem.textContent = item.comment.MEM_ID;
                break;
              case 1:
                qnaCommentItem.textContent = '답변';
                break;
              case 2:
                qnaCommentItem.textContent = `[답변] ${item.comment.TITLE}`;
                qnaCommentItem.classList.add('qna-item__title');
                qnaCommentItem.addEventListener('click', (e) => {
                  e.target.parentNode.parentNode.childNodes[3].classList.toggle(
                    'open'
                  );
                });
                break;
              case 3:
                qnaCommentItem.textContent = item.comment.REG_DATE;
                break;
            }
            qnaCommentWrapper.appendChild(qnaCommentItem);
          }
          const qnaCommentArrow = document.createElement('div');
          qnaCommentArrow.classList.add('comment-arrow');
          const arrow = document.createElement('i');
          arrow.classList.add('bi-arrow-return-right');
          arrow.classList.add('bi');
          qnaCommentArrow.appendChild(arrow);
          const qnaCommentContent = document.createElement('div');
          qnaCommentContent.classList.add(
            'product-info__footer-qna--comment-content'
          );
          const qnaCommentContentTitle = document.createElement('span');
          qnaCommentContentTitle.textContent = item.comment.CONTENT;
          qnaCommentWrapper.appendChild(qnaCommentArrow);
          qnaCommentContent.appendChild(qnaCommentContentTitle);
          qnaMainWrapper.appendChild(qnaCommentWrapper);
          qnaMainWrapper.appendChild(qnaCommentContent);
        }
      });
      
      
      const answerButton = document.querySelectorAll('.qna-content-add');
      const root_idx = document.querySelectorAll('.root_idx');
      for(let i=0; i<answerButton.length; i++){
         answerButton[i].addEventListener('click', ()=> {
            window.open(
               '/qna/write?PRO_IDX='+productNumber.value+'&ROOT_IDX='+root_idx[i].innerHTML,
               'Child',
               'width = 670, height = 800'
            );
         })
      }
      
      
      renderQnaWrapper.appendChild(qnaWrapper);
    };
    if (qnaCurrentPage === 1) {
      detailQnaWrapper.innerHTML = '';
      const data = [...qnaQnaList].slice(0, 8);
      renderQna(data);
    }
    renderQnaPageList([...qnaQnaList], qnaCurrentPage, renderQna);
    const qnaPaginationItem = document.querySelectorAll('.qna-page-item');
    qnaPaginationItem[0].classList.add('is-active');
    for (const pageItem of qnaPaginationItem) {
      pageItem.addEventListener('click', () => {
        qnaPaginationItem.forEach((v) => v.classList.remove('is-active'));
        pageItem.classList.add('is-active');
        qnaCurrentPage = parseInt(pageItem.childNodes[0].innerHTML);
        console.log(qnaCurrentPage);
        if (qnaCurrentPage === 1) {
          detailQnaWrapper.innerHTML = '';
          const data = [...qnaQnaList].slice(
            qnaCurrentPage - 1,
            qnaCurrentPage * 8
          );
          renderQna(data);
        } else {
          console.log(qnaCurrentPage);
          console.log((qnaCurrentPage - 1) * 8, qnaCurrentPage * 8);
          const data2 = [...qnaQnaList].slice(
            (qnaCurrentPage - 1) * 8,
            qnaCurrentPage * 8
          );
          console.log(data2);
          detailQnaWrapper.innerHTML = '';
          renderQna(data2);
        }
      });
    }
  } catch (err) {
    console.error(err);
  }
};
const renderPageList = (list, reviewCurrentPage, renderReview) => {
  const newList = [...list];
  const pageListWrapper = document.querySelector('.review-page-list');
  const renderPageItem = (i) => {
    const pageListItem = document.createElement('li');
    pageListItem.classList.add('page-item');
    pageListItem.classList.add('pc-item__page');
    pageListItem.classList.add(`review-page-item`);
    const pageListSpan = document.createElement('span');
    pageListSpan.textContent = i + 1;
    pageListItem.appendChild(pageListSpan);
    pageListWrapper.appendChild(pageListItem);
  };
  console.log(pageListWrapper);
  for (let i = 0; i < Math.ceil(newList.length / 8); i++) {
    renderPageItem(i);
  }
  const reviewPageItem = document.querySelectorAll('.review-page-item');
  const reviewPageBlock = Math.ceil(reviewPageItem.length / 5);
  let reviewCurrentBlock = 0;
  const nextButton = document.querySelector('.review-page-next');
  const prevButton = document.querySelector('.review-page-prev');
  
  reviewPageItem.forEach((v) => {
    v.style.display = 'none';
  });
  
  if (reviewPageBlock > 1) {
	  for (let i = 0; i < 5; i++) {
	    reviewPageItem[i].style.display = 'block';
	  }
  } else if (reviewPageBlock === 1) {
    for (let i = 0; i < reviewPageItem.length; i++) {
      reviewPageItem[i].style.display = 'block';
    }
  }

  nextButton.addEventListener('click', () => {
    if (reviewCurrentBlock < reviewPageBlock - 1) {
      reviewPageItem.forEach((v) => {
        v.style.display = 'none';
        v.classList.remove('is-active');
      });
    }
    if (reviewCurrentBlock < reviewPageBlock - 1) {
      reviewCurrentBlock++;
      reviewCurrentPage = reviewCurrentBlock * 5 + 1;
      const detailReviewWrapper = document.querySelector(
        '.product-info__footer-review'
      );
      const data2 = [...list].slice(
        (reviewCurrentPage - 1) * 7,
        reviewCurrentPage * 7
      );
      detailReviewWrapper.innerHTML = '';
      renderReview(data2);
      reviewPageItem[reviewCurrentPage - 1].classList.add('is-active');
      for (
        let i = reviewCurrentBlock * 5;
        i < (reviewCurrentBlock + 1) * 5;
        i++
      ) {
        if (i < reviewPageItem.length) {
          reviewPageItem[i].style.display = 'block';
        }
      }
    } else {
      return;
    }
  });
  prevButton.addEventListener('click', () => {
    if (reviewCurrentBlock > 0) {
      reviewCurrentBlock--;
      console.log(reviewCurrentBlock);
      reviewPageItem.forEach((v) => {
        v.style.display = 'none';
        v.classList.remove('is-active');
      });
      reviewCurrentPage = reviewCurrentBlock * 5 + 1;
      const detailReviewWrapper = document.querySelector(
        '.product-info__footer-review'
      );
      const data2 = [...list].slice(
        (reviewCurrentPage - 1) * 7,
        reviewCurrentPage * 7
      );
      detailReviewWrapper.innerHTML = '';
      renderReview(data2);
      reviewPageItem[reviewCurrentPage - 1].classList.add('is-active');
      for (
        let i = reviewCurrentBlock * 5;
        i < (reviewCurrentBlock + 1) * 5;
        i++
      ) {
        if (i < reviewPageItem.length) {
          reviewPageItem[i].style.display = 'block';
        }
      }
    } else {
      return;
    }
  });
};

const renderQnaPageList = (list, qnaCurrentPage, renderQna) => {
  const totalPageItem = Math.ceil(list.length / 8);
  const qnaPageList = document.querySelector('.qna-page-list');
  console.log(totalPageItem, qnaPageList);
  for (let i = 0; i < totalPageItem; i++) {
    const pageListItem = document.createElement('li');
    pageListItem.classList.add('page-item');
    pageListItem.classList.add('pc-item__page');
    pageListItem.classList.add(`qna-page-item`);
    const pageListSpan = document.createElement('span');
    pageListSpan.textContent = i + 1;
    pageListItem.appendChild(pageListSpan);
    qnaPageList.appendChild(pageListItem);
  }
  const qnaPageItem = document.querySelectorAll('.qna-page-item');
  const qnaPageBlock = Math.ceil(qnaPageItem.length / 5);
  let qnaCurrentBlock = 0;
  const nextButton = document.querySelector('.qna-page-next');
  const prevButton = document.querySelector('.qna-page-prev');
  qnaPageItem.forEach((v) => {
    v.style.display = 'none';
  });
  if (qnaPageBlock > 1) {
    for (let i = 0; i < 5; i++) {
      qnaPageItem[i].style.display = 'block';
    }
  } else if (qnaPageBlock === 1) {
    for (let i = 0; i < qnaPageItem.length; i++) {
      qnaPageItem[i].style.display = 'block';
    }
  }
  nextButton.addEventListener('click', () => {
    if (qnaCurrentBlock < qnaPageBlock - 1) {
      qnaPageItem.forEach((v) => {
        v.style.display = 'none';
        v.classList.remove('is-active');
      });
    }
    if (qnaCurrentBlock < qnaPageBlock - 1) {
      qnaCurrentBlock++;
      qnaCurrentPage = qnaCurrentBlock * 5 + 1;
      const detailQnaWrapper = document.querySelector(
        '.product-info__footer-qna'
      );
      const data2 = [...list].slice(
        (qnaCurrentPage - 1) * 8,
        qnaCurrentPage * 8
      );
      detailQnaWrapper.innerHTML = '';
      renderQna(data2);
      qnaPageItem[qnaCurrentPage - 1].classList.add('is-active');
      for (let i = qnaCurrentBlock * 5; i < (qnaCurrentBlock + 1) * 5; i++) {
        if (i < qnaPageItem.length) {
          qnaPageItem[i].style.display = 'block';
        }
      }
    } else {
      return;
    }
    prevButton.addEventListener('click', () => {
      if (qnaCurrentBlock > 0) {
        qnaCurrentBlock--;
        qnaPageItem.forEach((v) => {
          v.style.display = 'none';
          v.classList.remove('is-active');
        });
        qnaCurrentPage = qnaCurrentBlock * 5 + 1;
        const detailQnaWrapper = document.querySelector(
          '.product-info__footer-qna'
        );
        const data2 = [...list].slice(
          (qnaCurrentPage - 1) * 8,
          qnaCurrentPage * 8
        );
        detailQnaWrapper.innerHTML = '';
        renderQna(data2);
        qnaPageItem[qnaCurrentPage - 1].classList.add('is-active');
        for (let i = qnaCurrentBlock * 5; i < (qnaCurrentBlock + 1) * 5; i++) {
          if (i < qnaPageItem.length) {
            qnaPageItem[i].style.display = 'block';
          }
        }
      } else {
        return;
      }
    });
  });
};
$(".disable").css("display", "none");
window.addEventListener('load', renderDetailPage);