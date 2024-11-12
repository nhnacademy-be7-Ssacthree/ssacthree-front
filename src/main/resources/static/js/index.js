// JavaScript로 이벤트 전파 방지 설정
document.querySelectorAll('.cart-button').forEach(button => {
    button.addEventListener('click', function(event) {
        event.stopPropagation();
        // 여기에 장바구니에 추가하는 동작을 추가할 수 있습니다
    });
});

document.querySelectorAll('.wishlist-button').forEach(button => {
    button.addEventListener('click', function(event) {
        event.stopPropagation();
        // 여기에 위시리스트에 추가하는 동작을 추가할 수 있습니다
    });
});

const swiper = new Swiper('.product-swiper', {
    slidesPerView: 5,
    spaceBetween: 20,
    navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev',
    },
    pagination: {
        el: '.swiper-pagination',
        clickable: true,
        type: 'bullets',
    },
    loop: false,
    slidesPerGroup: 5,
    breakpoints: {
        320: {
            slidesPerView: 1,
            spaceBetween: 10
        },
        640: {
            slidesPerView: 2,
            spaceBetween: 15
        },
        768: {
            slidesPerView: 3,
            spaceBetween: 15
        },
        1024: {
            slidesPerView: 5,
            spaceBetween: 20
        }
    }
});