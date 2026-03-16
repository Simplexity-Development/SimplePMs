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

### Webhook Support

Using v2.5.0 or higher, you can use Webhooks as a way to log messages sent via SimplePMs. Previously you may have used DiscordSRV to log SimplePM messages to Discord.

By default the `config.yml` offers these options:

```yml
###
# This adds Webhook Support focused for Discord
###
webhook:
  enabled: false
  url: ""
  # This is the JSON Body to send to the webhook.
  # The default value is modeled after https://docs.discord.com/developers/resources/webhook#execute-webhook
  # These are the placeholders that can be used:
  #   <sender>: Sender's Username
  #   <sender_display_name>: Sender's Display Name (Nickname), Non-players will have the same name as <sender>
  #   <sender_uuid>: Sender's UUID, formatted as xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx, this is blank for non-players.
  #   <recipient>: Recipient's Username
  #   <recipient_display_name>: Recipient's Display Name (Nickname), Non-players will have the same name as <recipient>
  #   <recipient_uuid>: Recipient's UUID, formatted as xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx, this is blank for non-players.
  #   <message>: The message sent through SimplePMs.
  #   <timestamp>: Unix Timestamp (https://www.unixtimestamp.com/)
  json-body: |
    {
      "content": "{message}\n-# {sender_display_name} ({sender}) sent {recipient_display_name} ({recipient}) this on <t:{timestamp}>",
      "avatar_url": "https://mc-heads.net/avatar/{sender_uuid}",
      "username": "{sender} > {recipient}"
    }
```

- **Enabled**: Whether or not webhooks are enabled.
- **URL**: URL of the webhook.
- **JSON Body**: JSON formatted string intended to be sent as the body in a POST request. Placeholders exist for use as well.

If you are using this for Discord,
- The documentation for the JSON body can be found here: https://docs.discord.com/developers/resources/webhook#create-webhook
- Creating a webhook for Discord can be found here: https://support.discord.com/hc/en-us/articles/228383668-Intro-to-Webhooks
