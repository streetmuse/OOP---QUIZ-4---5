# REST Blog Platform Chat Bot

A Java console application that communicates with a REST API to manage blog posts.

## Features

- ✅ Configurable REST server URL via text file
- ✅ Configurable bot name via text file
- ✅ User-friendly console interface
- ✅ Create new blog posts
- ✅ View all blog posts
- ✅ View site statistics
- ✅ Full REST API integration

## Files Structure

```
├── ChatBot.java       # Main application
├── config.txt         # Configuration file
├── pom.xml            # Maven dependencies
└── README.md          # This file
```

## Configuration

The `config.txt` file contains:
- `server.url`: REST API endpoint URL
- `bot.name`: Name displayed during chat interactions

## How to Run

### Option 1: With Maven
```bash
mvn compile exec:java
```

### Option 2: Manual Compilation
```bash
# Compile (make sure Jackson library is in classpath)
javac -cp "jackson-databind-2.15.2.jar:jackson-core-2.15.2.jar:jackson-annotations-2.15.2.jar" ChatBot.java

# Run
java -cp ".:jackson-databind-2.15.2.jar:jackson-core-2.15.2.jar:jackson-annotations-2.15.2.jar" ChatBot
```

## API Endpoints Used

- `GET ?api=blogs` - Retrieve all blog posts
- `POST ?api=blogs` - Create new blog post
- `GET ?api=stats` - Get site statistics

## Usage

1. Run the application
2. Choose from the menu options:
    - Create a new blog post (enter title, author, content)
    - View all existing blog posts
    - View site statistics (total posts, remaining posts, etc.)
    - Exit the application

## Assignment Requirements Fulfilled

- [x] Java console application (10 points total)
- [x] REST API communication with max.ge/q45/93746182/index.php
- [x] Configurable URL in text file (1 point)
- [x] Configurable bot name in text file (1 point)
- [x] Convenient user interaction (2 points)
- [x] Create new blog posts (2 points)
- [x] View all blog posts (2 points)
- [x] View general statistics (2 points)

## Notes

- The application uses Java 11+ HTTP client for REST API calls
- JSON parsing is handled by Jackson library
- Error handling is implemented for network issues and API errors
- The bot name appears in all user interactions as specified