package com.physics.simulations.gravity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Control panel for the gravity simulation.
 * Handles user input for adding planets and point masses.
 */
public class ControlPanel extends JPanel {
    // Input fields
    private JTextField massField, radiusField, vxField, vyField, periodField;
    private JComboBox<String> colorCombo, textureCombo;
    private JSlider gravitySlider, timeFactorSlider;
    private JPanel advancedPanel;
    private boolean advancedExpanded = false;
    
    // Callbacks
    private Runnable onAddPlanet;
    private Runnable onClearSimulation;
    private java.util.function.Consumer<Double> onGravityChanged;
    private java.util.function.Consumer<Double> onTimeFactorChanged;
    
    /**
     * Creates a new control panel with the specified callbacks.
     * 
     * @param onAddPlanet Called when "Add Planet" button is clicked
     * @param onClearSimulation Called when "Clear Simulation" button is clicked
     * @param onGravityChanged Called when gravity slider changes
     * @param onTimeFactorChanged Called when time factor slider changes
     */
    public ControlPanel(Runnable onAddPlanet, Runnable onClearSimulation, 
                       java.util.function.Consumer<Double> onGravityChanged,
                       java.util.function.Consumer<Double> onTimeFactorChanged) {
        this.onAddPlanet = onAddPlanet;
        this.onClearSimulation = onClearSimulation;
        this.onGravityChanged = onGravityChanged;
        this.onTimeFactorChanged = onTimeFactorChanged;
        
        setupPanel();
    }
    
    /**
     * Sets up the control panel UI.
     */
    private void setupPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(50, 50, 50));
        
        // Set preferred width
        setPreferredSize(new Dimension(280, 0));
        
        // Create tabbed pane for Add and Settings
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(50, 50, 50));
        tabbedPane.setForeground(Color.WHITE);
        
        // Add tab
        JPanel addPanel = createAddPanel();
        tabbedPane.addTab("Add", addPanel);
        
        // Settings tab
        JPanel settingsPanel = createSettingsPanel();
        tabbedPane.addTab("Settings", settingsPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Clear button at bottom
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(new Color(50, 50, 50));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton clearSimulationButton = new JButton("Clear Simulation");
        clearSimulationButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, clearSimulationButton.getPreferredSize().height));
        clearSimulationButton.setForeground(Color.RED);
        clearSimulationButton.addActionListener(e -> {
            if (onClearSimulation != null) {
                onClearSimulation.run();
            }
        });
        removeSpacebarActivation(clearSimulationButton);
        bottomPanel.add(clearSimulationButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates the Add panel.
     */
    private JPanel createAddPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(50, 50, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Mass
        JLabel massLabel = new JLabel("Mass:");
        massLabel.setForeground(Color.WHITE);
        panel.add(massLabel);
        massField = new JTextField("50.0");
        massField.setMaximumSize(new Dimension(Integer.MAX_VALUE, massField.getPreferredSize().height));
        panel.add(massField);
        panel.add(Box.createVerticalStrut(5));
        
        // Radius
        JLabel radiusLabel = new JLabel("Radius:");
        radiusLabel.setForeground(Color.WHITE);
        panel.add(radiusLabel);
        radiusField = new JTextField("10.0");
        radiusField.setMaximumSize(new Dimension(Integer.MAX_VALUE, radiusField.getPreferredSize().height));
        panel.add(radiusField);
        panel.add(Box.createVerticalStrut(5));
        
        // VX
        JLabel vxLabel = new JLabel("VX:");
        vxLabel.setForeground(Color.WHITE);
        panel.add(vxLabel);
        vxField = new JTextField("0.0");
        vxField.setMaximumSize(new Dimension(Integer.MAX_VALUE, vxField.getPreferredSize().height));
        panel.add(vxField);
        panel.add(Box.createVerticalStrut(5));
        
        // VY
        JLabel vyLabel = new JLabel("VY:");
        vyLabel.setForeground(Color.WHITE);
        panel.add(vyLabel);
        vyField = new JTextField("0.0");
        vyField.setMaximumSize(new Dimension(Integer.MAX_VALUE, vyField.getPreferredSize().height));
        panel.add(vyField);
        panel.add(Box.createVerticalStrut(5));
        
        // Color
        JLabel colorLabel = new JLabel("Color:");
        colorLabel.setForeground(Color.WHITE);
        panel.add(colorLabel);
        colorCombo = new JComboBox<>(new String[]{
            "Blue", "Red", "Green", "Yellow", "Orange", "Purple", "Cyan", "White"
        });
        colorCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, colorCombo.getPreferredSize().height));
        panel.add(colorCombo);
        panel.add(Box.createVerticalStrut(5));
        
        // Texture
        JLabel textureLabel = new JLabel("Texture:");
        textureLabel.setForeground(Color.WHITE);
        panel.add(textureLabel);
        textureCombo = new JComboBox<>(new String[]{
            "None (Solid Color)", "Earth", "Mars", "Jupiter", "Moon", "Sun", "Venus"
        });
        textureCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, textureCombo.getPreferredSize().height));
        panel.add(textureCombo);
        panel.add(Box.createVerticalStrut(10));
        
        // Advanced Settings collapsible section
        JPanel advancedHeader = new JPanel();
        advancedHeader.setLayout(new BorderLayout());
        advancedHeader.setBackground(new Color(60, 60, 60));
        advancedHeader.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        advancedHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel advancedLabel = new JLabel("▶ Advanced Settings");
        advancedLabel.setForeground(Color.LIGHT_GRAY);
        advancedLabel.setFont(new Font("Sans-serif", Font.BOLD, 11));
        advancedHeader.add(advancedLabel, BorderLayout.WEST);
        
        // Make the header clickable
        advancedHeader.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        advancedHeader.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                advancedExpanded = !advancedExpanded;
                advancedLabel.setText(advancedExpanded ? "▼ Advanced Settings" : "▶ Advanced Settings");
                advancedPanel.setVisible(advancedExpanded);
                panel.revalidate();
                panel.repaint();
            }
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                advancedHeader.setBackground(new Color(70, 70, 70));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                advancedHeader.setBackground(new Color(60, 60, 60));
            }
        });
        
        panel.add(advancedHeader);
        
        // Advanced settings content panel
        advancedPanel = new JPanel();
        advancedPanel.setLayout(new BoxLayout(advancedPanel, BoxLayout.Y_AXIS));
        advancedPanel.setBackground(new Color(50, 50, 50));
        advancedPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        advancedPanel.setVisible(false);
        
        // Period of Rotation field
        JLabel periodLabel = new JLabel("Period of Rotation (T):");
        periodLabel.setForeground(Color.WHITE);
        periodLabel.setFont(new Font("Sans-serif", Font.PLAIN, 11));
        advancedPanel.add(periodLabel);
        
        periodField = new JTextField("100.0");
        periodField.setMaximumSize(new Dimension(Integer.MAX_VALUE, periodField.getPreferredSize().height));
        periodField.setToolTipText("Time for one complete rotation (seconds)");
        advancedPanel.add(periodField);
        
        panel.add(advancedPanel);
        panel.add(Box.createVerticalStrut(10));
        
        // Add Planet button
        JButton addPlanetButton = new JButton("Add Planet");
        addPlanetButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, addPlanetButton.getPreferredSize().height));
        addPlanetButton.addActionListener(e -> {
            if (onAddPlanet != null) {
                onAddPlanet.run();
            }
        });
        removeSpacebarActivation(addPlanetButton);
        panel.add(addPlanetButton);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    /**
     * Creates the Settings panel.
     */
    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(50, 50, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Gravity slider
        JLabel gLabel = new JLabel("Gravitational Constant (G):");
        gLabel.setForeground(Color.WHITE);
        panel.add(gLabel);
        
        gravitySlider = new JSlider(0, 20000, 6000);
        gravitySlider.setMaximumSize(new Dimension(Integer.MAX_VALUE, gravitySlider.getPreferredSize().height));
        gravitySlider.setBackground(new Color(50, 50, 50));
        gravitySlider.setForeground(Color.WHITE);
        gravitySlider.addChangeListener(e -> {
            if (onGravityChanged != null) {
                onGravityChanged.accept((double) gravitySlider.getValue());
            }
        });
        panel.add(gravitySlider);
        
        JLabel gValueLabel = new JLabel("G = 6000");
        gValueLabel.setForeground(Color.LIGHT_GRAY);
        gValueLabel.setFont(new Font("Sans-serif", Font.PLAIN, 11));
        gravitySlider.addChangeListener(e -> {
            gValueLabel.setText("G = " + gravitySlider.getValue());
        });
        panel.add(gValueLabel);
        
        panel.add(Box.createVerticalStrut(15));
        
        // Time Factor slider
        JLabel timeFactorLabel = new JLabel("Time Factor:");
        timeFactorLabel.setForeground(Color.WHITE);
        panel.add(timeFactorLabel);
        
        timeFactorSlider = new JSlider(1, 100, 10); // 0.1 to 10.0 (scaled by 10)
        timeFactorSlider.setMaximumSize(new Dimension(Integer.MAX_VALUE, timeFactorSlider.getPreferredSize().height));
        timeFactorSlider.setBackground(new Color(50, 50, 50));
        timeFactorSlider.setForeground(Color.WHITE);
        timeFactorSlider.addChangeListener(e -> {
            if (onTimeFactorChanged != null) {
                double timeFactor = timeFactorSlider.getValue() / 10.0;
                onTimeFactorChanged.accept(timeFactor);
            }
        });
        panel.add(timeFactorSlider);
        
        JLabel timeFactorValueLabel = new JLabel("1.0 s/s");
        timeFactorValueLabel.setForeground(Color.LIGHT_GRAY);
        timeFactorValueLabel.setFont(new Font("Sans-serif", Font.PLAIN, 11));
        timeFactorSlider.addChangeListener(e -> {
            double timeFactor = timeFactorSlider.getValue() / 10.0;
            timeFactorValueLabel.setText(String.format("%.1f s/s", timeFactor));
        });
        panel.add(timeFactorValueLabel);
        
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
            double mass = Double.parseDouble(massField.getText());
            double radius = Double.parseDouble(radiusField.getText());
            double vx = Double.parseDouble(vxField.getText());
            double vy = Double.parseDouble(vyField.getText());
            double period = Double.parseDouble(periodField.getText());
            Color color = getColorFromCombo(colorCombo);
            String texturePath = getTexturePath();
            
            return new PlanetData(mass, radius, vx, vy, period, color, texturePath);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Gets the texture path based on selection
     */
    private String getTexturePath() {
        String selected = (String) textureCombo.getSelectedItem();
        if (selected == null || selected.equals("None (Solid Color)")) {
            return null;
        }
        return "resources/textures/" + selected + ".jpg";
    }
    
    /**
     * Gets the selected color from the combo box.
     */
    private Color getColorFromCombo(JComboBox<String> combo) {
        if (combo == null || combo.getSelectedItem() == null) return Color.BLUE;
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
        public final double period;  // Period of rotation in seconds
        public final Color color;
        public final String texturePath;
        
        public PlanetData(double mass, double radius, double vx, double vy, double period, Color color, String texturePath) {
            this.mass = mass;
            this.radius = radius;
            this.vx = vx;
            this.vy = vy;
            this.period = period;
            this.color = color;
            this.texturePath = texturePath;
        }
        
        /**
         * Calculates angular velocity from the period of rotation.
         * Formula: ω = 2π / T
         * 
         * @return Angular velocity in radians per second
         */
        public double getAngularVelocity() {
            if (period <= 0) {
                return 0.0;
            }
            return (2.0 * Math.PI) / period;
        }
    }
    
}

