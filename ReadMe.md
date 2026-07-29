# Report App

This is a lightweight web application for generating reports from game transaction records.

## Tech Stack

- Java 8
- Maven 3.x
- Spring MVC
- Hibernate / JPA
- MySQL 8
- JSP
- JUnit 5
- Mockito
- Docker (optional)

## Prerequisites

Before running this project, make sure you have installed
- Java 8+
- Maven 3.8+
- MySQL 8
- Git

> **Note:** If you have <b>Docker</b> installed, none of the above prerequisites are necessary.

## Clone Project
download [Git](https://git-scm.com/install/windows) and install
Verify Installation
```
git --version
git version 2.41.0.windows.1
```
After verification run below command to clone project 
```
git clone https://github.com/wangran326990/company_project.git
```

## Quick Setup
This project provides a Docker Compose file that can be used to quickly set up the application. 


1. download docker desktop [here](https://www.docker.com/products/docker-desktop/).
2. install docker desktop ([Window](https://docs.docker.com/desktop/setup/install/windows-install/)/[Mac](https://docs.docker.com/desktop/setup/install/mac-install/))

    Verify installation
    ```cmd
    docker -v
    Docker version 28.5.2, build ecc6942

    docker-compose -v
    Docker Compose version v2.40.3-desktop.1
    ```
3. run project.

    ```
    git clone https://github.com/wangran326990/company_project.git
    ```
    ```
    cd company_project
    ```
    
    ```
    docker compose up -d
    [+] Running 2/2
    ✔ Container mysql8              Healthy                                                                          10.7s
    ✔ Container transaction-report-app  Started                                                                           0.2s
    ```
    open brower visit
    ```
    http://localhost:8080
    ```

## Dev Enviornment Setup
This section provides details on how to set up the development environment for this project.

### Java Setup 
1. download java from [here](https://www.oracle.com/asean/java/technologies/javase/javase8-archive-downloads.html).
2. Run the installer.
3. Add Java to the PATH the steps can be find [here](https://docs.oracle.com/cd/F74770_01/English/Installing/p6_eppm_install_config/89522.htm).

Verify installation:

```bash
java -version
```

```
openjdk version "1.8.0_502"
OpenJDK Runtime Environment Corretto-8.502.07.1 (build 1.8.0_502-b07)
OpenJDK 64-Bit Server VM Corretto-8.502.07.1 (build 25.502-b07, mixed mode)
```

### Maven Setup
1. download maven from [here](https://maven.apache.org/docs/3.8.8/release-notes.html).

2. unzip it and details of how to install can be find [here](https://maven.apache.org/install.html).


### MySQL Setup (Optional/Can simply use Docker MySQL 8)
1. download [MySQL8](https://www.mysql.com/downloads/)
2. make use no other application use port 3306 

    Windows
    ```
    netstat -ano | findstr :3306
    ```

    Mac
    ```
    sudo lsof -i tcp:3306
    ```
3. install mysql([Window](https://dev.mysql.com/downloads/installer/)/[Mac](https://dev.mysql.com/doc/refman/8.4/en/macos-installation.html))
4. The username/password need to be set to root/root. otherwise few hardcoded place need to be changed 





