# 🕹️ Ghost Hunter Game (Java)

## 📌 Overview

A terminal-based grid game implemented in Java, where the player navigates a board to collect food, avoid ghosts, and manage energy efficiently to win.


## 🎮 Gameplay Features

The game is played on a toroidal 2D board containing multiple elements:

- **Player (`X`)** – Controlled by the user. Starts with initial energy and moves based on keyboard input.
- **Ghosts (`@`)** – Move autonomously toward the player using shortest-path logic (Dijkstra-based).
- **Obstacles (`#`)** – Block player and ghost movement.
- **Food Items**:
  - Vegetable (`v`) – Restores 6 energy
  - Fish (`f`) – Restores 10 energy
  - Meat (`m`) – Restores 14 energy

The board wraps around (top connects to bottom, left to right), adding strategic complexity.

## 🧠 Key Concepts & Techniques

- **Interactive Console Menu**: Load game, view history, debug distances, jump to move, and continue play.
- **Toroidal Movement**: Moving off one edge of the board wraps to the other side.
- **Energy Management**: Player loses 1 energy per move and must eat food to survive.
- **Win Condition**: Player wins by visiting all accessible cells.
- **Loss Conditions**: Energy reaches zero or a ghost catches the player.
- **Jump to Past Moves**: Game state is saved after every round and can be restored from a specific point.
- **JSON File Handling**: Initial board setup and game state saving/loading handled via JSON.


## 📂 Project Structure

- Object-Oriented Programming (OOP)
- Interfaces (`Movable`, `Eatable`)
- Inheritance & Polymorphism (`BoardElement`, subclasses)
- Pathfinding Algorithm (Distance board simulating Dijkstra)
- File I/O and JSON parsing with `org.json`
- Encapsulation of game logic (`Board.java`, `DistBoard.java`, etc.)




