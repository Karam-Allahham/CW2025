COMP2042 Coursework

Name: Karam Allahham
Student ID: 20702950
Github repository: https://github.com/Karam-Allahham/CW2025.git

------

# Overview

This project is a fork of the given Tetris re-implementation for the COMP2042 coursework. 

My submission aims to restructure the codebase for improved clarity and maintainability, introduce gameplay features, incorporate unit tests, and deliver necessary
documentation (Javadoc and Design diagram) along with a brief demo.

#Maintenance

Refactor: extract BoardRenderer and InputHandler from GuiController which improves SRP and simplifies testing

Refactor: extract GameLoop from GuiController to separate timing responsibilities from UI code which improves SRP and simplifies testing

Fix: Pause button now toggles game state
- Add pause check to game loop to stop processing when paused
- Implement pauseGame() to toggle isPause state
- Add P key binding in InputHandler to trigger pause

Fix: Implement score display functionality
- Add scorelabel FXML field to GuiController
- Implement bindScore() to bind score property to label
- Add Label element to gameLayout.fxml with scoreClass styling

Fix: Added missing lower Y bound check in MatrixOperations
- Added targetY >= 0 check to prevent negative Y values from bypassing bounds validation in checkOutOfBound method
