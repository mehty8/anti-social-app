<div align="center">
   <h1>Anti Social App</h1>
   <img src="/android-logo.png" alt="Android logo">
   <p>This is a video-sending android application</p>
</div>

## Prerequisites

- Android 11 or higher

## Aim of this project

- To learn the basics of android application development
- To learn how to dynamically store and serve media content (in this case video)
- To learn the basics of cloud computing (in this case AWS)
- To learn how to deploy an application to production environment

## Technologies

- Java spring boot (backend)
- Java android studio (frontend)
- Postgres (database)
- AWS services:
   - S3 Bucket (storing videos)
   - Elastic Beanstalk (Deploying backend)
   - RDS (Database)
   - Security Manager (Storing secret)
   - System manager (Storing insensitive information)
 

## Features

- Register/Login
- Search for user, send friend request
- Accept/Deny friend request
- Record, send video to a friend
- Watch sent/received video

## Installation

- The app is self-distributed, not via any app store, therefore make sure your phone allows installation from unknown sources:<br>
    Settings -> Security -> Application from unknown sources -> Allow Google Drive, or something like that, It differs from phone to phone.
- Open this link on your phone:
    https://drive.google.com/file/d/167xb3ZZlQSEAKsg8V_5cv5FnAcudQ193/view?usp=drive_link
- The system's package installer will pop up, tap on that.
- It may look like nothing is happening but in the backgorund the package installer is preparing the app for installation, sometimes it takes even 30 seconds to finish.
- The Installation will pop up, install the app.
- Since the app is from unknown sources, not via an app store, play protect may show up to check the app. Just tap on details, and choose to ignore it.
- Open the app and play along :).

## Tutorial

<div align="center">
   <h3>Storage access permission</h3>
   <img src="/storage-acces very first page.png" alt="Storage access permission">
   <p> </p>
   <p>
      This is the welcome page if You open the app for the very first time. You have to give access, cause the application does not stream the video when You record it, instead, saves it on the phone till You send it or cancel it, then the system deletes the recorded video. So push the switch to right, then tap the "back" arrow at the top-left corner.
   </p>
   <h2></h2>
</div>

<div align="center">
   <h3>Register and login</h3>
   <img src="/register-login.png" alt="Register and login page">
   <p> </p>
   <p>
      The Username can only have letters, numbers and underscore. The Password must be at least 8 characters long and have at least 1 uppercase, 1 lowercase and 1 digit. So please proceed accordingly, however if the provided credentials are invalid, or something goes wrong during registration, a message, down at the bottom, will be shown and it is going to tell You what to do. After you registered You can login immediately.
   </p>
   <h2></h2>
</div>

<div align="center">
   <h3>Video/Audio permission</h3>
   <img src="/permission-video.png" alt="Video permission">
   <img src="/permission-audio.png" alt="Audio permission">
   <p> </p>
   <p>
      You have to provide permission to record video and audio. Choose "while using the app" if You do not wish to do this each and every time before recording a video.
   </p>
   <h2></h2>
</div>

<div align="center">
   <h3>Main page</h3>
   <img src="/main-page.png" alt="Main page">
   <p> </p>
   <p>
      Here We are, the main page. Amazing design right :). At the top, You can check your sent or received videos. The videos are avaiable up to 12 hours from the making, after that They got completely deleted from the storage.<br>
      Below You can look for users. The system searches for any match that has the provided text in it. For "test", it will find "test1", "testarosa", "testicle" users as well.
      However only users that You are not friends with, or sent/received a friend request will appear.<br>
      After that You can see the friend requests that were sent to You. In the frame, at the left side, there is the username of the requester, decline or accept it.<br>
      In the next segment You can see Your friends. By tapping on them You can record and send videos.<br>
      Finally at the bottom, You can logout or refresh the page. The session lasts for 10 minutes, so after login, You will have 10 minutes before the system logs You out, to login again and start a new session. Because of this there are safety measures introduced, so the session will not expire while for instance, You are recording a video, or if do so, the system can handle the error. You can only start a user search up until 15 seconds, or a video recording up until 1 minute before the session expires.<br>
      The refresh button is needed cause the auto-update function is not developed. When You get a friend request, or your friend request was accepted, You will not be notified, unless You refresh the page.
   </p>
   <h2></h2>
</div>

<div align="center">
   <h3>Video list</h3>
   <img src="/video-list.png" alt="Video list">
   <p> </p>
   <p>
      This is the sent or received video list segment. In the frame, at the left side, there is the username of the user that You got the video from or You sent it to. After the colon comes the name of the video. By tapping on the frames, You can watch the videos. The videos are not downloaded to your phone, They are streamed from the cloud, therefore They might stutter.
   </p>
   <h2></h2>
</div>

<div align="center">
   <h3>User-search</h3>
   <img src="/user-finder.png" alt="Look for users" >
   <p> </p>
   <p>
      Here You can find the result of the user-search. By tapping on the frames, You can send friend requests.
   </p>
   <h2></h2>
</div>

<div align="center">
   <h3>Start/Stop video</h3>
   <img src="/start-video-recording.png" alt="Starting video">
   <img src="/stop-video-recording.png" alt="Stopping video">
   <p> </p>
   <p>
      This is the video recording page. You get here after You tapped on one of your friends. Tap the red button at the bottom to start recording the video. You can also flip the camrea by tapping the "recycle" button at the top right corner. To stop the recording, tap the square at the bottom.
   </p>
   <h2></h2>
</div>

<div align="center">
   <h3>Naming video</h3>
   <img src="/name-video.png" alt="Naming video">
   <p> </p>
   <p>
      This dialog pops up after You are done recording the video. You have to name it to finalize and send the video. If, for some reason, You wanna cancel the procedure and delete the video, just tap cancel.<br> If despite the precaution, your session expires while You were making the video, no matter what You tap on, the system will automatically cancel the procedure, delete the video and log You out.
   </p>
</div>

## Have fun...

That is it, have fun and thanks for dropping by. 
If You are a recruiter, a hiring manager or basicaly anybody that is looking for a junior developer, please do not hesitate to contact me.

### Contact
- Name: Balint Csanyi
- Email: mehty8@gmail.com
- linkedin: https://www.linkedin.com/in/balint-csanyi/
