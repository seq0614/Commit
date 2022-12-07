const idInputEl = document.querySelector('#sign-up__id');
const pwInputEl = document.querySelector('.sign-up__input--pw');
const confirmInputEl = document.querySelector('.sign-up__input--confirm');
const nameInputEl = document.querySelector('.sign-up__input--name');
const warningMessage = document.querySelector('.warning-message');
const signupButton = document.querySelector('.signup__button--disabled');
const emailInputEl = document.querySelector('#sign-up__email');
const selectValue = document.querySelector('.email-select');
const directInput = document.querySelector('.email-direct');

const confirmIdButton = document.querySelector('.btn-secondary');
const confirmEmailButton = document.querySelector('.email-check');
const disable = document.querySelectorAll('.disable');

let idValue = '';
let pwValue = '';
let confirmValue = '';
let nameValue = '';
let emailValue = '';
let warningState = false;
let idValid = false;//아이디 중복 확인
let emailValid = false;//이메일 중복 확인

const confirmPassword = () => {
  const passWordValue = pwInputEl.value;
  const passWordConfirmValue = confirmInputEl.value;

  if (passWordValue !== passWordConfirmValue) {
    warningMessage.classList.add('warning');
    warningState = true;
  } else {
    warningMessage.classList.remove('warning');
    warningState = false;
  }
};

const buttonActive = () => {
  console.log(warningState);
  if(disable != null){
    idValue = 'temp';
    nameValue = 'temp';
    idValid = true;
  }
  signupButton.disabled = !(idValue !== '' &&
      pwValue !== '' &&
      confirmValue !== '' &&
      nameValue !== '' &&
      emailValue !== '' &&
      warningState === false &&
      idValid === true &&
      emailValid === true);
};

idInputEl.addEventListener('input', (e) => {
  idValue = e.target.value;
  buttonActive();
});

pwInputEl.addEventListener('input', (e) => {
  confirmPassword();
  pwValue = e.target.value;
  buttonActive();
});

confirmInputEl.addEventListener('input', (e) => {
  confirmPassword();
  confirmValue = e.target.value;
  buttonActive();
});

nameInputEl.addEventListener('input', (e) => {
  nameValue = e.target.value;
  buttonActive();
});

emailInputEl.addEventListener('input', (e) => {
  emailValue = e.target.value;
  buttonActive();
});

const emailSelect = () => {
  let value = selectValue.options[selectValue.selectedIndex].text;
  if (value === '직접입력') {
    directInput.readOnly = false;
    directInput.value = '';
    directInput.focus();
  } else {
    directInput.readOnly = true;
    directInput.value = value;
  }
};

selectValue.addEventListener('change', emailSelect);

//아이디 중복 확인
confirmIdButton.addEventListener('click', checkId);

function checkId(){
  const inputId = idInputEl.value;
  if(inputId !== ''){
    confirmIdAjax(inputId);
  } else {
    alert('아이디를 입력해주세요.');
    idInputEl.focus();
  }

}

function confirmIdAjax(id) {

  let sendId = {"MEM_ID" : id}

  $.ajax({
    type: 'post',
    url: '/member/confirm/id',
    data: JSON.stringify(sendId),
    contentType: 'application/json; charset=utf-8',
    dataType: 'text',
    success: function (data) {

      const check = parseInt(data);
      if(check === 1){
        alert('이미 사용 중인 아이디입니다.');
        idValid = false;
        idInputEl.focus();
      } else {
        alert('사용 가능한 아이디입니다.');
        idValid = true;
        confirmIdButton.disabled = true;
      }
      buttonActive();

    },
    error: function (error) {
      console.log(error.status);
    }
  });
}
//아이디 중복확인 후 변경했을때
idInputEl.addEventListener('input', function(){

  confirmIdButton.disabled = false;
  idValid = false;
  buttonActive();
})

//이메일 중복 확인
confirmEmailButton.addEventListener('click', checkEmail);

function checkEmail(){

  const email = emailInputEl.value;
  const at = '@';
  const domain = directInput.value;
  const totalEmail = email + at + domain;

  if(email !== '' && domain !== ''){
    confirmEmailAjax(totalEmail);
  }
  else{
    alert('이메일을 입력해주세요.');
    email.focus();
  }
}

function confirmEmailAjax(email) {

  const sendEmail = {"EMAIL" : email}

  $.ajax({
    type: 'post',
    url: '/member/confirm/email',
    data: JSON.stringify(sendEmail),
    contentType: 'application/json; charset=utf-8',
    dataType: 'text',
    success: function (data) {

      const check = parseInt(data);
      if(check === 1){
        alert('이미 사용 중인 이메일입니다.');
        emailValid = false;
        emailInputEl.focus();
      } else {
        alert('사용 가능한 이메일입니다.');
        emailValid = true;
        confirmEmailButton.disabled = true;
      }
      buttonActive();

    },
    error: function (error) {
      console.log(error.status);
    }
  });

}

function detectChange(){

  confirmEmailButton.disabled = false;
  emailValid = false;
  buttonActive();

}

//이메일 중복 확인 후 변경했을때(이메일 앞부분)
emailInputEl.addEventListener('input', detectChange);

//이메일 중복 확인 후 변경했을때(이메일 뒷부분 select)
selectValue.addEventListener('change', detectChange);

//이메일 중복 확인 후 변경했을때(이메일 뒷부분 직접입력)
directInput.addEventListener('input', detectChange);