// bookUpdate.js
const form = document.getElementById('updateForm');

document.addEventListener("DOMContentLoaded", function () {
  // Toast UI Editor 초기화
  const editor = new toastui.Editor({
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

  // 공통 함수: Hidden Input 값 업데이트
  function updateHiddenInput(input, value) {
    let values = input.value ? input.value.split(",") : [];
    if (!values.includes(value)) {
      values = [value]; // 기존 값을 제거하고 새 값만 설정
    }
    input.value = values.join(",");
  }

  function removeHiddenInputValue(input, value) {
    let values = input.value ? input.value.split(",") : [];
    input.value = values.filter((v) => v !== value).join(",");
  }


  // 공통 로직: 선택 이벤트와 삭제 이벤트 처리
  function setupSelectHandler(selectId, containerId, hiddenInputId) {
    const select = document.getElementById(selectId);
    const container = document.getElementById(containerId);
    const hiddenInput = document.getElementById(hiddenInputId);

    // 선택 이벤트 처리
    select.addEventListener("change", function () {
      const selectedId = this.value;
      const selectedName = this.options[this.selectedIndex].text;

      if (selectedId) {
        // 중복 선택 방지
        if (Array.from(container.children).some((child) => child.dataset.id === selectedId)) {
          return;
        }

        // 태그 생성 및 추가
        const badge = document.createElement("span");
        badge.className = "tag-badge";
        badge.dataset.id = selectedId;
        badge.innerHTML = `
          ${selectedName}
          <button type="button" class="remove-tag-btn" data-id="${selectedId}">x</button>
        `;
        container.appendChild(badge);

        // Hidden Input 업데이트
        updateHiddenInput(hiddenInput, selectedId);
      }
    });

    // 삭제 이벤트 처리
    container.addEventListener("click", function (e) {
      if (e.target.classList.contains("remove-tag-btn")) {
        const id = e.target.dataset.id;
        const badge = e.target.closest(".tag-badge");
        badge.remove(); // 태그 제거
        removeHiddenInputValue(hiddenInput, id); // Hidden Input 값 제거
      }
    });
  }

  // 카테고리, 태그, 작가 선택 핸들러 설정
  setupSelectHandler("category-select", "selected-categories-container", "categories");
  setupSelectHandler("tag-select", "selected-tags-container", "tags");
  setupSelectHandler("author-select", "selected-authors-container", "authors");
});
