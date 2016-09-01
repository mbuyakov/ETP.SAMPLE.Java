$(document).ready( function () {
//use bootstrap datatable
    $('#table_out').DataTable();
    $('#table_inc').DataTable();
//enable all tooltips in the document
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

$('.container').find('.refreshBtn').on('click', function () {
    location.reload();
});

$('#modalEdit0').find('.saveBtn').on('click', function () {
    var message = $('#message').val();
    var queue = $('#queue').val();

    $.ajax({
        type: "POST",
        url: sendMessage,
        data: {message: message, queue: queue},
        success: function () {
            showAlert($('#modalEdit0'), 'Сообщение ' + message + ' было успешно отправлено в очередь ' + queue, 'success');
        },
        error: function (e) {
             console.log("Error = %s", e);
            showAlert($('#modalEdit0'), 'При отправке сообщения ' + message + ' в очередь ' + queue + ' произошла ошибка', 'danger');
        }
    });
});

$('#modalEdit0').on('show.bs.modal', function (e) {
    resetEdit($(this));
});

function showAlert(modalToHide, message, alertType) {
    modalToHide.modal('hide');

    $('#alert_placeholder').append('<div id="alertdiv" class="alert alert-' + alertType + '"><a class="close" data-dismiss="alert">×</a><span>' + message + '</span></div>');
}

function resetEdit(modal) {
    modal.find(':input').val('');
}

