function connect(event){
    var socket = new SockJS("/ws-stomp");
    stompClient = Stomp.over(socket,onConnected,onError);
    stompClient.connect({})
    event.preventDefault();
}

function onError(){
    alert("Websocket Error!")
}

function onConnected(){
    stompClient.subscribe("/sub/chat/room",onMessageReceived);

    stompClient.send(
        "/pub/chat/enterUser",
        {},
        JSON.stringify({
            type:"ENTER"
        })
    );
}

function sendMessage(event){
    var messageContent = messageInput.value.trim();
    if(stompClient && messageContent){
        var chatMessage = {
            type:"TALK",
            message:messageContent
        };

        stompClient.send("/pub/chat/send/message",{},JSON.stringify(chatMessage));
        messageInput.value = "";
    }
    event.preventDefault()
}

function onMessageReceived(payload){
    var chat = JSON.parse(payload.body)
    switch (chat) {
        case "ENTER":
            alert("입장")
            break;
        case "TALK":
            alert("말하기!")
            break;
        case "EXIT":
            alert("나가기")
            break;
    }
}