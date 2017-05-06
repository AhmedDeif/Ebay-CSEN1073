/*price range*/

 // $('#sl2').slider();

	// var RGBChange = function() {
	//   $('#RGB').css('background', 'rgb('+r.getValue()+','+g.getValue()+','+b.getValue()+')')
	// };	
		
/*scroll to top*/

$(document).ready(function(){
	$(function () {
		// $.scrollUp({
	 //        scrollName: 'scrollUp', // Element ID
	 //        scrollDistance: 300, // Distance from top/bottom before showing element (px)
	 //        scrollFrom: 'top', // 'top' or 'bottom'
	 //        scrollSpeed: 300, // Speed back to top (ms)
	 //        easingType: 'linear', // Scroll to top easing (see http://easings.net/)
	 //        animation: 'fade', // Fade, slide, none
	 //        animationSpeed: 200, // Animation in speed (ms)
	 //        scrollTrigger: false, // Set a custom triggering element. Can be an HTML string or jQuery object
		// 			//scrollTarget: false, // Set a custom target element for scrolling to the top
	 //        scrollText: '<i class="fa fa-angle-up"></i>', // Text for element, can contain HTML
	 //        scrollTitle: false, // Set a custom <a> title if required.
	 //        scrollImg: false, // Set true to use image
	 //        activeOverlay: false, // Set CSS color to display scrollUp active point, e.g '#00FFFF'
	 //        zIndex: 2147483647 // Z-Index for the overlay
		// });

		var searchForm = $('.searchform2');
		searchForm.submit(function () 
		{
			var inField = document.getElementById("searchText").value;
			console.log(inField);
			console.log("IN SEARCH");

				var search_txt = inField;
				
				var sendInfo = {
					search_txt
				};

				console.log(sendInfo);

				$.ajax({
					url: "http://localhost:9000/search",
					method: "POST",
					data: sendInfo,
					failure: function(err) {
						console.log(err);
						console.log("ERROR");

					},
					success: function(result){
						console.log("nesro###########");

						console.log(JSON.parse(result).responseData);
						let txt = JSON.parse(result).responseData;
						console.log("RANAAA###########" + txt);
						// window.location = 'index.html'
						// var searchres = {userID: window.localStorage.getItem("search_Txt")};
						// let itemName =   JSON.parse(result).responseData.itemName;
						window.localStorage.setItem("search_Txt", JSON.stringify(txt));
					
						
						window.location = 'search_results.html';



					}});
				});

			
		});

	});
	//form
