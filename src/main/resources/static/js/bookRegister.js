const form = document.getElementById('registerForm');
let editor;

// Toast UI Editor 초기화
document.addEventListener("DOMContentLoaded", function () {
  editor = new toastui.Editor({
    el: document.querySelector("#bookInfoEditor"),
    height: "400px",
    initialEditType: "wysiwyg", // 'markdown' or 'wysiwyg'
    previewStyle: "vertical",  // 'tab' or 'vertical'
    initialValue: "",
    hideModeSwitch: true       // 에디터 모드 전환 버튼 숨김
  });
});

form.onsubmit = function () {
  const bookInfoField = document.querySelector("#bookInfo");
  bookInfoField.value = editor.getMarkdown();

  console.log("bookInfoField 값:", bookInfoField.value); // 디버깅용

};


document.addEventListener('DOMContentLoaded', function () {
  function handleSelection(selectElement, containerId, inputId, badgeClass, removeFunction) {
    const select = document.getElementById(selectElement);
    const container = document.getElementById(containerId);
    const hiddenInput = document.getElementById(inputId);

    select.addEventListener('change', function () {
      const selectedValue = select.value;
      const selectedText = select.options[select.selectedIndex].text;

      if (selectedValue && !container.querySelector(`[data-id="${selectedValue}"]`)) {
        // 배지 생성
        const badge = document.createElement('span');
        badge.className = `badge bg-secondary me-2 ${badgeClass}`;
        badge.setAttribute('data-id', selectedValue);
        badge.innerHTML = `
          <span>${selectedText}</span>
          <button type="button" class="btn-close ms-1 remove-badge" aria-label="Remove"></button>
        `;
        container.appendChild(badge);

        // hidden input 업데이트
        const currentValues = hiddenInput.value ? hiddenInput.value.split(',') : [];
        currentValues.push(selectedValue);
        hiddenInput.value = currentValues.join(',');
      }
    });

    // 삭제 버튼 이벤트 처리
    container.addEventListener('click', function (event) {
      if (event.target.classList.contains('remove-badge')) {
        const badge = event.target.closest(`.${badgeClass}`);
        if (badge) {
          const value = badge.getAttribute('data-id');
          badge.remove();
          removeFunction(hiddenInput, value);
        }
      }
    });
  }

  function removeValueFromInput(input, value) {
    const currentValues = input.value.split(',').filter(id => id !== value);
    input.value = currentValues.join(',');
  }

  // 작가 선택 핸들링
  handleSelection('author-select', 'selected-authors-container', 'authors', 'selected-author-badge', removeValueFromInput);

  // 태그 선택 핸들링
  handleSelection('tag-select', 'selected-tags-container', 'tags', 'selected-tag-badge', removeValueFromInput);

  // 카테고리 선택 핸들링
  handleSelection('category-select', 'selected-categories-container', 'categories', 'selected-category-badge', removeValueFromInput);
});
