# TP Car rental 

## Architecture

```
.
├── HELP.md
├── LICENSE
├── assets                                      // Dossier contenant des screenshots des tests et analyse SonarQube
│   ├── all-tests-passed.png
│   ├── sonar-code-coverage.png
│   └── sonar-overview.png
├── docker-compose.yml                          // Fichier docker-compose pour le container SonarQube
├── mvnw
├── mvnw.cmd
├── pom.xml
src/main
├── java
│   └── fr
│       └── gelk
│           └── carrental
│               ├── CarRentalApplication.java
│               ├── controllers
│               │   └── CarController.java
│               ├── models
│               │   └── Car.java
│               ├── repositories
│               │   └── CarRepository.java
│               └── services
│                   └── CarRentalService.java
```
