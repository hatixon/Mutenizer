Blacklist:
#Blacklisted words go here.
#Formatting is on the forums, or search up java regex
Whitelist:
#Whitelisted words go here. Formatting is the same as blacklisted words. These words are the top. They are allowed no matter what.
InstaBanList:
#Words resulting in an instant ban go here. These words do not take precedence over the whitelist.
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
  #If caps are censored
  Enabled: true
  #If caps result in punishment
  Enforced: true
#Messages for each scenario
Message:
  FirstJoin: You have been given the default number of warnings!
  Swear:  
    Warning: You are not allowed to say that!
    Kick: You have been kicked for swearing!
    Ban: You have been banned for repeated swearing!
  InstaBan: You have been instantly banned for using a word on the instant ban list!
  CapsM:
    Warn: You have used too many capitals!
    Kick: You were kicked for using too many capitals!
    Ban: You were banned for spamming capitals!
#Command to be executed when a player is banned. If you are using a tempban command just put it as "<command> <time>" It won't matter if time is set as '10d' or '10 d' as long as it suits your banning plugin
BanCommand: ban
#Command to be executed when a player uses an instant ban word. Same rules apply as the regular command
InstaBanCommand: ban
#List of users warnings
Warnings:
  Warned:
#List of users(please do not touch as this is part of the /mutenizer resetall command. If you would like to remove someone from that command then remove their name from this list.
UserList:
