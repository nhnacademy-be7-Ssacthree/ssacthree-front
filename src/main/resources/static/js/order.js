// TODO : 포장지 선택
let currentRow; // 현재 포장지 선택을 위한 행 참조

function setCurrentRow(button) {
    currentRow = button.closest('tr'); // 현재 버튼이 속한 행을 저장
}

function selectPackaging(packagingId, packagingName, packagingPrice) {
    // 선택한 포장지 이름을 행에 표시
    if (currentRow) {
        const bookId = currentRow.getAttribute('data-book-id');

        const requestBody = {
            bookId: bookId,
            packagingId: packagingId
        };

        fetch('/orders/connect-packaging', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
        })
            .then(response => response.json())
            .then(data => console.log('Success:', data))
            .catch(error => console.error('Error:', error));


        const packagingSpan = currentRow.querySelector('.selected-packaging');
        packagingSpan.innerText = packagingName; // 선택된 포장지 이름 표시
        packagingSpan.dataset.packagingId = packagingId; // 선택한 포장지 ID 저장
        const packagingCostSpan = currentRow.querySelector('.selected-packaging-cost');
        packagingCostSpan.innerText = packagingPrice;
    }

    let paymentPrice = parseInt(document.getElementById("payment-price").value);
    paymentPrice -= packagingPrice;
    document.getElementById("PaymentPrice").value = paymentPrice;
    document.getElementById("PaymentPrice").innerText = `총 결제 금액: ${paymentPrice.toLocaleString()}원`;
    document.querySelector("#packaging-price").innerText = `-${packagingPrice.toLocaleString()}원`;

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


// TODO : 오른쪽 결제 버튼 누르면 보내짐.
function submitOrderForm() {
    // 왼쪽의 폼 ID를 타겟팅하여 서브밋
    document.getElementById('order-form').submit();
}


//밸리데이션
document.getElementById("order-form").addEventListener("submit", function(event) {
    let isValid = true;
    const buyerName = document.getElementById("buyer-name").value.trim();
    const buyerPhone = document.getElementById("buyer-phone").value.trim();

    // 이름 추가 검증
    if (!/^[가-힣a-zA-Z\s]+$/.test(buyerName)) {
        alert("이름은 한글 또는 영어로 입력해야 합니다.");
        isValid = false;
    }

    // 휴대폰 번호 추가 검증
    if (!/^01[016789]-\d{3,4}-\d{4}$/.test(buyerPhone)) {
        alert("휴대폰 번호는 올바른 형식으로 입력해야 합니다. (예: 010-1234-5678)");
        isValid = false;
    }

    if (!isValid) {
        event.preventDefault(); // 폼 제출 중단
    }
});

function validateForm() {
    const form = document.getElementById("order-form");
    if (!form.checkValidity()) {
        form.reportValidity(); // 기본 HTML5 에러 메시지 표시
        return false;
    }
    return true;
}

document.getElementById("order-form").addEventListener("submit", function(event) {
    if (!validateForm()) {
        event.preventDefault();
    }
});

