let currentRow; // 현재 포장지 선택을 위한 행 참조

function setCurrentRow(button) {
    currentRow = button.closest('tr'); // 현재 버튼이 속한 행을 저장
}

function selectPackaging(packagingId, packagingName) {
    // 선택한 포장지 이름을 행에 표시
    if (currentRow) {
        const packagingSpan = currentRow.querySelector('.selected-packaging');
        packagingSpan.innerText = packagingName; // 선택된 포장지 이름 표시
        packagingSpan.dataset.packagingId = packagingId; // 선택한 포장지 ID 저장
    }

    // 모달 닫기
    const packagingModal = new bootstrap.Modal(document.getElementById('packagingModal'));
    packagingModal.hide();
}

// 주소를 선택했을 때 입력창에 자동으로 채워주는 함수
function selectAddress(postalCode, roadAddress, detailAddress) {
    // 각 입력 필드에 값을 넣어줍니다.
    document.getElementById("postal-code").value = postalCode;
    document.getElementById("road-address").value = roadAddress;
    document.getElementById("detail-address").value = detailAddress;

    // 모달을 닫습니다.
    var myModal = new bootstrap.Modal(document.getElementById('addressModal'));
    myModal.hide();
}

// 주문서 스크립트 로직
function openPostcodeSearch() {
    new daum.Postcode({
        oncomplete: function(data) {
            document.getElementById('postal-code').value = data.zonecode;
            document.getElementById('road-address').value = data.roadAddress;
            document.getElementById('detail-address').focus();
        }
    }).open();
}

/* Thymeleaf를 통해 서버의 데이터를 JSON 형태로 자바스크립트에 전달 */
let guestCartInfo = /*[[${guestCartInfo}]]*/ [];

function calculatePaymentPrice() {
    let paymentPrice = 0;

    guestCartInfo.forEach(item => {
        paymentPrice += item.price * item.quantity;
    });

    document.getElementById("PaymentPrice").innerText = paymentPrice + "원";
}
