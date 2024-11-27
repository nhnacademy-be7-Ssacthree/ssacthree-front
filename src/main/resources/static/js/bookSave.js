const form = document.getElementById('registerForm');
let editor;

document.addEventListener("DOMContentLoaded", function () {
  // Toast UI Editor 초기화
  editor = new toastui.Editor({
    el: document.querySelector("#bookInfoEditor"),
    height: "400px",
    initialEditType: "wysiwyg",  // 기본 값: WYSIWYG, 'markdown'으로도 설정 가능
    previewStyle: "vertical",    // 미리보기 스타일: 'tab' 또는 'vertical'
    initialValue: "",            // 초기값
    hideModeSwitch: false,       // 마크다운과 WYSIWYG 모드 전환 버튼을 표시
  });

  // 폼 제출 시 에디터 내용을 숨겨진 textarea에 반영
  const form = document.querySelector("#registerForm");
  form.addEventListener("submit", function () {
    const bookInfoField = document.querySelector("#bookInfo");
    // 에디터 내용을 textarea에 설정
    bookInfoField.value = editor.getMarkdown();

    // 디버깅용 로그
    console.log("bookInfoField 값:", bookInfoField.value);
  });
});


document.getElementById('bookIsbn').addEventListener('input', function (event) {
  const value = event.target.value;
  // 숫자가 아닌 문자는 제거
  const numericValue = value.replace(/\D/g, '');
  // 13자리까지만 허용
  if (numericValue.length > 13) {
    event.target.value = numericValue.substring(0, 13);
  } else {
    event.target.value = numericValue;
  }
});


$(document).ready(function () {
  // 카테고리 선택 이벤트 처리
  $('#category-dropdown').change(function () {
    const selectedCategoryId = $(this).val();
    const selectedCategoryName = $(this).find('option:selected').text();

    if (selectedCategoryId) {
      $('#selected-categories-container').append(`
        <div class="tag-badge" data-id="${selectedCategoryId}">
          ${selectedCategoryName}
          <button type="button" class="remove-tag-btn" data-id="${selectedCategoryId}">x</button>
        </div>
      `);


      updateHiddenInput('#categories', selectedCategoryId);
    }
  });

  // 태그 선택 이벤트 처리
  $('#tag-dropdown').change(function () {
    const selectedTagId = $(this).val();
    const selectedTagName = $(this).find('option:selected').text();

    if (selectedTagId) {
      $('#selected-tags-container').append(`
        <div class="tag-badge" data-id="${selectedTagId}">
          ${selectedTagName}
          <button type="button" class="remove-tag-btn" data-id="${selectedTagId}">x</button>
        </div>
      `);


      updateHiddenInput('#tags', selectedTagId);
    }
  });

  // 작가 선택 이벤트 처리
  $('#author-dropdown').change(function () {
    const selectedAuthorId = $(this).val();
    const selectedAuthorName = $(this).find('option:selected').text();

    if (selectedAuthorId) {
      $('#selected-authors-container').append(`
        <div class="tag-badge" data-id="${selectedAuthorId}">
          ${selectedAuthorName}
          <button type="button" class="remove-tag-btn" data-id="${selectedAuthorId}">x</button>
        </div>
      `);

      updateHiddenInput('#authors', selectedAuthorId);
    }
  });

  // Hidden input 업데이트
  function updateHiddenInput(inputId, value) {
    const hiddenInput = $(inputId);
    const currentValues = hiddenInput.val() ? hiddenInput.val().split(',') : [];
    if (!currentValues.includes(value)) {
      currentValues.push(value);
    }
    hiddenInput.val(currentValues.join(','));
  }

  // 삭제 버튼 이벤트 처리
  $(document).on('click', '.remove-tag-btn', function () {
    const id = $(this).data('id');
    $(this).parent().remove();

    removeHiddenInputValue('#categories', id);
    removeHiddenInputValue('#tags', id);
    removeHiddenInputValue('#authors', id);
  });

  function removeHiddenInputValue(inputId, value) {
    const hiddenInput = document.querySelector(inputId);
    const currentValues = hiddenInput.value ? hiddenInput.value.split(',') : [];
    const newValues = currentValues.filter((item) => item !== value);
    hiddenInput.value = newValues.join(',');
  }
});

// const regularPriceInput = document.getElementById('regularPrice');
// const salePriceInput = document.getElementById('salePrice');
// const discountRateInput = document.getElementById('discountRate');
// const bookDiscountInput = document.getElementById('bookDiscount');
//
// function calculateDiscount() {
//   const regularPrice = parseFloat(regularPriceInput.value);
//   const salePrice = parseFloat(salePriceInput.value);
//
//   if (!isNaN(regularPrice) && !isNaN(salePrice) && regularPrice > 0) {
//     const discountRate = ((regularPrice - salePrice) / regularPrice) * 100;
//     discountRateInput.value = discountRate.toFixed(2); // 소수점 2자리까지 표시
//     bookDiscountInput.value = discountRate.toFixed(2);
//   } else {
//     discountRateInput.value = '';
//   }
// }
//
// regularPriceInput.addEventListener('input', calculateDiscount);
// salePriceInput.addEventListener('input', calculateDiscount);
//
