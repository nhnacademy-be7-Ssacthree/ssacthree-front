
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
    totalPriceElement.textContent = (parseInt(unitPrice) * parseInt(quantityInput.value)).toString(); // 금액에 콤마 추가
}
