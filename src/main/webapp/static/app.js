//TOAST SERVICE
function toast(message,type){
    $('.toast-body').text(message)
    switch (type){
        case 'INFO':
            $('#toast').css({"background": "#63d69d", "opacity": "1"});
            break;
        case 'WARN':
            $('#toast').css({"background": "#ff0000", "opacity": "1"});
            break;
        default :
            $('#toast').css({"background": "#63d69d", "opacity": "1"});
            break;
    };
    $('#toast').css('opacity', '1');
    setTimeout(function(){ $('#toast').css({"background": "#ffffff00", "opacity": "0"})}, 3000);
}

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



function downloadErrors(){
	writeFileData(errorData);
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





