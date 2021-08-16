let prevValue = {};

function displayBrandData(brands) {
    console.log(brands)
    var $tbody = $('#brand-table').find('tbody');
    $tbody.empty();
    var dataTable = $("#brand-table").DataTable();
    dataTable.clear();
    let brandsList = [];
    for (var i in brands) {
        var e = brands[i];

        var brand = [];
        brand.push(e.id)
        brand.push(e.brand)
        brand.push(e.category)
        dataTable.row.add(e).draw(false);
        //    		var buttonHtml = '<span id="editButton" onclick="editBrand('+e.id+')"><span class="material-icons md-24">edit</span></span>'
        //    		var row = '<tr id="brand'+i+'">'
        //    		+ '<td>' + e.id + '</td>'
        //    		+ '<td>' + e.brand + '</td>'
        //    		+ '<td>'  + e.category + '</td>'
        //    		+ '<td>' + buttonHtml + '</td>'
        //    		+ '</tr>';
        //            $tbody.append(row);
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
            alert("Could not Retrive Data From Server");
        }
    });
}

function openCreateModal() {
    // $(':input', '#')
    //     .not(':button, :submit, :reset, :hidden')
    //     .val('');
    $('#createBrand').trigger("reset");
    $('#addModal').modal('show');

}

function addBrand(e) {
    e.preventDefault();
    var $form = $('#createBrand');
    var json = toJson($form);
    var url = getBrandUrl();
    console.log(json);
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
            toast('Successfully created a Brand');
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
            console.log(response);
            $('#brandId').val(response.id)
            $('#brandName').val(response.brand)
            $('#categoryName').val(response.category)
            $('#editModal').modal('show');
            $('#update-brand').prop('disabled', true);
            prevValue["brand"] = response.brand;
            prevValue["category"] = response.category;
        },
        error: function () {
            toast("Error while Retriving Data");        }
    });
}

function updateBrand(e) {
    e.preventDefault();
    var $form = $('#editBrand');
    var id = $('#brandId').val();
    var json = toJson($form);
    var url = getBrandUrl();
    console.log(json);
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
            toast('Successfully created', 'INFO');
        },
        error: function (error) {
            error = JSON.parse(error.responseText);
            toast(error.message, 'WARN');

        }
    });
}

//Upload Data
function upload(){
    var $file = $('#brandFile')
    processData($file);
}

function updateFileName(){
	var $file = $('#brandFile');
	var fileName = $file.val();
	$('#brandFileName').html(fileName);
}

function uploadRows(){
	
	var json = JSON.stringify(fileData);
    console.log(json);
    let url = getBrandUrl();
    switch(type){
        case 'Brand':
            url = getBrandUrl();
    }

	// Make ajax call
	$.ajax({
	   url: url+'/upload',
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		console.log("Susccessfully Uploaded",'INFO')  
	   },
	   error: function(error){
           console.log(error);
           toast(error.responseJSON.message,'WARN')
	   }
	});

}

function download(){
    let url = getReportUrl()+"/brand";
    $.ajax({
        url: url,
        cache: false,
        xhr: function () {
            var xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function () {
                if (xhr.readyState == 2) {
                    if (xhr.status == 200) {
                        xhr.responseType = "blob";
                    } else {
                        xhr.responseType = "text";
                    }
                }
            };
            return xhr;
        },
        success: function (data) {
            //Convert the Byte Data to BLOB object.
            var blob = new Blob([data], { type: "application/octetstream" });

            //Check the Browser type and download the File.
            var isIE = false || !!document.documentMode;
            if (isIE) {
                window.navigator.msSaveBlob(blob, fileName);
            } else {
                var url = window.URL || window.webkitURL;
                link = url.createObjectURL(blob);
                var a = $("<a />");
                a.attr("download", fileName);
                a.attr("href", link);
                $("body").append(a);
                a[0].click();
                $("body").remove(a);
            }
        }
    });
}

//ACTIVE TAB
function activeTab() {
    $('#nav-brand').addClass('active');
    $('#nav-product').removeClass('active');
    $('#nav-inventory').removeClass('active');
    $('#nav-order').removeClass('active');
}


function init() {
    $('#editBrand').submit(updateBrand);
    $('#addBrand').click(openCreateModal);
    $('#createBrand').submit(addBrand);
    $('#brandFile').on('change', updateFileName);
    $('#download').click(download); 
    $('#upload-data').click(upload);
    $("#brand-table").DataTable({
        data: [],
        info: false,
        // stripeClasses: [ 'odd-row', 'even-row' ],
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
                    return '<span id="editButton" onclick="editBrand(' + o.id + ')"><span class="material-icons md-24">edit</span></span>';
                }
            }
        ]
    });
    $('#editBrand :input').keyup(function () {
        let brand = $('#brandName').val();
        let category = $('#categoryName').val();
        console.log(prevValue)
        console.log(brand, category)
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