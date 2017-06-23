# simple-db-client

A simple database client for Java

## Using simple-db-client

### Step 1): Add the jar to your classpath

Using Maven:

First add the repository to your repositories:

```xml
<repositories>
  <repository>
    <id>simple-db-client</id>
    <url>https://dl.bintray.com/legosteen11/simple-db-client/</url>
  </repository>
</repositories>
```

```xml
<dependency>
  <groupId>io.github.legosteen11</groupId>
  <artifactId>simple-db-client</artifactId>
  <version>1.0</version>
  <type>pom</type>
</dependency>
```

Using Gradle:

First add the repository to your repositories:

```groovy
repositories {
    maven {
        url "https://dl.bintray.com/legosteen11/simple-db-client/"
    }
}
```

```groovy
compile 'io.github.legosteen11:simple-db-client:1.0'
```

### Step 2): Choose your client implementation

Now you can start using simple-db-client:

First create a new object of an implementation of IDatabaseClient. 

Currently you can choose from these implementations:
- `SimpleDatabaseClient`

In Kotlin:
`val client = SimpleDatabaseClient(host, username, password, database, port)`

In Java:
`SimpleDatabaseClient client = new SimpleDatabaseClient(host, username, password, database, port)`

The `port` parameter is optional and is `3306` (for MySQL) by default.

### Step 3): Start using the client object

Now you can start using the client object

You can call the following functions:
##### executeUpdate

```java
client.executeUpdate(sql, parameters...)
```

Will xecute an update (for example: 

```java
client.executeUpdate("CREATE TABLE IF NOT EXISTS users(id INT(11) AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255))")
``` 
will execute the SQL and create the table)

##### executeQuery
```java
client.executeQuery(sql, parameters...)
``` 

Will execute a query (for example: 
```java
client.executeQuery("SELECT * FROM users WHERE id = ?", 3)
``` 

will return a ResultSet with the results of the query.)

##### getConnection
```java
client.getConnection()
``` 

Get a Connection

##### getSingleValueQuery
```java
client.getSingleValueQuery(sql, parameters...)
``` 

Will return a single value from a query (for example: 
```java
client.getSingleValueQuery("SELECT name FROM users WHERE id = ?", 3
``` 
will return the name of the user with id 3)

## Contributing/Helping
Thank you for considering to contribute!

If you want to help by adding an implementation of the IDatabaseClient please add your implementation in the `io.github.legosteen11.client` package (in `src/main/kotlin`) and add a unit test in the same package under the `src/test/kotlin` folder with all the functions you are overriding. Check [this](https://github.com/Legosteen11/simple-db-client/blob/master/src/test/kotlin/io/github/legosteen11/simpledbclient/client/SimpleDatabaseClientTest.kt) example for how to add a unit test.

## About

This library was created for use in a few projects for myself
