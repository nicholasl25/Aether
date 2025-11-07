package com.physics.simulations.gravity;

import com.physics.simulations.BaseSimulation;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

/**
 * Gravity Simulation - Multiple planets interacting through gravitational forces
 */
public class GravitySimulation extends BaseSimulation {
    /** List of all planets/point masses in the simulation */
    private List<Planet> planets = new ArrayList<>();
     private List<PointMass> masses = new ArrayList<>();
    
    /** Gravitational constant (scaled for visualization - not real-world value) */
    private double gravitationalConstant = 6000.0;
    
    /** Animation timer - calls update() repeatedly */
    private Timer animationTimer;
    
    /** Time step for physics calculations (in seconds) */
    private static final double DELTA_TIME = 1.0 / 60.0; // 60 FPS
    
    /** Drawing panel - custom component for rendering */
    private DrawingPanel drawingPanel;

    /** Zoom factor that controls the scale of the simulation */
    private double zoomFactor = 1.0;
    private double panLevelX = 0.0;
    private double panLevelY = 0.0;

    private double minZoom = 0.1;
    private double maxZoom = 10.0;

    /** Mouse drag tracking */
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private boolean isDragging = false;
    
    /** Pause state */
    private boolean isPaused = false;
    
    /**
     * Sets up the simulation window and creates initial planets.
     */
    @Override
    public void initialize() {
        // Set window properties
        setTitle("Gravity Simulation");
        setSize(1000, 800);
        setLocationRelativeTo(null); // Center window on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Initialize the list of planets
        planets = new ArrayList<>();
        masses = new ArrayList<>();
        
        // Create a custom drawing panel to handle rendering
        // We'll override its paintComponent() method to draw our planets
        drawingPanel = new DrawingPanel();
        setupMouseListeners();
        setupKeyListener();
        add(drawingPanel);
        
        // Make sure the panel can receive focus for keyboard events
        drawingPanel.setFocusable(true);
        drawingPanel.requestFocus();
        
        // Create initial planets
        setupPlanets();
        setupMasses();
    }
    
    /**
     * Creates large mass at the center of the screen and a smaller mass orbiting it.
     */
    private void setupPlanets() {
        Planet sun = new Planet(
            1000.0,                   
            20.0,                      
            500.0, 400.0,        
            0.0, 0.0,              
            Color.YELLOW
        );


        Planet planet1 = new Planet(
            50.0,                     
            10.0,                 
            700.0, 400.0,
            0.0, -80.0,
            Color.BLUE
        );
        
        planets.add(sun);
        planets.add(planet1);
    }

    private void setupMasses() {
        PointMass mass = new PointMass(500, 500, 500);
        masses.add(mass);
    }
    
    /**
     * Sets up mouse listeners for zoom and pan
     */
    private void setupMouseListeners() {
        // Mouse wheel listener for zoom
        drawingPanel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                double zoomChange = 1.1;
                
                if (notches > 0) {
                    // Scroll down - zoom out
                    zoomFactor /= zoomChange;
                } else {
                    // Scroll up - zoom in
                    zoomFactor *= zoomChange;
                }

                zoomFactor = Math.max(minZoom, Math.min(maxZoom, zoomFactor));
                repaint();
            }
        });
        
        // Mouse listeners for drag-to-pan
        drawingPanel.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Left mouse button
                    isDragging = true;
                    lastMouseX = e.getX();
                    lastMouseY = e.getY();
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    isDragging = false;
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {}
            
            @Override
            public void mouseEntered(MouseEvent e) {}
            
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        
        // Mouse motion listener for drag
        drawingPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) {
                    int currentX = e.getX();
                    int currentY = e.getY();
                    
                    // Calculate delta (difference in mouse position)
                    int deltaX = currentX - lastMouseX;
                    int deltaY = currentY - lastMouseY;
                    
                    // Update pan (adjust for zoom level so panning feels consistent)
                    // Positive to move the view in the direction of the drag
                    panLevelX += deltaX / zoomFactor;
                    panLevelY += deltaY / zoomFactor;
                    
                    // Update last mouse position
                    lastMouseX = currentX;
                    lastMouseY = currentY;
                    
                    repaint();
                }
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {}
        });
    }
    
    /**
     * Sets up key listener for pause/resume
     */
    private void setupKeyListener() {
        drawingPanel.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    isPaused = !isPaused;
                    repaint();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {}
            
            @Override
            public void keyTyped(KeyEvent e) {}
        });
    }
    
    
    // ============================================
    // UPDATE METHOD
    // ============================================
    /**
     * Updates the physics simulation by one time at a time
     * 
     * @param deltaTime Time elapsed since last update (in seconds)
     */
    @Override
    public void update(double deltaTime) {
        // Don't update physics if paused
        if (isPaused) {
            return;
        }
        
        List<Planet> toAdd = new ArrayList<>();
        List<Planet> toRemove = new ArrayList<>();

        for (Planet planet : planets) {
            // Initialize total force components
            double totalForceX = 0.0;
            double totalForceY = 0.0;

            for (Planet other : planets) {
                if (planet == other) continue;

                // Check for collisions
                if (planet.collidesWith(other)) {
                    Planet merged = planet.handleCollision(other);
                    toAdd.add(merged);       // new merged planet to add later
                    toRemove.add(planet);    // both old ones should be removed
                    toRemove.add(other);
                    break;                   // stop computing further for this planet
                }

                // Compute gravitational force
                double[] force = planet.gravitationalForceFrom(other, gravitationalConstant);
                totalForceX += force[0];
                totalForceY += force[1];
            }

            for (PointMass mass : masses) {
                if (mass.collidesWith(planet)) {
                    mass.handleCollision(planet);
                    toRemove.add(planet);          // planet should be removed
                    break;                         // stop computing further for this planet
                }

                double[] force = mass.gravitationalForceFrom(planet, gravitationalConstant);
                // Equal and opposite forces but pointmasses don't move
                totalForceX -= force[0];
                totalForceY -= force[1];
            }

            // Skip velocity update if planet is set to be removed
            if (toRemove.contains(planet)) continue;

            // Newton's second law: F = ma → a = F/m
            double accelerationX = totalForceX / planet.mass;
            double accelerationY = totalForceY / planet.mass;

            planet.updateVelocity(accelerationX, accelerationY, deltaTime);
        }

        // Apply removals and additions safely after iteration
        planets.removeAll(toRemove);
        planets.addAll(toAdd);
        
        // Update positions based on velocities (after all velocities are updated)
        for (Planet planet : planets) {
            planet.updatePosition(deltaTime);
        }
    }
    
    
    @Override
    public void render() {
        repaint();
    }
    
    
    @Override
    public void start() {
        initialize();
    
        // 1000ms / 60 FPS ≈ 16.67ms per frame
        animationTimer = new Timer(16, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update(DELTA_TIME);
                render();
            }
        });
        
        setVisible(true);
        
        // Start the animation timer
        animationTimer.start();
    }
    
    @Override
    public void stop() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
        dispose();
    }
    
    

    private class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Important! Clears previous frame
            
            // Cast Graphics to Graphics2D for better drawing capabilities
            Graphics2D g2d = (Graphics2D) g;
            
            // Enable anti-aliasing - makes edges smoother (less pixelated)
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                 RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw dark space background
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Save the original transform for text drawing
            AffineTransform originalTransform = g2d.getTransform();
            
            // Apply transformations: translate to center, pan, then zoom
            g2d.translate(0.0, 0.0);
            g2d.translate(panLevelX, panLevelY);
            g2d.scale(zoomFactor, zoomFactor);
            
            // Draw grid background for position reference
            drawGrid(g2d);
            
            // Draw all planets
            if (planets != null) {
                for (Planet planet : planets) {
                    planet.draw(g2d);
                }
            }
            
            // Draw all point masses
            if (masses != null) {
                for (PointMass mass : masses) {
                    mass.draw(g2d);
                }
            }
            
            // Restore original transform for text (so it's not zoomed/panned)
            g2d.setTransform(originalTransform);
            
            // Draw info text (always at same screen position, not affected by zoom/pan)
            g2d.setColor(Color.WHITE);
            g2d.drawString("Planets: " + (planets != null ? planets.size() : 0), 10, 20);
            g2d.drawString("Point Masses: " + (masses != null ? masses.size() : 0), 10, 35);
            g2d.drawString("G = " + gravitationalConstant, 10, 50);
            g2d.drawString(String.format("Zoom: %.2fx", zoomFactor), 10, 65);
            if (isPaused) {
                g2d.setColor(Color.YELLOW);
                g2d.drawString("PAUSED - Press SPACE to resume", 10, 80);
            }
            g2d.setColor(Color.WHITE);
            g2d.drawString("Controls: Mouse wheel = zoom, Drag = pan, SPACE = pause/resume", 10, getHeight() - 10);
        }
        
        /**
         * Draws a grid background to provide visual position reference.
         * The grid scales with zoom and pans with the view.
         */
        private void drawGrid(Graphics2D g2d) {
            // Grid spacing (in world coordinates)
            int gridSpacing = 100;
            int majorGridSpacing = 500;
            
            // Get the visible area bounds (approximate based on screen size and zoom)
            double visibleWidth = getWidth() / zoomFactor;
            double visibleHeight = getHeight() / zoomFactor;
            double centerX = -panLevelX;
            double centerY = -panLevelY;
            
            double startX = centerX - visibleWidth / 2;
            double endX = centerX + visibleWidth / 2;
            double startY = centerY - visibleHeight / 2;
            double endY = centerY + visibleHeight / 2;
            
            // Draw minor grid lines (lighter)
            g2d.setColor(new Color(30, 30, 30)); // Dark gray
            g2d.setStroke(new BasicStroke(0.5f));
            
            // Vertical lines
            for (int x = (int)(startX / gridSpacing) * gridSpacing; x <= endX; x += gridSpacing) {
                g2d.drawLine(x, (int)startY, x, (int)endY);
            }
            
            // Horizontal lines
            for (int y = (int)(startY / gridSpacing) * gridSpacing; y <= endY; y += gridSpacing) {
                g2d.drawLine((int)startX, y, (int)endX, y);
            }
            
            // Draw major grid lines (slightly brighter)
            g2d.setColor(new Color(50, 50, 50));
            g2d.setStroke(new BasicStroke(1.0f));
            
            // Vertical major lines
            for (int x = (int)(startX / majorGridSpacing) * majorGridSpacing; x <= endX; x += majorGridSpacing) {
                g2d.drawLine(x, (int)startY, x, (int)endY);
            }
            
            // Horizontal major lines
            for (int y = (int)(startY / majorGridSpacing) * majorGridSpacing; y <= endY; y += majorGridSpacing) {
                g2d.drawLine((int)startX, y, (int)endX, y);
            }
            
            // Draw center axes (brighter)
            g2d.setColor(new Color(80, 80, 80));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawLine(0, (int)startY, 0, (int)endY); // Vertical center line
            g2d.drawLine((int)startX, 0, (int)endX, 0); // Horizontal center line
        }
    }
}
