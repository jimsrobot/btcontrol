
/**************************************
*
* REFLOW OVEN BLUETOOTH CONTROLLER
*
****************************************
 
The reflow profile has a number of stages, these are 
built on the android device and the profile is sent to 
the board where it gets saved into the EEPROM

To run the profile you can press the button on the board
or you can send a start signal from the android device

The board sends the temperature out and various progress
messages.

********************************************************
* EEPROM Map
********************************************************

!! MAKE SURE YOU SEND STRINGS TO BOARD ALREADY PADDED OUT 
!! WITH LEADING ZEROS. YOUR ANDROID/BT SHOULD SUPPLY CORRECTLY 
!! FORMATTED STRINGS.

The profile is sent via BT serial and arrives as a string.
The string has the profile name, then the nuber of stages, 
then each stage (each stage is 10 bytes)

** First we get profile name and number of stages
** Following is where we store them in the EEPROM
0 to 19 bytes = name of profile (max 20 characters)
49-50 xx = number of stages (max 12)

** Each stage uses 10 Bytes
** Stage 1 is 51 to 60
51      x      stage type
52-54   xxx    temperature - 000 to 399
55-57   xxx    percent rate - 000 to 100
58-60   xxx    seconds - 000 to 999

61-70      stage 2
71-80      stage 3
81-90      stage 4
91-100     stage 5
101-110    stage 6
111-120    stage 7
121-130    stage 8
131-140    stage 9
141-150    stage 10
151-160    stage 11
161-170    stage 12

*********************************************************
* Output prefixes:
Output is prefixed byt a 2 character code so you know
what's coming out.

C: Temperature
P: Profile Name
S: Stage number that's starting
D: Profile complete
B: Beep
E: Profile saved to EEPROM

*********************************************************/

#include <SoftwareSerial.h>
#include <itoa.h>
#include <EEPROM.h>
#define DEF_BTBAUD 9600
#define MAX_TIME 2147483647

const byte buttonPin = 0;    // the number of the pushbutton pin
const byte rxPin = 4;
const byte txPin = 3;
const byte relayPin = 1;        // Digital 1
const byte thermoPin = 1;       // Analog 1 
//const byte serialDelay = 4;     // milliseconds delay to use - reduces serial buffer issues

// Variables will change:
byte     relayState = LOW;         // the current state of the output pin
byte     buttonState = LOW;        // the current reading from the input pin
byte     lastButtonState = LOW;    // the previous reading from the input pin
byte     inByte = 0;
int      counter = 0;
byte     inputStringMax = 145;

// the following variables are long's because the time, measured in miliseconds,
// will quickly become a bigger number than can be stored in an int.
unsigned long    lastDebounceTime = millis();  // the last time the output pin was toggled
long    debounceDelay = 20;    // the debounce time; increase if the output flickers
unsigned long    lastStatusSent = 0;    // when we last sent the status
int     statusInterval = 1500;  // millisecs between sending status

// We need to track where we are in the profile stages
int     stage_pos = 0;          // which stage are we in
byte    stage_type = 0;         // what type of stage? 1=pause,2=heat,3=beep
int     target_temp = 0;        // temperature we want to reach
unsigned long    stage_end_time = MAX_TIME;     // end time for this stage
byte    temp_reached = 0;       // have we completed the stage?

// Prepare the softwareserial
SoftwareSerial BTSerial(rxPin, txPin); // RX, TX

// the setup routine runs once when you press reset:
void setup() {                
  
  BTSerial.begin(9600);  
  BTSerial.listen();  

  pinMode(relayPin, OUTPUT);           // initialize the digital pins as an output or input.
  pinMode(buttonPin, INPUT);

  digitalWrite(relayPin, relayState);  
  lastStatusSent = millis();           //  reset the update interval
}



// the loop routine runs over and over again forever:
void loop() {         

  
  //Check if we need to send an update via BT
  if ((millis() - statusInterval) > lastStatusSent) {    
    if (lastStatusSent > 3000) {
      SendTemperature();
    }
    //BTSerial.print(F("sp:"));BTSerial.println(lastStatusSent);
    lastStatusSent = millis();
  }
  
  CheckButtonPress();    // start / stop reflow profile
  CheckSerialIn();       // check for uploads of a new profile
  
  // If stage_pos > 0 then we're in the middle of a reflow process.
  // A stage can complete on time or on temperature. E.g. we can finish
  // after 30 seconds or when we reach 150 degrees C.
  if (stage_pos > 0){
  //  BTSerial.println(F("xxx"));
    switch (stage_type){
      case 1:    // Pause for nnn seconds
        if ( stage_end_time < millis() ){
          GetNextStage();              // move to next stage
        }
        break;
        
      case 2:    // Heat to nnn degrees, and hold for nnn seconds
        // First we need to reach the required temperature and then
        // we can finish at the set time
        if (target_temp > GetTemperature() ){
          digitalWrite(relayPin, 1);  // Relay On
        }
        else
        { 
          digitalWrite(relayPin, 0);  // Relay off         
          temp_reached = 1;           // Temperature must be reached to stop
        }
        
        if ( (stage_end_time < millis()) && (temp_reached = 1) ){
          GetNextStage();              // move to next stage
        }
        
        break;
        
      case 3:    // return "BEEP"
        BTSerial.println(F("B:"));//delay(serialDelay);
        GetNextStage();              // move to next stage
        break;
    }        
  }
}

// GetNextStage will increment through all the stages
// Stage 0 (zero) will do nothing, so setting 
// GetNextStage = 0 will pause any reflow
int GetNextStage(){
  
  // If we're at zero we're just starting, send the stage name
  if ( stage_pos == 0 ) {
    
    char profName[20]; 
    for(byte i=0; i<20; i++)
    {
      // Get a character
      profName[i] = EEPROM.read(i); 
      if(profName[i] == '\0'){     
        i = 21;  //break   
      }
    }
    BTSerial.print(F("P:"));
    BTSerial.println(profName);
  }
  
  stage_pos++;
 
  
  if ( stage_pos > MaxStages() ) {
    // we've finished so reset everything and stop
    BTSerial.println(F("reset"));
    stage_pos = 0;
    stage_type = 0;         // what type of stage? 1=pause,2=heat,3=beep
    target_temp = 0;        // temperature we want to reach
    stage_end_time = MAX_TIME;     // end time for this stage
    BTSerial.println(F("D:")); // Profile done message
  }
  else
  {
    // We need to load all the settings for this stage and
    // get it started
    
       /** Each stage uses 10 Bytes
        ** Stage 1 is 51 to 60
        51      x      stage type
        52-54   xxx    temperature - 000 to 399
        55-57   xxx    percent rate - 000 to 100
        58-60   xxx    seconds - 000 to 999   */
   // BTSerial.println(F("get set"));
    stage_type = GetEEPROMSetting(0, 1);        // what type of stage? 1=pause,2=heat,3=beep
    target_temp = GetEEPROMSetting(1, 3);        // temperature we want to reach
    stage_end_time = millis() + (1000*GetEEPROMSetting(7, 3));     // end time for this stage
    
    // Send the stage number to output
    //char stage_out[5];     
    //itoa(stage_pos,stage_out, 10);  
    //stage_out[4] = '\0'; 
    BTSerial.print(F("S:"));
    //BTSerial.println(stage_out);
    BTSerial.println(stage_pos);  
  }  
}

// Get all the EEPROM settings
int GetEEPROMSetting(byte offset, byte len){
  int ret_val;
  byte eep_pos;
  char eep_val[4];
  
  // Stages start at 51 and are 10 bytes long
  eep_pos = (stage_pos * 10) + 41 + offset;
  
  eep_val[0] = EEPROM.read(eep_pos + 0);   
  eep_val[1] = '\0';
  eep_val[2] = '\0';
  
  if ( len == 3 ){
    eep_val[1] = EEPROM.read(eep_pos + 1); 
    eep_val[2] = EEPROM.read(eep_pos + 2 );   
    eep_val[3] = '\0';
  }
  
  ret_val = atoi(&eep_val[0]);  
  return ret_val;
}

int CheckSerialIn(){
  if (BTSerial.available() > 0)  {
    //We have serial data to grab 
    counter = 0;
    char strMsg[inputStringMax];
    while ((counter < inputStringMax) && (BTSerial.available() > 0)) {
      strMsg[counter] = BTSerial.read();              
      counter++;
    }
    
    strMsg[counter+1] = '\0';    // Null string terminator
         
    // Now that we have the serial data we need to process it
    // The string GO means to start the profile.
    if ((strMsg[0] == 'G') && (strMsg[1] == 'O')) {
      BTSerial.println(F("GO!"));
      stage_pos = 0;     // start at the beginning
      GetNextStage();    // triggers the next stage and starts profile
      //ReadFromEEPROM();
      //return 2;       
    }
    else  //It wasn't "GO" so it must (hopefully) be a profile to save
    {
      SaveProfileToEEPROM(strMsg, sizeof(strMsg));    
    }
  }   
}

void ReadFromEEPROM()
{
  char profName[20];
  
  for(byte i=0; i<20; i++)
  {
   // Get a character
   profName[i] = EEPROM.read(i);   
   if(profName[i] == '\0')
      break;  
  }
  
  // BTSerial.println(profName);
  
}


void SaveProfileToEEPROM(char *ProfileInfo, int bufsize){
  int i = -2;   // Count through the profile bits, start at -2 for ease of positioning eeprom
  int j = 0;    // loop variable
 
  int eeprom_pos = 0;  
  char *str;

  while ((str = strtok_r(ProfileInfo, "[", &ProfileInfo)) != NULL) 
    {
      if (i == -2){
       
        while (j<20 && str[j] != '\0'){
          EEPROM.write(j, str[j]);
          j++;    
        }
        EEPROM.write(j, '\0');

        i++; // Move on to the next delimited string
        
        // Send a message that the EEPROM loaded
        BTSerial.print("E:"); //SerialDelay();
        BTSerial.println(str);  //SerialDelay();
      }
      
      else if (i == -1){
        // this is the number of stages to save - needs to arrive
        // already padded to 2 digits with a leading zero if needed
       // BTSerial.println("h11");delay(serialDelay);
        EEPROM.write(49, str[0]);
        EEPROM.write(50, str[1]);        
        i++; // Move on to the next part
      }
      
      else
      {
        // this is a stage - 10 bytes of info
     
        eeprom_pos = (i * 10) + 51;    // Starting eeprom position
        for (int k = 0; k < 10; k++){
          EEPROM.write(eeprom_pos + k, str[k]);
        }
        i++;       
      }           
    }
}


void CheckButtonPress(){
 
   // read the state of the switch into a local variable:
  byte reading = digitalRead(buttonPin);
  
    // If the switch changed, due to noise or pressing:
  if (reading != lastButtonState) {
    // reset the debouncing timer
    lastDebounceTime = millis();
  }
  
  if ((millis() - lastDebounceTime) > debounceDelay) {
    // whatever the reading is at, it's been there for longer
    // than the debounce delay, so take it as the actual current state:

    // if the button state has changed:
    if (reading != buttonState) {
      buttonState = reading;
      
      // stage_pos is zero when we're not in a stage and 
      // we are idle
      if ( stage_pos == 0 ) {
        stage_pos = 0;     // start at the beginning
        GetNextStage();    // triggers the next stage and starts profile
      }
      else
      {
        stage_pos = 0;     // stops any stages. 
        digitalWrite(relayPin, 0);
      }      
    }
    
    // set the relay:
    digitalWrite(relayPin, relayState);
  }  

  // save the reading.  Next time through the loop,
  // it'll be the lastButtonState:
  lastButtonState = reading; 
  
}

void SendTemperature(){
    
  //Messaging variables
//  char strOutCE[7]="C: ";
  char strOutTemperature[5];
  
  itoa(GetTemperature(),strOutTemperature, 10);  

 // for(byte i=0; i<4; i++)
 // {
   // Get a character
  // strOutCE[i+2] = strOutTemperature[i];      
 // }
 // strOutCE[7] = '\0';
  
  BTSerial.print(F("C:"));
  BTSerial.println(strOutTemperature);
 // BTSerial.println();
}

int GetTemperature(){
  // Get the temperature reading  
  // Each increment = 3.3 / 1024 = 0.00322265625v
  int temperature = (analogRead(thermoPin) * 3.3 / 5);
  
  // A bit of safety - cut off relay at 300 C
  if ( temperature > 300 ){
    digitalWrite(relayPin, 0);
  }  
  return temperature;
}


// The number of stages are written to EEPROM 49 and 50
int MaxStages(){
  int num_stages = 0;
  char stage_count[3] = "00";
  stage_count[0] = EEPROM.read(49); 
  stage_count[1] = EEPROM.read(50);
  
  num_stages = atoi(&stage_count[0]);  
 // BTSerial.print("ns:");
 // BTSerial.println(num_stages);
  return num_stages;
}




/*
void SerialDelay(){
  long delay_time = millis() + 200; // millisecs
  byte nothing = 0;
  while (millis() < delay_time){nothing = 0;}
  //BTSerial.println("waiting");
}

*/
 
// ATMEL ATTINYX5 / ARDUINO
//
//                           +-\/-+
//  Ain0       (D  5)  PB5  1|    |8   VCC
//  Ain3       (D  3)  PB3  2|    |7   PB2  (D  2)  INT0  Ain1
//  Ain2       (D  4)  PB4  3|    |6   PB1  (D  1)        pwm1
//                     GND  4|    |5   PB0  (D  0)        pwm0
//                           +----+


