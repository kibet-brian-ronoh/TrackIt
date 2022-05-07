#include <DFRobot_sim808.h>
#include <SoftwareSerial.h>

#define MESSAGE_LENGTH 150
char ask_msg[5] = "GPS"; //Sender should send this
char message[MESSAGE_LENGTH]; // Message received
char loc_msg[150]; ////The content of message to be sent
int messageIndex = 0; //to count unread messages
char url[200];

char lat[12]; //latitude
char lon[12]; //longitude
char wspeed[12]; //speed of vehicle

char phone[16]; //Phone number requesting for location
char datetime[24]; //Date and time requested

#define PIN_TX    11 //The TX pin of SIM808 is connected to pin 11 of Arduino
#define PIN_RX    10 //The RX pin of SIM808 is connected to pin 10 of Arduino

SoftwareSerial mySerial(PIN_TX,PIN_RX); //Open a serial port to communicate with Arduino
DFRobot_SIM808 sim808(&mySerial);//Connect RX,TX,PWR,

void setup() {
  mySerial.begin(9600);  //Begin serial communication with SIM808 at 9600 baud rate
  Serial.begin(9600); //Begin serial communication with the serial monitor at 9600 baud rate

  //******** Initialize sim808 module *************
  while(!sim808.init()) {
      Serial.print("Sim808 init error\r\n");
      delay(1000);
  }
  delay(3000);
  //*********** Attempt DHCP *******************
  //Parameters are APN, USERNEME and Password to join the network
  /*while(!sim808.join(F("safaricom"), F("saf"), F("data"))) {
      Serial.println("Sim808 join network error");
      delay(1000);
  }*/

  //************ Successful DHCP ****************
  Serial.print("IP Address is ");
  Serial.println(sim808.getIPAddress());  
  Serial.println("Initialization Success, please send SMS message to me!");
}

void loop() {
  getGPS();
  
  //*********** Detecting unread SMS ************************
   messageIndex = sim808.isSMSunread();
    Serial.print("messageIndex: ");
    Serial.println(messageIndex);

   //*********** At least, there is one UNREAD SMS ***********
   if (messageIndex > 0) { 
      sim808.readSMS(messageIndex, message, MESSAGE_LENGTH, phone, datetime);
      delay(2000);
                 
      //***********In order not to full SIM Memory, is better to delete it**********
      sim808.deleteSMS(messageIndex);
      Serial.print("From number: ");
      Serial.println(phone);  
      Serial.print("Datetime: ");
      Serial.println(datetime);        
      Serial.print("Received Message: ");
      Serial.println(message);

      String aa = String(ask_msg); 
      String bb = String(message);

      if(aa.equalsIgnoreCase(bb)){
        //******** define phone number and text **********
        sprintf(loc_msg, "Latitude : %s\nLongitude : %s\nSpeed : %s kph\nGoogle Maps Link: \nhttp://maps.google.com/maps?q=%s,%s", lat, lon, wspeed, lat, lon);
        sim808.sendSMS(phone,loc_msg);
      }
      else{
        Serial.println("Not a location request!");
      }
   }
   sendtoServer();
   delay(1000);
}

void sendtoServer(){
  //sprintf(url, "GET /storeLocation.php?trackingUnit=5&latitude=%s&longitude=%s&speed=%s HTTP/1.1\r\nHost:kiptrack.000webhostapp.com\r\n\r\n", lat, lon, wspeed);
  sprintf(url, "POST /storeLocation.php HTTP/1.1\r\nHost:kiptrack.000webhostapp.com\r\ntrackingUnit=5&latitude=%s&longitude=%s&speed=%s\r\n\r\n", lat, lon, wspeed);
  char buffer[512];
  
  while(!sim808.connect(TCP,"http://kiptrack.000webhostapp.com", 80)) {
      Serial.println("Connect error");
      delay(2000);
  }
  Serial.println("Connect Kiptrack success");

  //*********** Send a GET request *****************
  Serial.println("waiting to fetch...");
  sim808.send(url, sizeof(url)-1);
  while (true) {
    int ret = sim808.recv(buffer, sizeof(buffer)-1);
    if (ret <= 0){
        Serial.println("fetch over...");
        break; 
    }
    buffer[ret] = '\0';
    Serial.print("Recv: ");
    Serial.print(ret);
    Serial.print(" bytes: ");
    Serial.println(buffer);
    break;
  }
  //************* Close TCP or UDP connections **********
  //sim808.close();
  //*** Disconnect wireless connection, Close Moving Scene *******
  //sim808.disconnect();
}

void getGPS(){   
  
  while(!sim808.getGPS())
  {
    
  }

  float la = sim808.GPSdata.lat;
  float lo = sim808.GPSdata.lon;
  float ws = sim808.GPSdata.speed_kph;

  dtostrf(la, 4, 6, lat); //put float value of la into char array of lat. 4 = number of digits before decimal sign. 6 = number of digits after the decimal sign.
  dtostrf(lo, 4, 6, lon); //put float value of lo into char array of lon
  dtostrf(ws, 6, 2, wspeed);  //put float value of ws into char array of wspeed

  Serial.print("\n");
  Serial.println("GPS acquired");
  //sprintf(url, "GET /storeLocation.php?trackingUnit=5&latitude=%s&longitude=%s&speed=%s HTTP/1.1\r\nHost:kiptrack.000webhostapp.com\r\n\r\n", lat, lon, wspeed);
}
