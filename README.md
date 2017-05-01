# Ebay Starter App

- Create DB using DDL in folder database
- Create Procedures using stored procedures in database
- Run Postgresql
- Run Mongod
- Run RabbitMq Server

### Run code
- Configure configuration.properties
- Run Server.java
- Run MqClient.java
- Run node tester.js with the commands you want to test
- Copy Json from terminal and publish to RabbitMq Queue

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
