var sock = null;
var alive = false;

function start() {
    sock = new WebSocket("ws://192.168.1.14:14641");
    
    sock.onopen = function (e) {
        document.getElementById("p").innerHTML += "<br>Opened!";
        sock.send("Hello");
    }

    sock.onclose = function (e) {
        document.getElementById("p").innerHTML += "<br>Closed!";
    }

    sock.onerror = function (e) {
        document.getElementById("p").innerHTML += "<br>Error!";
    }

    sock.onmessage = function (e) {
        document.getElementById("p").innerHTML += "<br>Server: " + e.data;
    }

}


function send(){
    if (alive) {

    }
}