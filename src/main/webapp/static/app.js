//TOAST SERVICE
function toast(message,type){
    $('.toast-body').html(message)
    switch (type){
        case 'INFO':
            $('#toast').css({"background": "#9affa2", "opacity": "1","color": "black"});
            $('#toast').css('opacity', '1');
            setTimeout(function(){ $('#toast').css({"background": "#ffffff00", "opacity": "0","color": "black"})}, 3000);
            break;
        case 'WARN':
            $('#toast').css({"background": "#e65e3f", "opacity": "1", "color": "white"});
            break;
        default :
            $('#toast').css({"background": "#9affa2", "opacity": "1","color": "black"});
            $('#toast').css('opacity', '1');
            setTimeout(function(){ $('#toast').css({"background": "#ffffff00", "opacity": "0", "color": "black"})}, 3000);
            break;
    };
    
}
$('#close-button').click(function(){
    $('#toast').css({"background": "#ffffff00", "opacity": "0"})
})

//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value'].trim().toLowerCase()
    }
    var json = JSON.stringify(data);
    return json;
}

function errorMsg(error) {
    msgs = error.split("#");
    rows = []
    for (msg of msgs) {
        row = {}

        row["number"] = msg.split("*")[0];
        row["message"] = msg.split("*")[1];
        rows.push(row);
    }
    var html = ''
    for (row of rows) {
        if(row.number != "")
            html += '<p>Row(' + row.number + ') ' + row.message.slice(0,-1) + "</p>"
    }
    return html;
}


//URL
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}
function getInventoryUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/inventory";
}
function getOrderUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/order";
}
function getReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report";
}


//validations

function validatePositiveNumber(num){
    var number = new Number(num)
    if(!Number.isInteger(number)){
        return "Not a Number";
    }
    if(number<0){
        return "Number should be non negative";
    }
    return true;
}
function validateDouble(number){
    var num = new Number(number);
    if(/^[0-9]{0,3}(\.[0-9]{0,2})?$/.test(num) && num > 0){
        return true;
    }
    return false;
}

//JSON to CSV

function jsonToCsv(json){
    const items = json
    const replacer = (key, value) => value === null ? '' : value // specify how you want to handle null values here
    const header = Object.keys(items[0])
    const csv = [
    header.join('\t'), // header row first
    ...items.map(row => header.map(fieldName => JSON.stringify(row[fieldName], replacer)).join('\t'))
    ].join('\r\n')

    blob = new Blob([csv], { type: 'text/csv' });
    var csvUrl = window.webkitURL.createObjectURL(blob);
    ReportTitle = "report";
    var filename =  (ReportTitle || 'UserExport') + '.tsv';
    //this trick will generate a temp "a" tag
    var link = document.createElement("a");
    link.id = "lnkDwnldLnk";

    //this part will append the anchor tag and remove it after automatic click
    document.body.appendChild(link);
    $("#lnkDwnldLnk")
        .attr({
            'download': filename,
            'href': csvUrl
        });

    $('#lnkDwnldLnk')[0].click();
    document.body.removeChild(link);
    console.log(csv)
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;
var type;


function processData($file,from){
    // console.log($file);
    type = from;
	var file = $file[0].files[0];
    // console.log("file" ,file)
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
    console.log(fileData)
    if(fileData.length>5000){
        alert("Cant upload more than 5000 rows")
        return false;
    }
	uploadRows();
}

const downloadFile = (blob, fileName) => {
    const link = document.createElement('a');
    // create a blobURI pointing to our Blob
    console.log(blob);
    link.href = URL.createObjectURL(blob);
    console.log(link.href);
    link.download = fileName;
    // some browser needs the anchor to be in the doc
    document.body.append(link);
    link.click();
    link.remove();
    // in case the Blob uses a lot of memory
    setTimeout(() => URL.revokeObjectURL(link.href), 7000);
  };

  function b64toBlob (b64Data, contentType='', sliceSize=512) {
    const byteCharacters = atob(b64Data);
    const byteArrays = [];
  
    for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
      const slice = byteCharacters.slice(offset, offset + sliceSize);
  
      const byteNumbers = new Array(slice.length);
      for (let i = 0; i < slice.length; i++) {
        byteNumbers[i] = slice.charCodeAt(i);
      }
  
      const byteArray = new Uint8Array(byteNumbers);
      byteArrays.push(byteArray);
    }
  
    const blob = new Blob(byteArrays, {type: contentType});
    return blob;
  }
  


function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}	
	}
	Papa.parse(file, config);
}


function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.tsv');
    tempLink.click(); 
}





