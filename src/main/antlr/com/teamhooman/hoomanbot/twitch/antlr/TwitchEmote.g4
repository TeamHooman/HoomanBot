//
// Twitch sends emote information about a TMI message in an IRCv3 tag "emotes". The format is below:
//
// EmoteId:beginIndex-endIndex,beginIndex,endIndex/EmoteId...
//
// ex: 47816:0-6,8-14/44751:16-23,25-32/44757:34-42/44579:44-51/37365:53-62,64-73/44577:75-81/38117:83-94,96-107
//
grammar TwitchEmote;

@header {
 package com.ryonday.automation.twitch.antlr;
}



emoteString : emote('/'emote)*;
emote: emoteid':'indices(','indices)*;
indices: startindex'-'endindex;
emoteid: INT;
startindex: INT;
endindex: INT;

INT: [0-9]+;

//emoteString : emote('/'emote)* ;
//emote : EMOTEID':'indices(','indices)* ;
//indices : STARTINDEX'-'ENDINDEX ;
//
//
//EMOTEID : INT;
//STARTINDEX: INT;
//ENDINDEX: INT;
//
//INT: [0-9]+;

