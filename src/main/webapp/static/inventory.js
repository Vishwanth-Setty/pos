//Global var
reportInventory = []

function displayInventoryData(inventories) {
    var $tbody = $('#inventory-table').find('tbody');
    var dataTable = $('#inventory-table').DataTable()
    dataTable.clear().draw();
    reportInventory = inventories;
    for(var i in inventories){
    		var e = inventories[i];
            // e["id"]=new Number(i)+1;
            dataTable.row.add(e).draw(false);
    		// var buttonHtml = '<span id="editButton" class="bi-pen" onclick="editInventory('+e.productId+')"></span>'
    		// var row = '<tr id="inventory'+i+'">'
    		// + '<td>' + e.productId + '</td>'
    		// + '<td>' + e.barcode + '</td>'
    		// + '<td>' + e.productName + '</td>'
    		// + '<td id="inventory'+e.productId+'quantity">' + e.quantity + '</td>'
    		// + '<td>' + buttonHtml + '</td>'
    		// + '</tr>';
            // $tbody.append(row);
    	}

    return false;
}

function addInventory(e) {
    var $form = $('#addInventory');
    console.log($form)
    e.preventDefault();
    var json = toJson($form);
    var url = getInventoryUrl();
    console.log(json);
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
            toast('Successfully created a Brand');
        },
        error: function (error) {
            error = JSON.parse(error.responseText);
            $(':input','#addInventory')
            .not(':button, :submit, :reset, :hidden')
            .val('')
            $('#addModal').modal('hide');
            toast(error.message,'WARN');
        }
    });

}

async function editInventory(barcode,quantity) {
    $('#editModal').modal('show');
    quantity = new Number(quantity);
    $('#quantity').val(quantity)
    $('#barcode').val(barcode)
}

function updateInventory(e) {
    e.preventDefault();
    var $form = $('#editInventory');
    console.log($form)
    var json = toJson($form);
    json = JSON.parse(json);
    json.quantity = parseInt(json.quantity);
    json = JSON.stringify(json);
    var url = getInventoryUrl();
    console.log(json);
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
            toast('Successful');
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
            console.log("sdf")
            console.log(inventories)
            displayInventoryData(inventories);
        },
        error: function (error) {
            console.log(error)
            //            alert(error+ "An error has occurred");
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
            console.log(error)
        }
    });
    return inventory;
}
//Upload

function upload(){
    var $file = $('#inventoryFile')
    processData($file);
}

function updateFileName(){
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	$('#inventoryFileName').html(fileName);
}

function uploadRows(){
	
	var json = JSON.stringify(fileData);
    console.log(json);
    let url = getInventoryUrl();
	// Make ajax call
	// $.ajax({
	//    url: url+'/upload',
	//    type: 'POST',
	//    data: json,
	//    headers: {
    //    	'Content-Type': 'application/json'
    //    },	   
	//    success: function(response) {
	//    		console.log("Susccessfully Uploaded",'INFO')  
	//    },
	//    error: function(error){
    //        console.log(error);
    //        toast(error.responseJSON.message,'WARN')
	//    }
	// });

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
    });
}

$(document).ready(init);
$(document).ready(getInventories);