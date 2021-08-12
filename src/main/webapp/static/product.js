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

function addProduct() {
  var $form = $("#addProduct");
  var json = toJson($form);
  var url = getProductUrl();
  if (!validateDouble(json.mrp)) {
    $("#mrpError").html("Error");
    setTimeout(function () {
      $("#mrpError").empty();
    }, 10000);
    return false;
  }

  $.ajax({
     url: url,
     type: 'POST',
     data: json,
     headers: {
      'Content-Type': 'application/json'
     },
     success: function(response) {
          getProducts();
          $(':input','#addProduct')
            .not(':button, :submit, :reset, :hidden')
            .val('')
          $('#addModal').modal('hide');
          toast('Successfully created a Brand');
     },
     error: function(error){
          error = JSON.parse(error.responseText);
          $(':input','#addProduct')
            .not(':button, :submit, :reset, :hidden')
            .val('')
          $('#addModal').modal('hide');
          toast(error.message);

     }
  });
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

function updateProduct() {
  var $form = $("#editProduct");
  console.log($form);
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
      toast("Successful");
    },
    error: function (error) {
      console.log(error);
      alert("An error has occurred");
    },
  });
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

//HELPER METHOD
function toJson($form) {
  console.log($form);
  var serialized = $form.serializeArray();
  console.log(serialized);
  var s = "";
  var data = {};
  for (s in serialized) {
    data[serialized[s]["name"]] = serialized[s]["value"];
  }
  var json = JSON.stringify(data);
  console.log(json);
  return json;
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

function init() {
  activeTab();
  $("#update-product").click(updateProduct);
  $("#addProductModal").click(openModal);
  $("#addProductButton").click(addProduct);
  $("#product-table").DataTable({
    data: [],
    info:false,
    columns: [
      {
        data: "brandId",
      },
      {
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

function selectBrandOptions(brands) {
  $(".selectpicker").empty();
  console.log(brands);
  for (i in brands) {
    let brand = brands[i];
    let html =
      '<option data-tokens="' +
      brand.id +
      '" value=' +
      brand.id +
      ">" +
      brand.brand +
      " " +
      brand.category +
      "</option>";
    $(".selectpicker").append(html);
  }
  $(".selectpicker").selectpicker("refresh");
}

function getBrands() {
  let url = getBrandUrl();
  let brands;
  $.ajax({
    url: url,
    type: "GET",
    success: function (response) {
      brands = response;
      selectBrandOptions(brands);
    },
    error: function () {
      alert("An error has occurred");
    },
  });
}

$(document).ready(init);
$(document).ready(getProducts);
$(document).ready(getBrands);
