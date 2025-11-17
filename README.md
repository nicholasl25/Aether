# PhysicsSandbox

A Java-based physics simulation application featuring realistic 2D gravity interactions.

## Features

### 2D Gravity Simulation
- **Realistic Physics**: N-body gravitational simulation with collision detection
- **Textured Rotating Planets**: Add planets with real NASA textures that rotate as they move
- **Interactive Controls**: 
  - Click to set spawn position (red X marker)
  - Drag to pan the view
  - Spacebar to pause/resume
  - Select planets to view their properties
- **Stationary Masses**: Add immovable massive objects for interesting orbital dynamics
- **Adjustable Gravity**: Real-time gravity constant slider (0-10000)
- **Clean Minimalist UI**: Modern sidebar with Add and Settings tabs

## How to Run

```bash
./run.sh
```

## Controls

- **Click**: Set position for next object (shows red X)
- **Drag**: Pan the simulation view
- **Spacebar**: Pause/Resume simulation
- **Click on Planet**: Select and view planet properties

## Adding Objects

### Add Tab
1. Set Mass, Radius, VX (velocity X), VY (velocity Y)
2. Choose Color
3. Choose Texture (optional):
   - **None**: Solid color planet
   - **Earth**: Blue marble
   - **Mars**: Red planet
   - **Jupiter**: Gas giant
   - **Moon**: Gray rocky surface
   - **Sun**: Yellow star
   - **Venus**: Orange/tan surface
4. Click "Add Planet" or "Add Stationary Mass"

### Settings Tab
- Adjust Gravitational Constant with slider

## Planet Textures

Planets can use realistic NASA textures that rotate as they move through space. Textures are located in `resources/textures/` and include Earth, Mars, Jupiter, Moon, Sun, and Venus.

For more information about textures, see `resources/textures/README.md`.

## Technologies

- Java Swing for GUI
- Custom physics engine with N-body simulation
- Image rotation and rendering with Graphics2D
- Real-time adjustable parameters

## Project Structure

```
Physics-/
├── src/com/physics/simulations/
│   ├── Main.java                    # Entry point
│   ├── PhysicsSimulationsApp.java   # Homepage
│   ├── BaseSimulation.java          # Simulation base class
│   └── gravity/
│       ├── GravitySimulation.java   # Main simulation
│       ├── ControlPanel.java        # UI controls
│       ├── Planet.java              # Planet with texture support
│       └── PointMass.java           # Stationary mass
├── resources/textures/              # Planet texture images
├── out/                             # Compiled classes
└── run.sh                           # Run script
```

