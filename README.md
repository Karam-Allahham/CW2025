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

Fix: Correct brick spawn position to top of board
- Changed brick Y spwan position from 10 to 0 to make sure it spawns at the top instead of halfway down

Fix: Refresh the brick preview when starting a new game
- Added call to refresh the brick preview after creating a new game so it no shows the new brick instead of the old one

Fix: Removing broken InputHandler recreation in setEventListener
- Removed code that created new InputHandler without setting up callbacks, which would break keyboard input handling

Fix: Remove unused code from GuiController
- Remove unused BRICK_SIZE constant
- Remove unused Reflection effect that was never applied
- Remove unused Reflection import

Refactor: Extract magic numbers to named constants
- Added SPAWN_X, SPAWN_Y in SimpleBoard
- Added BOARD_ROWS, BOARD_COLS in GameController
- Added GAME_TICK_MS in GuiController
- Added BASE_SCORE_PER_LINE in MatrixOperations

Refactor: Implement State Pattern for game states
- Create GameState interface with canProcessGameTick() and canAcceptInput()
- Added PlayingState, PausedState and GameOverState implementations
- Replaced isPause and isGameOver boolean flags with state pattern
- Improves code organization and extensibility

Refactor: Implement Factory Pattern for brick creation
- Created BrickFactory class with createBrick() and createRandomBrick()
- Add BrickType enum for type-safe brick identification
- Update RandomBrickGenerator to use BrickFactory
- Centralizes brick instantiation for better maintainability

Refactor: Completed package reorganization
- controller / GameController, GuiController
- model / Board, SimpleBoard, Score, ClearRow, BrickRotator, NextShapeInfo
- view / ViewData, BoardRenderer, GameOverPanel, NotificationPanel
- event / MoveEvent, EventType, EventSource, DownData, InputEventListener
- input / InputHandler
- util / MatrixOperations
- Updated all imports and FXML references
  
Fix: Return copy of matrix board to preserve encapsulation
- getBoardMatrix() now returns a defensive copy instead of the direct reference which prevents external modification of game state


#Tests

Test 1: Added unit tests for Score class

Test 2: Added unit tests for BrickFactory class

Test 3: Added unit tests for MatrixOperations class

Test 4: Added unit tests for State Pattern implementation

#Additional features

Feature: Display game state and added New Game button
- Added statelabel to show current game state on screen
- Added New Game button to UI
- State updates when pausing, resuming, game over and new game
