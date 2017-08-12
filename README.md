# VRwearSpaceShooter
**Android VR space shooter controlled with a smartwatch**

The goal of this project is to explore some possibilities of how a smartwatch can be used to interact and control a virtual reality environment. The focus is on how to read the input data from a watch, transfer it to the phone and interpret it as controller input for a 3D space. A possible use case of this input mechanisms is demonstrated in a simple 3D virtual reality game. This game uses the smartwatch as input device.

## Info

The problem of simple VR setups (therefore when only using a headset and no other external components) are the limited control mechanisms. One possibility to control the VR application is by using another device an increasingly number of people are using: a smartwatch. In this project, we want to experiment with various input methods of the watch to navigate and control a VR application. We especially want to focus on touch and motion gestures. To demonstrate the input methods, we are building a simple game set in space, in which the user controls a spaceship and fight off incoming asteroids.

### Software
- Android (for the Phone and the Smartwatch)
- Unity Game Engine

We use the Android ecosystem to develop the apps (Android Phone and Android Watch) and the Unity Game Engine to develop the game.

### Hardware
- Google Cardboard 
- LG Watch R

### Project Setup

<img src="/img/Project_Setup_Overview.png" width="60%">

The game is made with the Unity game engine and programmed using C# scripts. To communicate with the watch, the Android Wear API is used. Therefore, a small android library (in Java) is imported into the Unity game and is used to receive the messages from the watch.
The Android App running on the watch that constantly reads the sensor values and sends them to the Android library inside the game on the phone. The library passes the values on to the game.



The App is available on the Google Play Store: https://play.google.com/store/apps/details?id=at.fhooe.pro2

Details about how the communication between the smartwatch and the phone is implemented: https://blog.haknode.com/using-android-watch-sensors-to-control-a-unity-game/

<img src="/img/screenshot.png" width="80%">
