const form = document.getElementById('registerForm');
let editorSave;

const regularPriceInputSave = document.getElementById('regularPrice');
const salePriceInputSave = document.getElementById('salePrice');
const bookDiscountDisplaySave = document.getElementById('bookDiscountDisplay');
const bookDiscountInputSave = document.getElementById('bookDiscount'); // hidden 필드

document.addEventListener("DOMContentLoaded", function () {
  // Toast UI Editor 초기화
  editorSave = new toastui.Editor({
    el: document.querySelector("#bookInfoEditor"),
    height: "400px",
    initialEditType: "wysiwyg",
    previewStyle: "vertical",
    initialValue: "",
    hideModeSwitch: false,
  });

  // 폼 제출 시 에디터 내용을 숨겨진 textarea에 반영
  form.addEventListener("submit", function () {
    const bookInfoFieldSave = document.querySelector("#bookInfo");
    bookInfoFieldSave.value = editorSave.getMarkdown();
    updateHiddenInputSave("#categories", categoryClickCountsSave);
    updateHiddenInputSave("#tags", tagClickCountsSave);
    updateHiddenInputSave("#authors", authorClickCountsSave);

  });

  function calculateDiscountSave() {
    const regularPriceSave = parseFloat(regularPriceInputSave.value);
    const salePriceSave = parseFloat(salePriceInputSave.value);

    if (!isNaN(regularPriceSave) && !isNaN(salePriceSave) && regularPriceSave > 0) {
      const discountRateSave = ((regularPriceSave - salePriceSave) / regularPriceSave) * 100;
      const discountRateIntSave = Math.floor(discountRateSave);

      bookDiscountDisplaySave.value = discountRateIntSave;
      bookDiscountInputSave.value = discountRateIntSave;
    } else {
      bookDiscountDisplaySave.value = '';
      bookDiscountInputSave.value = '';
    }
  }

  regularPriceInputSave.addEventListener('input', calculateDiscountSave);
  salePriceInputSave.addEventListener('input', calculateDiscountSave);

  const categoryClickCountsSave = new Map();
  const tagClickCountsSave = new Map();
  const authorClickCountsSave = new Map();
// 선택 핸들러 설정
  setupSelectHandlerSave("category-dropdown", "selected-categories-container", categoryClickCountsSave);
  setupSelectHandlerSave("tag-dropdown", "selected-tags-container", tagClickCountsSave);
  setupSelectHandlerSave("author-dropdown", "selected-authors-container", authorClickCountsSave);

  // 폼 제출 시 Hidden Input 업데이트
  const formSave = document.querySelector("form");
  formSave.addEventListener("submit", function () {
    updateHiddenInputSave("#categories", categoryClickCountsSave);
    updateHiddenInputSave("#tags", tagClickCountsSave);
    updateHiddenInputSave("#authors", authorClickCountsSave);
  });

  function setupSelectHandlerSave(selectIdSave, containerIdSave, clickCountsSave) {
    const selectSave = document.getElementById(selectIdSave);
    const containerSave = document.getElementById(containerIdSave);

    // 선택 이벤트 처리
    selectSave.addEventListener("change", function () {
      const selectedIdSave = selectSave.value;
      const selectedNameSave = selectSave.options[selectSave.selectedIndex]?.text;

      if (selectedIdSave) {
        incrementClickCountSave(clickCountsSave, selectedIdSave);
        // 이미 추가된 항목이라도 다시 렌더링
        containerSave.insertAdjacentHTML("beforeend", `
          <span class="tag-badge" data-id="${selectedIdSave}">
            ${selectedNameSave}
            <button type="button" class="remove-tag-btn" data-id="${selectedIdSave}">x</button>
          </span>
        `);
      }
    });

    // 삭제 버튼 이벤트 처리
    containerSave.addEventListener("click", function (eSave) {
      if (eSave.target.classList.contains("remove-tag-btn")) {
        const idSave = eSave.target.dataset.id;
        if (idSave) {
          incrementClickCountSave(clickCountsSave, idSave);
          eSave.target.closest(".tag-badge").remove();
        }
      }
    });
  }

  function incrementClickCountSave(clickCountsSave, idSave) {
    if (!clickCountsSave.has(idSave)) {
      clickCountsSave.set(idSave, 0); // 초기화
    }
    clickCountsSave.set(idSave, clickCountsSave.get(idSave) + 1);
  }

  function updateHiddenInputSave(inputIdSave, clickCountsSave) {
    const hiddenInputSave = document.querySelector(inputIdSave);
    const resultSave = Array.from(clickCountsSave.entries())
    .filter(([_, countSave]) => countSave % 2 === 1) // 홀수만 필터링
    .map(([idSave]) => idSave);
    hiddenInputSave.value = resultSave.join(",");
    console.log(`Updated ${inputIdSave}:`, hiddenInputSave.value); // 디버깅용 로그
  }
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

