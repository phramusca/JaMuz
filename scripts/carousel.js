$(document).ready(function () {
    // JaMuz Android
    var numberOfImages = 15;
    var carouselAndroid = $('.carousel-android');
    for (var i = 1; i <= numberOfImages; i++) {
        var imageUrl = 'img/phoneScreenshots/' + i + '.png';
        carouselAndroid.append('<div><img src="' + imageUrl + '" alt="Image" class="carousel-android-image"></div>');
    }
    carouselAndroid.slick({
        dots: true,
        infinite: true,
        autoplay: true,
        autoplaySpeed: 3000,
        fade: true,
        cssEase: 'linear'
    });

    // JaMuz Desktop
    var images = [
        'img/desktop/Choisir.png',
        'img/desktop/Fusionner.png',
        'img/desktop/Exporter.png',
        'img/desktop/Verifier.png',
        'img/desktop/Listes.png',
        'img/desktop/Slsk.png',
        'img/desktop/Stats.png',
        'img/desktop/Serveur.png',
        'img/desktop/Options.png',
    ];
    var carouselDesktop = $('.carousel-desktop');
    images.forEach(function (imageUrl) {
        carouselDesktop.append('<div><img src="' + imageUrl + '" alt="Image" class="carousel-desktop-image"></div>');
    });
    carouselDesktop.slick({
        dots: true,
        infinite: true,
        autoplay: true,
        autoplaySpeed: 3000,
        fade: true,
        cssEase: 'linear'
    });
});