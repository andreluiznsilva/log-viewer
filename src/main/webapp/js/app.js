function Connection(callback) {

	this.contentType = "application/json";
	this.logLevel = 'debug';
	this.trackMessageLength = true;
	this.transport = 'websocket';
	this.fallbackTransport = 'long-polling';

	this.onMessage = function(response) {
		var messages = eval(response.responseBody);
		callback(messages);
	};

	this.open = function(fileName) {
		this.url = document.location.toString() + 'log/change/' + fileName;
		atmosphere.subscribe(this);
	}

	this.close = function() {
		atmosphere.unsubscribe(this);
	}

}

function Terminal(selector) {

	this.output = $(selector);
	
	this.autoScroll = true;

	this.clear = function() {
		this.output.html('');
		this.waiting = false;
	}

	this.wait = function() {
		this.output.html('Waiting...</br>');
		this.waiting = true;
	}

	this.write = function(messages) {
		if (this.waiting) {
			this.clear();
		}
		for (var i = 0; i < messages.length; i++) {
			this.output.append(messages[i] + "</br>");
		}
		if (this.autoScroll) {
			this.output.stop().animate({
				scrollTop : this.output[0].scrollHeight
			}, 800);
		}
	}

}

$(document).ready(function() {

	var selector = $('#selector');
	var terminal = new Terminal('#terminal');
	var connection = new Connection(function(messages) {
		terminal.write(messages);
	});

	$.get('/log/list', function(files) {
		selector.find('option').remove();
		$.each(files, function(key, value) {
			$('<option>').val(value).text(value).appendTo(selector);
		});
		terminal.wait();
		connection.open(files[0]);
	});

	selector.change(function() {
		var fileName = $("#selector option:selected").text();
		connection.close();
		terminal.clear();
		terminal.wait();
		connection.open(fileName);
	});

	$('#clear').click(function() {
		terminal.clear();
	});

	$('#autoScroll').click(function() {
		terminal.autoScroll = !terminal.autoScroll;
		$('#autoScroll').html(terminal.autoScroll ? 'Auto Scrool On' : 'Auto Scrool Off');
		$('#autoScroll').toggleClass('btn-info', !terminal.autoScroll);
	});

});