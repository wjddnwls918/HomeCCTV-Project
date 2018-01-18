
var app = require('express')();
var server = require('http').createServer(app);
var io = require('socket.io')(server);

server.listen(3000, () =>
		{
		console.log ('Start the server using the port 3000');
		});

io.on('connection', function(client)
		{

		console.log("connected succes");
		//client.emit('hello', {hello:'hello'});

		client.on("connect",function(data)
				{
					console.log("connected : " +data.connect);
				}
			 );
		client.on("event",function(data){
				
				console.log("data from client :"+data.key1+"," + data.key2);

				var obj = {"hello":"client"};
				console.log(obj);

				
				client.emit("response",obj);							

				});

		client.on('disconnect',function()
				{

				console.log("disconnected");
				});

		});


