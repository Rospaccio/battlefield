![Battlefield3d](src/main/resources/static/pics/logo_internal.png?raw=true)

## A minimal HTML5 multiplayer game based on BabylonJS and Spring Boot

Battlefield3D is a Multiplayer Online Battle Arena (MOBA) kind of game, where you are a cube trying to throw your opponents into the abyss. A right click on the playground gives you an impulse and, if you click enough, you will be able to move - although quite clumsily.

With a left click, on the other hand, you fire a powerfull spheric cannonball which is ten times heavier than a poor cube, thus allowing you to strike your opponents and throw them over the board, where they will meet a destiny worse than death: indefinite fall into the bottomless abyss (Actually, if you fall off the ground, you promptly respawn right in the middle).

### Why
Battlefield3D started as a fun test bed to explore and learn how to build a highly interactive networked system with Web Socket technology and its Spring implementation.

### Goals
The goal of the project is to build a MOBA (Multiplayer Online Battle Arena) that is playable in the browser without giving up cool features like 3D environments and pseudo-real time interactions.

### Methodologies
This is not only a Web application, which is a kind of beast that I know fairly well, it's also a client-server application with nearly real time interactions from a bunch of concurrent users. That introduces a whole new range of problems that I do not know exactly how to solve in the best possible way. 

When confronted with an issue that is new to me (e.g: how to synchronize player positions across several clients?), I usually try to solve it just with my knowledge and intuition (the "naive" way). Then, when the thing more or less works, I go back to read up and study the best practices, in order to understand what solutions I got right and what is wrong.

### Play online
Depending on the amount of personal free time and newly introduced bugs, an updated instance of the game should usually be available [here](http://www.codevomit.xyz/battlefield3d)

### Meta-Goals
Learning Web Socket and techniques to build a message based distributed system.
