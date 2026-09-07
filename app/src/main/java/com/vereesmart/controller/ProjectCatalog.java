package com.vereesmart.controller;

public final class ProjectCatalog {
  public static final String[][] NAMES = {
    {"Push and Glow Board","LED Pattern Board","Sound Alert Box","Colour Light Selector","Traffic Light Model","Rain Alert System"},
    {"Smart Night Lamp","Motion Alert System","Temperature Indicator","Digital Counter Model","Password Entry System","Touch Music Panel"},
    {"Smart Room Monitor","Distance Measurement System","Water Level Indicator","Automated Gate System","Smart Switch","Sensor Status Dashboard"},
    {"Servo Controlled Gate Model","Smart Parking Indicator System","Event-Based Intrusion Recorder","Smart Energy Saving System","Magnetic Door Status Monitor","Laser Tripwire Security System"}
  };
  public static final String[][][] STEPS = {
    {{"Connect Push Button to IN2","Use LEDs L1-L3","Power ON system","Press button -> LEDs glow"},{"Connect Push Button to IN2","Use LEDs L1-L6","Power ON system","Press -> change pattern"},{"Connect Touch Sensor to IN2","Connect Buzzer to OUT1","Use LED L1","Touch -> alert"},{"Connect Touch Sensor to IN2","Use RGB LED L1-L3","Power ON system","Touch -> change colour"},{"Use LEDs L1-L6","Power ON system","Observe sequence"},{"Connect Water Sensor to IN4","Connect Buzzer to OUT1","Power ON system","Water detected -> alert"}},
    {{"Connect LDR to IN4","Use LED L1","Power ON system","Dark -> LED ON"},{"Connect PIR to IN2","Connect Buzzer to OUT1","Power ON system","Motion -> alert"},{"Connect Temp Sensor to IN4","Use LEDs L1-L3","Power ON system","Temp -> LED levels"},{"Connect Button to IN2","Connect 7-Segment to OUT2","Power ON system","Press -> count"},{"Connect Keypad to inputs","Connect Servo to OUT1","Power ON system","Enter password -> unlock"},{"Connect Touch Sensors to IN2/IN3","Connect Buzzer to OUT1","Power ON system","Touch -> tones"}},
    {{"Connect Temp Sensor to IN4","Connect OLED to OUT2","Power ON system","Display temperature"},{"Connect Ultrasonic to IN1","Connect OLED to OUT2","Power ON system","Display distance"},{"Connect Water Sensor to IN4","Use LEDs L1-L3","Power ON system","Show water level"},{"Connect IR Sensors to IN2/IN3","Connect Servo to OUT1","Power ON system","Auto gate control"},{"Connect Touch Sensor to IN2","Connect Relay to OUT1","Power ON system","Touch -> ON/OFF"},{"Connect Temp + Ultrasonic","Connect OLED to OUT2","Power ON system","Display all data"}},
    {{"Connect Keypad to inputs","Connect Servo to OUT1","Connect OLED to OUT2","Connect Buzzer to OUT1","Enter password -> unlock","Wrong -> alert"},{"Connect Ultrasonic to IN1","Use LEDs L1-L6","Power ON system","Distance -> indication"},{"Connect PIR Sensor to IN2","Connect OLED to OUT2","Connect Buzzer to OUT1","Detect motion","Count events","Display count"},{"Connect PIR to IN2","Connect LDR to IN3","Connect Relay to OUT1","Detect light + motion","Apply AND logic","Control output"},{"Connect Reed Switch to IN2","Connect Buzzer to OUT1","Use LED L1","Door open -> alert"},{"Connect Laser to IN2","Connect LDR to IN4","Connect Buzzer to OUT1","Use LED L1","Beam break -> alert"}}
  };
}
