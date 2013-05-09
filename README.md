# Team 111's 2013 Robot Code

This repository contains the Team 111 2013 FRC codebase. This code is released under the BSD 2-clause license. A copy of this license is included in COPYING.

Similar to Team 254, we also decided to switch to Java from C++ this year. Java is easier to teach, many students already had some instruction in school, and the students can grasp the concepts faster. Also the students and mentors could work on both Windows or Mac computers. 

After the initial heartburn from Sqwuak's and Java ME's limitations, we feel the change was a success and will continue using Java in future years. I would recommend it for any team thinking about making the switch. However, they should understand how limited this version of Java really is. We lost about 2 weeks just trying to get basic functionality to compile and run because the compile/run time error messages are very cryptic. It took examining the WPI libraries design patterns and their use of this "Java" to understand how to recreate "simple" things like an unsynchronized List or a way to do a type-safe "enum". 

## Intro

The repository contains two Netbeans projects; crioTarget and WsSimulation. The crioTarget is what is run on the robot. The WsSimulation links against the crioTarget and replaces many of WPI classes with its own stub or simulation implementation. 

## crioTarget

The robot framework was designed using the Subject/Observer object oriented design pattern and inputs and outputs can notify any registered object when they change. To keep things simple and synchronous, all updates occured during the 20 ms periodic loop.     

### com.wildstangs.autonomous
Contains the autonomous framework, all autonomous programs and steps. 
  
### com.wildstangs.config/configfacade
Contains the base objects and singleton for managing configuration parameters from a file

### com.wildstangs.logger
Contains the singleton for handling logging. This needs to be used sparingly as printOut's are very slow

### com.wildstangs.inputfacade
Contains the subjects and implementations for inputs

### com.wildstangs.outputfacade
Contains the subjects and implementations for outputs

### com.wildstangs.subjects.base
Contains the base classes for subjects that are used throughout the framework

### com.wildstangs.subsystems
Contains all of the subsystems that control driving, shooting, and hanging

## WsSimulation
This allows the software team to test all of its logic before loading it on the hardware. The simulation was originally based on the project frcjcss ( https://code.google.com/p/frcjcss/ ) released under GNU GPL v3. frcjcss stubbed out a lot of the classes from the WPI library like Victors, Solenoids, Relays, and Joysticks and got us quickly started in getting our cRio code running on the desktop. Our adapted version simulates a variety of hardware including the drive encoders, flywheel encoders, gyro, and limit switches to test proper control schemes. frcjcss had an onscreen joystick and we added support for a USB joystick for inputs. The USB joysticks are supported using the javahidapi project (https://code.google.com/p/javahidapi/) released under the BSD license. We used two different PS3/Xbox type controllers and aside from the Directional Pad (see known issues), all buttons and axises read as expected.    

After the build season, it was determined that the SmartDashboard would work by linking in the desktop version of the Smartdashboard/Network table jars into the Simulation. SmartDashboard justs needs to be started with the command line parameter for the ip as localhost. This greatly impacts the entire UI and alot of the single UI windows can be removed.

The logger and config manager on the cRio with sqwuak "calls" had to be reimplemented for Java 6 for it to run on the desktop. 

## Known Issues
### Robot Framework
* Some autonomous steps do not clean up their listeners 
* FileLogger seems to overwrite rather than append after a robot reboot
* Containers for Subsystems, Inputs, and Outputs using DataElements and Lists seem overblown and inefficient
 
### Simulation
* Dpad on the hardware Joystick is not mapped correctly (not supported with the onscreen joystick)
* No way to simualate "practice" (Disabled->Autonomous->Disabled->Teleop->Disabled) Currently either Autononomous or Teleop is chosen as a compile time flag 
* UI didn't scale/Too many individual windows (Before the smartdashboard was working, these windows were all there was. The entire UI will need to be rethought for next year heavily utilizing SmartDashboard.)
* USB controllers are not assigned to driver/manipulator and can switch roles between simulation runs


