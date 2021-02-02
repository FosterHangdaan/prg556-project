window.onload = function(){
	new JsDatePick({
		useMode:2,
		target:"pickupDate",
		dateFormat:"%F %d, %Y"
	});
	
	new JsDatePick({
		useMode:2,
		target:"dropoffDate",
		dateFormat:"%F %d, %Y"
	});
};
