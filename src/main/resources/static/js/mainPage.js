document.addEventListener("DOMContentLoaded", function () {
    // 모든 카테고리 버튼에 클릭 이벤트 추가
    const categoryButtons = document.querySelectorAll(".btn-filter");

    categoryButtons.forEach(button => {
        button.addEventListener("click", function () {
            // 모든 버튼에서 'active' 클래스 제거
            categoryButtons.forEach(btn => btn.classList.remove("active"));

            // 현재 클릭한 버튼에 'active' 클래스 추가
            this.classList.add("active");

            // 카테고리 ID 가져오기
            const categoryId = this.getAttribute("data-category-id") || null;

            // 카테고리 ID에 따라 책 목록 로드
            loadBooksByCategory(categoryId);
        });
    });

    // 처음 들어왔을 때 "종합" 데이터를 기본으로 로드
    loadBooksByCategory(null); // 종합 데이터를 가져오기 위해 null 전달
});

// 특정 카테고리의 책 데이터를 가져오는 함수
function loadBooksByCategory(categoryId) {
    // 카테고리 ID에 따른 URL 생성
    let url = categoryId ? `/shop/books/categories/${categoryId}` : `/shop/books`;

    fetch(url, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(error => {
                    console.error("책 데이터 로드 실패:", error);
                    throw new Error(`책 데이터 로드 실패: ${response.status}`);
                });
            }
            return response.json();
        })
        .then(data => {
            // 책 리스트 업데이트 (최대 10개)
            updateBookList(data.content);
        })
        .catch(error => {
            console.error("에러 발생:", error);
        });
}

function updateBookList(books) {
    const bookContainer = document.querySelector("#book-container");

    // 기존 책 리스트 제거
    bookContainer.innerHTML = "";

    // 새로운 책 리스트 렌더링 (최대 10개)
    books.forEach(book => {
        const bookCard = document.createElement("div");
        bookCard.className = "custom-col mb-3";
        bookCard.innerHTML = `
            <div class="card position-relative p-4 border rounded-3" style="cursor: pointer; height: 525px;">
                <!-- 할인 정보 -->
                <div class="position-absolute">
                    <p class="bg-primary py-1 px-3 fs-6 text-white rounded-2">
                        ${book.bookDiscount ? `${book.bookDiscount}% off` : ""}
                    </p>
                </div>

                <!-- 책 이미지 -->
                <img src="${book.bookThumbnailImageUrl}" class="img-fluid shadow-sm" alt="book cover"
                     style="width: 100%; height: auto; object-fit: cover;">

                <!-- 책 제목 -->
                <h6 class="mt-4 mb-0 fw-bold">
                    ${book.bookName.length > 50 ? `${book.bookName.substring(0, 40)}...` : book.bookName}
                </h6>

                <!-- 리뷰 별점 -->
                <div class="review-content d-flex mt-2">
                    <div class="rating text-warning d-flex align-items-center">
                        ${Array.from({length: 5}, (_, i) => `
                            <svg class="rating-star" style="width:20px; height:20px;">
                                <use xlink:href="#star-fill" class="${i < book.reviewRateAverage ? 'text-warning' : 'text-muted'}"></use>
                            </svg>
                        `).join('')}
                    </div>
                </div>

                <!-- 가격 정보 -->
                <p><span class="price text-primary fw-bold mb-2 fs-5">${book.salePrice}</span>원</p>

                <!-- 액션 버튼 -->
                <div class="card-concern position-absolute start-0 end-0 d-flex gap-2">
                    <button type="button" class="btn btn-dark cart-button"
                            onclick="addToCart(${book.bookId})">
                        <svg class="cart"><use xlink:href="#cart"></use></svg>
                    </button>
                    <button type="button" class="btn btn-dark wishlist-button"
                            onclick="toggleLike(this, ${book.bookId})">
                        <svg class="wishlist">
                            <use xlink:href="#heart"></use>
                        </svg>
                    </button>
                </div>
            </div>
        `;

        // 카드 클릭 이벤트 추가
        const card = bookCard.querySelector(".card");
        card.addEventListener("click", function (event) {
            // 클릭한 요소가 카트 버튼 또는 좋아요 버튼이면 상세 페이지로 이동하지 않음
            if (event.target.closest(".cart-button") || event.target.closest(".wishlist-button")) {
                return;
            }

            // 상세 페이지로 이동
            window.location.href = `/books/${book.bookId}`;
        });

        bookContainer.appendChild(bookCard);
    });
}


function addToCart(bookId) {
    // URL 생성
    const url = `/shop/carts/add?bookId=${bookId}&quantity=1`;

    // URL로 이동
    window.location.href = url;
}