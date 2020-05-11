package com.avian.engine;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.avian.entity.EntityPlayer;

public class Visuals
{
	private BufferedImage[][] crosshairs, reloading, weapons, healthbar;
	
	public GameFrame FRAME;
	public Graphics2D GRAPHICS;
	public double SCALEX, SCALEY;
	
	public Visuals(GameFrame frame, Graphics2D graphics, int width, int height)
	{
		FRAME = frame;
		GRAPHICS = graphics;
		SCALEX = 1f / width;
		SCALEY = 1f / height;
		
		crosshairs = FRAME.ENGINE.UTIL.V_CROSSHAIR;
		reloading = FRAME.ENGINE.UTIL.V_RELOADING;
		weapons = FRAME.ENGINE.UTIL.V_WEAPONS;
		healthbar = FRAME.ENGINE.UTIL.V_HEALTHBAR;
	}
	
	public static BufferedImage loadImage(String name)
	{
		name = "/png/" + name + ".png";
		
		try 
		{
			return ImageIO.read(Engine.class.getResource(name));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}
	
	public static BufferedImage[][] cut(BufferedImage toCut, int dimeX, int dimeY)
	{
		BufferedImage[][] ret = new BufferedImage[toCut.getWidth() / dimeX][toCut.getHeight() / dimeY];
		
		for(int i = 0; i < toCut.getWidth() / dimeX; i++)
		{
			for(int j = 0; j < toCut.getHeight() / dimeY; j++)
			{
				ret[i][j] = toCut.getSubimage(i * dimeX, j * dimeY, dimeX, dimeY);
			}
		}
		
		return ret;
	}
	
	public static BufferedImage tintImage(BufferedImage original, float r, float g, float b)
	{
		BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TRANSLUCENT);
		Graphics2D graphics = image.createGraphics();
		graphics.drawImage(original, 0, 0, null);
		graphics.dispose();
		
		for(int i = 0; i < image.getWidth(); i++)
		{
			for(int j = 0; j < image.getHeight(); j++)
			{
				int ax = image.getColorModel().getAlpha(image.getRaster().getDataElements(i, j, null));
				int rx = image.getColorModel().getRed(image.getRaster().getDataElements(i, j, null));
				int gx = image.getColorModel().getGreen(image.getRaster().getDataElements(i, j, null));
				int bx = image.getColorModel().getBlue(image.getRaster().getDataElements(i, j, null));
				
				rx *= r;
				gx *= g;
				bx *= b;
				
				image.setRGB(i, j, (ax << 24) | (rx << 16) | (gx << 8) | (bx << 0));
			}
		}
		
		return image;
	}
	
	public void drawImage(BufferedImage img, int x, int y, int w, int h)
	{
		GRAPHICS.drawImage(img,
				(int) ((x * SCALEX * FRAME.WIDTH) + FRAME.BORDER_WIDTH),
				(int) ((y * SCALEY * FRAME.HEIGHT) + FRAME.BORDER_HEIGHT),
				(int) (w * SCALEX * FRAME.WIDTH),
				(int) (h * SCALEY * FRAME.HEIGHT),
				null);
	}
	
	public void drawText(String toDraw, int x, int y, int w, int h)
	{
		BufferedImage[] font = new BufferedImage[(FRAME.ENGINE.UTIL.G_GENERAL_FONT.getWidth() / 48) * (FRAME.ENGINE.UTIL.G_GENERAL_FONT.getHeight() / 64)];
		char[] characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.,!?:;%'$()/ -1234567890   ".toCharArray();
		char[] string = toDraw.toCharArray();
		
		for(int j = 0; j < FRAME.ENGINE.UTIL.G_GENERAL_FONT.getHeight() / 64; j++)
		{
			for(int i = 0; i < FRAME.ENGINE.UTIL.G_GENERAL_FONT.getWidth() / 48; i++)
			{
				font[i + ((FRAME.ENGINE.UTIL.G_GENERAL_FONT.getWidth() / 48) * j)] = FRAME.ENGINE.UTIL.G_GENERAL_FONT.getSubimage(i * 48, j * 64, 48, 64);
			}
		}
		
		draw:
			for(int i = 0; i < string.length; i++)
			{
				for(int j = 0; j < characters.length; j++)
				{
					if(string[i] == characters[j])
					{
						drawImage(font[j], x + (i * (w / string.length)), y, w / (string.length), h);
						continue draw;
					}
				}
			}
	}
	
	public void drawColoredText(String toDraw, int x, int y, int w, int h, int r, int g, int b)
	{
		float red = Utilities.clamp(r, 0, 255) / 255f;
		float green = Utilities.clamp(g, 0, 255) / 255f;
		float blue = Utilities.clamp(b, 0, 255) / 255f;
		
		BufferedImage[] font = new BufferedImage[(FRAME.ENGINE.UTIL.G_GENERAL_FONT.getWidth() / 48) * (FRAME.ENGINE.UTIL.G_GENERAL_FONT.getHeight() / 64)];
		char[] characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.,!?:;%'$()/ -1234567890   ".toCharArray();
		char[] string = toDraw.toCharArray();
		
		for(int j = 0; j < FRAME.ENGINE.UTIL.G_GENERAL_FONT.getHeight() / 64; j++)
		{
			for(int i = 0; i < FRAME.ENGINE.UTIL.G_GENERAL_FONT.getWidth() / 48; i++)
			{
				font[i + ((FRAME.ENGINE.UTIL.G_GENERAL_FONT.getWidth() / 48) * j)] = FRAME.ENGINE.UTIL.G_GENERAL_FONT.getSubimage(i * 48, j * 64, 48, 64);
			}
		}
		
		draw:
			for(int i = 0; i < string.length; i++)
			{
				for(int j = 0; j < characters.length; j++)
				{
					if(string[i] == characters[j])
					{
						drawImage(tintImage(font[j], red, green, blue), x + (i * (w / string.length)), y, w / (string.length), h);
						continue draw;
					}
				}
			}
	}
	
	public void tween(BufferedImage[] images, int ageValue, int ticksPerImage, int x, int y, int w, int h, boolean swap)
	{
		float alpha = (ageValue % ticksPerImage) / (float) ticksPerImage;
		float beta = swap ? 1f - alpha : 1f;
		int tweenCycle = (ageValue % (ticksPerImage * images.length)) / ticksPerImage;
		int nextImage = tweenCycle + 1;
		
		if(nextImage == images.length)
		{
			nextImage = 0;
		}
		
		drawImage(images[tweenCycle], x, y, w, h);
		GRAPHICS.setComposite(AlphaComposite.SrcOver.derive(alpha));
		drawImage(images[nextImage], x, y, w, h);
		GRAPHICS.setComposite(AlphaComposite.SrcOver.derive(beta));
	}
	
	public void typeText(String toDraw, int ageValue, int timeInterval, int x, int y, int w, int h)
	{
		char[] characters = toDraw.toCharArray();
		int timeBetweenTypes = timeInterval / characters.length;
		int ticks = (ageValue % timeInterval) / timeBetweenTypes;
		
		if(ticks > characters.length)
		{
			ticks = characters.length;
		}
		
		for(int i = 0; i < ticks; i++)
		{
			drawText("" + characters[i], x + (i * (w / characters.length)), y, w / characters.length, h);
		}
	}
	
	public void typeOutParagraph(String[] type, int ageValue, int startingAge, int timePerLine, int x, int y, int w, int h, int spaceBetweenLines)
	{
		if(ageValue < startingAge)
		{
			return;
		}
		
		for(int i = 0; i < type.length; i++)
		{
			if(ageValue - startingAge < timePerLine * (i + 1))
			{
				typeText(type[i], ageValue - startingAge, timePerLine, x, y + (i * ((h / type.length) + spaceBetweenLines)), w, h / type.length);
				break;
			}
			else
			{
				drawText(type[i], x, y + (i * ((h / type.length) + spaceBetweenLines)), w, h / type.length);
			}
		}
	}
	
	public void setScale(int newWidth, int newHeight)
	{
		SCALEX = (1 / newWidth);
		SCALEY = (1 / newHeight);
	}
	
	public void drawHUD(EntityPlayer player)
	{
		String addOnAmmo = player.CURRENT_WEAPON.ID == 0 ? "" : " - " + player.TOTAL_AMMO[player.CURRENT_WEAPON.ID];
		drawText(player.CURRENT_WEAPON.GUN_NAME + addOnAmmo, 20, 10, 240, 32);
		drawImage(crosshairs[player.CURRENT_WEAPON.ID == 0 ? 4 : FRAME.ENGINE.SETTINGS.CROSSHAIR][0], player.LOOK_X - (64), player.LOOK_Y - (64), 128, 128);
		drawImage(weapons[player.CURRENT_WEAPON.ID % 4][player.CURRENT_WEAPON.ID / 4], 0, -32, 256, 256);
		drawImage(healthbar[0][1], 272, 76, 392, 40);
		drawImage(healthbar[0][0], 276, 80, (int) (384 * (player.HEALTH / player.MAX_HEALTH)), 32);
		drawColoredText(player.HEALTH + " / " + player.MAX_HEALTH, 372, 80, 192, 32, 160, 0, 0);
		
		for(int i = 0; i < player.CURRENT_AMMO; i++)
		{
			drawImage(FRAME.ENGINE.UTIL.G_HUD_AMMOICON, 64 + ((i / (player.CURRENT_WEAPON.CLIP_SIZE / 2)) * 48), 180 + ((i % (player.CURRENT_WEAPON.CLIP_SIZE / 2)) * 12), 32, 8);
		}
		
		if(player.IS_RELOADING)
		{
			int cycle = Utilities.clamp((int)(((float)(player.AGE - player.AGE_AT_RELOAD) / player.CURRENT_WEAPON.RELOAD_TIME) * 13), 0, 12);
			drawImage(reloading[cycle][0], player.LEVEL.XLOC + player.XPOS - 32, player.LEVEL.YLOC + player.YPOS - 128, 64, 64);
		}
	}
}
