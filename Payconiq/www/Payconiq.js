



var exec = require('cordova/exec');

var PLUGIN_NAME = 'Payconiq';

var Payconiq = {
  pay: function(transactionId,returnUrl) {
    exec(
        function(){
            alert("Success");
        }, 
        function(error){
            console.error(error);
            alert("error");
        }, 
        PLUGIN_NAME, 
        'pay', 
        [transactionId,returnUrl]);
  }
};

module.exports = Payconiq;