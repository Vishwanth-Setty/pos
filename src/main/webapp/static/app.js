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
    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    console.log(json);
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




