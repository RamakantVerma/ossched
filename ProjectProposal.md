### Abstract ###

The following document is a proposal for the fourth year project “Calendar Mashups for Open Source Web Conferencing” under the Department Of Systems And Computer Engineering at Carleton University. The project is supervised by Professors Steven Muegge and Michael Weiss and is to be completed by Bryan Langille and Mykhaylo Ryechkin in the 2008-2009 academic year.  The project is based on an open-source web conferencing platform called Big Blue Button.

### Details ###

  * Develop an API to Big Blue Button that will expose its scheduling-related functionality
  * Enable use of third-party calendar web applications as a scheduling tool for Big Blue Button

### Background Information ###

Big Blue Button is an open-source project and "was developed and first deployed at Carleton's Department of Systems and Computer Engineering in 2007 to deliver graduate courses within the Technology Innovation Management program. It currently implements real-time voice conferencing, video conferencing, document sharing, group chat, and audio archiving."[source](http://www.sce.carleton.ca/courses/sysc-4907/webforms/faculty/proposalmain.php?prop=176) Big Blue Button offers a simple, yet functional user interface and an expandable platform with room for future enhancements. Functionality yet-to-come includes application/desktop sharing, scheduling, recording / archiving and whiteboard.

### Motivation ###

This project will introduce us to the world of open-source development and further improve our object-oriented programming skills by giving us a chance to work on a commercial-grade software solution. Following the success of the project, there also exists an opportunity to have our work recognized by being included as part of the core framework.

### Description ###

This project will enable users to schedule new events such as web conferences and online classes, as well as view and modify the existing schedule. The creation and modification of events will be controlled by user privileges (ie. only a professor can schedule new online classes; a student can only view the schedule, etc.).

The above mentioned scheduling functionality will be exposed by creating an API, which will allow the interfacing of Big Blue Button with a number of third-party calendar web applications.

### Method ###

Agile software development methodology will be employed for this project. We will hold biweekly meetings (scrums) with the project stakeholders (Professors Muegge and Weiss) in order to get feedback on the current stage of development. During each meeting, we will assess progress made from the preceding scrum and outline what the next set of objectives will be for the following two weeks.

There are many aspects of the project that are unknown at this time, and therefore using the above approach will allow us to define more detailed requirements based on the stakeholders’ feedback, as the project progresses.

Big Blue Button is an open-source project and presently has an existing community of developers actively working on it. As such, we will make use of the community’s resources for help and/or reference regarding the project. We will join the developers’ discussion group and mailing list in order to get quick and up-to-date information on the overall progress of Big Blue Button project as well as specifics related to our project. This way, we will also be able to share any knowledge or experiences that may be helpful to other developers in the community. In addition, a Google Code project page and a Wiki will be created specifically for our project where we will post updates on our progress.

### Proposed Timeline ###

As mentioned above, we will be using an Agile software development approach, and as a result the development will be done in two-week iterations. The timeline on the following page outlines our projected development path. It shows project-independent milestones (as set by the course co-ordinator) as well as our biweekly development schedule. Some of the forecasted development stages are subject to change as the project progresses and the stakeholders’ requirements are assessed in more detail.

![http://img144.imageshack.us/img144/8818/proposaltimelinecopyxi0.jpg](http://img144.imageshack.us/img144/8818/proposaltimelinecopyxi0.jpg)

### Dependencies ###

  * Access to Big Blue Button database structure
  * Familiarization with Adobe Flex technology
  * Software required:
    * Adobe Flex Builder 3
    * Java Development Kit
    * Ant
    * ActiveMQ
    * Tomcat
    * SWF Tools and PDF Toolkit
    * (Sub)Version Control Client