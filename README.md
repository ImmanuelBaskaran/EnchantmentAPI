# EnchantmentAPI

This is a fork of https://github.com/Eniripsa96/MCCore in order to :
- mavenize the project
- upgrade to 1.14.2
- upgrade to java 12

## Dependencies

Theses dependencies are required at build-time and run-time : 

- [MCCore](https://www.spigotmc.org/resources/mccore.49456/)
- [SkillAPI](https://www.spigotmc.org/resources/skillapi.4824/)

The latest versions at the time of writing was copied into the `lib` directory for reference :
- MCCore 1.0 
- SkillAPI 3.108

To install them in the local repository just run these commands :

```
mvn install:install-file -Dfile=./lib/MCCore-1.0.jar -DgroupId=com.sucy.mc -DartifactId=McCore -Dpackaging=jar -Dversion=1.0
mvn install:install-file -Dfile=./lib/SkillAPI-3.108.jar -DgroupId=com.sucy.mc -DartifactId=SkillAPI -Dpackaging=jar -Dversion=3.108

```
