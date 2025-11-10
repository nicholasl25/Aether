package com.physics.simulations.gravity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Control panel for the gravity simulation.
 * Handles user input for adding planets and point masses.
 */
public class ControlPanel extends JPanel {
    // Planet fields
    private JTextField planetMassField, planetRadiusField, planetVxField, planetVyField;
    private JComboBox<String> planetColorCombo;
    
    // PointMass fields
    private JTextField pointMassField, pointMassRadiusField;
    private JComboBox<String> pointMassColorCombo;
    
    // Callbacks
    private Runnable onAddPlanet;
    private Runnable onAddPointMass;
    private Runnable onClearSimulation;
    
    /**
     * Creates a new control panel with the specified callbacks.
     * 
     * @param onAddPlanet Called when "Add Planet" button is clicked
     * @param onAddPointMass Called when "Add Point Mass" button is clicked
     * @param onClearSimulation Called when "Clear Simulation" button is clicked
     */
    public ControlPanel(Runnable onAddPlanet, Runnable onAddPointMass, Runnable onClearSimulation) {
        this.onAddPlanet = onAddPlanet;
        this.onAddPointMass = onAddPointMass;
        this.onClearSimulation = onClearSimulation;
        
        setupPanel();
    }
    
    /**
     * Sets up the control panel UI.
     */
    private void setupPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(50, 50, 50));
        
        // Set preferred width
        setPreferredSize(new Dimension(280, 0));
        
        // Create a titled border with white title and padding
        javax.swing.border.TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "User Controls"
        );
        border.setTitleColor(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            border,
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(50, 50, 50));
        tabbedPane.setForeground(Color.WHITE);
        
        // Create Planet tab panel
        JPanel planetPanel = createPlanetPanel();
        tabbedPane.addTab("Planets", planetPanel);
        
        // Create PointMass tab panel
        JPanel pointMassPanel = createPointMassPanel();
        tabbedPane.addTab("PointMasses", pointMassPanel);
        
        add(tabbedPane);
        add(Box.createVerticalStrut(10));
        
        // Clear simulation button (outside tabs)
        JButton clearSimulationButton = new JButton("Clear Simulation");
        clearSimulationButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, clearSimulationButton.getPreferredSize().height));
        clearSimulationButton.setForeground(Color.RED);
        clearSimulationButton.addActionListener(e -> {
            if (onClearSimulation != null) {
                onClearSimulation.run();
            }
        });
        // Remove spacebar activation from button
        removeSpacebarActivation(clearSimulationButton);
        add(clearSimulationButton);
        
        // Add flexible space at bottom
        add(Box.createVerticalGlue());
    }
    
    /**
     * Creates the panel for Planet controls.
     */
    private JPanel createPlanetPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(50, 50, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Mass field
        JLabel massLabel = new JLabel("Mass:");
        massLabel.setForeground(Color.WHITE);
        panel.add(massLabel);
        planetMassField = new JTextField("50.0");
        planetMassField.setMaximumSize(new Dimension(Integer.MAX_VALUE, planetMassField.getPreferredSize().height));
        panel.add(planetMassField);
        panel.add(Box.createVerticalStrut(5));
        
        // Radius field
        JLabel radiusLabel = new JLabel("Radius:");
        radiusLabel.setForeground(Color.WHITE);
        panel.add(radiusLabel);
        planetRadiusField = new JTextField("10.0");
        planetRadiusField.setMaximumSize(new Dimension(Integer.MAX_VALUE, planetRadiusField.getPreferredSize().height));
        panel.add(planetRadiusField);
        panel.add(Box.createVerticalStrut(5));
        
        // Velocity fields
        JLabel vxLabel = new JLabel("VX:");
        vxLabel.setForeground(Color.WHITE);
        panel.add(vxLabel);
        planetVxField = new JTextField("0.0");
        planetVxField.setMaximumSize(new Dimension(Integer.MAX_VALUE, planetVxField.getPreferredSize().height));
        panel.add(planetVxField);
        panel.add(Box.createVerticalStrut(5));
        
        JLabel vyLabel = new JLabel("VY:");
        vyLabel.setForeground(Color.WHITE);
        panel.add(vyLabel);
        planetVyField = new JTextField("0.0");
        planetVyField.setMaximumSize(new Dimension(Integer.MAX_VALUE, planetVyField.getPreferredSize().height));
        panel.add(planetVyField);
        panel.add(Box.createVerticalStrut(5));
        
        // Color dropdown
        JLabel colorLabel = new JLabel("Color:");
        colorLabel.setForeground(Color.WHITE);
        panel.add(colorLabel);
        planetColorCombo = new JComboBox<>(new String[]{
            "Blue", "Red", "Green", "Yellow", "Orange", "Purple", "Cyan", "White"
        });
        planetColorCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, planetColorCombo.getPreferredSize().height));
        panel.add(planetColorCombo);
        panel.add(Box.createVerticalStrut(10));
        
        // Add Planet button
        JButton addPlanetButton = new JButton("Add Planet");
        addPlanetButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, addPlanetButton.getPreferredSize().height));
        addPlanetButton.addActionListener(e -> {
            if (onAddPlanet != null) {
                onAddPlanet.run();
            }
        });
        // Remove spacebar activation from button
        removeSpacebarActivation(addPlanetButton);
        panel.add(addPlanetButton);
        
        // Add flexible space at bottom
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    /**
     * Creates the panel for PointMass controls.
     */
    private JPanel createPointMassPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(50, 50, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Mass field
        JLabel massLabel = new JLabel("Mass:");
        massLabel.setForeground(Color.WHITE);
        panel.add(massLabel);
        pointMassField = new JTextField("500.0");
        pointMassField.setMaximumSize(new Dimension(Integer.MAX_VALUE, pointMassField.getPreferredSize().height));
        panel.add(pointMassField);
        panel.add(Box.createVerticalStrut(5));
        
        // Radius field
        JLabel radiusLabel = new JLabel("Radius:");
        radiusLabel.setForeground(Color.WHITE);
        panel.add(radiusLabel);
        pointMassRadiusField = new JTextField("10.0");
        pointMassRadiusField.setMaximumSize(new Dimension(Integer.MAX_VALUE, pointMassRadiusField.getPreferredSize().height));
        panel.add(pointMassRadiusField);
        panel.add(Box.createVerticalStrut(5));
        
        // Color dropdown
        JLabel colorLabel = new JLabel("Color:");
        colorLabel.setForeground(Color.WHITE);
        panel.add(colorLabel);
        pointMassColorCombo = new JComboBox<>(new String[]{
            "Blue", "Red", "Green", "Yellow", "Orange", "Purple", "Cyan", "White"
        });
        pointMassColorCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, pointMassColorCombo.getPreferredSize().height));
        panel.add(pointMassColorCombo);
        panel.add(Box.createVerticalStrut(10));
        
        // Add Point Mass button
        JButton addPointMassButton = new JButton("Add Point Mass");
        addPointMassButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, addPointMassButton.getPreferredSize().height));
        addPointMassButton.addActionListener(e -> {
            if (onAddPointMass != null) {
                onAddPointMass.run();
            }
        });
        // Remove spacebar activation from button
        removeSpacebarActivation(addPointMassButton);
        panel.add(addPointMassButton);
        
        // Add flexible space at bottom
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    /**
     * Gets planet data from the control fields.
     * 
     * @return PlanetData object containing the values, or null if invalid
     */
    public PlanetData getPlanetData() {
        try {
            double mass = Double.parseDouble(planetMassField.getText());
            double radius = Double.parseDouble(planetRadiusField.getText());
            double vx = Double.parseDouble(planetVxField.getText());
            double vy = Double.parseDouble(planetVyField.getText());
            Color color = getColorFromCombo(planetColorCombo);
            
            return new PlanetData(mass, radius, vx, vy, color);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Gets point mass data from the control fields.
     * 
     * @return PointMassData object containing the values, or null if invalid
     */
    public PointMassData getPointMassData() {
        try {
            double mass = Double.parseDouble(pointMassField.getText());
            double radius = Double.parseDouble(pointMassRadiusField.getText());
            Color color = getColorFromCombo(pointMassColorCombo);
            
            return new PointMassData(mass, radius, color);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Gets the selected color from the combo box.
     */
    private Color getColorFromCombo(JComboBox<String> combo) {
        String colorName = (String) combo.getSelectedItem();
        switch (colorName) {
            case "Blue": return Color.BLUE;
            case "Red": return Color.RED;
            case "Green": return Color.GREEN;
            case "Yellow": return Color.YELLOW;
            case "Orange": return Color.ORANGE;
            case "Purple": return Color.MAGENTA;
            case "Cyan": return Color.CYAN;
            case "White": return Color.WHITE;
            default: return Color.BLUE;
        }
    }
    
    /**
     * Removes spacebar activation from a button.
     * This prevents the button from being activated when spacebar is pressed,
     * ensuring spacebar is reserved for pause/resume in the simulation.
     */
    private void removeSpacebarActivation(JButton button) {
        // Remove spacebar from button's input map
        InputMap inputMap = button.getInputMap(JComponent.WHEN_FOCUSED);
        KeyStroke spaceKey = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);
        inputMap.put(spaceKey, "none");
        
        // Also remove from ancestor input map
        InputMap ancestorMap = button.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        if (ancestorMap != null) {
            ancestorMap.put(spaceKey, "none");
        }
    }
    
    /**
     * Data class for planet creation.
     */
    public static class PlanetData {
        public final double mass;
        public final double radius;
        public final double vx;
        public final double vy;
        public final Color color;
        
        public PlanetData(double mass, double radius, double vx, double vy, Color color) {
            this.mass = mass;
            this.radius = radius;
            this.vx = vx;
            this.vy = vy;
            this.color = color;
        }
    }
    
    /**
     * Data class for point mass creation.
     */
    public static class PointMassData {
        public final double mass;
        public final double radius;
        public final Color color;
        
        public PointMassData(double mass, double radius, Color color) {
            this.mass = mass;
            this.radius = radius;
            this.color = color;
        }
    }
}

