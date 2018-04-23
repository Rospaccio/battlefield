(function(){
    console.log("Initializing game instance");
    
    var SYNC_TIMEOUT_MILLIS = BATTLEFIELD_CONSTANTS.SYNC_TIMEOUT_MILLIS;
    var SERVER_CONTEXT_PATH = BATTLEFIELD_CONSTANTS.SERVER_CONTEXT_PATH;
    
    var stompClient = {};
        
    battlefield = new Battlefield('battlefieldCanvas', SERVER_CONTEXT_PATH);
    
    window.addEventListener('DOMContentLoaded', function(){
        battlefield.init();
        connect();
    });
    
    window.addEventListener('resize', function () {
        battlefield.engine.resize();
    });
    
    battlefield.onToggleMusic = function(isPlaying){
    	$('#soundlink').removeClass("not-active");
    	if(isPlaying){
    		$('#soundicon').removeClass("glyphicon-volume-off");
    		$('#soundicon').addClass("glyphicon-volume-up");
    	} else {
    		$('#soundicon').removeClass("glyphicon-volume-up");
    		$('#soundicon').addClass("glyphicon-volume-off");    		
    	}
    }
    
    
    function sync(){
        battlefield.sendSyncCommand();
        setTimeout(sync, SYNC_TIMEOUT_MILLIS);
    };
    
    connect = function(){
        $('#disconnectButton').attr("disabled", "false");
        $('#connectButton').attr("disabled", "disabled");
        console.log("connecting to the WebSocket controller");
        var socket = new SockJS(SERVER_CONTEXT_PATH + '/battlefield');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnectionEstablished, onConnectionError);
        battlefield.stompClient = stompClient;      
    };
    
    onConnectionEstablished = function(frame) {
        console.log("Connected to '/battlefield', frame = :", frame);
        stompClient.subscribe("/topic/battlefield", onMessageReceived);
        var username = frame.headers["user-name"];
        battlefield.changeUser(username);   
        setTimeout(sync, SYNC_TIMEOUT_MILLIS);
    };
    
    onConnectionError = function(error) {
    	console.log("Socket connection error: "+error);
    	window.location = SERVER_CONTEXT_PATH + "/logout";
    };
    
    onMessageReceived = function(message){
        console.log("received a message: ", message);
        battlefield.handleMessage(message);
    };
    
    disconnect = function () {
        $('#disconnectButton').attr("disabled", "disabled");
        $('#connectButton').attr("disabled", "false");
        if (!!stompClient) {
            stompClient.disconnect();
            battlefield.stop();
            stompClient = null;
        }
        console.log("Disconnected");
    };
    
    toggleMusic = function(){
    	battlefield.toggleMusic();
    }
    
})();