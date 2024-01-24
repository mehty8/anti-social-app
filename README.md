Contact information:
- Name: Balint Csanyi
- Email: mehty8@gmail.com
- linkedin: https://www.linkedin.com/in/balint-csanyi/


This is a video sending android application, that is a bit far from being done, however it is already operational.

What it already can do:
- User registration
- User login
- Look for users by username
- Send friend reuquest
- Accept friend request
- Record and send video to a friend
- Watch the received or sent video anytime up to 12 hours from recording.

There are many features to develope, and also some refactoring to align with the OOP principles, readability and so on.
These are gonna be developed in the months to come but because I'm looking for a job as a junior software developer,
I made this project public, as a reflection on my current spring boot knowledge.

So the main target here is inspecting the code. However - since docerization is not fully possible due to AWS S3 bucket, there is none at all - 
if You wish to run the app, these are the steps to make:

//AWS
- Create an account on AWS (it is free of charge)
- Create an IAM User with s3 full access permission policy
- Generate the access key (application running outside AWS), save it and the secret key on your computer.
- Create a bucket. Name it and choose the closest region to You and that is it.

//Postgress
- Create a database called "anti-social-app"

//Android studio
- Open the frontend in android studio
- Have an emulator, preferably api 33 or higher and medium-size with tiramisu.
- Go to the VideoUploadService class and change the bucket name to your bucket"s name.

//Intellij
- Open the backend in Intellij (or any other java ide)
- In the resources directory, create a file called "application.properties".
- Copy the content of the application.properties.template to the created application.properties.
- The values in CAPITAL, must be replaced with your data:
   - Choose your own jwt secret, make sure it is long enough and does not contain special characters like /*?...
   - Set the expiration time of the jwt in millisecond. However since the logout function is not developed yet, do not set it for longer than 10 minutes.
   - In the AWS section you have to provide your access key, secret key and the region of the bucket.
   - In the Postgres section provide your postgres username (the owner of the database), and the password.
   - The rest is good as it is, no need to change them.
     
Run the backend, it is gonna migrate the database.
Run the frontend emulator, give permission when asked, and play around with it.

The design is hideous, I know :), again the project is far from done.
If it does not say, You just have to tap on the names to send or accept friend request, record, send and watch videos.
The "back" function is not developed yet , so just use the "back" button over the emulator :).

That is it so far, thanks for dropping by. 
If You are a recruiter, a hiring manager or basicaly anybody that is looking for a junior developer, please do not hesitate to contact me:
mehty8@gmail.com
