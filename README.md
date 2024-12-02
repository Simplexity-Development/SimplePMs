# SimplePMs
## Releases available on [Modrinth](https://modrinth.com/plugin/simplepms)
A simple plugin that allows for players to send private messages to one another, and reply to the last person who they messaged or received a message from.
<br>This plugin also has 'social spy' which allows for moderation of these messages.

### Commands

- `/msg` (or `dm`, `m`, `pm`, `tell`, `t`)
   <br>Allows the user to send a private message to another player. 
   <br>**Usage**: /msg <recipient\> <message\>. 
   <br>This command requires the `spm.message.send` permission.

-  `/reply` (or `/r`, `/re`): 
   <br> Allows the user to reply to a previously received private message. 
   <br> **Usage:** /r <message\>. 
   <br> This command also requires the `spm.message.send` permission.

- `/socialspy` (or `/ss`, `/spy`, `/sspy`):
   <br> Toggles socialspy for the user. 
   <br> Allows staff members to read other people's private messages. 
   <br> This command requires the `spm.socialspy.toggle` permission.

- `/spmreload`: 
   <br> Reloads the SimplePM configuration. 
   <br> This command requires the `spm.reload` permission.

### Permissions

| Permission                  | What it do                                                                                                       |
|-----------------------------|------------------------------------------------------------------------------------------------------------------|
| `message.reload`            | Allows reloading the config/locale                                                                               |
| `message.basic.send`        | Allows sending messages                                                                                          |
| `message.basic.receive`     | Allows receiving messages                                                                                        |
| `message.basic.toggle`      | Allows toggling direct messages on or off                                                                        |
| `message.basic.block`       | Allows blocking, unblocking, and listing your blocked users                                                      |
| `message.admin.override`    | Allows messaging users who have their messages disabled, have you blocked, or do not have the receive permission |
| `message.admin.social-spy`  | Shows a log of direct messages being sent between players                                                        |
| `message.admin.console-spy` | Shows a log of any direct messages being sent between the console and players (from this plugin)                 |

### PlaceholderAPI integration

You can use placeholderAPI placeholders in the messages by doing: `<papi:placeholder>`
<br>You will need to remove any `%` from the placeholder, and put in the text alone, so for the placeholder `%player_displayname%` you would use `<papi:player_displayname>` in the file here.
<br>You will need placeholderAPI installed for those placeholders to work
