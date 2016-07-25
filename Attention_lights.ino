#include<SoftwareSerial.h>

SoftwareSerial esp(5,6);   //RX TX 
String port="5500";

int ledPin=13;
int rpin=9;
int bpin = 11;



void setup(){ 
 esp.begin(115200);
 Serial.begin(115200);
 Serial.println("Connecting...");

//pinmode
pinMode(ledPin,OUTPUT);


//pinmode

 
//ESP STATUS 
 int state=0;
 while(!state){
    esp.println("AT");
   
    delay(100);
    String a = esp.readString();
    Serial.println(a); 
    if(a.indexOf("OK")>-1){
      state=1;
      Serial.println("Ready!"); 
    }
    delay(100);
 }

// CONNECTION STATUS
state=0;
while(!state){
    esp.println("AT+CIFSR");
    
    delay(100);
    String a = esp.readString();
    delay(100);
    esp.println("AT+CWJAP?");
    delay(100);
    a+="\n"+esp.readString();
     if(a.indexOf("OK")>-1){
      state=1;
      Serial.println("Connected! "+a); 
    }
    delay(100);

}

//CIPMUX
state=0;
while(!state){
    esp.println("AT+CIPMUX=1");
    delay(100);
    String a = esp.readString(); 
     if(a.indexOf("OK")>-1){
      state=1;
      Serial.println("CIPMUX SUCCESS "+a); 
    }
    else
       Serial.println("CIPMUX FAILED "+a); 
       delay(100);
}
//start server
 state=0;
 while(!state){
    esp.println("AT+CIPSERVER=1,"+port);
    delay(100);
    String a = esp.readString(); 
    if(a.indexOf("OK")>-1){
      state=1;
      Serial.println("SERVER STARTED AT "+port); 
    }
    else
       Serial.println("AT+CIPSERVER=1,"+port); 
    delay(100);
    digitalWrite(13,HIGH);
 }
  

}
void loop(){
     String a="";
     while((a=esp.readString()).equals(""));
     Serial.println(a);
     if(a.indexOf("RN")>-1)  {
      on(rpin);
     }
     if(a.indexOf("RF")>-1){
      off(rpin);
     }
     delay(10);
}

void on(int lPin){
     for(int i=10;i<255;i++){
        analogWrite(lPin,i);
        delay(10);
     }
     digitalWrite(lPin,HIGH); 
}

void off(int lPin){
     
     for(int i=255;i>-1;i--){
        analogWrite(lPin,i);
        delay(1);
     }
      
}





