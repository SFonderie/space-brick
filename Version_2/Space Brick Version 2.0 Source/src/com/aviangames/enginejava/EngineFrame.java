package com.aviangames.enginejava;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * Functional class that turns update ticks into rendering.
 * 
 * @author Sydney Fonderie
 *
 */
final class EngineFrame extends JFrame implements MouseListener, MouseMotionListener, KeyListener
{
	private static final long serialVersionUID = 5244299823303787579L;
	
	private final Canvas renderCanvas;
	
	private final EngineCore parentEngine;
	
	private final int frameWidth;
	
	private final int frameHeight;
	
	private final int preRenderWidth;
	
	private final int preRenderHeight;
	
	private final int renderBorderWidth;
	
	private final int renderBorderHeight;
	
	private final int renderWidth;
	
	private final int renderHeight;
	
	EngineFrame(EngineCore engine, int width, int height, int renderWidth, int renderHeight, boolean fullscreen)
	{
		//Sets the simple final variables.
		this.parentEngine = engine;
		this.preRenderWidth = renderWidth;
		this.preRenderHeight = renderHeight;
		this.renderCanvas = new Canvas();
		
		int monitorWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int monitorHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		//Clamps the width to the monitor width.
		if(width >= monitorWidth)
		{
			width = monitorWidth;
			fullscreen = true;
		}
		
		//Clamps the height to the monitor height.
		if(height >= monitorHeight)
		{
			height = monitorHeight;
			fullscreen = true;
		}
		
		//Sets the size of the frame to the originally demanded parameters (bounded by the monitor size).
		this.frameWidth = fullscreen ? monitorWidth : width;
		this.frameHeight = fullscreen ? monitorHeight : height;
		
		//Sets the size of the true drawing area to a 16:9 ratio within the frame.
		int sX = width / 16;
		int sY = height / 9;
		int scale = sX < sY ? sX : sY;
		this.renderWidth = scale * 16;
		this.renderHeight = scale * 9;
		
		//Finally, the border (frame - render) dimensions.
		this.renderBorderWidth = (this.frameWidth - this.renderWidth) / 2;
		this.renderBorderHeight = (this.frameHeight - this.renderHeight) / 2;
		
		//======================================================================
		//====================AND NOW THE ACTUAL STUFF==========================
		//======================================================================
		
		Dimension size = new Dimension(this.frameWidth, this.frameHeight);
		
		this.setMinimumSize(size);
		this.setMaximumSize(size);
		this.setPreferredSize(size);
		this.setResizable(false);
		this.setUndecorated(fullscreen);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(this.parentEngine.toString());
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.renderCanvas.setMinimumSize(size);
		this.renderCanvas.setMaximumSize(size);
		this.renderCanvas.setPreferredSize(size);
		this.renderCanvas.addKeyListener(this);
		this.renderCanvas.addMouseListener(this);
		this.renderCanvas.addMouseMotionListener(this);
		this.add(this.renderCanvas);
		System.out.println(this);
	}
	
	//====================================================================================================
	//========================================GETTERS AND SETTERS=========================================
	//====================================================================================================
	
	final int convertMouseX(int mouseX)
	{
		mouseX -= this.renderBorderWidth;
		mouseX *= ((double) this.preRenderWidth / this.renderWidth);
		return mouseX;
	}
	
	final int convertMouseY(int mouseY)
	{
		mouseY -= this.renderBorderHeight;
		mouseY *= ((double) this.preRenderHeight / this.renderHeight);
		return mouseY;
	}
	
	//====================================================================================================
	//========================================OVERRIDES AND IMPLEMENTS====================================
	//====================================================================================================
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		if(this.parentEngine.getActiveState() != null)
		{
			this.parentEngine.getActiveState().keyEvent(e, true);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		if(this.parentEngine.getActiveState() != null)
		{
			this.parentEngine.getActiveState().keyEvent(e, false);
		}
	}
	
	@Override
	@Deprecated
	public void keyTyped(KeyEvent e) { }
	
	@Override
	@Deprecated
	public void mouseClicked(MouseEvent e) { }
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if(this.parentEngine.getActiveState() != null)
		{
			this.parentEngine.getActiveState().mouseMoveEvent(e, true);
		}
	}
	
	@Override
	@Deprecated
	public void mouseEntered(MouseEvent e) { }
	
	@Override
	@Deprecated
	public void mouseExited(MouseEvent e) { }
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		if(this.parentEngine.getActiveState() != null)
		{
			this.parentEngine.getActiveState().mouseMoveEvent(e, false);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) 
	{
		if(this.parentEngine.getActiveState() != null)
		{
			this.parentEngine.getActiveState().mouseEvent(e, true);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(this.parentEngine.getActiveState() != null)
		{
			this.parentEngine.getActiveState().mouseEvent(e, false);
		}
	}
	
	@Override
	public String toString()
	{
		String frame = "Frame Width: " + this.frameWidth + ", Frame Height: " + this.frameHeight + "\n";
		String render = "Canvas Width: " + this.renderWidth + ", Canvas Height: " + this.renderHeight + "\n";
		String relative = "Render Width: " + this.preRenderWidth + ", Render Height: " + this.preRenderHeight + "\n";
		String border = "Border Width: " + this.renderBorderWidth + ", Border Height: " + this.renderBorderHeight + "\n";
		
		return frame + render + relative + border;
	}
	
	//====================================================================================================
	//========================================PRIVATE METHODS AND OPERATIONS==============================
	//====================================================================================================
	
	void update()
	{
		if(!this.isVisible())
		{
			this.setVisible(true);
			this.pack();
		}
		
		BufferStrategy bs = this.renderCanvas.getBufferStrategy();
		
		if(bs == null)
		{
			this.renderCanvas.createBufferStrategy(3);
			return;
		}
		
		//The canvas drawing tool
		Graphics2D gfx = (Graphics2D) bs.getDrawGraphics();
		gfx.clearRect(0, 0, this.frameWidth, this.frameHeight);
		
		//The actual rendered image and its render tool
		BufferedImage render = new BufferedImage(this.preRenderWidth, this.preRenderHeight, 3);
		EngineVisuals vfx = new EngineVisuals(render.createGraphics(), this.preRenderWidth, this.preRenderHeight);
		
		if(this.parentEngine.getActiveState() != null)
		{
			this.parentEngine.getActiveState().render(vfx);
		}
		
		//Draws the game render
		gfx.drawImage(render, this.renderBorderWidth, this.renderBorderHeight, this.renderWidth, this.renderHeight, null);
		
		//Draws the borders
		gfx.setColor(Color.BLACK);
		gfx.fillRect(0, 0, this.frameWidth, this.renderBorderHeight);
		gfx.fillRect(0, 0, this.renderBorderWidth, this.frameHeight);
		gfx.fillRect(0, this.frameHeight - this.renderBorderHeight, this.frameWidth, this.renderBorderHeight);
		gfx.fillRect(this.frameWidth - this.renderBorderWidth, 0, this.renderBorderWidth, this.frameHeight);
		
		//Finalizes this drawing
		gfx.dispose();
		bs.show();
	}
}
