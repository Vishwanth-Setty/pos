//Global var
reportInventory = []

prevValue = {};

function displayInventoryData(inventories) {
    var dataTable = $('#inventory-table').DataTable()
    dataTable.clear().draw();
    reportInventory = inventories;
    for(var i in inventories){
    		var e = inventories[i];
            dataTable.row.add(e).draw(false);
    	}

    return false;
}

function addInventory(e) {
    var $form = $('#addInventory');
    e.preventDefault();
    var json = toJson($form);
    var url = getInventoryUrl();
    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            getInventories();
            $(':input', '#addInventory')
                .not(':button, :submit, :reset, :hidden')
                .val('')
            $('#addModal').modal('hide');
            toast('Success');
        },
        error: function (error) {
            error = JSON.parse(error.responseText);
            toast(error.message,'WARN');
        }
    });

}

async function editInventory(barcode,quantity) {
    $('#editModal').modal('show');
    quantity = new Number(quantity);
    $('#quantity').val(quantity)
    $('#barcode').val(barcode)
    prevValue["quantity"] = parseInt($('#quantity').val());
}

function updateInventory(e) {
    e.preventDefault();
    var $form = $('#editInventory');
    var json = toJson($form);
    json = JSON.parse(json);
    json.quantity = parseInt(json.quantity);
    json = JSON.stringify(json);
    var url = getInventoryUrl();
    $.ajax({
        url: url,
        type: 'PUT',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            getInventories();
            $('#editModal').modal('hide');
            toast('Success');
        },
        error: function (error) {
            error = JSON.parse(error.responseText);
            toast(error.message,'WARN');
        }
    });
}


function getInventories() {
    let url = getInventoryUrl();
    let inventories;
    $.ajax({
        url: url,
        type: 'GET',
        success: function (response) {
            inventories = response;
            displayInventoryData(inventories);
        },
        error: function (error) {
            toast("Could not Retrive Information",'WARN');
        }
    });
}
async function getInventoryById(id) {
    let url = getInventoryUrl() + '/' + id;
    let inventory;
    await $.ajax({
        url: url,
        type: 'GET',
        success: function (response) {
            inventory = response;
        },
        error: function (error) {
            toast("Could not Retrive Information",'WARN');
        }
    });
    return inventory;
}

//Upload

function upload(){
    var $file = $('#inventoryFile')
    if($file.val() == ''){
        toast("Select File","WARN");
        return ;
    }
    processData($file);
}

function updateFileName(){
	var $file = $('#inventoryFile');
    var fileName = $file.val().replace(/^.*[\\\/]/, '');
	$('#inventoryFileName').html(fileName);
}

function uploadRows(){
	
	var json = JSON.stringify(fileData);
    if(!checkFile(fileData)){
        toast("Invalid file format",'WARN')
        return;
    }
    let url = getInventoryUrl();

	// Make ajax call
	$.ajax({
	   url: url+'/upload',
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
            toast("Susccess",'INFO');
            getInventories()
            $("#uploadModal").modal("hide");
	   },
	   error: function(error){
           toast(error.responseJSON.message,'WARN')
	   }
	});

}

function checkFile(jsonFile){
    let keys = Object.keys(jsonFile[0])
    if(keys[0] == "barcode" && keys[1]=="quantity"){
        return true;
    }
    return false;
}

// Download CSV
function downloadCSV(){
    jsonToCsv(reportInventory);
}

//ACTIVE TAB
function activeTab() {
    $('#nav-brand').removeClass('active');
    $('#nav-product').removeClass('active');
    $('#nav-inventory').addClass('active');
    $('#nav-order').removeClass('active');
}

function openModal() {
    $('#addInventory').trigger("reset");
    $('#addModal').modal('show');
    $file.val("");
}

function init() {
    activeTab();
    $('#downloadCSVButton').click(downloadCSV);
    $('#editInventory').submit(updateInventory);
    $('#addInventoryModal').click(openModal);
    $('#addInventory').submit(addInventory);
    $('#inventoryFile').on('change', updateFileName);
    $('#upload-data').click(upload);
    $('#uploadModalButton').click(function(){
        $("#uploadModal").modal("show");
        $('#productFile').val("");
        $('#productFileName').html("Choose file");
    });
    $("#inventory-table").DataTable({
        data: [],
        info:false,
        columns: [
            {
                data: "barcode",
            },
            {
                data: "productName",
            },
            {
                data: "quantity",
            },
            {
                mData: null,
                bSortable: false,
                mRender: function (o) {
                    return (
                        "<span id='editButton' onclick="+'"editInventory(\'' +
                        o.barcode +'\','+o.quantity+
                        ')"><span class="material-icons md-24">edit</span></span>'
                    );
                },
            },
        ],
        "columnDefs": [
            { "width": "10px", "targets": -1 }
        ],
    });
    $('#editInventory :input').keyup(function () {
        let quantity = parseInt($('#quantity').val());
        if (quantity == (prevValue["quantity"])) {
            $('#update-inventory').prop('disabled', true);
            return;
        }
        $('#update-inventory').prop('disabled', false);
    });
}

$(document).ready(init);
$(document).ready(getInventories);