const formUpdate = document.getElementById('updateForm');
let editorUpdate;

const regularPriceInputUpdate = document.getElementById('regularPrice');
const salePriceInputUpdate = document.getElementById('salePrice');
const bookDiscountDisplayUpdate = document.getElementById('bookDiscountDisplay');
const bookDiscountInputUpdate = document.getElementById('bookDiscount'); // Hidden 필드


document.addEventListener("DOMContentLoaded", function () {
  editorUpdate = new toastui.Editor({
    el: document.querySelector("#bookInfoEditor"),
    height: "400px",
    initialEditType: "wysiwyg",
    previewStyle: "vertical",
    initialValue: document.querySelector("#bookInfo").textContent || "",
    hideModeSwitch: false,
  });

  formUpdate.addEventListener("submit", function () {
    const bookInfoFieldUpdate = document.querySelector("#bookInfo");
    bookInfoFieldUpdate.value = editorUpdate.getMarkdown();
    updateHiddenInputUpdate("#categories", categoryClickCountsUpdate);
    updateHiddenInputUpdate("#tags", tagClickCountsUpdate);
    updateHiddenInputUpdate("#authors", authorClickCountsUpdate);
  });

  function calculateDiscountUpdate() {
    const regularPriceUpdate = parseFloat(regularPriceInputUpdate.value);
    const salePriceUpdate = parseFloat(salePriceInputUpdate.value);

    if (!isNaN(regularPriceUpdate) && !isNaN(salePriceUpdate) && regularPriceUpdate > 0) {
      const discountRateUpdate = ((regularPriceUpdate - salePriceUpdate) / regularPriceUpdate) * 100;
      const discountRateIntUpdate = Math.floor(discountRateUpdate);

      bookDiscountDisplayUpdate.value = discountRateIntUpdate;
      bookDiscountInputUpdate.value = discountRateIntUpdate;
    } else {
      bookDiscountDisplayUpdate.value = '';
      bookDiscountInputUpdate.value = '';
    }
  }

  regularPriceInputUpdate.addEventListener("input", calculateDiscountUpdate);
  salePriceInputUpdate.addEventListener("input", calculateDiscountUpdate);

  // 클릭 횟수 기반 데이터 구조
  const categoryClickCountsUpdate = new Map();
  const tagClickCountsUpdate = new Map();
  const authorClickCountsUpdate = new Map();

  // 초기 데이터 동기화
  initializeClickCounts("selected-categories-container", categoryClickCountsUpdate);
  initializeClickCounts("selected-tags-container", tagClickCountsUpdate);
  initializeClickCounts("selected-authors-container", authorClickCountsUpdate);

  setupSelectHandlerUpdate("category-select", "selected-categories-container", categoryClickCountsUpdate);
  setupSelectHandlerUpdate("tag-select", "selected-tags-container", tagClickCountsUpdate);
  setupSelectHandlerUpdate("author-select", "selected-authors-container", authorClickCountsUpdate);

  function initializeClickCounts(containerId, clickCounts) {
    const container = document.getElementById(containerId);
    Array.from(container.children).forEach((child) => {
      const id = child.getAttribute("data-id");
      if (id) {
        clickCounts.set(id, 1); // 초기화
      }
    });
    updateHiddenInputUpdate(`#${containerId.split('-')[1]}`, clickCounts); // 초기 값 설정
  }

  function setupSelectHandlerUpdate(selectId, containerId, clickCounts) {
    const select = document.getElementById(selectId);
    const container = document.getElementById(containerId);

    select.addEventListener("change", function () {
      const selectedId = select.value;
      const selectedName = select.options[select.selectedIndex]?.text;

      if (selectedId) {
        incrementClickCount(clickCounts, selectedId);
        renderTag(container, selectedName, selectedId, clickCounts);
      }
    });

    container.addEventListener("click", function (e) {
      if (e.target.classList.contains("remove-tag-btn")) {
        const id = e.target.dataset.id;
        if (id) {
          incrementClickCount(clickCounts, id);
          renderTag(container, null, id, clickCounts);
        }
      }
    });
  }

  function incrementClickCount(clickCounts, id) {
    if (!clickCounts.has(id)) {
      clickCounts.set(id, 0); // 초기화
    }
    clickCounts.set(id, clickCounts.get(id) + 1);
  }

  function renderTag(container, name, id, clickCounts) {
    const count = clickCounts.get(id);
    if (count % 2 === 1) {
      // 홀수면 추가
      if (!container.querySelector(`[data-id="${id}"]`)) {
        container.insertAdjacentHTML(
            "beforeend",
            `<span class="tag-badge" data-id="${id}">
            ${name}
            <button type="button" class="remove-tag-btn" data-id="${id}">x</button>
          </span>`
        );
      }
    } else {
      // 짝수면 삭제
      const tag = container.querySelector(`[data-id="${id}"]`);
      if (tag) {
        tag.remove();
      }
    }
  }
// Hidden Input 업데이트 수정
  function updateHiddenInputUpdate(inputIdUpdate, clickCountsUpdate) {
    const hiddenInputUpdate = document.querySelector(inputIdUpdate);
    const resultUpdate = Array.from(clickCountsUpdate.entries())
    .filter(([_, countUpdate]) => countUpdate % 2 === 1) // 홀수인 항목만 선택된 상태로 유지
    .map(([idUpdate]) => idUpdate);

    // 아무것도 클릭되지 않았으면 기존 상태를 유지
    if (resultUpdate.length === 0) {
      console.log(`No changes made for ${inputIdUpdate}. Keeping original values.`);
      return; // 서버로 전달하지 않음
    }

    hiddenInputUpdate.value = resultUpdate.join(",");
    console.log(`Updated ${inputIdUpdate}:`, hiddenInputUpdate.value); // 디버깅용 로그
  }

});

