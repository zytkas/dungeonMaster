# 🏰 Dungeon Master

> *Because escaping dungeons is hard enough without enemies chasing you.*

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-007396?style=for-the-badge&logo=java&logoColor=white)
![OOP](https://img.shields.io/badge/OOP-5C2D91?style=for-the-badge&logo=java&logoColor=white)

## 🎮 What is this?

Dungeon Master is my university project for NOVA FCT 24/25 that I created to practice Java programming fundamentals. It's a text-based dungeon crawler where you navigate through a two-level labyrinth, collect treasures, and avoid enemies with unique movement patterns. I later added a GUI because, well, pixels are prettier than text!

**Is this Zelda?** Not even close - but it's my first step into game development with all the fundamental mechanics: player movement, enemy AI, and that sweet feeling of victory when you collect all the treasures and reach the exit.

## 💻 Tech Stack

- **Java** - Because everyone's programming journey starts somewhere
- **Swing** - For the GUI extension I added to make things visual
- **OOP Principles** - Classes, inheritance, encapsulation - the whole Java package
- **Core Java Libraries** - No external dependencies, just pure Java power

## ✨ Features

- 🧭 **Two-Level Dungeon** - Navigate between levels via stairs
- 👾 **Unique Enemies** - Each with their own movement patterns:
  - 🔄 **Looper** - Goes right and wraps around, simple but deadly
  - ⚡ **Zigzagger** - Moves in unpredictable patterns (1-5 steps)
- 💎 **Treasure Collection** - Gotta catch 'em all before the exit works
- 🖥️ **Dual Interface** - Text-based console for purists, GUI for the visually inclined
- 🎲 **Simple Gameplay** - Left, right, and the constant fear of enemy collision

## 🖼️ Visual Experience

The GUI extension visualizes the dungeon with:
- Color-coded cells for different elements
- Real-time game state display
- Interactive controls for player movement
- Visual representation of enemies and player

## 📝 What I Learned

This project was my playground for:

- Building a **complete Java program** from scratch
- Implementing **game state management** without losing track of what's happening
- Creating **enemy AI algorithms** with distinct movement patterns
- Working with **conditional logic** for collision detection and game rules
- Handling **user input** in both console and GUI environments
- Adding a **GUI extension** to practice Swing and visual programming

## 🏗️ Structure

```
DungeonMaster/
├── Main.java          # Entry point and game initialization
├── Game.java          # Core game logic and mechanics
└── GameGUI.java       # The visual extension I added
```

## 🎲 Game Elements

- **P** - Player (that's you!)
- **E** - Exit (your ticket to victory)
- **T** - Treasure (collect these)
- **S** - Stairs (your path between levels)
- **L** - Looper enemy (avoid at all costs)
- **Z** - Zigzagger enemy (even more unpredictable)
- **.** - Empty space (safe to travel)

## 🚀 Getting Started

1. Compile the Java files:
   ```
   javac Main.java GameGUI.java
   ```
2. Run the program:
   ```
   java Main
   ```
3. Input dungeon layouts when prompted (or use these examples):
   - Level 2: `............P....E..........S.............Z.......T.`
   - Level 1: `........L...............S...........T...........E.`
4. Use the GUI controls or text commands to navigate and survive!

## 🧠 How It Works

The core of the game relies on:
- **Character arrays** for dungeon representation
- **Position tracking** for player and enemies
- **Movement algorithms** for different entity types
- **Collision detection** for interactions between entities
- **State management** to track game progress

## ⚠️ Gaming Tips

- Watch those enemy patterns! Loopers are predictable, Zigzaggers not so much.
- Always know where the stairs are - quick level changes can save you.
- Collect ALL treasures before heading to the exit.
- Sometimes waiting for an enemy to pass is the best strategy.

## 📧 Contact

Created by Danylo Zhdanov for NOVA FCT 24/25.

---

*Built with ☕ and Java fundamentals by Danylo Zhdanov*
