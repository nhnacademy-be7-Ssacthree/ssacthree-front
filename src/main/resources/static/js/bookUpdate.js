// bookUpdate.js
const form = document.getElementById('updateForm');
let editor;

document.addEventListener("DOMContentLoaded", function () {
  // Toast UI Editor 초기화
  editor = new toastui.Editor({
    el: document.querySelector("#bookInfoEditor"), // 에디터가 렌더링될 DOM 요소
    height: "400px",
    initialEditType: "wysiwyg", // 기본 편집 타입: WYSIWYG
    previewStyle: "vertical", // 미리보기 스타일
    initialValue: document.querySelector("#bookInfo").textContent || "", // textarea 값 사용
    hooks: {
      // 에디터 내용 변경 시 textarea에 반영
      change: function () {
        document.querySelector("#bookInfo").value = editor.getHTML();
      },
    },
  });

  // 폼 제출 시 데이터 동기화
  const form = document.querySelector("#bookUpdateForm");
  if (form) {
    form.addEventListener("submit", function () {
      // 에디터의 최종 HTML 내용을 textarea에 저장
      document.querySelector("#bookInfo").value = editor.getHTML();
    });
  }
  function updateHiddenInput(input, value) {
    let values = input.value ? input.value.split(",") : [];
    if (!values.includes(value)) {
      values.push(value); // 새로운 값 추가
    }
    input.value = values.join(",");
  }

  function removeHiddenInputValue(input, value) {
    let values = input.value ? input.value.split(",") : [];
    input.value = values.filter((v) => v !== value).join(","); // 삭제된 값 제외
  }

  function setupSelectHandler(selectId, containerId, hiddenInputId) {
    const select = document.getElementById(selectId);
    const container = document.getElementById(containerId);
    const hiddenInput = document.getElementById(hiddenInputId);

    select.addEventListener("change", function () {
      const selectedId = this.value;
      const selectedName = this.options[this.selectedIndex].text;

      if (selectedId && !Array.from(container.children).some((child) => child.dataset.id === selectedId)) {
        const badge = document.createElement("span");
        badge.className = "tag-badge";
        badge.dataset.id = selectedId;
        badge.innerHTML = `
          ${selectedName}
          <button type="button" class="remove-tag-btn" data-id="${selectedId}">x</button>
        `;
        container.appendChild(badge);
        updateHiddenInput(hiddenInput, selectedId);
      }
    });

    container.addEventListener("click", function (e) {
      if (e.target.classList.contains("remove-tag-btn")) {
        const id = e.target.dataset.id;
        const badge = e.target.closest(".tag-badge");
        badge.remove();
        removeHiddenInputValue(hiddenInput, id); // Hidden Input 값에서 제거
      }
    });
  }

  // 적용
  setupSelectHandler("category-select", "selected-categories-container", "categories");
  setupSelectHandler("tag-select", "selected-tags-container", "tags");
  setupSelectHandler("author-select", "selected-authors-container", "authors");


});

// const regularPriceInputUpdate = document.getElementById('regularPrice');
// const salePriceInputUpdate = document.getElementById('salePrice');
// const discountRateInputUpdate = document.getElementById('discountRate');
// const bookDiscountInputUpdate = document.getElementById('bookDiscount'); // Hidden input
//
// function calculateDiscountUpdate() {
//   const regularPriceUpdate = parseFloat(regularPriceInputUpdate.value);
//   const salePriceUpdate = parseFloat(salePriceInputUpdate.value);
//
//   if (!isNaN(regularPriceUpdate) && !isNaN(salePriceUpdate) && regularPriceUpdate > 0) {
//     const discountRate = ((regularPriceUpdate - salePriceUpdate) / regularPriceUpdate) * 100;
//     discountRateInputUpdate.value = discountRate.toFixed(2); // 소수점 2자리까지 표시
//     bookDiscountInputUpdate.value = discountRate.toFixed(2);
//   } else {
//     discountRateInputUpdate.value = '';
//     bookDiscountInputUpdate.value = '';
//   }
// }
//
// regularPriceInputUpdate.addEventListener('input', calculateDiscountUpdate);
// salePriceInputUpdate.addEventListener('input', calculateDiscountUpdate);