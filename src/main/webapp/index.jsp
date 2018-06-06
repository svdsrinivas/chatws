<html>
<head>
<title>Chat WebSocket with Lettuce Redis - by Karthik Chengayan
	Sridhar</title>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.2.10/semantic.min.css">
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.2.10/semantic.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/0.3.4/sockjs.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.js"></script>
<script type="text/javascript">
	var stompClient = null;

	function setConnected(connected) {
		document.getElementById('connect').disabled = connected;
		document.getElementById('disconnect').disabled = !connected;
		document.getElementById('from-field').style.visibility = connected ? 'hidden'
				: 'visible';
		document.getElementById('conversationDiv').style.visibility = connected ? 'visible'
				: 'hidden';
		document.getElementById('response').innerHTML = '';
	}

	function connect() {
		var socket = new SockJS('/lettuceexample/rest/chat');
		stompClient = Stomp.over(socket);
		stompClient.connect({
			userId : 'codeninja'
		}, function(frame) {
			setConnected(true);
			console.log('Connected: ' + frame);
			stompClient.subscribe('/topic/messages', function(messageOutput) {
				showMessageOutput(JSON.parse(messageOutput.body));
			});

			stompClient.subscribe('/user/queue/reply', function(messageOutput) {
				console.log(JSON.parse(messageOutput.body));
				showChatMessage(JSON.parse(messageOutput.body));
			});
		});
	}

	function disconnect() {
		if (stompClient != null) {
			stompClient.disconnect();
		}
		setConnected(false);
		console.log("Disconnected");
	}

	function sendMessage() {
		var from = document.getElementById('from').value;
		var text = document.getElementById('text').value;
		stompClient.send("/app/chat", {}, JSON.stringify({
			'from' : from,
			'text' : text
		}));
	}

	function showMessageOutput(messageOutput) {
		createListItemAndAppend(messageOutput.from, messageOutput.text + " ("
				+ new Date(messageOutput.timestamp) + ")");
	}

	function showChatMessage(messageOutput) {
		createListItemAndAppend(messageOutput.from, messageOutput.message);
	}

	function createListItemAndAppend(from, message) {
		var parent = document.getElementById('chat-list');

		var itemParent = document.createElement('div');
		itemParent.className = "item";

		var avatar = document.createElement('img');
		avatar.className = "ui avatar image";
		avatar.src = "assets/avatar.png";
		itemParent.appendChild(avatar);

		var content = document.createElement('div');
		content.className = "content";
		var name = document.createElement('p');
		name.className = "header";
		name.innerText = from;
		content.appendChild(name);

		var messageContainer = document.createElement('div');
		messageContainer.className = "description";
		messageContainer.innerText = message;
		content.appendChild(messageContainer);

		itemParent.appendChild(content);
		parent.appendChild(itemParent);

	}
</script>
</head>
<body onload="disconnect()">

	<div class="ui icon message">
		<div class="content" style="text-align: center;">
			<div class="header">Spring, Websocket, Redis (Lettuce) based
				chat application - Karthik Chengayan Sridhar</div>
		</div>
	</div>

	<div class="ui container">

		<div>
			<div class="ui form">
				<div id="from-field" class="field">
					<label>Nickname</label> <input type="text" name="from" id="from"
						placeholder="Choose a nickname">
				</div>

				<button class="ui primary button" id="connect" onclick="connect();">
					Connect</button>
				<button class="ui button" id="disconnect" disabled="disabled"
					onclick="disconnect();">Disconnect</button>

				<br />
				<br />
				<div id="conversationDiv">
					<input type="text" id="text" placeholder="Write a message..." /><br />
					<br />
					<button id="sendMessage" class="ui primary button"
						onclick="sendMessage();">Send</button>
					<p id="response"></p>
				</div>
			</div>
		</div>

		<div id="chat-list" class="ui list"></div>
</body>
</html>