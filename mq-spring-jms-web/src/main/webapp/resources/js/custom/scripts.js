//$(document).ready( function () {
//    $('#table_out').DataTable();
//} );
//
//$(document).ready( function () {
//    $('#table_inc').DataTable();
//} );

$('#myTab a').click(function(e) {
  e.preventDefault();
  $(this).tab('show');
});

// store the currently selected tab in the hash value
$("ul.nav-tabs > li > a").on("shown.bs.tab", function(e) {
  var id = $(e.target).attr("href").substr(1);
  window.location.hash = id;
});

// on load of the page: switch to the currently selected tab
var hash = window.location.hash;
$('#myTab a[href="' + hash + '"]').tab('show');

$('#modalEdit0').find('.saveBtn').on('click', function () {
    var message = $('#message').val();
    var queue = $('#queue').val();

    console.log("message = %s, queue = %s", message, queue);

    $.ajax({
        type: "POST",
        url: sendMessage,
        data: {message: message, queue: queue},
        success: function () {
            showAlert($('#modalEdit0'), 'Сообщение ' + message + ' было успешно отправлено в очередь ' + queue, 'success');
        },
        error: function (e) {
         //   alert('Error: ' + e);
            showAlert($('#modalEdit0'), 'При отправке сообщения ' + message + ' в очередь ' + queue + ' произошла ошибка', 'danger');
        }
    });
});


//function showAlert(message, alertType) {
//    $('#alert_placeholder').
//    append('<div id="alertdiv" class="alert alert-' + alertType + '"><a class="close" data-dismiss="alert">×</a><span>' + message + '</span></div>');
//    setTimeout(function () {
//        $("#alertdiv").remove();
//    }, 4000);
//}

function showAlert(modalToHide, message, alertType) {
    modalToHide.modal('hide');

    $('#alert_placeholder').append('<div id="alertdiv" class="alert alert-' + alertType + '"><a class="close" data-dismiss="alert">×</a><span>' + message + '</span></div>');
//    setTimeout(function () {
//        $("#alertdiv").remove();
//    }, 4000);
//    modalToHide.on('hide.bs.modal', $('#tab-loaded-content').load($('li.active a[data-toggle="tab"]').attr('href')));
//    $('#tab-loaded-content').load($('li.active a[data-toggle="tab"]').attr('href'));
    modalToHide.on('hide.bs.modal', location.reload());
}

function updateTab() {
    var tab = $('.nav-tabs .active').text().toLowerCase();
    console.log("tab = %s", tab);

    $.ajax({
        type: "POST",
        cache: false,
        url: updateTabs,
        data: {tab: tab},
        success: function (response) {
            $('.tab-content').find('#' + tab).html(response);
//            jump('top');
        },
        error: function (e) {
            alert('Error: ' + e);
        }
    });
}


