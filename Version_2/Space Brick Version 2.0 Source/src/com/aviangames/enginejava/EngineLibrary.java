package com.aviangames.enginejava;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Used as a place to load assets into memory, so that it doesn't have to be done in real time.
 * 
 * @author Sydney Fonderie
 *
 */
public final class EngineLibrary 
{
	private final ArrayList<BufferedImage> loadedImages = new ArrayList<BufferedImage>();
	
	private final ArrayList<BufferedImage[]> loadedImageSingleArrays = new ArrayList<BufferedImage[]>();
	
	private final ArrayList<BufferedImage[][]> loadedImageDoubleArrays = new ArrayList<BufferedImage[][]>();
	
	private final ArrayList<String> loadedImageIDs = new ArrayList<String>();
	
	private final ArrayList<String> loadedImageSingleArrayIDs = new ArrayList<String>();
	
	private final ArrayList<String> loadedImageDoubleArrayIDs = new ArrayList<String>();
	
	EngineLibrary() { }
	
	//====================================================================================================
	//========================================STATIC METHODS AND FUNCTIONS================================
	//====================================================================================================
	
	/**
	 * Creates a copy of the provided image, but gray-scaled and then recolored to the provided color values.
	 * 
	 * @param image The original image to be copied off of.
	 * @param red The red tint of the recolored image.
	 * @param green The green tint of the recolored image.
	 * @param blue The blue tint of the recolored image.
	 * @param alpha The new alpha value of the recolored image.
	 * 
	 * @return The recolored image.
	 */
	public static BufferedImage createRecoloredImage(BufferedImage image, double red, double green, double blue, double alpha)
	{
		float r = (float) EngineCore.forceInvariance(red, 0.0, 1.0);
		float g = (float) EngineCore.forceInvariance(green, 0.0, 1.0);
		float b = (float) EngineCore.forceInvariance(blue, 0.0, 1.0);
		float a = (float) EngineCore.forceInvariance(alpha, 0.0, 1.0);
		
		BufferedImage recolored = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TRANSLUCENT);
		Graphics2D graphics = recolored.createGraphics();
		graphics.drawImage(image, 0, 0, null);
		graphics.dispose();
		
		for(int i = 0; i < recolored.getWidth(); i++)
		{
			for(int j = 0; j < recolored.getHeight(); j++)
			{
				int ax = recolored.getColorModel().getAlpha(recolored.getRaster().getDataElements(i, j, null));
				int rx = recolored.getColorModel().getRed(recolored.getRaster().getDataElements(i, j, null));
				int gx = recolored.getColorModel().getGreen(recolored.getRaster().getDataElements(i, j, null));
				int bx = recolored.getColorModel().getBlue(recolored.getRaster().getDataElements(i, j, null));
				
				ax *= a;
				rx *= r;
				gx *= g;
				bx *= b;
				
				recolored.setRGB(i, j, (ax << 24) | (rx << 16) | (gx << 8) | (bx << 0));
			}
		}
		
		return recolored;
	}
	
	/**
	 * Creates a one-dimensional array by cutting the provided image into the number of rows and columns, and then 
	 * loading them from left to right and then down a row when necessary.
	 * 
	 * @param image The original image.
	 * @param columns The number of columns (cuts in the x direction) to make.
	 * @param rows The number of rows (cuts in the y direction) to make.
	 * 
	 * @return The original image cut into a one-dimensional array.
	 */
	public static BufferedImage[] createCutSingleArray(BufferedImage image, int columns, int rows)
	{
		BufferedImage[] array = new BufferedImage[columns * rows];
		
		int cutWidth = image.getWidth() / columns;
		int cutHeight = image.getHeight() / rows;
		
		for(int row = 0; row < rows; row++)
		{
			for(int column = 0; column < columns; column++)
			{
				array[row * columns + column] = image.getSubimage(column * cutWidth, row * cutHeight, cutWidth, cutHeight);
			}
		}
		
		return array;
	}
	
	/**
	 * Creates a two-dimensional array by cutting the provided image into the number of rows and columns, and then 
	 * loading them as they appear relative to the image.
	 * 
	 * @param image The original image.
	 * @param columns The number of columns (cuts in the x direction) to make.
	 * @param rows The number of rows (cuts in the y direction) to make.
	 * 
	 * @return The original image cut into a two-dimensional array.
	 */
	public static BufferedImage[][] createCutDoubleArray(BufferedImage image, int columns, int rows)
	{
		BufferedImage[][] array = new BufferedImage[columns][rows];
		
		int cutWidth = image.getWidth() / columns;
		int cutHeight = image.getHeight() / rows;
		
		for(int row = 0; row < rows; row++)
		{
			for(int column = 0; column < columns; column++)
			{
				array[column][row] = image.getSubimage(column * cutWidth, row * cutHeight, cutWidth, cutHeight);
			}
		}
		
		return array;
	}
	
	//====================================================================================================
	//========================================PUBLIC METHODS AND FUNCTIONS================================
	//====================================================================================================
	
	/**
	 * Loads an image from the specified file path and then adds it to the library's storage.
	 * 
	 * @param filepath The file path to the image to load.
	 * @param identifier A string identifier by which to find the image once loaded.
	 */
	public void loadImage(String filepath, String identifier)
	{
		try 
		{
			this.loadedImages.add(ImageIO.read(EngineLibrary.class.getResource(filepath)));
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		this.loadedImageIDs.add(identifier);
	}
	
	/**
	 * Loads an image from the specified file path and then cuts it into a one-dimensional array by the number of 
	 * columns and rows specified. The array is then loaded into the library's storage.
	 * 
	 * @param filepath The file path to the image to load.
	 * @param identifier A string identifier by which to find the image single array once loaded.
	 * @param columns The number of columns to cut the image by.
	 * @param rows The number of rows to cut the image by.
	 */
	public void loadImageArray(String filepath, String identifier, int columns, int rows)
	{
		try 
		{
			this.loadedImageSingleArrays.add(EngineLibrary.createCutSingleArray(ImageIO.read(EngineLibrary.class.getResource(filepath)), columns, rows));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		this.loadedImageSingleArrayIDs.add(identifier);
	}
	
	/**
	 * Loads an image from the specified file path and then cuts it into a two-dimensional array by the number of 
	 * columns and rows specified. The array is then loaded into the library's storage.
	 * 
	 * @param filepath The file path to the image to load.
	 * @param identifier A string identifier by which to find the image double array once loaded.
	 * @param columns The number of columns to cut the image by.
	 * @param rows The number of rows to cut the image by.
	 */
	public void loadImageDoubleArray(String filepath, String identifier, int columns, int rows)
	{
		try 
		{
			this.loadedImageDoubleArrays.add(EngineLibrary.createCutDoubleArray(ImageIO.read(EngineLibrary.class.getResource(filepath)), columns, rows));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		this.loadedImageDoubleArrayIDs.add(identifier);
	}
	
	//====================================================================================================
	//========================================GETTERS AND SETTERS=========================================
	//====================================================================================================
	
	/**
	 * Gets an image from the loaded library by the specified identifier.
	 * 
	 * @param identifier The string identifier associated with the image to find.
	 * 
	 * @return The image associated with the identifier, if it exists.
	 */
	public BufferedImage getImage(String identifier)
	{
		for(int i = 0; i < this.loadedImageIDs.size(); i++)
		{
			if(this.loadedImageIDs.get(i) == identifier)
			{
				return this.loadedImages.get(i);
			}
		}
		
		System.out.println("NO SUCH IMAGE WITH ID \"" + identifier + "\" COULD BE FOUND");
		return null;
	}
	
	/**
	 * Gets an image array from the loaded library by the specified identifier.
	 * 
	 * @param identifier The string identifier associated with the image to find.
	 * 
	 * @return The image array associated with the identifier, if it exists.
	 */
	public BufferedImage[] getImageArray(String identifier)
	{
		for(int i = 0; i < this.loadedImageSingleArrayIDs.size(); i++)
		{
			if(this.loadedImageSingleArrayIDs.get(i) == identifier)
			{
				return this.loadedImageSingleArrays.get(i);
			}
		}
		
		System.out.println("NO SUCH IMAGE ARRAY WITH ID \"" + identifier + "\" COULD BE FOUND");
		return null;
	}
	
	/**
	 * Gets an image double array from the loaded library by the specified identifier.
	 * 
	 * @param identifier The string identifier associated with the image to find.
	 * 
	 * @return The image double array associated with the identifier, if it exists.
	 */
	public BufferedImage[][] getImageDoubleArray(String identifier)
	{
		for(int i = 0; i < this.loadedImageDoubleArrayIDs.size(); i++)
		{
			if(this.loadedImageDoubleArrayIDs.get(i) == identifier)
			{
				return this.loadedImageDoubleArrays.get(i);
			}
		}
		
		System.out.println("NO SUCH IMAGE DOUBLE ARRAY WITH ID \"" + identifier + "\" COULD BE FOUND");
		return null;
	}
}
