# Ebay Starter App

### Run code
- Run Server.java
- Run MqClient.java

### Extend Starter with Commands
- Create `Your Command` Class extends `command` with method `execute`
- Add it `commands` package
- Add `action name` and `Your Command` class name to `commands.properties`
- Change Queues Names in `config.properties`


### Json Requests Format
``` Json
{
  "action": "actionName",
  "properties": {"Message Queue properties and headers"},
  "data": {"Request Data"}
}
```
