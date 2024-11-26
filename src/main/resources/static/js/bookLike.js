window.toggleLike = function (element) {
    const bookId = element.getAttribute('data-book-id');
    const isLiked = element.classList.contains('liked');
    const useElement = element.querySelector('use'); // <use> 태그 선택

    // likeBooks가 null인 경우 알림창만 띄우기
    if (typeof likeBooks === 'undefined' || likeBooks === null) {
        alert('로그인이 필요합니다. 회원만 좋아요를 사용할 수 있습니다.');
        return;
    }

    if (isLiked) {
        // 좋아요 삭제
        deleteLike(bookId)
            .then(() => {
                element.classList.remove('liked');
                useElement.setAttribute('xlink:href', '#heart'); // 빈 하트로 변경
                console.log('좋아요 삭제 성공');
            })
            .catch(error => {
                console.error('좋아요 삭제 실패:', error.message);
            });
    } else {
        // 좋아요 추가
        addLike(bookId)
            .then(() => {
                element.classList.add('liked');
                useElement.setAttribute('xlink:href', '#heart-filled'); // 채워진 하트로 변경
                console.log('좋아요 추가 성공');
            })
            .catch(error => {
                console.error('좋아요 추가 실패:', error.message);
            });
    }
};


function addLike(bookId) {
    return fetch(`/shop/members/likes`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({bookId: bookId}),
        credentials: 'include' // 쿠키 포함
    }).then(response => {
        if (!response.ok) {
            return response.text().then(errorText => {
                console.error('좋아요 추가 실패:', errorText);
                throw new Error(`좋아요 추가 실패: ${response.status}`);
            });
        }
        return response.json();
    }).then(data => {
        console.log("서버 응답:", data);
        return data;
    }).catch(error => {
        console.error("에러 발생:", error);
    });
}

function deleteLike(bookId) {
    return fetch(`/shop/members/likes/${bookId}`, {
        method: 'DELETE',
        credentials: 'include', // 쿠키 포함
        headers: {
            'Accept': 'application/json',
        },
    }).then(response => {
        if (!response.ok) {
            return response.json().then(errorJson => {
                console.error('좋아요 삭제 실패:', errorJson);
                throw new Error(`좋아요 삭제 실패: ${response.status} - ${errorJson.message || 'Unknown error'}`);
            });
        }
        return response.json();
    }).then(data => {
        console.log("서버 응답:", data);
        return data;
    }).catch(error => {
        console.error("에러 발생:", error);
    });
}
