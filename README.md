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

* Language:
  * Android / Java; Minimum API 19
* Database: 
  * no internal database, but shared preferences are used
* Is the project localized?
  * No, but Strings are in strings.xml
* Ticket System:
  * JIRA: https://generalui.net/projects/BIK
* Version control:
  * Github: https://github.com/generalui/BikeFit-WedgeCalculator-Android
* CI / Build System:
  * Buddy Build: https://dashboard.buddybuild.com/apps/57e2d495759eca0100634205 

### Project setup quickstart :
* What applications are recommended to work on the project?
  * Android Studio (including SDK, etc)
  * Git
  * The Bike Fit repo to your local machine

### All deployment environments, with description of use:

#### Debug setup
* The current project setup has been setup for debug

#### Production setup
* A Production setup has not been setup yet


### Description of the project’s branching / versioning approach:

* Which branch is the one to develop off of?
  * each ticket has its own branch within reason (i.e. BIK-20/camera)
* Branching strategy
  * Branch names are <ticket>/<simpe_human_readable_description>
  * We use the jira ticket number
  * Example: BIK-20/camera (ticket 20, for adding a camera integration)
* Merge / Rebase approach for CI?
  * Rebase on develop before making a PR 

### Test accounts
* currently none

### Android details

* Minimum API 19
* ButterKnife
* Leak Canary

Special libraries:
* Material-Camera (https://github.com/afollestad/material-camera)
  * Supports both Camera1 api and Camera2 api
  
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
