
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

function addBrand() {
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
            $('#editModal').modal('hide');
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
            alert("An error has occurred");
        }
    });
}

function updateBrand() {
    var $form = $('#editBrand');
    console.log($form)
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
            // $('#editModal').modal('hide');

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
    $('#update-brand').click(updateBrand);
    $('#addBrand').click(openCreateModal);
    $('#addBrandButton').click(addBrand);
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
    $('#editBrand :input').keyup(function() {
        let brand = $('#brandName').val();
        let category = $('#categoryName').val();
        console.log(prevValue)
        console.log(brand,category)
        if(brand == prevValue["brand"] && category == prevValue["category"]){
            $('#update-brand').prop('disabled', true);
            return;
        }
        $('#update-brand').prop('disabled', false);
    });
      
}
$(document).ready(init);
$(document).ready(activeTab);
$(document).ready(getBrands);