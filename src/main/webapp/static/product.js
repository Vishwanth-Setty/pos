function displayProductData(products) {
    console.log(products);
    var $tbody = $("#product-table").find("tbody");
    let table = $("#product-table").DataTable();
    table.clear();
    table.column(0).visible(false);
    for (var i in products) {
        var e = products[i];
        table.row.add(e).draw(false);
        // var buttonHtml = '<span id="editButton" class="bi-pen" onclick="editProduct('+
        // e.id+')"><span class="material-icons md-24">edit</span></span>'
        // var row = '<tr id="product'+i+'">'
        // + '<td>' + e.id + '</td>'
        // + '<td>' + e.barcode + '</td>'
        // + '<td>' + e.brand + '</td>'
        // + '<td>'  + e.category + '</td>'
        // + '<td>' + e.name + '</td>'
        // + '<td> &#8377 ' + e.mrp + '</td>'
        // + '<td>' + buttonHtml + '</td>'
        // + '</tr>';
        // $tbody.append(row);
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
            // $(':input', '#addProduct')
            //     .not(':button, :submit, :reset, :hidden')
            //     .val('')
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
}

function updateProduct(e) {
    e.preventDefault()
    var $form = $("#editProduct");
    var id = $("#productId").val();
    var json = toJson($form);
    var url = getProductUrl();
    console.log(json);
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
            console.log(products);
            displayProductData(products);
        },
        error: function (error) {
            console.log(error);
            //            alert(error+ "An error has occurred");
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
            console.log(error);
        },
    });
    return product;
}

//Upload

function upload(){
    var $file = $('#productFile');
    
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
	var json = JSON.stringify(fileData);
    console.log(json);
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
	   },
	   error: function(error){
           console.log(error);
           toast(error.responseJSON.message,'WARN')
	   }
	});

}


//ACTIVE TAB
function activeTab() {
    console.log("Asdfa");
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
                        ')"><span class="material-icons md-24">edit</span></span>'
                    );
                },
            },
        ],
    });
}

$(document).ready(init);
$(document).ready(getProducts);
