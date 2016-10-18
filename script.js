var sock = null;
var alive = false;

function start() {
    sock = new WebSocket("192.168.1.11:14641");

    sock.onopen() = function (e) {
        document.getElementById("p").innerHTML += "\nOpened!";
    }

    sock.onclose() = function (e) {
        document.getElementById("p").innerHTML += "\nClosed!";
    }


    sock.onmessage = function (e) {
        document.getElementById("p").innerHTML += "\nServer: " + e.data;
    }

}



function send(){
    if (alive) {

    }
}