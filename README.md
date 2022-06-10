# Chat

A simple chat application using RabbitMQ. Customer service via a console terminal.

## Run (instructions):

You should prepare file with fields:
```java
// data.properties file
-
QUEUE_NAME=<name>
QUEUE_HOST=<host>
QUEUE_USER=<username from API>
QUEUE_PASSWORD=<password from API>
```
## Commands:

| command  |  result  | 
|---|---|
| -users  | list of active users  |
| -channels | list of your channels  |
| -private $username, $message | send private $message to $username |
| -public $message | send public $message |
| -new channel $name | create a new channel $name |
| -delete channel $name | delete a channel $name |
| -add user $username, $channel name | add user $username to channel $channel name |
| -delete user $username, $channel name | delete user $username from channel $channel name |
| -message $message, $channel name | send $message to channel $channel name |
