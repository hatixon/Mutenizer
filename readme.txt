#ALL WORDS ARE IN THEIR RESPECTIVE .YML FILES
#FORMATTING CAN BE FOUND AT dev.bukkit.org/server-mods/mutenizer/pages/main/formatting

commandsList:
#Commands here are listened to for swearing.
#Command can only be one word.

Check:
#When true, signs and/or commands will be listened to for swearing
  Commands: true
  Signs: true
  
#If a sign is found with blacklist/instant ban words on it, the sign is broken.
BreakSignsOnSwear: true

#Different types of punishment are: damage(damage the player for the amount specified in Damage:), lightning(Strikes lightning on the player), warnings(uses mutenizers warning and ban system)
#Special feature is that you can have all 3 at the same time
#Warning on the lightning. It hurts other players and can be seen as a way of "trolling" other players.
PunishmentType: warnings

#Amount of damage dealt(max 20)(1 = 1/2 heart)
Damage: 6

#Determines the number of remaining warnings left before a player is kicked for each infraction.
RemainingWarningsBeforeKick: 3

#Sets the number of warnings a player is given.
TotalWarnings: 6

#Determines who is notified
Notify:
  Player: true
  Op: true
  
#When true, no matter what punishment type, the specified amount of money will be taken away(Requires Essentials for now)
Money:
  Penalty: 100
  Enabled: false
  
#Determines if a players warnings are reset when they are banned(leave true for hassle free unbanning)
ResetWarningsOnBan: true

#Determines if caps are censored, result in punishment, and what percentage of capitals are allowed in any one message
Caps:
  Percentage: 50
  #If true, then caps filter will be enabled
  Enabled: true
  #If true caps result in punishment
  Enforced: true
  
#Messages for different scenarios
Message:
  FirstJoin: You have been given the default number of warnings!
  Swear:  
    Warning: You are not allowed to say that!
    Kick: You have been kicked for swearing!
    Ban: You have been banned for repeated swearing!
  InstaBan: You have been instantly banned for using a word on the instant ban list!
  Caps:
    Warn: You have used too many capitals!
    Kick: You were kicked for using too many capitals!
    Ban: You were banned for spamming capitals!
    
#Command to be executed when a player is banned. If you are using a tempban command just put it as "<command> <time>" It won't matter if time is set as '10d' or '10 d' as long as it suits your banning plugin
BanCommands:
 Ban: ban
 InstaBan: ban
 CapsBan: ban