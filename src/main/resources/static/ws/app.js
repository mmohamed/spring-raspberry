$.ws = {}, $.ws.app = {
	client : null,
	endpoint : '/endpoint-system',
	status : '/ws/status'
};

$.ws.init = function() {
	var socket = new SockJS($.ws.app.endpoint);
	$.ws.app.client = Stomp.over(socket);
	$.ws.app.client.debug = null;
	$.ws.app.client.connect({}, function(frame) {
		$.ws.app.client.subscribe($.ws.app.status, function(status) {
			var status = JSON.parse(status.body);
			$.ws.live.update('cpu-temperature', status.hardware.cpu.temperature, function(lastValue, newValue){
				if(newValue > 60) 
					return false; 
				return true;
			}, 'C°');
			
			$.ws.live.update('cpu-voltage', Math.round(status.hardware.cpu.coreVoltage*100)/100, function(lastValue, newValue){
				if(newValue > 1.5) 
					return false; 
				return true;
			}, 'V');
			
			$.ws.live.update('memory-used', Math.round(status.hardware.memory.used / (1024 * 1024)), function(lastValue, newValue){
				if(newValue > (1024*0.8)) 
					return false; 
				return true;
			}, 'MO');
			
			$.ws.live.update('memory-cached', Math.round(status.hardware.memory.cached / (1024 * 1024)), function(lastValue, newValue){
				if(newValue > (1024*3)) 
					return false; 
				return true;
			}, 'MO');
			
			$.ws.live.update('memory-shared', Math.round(status.hardware.memory.shared / (1024 * 1024)), function(lastValue, newValue){
				if(newValue > (1024*0.2)) 
					return false; 
				return true;
			}, 'MO');
			
			$.ws.live.update('memory-buffers', Math.round(status.hardware.memory.buffers / (1024 * 1024)), function(lastValue, newValue){
				if(newValue > (1024*0.1)) 
					return false; 
				return true;
			}, 'MO');
		})
	});
}

$.unload = function() {
	if ($.ws.app.client !== null) {
		$.ws.app.client.disconnect();
	}
}

$.ws.live = {}, $.ws.live.update = function(target, value, callback, unit) {
	var viewElement = $('#' + target),
		trendElement = $('#' + target + '-trend'),
		currentValue = viewElement.text().substr(0, viewElement.text().length-unit.length-1);
	
	if($.isNumeric(currentValue) && $.isNumeric(value)){
		var diff = Math.round((value-currentValue)*100)/100;
		if(diff >= 0){
			trendElement.find('i:first').html('<i class="fa fa-sort-asc"></i>'+Math.abs(diff)+' '+unit);
		}else{
			trendElement.find('i:first').html('<i class="fa fa-sort-desc"></i>'+Math.abs(diff)+' '+unit);
		}
	}else{
		trendElement.find('i:first').html('<i class="fa fa-sort"></i>--.- '+unit);
	}
	// callback for color
	trendElement.find('i:first').removeClass('red');
	trendElement.find('i:first').removeClass('green');
	if(typeof callback == 'function'){
		if(callback(currentValue, value)){
			trendElement.find('i:first').addClass('green');
		}else{
			trendElement.find('i:first').addClass('red');
		}
	}
	viewElement.text(value+' '+unit);
}