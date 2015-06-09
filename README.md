# Reactive-JEE7
Example of using RxJava with JEE7 Websockets

Observable element are stored in a memory stored ( ehcache ) and observed by websocket sessions. 
Unit test initiate websockets sessions on client side and create observable items in server memory store. Other session are created that register are observer. Update of observable item notify websocket client of item changes.

Show use of JEE8 JCache annotations, JEE7 WebSocket mecanism. Build with Gradle.
Unit tests ran on Arquillian with install of Wildfly 8.2
