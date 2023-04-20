# CS389_AdvSoftwareProject
A repository to house the semester long project for CS-389: Advanced Software Engineering.

This project is a webapp with an ip "tracker" which stores the information of people connecting to the webapp in a postgres database.
It is a very simple project and eventually is intended to house a resume of sorts; however, actually fleshing this out is low on my priority list right now.

You are welcome to download the project, build it, and set up the database yourself; however, for easy deployment follow the subsequent instructions:

1. In order to deploy, ensure that you have docker and docker compose installed on your computer. 
    * Instructions can be found here: https://docs.docker.com/get-docker/
    * Instructions on the docker compose plugin can be found here: https://docs.docker.com/compose/install/

2. Navigate to the releases for this repository and download the DockerDeployment.zip file from the latest release.

3. Proceed to unzip this on your local machine and, through the command-line interface of your choosing, navigate into the "Grosse_AdvSoft_App" folder within the folder resulting from the extraction process.

4. Lastly, run the command "docker compose up" to launch the application. 
    * (If you have used this application prior to now, make sure to run "docker compose build" before you run "docker compose up" to ensure the fresh images are downloaded)

5. The webapp can be accessed via the address "http://localhost/".

---


If you would prefer the difficult way either to learn more or to customize to your liking, here are the steps, these instructions will use docker and postgresql, but can be adapted for any method of deploying a database that you choose:

1. The easiest method of setting up your database is still using docker, please use the instructions linked at the following link:
    * Instructions can be found here: https://docs.docker.com/get-docker/

2. Once docker has been downloaded, open up the command line interface of your choice, type the following: "docker pull postgres"

3. Execute the following command to set up your database container: "docker run --name \<your container name here> -e POSTGRES_USER=appUser -e POSTGRES_PASSWORD=appPW -e POSTGRES_DB=tracker_db -p 5432:5432 -d postgres" replacing \<your container name here> with whatever name you would like to give your docker container to keep track of the purpose of the container.
    * Note that if you are using a database other than postgres, you can either use the postgres default port (5432) to expose and not have to configure the application persistence cconnection port, or you can choose the alternative database's default port to expose, but make sure you change the configuration of the port the application connects to in the application.properties file at src/main/resources/.
        * If using a different database you will need to change the "spring.datasource.url=" value to the proper value for a database flavor of your choice (I will not provide a resource on this)
        * If using a different database you will need to change the "spring.datasource.driverClassName=" value to the driver for the sql dialect of your choice (I will not provide a resource on this)

4. Now that your database is running, ensure you have capability to use git command line interface on your computer.
    * For Windows, I personally use GitBash, which comes with git for windows (https://gitforwindows.org/).
    * For unix based operating systems, use the package manager of your choice to install the "git" package. I also find it helpful to install and configure the github CLI package (https://cli.github.com/manual/installation) as a credential manager for git to avoid the use of personal access tokens to sign into the git command line for each new repository. I am not familiar with the interoperation of the github cli package and GitBash for Windows and cannot guarantee they will work together; however, the github cli plugin can also be installed for Windows.

5. Execute the following command: "git clone https://github.com/jgrosse01/CS389_AdvSoftwareProject" in a directory of your choosing.

6. If needed, this would be the time to configure the application to connect to the database of your choosing as mentioned in step 3 using a text editor of your choosing.

7. Once configured, navigate using a command line interface of your choosing to the top level of the cloned repository on your local computer.

8. Run the command "gradlew build" if on Windows terminals or the command "./gradlew build" if using a Unix based terminal (including GitBash for Windows).

9. You may now choose to run the program with gradle or by using java to execute the compiled jar.
    * If you choose to run the application with gradle:

        1. In your command line that still resides in the top level of the cloned project, simply type "gradlew bootRun" for Windows terminals or "./gradlew bootRun" for Unix terminals. The application should now connect to the running database you set up in step 3 and you may connect to the website from the address "http://localhost:8080".

    * If you choose to run the application with Java from the compiled jar:

        1. IF you do not already have Java 17 installed, install the Java JRE or JDK flavor of your choice, ensuring that the version is the latest release of Java 17. Here is the link to Oracle Java 17 (https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html). Ensure you install the proper version for your operating system.

        2. Navigate to the "build/libs" directory in the cloned project and there should be two jar files. We are interested in the one that does NOT contain "plain" in the name. 

        3. Move the proper jar to wherever you would like to execute the application from (ensure that you do not run the application from a priviliged directory as it will create log files and does not require administrator permission to run, running from priviliged directories is unsupported and untested). 

        4. Navigate to the folder containing our jar file using the terminal of our choosing and execute the command "java -jar \<jar file name here>", replacing \<jar file name here> with the name of the jar file of the application, and, similar to the process with gradle, the application will connect to the databse you have created and you can now connect to the website from the address "http://localhost:8080".
            * NOTE ON JAVA VERSIONS: If you have multiple versions of java installed/had older versions installed prior to this version, you may need to specify the path to your java executable. For example, on Windows, having Java 8 installed prior to installing Java 17, the command became ""C:\Program Files\Java\jdk-17\bin\java.exe" -jar \<jar file name here>" instead of simply being able to call "java".
