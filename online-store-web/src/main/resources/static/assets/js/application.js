// application.js

$(document).ready(function($) {

	var owlCast = $("#owl-demo-cast");

	owlCast.owlCarousel({
		items : 4,
		itemsDesktop : [980,4],
		itemsDesktopSmall : [980,4],
		itemsTablet: [768,3],
		itemsMobile: [479, 2]
	});
});
