var express = require('express')
var app = express();
var bodyParser = require('body-parser');
require('date-utils');
var server = require('http').createServer(app);
var io = require('socket.io')(server);
var Gpio = require('onoff').Gpio,
	left = new Gpio(17,'out'),
	right = new Gpio(18,'out');
var FCM = require('fcm-node');
var serverKey = '#';

var fcm = new FCM(serverKey);



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
var selectresult = "";
var hstateresult = "";
var sensordata ;

function test(data)
{
//	console.log(data);

	var select = connection.query('select * from fcm',function(error,results){
			if(error){
			console.error(error);
			}

			selectresult = results;
//			console.log(selectresult[0].token);

			});



	sensordata = String(data).split(',');

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


	//check flame
	if(sensordata[3] == 0)
	{
		
		for( var i = 0; i< selectresult.length; i++)
		{
	//	console.log(selectresult[i].token);
		var push_data = {
			to : selectresult[i].token,
			     notification:
			     {
				title:"Alert flame",
				      body:"Flame detected, check your HomeCCTV applicaiton",
				      sound:"default",
				      click_action: "FCM_PLUGIN_ACTIVITY",
				      icon:"fcm_push_icon"
			     },

				priority:"high",
				restricted_package_name: "cobong.jeongwoojin.homecctv.myhomecctv"
		};
		fcm.send(push_data, function(err,response){
				
				if(err)
				{
				console.error("Push is failed");
			console.error(err);
				}
				
				//console.log('Push메시지가 발송되었습니다.');
				//console.log(response);

				});
		}


	}

	//under moderate tem
	if( sensordata[0] < 10)
	{
		for(var i = 0; i < selectresult.length; i++)
		{
		var undertem = 
		{
		to : selectresult[i].token,
		notification:
		{
			title: "Your Homes temperature is under moderate",
			body : "check your Home",
			sound:"default",
			click_action: "FCM_PLUGIN_ACTIVITY",
			icon:"fcm_push_icon"
		},

		priority : "high",
		restricted_package_name : "cobong.jeongwoojin.homecctv.myhomecctv"
		};
		
		fcm.send(undertem,function(err,response)
				{
				if(err)
				{
				console.error("Push is failed");
				console.error(err);
				}

				});

		}
	}

// humdetect
	var hstateselect = connection.query('select * from hdetect order by idx desc',function(error,results)
			{
				if(error)
				{
				console.error(error);
				}
				else{
				hstateresult = results;
				//console.log(hstateresult[0].hstate);
				//console.log(sensordata[4]);

	if(sensordata[4] == 1 && hstateresult[0].hstate == 1)
	{
		for( var i = 0; i < selectresult.length; i++)
		{
		var halarm = {
			to : selectresult[i].token,
			     notification:
				{
				title:"human detected"
				,body:"human detected, check your CCTV",
				sound:"default",
				click_action:"FCM_PLUGIN_ACTIVITY",
				icon:"fcm_push_icon"
				},
				priority:"high",
				restricted_package_name: "cobong.jeongwoojin.homecctv.myhomecctv"

		};
		fcm.send(halarm, function(err,response)
			{
				if(err)
				{
				console.error("Push is failed");
				console.error(err);
				}
			});
		}
	}
				}

			});

//data input 

	var insert = connection.query('insert into sensordata set ?',temp,function(error,results,fields)
			{
			if(error)
			{

			}
			}); //end of insert
			
	
}//end of file
