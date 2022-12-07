const couponName = document.querySelector('#coupon-name__name');
const couponDiscount = document.querySelector('#coupon-discount__discount');
const couponMinDate = document.querySelector('#coupon-date__min');
const couponMaxDate = document.querySelector('#coupon-date__max');
const couponAddButton = document.querySelector('.coupon-add__button');

const ableButton = () => {
  const nameValue = couponName.value;
  const discountValue = couponDiscount.value;
  const minDateValue = couponMinDate.value;
  const maxDateValue = couponMaxDate.value;
  console.log(nameValue, discountValue, minDateValue, maxDateValue);
  if (
    nameValue !== '' &&
    discountValue !== '' &&
    minDateValue !== '' &&
    maxDateValue !== ''
  ) {
    couponAddButton.disabled = false;
  }
};

const inputEvent = (input) => {
  return input.addEventListener('input', () => {
    ableButton();
  });
};
inputEvent(couponName);
inputEvent(couponDiscount);
inputEvent(couponMinDate);
inputEvent(couponMaxDate);
