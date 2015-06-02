$(document).ready( function() {
	refresh();
	$("#contents").focus();
});

function refresh() {
	setTimeout( function() {
	  getBoard();
	  refresh();
	}, 5000);
}

function getBoard() {
	(function() {
	  $(function() {
	  	$("#messages").html(null);
	    return $.get("/messages", function(data) {
	      return $.each(data, function(index, message) {
	        return $("#messages").append($("<ul>").text(message.contents));
	      });
	    });
	  });
	}).call(this);
}