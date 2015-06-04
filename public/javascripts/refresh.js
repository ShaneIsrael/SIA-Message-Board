$(document).ready( function() {
	refresh();
	submitHandler();
});

function refresh() {
	setTimeout( function() {
	  getBoard();
	  refresh();
	}, 1000);
}

function getBoard() {
	$.getJSON( "/messages", function( data ) {
		$("#messages").html(null);
		  var items = [];
		  for (ix in data) {
			  var message = data[ix];
			  $("#messages").append($("<ul>").text(message.contents));
		  }
	});
}

function submitHandler() {
	// Attach a submit handler to the form
	$( "#submitForm" ).submit(function( event ) {

	  // Stop form from submitting normally
	  event.preventDefault();

	  // Get some values from elements on the page:
	  var $form = $( this );
	  var term = $form.find( "input[name='contents']" ).val();
	  var url = $form.attr( "action" );

	  // Send the data using post
	  var posting = $.post( url, { "contents": term } );
	  $("#contents").focus();
	  $("#contents").val("");
	  getBoard();
	});
}
