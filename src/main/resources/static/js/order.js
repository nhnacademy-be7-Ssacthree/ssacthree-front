// TODO : 포장지 선택
let currentRow; // 현재 포장지 선택을 위한 행 참조

function setCurrentRow(button) {
    currentRow = button.closest('tr'); // 현재 버튼이 속한 행을 저장
}

function selectPackaging(packagingId, packagingName, packagingPrice) {
    // 선택한 포장지 이름을 행에 표시
    if (currentRow) {
        const packagingSpan = currentRow.querySelector('.selected-packaging');
        packagingSpan.innerText = packagingName; // 선택된 포장지 이름 표시
        packagingSpan.dataset.packagingId = packagingId; // 선택한 포장지 ID 저장
        const packagingCostSpan = currentRow.querySelector('.selected-packaging-cost');
        packagingCostSpan.innerText = packagingPrice;
    }

    // 모달을 닫음
    const myModalEl = document.getElementById('packagingModal');
    const modalInstance = bootstrap.Modal.getInstance(myModalEl);
    modalInstance.hide();

}


// TODO : 회원 주소 선택
// 주소를 선택했을 때 입력창에 자동으로 채워주는 함수
function selectAddress(postalCode, roadAddress, detailAddress) {
    console.log(postalCode, roadAddress, detailAddress); // 값 확인
    document.getElementById("postal-code").value = postalCode;
    document.getElementById("road-address").value = roadAddress;
    document.getElementById("detail-address").value = detailAddress;

    // TODO : 중요 !! 모달을 누르면 backdrop이 두개 생기는 문제 ... js중첩때문.. 닫을때 지우게 함.
    // 모달 닫기
    const myModalEl = document.getElementById('addressModal');
    const modalInstance = bootstrap.Modal.getInstance(myModalEl);
    modalInstance.hide();
}

// TODO : 주소 조회 모달
function openPostcodeSearch() {
    new daum.Postcode({
        oncomplete: function (data) {
            document.getElementById('postal-code').value = data.zonecode;
            document.getElementById('road-address').value = data.roadAddress;
            document.getElementById('detail-address').value = "";
            document.getElementById('detail-address').focus();
            document.getElementById('order-request').value = "";
        }
    }).open();
}

// TODO : 배송 날짜 5일 제한
const deliveryDateInput = document.getElementById("delivery-date");
const dateMessage = document.getElementById("date-message");
const today = new Date();

// 설정할 날짜 포맷 (YYYY-MM-DD)
const formatDate = date => date.toISOString().split("T")[0];

// 오늘 날짜 설정
deliveryDateInput.min = formatDate(today);

// 기본값을 오늘 날짜로 설정
deliveryDateInput.value = formatDate(today);

// 오늘로부터 5일 이후 날짜 설정
const maxDate = new Date(today);
maxDate.setDate(today.getDate() + 5);
deliveryDateInput.max = formatDate(maxDate);

// 날짜 선택 시 범위 체크
deliveryDateInput.addEventListener("input", () => {
    const selectedDate = new Date(deliveryDateInput.value);
    if (selectedDate < today || selectedDate > maxDate) {
        alert("선택 가능한 날짜는 오늘부터 5일 후까지입니다.");
        deliveryDateInput.value = ""; // 잘못된 입력 제거
    }
});


// TODO : 결제 내역 업데이트 함수 (여기에 각 값들을 업데이트)
function updatePaymentDetails(orderAmount, couponDiscount, giftWrap, pointPayment, deliveryFee) {
    // 결제 총액 계산
    let totalAmount = orderAmount - couponDiscount - pointPayment + giftWrap + deliveryFee;

    // HTML 요소에 값 업데이트
    document.getElementById('order-product-amount').innerText = `총 1권 ~${orderAmount}원`; // 예시로 1권
    document.getElementById('total-product-amount').innerText = `${orderAmount}원`;
    document.getElementById('coupon-discount').innerText = `${couponDiscount}원`;
    document.getElementById('gift-wrap').innerText = `${giftWrap}원`;
    document.getElementById('point-payment').innerText = `${pointPayment}원`;
    document.getElementById('delivery-fee').innerText = `${deliveryFee}원`;
    document.getElementById('total-payment-amount').innerText = `${orderAmount + giftWrap + deliveryFee - couponDiscount - pointPayment}원`;
    document.getElementById('remaining-payment').innerText = `${totalAmount}원`;
}

// 페이지 로드 시 결제 내역 업데이트 (예시 데이터 사용)
updatePaymentDetails(16020, 0, 0, 0, 0);


// TODO : 오른쪽 결제 버튼 누르면 보내짐.
function submitOrderForm() {
    // 왼쪽의 폼 ID를 타겟팅하여 서브밋
    document.getElementById('order-form').submit();
}
