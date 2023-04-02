# CS389_AdvSoftwareProject
A repository to house the semester long project for CS-389: Advanced Software Engineering.

This project is a webapp with an ip "tracker" which stores the information of people connecting to the webapp in a postgres database.
It is a very simple project and eventually is intended to house a resume of sorts; however, actually fleshing this out is low on my priority list.

You are welcome to download the project, build it, and set up the database yourself; however, for easy deployment follow the subsequent instructions:

1. In order to deploy, ensure that you have docker and docker compose installed on your computer. Instructions can be found here: https://docs.docker.com/get-docker/

2. Navigate to the releases for this repository and download the DockerDeployment.zip file from the latest release.

3. Proceed to unzip this on your local machine and, through the command-line interface of your choosing, navigate into the top level of the folder produced by the zip extraction.

4. Lastly, run the command "docker compose up" to launch the application.

5. The webapp can be accessed via the address "localhost".
