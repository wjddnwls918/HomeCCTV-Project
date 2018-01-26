//#include <DHT11.h>
#include <DHT.h>
#include <WiFi.h>
#include "MQ135.h"
#include <Servo.h>
#include <ArduinoJson.h>
#define DHTPIN 6
#define DHTTYPE DHT22
#define BUZZER 10
#define RLOAD 10.0

#define RZERO 76.63

#define PARA 116.6020682
#define PARB 2.769034858

#define ANALOGPIN A0
//#define RZERO 206.85
MQ135 gasSensor = MQ135(ANALOGPIN);

const int beepFrequency = 900;
const int beepDuration = 500;
Servo servo;

int flag = 0;
int sw=2;
int flame = 4;
int state = 0;
int led = 7;
int humSensor = 5;
int angle = 0;

DHT dht(DHTPIN,DHTTYPE);
int tones[] = {261,294,330,349,392,440,494,523};

//DHT11 dht11(temhu);

void setup()
{
  
  dht.begin();
  pinMode(sw,INPUT_PULLUP);
  pinMode(DHTPIN,INPUT);
  pinMode(flame, INPUT);
  pinMode(led , OUTPUT);
  pinMode(humSensor, INPUT);
  attachInterrupt(0,left,RISING);
  attachInterrupt(1,right,RISING);
  servo.attach(8);
  angle = 90;
  servo.write(angle);
  //float rzero = gasSensor.getRZero();
  //delay(3000);
  //Serial.print("MQ135 RZERO Calibration Value : ");
  //Serial.println(rzero);
  Serial.begin(115200);
}

void loop()
{
  state = digitalRead(flame);
  
  digitalWrite(led,LOW);
  
  if (state ==0)
  {
    //Serial.println("ON");
    digitalWrite(led,HIGH);
    delay(100);
  }
  else
  {
    //Serial.println("OFF");
    digitalWrite(led, LOW);
  }
  
  //delay(100);
  
  int humState = digitalRead(humSensor);
  //Serial.println(humstate);
  if(humState == HIGH)
  {
    digitalWrite(led,HIGH);
    delay(500);
    digitalWrite(led,LOW);
    delay(500);
  }
    
  
  
  int buttonState = digitalRead(sw);
  float h = dht.readHumidity();
  float t = dht.readTemperature();
 
 
  float correctedRZero = gasSensor.getCorrectedRZero(t,h);
  float resistance = gasSensor.getResistance();
  float ppm = gasSensor.getPPM();
  float correctedPPM = gasSensor.getCorrectedPPM(t,h);
 
  // Serial.print("ppm : ");
  // Serial.println(ppm *1000);
 
  
 
  //  Serial.print(t);
  //  Serial.print(" ");
    //delay(1000);
  //  Serial.println(h);
    //Serial.print(" ");
    //delay(1000);

  buzzerAct(buttonState);  
  
  Serial.print(t);
  Serial.print(',');
  Serial.print(h);
  Serial.print(',');
  Serial.print(ppm*1000);
  Serial.print(',');
  //flame sensor
  Serial.print(state);
  Serial.print(',');
  Serial.print(humState);
  Serial.print('\n');
  
  /*
   String jsondata ="";
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject &root = jsonBuffer.createObject();
  root["temp"] = t;
  root["humi"] = h;
  root["ppm"] = ppm *1000;
  root["flameState"] = state;
  root["humState"] = humState;
  root.printTo(jsondata);
  Serial.println(jsondata);
 */
  
  delay(3000);
}
  
void buzzerAct(int buttonState)
{
  if( buttonState == 0)
  {
    tone(BUZZER, beepFrequency, beepDuration);
  }
}


void left()
{
 //Serial.print("left!!"); 
  
  angle -=10;
  if(angle <0)
  angle = 0;
  servo.write(angle);
}

void right()
{
  //Serial.print("right!!");
  
  angle +=10;
  if(angle >180)
  angle =180;
  servo.write(angle);
  
}
  
  
  
