package com.aviangames.enginejava;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A library of visual rendering methods that will output to the EngineFrame.
 * 
 * @author Sydney Fonderie
 *
 */
public final class EngineVisuals
{	
	private static final ArrayList<BufferedImage[]> fontList = new ArrayList<BufferedImage[]>();
	
	private static String fontOrder = "";
	
	/**
	 * The width of the EngineVisuals render space.
	 */
	public final int WIDTH;
	
	/**
	 * The height of the EngineVisuals render space.
	 */
	public final int HEIGHT;
	
	private final Graphics2D graphics;
	
	EngineVisuals(Graphics2D gfx, int width, int height)
	{
		this.WIDTH = width;
		this.HEIGHT = height;
		this.graphics = gfx;
	}
	
	//====================================================================================================
	//========================================STATIC METHODS AND FUNCTIONS================================
	//====================================================================================================
	
	/**
	 * Adds the provided font array to the list of usable fonts.
	 * 
	 * @param font The font array to add.
	 * @param red The red tinting of this new font.
	 * @param green The green tinting of this new font.
	 * @param blue The blue tinting of this new font.
	 */
	public static void addFont(BufferedImage[] font, double red, double green, double blue)
	{
		BufferedImage[] modded = new BufferedImage[font.length];
		
		for(int i = 0; i < font.length; i++)
		{
			modded[i] = EngineLibrary.createRecoloredImage(font[i], red, green, blue, 1.0);
		}
		
		EngineVisuals.fontList.add(modded);
	}
	
	/**
	 * Sets the order of characters as they appear in font sheets and arrays. The order is assumed to go from left to 
	 * right and then down when needed, similar to handwriting. This is used to assist the non-static text drawing 
	 * methods as they appear in the EngineVisuals class. Note that this method will fail if the characters are repeated 
	 * in the provided String.
	 * 
	 * @param order The order of characters.
	 * 
	 * @return Whether or not the provided String could be accepted.
	 */
	public static void setFont(String order)
	{
		EngineVisuals.fontOrder = order;
	}
	
	//====================================================================================================
	//========================================PUBLIC METHODS AND FUNCTIONS================================
	//====================================================================================================
	
	/**
	 * Renders the image provided by the dimensions provided.
	 * 
	 * @param image The image to render.
	 * @param x The x-coordinate of the image render.
	 * @param y The y-coordinate of the image render.
	 * @param w The width of the image render.
	 * @param h The height of the image render.
	 */
	public void drawImage(BufferedImage image, int x, int y, int w, int h)
	{
		this.graphics.drawImage(image, x, y, w, h, null);
	}
	
	/**
	 * Renders the provided image by the dimensions provided.
	 * 
	 * @param image The image to render angled.
	 * @param x the x position of the angled image render.
	 * @param y The y position of the angled image render.
	 * @param w The width of the angled image render.
	 * @param h The height of the angled image render.
	 * @param theta The angle at which to render the image.
	 * @param anchX The x-coordinate of the rotational anchor point.
	 * @param anchY The y-coordinate of the rotational anchor point.
	 * @param resX The width resolution of the image rotation.
	 * @param resY The height resolution of the image rotation.
	 */
	public void drawImageAngled(BufferedImage image, int x, int y, int w, int h, double theta, int anchX, int anchY, int resX, int resY)
	{
		BufferedImage temp = new BufferedImage(resX, resY, 2);
		temp.getGraphics().drawImage(image, 0, 0, resX, resY, null);
		BufferedImage rotated = new BufferedImage(resX, resY, 2);
		
		AffineTransform rotation = AffineTransform.getRotateInstance(theta, anchX * (resX / image.getWidth()), anchY * (resY / image.getHeight()));
		AffineTransformOp operation = new AffineTransformOp(rotation, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		operation.filter(temp, rotated);
		
		this.drawImage(rotated, x, y, w, h);
	}
	
	/**
	 * Fades between each of the images in the provided array based on the elapsed time value of the provided timer 
	 * object, taking the provided duration to complete each fade.
	 * 
	 * @param images The images to fade through.
	 * @param timer The timer object by which to fade with.
	 * @param duration The duration of each fade.
	 * @param x The x-coordinate of the rendered fade.
	 * @param y The y-coordinate of the rendered fade.
	 * @param w The width of the rendered fade.
	 * @param h The height of the rendered fade.
	 */
	public void drawImageFade(BufferedImage[] images, EngineObject timer, double duration, int x, int y, int w, int h)
	{
		float alpha = (float) ((timer.getElapsedTime() % duration) / duration);
		int cycle = (int) ((timer.getElapsedTime() % (duration * images.length)) / duration);
		int next = (cycle + 1 == images.length) ? 0 : cycle + 1;
		
		this.drawImage(images[cycle], x, y, w, h);
		this.graphics.setComposite(AlphaComposite.SrcOver.derive(alpha));
		this.drawImage(images[next], x, y, w, h);
		this.graphics.setComposite(AlphaComposite.SrcOver.derive(1f));
	}
	
	/**
	 * Renders the given string using one of the fonts currently loaded into the EngineVisuals font list. The string will 
	 * be rendered over the rectangle described.
	 * 
	 * @param string The string to render.
	 * @param font The font to render with.
	 * @param x The x-coordinate of the rectangle to render the text over.
	 * @param y The y-coordinate of the rectangle to render the text over.
	 * @param w The width over which the string will be rendered, sharing itself per character.
	 * @param h The height that the string will be rendered at.
	 */
	public void drawText(String string, int font, int x, int y, int w, int h)
	{
		font = (int) EngineCore.forceInvariance(font, 0, EngineVisuals.fontList.size() - 1);
		
		char[] characters = EngineVisuals.fontOrder.toCharArray();
		char[] total = string.toCharArray();
		
		for(int i = 0; i < total.length; i++)
		{
			this.drawText(total, characters, i, x, y, w, h, font);
		}
	}
	
	public void fillRect(int x, int y, int w, int h, double r, double g, double b, double a)
	{
		this.setColor(r, g, b, a);
		this.graphics.fillRect(x, y, w, h);
	}
	
	//====================================================================================================
	//========================================GETTERS AND SETTERS=========================================
	//====================================================================================================
	
	/**
	 * Gets the graphics object that the visuals use to render objects.
	 * 
	 * @return The visuals' graphics object.
	 */
	public Graphics2D getGFX()
	{
		return this.graphics;
	}
	
	/**
	 * Sets the color of the graphics object using the parameters given.
	 * 
	 * @param red The red value on [0.0 - 1.0].
	 * @param green The green value on [0.0 - 1.0].
	 * @param blue The blue value on [0.0 - 1.0].
	 * @param alpha The alpha value on [0.0 - 1.0].
	 */
	public void setColor(double red, double green, double blue, double alpha)
	{
		float r = (float) EngineCore.forceInvariance(red, 0.0, 1.0);
		float g = (float) EngineCore.forceInvariance(green, 0.0, 1.0);
		float b = (float) EngineCore.forceInvariance(blue, 0.0, 1.0);
		float a = (float) EngineCore.forceInvariance(alpha, 0.0, 1.0);
		this.graphics.setColor(new Color(r, g, b, a));
	}
	
	//====================================================================================================
	//========================================PRIVATE METHODS AND OPERATIONS==============================
	//====================================================================================================
	
	private void drawText(char[] string, char[] order, int i, int x, int y, int w, int h, int font)
	{
		for(int j = 0; j < order.length; j++)
		{
			if(string[i] == order[j])
			{
				this.drawImage(EngineVisuals.fontList.get(font)[j], x + (i * (w / string.length)), y, w / (string.length), h);
				return;
			}
		}
	}
}
