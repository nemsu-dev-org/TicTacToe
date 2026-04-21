<div align="center">

# TicTacToe

[![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)](#)
[![Platform](https://img.shields.io/badge/platform-Android-blueviolet.svg)](#)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](#)

**A classic Tic-Tac-Toe game for Android with Single-Player AI and Two-Player modes.**

</div>

---

## Features

TicTacToe provides a fun and simple game environment designed for both casual play and a challenging AI opponent.  
Built to be lightweight, interactive, and entertaining.

- **Single Player Mode:** Play against a smart AI that features Easy and Hard difficulties (using the Minimax algorithm).
- **Two Player Mode:** Play against a friend on the same device offline.
- **Sound Effects & Feedback:** Enjoy crisp sound effects for wins, losses, ties, and general gameplay clicks.
- **Responsive Layout:** Adapts seamlessly across Android devices using standard XML constraints.

---

## Installation Guide

You can install TicTacToe either by downloading the pre-compiled APK or by building it from the source code.

### Option 1: Install via APK

1. Download `TicTacToe-v1.0.apk` from the Releases / Assets section.
2. Tap the downloaded file and allow installation from "Unknown sources" if prompted on your Android device.
3. Open **TicTacToe** from your app drawer to start playing!

---

### Option 2: Build from Source

#### Prerequisites

- **Android Studio**
- **Android SDK** (API 24 or higher recommended)

---

#### Step 1: Get the Code

```bash
git clone https://github.com/Huerte/TicTacToe.git
cd TicTacToe
```

---

### Step 2: Build & Run

Open the project in Android Studio, connect your emulator or Android device, and run:

```bash
./gradlew assembleDebug
```
Or simply click the **Run** button in Android Studio.

---

## Usage

1. Open the **TicTacToe** app on your Android device.
2. Select between **One Player** or **Two Player** mode.
3. If playing One Player, choose your difficulty (Easy or Hard).
4. Tap the tiles to place your symbol (X or O) and try to connect three in a row!

---

## Project Structure

```
TicTacToe/
│
├── app/src/main/java/com/example/tictactoe/ # Core logic & Activities
├── app/src/main/res/                        # UI Layouts, Colors, and Sound Assets
├── build.gradle.kts                         # Build configuration
└── README.md
```

---

## Contributing

1. Fork the Project
2. Create a Feature Branch
3. Commit Changes
4. Push to Branch
5. Open Pull Request

---

## License

Distributed under the MIT License. See `LICENSE` for details.

---

&copy; 2026 Huerte. All Rights Reserved.
