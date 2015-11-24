grammar HueCommand;

@header {
 package com.ryonday.automation.hue.antlr;
}

command : directive ;

directive : nameOnlyDirective LIGHTNAME
          | argumentedDirective
          ;

nameOnlyDirective : turnOff
         | turnOn
         | brighten
         | darken
         | saturate
         | desaturate
         | cycle
         ;

argumentedDirective: changeColor LIGHTNAME colorspec;

changeColor : '!color' LIGHTNAME colorspec ;
turnOn: '!on';
turnOff : '!off' ;
brighten : '!brighter' ;
darken : '!darker' ;
saturate : '!saturate' ;
desaturate : '!desaturate' ;
cycle : '!cycle' ;

colorspec : rgbcolor
          | HEXCOLOR
          | COLORNAME
          ;

rgbcolor: RED GREEN BLUE;

RED: INT;
GREEN: INT;
BLUE: INT;

HEXCOLOR: '#'(HEXNUM);
HEXNUM: [0-9a-f]+;
INT: [0-9]+;
COLORNAME: [a-z]+;
LIGHTNAME : [a-z0-9]+ ;
WS : [ \t\n\r]+ -> skip;
