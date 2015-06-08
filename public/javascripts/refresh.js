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
			  var colors = ["red", "green", "blue", "cyan", "pink", "purple", "yellow", "gray", "magenta"];
			  var begin = message.contents.indexOf("#");
			  var end = getPosition(message.contents, ":", 2);
			  var hasColor = false;
			  for(i in colors) {
				  var color = colors[i];
				  if (message.contents.indexOf(""+color+":") > -1) {
					  message.contents = message.contents.replace(""+color+":","");
					  var newFont = $("<font color='"+color+"'></font>").text(message.contents);
					  var newUl = $("<ul></ul>");
					  newUl.append(newFont);
					  $("#messages").append(newUl);
					  hasColor = true;
					  break;
				  }
			  }
			  if (hasColor == false) {
				  if (begin > -1 && end > -1) {
					  var hexColor = message.contents.substring(begin, end);
					  if (hexColor.indexOf(" ") == -1) {
						 if(/^#[0-9a-f]|[0-9A-F]{3,6}$/.test(hexColor)) {
							 hasColor = true;
							 message.contents = message.contents.replace(""+hexColor+":","");
							  var newFont = $("<font color='"+hexColor+"'></font>").text(message.contents);
							  var newUl = $("<ul></ul>");
							  newUl.append(newFont);
							  $("#messages").append(newUl);
						 }
					  }
				  }
			  }

			  if (hasColor == false) {
				  $("#messages").append($("<ul>").text(message.contents));
			  }

		  }
	});
}
function getPosition(str, m, i) {
	   return str.split(m, i).join(m).length;
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
