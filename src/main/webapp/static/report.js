
var reportData = [];

function displaySalesReport(report){
    var dataTable = $('#report-table').DataTable()
    dataTable.clear().draw();
    for (var i in report) {
        var e = report[i];
        dataTable.row.add(e).draw(false);
    }
    return false;
}

function getReport() {
    var startDate = $('#startDatepicker').datepicker().val();
    var endDate = $('#endDatepicker').datepicker().val();
    var brand = $('#brand').val();
    var category = $('#category').val();
    var json = {
        startDate: startDate,
        endDate: endDate,
        brand: brand,
        category: category,
    }
    json = JSON.stringify(json);
    var url = getReportUrl() + '/sales';
    $.ajax({
        url: url,
        type: "POST",
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            reportData = response;
            displaySalesReport(response);
            if(response.length==0){
                toast("Not Report", 'WARN');
                displaySalesReport([]);
            $('#downloadCSVButton').removeClass('btn-large-text');;
                $('#downloadCSVButton').attr('disabled', true);   
            }
            else{
                $('#downloadCSVButton').addClass("btn-large-text"); 
                $('#downloadCSVButton').attr('disabled', false);   
            }
        },
        error: function (error) {
            error = error.responseJSON;
            $('#downloadCSVButton').removeClass('btn-large-text');
            $('#downloadCSVButton').attr('disabled', true);   
            toast(error.message, 'WARN');
        },
    });
}

// Download CSV
function downloadCSV(){
    jsonToCsv(reportData);
    $('')
}


function init() {
    $('#startDatepicker').datepicker({
        uiLibrary: 'bootstrap4',
        format: 'dd/mm/yyyy'
    });

    $('#endDatepicker').datepicker({
        uiLibrary: 'bootstrap4',
        format: 'dd/mm/yyyy'
    });

    $("#report-table").DataTable({
        data: [],
        info: false,
        columns: [{
                data: "category",
            },
            {
                data: "quantity",
            },
            {
                data: "revenue",
            }
        ],
    });
    $('#generateReport').click(getReport);
    
    $('#downloadCSVButton').click(downloadCSV);

}


//ACTIVE TAB
function activeTab() {
    $('#nav-brand').removeClass('active');
    $('#nav-product').removeClass('active');
    $('#nav-inventory').removeClass('active');
    $('#nav-order').removeClass('active');
    $('#nav-report').addClass('active');
}

$(document).ready(init);
$(document).ready(function(){
    if(reportData.length==0){
        $('#downloadCSVButton').attr('disabled', true);
        $('#downloadCSVButton').removeClass('btn-large-text');;
    }
    else{
        $('#downloadCSVButton').addClass("btn-large-text");
        $('#downloadCSVButton').attr('disabled', false); 
    }
});
$(document).ready(activeTab);