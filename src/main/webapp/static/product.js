let prevValue = {};

function displayProductData(products) {
    let table = $("#product-table").DataTable();
    table.clear();
    table.column(0).visible(false);
    for (var i in products) {
        var e = products[i];
        table.row.add(e).draw(false);
    }
    return false;
}

function addProduct(e) {
    e.preventDefault();
    var $form = $("#addProduct");
    var json = toJson($form);
    var url = getProductUrl();
    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            getProducts();
            $(':input', '#addProduct')
                .not(':button, :submit, :reset, :hidden')
                .val('')
            $('#addModal').modal('hide');
            toast('Success','INFO');
        },
        error: function (error) {
            error = JSON.parse(error.responseText);
            toast(error.message,'WARN');

        }
    });
    return false;
}

async function editProduct(id) {
    $("#editModal").modal("show");
    let product = await getProductById(id);
    $("#productId").val(product.id);
    $("#brandId").val(product.brandId);
    $("#barcode").val(product.barcode);
    $("#brandName").val(product.brand);
    $("#categoryName").val(product.category);
    $("#name").val(product.name);
    $("#mrp").val(product.mrp);

    prevValue["name"] = product.name;
    prevValue["mrp"] = product.mrp;
}

function updateProduct(e) {
    e.preventDefault()
    var $form = $("#editProduct");
    var id = $("#productId").val();
    var json = toJson($form);
    var url = getProductUrl();
    $.ajax({
        url: url + "/" + id,
        type: "PUT",
        data: json,
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            getProducts();
            $("#editModal").modal("hide");
            toast("Success");
        },
        error: function (error) {
            error = JSON.parse(error.responseText);
            toast(error.message,'WARN');
        },
    });
    return false;
}

function getProducts() {
    let url = getProductUrl();
    let products;
    $.ajax({
        url: url,
        type: "GET",
        success: function (response) {
            products = response;
            displayProductData(products);
        },
        error: function (error) {
            toast("Could not Retrive Information",'WARN');
        },
    });
}
async function getProductById(id) {
    let url = getProductUrl() + "/" + id;
    let product;
    await $.ajax({
        url: url,
        type: "GET",
        success: function (response) {
            product = response;
        },
        error: function (error) {
        },
    });
    return product;
}

//Upload

function upload(){
    var $file = $('#productFile');
    if($file.val() == ''){
        toast("Select File","WARN");
        return ;
    }
    processData($file);
}

function updateFileName(){
	var $file = $('#productFile');
    var fileName = $file.val().replace(/^.*[\\\/]/, '');
	$('#productFileName').html(fileName);
}

function uploadRows(){
	for(item in fileData){
        fileData[item]["mrp"] = parseInt(fileData[item]["mrp"]);
    }
    if(!checkFile(fileData)){
        toast("Invalid file format",'WARN')
        return;
    }

	var json = JSON.stringify(fileData);
    let url = getProductUrl();

	// Make ajax call
	$.ajax({
	   url: url+'/upload',
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
        toast("Susccessfull",'INFO');
        getProducts(); 
        $("#uploadModal").modal("hide");

	   },
	   error: function(error){
           toast(error.responseJSON.message,'WARN');
           var $file = $('#productFile');
            $file.val('');
            $('#productFileName').html('');
	   }
	});

}

function checkFile(jsonFile){
    let keys = Object.keys(jsonFile[0])
    if(keys[0] == "barcode" && keys[1]=="brand"  && keys[2]=="category"  && keys[3]=="name"  && keys[4]=="mrp" ){
        return true;
    }
    return false;
}

//ACTIVE TAB
function activeTab() {
    $("#nav-brand").removeClass("active");
    $("#nav-product").addClass("active");
    $("#nav-inventory").removeClass("active");
    $("#nav-order").removeClass("active");
}

function openModal() {
    $('#addProduct').trigger("reset");
    $("#addModal").modal("show");
}

//Validation


function init() {
    activeTab();
    
    $("#editProduct").submit(updateProduct);
    $("#addProductModal").click(openModal);
    $("#addProduct").submit(addProduct);
    $('#productFile').on('change', updateFileName);
    $('#upload-data').click(upload);
    $('#uploadModalButton').click(function(){
        $("#uploadModal").modal("show");
        $('#inventoryFile').val("");
        $('#inventoryFileName').html("Choose file");
    });

    $("#product-table").DataTable({
        data: [],
        info: false,
        columns: [{
                data: "id",
            },
            {
                data: "barcode",
            },
            {
                data: "brand",
            },
            {
                data: "category",
            },
            {
                data: "name",
            },
            {
                data: "mrp",
            },
            {
                mData: null,
                bSortable: false,
                mRender: function (o) {
                    return (
                        '<span id="editButton" onclick="editProduct(' +
                        o.id +
                        ')"><span data-toggle="tooltip" data-placement="top" title="Edit" class="material-icons md-24">edit</span></span>'
                    );
                },
            },
        ],
        "columnDefs": [
            { "width": "10px", "targets": -1 }
        ],
    });

    $('#editProduct :input').keyup(function () {
        let name = $('#name').val();
        let mrp = $('#mrp').val();
        if (name == prevValue["name"] && mrp == prevValue["mrp"]) {
            $('#update-product').prop('disabled', true);
            return;
        }
        $('#update-product').prop('disabled', false);
    });
}

$(document).ready(init);
$(document).ready(getProducts);
