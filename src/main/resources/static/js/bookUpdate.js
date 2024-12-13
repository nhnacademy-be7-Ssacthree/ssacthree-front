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


// 기존 선택된 데이터 동기화
  const initialCategories = new Set();
  const initialTags = new Set();
  const initialAuthors = new Set();

  // 기존 선택된 데이터 초기화
  document.querySelectorAll("#selected-categories-container [data-id]").forEach((el) => {
    initialCategories.add(el.dataset.id);
  });

  document.querySelectorAll("#selected-tags-container [data-id]").forEach((el) => {
    initialTags.add(el.dataset.id);
  });

  document.querySelectorAll("#selected-authors-container [data-id]").forEach((el) => {
    initialAuthors.add(el.dataset.id);
  });

  console.log("Initial Categories:", Array.from(initialCategories));
  console.log("Initial Tags:", Array.from(initialTags));
  console.log("Initial Authors:", Array.from(initialAuthors));

  // Hidden Input 초기화
  updateHiddenInput("#categories", initialCategories);
  updateHiddenInput("#tags", initialTags);
  updateHiddenInput("#authors", initialAuthors);

  // 선택 및 제거 이벤트 설정
  setupSelectHandler("category-select", "selected-categories-container", initialCategories);
  setupSelectHandler("tag-select", "selected-tags-container", initialTags);
  setupSelectHandler("author-select", "selected-authors-container", initialAuthors);

  formUpdate.addEventListener("submit", function () {
    const bookInfoFieldUpdate = document.querySelector("#bookInfo");
    bookInfoFieldUpdate.value = editorUpdate.getMarkdown();

    updateHiddenInput("#categories", initialCategories);
    updateHiddenInput("#tags", initialTags);
    updateHiddenInput("#authors", initialAuthors);

    console.log("Submitting Categories:", Array.from(initialCategories));
    console.log("Submitting Tags:", Array.from(initialTags));
    console.log("Submitting Authors:", Array.from(initialAuthors));
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

  // 선택 핸들러
  function setupSelectHandler(selectId, containerId, selectionSet) {
    const select = document.getElementById(selectId);
    const container = document.getElementById(containerId);

    // 선택 시 추가
    select.addEventListener("change", function () {
      const selectedId = select.value;
      const selectedName = select.options[select.selectedIndex]?.text;

      if (selectedId) {
        if (!selectionSet.has(selectedId)) {
          selectionSet.add(selectedId);
          renderTag(container, selectedName, selectedId);
          updateHiddenInput(`#${container.id.split("-")[1]}`, selectionSet);
          console.log(`Added ${selectedId} to ${containerId}`);
        } else {
          console.log(`${selectedId} is already in ${containerId}`);
        }
      }
    });

    // 삭제 시 제거
    container.addEventListener("click", function (e) {
      if (e.target.classList.contains("remove-tag-btn")) {
        const id = e.target.dataset.id;
        if (id) {
          selectionSet.delete(id);
          const tagElement = container.querySelector(`[data-id="${id}"]`);
          if (tagElement) {
            tagElement.remove();
          }
          updateHiddenInput(`#${container.id.split("-")[1]}`, selectionSet);
          console.log(`Removed ${id} from ${containerId}`);
        }
      }
    });
  }

  // 태그 렌더링
  function renderTag(container, name, id, selectionSet) {
    if (!container.querySelector(`[data-id="${id}"]`)) {
      container.insertAdjacentHTML(
          "beforeend",
          `<span class="tag-badge" data-id="${id}">
          ${name}
          <button type="button" class="remove-tag-btn" data-id="${id}">x</button>
        </span>`
      );
    }
  }

  // Hidden Input 업데이트
  function updateHiddenInput(inputId, selectionSet) {
    const hiddenInput = document.querySelector(inputId);
    hiddenInput.value = Array.from(selectionSet).join(","); // Set → 콤마 구분 문자열
    console.log(`Updated ${inputId}:`, hiddenInput.value);
  }
});

