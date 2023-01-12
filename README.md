# SimplePMs

This plugin allows players to send private messages to one another, and reply to previous messages. 
This plugin also gives staff members the ability to toggle 'socialspy', letting them view these messages

 ### Commands

- `/msg` (or `/dm`, `/m`, `/pm`)
   <br>Allows the user to send a private message to another player. 
   <br>**Usage**: /msg <recipient\> <message\>. 
   <br>This command requires the `spm.send` permission.

-  `/reply` (or `/r`, `/re`): 
   <br> Allows the user to reply to a previously received private message. 
   <br> **Usage:** /r <message\>. 
   <br> This command also requires the spm.send permission.

- `/socialspy` (or `/ss`, `/spy`, `/sspy`):
   <br> Toggles socialspy for the user. 
   <br> Allows staff members to read other people's private messages. 
   <br> This command requires the `spm.socialspy.toggle` permission.

- `/spmreload`: 
   <br> Reloads the SimplePM configuration. 
   <br> This command requires the `spm.reload` permission.

 ### Permissions

- `spm.socialspy.toggle`: 
   <br> Allows a user to read other people's private messages. 
   <br> Default: `op`
- `spm.reload`: 
   <br> Allows the player to reload the configuration. 
   <br> Default: `op`
- `spm.send`: 
   <br> Allows a player to send direct messages with /msg and /r. 
   <br> Default: `true`
