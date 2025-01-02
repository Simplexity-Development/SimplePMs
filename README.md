# SimplePMs

## Releases available on [Modrinth](https://modrinth.com/plugin/simplepms)

Simple plugin for handling direct messaging that features:

- Message and Reply
- Toggle messages on or off
- Block users
- Custom formatting with Placeholder API integration
- Social Spy
- Ability to message users from the console, and them to message back

### Commands

| Command                   | Permission                 | What it do                                                          |
|---------------------------|----------------------------|---------------------------------------------------------------------|
| `/msg <player> <message>` | `message.basic.send`       | Send a private message to another user                              |
| `/msgtoggle`              | `message.basic.toggle`     | Enables/disables recieving private messages                         |
| `/r <message>`            | `message.basic.send`       | Replies to the last person you were talking to                      |
| `/block <user> <reason>`  | `message.basic.block`      | Prevents this user from private messaging you                       |
| `/unblock <user>`         | `message.basic.block`      | Allows this user to private message you again                       |
| `/blocklist`              | `message.basic.block`      | See the list of users you have blocked                              |
| `/socialspy`              | `message.admin.social-spy` | Toggles whether the user can see the logged messages of other users |
| `/spmreload`              | `message.reload`           | Reloads the plugin configuration and messages                       |

### Permissions

| Permission                   | Default | What it do                                                                                                       |
|------------------------------|:-------:|------------------------------------------------------------------------------------------------------------------|
| `message.reload`             |   OP    | Allows reloading the config/locale                                                                               |
| `message.basic.*`            | `true`  | Gives all basic messaging functionality                                                                          |
| `message.basic.send`         | `true`  | Allows sending messages                                                                                          |
| `message.basic.receive`      | `true`  | Allows receiving messages                                                                                        |
| `message.basic.toggle`       | `true`  | Allows toggling direct messages on or off                                                                        |
| `message.basic.block`        | `true`  | Allows blocking, unblocking, and listing your blocked users                                                      |
| `message.admin.*`            |   OP    | Gives all subsequent admin permissions                                                                           |
| `message.admin.override`     |   OP    | Allows messaging users who have their messages disabled, have you blocked, or do not have the receive permission |
| `message.admin.social-spy`   |   OP    | Shows a log of direct messages being sent between players                                                        |
| `message.admin.console-spy`  |   OP    | Shows a log of any direct messages being sent between the console and players (from this plugin)                 |
| `message.bypass.social-spy`  |   OP    | Prevents messages you send or messages being sent to you from being shown to others with social spy              |
| `message.bypass.command-spy` |   OP    | Prevents commands you send from being shown to social spy                                                        |

### PlaceholderAPI integration

You can use placeholderAPI placeholders in the messages in the config by doing: `<papi:placeholder>`
<br>You will need to remove any `%` from the placeholder, and put in the text alone, so for the placeholder
`%player_displayname%` you would use `<papi:player_displayname>` in the file here.
<br>You will need placeholderAPI installed for those placeholders to work

> Note: This only works in the `config.yml`
>
> This will not work for messages in the `locale.yml`