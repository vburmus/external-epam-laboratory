# Task 6 CI/CD
### Task

1. Configure Jenkins security (install Role strategy plugin). Remove anonymous access. Create administrator user (all permissions) and developer user (build job, cancel builds). Add Jenkins credentials to Readme file in your git repository. 
2. Configure Jenkins build job (pool, run test, build) to checkout your repository, use pooling interval.
3. Install SonarQube. Configure Jenkins to use local SonarQube installation. Analyze your source code with SonarQube after Maven builds your project. Use JaCoCo for code coverage. 
4. Jenkins should deploy your application (after passing SonarQube quality gate) under your local tomcat server. Please use Jenkins Tomcat Deploy plugin.

#### General requirements

1. Jenkins have to build your project according to the Maven build script.
2. Project (application you have developed for Rest API module?) is deployed at your local Tomcat Server by Jenkins job. 
3. Jenkins should be integrated with SonarQube.

##In addition, I have added Jenkins file for docker to deploy my application there.
