$ ->
  $.get "/messages", (data) ->
    $.each data, (index, message) ->
      $("#messages").append $("<ul>").text message.contents