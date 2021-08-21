let orderItems = [];
let order = {};
let addOrderItems = [];
function displayOrderData(inventories) {
    var $tbody = $('#order-table').find('tbody');
    var dataTable = $('#order-table').DataTable()
    dataTable.clear();
    for (var i in inventories) {
        var e = inventories[i];
        console.log(e);
        var time = ( (e.orderTime.hour<10)?('0'+e.orderTime.hour.toString()):(e.orderTime.hour.toString())) 
                    + ":" +( (e.orderTime.minute<10)?('0'+e.orderTime.minute.toString()):(e.orderTime.minute.toString()));
        e.orderTime = new Date(e.orderTime.year,e.orderTime.monthValue-1,e.orderTime.dayOfMonth).toLocaleDateString();
        e.orderTime = e.orderTime + " " + time;
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
    dataTable.column(2).visible(false);


    return false;
}

function displayEditOrderItemData(editOrderData){
    var dataTable = $('#order-item-table').DataTable()
    dataTable.clear();
    for (var i in editOrderData) {
        var orderItem = editOrderData[i];
        console.log(orderItem);
        dataTable.row.add(orderItem).draw(false);
    }

}

function addOrder() {
    var dataTable = $('#new-order-table').DataTable()
    console.log(dataTable.rows().data())
    addOrderItems = [];
    for(let i=0;i<dataTable.rows().data().length;i++){
        addOrderItems.push(dataTable.rows().data()[i]);
    }
    console.log(addOrderItems);
    order["orderItemList"] = addOrderItems;
    var json = JSON.stringify(order);
    console.log(json);
    var url = getOrderUrl();
    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            getOrders();
            $(':input', '#createOrder')
                .not(':button, :submit, :reset, :hidden')
                .val('')
            $('#addModal').modal('hide');
            toast('Successfully created a Order');
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
    let url = getOrderUrl();
    $('#orderId').val(id);
    $('#editOrder').trigger('reset');
    $.ajax({ 
        url: url + '/' +id,
        type: 'GET',
        success: function (response) {
            orderItems = response;
            console.log(orderItems)
            displayEditOrderItemData(orderItems);
        },
        error: function (error) {
            alert(error+ "An error has occurred");
        }
    });
}

function updateOrder() {
    var dataTable = $('#order-item-table').DataTable()
    console.log(dataTable.rows().data())
    addOrderItems = [];
    for(let i=0;i<dataTable.rows().data().length;i++){
        addOrderItems.push(dataTable.rows().data()[i]);
    }

    console.log(addOrderItems);
    for(i in addOrderItems){
        addOrderItems[i]["quantity"] = parseInt(addOrderItems[i]["quantity"]);
        addOrderItems[i]["sellingPrice"] = parseFloat(addOrderItems[i]["sellingPrice"]);
    }
    
    order["orderItemList"] = addOrderItems;
    order["id"] = parseInt($('#orderId').val());
    var json = JSON.stringify(order);
    console.log(json);
    var url = getOrderUrl();
    $.ajax({
        url: url,
        type: 'PUT',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            getOrders();
            $('#editModal').modal('hide');
            toast('Successful');
        },
        error: function (error) {
            console.log(error);
            alert("An error has occurred");
        }
    });
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


function addProductToTable(e){
    e.preventDefault();
    let url = getProductUrl() + "/barcode/" + $('#addBarcode').val();
    let product = {};
    $.ajax({
      url: url,
      type: "GET",
      success: function (response) {
        product["barcode"] = response.barcode
        product["quantity"] = $('#addQuantity').val()
        product["sellingPrice"] = $('#addSellingPrice').val()
        var dataTable = $("#new-order-table").DataTable();        
        dataTable.row.add(product).draw(false);

      },
      error: function (error) {
          console.log("error");
          toast("Invalid Barcode",'WARN');
    },
    });
    
}

async function addProductToTableToUpdateOrderItem(){
    var validProduct = await checkProduct($('#addItemBarcodeToUpdate').val());
    if(validProduct == "" ){
        console.log("invalid");
        return ;
    }
    product = {};
    product["orderItemId"] = 0
    product["barcode"] = validProduct.barcode
    product["quantity"] = $('#addItemQuantityToUpdate').val()
    product["sellingPrice"] = $('#addItemSellingPriceToUpdate').val()
    product["orderId"] = $('#orderId').val()
    var dataTable = $("#order-item-table").DataTable();
    dataTable.row.add(product).draw(false);
    return false;
}

function editOrderItem(barcode,quantity,sellingPrice){

    $('#editOrderItems').trigger("reset");
    $('#editItemModal').modal('show');
    $('#editItemQuantity').val(quantity);
    $('#editItemSellingPrice').val(sellingPrice);
    $('#editItemBarcode').val(barcode);

}

function changeEditOrderItemData(){
    for( items of orderItems){
        if(items["barcode"] == $('#editItemBarcode').val()){
    
            items["quantity"] = $('#editItemQuantity').val();
            items["sellingPrice"] = $('#editItemSellingPrice').val();

        }
    }
    $('#editItemModal').modal('hide');
    displayEditOrderItemData(orderItems);
}

//ACTIVE TAB
function activeTab() {
    $('#nav-brand').removeClass('active');
    $('#nav-product').removeClass('active');
    $('#nav-inventory').removeClass('active');
    $('#nav-order').addClass('active');
}

//Deleted Product
$('#new-order-table tbody').on( 'click', 'tr', function () {
    var dataTable = $("#new-order-table").DataTable();

    if ( $(this).hasClass('selected') ) {
        $(this).removeClass('selected');
    }
    else {
        dataTable.$('tr.selected').removeClass('selected');
        $(this).addClass('selected');
    }
} );

$('#new-order-table').on( 'click', 'tbody td.row-remove', function (e) {
    var dataTable = $("#new-order-table").DataTable();
    dataTable.row('.selected').remove().draw( false );
} );


//GenerateInvoice
function generateInvoice(orderId){
    orderId = parseInt(orderId);
    var url = getOrderUrl()+"/invoice/"+orderId;
    $.ajax({
        url: url,
        type: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            getOrders();
            toast('Successfull');
        },
        error: function (error) {
            error = JSON.parse(error.responseText);
            toast(error.message, 'WARN');
        }
    });
}
function openModal() {
    $('#addModal').modal('show');
}

function init() {
    activeTab();
    $('#createOrderModal').click(openModal);
    $('#addOrderSubmit').click(addOrder);
    $('#createOrder ').submit(addProductToTable);
    $('#editItemSubmit').click(changeEditOrderItemData);
    $('#update-order').click(updateOrder);
    $('#addProductToUpdateOrder').click(addProductToTableToUpdateOrderItem);

    
    $("#order-table").DataTable({
        data: [],
        info: false,
        columns: [{
                data: "orderId",
            },
            {
                data: "orderTime",
            },
            {
                data: "invoiceGenerated",
            },
            {
                mData: null,
                bSortable: false,
                mRender: function (o) {
                    var html = '<span class="invoice" onclick="generateInvoice(' +
                    o.orderId +
                    ')"><span data-toggle="tooltip" data-placement="top" title="Generate Invoice" class="material-icons md-24">receipt</span></span>'
                    if(!o.invoiceGenerated){
                        html+= '<span id="editButton" onclick="editOrder(' +
                        o.orderId +
                        ')"><span data-toggle="tooltip" data-placement="top" title="Edit Order" class="material-icons md-24">edit</span></span>'

                    }
                    else{
                        html+= '<span id="editButton"style="color:#c3c3c3;"><span  class="disable-edit material-icons md-24">edit</span></span>'
                    }
                    return html;
                },
            },
        ],
        "columnDefs": [
            { "width": "100px", "targets": -1 }
        ]
    });
    
    $("#new-order-table").DataTable({
        data: [],
        paging:   false,
        ordering: false,
        info:     false,
        searching: false,
        columns: [
            {
                data: "barcode",
            },
            {
                data: "quantity",
            },
            {
                data: "sellingPrice",
            },
            {
                mData: null,
                bSortable: false,
                className: 'row-remove',
                mRender: function (o) {
                    return (
                        '<span class="remove-row"><span class="material-icons md-24">delete_outline</span></span>'
                    );
                },
            },
        ],
        "columnDefs": [
            { "width": "15px", "targets": 1 }
        ],
    });
    $("#order-item-table").DataTable({
        data: [],
        paging:   false,
        ordering: false,
        info:     false,
        searching: false,
        columns: [
            {
                data: "barcode",
            },
            {
                data: "quantity",
            },
            {
                data: "orderId"
            },
            {
                data: "sellingPrice",
            },
            {
                mData: null,
                bSortable: false,
                className: 'row-remove',
                mRender: function (o) {
                    return (                        
                        "<span id='editButton' onclick=editOrderItem(\'" +
                        o.barcode + "\',"+ o.quantity+','+o.sellingPrice+
                        ')><span class="material-icons md-24">edit</span></span>'
                    );
                },
            },
        ],
    });
    
}

$(document).ready(init);
$(document).ready(getOrders);