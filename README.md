# SMS Interactive Anonymous Message Board
----
The SMS Interactive Anonymous Message Board is a very basic chat client that allows any user the ability to add a timestamped message. These messages can be added and viewed through the web-interface or by sending a text message to the Message Board's phone number.

### Tech Used
----
* Play Framework
* Spring
* Hibernate
* Twilio
* MySQL
* Twitter Bootstrap
* jQuery
* SBT
* ngrok *[Required for Twilio Support]*

### Installation
----
1. `Install sbt` to your system. This is a must have and required to build the project and download the dependencies located in the `build.sbt` file.


2. If you don't have it, insteall MySQL and create a new empty mysql database and name it `messagesDB`. Don't add any tables to it.


3. Now run the following commands

        $ git clone [git-repo-url] SIAMB
        $ cd SIAMB
        
4. open `conf/application.conf` in your favorite text editor and find these lines:

        db.default.driver=com.mysql.jdbc.Driver
        db.default.url="jdbc:mysql://localhost/messagesDB"
        db.default.user=
        db.default.password=""

5. Add your MySQL user to the user field. Add your password to the inside of the empty quotes. Save the file. 


6. Run:

        $ sbt ~run

Open your browser and go to `http://localhost:9000`. Play Framework may pop-up and tell you to apply a script, do it.

### Import into eclipse
---
To import the project into eclipse you will need to run an SBT command on the project to set it up for eclipse.

        $ sbt eclipse
        
You should now be able to import the project into eclipse. If you are going to make changes to anything be sure to run the command:

        $ sbt ~run

Now any changes you make will be automatically compiled whenever you save a modified file. You will not need to use the eclipse built in build tools.

### Setup Twilio SMS Support
---
To enable SMS support you will need to create a Twilio account. For this you can use the Trial Account credentials that they give you.

1. Log into your Twilio account and click `Get your Twilio number` button if you haven't already.


2. Using your favorite text editor open `conf/application.conf`. Locate the following lines:

        #Twilio
        #~~~~~~
        The default sms number should be in the form +9995553333
        #where 9's are the area code and so forth
        account.sid=""
        auth.token=""
        sms.default.number=""

3. On your Twilio account, add your Trial `account sid, auth token, and phone number` into empty quotes. Make sure you format the phone number as +9995553333. Save the file.


4. Now go back to your Twilio account and select `DEV TOOLS`from the navbar. Select `TWIML APPS` from the sub-menu. Now create a new TwiML App by clicking the `Create TwiML App` button. Pause and go to step 4.


5. Open a new table and go to `http://ngrok.com` and download ngrok to your machine. Open up new terminal and navigate to the directory to where you downloaded ngrok. Type:

        $ ./ngrok http 9000

6. This will create a tunnel to your localhost address. This is required so Twilio can make Post calls to your website. Normally you would be running this Message Board on a web-server and would not need ngrok. But for testing purposes ngrok is very useful.


7. In the ngrok terminal window, copy the Forwarding address it generated. Make sure you copy the `http` address *AND NOT* the `https` address. Go back to the browser tab you left open from Step 


8. Give your new TwiML App a friendly name and then the ngrok http address that you copied into the `Request URL` field. In the dropdown box select `HTTP POST`. Save the app.


9. In your Twilio account, select `NUMBERS` from the navbar. You should see your phone number below. We are going to assign the TwiML App that you created to that number. To do this, click on your number. Under `SMS & MMS` change the `Configure with` option to `TwiML App`. Under the dropdown that appears choose your TwiML App. Save.

Congratulations. You have successfully configured Twilio.

___
#Author - Shane Israel
#Contact - sisrael@sofi.org
