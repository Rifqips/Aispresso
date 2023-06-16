# Documentation

1. Create ML model &  API  (for information and health facilities)
2. Convert ML model to tflite
3. Deploy API to google cloud using Google app engine
4. Create android project using android studio
5. Import some library that we need
6. Download and copy tflite model to asset folder in android studio
7. Using tflite library to get data prediction
8. Using retrofit to get data from API

## Mobile Development

1.	Open the Aispresso application.
2.	Read the Onboarding to understand the features of Aispresso.
3.	Log in using your email and password, or press the Register button if you don't have an account yet.
4.	Go to the Home page and tap the Camera button.
5.	Select the Leaf button to detect plant diseases or the Coffee Bean button to classify coffee bean quality.
6.	Enter the Classification page and tap Take Photo to access the camera.
7.	Capture an image of the object based on the options mentioned in step 5.
8.	Return to the Classification page with the results corresponding to the captured image.
9.	Click Save to add the data to the History page.
10.	Go back to the Home page and tap the History icon in the Navigation button to access the History page.
11.	Users can select a specific data entry to view its details.
12.	Return to the Home page, tap the Weather icon to access the Weather page, and view the current weather conditions at the user's location.
13.	Users can tap their profile to access the Profile page and press Logout to return to the Login page.


## Machine Learning

- System Requirement
- Working on : Google Colaboratory
- Tensorflow : 2.12.0
- Dataset : [USK-Coffee Dataset](https://comvis.unsyiah.ac.id/usk-coffee), [Kaggle Coffee Leaf Diseases](https://www.kaggle.com/datasets/badasstechie/coffee-leaf-diseases)

the steps to create an ML model
1.	Check the version of Tensorflow
2.	Import all required library
3.	Gather dataset
4.	Import dataset
5.	Split dataset and make dataset directory
6.	Preprocess the dataset: Image Augmentation
7.	Create model using transfer learning with ResNet and MobileNet
8.	Create callback for data training
9.	Define loss, optimizer, and metrics
10.	Define epoch for data training
11.	Train the data
12.	Plot training and validation accuracy
13.	Predict images with the created model
14.	Save the model
15.	Convert the model to Tensorflow Lite


## Cloud Computing

Cloud Computing Path
Creating RESTful APIs and deploying to Google Cloud Platform by using Cloud Run for connection between android application and database. Using Cloud SQL for creating the database server.

RESTful APIs
In making the RESTful APIs we use NodeJS with the Express Framework for building an API server, and for responses using JSON format. Explanation for each URL that can be used :

Base URL :
https://aispresso-e22whqvy2q-uc.a.run.app/

-------------------------------------------------------------------------

Register
Path: 
/auth/register

Method:
POST

Request body: 
name as string
email as string
password as string must be at least 8 characters

-------------------------------------------------------------------------

Login
Path:
auth/login

Method: 
POST

Request Body:
email as string
password as string
