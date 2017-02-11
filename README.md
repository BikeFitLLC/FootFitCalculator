# BikeFit-WedgeCalculator-Android

### Project Vision Statement:	
	FOR  Highend Bikers,
	WHO  Want better foot/pedal alignment,
	THE  Wedge Calculator, 
	IS A mobile application,
	THAT performs a personal wedge calculation.
	
	UNLIKE Going to a bikefitter,
	IT     Can be done anytime 

### Project information (tech stack):

* Android / Java; Minimum API 19
* Database: no internal database, but shared preferences are used
* Project is not localized, but Strings are in strings.xml
* JIRA: https://generalui.net/projects/BIK
* Github: https://github.com/generalui/BikeFit-WedgeCalculator-Android
* Buddy Build: https://dashboard.buddybuild.com/apps/57e2d495759eca0100634205 

### Project setup quickstart :
* What applications are recommended to work on the project?
  * Android Studio (including SDK, etc)
  * Git
  * The Bike Fit repo on your local machine

### All deployment environments, with description of use:

#### Debug setup
* The current project setup has been setup for debug

#### Production setup
* A Production setup has not been setup yet


### Description of the project’s branching / versioning approach:

* Master is the main branch
* Each ticket has its own branch (i.e. BIK-20/camera)
* Branching strategy
  * Branch names are <ticket>/<simpe_human_readable_description>
  * We use the jira ticket number
  * Example: BIK-20/camera (ticket 20, for adding a camera integration)

### Test accounts
* currently none

### Android details
* Minimum API 19
* ButterKnife
* Leak Canary

Special libraries:
* Material-Camera (https://github.com/afollestad/material-camera)
  * For supporting both Camera1 api and Camera2 api
  
Testing libraries:
* junit4
* mockito 

### Contribution guidelines

* Writing tests
  * Write ignored tests for features that are not available yet
* Code review
  * Code review pull-requests
* Other guidelines


### Who do I talk to?

* Jason Jerome jjerome@generalui.com


### Analytics

* Log in to google analytics as bikefitapps@gmail.com
* Note: the debug build does not send any analytics events
* Note: the iPhone events from Dec 2016 are 'spam' events.  Subsequently, Matt created a filter to block all traffic from Country = Russia, but George removed it Feb 2017, since the recommendation is you have at least one view that contains all traffic.

### How do I find it in the Play Store?

* The app is called "The Foot Fit Calculator" in the play store.
