# Demo Java App

## Description
This application was created in order to demonstrate CI/CD capabilities for a Java application.

### What does it do?
The short answer - not a lot! When this application is started, it essentially just schedules a
task that fires at regular intervals (default is 5 seconds). Every interval, the application sends
a HTTP POST request to an endpoint with the following information:

* The name of this application
* The version of this application
* The name of the host on which this application is running

That's all there is to it. The application is intentionally simple.

### How can I execute the tests?
From the root directory of this repository, execute the following command:
`./gradlew clean test`

### How can I run this application locally?
From the root directory of this repository, execute the following command:
`./gradlew clean bootRun`

NOTE - Locally, this application will try to post updates to a local endpoint. If you run this
application locally you will likely see exceptions for that reason.

### Where is the Jenkins pipeline for this application?
The build job is [here](http://52.61.105.186:8080/job/demo-java-app-pipeline/).
The deploy job is [here](http://52.61.105.186:8080/job/demo-java-app-deploy/).