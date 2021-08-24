//global
let reportBrand = []
let prevValue = {};


function displayBrandData(brands) {
    var $tbody = $('#brand-table').find('tbody');
    $tbody.empty();
    var dataTable = $("#brand-table").DataTable();
    dataTable.clear().draw();
    reportBrand = [];
    for(items of brands){
        let brandData = {
            brand:items.brand,
            category:items.category,
        }
        reportBrand.push(brandData);
    }
    reportBrand = reportBrand;
    for (var i in brands) {
        var e = brands[i];
        var brand = [];
        brand.push(e.id)
        brand.push(e.brand)
        brand.push(e.category)
        dataTable.row.add(e).draw(false);
    }
    return false;
}

function getBrands() {
    let url = getBrandUrl();
    let brands;
    $.ajax({
        url: url,
        type: 'GET',
        success: function (response) {
            brands = response;
            displayBrandData(brands);
        },
        error: function () {
            toast("Could not Retrive Information", 'WARN');
        }
    });
}

function openCreateModal() {
    $('#createBrand').trigger("reset");
    $('#addModal').modal('show');
}

function addBrand(e) {
    e.preventDefault();
    var $form = $('#createBrand');
    var json = toJson($form);
    var url = getBrandUrl();
    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            getBrands();
            $(':input', '#createBrand')
                .not(':button, :submit, :reset, :hidden')
                .val('')
            $('#addModal').modal('hide');
            toast('Success');
        },
        error: function (error) {
            error = JSON.parse(error.responseText);
            toast(error.message, 'WARN');
        }
    });

}

function editBrand(e) {
    $.ajax({
        url: getBrandUrl() + '/' + e,
        type: 'GET',
        success: function (response) {
            $('#brandId').val(response.id)
            $('#brandName').val(response.brand)
            $('#categoryName').val(response.category)
            $('#editModal').modal('show');
            $('#update-brand').prop('disabled', true);
            prevValue["brand"] = response.brand;
            prevValue["category"] = response.category;
        },
        error: function () {
            toast("Error while Retriving Information",'WARN');        }
    });
}

function updateBrand(e) {
    e.preventDefault();
    var $form = $('#editBrand');
    var id = $('#brandId').val();
    var json = toJson($form);
    var url = getBrandUrl();
    $.ajax({
        url: url + '/' + id,
        type: 'PUT',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            getBrands();
            $('#editModal').modal('hide');
            toast('Success', 'INFO');
        },
        error: function (error) {
            error = JSON.parse(error.responseText);
            toast(error.message, 'WARN');

        }
    });
}

//Upload Data
function upload(){
    var $file = $('#brandFile');
    if($file.val() == ''){
        toast("Select File","WARN");
        return ;
    }
    processData($file);
}

function updateFileName(){
	var $file = $('#brandFile');
	var fileName = $file.val().replace(/^.*[\\\/]/, '');
	$('#brandFileName').html(fileName);
}

function uploadRows(){
	var json = JSON.stringify(fileData);
    if(!checkFile(fileData)){
        toast("Invalid file format",'WARN')
        return;
    }
    let url = getBrandUrl();
    switch(type){
        case 'Brand':
            url = getBrandUrl();
    }

	// Make ajax call
	$.ajax({
	   url: url+'/list',
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		toast("Susccess",'INFO');
            getBrands();
            $("#uploadModal").modal("hide");
	   },
	   error: function(error){
           toast(error.responseJSON.message,'WARN')
	   }
	});

}

function checkFile(jsonFile){
    let keys = Object.keys(jsonFile[0])
    if(keys[0] == "brand" && keys[1]=="category"){
        return true;
    }
    return false;
}

// Download CSV
function downloadCSV(){
    jsonToCsv(reportBrand);
}

//ACTIVE TAB
function activeTab() {
    $('#nav-brand').addClass('active');
    $('#nav-product').removeClass('active');
    $('#nav-inventory').removeClass('active');
    $('#nav-order').removeClass('active');
}


function init() {
    $('#downloadCSVButton').click(downloadCSV);
    $('#editBrand').submit(updateBrand);
    $('#addBrand').click(openCreateModal);
    $('#createBrand').submit(addBrand);
    $('#brandFile').on('change', updateFileName);
    $('#upload-data').click(upload);
    $('#uploadModalButton').click(function(){
        $('#uploadModal').modal('show');
        $('#brandFile').val("");
        $('#brandFileName').html("Choose file");
    });

    $("#brand-table").DataTable({
        data: [],
        info: false,
        columns: [{
                data: "brand"
            },
            {
                data: "category"
            },
            {
                "mData": null,
                "bSortable": false,
                "mRender": function (o) {
                    return '<span id="editButton" onclick="editBrand(' + o.id +
                     ')"><span data-toggle="tooltip" data-placement="top" title="Edit" class="material-icons md-24">edit</span></span>';
                }
            }
        ],
        "columnDefs": [
            { "width": "10px", "targets": -1 }
        ]
    });
    
    $('#editBrand :input').keyup(function () {
        let brand = $('#brandName').val();
        let category = $('#categoryName').val();
        if (brand == prevValue["brand"] && category == prevValue["category"]) {
            $('#update-brand').prop('disabled', true);
            return;
        }
        $('#update-brand').prop('disabled', false);
    });

}
$(document).ready(init);
$(document).ready(activeTab);
$(document).ready(getBrands);