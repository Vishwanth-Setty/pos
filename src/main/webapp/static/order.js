let orderItems = [];
let order = {};
let addOrderItems = [];
var editor;

function displayOrderData(inventories) {
    var $tbody = $('#order-table').find('tbody');
    var dataTable = $('#order-table').DataTable()
    dataTable.clear();
    for (var i in inventories) {
        var e = inventories[i];
        var time = ((e.orderTime.hour < 10) ? ('0' + e.orderTime.hour.toString()) : (e.orderTime.hour.toString())) +
            ":" + ((e.orderTime.minute < 10) ? ('0' + e.orderTime.minute.toString()) : (e.orderTime.minute.toString()));
        e.orderTime = new Date(e.orderTime.year, e.orderTime.monthValue - 1, e.orderTime.dayOfMonth).toLocaleDateString();
        e.orderTime = e.orderTime + " " + time;
        dataTable.row.add(e).draw(false);
    }
    dataTable.column(2).visible(false);


    return false;
}

function displayEditOrderItemData(editOrderData) {
    var dataTable = $('#order-item-table').DataTable()
    dataTable.clear();
    for (var i in editOrderData) {
        var orderItem = editOrderData[i];
        dataTable.row.add(orderItem).draw(false);
    }

}

function addOrder() {
    var dataTable = $('#new-order-table').DataTable()
    addOrderItems = [];
    for (let i = 0; i < dataTable.rows().data().length; i++) {
        addOrderItems.push(dataTable.rows().data()[i]);
    }
    order["orderItemList"] = addOrderItems;
    var json = JSON.stringify(order);
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
            toast('Success');
        },
        error: function (error) {
            error = JSON.parse(error.responseText);
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
        url: url + '/' + id,
        type: 'GET',
        success: function (response) {
            orderItems = response;
            displayEditOrderItemData(orderItems);
        },
        error: function (error) {
            toast("Could not Retrive Information", 'WARN');
        }
    });
}

function updateOrder(e) {
    e.preventDefault();
    var dataTable = $('#order-item-table').DataTable()
    addOrderItems = [];
    for (let i = 0; i < dataTable.rows().data().length; i++) {
        addOrderItems.push(dataTable.rows().data()[i]);
    }

    for (i in addOrderItems) {
        var quantityid = '#Q' + addOrderItems[i]["orderItemId"].toString() + addOrderItems[i].barcode;
        var sellingPriceid = '#S' + addOrderItems[i]["orderItemId"].toString() + addOrderItems[i].barcode;

        addOrderItems[i]["orderId"] = parseInt(addOrderItems[i]["orderId"]);
        addOrderItems[i]["quantity"] = parseInt($(quantityid).val());
        addOrderItems[i]["sellingPrice"] = parseFloat($(sellingPriceid).val());
    }

    order["orderItemList"] = addOrderItems;
    order["id"] = parseInt($('#orderId').val());
    var json = JSON.stringify(order);
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
            toast('Success');
        },
        error: function (error) {
            error = JSON.parse(error.responseText);
            toast(error.message, 'WARN');
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
            displayOrderData(orders);
        },
        error: function (error) {
            toast("Could not Retrive Information", 'WARN');
        }
    });
}


function addProductToTable(e) {
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
            toast("Invalid Barcode", 'WARN');
        },
    });

}

async function addProductToTableToUpdateOrderItem(e) {
    e.preventDefault();
    let url = getProductUrl() + "/barcode/" + $('#addItemBarcodeToUpdate').val();
    let product = {};
    $.ajax({
        url: url,
        type: "GET",
        success: function (validProduct) {
            product = {};
            product["orderItemId"] = 0
            product["barcode"] = validProduct.barcode
            product["quantity"] = $('#addItemQuantityToUpdate').val()
            product["sellingPrice"] = $('#addItemSellingPriceToUpdate').val()
            product["orderId"] = $('#orderId').val()
            var dataTable = $("#order-item-table").DataTable();
            dataTable.row.add(product).draw(false);

        },
        error: function (error) {
            toast("Invalid Barcode", 'WARN');
        },
    });
}

function editOrderItem(barcode, quantity, sellingPrice) {

    $('#editOrderItems').trigger("reset");
    // $('#editItemModal').modal('show');
    $('#editOrderItemBlock').slideDown()
    $('#editItemQuantity').val(quantity);
    $('#editItemSellingPrice').val(sellingPrice);
    $('#editItemBarcode').val(barcode);

}

function changeEditOrderItemData() {
    for (items of orderItems) {
        if (items["barcode"] == $('#editItemBarcode').val()) {

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
$('#new-order-table tbody').on('click', 'tr', function () {
    var dataTable = $("#new-order-table").DataTable();

    if ($(this).hasClass('selected')) {
        $(this).removeClass('selected');
    } else {
        dataTable.$('tr.selected').removeClass('selected');
        $(this).addClass('selected');
    }
});

$('#new-order-table').on('click', 'tbody td.row-remove', function (e) {
    var dataTable = $("#new-order-table").DataTable();
    dataTable.row('.selected').remove().draw(false);
});


//GenerateInvoice
function generateInvoice() {
    orderId = parseInt($('#orderIdForInvoice').val());
    var url = getOrderUrl() + "/invoice/" + orderId;
    $.ajax({
        url: url,
        responseType: 'blob',
        type: 'PUT',
        headers: {
            'Content-Type': 'application/pdf'
        },
        success: function (response) {
            getOrders();
            toast('Success');
            var file = b64toBlob(response);
            downloadFile(file, "test.pdf");
            $('#downloadInvoiceModal').modal('hide');

        },
        error: function (error) {
            error = JSON.parse(error.responseText);
            toast(error.message, 'WARN');
        }
    });
}

function openModal() {
    $('#createOrder').trigger('reset');
    $('#new-order-table').DataTable().clear().draw();
    $('#addModal').modal('show');
}

function openModalForInvoice(orderId) {
    $('#orderIdForInvoice').val(orderId);
    $('#downloadInvoiceModal').modal('show');
}

function init() {
    activeTab();
    $('#createOrderModal').click(openModal);
    $('#addOrderSubmit').click(addOrder);
    $('#createOrder').submit(addProductToTable);
    $('#editItemSubmit').click(changeEditOrderItemData);
    $('#editOrder').submit(updateOrder);
    $('#addProductToUpdateOrder').click(addProductToTableToUpdateOrderItem);
    $('#downloadInvoice').click(generateInvoice);
    $('#editOrderItemBlock').hide()

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
                    var html = '<span class="invoice" onclick="openModalForInvoice(' +
                        o.orderId +
                        ')"><span data-toggle="tooltip" data-placement="top" title="Generate Invoice" class="material-icons md-24">receipt</span></span>'
                    if (!o.invoiceGenerated) {
                        html += '<span id="editButton" onclick="editOrder(' +
                            o.orderId +
                            ')"><span data-toggle="tooltip" data-placement="top" title="Edit Order" class="material-icons md-24">edit</span></span>'

                    } else {
                        html += '<span id="editButton"style="color:#c3c3c3;"><span  class="disable-edit material-icons md-24">edit</span></span>'
                    }
                    return html;
                },
            },
        ],
        "columnDefs": [{
            "width": "100px",
            "targets": -1
        }]
    });

    $("#new-order-table").DataTable({
        data: [],
        paging: false,
        ordering: false,
        info: false,
        searching: false,
        bAutoWidth: false,
        columns: [{
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
                        '<span class="remove-row"><span data-toggle="tooltip" data-placement="top" title="Delete" class="material-icons md-24">delete_outline</span></span>'
                    );
                },
            },
        ],
        "columnDefs": [{
            "width": "15px",
            "targets": 1
        }],
    });
    $("#order-item-table").DataTable({
        data: [],
        paging: false,
        ordering: false,
        info: false,
        searching: false,
        bAutoWidth: false,
        columns: [{
                data: "barcode",
            },
            {
                data: "quantity",
                render: function (data, type, row) {
                    return '<input class="form-control" style="height:20px;border:none;background:none;" type="text" pattern="^[0-9]\\d*$"title="Enter Only Non Negetive Integers"  id="Q' +
                        row.orderItemId + row.barcode + '" value="' + data + '"/>'
                },
            },
            {
                data: "orderId"
            },
            {
                data: "sellingPrice",
                render: function (data, type, row) {
                    return '<input class="form-control" style="height:20px;border:none;background:none;" type="text" pattern="^(?:[1-9]\\d*|0)?(?:\\.\\d+)?$" title="Enter Valid positive number." id="S' +
                        row.orderItemId + row.barcode + '" value="' + data + '"/>'
                },
            },
        ],
    });

}

$(document).ready(init);
$(document).ready(getOrders);