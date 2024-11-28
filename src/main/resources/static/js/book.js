const unitPrice = parseInt(document.getElementById("unitPrice").textContent);
const quantityInput = document.getElementById("quantity");
const totalPriceElement = document.getElementById("totalPrice");

// 수량 변경 이벤트
document.getElementById("decreaseQuantity").addEventListener("click", () => {
    if (quantityInput.value > 1) {
        quantityInput.value--;
        updateTotalPrice();
    }
});

document.getElementById("increaseQuantity").addEventListener("click", () => {
    quantityInput.value++;
    updateTotalPrice();
});

// 총 금액 업데이트 함수
function updateTotalPrice() {

    const price = parseInt(unitPrice) * parseInt(quantityInput.value); // 쉼표 제거 후 숫자로 변환
    if (!isNaN(price)) {
        // 쉼표 포맷 적용
        totalPriceElement.textContent = new Intl.NumberFormat('ko-KR').format(price);
    }
}