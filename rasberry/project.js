var express = require('express')
var app = express();
var bodyParser = require('body-parser');
require('date-utils');
var server = require('http').createServer(app);
var io = require('socket.io')(server);
var Gpio = require('onoff').Gpio,
	left = new Gpio(17,'out'),
	right = new Gpio(18,'out');

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
//		console.log('Start the server using the port 3000');

		});

io.on('connection', function(client)
		{
		//console.log("connect success");
		
		client.on("connect",function(data)
				{
//				console.log("connected : "+ data.connect);
				}
			 );

		client.on("control",function(data)
				{

				//OK
				//console.log("data from client : "+data.command);
				if(data.command == "left")
				{
					left.writeSync(1);
					left.writeSync(0);
				}
				else if( data.command == "right")
				{
					right.writeSync(1);
					right.writeSync(0);

				}
		//		client.emit("response",{"hello":"hi"});

				});


		client.on("disconnect",function()
				{
//				console.log("disconnected");
				});

		});


//port config
var port = new SerialPort('/dev/ttyACM0',{
	baudRate: 115200
});
	sensorVal = 0;



port.on('open',function()
	{
//	console.log('serial port OPEN');

	});

port.on('error', function(err)
	{
//	console.log('Error: ', err.message);
	});


//read data from Arduino
const Readline = SerialPort.parsers.Readline;
const parser = port.pipe(new Readline({ delimiter: '\n'  }));
parser.on('data', test);

var temp;

function test(data)
{
//	console.log(data);


	var sensordata = String(data).split(',');
	/*
	for( var i =0; i<sensordata.length; i++)
	{
		console.log(sensordata[i]);
	
	}*/
	var newDate = new Date();
	var time = newDate.toFormat('YYYY-MM-DD HH24:MI:SS');

	temp = {temperature:sensordata[0] ,
		    humidity : sensordata[1],
		    ppm : sensordata[2] ,
		    flameState : sensordata[3],
		    humState : sensordata[4],
		    inputtime : time
			   };

	// data input 

/*			
	var insert = connection.query('insert into sensordata set ?',temp,function(error,results,fields)
			{
				if(error) 
				{
					console.log(error.code);
					console.log(error.fatal);
				}
				//console.log("insert completed!, row num :" +					results.insertId);
				
				
			//connection.end();
			
			});
	
*/
	
}
