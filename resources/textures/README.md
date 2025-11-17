# Planet Textures

This directory contains planet texture images for the PhysicsSandbox gravity simulation.

## Required Texture Files

Place planet texture images (JPG format) in this directory with these exact names:

- `Earth.jpg` - Earth texture
- `Mars.jpg` - Mars texture  
- `Jupiter.jpg` - Jupiter texture
- `Moon.jpg` - Moon texture
- `Sun.jpg` - Sun texture
- `Venus.jpg` - Venus texture

## Where to Get Textures

### Free High-Quality Sources:

1. **NASA/Solar System Scope**
   - https://www.solarsystemscope.com/textures/
   - Free, high-resolution planet textures

2. **Planet Pixel Emporium**
   - http://planetpixelemporium.com/planets.html
   - Various resolutions available

3. **NASA's Visible Earth**
   - https://visibleearth.nasa.gov/
   - Real NASA satellite imagery

## Image Requirements

- **Format:** JPG (JPEG)
- **Recommended Size:** 512x512 to 2048x2048 pixels
- **Aspect Ratio:** Square (1:1)
- **Note:** Images will be automatically scaled to match planet radius

## How It Works

1. Select a texture from the dropdown in the "Add" tab
2. Create a planet - it will use the selected texture
3. Planets with textures will rotate as they move
4. If texture file is missing, planet will use solid color as fallback

## Tips

- Larger textures (1024x1024+) look better but use more memory
- Smaller textures (512x512) are faster for many planets
- Textures are pre-scaled when loaded for better performance
- Each planet can have a different texture

