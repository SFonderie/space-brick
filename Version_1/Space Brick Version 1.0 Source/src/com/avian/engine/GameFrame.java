package com.avian.engine;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

import com.avian.state.GameState;
import com.avian.state.MainmenuState;
import com.avian.state.CreationState;
import com.avian.state.OvertureState;
import com.avian.state.SettingsState;
import com.avian.state.TutorialState;

public class GameFrame extends JFrame implements KeyListener, MouseInputListener
{
	private static final long serialVersionUID = 1L;
	private Canvas canvas;
	
	public int AGE;
	public int SCREEN_WIDTH, SCREEN_HEIGHT;
	public int BORDER_WIDTH, BORDER_HEIGHT;
	public int NATIVE_WIDTH, NATIVE_HEIGHT;
	public int WIDTH, HEIGHT, SCALE;
	public boolean TICKING;
	public GameState ACTIVE_STATE;
	public Engine ENGINE;
	
	public GameFrame(Engine engine, int width, int height, boolean doFullscreen)
	{
		super("SUPERIORITY COMPLEX");
		
		if(width > Toolkit.getDefaultToolkit().getScreenSize().width)
		{
			width = Toolkit.getDefaultToolkit().getScreenSize().width;
		}
		
		if(height > Toolkit.getDefaultToolkit().getScreenSize().height)
		{
			height = Toolkit.getDefaultToolkit().getScreenSize().height;
		}
		
		SCREEN_WIDTH = width;
		SCREEN_HEIGHT = height;
		
		if(doFullscreen)
		{
			SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
			SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
		}
		
		SCALE = width / 64;
		WIDTH = SCALE * 64;
		HEIGHT = SCALE * 36;
		
		BORDER_WIDTH = (SCREEN_WIDTH - WIDTH) / 2;
		BORDER_HEIGHT = (SCREEN_HEIGHT - HEIGHT) / 2;
		NATIVE_WIDTH = 1920;
		NATIVE_HEIGHT = 1080;
		ENGINE = engine;
		
		Dimension size = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
		
		this.setSize(size);
		this.setResizable(false);
		this.setUndecorated(true);
		this.setLocationRelativeTo(null);
		this.setFocusTraversalKeysEnabled(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		canvas = new Canvas();
		canvas.setPreferredSize(size);
		canvas.setMinimumSize(size);
		canvas.setMaximumSize(size);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		
		this.add(canvas);
		this.setVisible(true);
		this.pack();
		
		this.getContentPane().setCursor(ENGINE.UTIL.CURSORS[0]);
		this.setActiveState(new OvertureState(this));
		TICKING = true;
		AGE = 0;
	}
	
	public void tick()
	{
		if(ACTIVE_STATE != null)
		{
			ACTIVE_STATE.tick();
		}
	}
	
	public void render()
	{
		BufferStrategy buffers = canvas.getBufferStrategy();
		Graphics2D graphics;
		Visuals visuals;
		
		if(buffers == null)
		{
			canvas.createBufferStrategy(3);
			return;
		}
		
		graphics = (Graphics2D) buffers.getDrawGraphics();
		visuals = new Visuals(this, graphics, NATIVE_WIDTH, NATIVE_HEIGHT);
		graphics.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		if(ACTIVE_STATE != null)
		{
			ACTIVE_STATE.render(visuals);
		}
		
		buffers.show();
		graphics.dispose();
	}
	
	public void setActiveState(GameState switchTo)
	{
		if(ACTIVE_STATE != null)
		{
			ACTIVE_STATE.onDeactivate();
		}
		
		ACTIVE_STATE = null;
		ACTIVE_STATE = switchTo;
		ACTIVE_STATE.onActivate();
	}
	
	public void keyPressed(KeyEvent e)
	{
		if(ACTIVE_STATE instanceof OvertureState && e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			OvertureState state = (OvertureState) ACTIVE_STATE;
			state.cont();
			return;
		}
		
		if(ACTIVE_STATE instanceof SettingsState)
		{
			SettingsState state = (SettingsState) ACTIVE_STATE;
			
			if(state.SWITCH > 0)
			{
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					state.SWITCH = 0;
				}
				else if(e.getKeyCode() == KeyEvent.VK_ALT)
				{
					return;
				}
				else
				{
					ENGINE.SETTINGS.switchKey(state.SWITCH - 1, e.getKeyCode());
					state.SWITCH = 0;
				}
				
				return;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			{
				state.returnToPreviousState();
			}
			
			return;
		}
		
		if(ACTIVE_STATE instanceof CreationState)
		{
			CreationState state = (CreationState) ACTIVE_STATE;
			
			if(!state.FADE_OUT)
			{
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					state.fadeOut(false);
				}
				
				if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
				{
					state.NAME = "";
				}
				
				if(state.NAME.length() < 18)
				{	
					state.NAME = state.NAME + Settings.getKey(e.getKeyCode());
				}
			}
			
			return;
		}
		
		if(ACTIVE_STATE instanceof TutorialState)
		{
			TutorialState state = (TutorialState) ACTIVE_STATE;
			
			if(state.INTRO_OVER && !state.ESC_SCREEN)
			{
				if(e.getKeyCode() == ENGINE.SETTINGS.KEYS[0])
				{
					state.ACTIVE_LEVEL.PLAYER.FORWARD = true;
					state.ACTIVE_LEVEL.PLAYER.BACKWARD = false;
				}
				if(e.getKeyCode() == ENGINE.SETTINGS.KEYS[1])
				{
					state.ACTIVE_LEVEL.PLAYER.BACKWARD = true;
					state.ACTIVE_LEVEL.PLAYER.FORWARD = false;
				}
				if(e.getKeyCode() == ENGINE.SETTINGS.KEYS[2])
				{
					state.ACTIVE_LEVEL.PLAYER.LEFT = true;
					state.ACTIVE_LEVEL.PLAYER.RIGHT = false;
				}
				if(e.getKeyCode() == ENGINE.SETTINGS.KEYS[3])
				{
					state.ACTIVE_LEVEL.PLAYER.RIGHT = true;
					state.ACTIVE_LEVEL.PLAYER.LEFT = false;
				}
				if(e.getKeyCode() == ENGINE.SETTINGS.KEYS[4])
				{
					state.ACTIVE_LEVEL.PLAYER.reload();
				}
				if(e.getKeyCode() == ENGINE.SETTINGS.KEYS[6])
				{
					state.ACTIVE_LEVEL.PLAYER.onCollectWeapon(1);
					state.ACTIVE_LEVEL.PLAYER.onCollectWeapon(2);
					state.ACTIVE_LEVEL.PLAYER.onCollectWeapon(3);
					state.ACTIVE_LEVEL.PLAYER.onCollectWeapon(4);
					state.ACTIVE_LEVEL.PLAYER.onCollectWeapon(5);
					state.ACTIVE_LEVEL.PLAYER.onCollectWeapon(6);
				}
				if(e.getKeyCode() == ENGINE.SETTINGS.KEYS[7])
				{
					state.ACTIVE_LEVEL.PLAYER.onSwitchWeapon();
				}
			}
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE && state.INTRO_OVER)
			{
				state.ESC_SCREEN = !state.ESC_SCREEN;
				state.ACTIVE_LEVEL.PLAYER.FORWARD = false;
				state.ACTIVE_LEVEL.PLAYER.BACKWARD = false;
				state.ACTIVE_LEVEL.PLAYER.LEFT = false;
				state.ACTIVE_LEVEL.PLAYER.RIGHT = false;
				getContentPane().setCursor(ENGINE.UTIL.CURSORS[state.ESC_SCREEN ? 0 : 1]);
			}
			
			return;
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
		if(ACTIVE_STATE instanceof TutorialState)
		{
			TutorialState state = (TutorialState) ACTIVE_STATE;
			
			if(e.getKeyCode() == ENGINE.SETTINGS.KEYS[0])
			{
				state.ACTIVE_LEVEL.PLAYER.FORWARD = false;
			}
			if(e.getKeyCode() == ENGINE.SETTINGS.KEYS[1])
			{
				state.ACTIVE_LEVEL.PLAYER.BACKWARD = false;
			}
			if(e.getKeyCode() == ENGINE.SETTINGS.KEYS[2])
			{
				state.ACTIVE_LEVEL.PLAYER.LEFT = false;
			}
			if(e.getKeyCode() == ENGINE.SETTINGS.KEYS[3])
			{
				state.ACTIVE_LEVEL.PLAYER.RIGHT = false;
			}
			
			return;
		}
	}
	
	public void mousePressed(MouseEvent e) 
	{
		if(ACTIVE_STATE instanceof OvertureState && e.getButton() == MouseEvent.BUTTON1)
		{
			OvertureState state = (OvertureState) ACTIVE_STATE;
			state.cont();
			return;
		}
		
		if(ACTIVE_STATE instanceof MainmenuState && e.getButton() == MouseEvent.BUTTON1)
		{
			MainmenuState state = (MainmenuState) ACTIVE_STATE;
			
			if(state.BUTTON_ACT[0])
			{
				state.fadeOut();
			}
			
			if(state.BUTTON_ACT[3])
			{
				setActiveState(new SettingsState(this, state.AGE));
			}
			
			if(state.BUTTON_ACT[4])
			{
				System.exit(0);
			}
			
			return;
		}
		
		if(ACTIVE_STATE instanceof SettingsState)
		{
			SettingsState state = (SettingsState) ACTIVE_STATE;
			
			if(e.getButton() == MouseEvent.BUTTON1)
			{
				for(int i = 0; i < state.MOUSE_OVER.length; i++)
				{
					if(state.MOUSE_OVER[i])
					{
						state.SWITCH = i + 1;
					}
				}
				
				if(state.BUTTON_ACT[5])
				{
					state.returnToPreviousState();
				}
				
				if(state.EXIT_BIND)
				{
					state.SWITCH = 0;
				}
			}
			
			if(state.BUTTON_ACT[0])
			{
				ENGINE.SETTINGS.cycleResolution(!(e.getButton() == MouseEvent.BUTTON3));
			}
			else if(state.BUTTON_ACT[1])
			{
				ENGINE.SETTINGS.cycleDifficulty(!(e.getButton() == MouseEvent.BUTTON3));
			}
			else if(state.BUTTON_ACT[2])
			{
				ENGINE.SETTINGS.cycleCrosshair(!(e.getButton() == MouseEvent.BUTTON3));
			}
			else if(state.BUTTON_ACT[3])
			{
				ENGINE.SETTINGS.cycleSoundLevel(!(e.getButton() == MouseEvent.BUTTON3));
			}
			else if(state.BUTTON_ACT[4])
			{
				ENGINE.SETTINGS.cycleMusicLevel(!(e.getButton() == MouseEvent.BUTTON3));
			}
			else if(state.BUTTON_ACT[11])
			{
				ENGINE.SETTINGS = new Settings();
			}
			
			for(int i = 0; i < ENGINE.SETTINGS.BOOL_OPTIONS.length; i++)
			{
				if(state.BUTTON_ACT[i + 6])
				{
					ENGINE.SETTINGS.BOOL_OPTIONS[i] = !ENGINE.SETTINGS.BOOL_OPTIONS[i];
				}
			}
			
			return;
		}
		
		if(ACTIVE_STATE instanceof CreationState)
		{
			CreationState state = (CreationState) ACTIVE_STATE;
			
			if(e.getButton() == MouseEvent.BUTTON1)
			{
				for(int i = 0; i < state.MOUSE_OVER.length; i++)
				{
					if(state.MOUSE_OVER[i])
					{
						ENGINE.SETTINGS.DIFFICULTY = Utilities.clamp(i, 0, 5);
					}
				}
				
				if(state.GO_BACK)
				{
					state.fadeOut(false);
				}
				
				if(state.GO_FORWARD && state.NAME.length() > 0)
				{
					state.fadeOut(true);
				}
			}
			
			return;
		}
		
		if(ACTIVE_STATE instanceof TutorialState)
		{
			TutorialState state = (TutorialState) ACTIVE_STATE;
			
			if(!state.ESC_SCREEN && state.INTRO_OVER)
			{
				if(e.getButton() == MouseEvent.BUTTON1)
				{
					state.ACTIVE_LEVEL.PLAYER.IS_FIRING = true;
				}
			}
			
			if(state.ESC_SCREEN && e.getButton() == MouseEvent.BUTTON1)
			{
				if(state.BUTTON_ACT[0])
				{
					state.ESC_SCREEN = false;
					this.getContentPane().setCursor(ENGINE.UTIL.CURSORS[1]);
				}
				
				if(state.BUTTON_ACT[1])
				{
					this.setActiveState(new SettingsState(this, state));
				}
				
				if(state.BUTTON_ACT[2])
				{
					this.setActiveState(new MainmenuState(this, true));
				}
			}
			
			return;
		}
	}
	
	public void mouseReleased(MouseEvent e) 
	{
		if(ACTIVE_STATE instanceof TutorialState)
		{
			TutorialState state = (TutorialState) ACTIVE_STATE;
			
			if(e.getButton() == MouseEvent.BUTTON1)
			{
				state.ACTIVE_LEVEL.PLAYER.IS_FIRING = false;
			}
			
			return;
		}
	}
	
	public void mouseMoved(MouseEvent e)
	{
		if(ACTIVE_STATE instanceof MainmenuState)
		{
			MainmenuState state = (MainmenuState) ACTIVE_STATE;
			
			if(!state.FADE_OUT)
			{
				for(int i = 0; i < state.NUM_BUTTONS; i++)
				{
					state.BUTTON_ACT[i] = hasMouseInBounds(e, 275, 240 + (i * 160), 500, 60);
				}
			}
			
			return;
		}
		
		if(ACTIVE_STATE instanceof SettingsState)
		{
			SettingsState state = (SettingsState) ACTIVE_STATE;
			
			for(int i = 0; i < state.MOUSE_OVER.length; i++)
			{
				state.MOUSE_OVER[i] = (hasMouseInBounds(e, 1360, 160 + (i * 72), 500, 64) && state.SWITCH == 0);
			}
			
			for(int i = 0; i < state.NUM_BUTTONS; i++)
			{
				state.BUTTON_ACT[i] = (hasMouseInBounds(e, 80 + ((i / 6) * 640), 180 + ((i % 6) * 144), 560, 100) && state.SWITCH == 0);
			}
			
			if(state.SWITCH > 0 && hasMouseInBounds(e, 450, 704, 300, 40))
			{
				state.EXIT_BIND = true;
			}
			else
			{
				state.EXIT_BIND = false;
			}
			
			return;
		}
		
		if(ACTIVE_STATE instanceof CreationState)
		{
			CreationState state = (CreationState) ACTIVE_STATE;
			
			if(!state.FADE_OUT)
			{
				for(int i = 0; i < state.MOUSE_OVER.length; i++)
				{
					state.MOUSE_OVER[i] = hasMouseInBounds(e, 80 + (i * 300), 895, 260, 90);
				}
				
				state.GO_BACK = hasMouseInBounds(e, 180, 600, 400, 80);
				state.GO_FORWARD = hasMouseInBounds(e, 1340, 600, 400, 80);
			}
			
			return;
		}
		
		if(ACTIVE_STATE instanceof TutorialState)
		{
			TutorialState state = (TutorialState) ACTIVE_STATE;
			
			if(state.INTRO_OVER)
			{
				if(state.ESC_SCREEN)
				{
					for(int i = 0; i < state.NUM_BUTTONS; i++)
					{
						state.BUTTON_ACT[i] = hasMouseInBounds(e, 150, 320 + (i * 240), 500, 80);
					}
				}
				else
				{
					state.ACTIVE_LEVEL.PLAYER.LOOK_X = e.getX();
					state.ACTIVE_LEVEL.PLAYER.LOOK_Y = e.getY();
				}
			}
			
			return;
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{
		mouseMoved(e);
	}
	
	public boolean hasMouseInBounds(MouseEvent e, int buttonX, int buttonY, int buttonW, int buttonH)
	{
		buttonX = (int) (((1f / NATIVE_WIDTH) * WIDTH * buttonX) + BORDER_WIDTH);
		buttonY = (int) (((1f / NATIVE_HEIGHT) * HEIGHT * buttonY) + BORDER_HEIGHT);
		buttonW = (int) ((1f / NATIVE_WIDTH) * WIDTH * buttonW);
		buttonH = (int) ((1f / NATIVE_HEIGHT) * HEIGHT * buttonH);
		
		if(e.getX() > buttonX && e.getX() < buttonX + buttonW)
		{
			if(e.getY() > buttonY && e.getY() < buttonY + buttonH)
			{
				return true;
			}
		}
		
		return false;
	}
	
	//====================================================================================================
	//========================================UNUSED METHODS==============================================
	//====================================================================================================
	
	public void mouseClicked(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}
	
	public void mouseExited(MouseEvent e) {}
	
	public void keyTyped(KeyEvent e) {}
}
