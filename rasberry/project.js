var express = require('express')
var app = express();
var bodyParser = require('body-parser');
require('date-utils');
var server = require('http').createServer(app);
var io = require('socket.io')(server);

app.use(bodyParser.urlencoded({extended:true}))

var mysql = require('mysql');
var connection = mysql.createConnection(
		{
host : 'localhost',
user : '#',
password :'#',
database : 'test'
});

connection.connect();
var SerialPort = require('serialport');

//server open
server.listen(3000, () =>
		{
		console.log('Start the server using the port 3000');
		});

io.on('connection', function(client)
		{
		console.log("connect success");

		client.on("connect",function(data)
				{
				console.log("connected : "+ data.connect);
				}
			 );

		client.on("event",function(data)
				{
				console.log("data from client : "+data.key1+"," + data.key2);

				var obj = {"hello":"client"};
				console.log(obj);
				client.emit("response",obj);

				});


		client.on("disconnect",function()
				{
				console.log("disconnected");
				});

		});


//port config
var port = new SerialPort('/dev/ttyACM0',{
	baudRate: 115200
});
	sensorVal = 0;



port.on('open',function()
	{
	console.log('serial port OPEN');

	});

port.on('error', function(err)
	{
	console.log('Error: ', err.message);
	});


//read data from Arduino
const Readline = SerialPort.parsers.Readline;
const parser = port.pipe(new Readline({ delimiter: '\n'  }));
parser.on('data', test);

function test(data)
{
	console.log(data);
	console.log("success ??");
	console.log(typeof(data));

	var sensordata = String(data).split(',');
	/*
	for( var i =0; i<sensordata.length; i++)
	{
		console.log(sensordata[i]);
	
	}*/
	var newDate = new Date();
	var time = newDate.toFormat('YYYY-MM-DD HH24:MI:SS');

	var temp = {temperature:sensordata[0] ,
		    humidity : sensordata[1],
		    ppm : sensordata[2] ,
		    flameState : sensordata[3],
		    humState : sensordata[4],
		    inputtime : time
			   };


	var insert = connection.query('insert into sensordata set ?',temp,function(error,results,fields)
			{
			if(error) throw error;
				console.log(results.insertId);
			
			//connection.end();

			});
	console.log("insert completed!");
}
