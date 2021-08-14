let orderItems = {};

function displayOrderData(inventories) {
    var $tbody = $('#order-table').find('tbody');
    var dataTable = $('#order-table').DataTable()
    dataTable.clear();
    for (var i in inventories) {
        var e = inventories[i];
        console.log(e);
        e.orderTime = new Date(e.orderTime.year,e.orderTime.monthValue-1,e.orderTime.dayOfMonth).toLocaleDateString();
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

function addOrder() {
    var $form = $('#addInventory');
    console.log($form)
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
            $(':input', '#addInventory')
                .not(':button, :submit, :reset, :hidden')
                .val('')
            $('#addModal').modal('hide');
            toast(error.message, 'WARN');
        }
    });

}

async function editOrder(id, quantity) {
    $('#editModal').modal('show');
    quantity = new Number(quantity);
    $('#quantity').val(quantity)
    $('#productId').val(id)
}

function updateInventory() {
    var $form = $('#editInventory');
    console.log($form)
    var id = $('#productId').val();
    var json = toJson($form);
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
        error: function () {
            alert("An error has occurred");
        }
    });
}

async function getProductByBarcode(barcode) {
    let url = getProductUrl() + "/barcode/" + barcode;
    let product;
    await $.ajax({
      url: url,
      type: "GET",
      success: function (response) {
        product = response;
        console.log(response+'sdf')
      },
      error: function (error) {
        console.log(error);
      },
    });
    return product;
}

function getOrders() {
    let url = getOrderUrl();
    let orders;
    $.ajax({
        url: url,
        type: 'GET',
        success: function (response) {
            orders = response;
            console.log(orders)
            displayOrderData(orders);
        },
        error: function (error) {
            console.log(error)
            //            alert(error+ "An error has occurred");
        }
    });
}
async function checkProduct(barcode){
    return await getProductByBarcode(barcode)
}

async function addProductToTable(){
    var validProduct = await checkProduct($('#addBarcode').val());
    if(validProduct=="" ){
        console.log("invalid");
        return ;
    }
    orderItems[validProduct.barcode] = $('#addQuantity').val()
    // var $tbody = $('#new-order-table').find('tbody');
    // var buttonHtml = '<span id="editButton" class="bi-pen" onclick="editInventory('+validProduct.barcode+')"></span>'
    // var row = '<tr>'
    // + '<td>' + validProduct.barcode + '</td>'
    // + '<td>' + $('#addQuantity').val() + '</td>'
    // + '<td>' + buttonHtml + '</td>'
    // + '</tr>';
    // $tbody.append(row);
    // console.log("Added");
    console.log(orderItems);
    return false;
}

//ACTIVE TAB
function activeTab() {
    $('#nav-brand').removeClass('active');
    $('#nav-product').removeClass('active');
    $('#nav-inventory').removeClass('active');
    $('#nav-order').addClass('active');
}

function openModal() {
    $('#addModal').modal('show');
}

function init() {
    activeTab();
    $('#createOrderModal').click(openModal);
    $('#addInventoryButton').click(addOrder);
    $("#order-table").DataTable({
        data: [],
        columns: [{
                data: "id",
            },
            {
                data: "orderTime",
            },
            {
                mData: null,
                bSortable: false,
                mRender: function (o) {
                    return (
                        '<span id="editButton" onclick="editOrder(' +
                        o.id +
                        ')"><span class="material-icons md-24">edit</span></span>'
                    );
                },
            },
        ],
    });
    
}

$(document).ready(init);
$(document).ready(getOrders);