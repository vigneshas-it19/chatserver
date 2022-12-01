/**
 * 
 */

var user;
var socket;




function makeConnection() {
	user = document.getElementById("username").value;
	// socket = new WebSocket("ws://localhost:8080/ChatServer/actions/" + user);
	// socket = new WebSocket("ws://6.tcp.ngrok.io:11407 /ChatServer/actions/" + user);
	socket = new WebSocket("ws://"+location.host+"/ChatServer/actions/" + user);
	socket.onmessage = onMessage;
	document.getElementById("message_inp").focus();
}
function sendMessage() {
	if(user==undefined){
		alert("Invalid Username");
		document.getElementById("message_inp").value = "";
		return;
	}
	var msg = document.getElementById("message_inp").value;
	socket.send(msg);
}


function onMessage(event) {
	var msg = JSON.parse(event.data);
	if (msg.from === user) {
		inner_html = "<div class='me'><div class='name'>You</div><div class='message'>" + msg.message + "</div>";
		var temp = document.getElementById("messages").innerHTML + inner_html;
		document.getElementById("messages").innerHTML = temp;
		document.getElementById("message_inp").value = "";
	} else {
		inner_html = "<div class='others'><div class='name'>" + msg.from + "</div><div class='message'>" + msg.message + "</div>";
		var temp = document.getElementById("messages").innerHTML + inner_html;
		document.getElementById("messages").innerHTML = temp;
		document.getElementById("message_inp").value = "";
	}
	// console.log(msg.from+"::::"+msg.message);
	var objDiv = document.getElementById("messages");
	objDiv.scrollTop = objDiv.scrollHeight;
}