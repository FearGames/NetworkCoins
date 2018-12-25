# NetworkCoins
[![Build Status](https://ci.codemc.org/buildStatus/icon?job=FearGames/NetworkCoins)](https://ci.codemc.org/job/FearGames/NetworkCoins)
![Maven](https://img.shields.io/maven-metadata/v/https/repo.codemc.org/repository/maven-public/it/feargames/networkcoins/maven-metadata.xml.svg)

### Description
NetworkCoins is a MySQL based currency system.
It stores your Minecraft's IGN UUID in a MySQL table along with your balance.
This plugin allows you to add currency, remove currency, get your balance and or another players and reset a players balance to 0.
The plugin comes with its own API so you can integrate it into your own projects.

### Commands
/coins: View your current balance
/coins help: View the plugins help
/coins give (player) (amount): Give a player an amount of coins
/coins take (player) (amount): Take an amount of coins from a player
/coins reset (player): Reset a players coins
/coins user (player): Get a players amount of coins

## Maven dependency
How to include NetworkCoins into your maven project:

```xml
    <repositories>
        <repository>
            <id>codemc-repo</id>
            <url>https://repo.codemc.org/repository/maven-public/</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>it.feargames</groupId>
            <artifactId>networkcoins</artifactId>
            <version>0.6.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
```
