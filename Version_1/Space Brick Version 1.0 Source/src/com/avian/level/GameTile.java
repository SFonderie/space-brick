package com.avian.level;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.avian.engine.Visuals;
import com.avian.entity.TileEntity;

public class GameTile
{
	public TileEntity[][] SUBTILES;
	public BufferedImage[][] TILESET;
	public int XPOS, YPOS;
	public int WIDTH, HEIGHT;
	public GameLevel LEVEL;
	public boolean[] GATES = new boolean[4];
	
	public static final int SCALE = 256;
	
	public GameTile(String fileLoc, GameLevel level, int xPos, int yPos)
	{
		LEVEL = level;
		XPOS = xPos;
		YPOS = yPos;
		
		WIDTH = SCALE * 16;
		HEIGHT = SCALE * 16;
		
		TILESET = Visuals.cut(LEVEL.STATE.FRAME.ENGINE.UTIL.L_STATION_TILES, 32, 32);
		
		try
		{
			SUBTILES = getTileMapping(fileLoc);
			GATES = getGates(fileLoc);
			
			for(int i = 0; i < 256; i++)
			{
				LEVEL.ATILES.add(SUBTILES[i % 16][i / 16]);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public TileEntity[][] getTileMapping(String basis) throws IOException
	{
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader("bin/com/avian/level/maps/" + basis + ".dlf"));
		TileEntity[][] out = new TileEntity[16][16];
		
		for(int i = 0; i < 16; i++)
		{
			String[] line = reader.readLine().split(",");
			
			for(int j = 0; j < 16; j++)
			{
				if(Integer.parseInt(line[j]) == 5 || Integer.parseInt(line[j]) == 10 || Integer.parseInt(line[j]) == 11 || Integer.parseInt(line[j]) == 16)
				{
					out[j][i] = new TileEntity(Integer.parseInt(line[j]), LEVEL, XPOS + (j * SCALE), YPOS + (i * SCALE), SCALE, SCALE, 0.0, TILESET[1][0], TILESET[Integer.parseInt(line[j]) % 8][Integer.parseInt(line[j]) / 8]);
				}
				else
				{
					out[j][i] = new TileEntity(Integer.parseInt(line[j]), LEVEL, XPOS + (j * SCALE), YPOS + (i * SCALE), SCALE, SCALE, 0.0, TILESET[Integer.parseInt(line[j]) % 8][Integer.parseInt(line[j]) / 8]);
				}
			}
		}
		
		return out;
	}
	
	public boolean[] getGates(String basis) throws IOException
	{
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader("bin/com/avian/level/maps/" + basis + ".dlf"));
		boolean[] out = new boolean[4];
		
		for(int i = 0; i < 17; i++)
		{
			String[] line = reader.readLine().split(",");
			
			if(i == 16)
			{
				for(int j = 0; j < 4; j++)
				{
					out[j] = Boolean.parseBoolean(line[j]);
				}
			}
		}
		
		return out;
	}
}
