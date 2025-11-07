package com.physics.simulations.gravity;

import java.awt.Color;
import java.awt.Graphics2D;

public class PointMass {
    
    double mass;
    double x, y;
    Color color;

    double radius;
    Color DEFAULT = Color.WHITE;
   
    public PointMass(double mass, double x, double y) {
        this(mass, x, y, 10, Color.WHITE);
    }

    public PointMass(double mass, double x, double y, double radius, Color color) {
        this.mass = mass;
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public double distanceTo(Planet other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx*dx + dy*dy);
    }

    public double[] gravitationalForceFrom(Planet other, double gravitationalConstant) {
        double distance = distanceTo(other);
        double directionX = (other.x - this.x) / distance;
        double directionY = (other.y - this.y) / distance;

        double forceMagnitude = gravitationalConstant * this.mass * other.mass / (distance * distance);

        double forceX = directionX * forceMagnitude;
        double forceY = directionY * forceMagnitude;


        double[] forceVect = {forceX, forceY};
        return forceVect; 
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        int drawX = (int)(x - radius);
        int drawY = (int)(y - radius);
        int size = (int)(radius * 2);
        g2d.fillOval(drawX, drawY, size, size);
    }

    public boolean collidesWith(Planet other) {
        return distanceTo(other) < (this.radius + other.radius);
    }

    public void handleCollision(Planet other) {
        this.mass += other.mass;

        Color c1 = this.color;
        Color c2 = other.color;
        int r = (c1.getRed() + c2.getRed()) / 2;
        int g = (c1.getGreen() + c2.getGreen()) / 2;
        int b = (c1.getBlue() + c2.getBlue()) / 2;

        Color newColor = new Color(r, g, b);

        this.color = newColor;
    }

}
