// 단가와 관련된 DOM 요소 가져오기
const unitPrice = parseInt(document.getElementById("unitPrice").textContent); // 단가
const quantityInput = document.getElementById("quantity"); // 수량 입력 필드
const totalPriceElement = document.getElementById("totalPrice"); // 총 금액 표시 필드
const savedAmountElement = document.getElementById("savedAmount"); // 적립금 표시 필드
const savedPercentElement = document.getElementById("savedPercent"); // 적립 퍼센트 표시 필드

// 수량 감소 버튼 클릭 이벤트
document.getElementById("decreaseQuantity").addEventListener("click", () => {
    if (quantityInput.value > 1) {
        quantityInput.value--; // 수량 감소
        updateTotalPrice(); // 총 금액 업데이트
        updatePointValues(); // 적립금 업데이트
    }
});

// 수량 증가 버튼 클릭 이벤트
document.getElementById("increaseQuantity").addEventListener("click", () => {
    quantityInput.value++; // 수량 증가
    updateTotalPrice(); // 총 금액 업데이트
    updatePointValues(); // 적립금 업데이트
});

// 총 금액 업데이트 함수
function updateTotalPrice() {
    const price = parseInt(unitPrice) * parseInt(quantityInput.value); // 총 금액 계산
    if (!isNaN(price)) {
        totalPriceElement.textContent = new Intl.NumberFormat('ko-KR').format(price); // 금액에 쉼표 포맷 추가
    }
}

// 적립금 업데이트 함수
function updatePointValues() {
    const totalPrice = parseInt(unitPrice) * parseInt(quantityInput.value); // 총 금액 계산

    if (totalPrice > 0) {
        if (bookPointSaveRule.pointSaveType === "INTEGER") {
            // 정액 적립금 계산
            const savedPercent = ((bookPointSaveRule.pointSaveAmount / totalPrice) * 100).toFixed(2); // 적립 퍼센트 계산
            savedAmountElement.textContent = new Intl.NumberFormat('ko-KR').format(bookPointSaveRule.pointSaveAmount); // 정액 표시
            savedPercentElement.textContent = savedPercent; // 퍼센트 표시
        } else if (bookPointSaveRule.pointSaveType === "PERCENT") {
            // 퍼센트 적립금 계산
            const savedAmount = (totalPrice * (bookPointSaveRule.pointSaveAmount / 100)).toFixed(0); // 적립 금액 계산
            savedAmountElement.textContent = parseInt(savedAmount).toLocaleString('ko-KR'); // 금액 표시
            savedPercentElement.textContent = bookPointSaveRule.pointSaveAmount; // 퍼센트 표시
        }
    }
}

// 초기 렌더링 시 총 금액과 적립금 업데이트
document.addEventListener("DOMContentLoaded", function () {
    updateTotalPrice(); // 총 금액 초기화
    updatePointValues(); // 적립금 초기화
});
