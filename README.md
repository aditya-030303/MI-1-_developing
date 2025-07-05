# Riddle Auth - A Riddle-Based Login and Sharing Platform

Riddle Auth is a fun and secure web application where users log in by solving a riddle. Once logged in, they can create profiles, post their own riddles, and try solving riddles submitted by others.

## What this project does

- Login by answering a riddle instead of using a traditional password
- Create and manage your user profile after successful login
- Add new riddles with answers to the platform
- View and attempt riddles submitted by other users
- See the name of the person who posted each riddle

## Tech stack

- Spring Boot for backend
- Java as the core programming language
- MySQL or H2 for the database
- Thymeleaf for rendering frontend pages

## How to run the project locally

1. Clone the repository  
   git clone https://github.com/yourusername/riddle-auth.git

2. Open the project in your preferred Java IDE (like IntelliJ IDEA or Eclipse)

3. Set up your database (MySQL or H2) and configure the connection in application.properties

4. Run the application  
   You can use your IDE's run button or the command:  
   ./mvnw spring-boot:run

5. Open your browser and go to  
   http://localhost:8080

## Folder structure (basic)

src  
 └── main  
     ├── java  
     │   └── com.example.riddleauth  
     ├── resources  
     │   ├── templates (HTML files)  
     │   ├── static (CSS and JS)  
     │   └── application.properties

## Future improvements

- Add riddle categories or tags
- Allow users to like or comment on riddles
- Include a leaderboard or user scoring system
