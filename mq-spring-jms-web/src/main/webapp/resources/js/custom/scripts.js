//use bootstrap datatable
$(document).ready( function () {
    $('#table_out').DataTable();
} );

//use bootstrap datatable
$(document).ready( function () {
    $('#table_inc').DataTable();
} );

//enable all tooltips in the document
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
});

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

function showAlert(modalToHide, message, alertType) {
    modalToHide.modal('hide');

    $('#alert_placeholder').append('<div id="alertdiv" class="alert alert-' + alertType + '"><a class="close" data-dismiss="alert">×</a><span>' + message + '</span></div>');
    modalToHide.on('hide.bs.modal', location.reload());
}


