window.toggleCategory = function (element) {
    const categoryId = element.getAttribute('data-category-id');

    return fetch(`/shop/categories/${categoryId}/books`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (!response.ok) {
            return response.json().then(errorJson => {
                console.error('카테고리 해당 도서 불러오기 실패:', errorJson);
                throw new Error(`카테고리 해당 도서 불러오기 실패: ${response.status} - ${errorJson.message || 'Unknown error'}`);
            })
        }

        return response.json();
    }).then(data => {
        console.log("서버 응답: ", data);
        return data;
    }).catch(error => {
        console.error("에러 발생:", error);
    });
}