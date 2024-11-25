document.querySelectorAll(".unitPrice").forEach((element) => {
    // 숫자로 변환
    const price = parseFloat(element.textContent.replace(/,/g, '')); // 쉼표 제거 후 숫자로 변환
    if (!isNaN(price)) {
        // 쉼표 포맷 적용
        element.textContent = new Intl.NumberFormat('ko-KR').format(price);
    }
});