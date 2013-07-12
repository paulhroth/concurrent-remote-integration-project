Remote Concurrent Integration Test Automation w/ Selenium, JUnit & SauceLabs
============================================================================

Intro
-----

This project contains everything you need to get started writing and running JUnit4 Selenium tests in the cloud.
Running your tests concurrently is vital if you have any concerns about speed - testing gets done a lot faster when you have fifteen tests running at once as opposed to one.
The cloud-based also approach has a number of key advantages, including saving you the likely substantial expense of purchasing and maintaining your own hardware, and allowing you to test any of 150+ platforms with ease.

This is a Maven project, so its depencies can be easily and automatically downloaded. You should be up and testing in no time!

I recommend using Eclipse as your platform from which to run your integration tests.
Note: Instructions on how to implement these tests into CI will be added soon.

Happy testing!

Setup
-----

1. Install Maven and Eclipse on your machine.

2. In Eclipse, add ```M2_REPO``` as a classpath variable pointing to your local Maven repository. 
   This can be done as follows from inside Eclipse:
   Select Eclipse -> Preferences -> Java -> Build Path -> Classpath Variables.
   Click on the button labeled "New", then enter "M2_REPO" into the name field and the path to your local Maven repository in the path field.
   The default path is ```/Users/example.user/.m2/repository```.

3. Clone this repo, and in the root directory run the following command: ```mvn eclipse:eclipse```. This will set up all required Eclipse metadata.

4. Import the project into Eclipse as follows: File -> Import -> General -> Existing Projects into Workspace -> Next
   Add the path to selenium-maven-project into the "Select root directory" field. Select it in the "Projects" field and hit finish.

5. In the root directory of the repo run: ```mvn package```. This will download all required dependencies, getting rid of any Eclipse error messages.

Configuration
-------------

In selenium-maven-project/src/main/config, you can modify the following files to suit your setup and requirements:

1. baseurls.properties: this takes key-value pairs, in the form "keyname=URL", e.g., "google=http://google.com".

2. userinfo.properties: Same syntax as above, except this takes three keys, "username", "password", and "accesskey".
                        Fill these in with your SauceLabs information.

3. config.xml: ```<sauceplatform os="operating system">``` tags contain ```<browser name="browser name">``` tags which themselves contain a ```<versions>``` tag in
               which you can type a string of versions to be used, e.g., "1 2 3 5 8.6".

In selenium-maven-project/src/main/java/com/paulhroth/selenium/tests, you will find commented example tests that you can easily modify to your liking.
Look for the ```@Test``` annotations: the method(s) below these contain the actual executed test code.

Running Tests
-------------

NOTE: Running tests via Buildbot is currently a WIP; instructions will be added here when finalized.

1. Navigate to selenium-maven-project (the root directory) and start the SauceConnect tunnel like so:
   ```sh sauceconnect.sh```
   You will be notified when the tunnel is ready via stdout.

2. In Eclipse, right click the test you wish to run -> Run As -> JUnit Test

3. View results and any failure traces via the Eclipse JUnit panel which will automatically appear.

4. View more detailed results, including a video of the test, screenshots and logs, on http://saucelabs.com
