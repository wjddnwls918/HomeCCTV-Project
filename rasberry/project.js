var express = require('express')

var app = express();
var bodyParser = require('body-parser');

app.use(bodyParser.urlencoded({extended:true}))

var mysql = require('mysql');
var connection = mysql.createConnection(
		{
host : 'localhost',
user : '#',
password :'#',
database : '#'
});

connection.connect();

/*
var query = connection.query('select * from `user`',function(err,result)
		{
			console.log(result);
			conncetion.end();
			})
*/

var SerialPort = require('serialport');


app.get('/',function(req,res)
		{
		
		res.send("Hello, World")
		})
app.listen(8888,function()
		{
		console.log("Server starting with 8888")
		})



//SerialPort.read(30);
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
/*
port.on('data',function(data)
	{
	//console.log(data.temp +", "+data.humi+", "+data.button);\
	console.log(data.length);
	if(data.length >= 21 )
	{
		console.log('Read Data: '+data);
		
		var insert = connection.query('insert into sensordata set ?'
				,sensordata,function(error,results,fields)
				{
				if(error) throw error;
				console.log(results.insertId);
				
			//	connection.end();
				
				});

	}

	}
);
*/

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

	for( var i =0; i<sensordata.length; i++)
	{
		console.log(sensordata[i]);
	
	}
}
