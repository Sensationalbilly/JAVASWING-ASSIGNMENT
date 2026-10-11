# PROG209 Java Swing Assignment

Individual assignment for **PROG209 – Event Driven Programming**, Limkokwing University of Creative Technology, Sierra Leone.

- **Student:** Mohamed Barry (BIT2101, Semester 5, Year 3)
- **Lecturer:** Amandus Benjamin Coker
- **Academic Session:** September 2026 – February 2027

## About

A basic interactive desktop GUI built with Java Swing. It demonstrates `JFrame`, `JPanel`, `JLabel`, `ImageIcon`, inheritance with constructor chaining (`BaseWindow` → `SwingAssignment`), and event-driven programming.

## Features

- Welcome label with a "Change Text" button that cycles through preset messages
- A text box — type your own message and press Enter to show it in the label
- "Change Background" button that smoothly fades between preset colours
- "Pick a custom colour…" link that opens a colour chooser, with the label text automatically switching to white on dark backgrounds for readability
- "Reset" link to restore the defaults
- Keyboard shortcuts: Alt+T, Alt+B, Alt+P, Alt+R
- A status line that counts the number of events handled
- Custom application icon loaded with `ImageIcon`

## Project structure

```
src/
  BaseWindow.java       Parent class: shared colours, icon loading, colour utilities
  SwingAssignment.java  Main class: builds the window and wires up the UI events
assets/
  icon.png              Application icon
run.bat                 Compiles and runs the application
```

## Running the application

```
run.bat
```

or manually:

```
javac -d out src/BaseWindow.java src/SwingAssignment.java
java -cp out SwingAssignment
```

## License

MIT — see [LICENSE](LICENSE).
