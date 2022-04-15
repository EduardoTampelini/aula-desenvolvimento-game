package com.ezgg.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
//import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
//import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.ezgg.entities.Enemy;
import com.ezgg.entities.BulletShoot;
import com.ezgg.entities.Entity;
import com.ezgg.entities.Player;
import com.ezgg.graficos.SpriteSheet;
import com.ezgg.graficos.UI;
//import com.ezgg.main.Sound;
//import com.ezgg.word.Camera;
import com.ezgg.word.World;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener {

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int largura = 240;
	public static final int altura = 160;
	public static final int escala = 3;
	private int cur_level=1,max_level=2;
	private BufferedImage image;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot>bullets;
	public  static SpriteSheet spritesheet;
	public static World world;
	public static Player player;
	public static Random rand;
	public UI ui;
	public static String gameState="MENU";
	private boolean showMessageGameOver=true;
	private int framesGameOver=0;
	private boolean restartGame=false;
	public Menu menu;
	
	

	public Game() {
		rand=new Random();
        addKeyListener(this);
        addMouseListener(this);
		this.setPreferredSize(new Dimension(largura * escala, altura * escala));
		initFrame();
		// inicializando objetos
		ui=new UI();
		image = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets =new ArrayList<BulletShoot>();
		menu = new Menu();
		spritesheet=new SpriteSheet("/spritesheet.png");
		player = new Player(0,0,16,16,spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world=new World("/level1.png");

	}

	public void initFrame() {
		frame = new JFrame("game #1");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
		// game.stop();
	}

	public void tick() {
		if(gameState=="NORMAL") {
			this.restartGame=false;
		for (int i = 0; i < entities.size(); i++) {
			Entity e =entities.get(i);
			e.tick();
		}
		for(int i=0;i<bullets.size();i++) {
			bullets.get(i).tick();
		}
		if(enemies.size()==0) {
			//proximo level
			cur_level++;
			if(cur_level>max_level) {
				cur_level=1;
			}
			String newWord="level"+cur_level+".png";
			world.restartGame(newWord);
			
		}
		}else if (gameState=="GAME_OVER") {
			this.framesGameOver++;
			if(this.framesGameOver==30) {
				framesGameOver=0;
				if(this.showMessageGameOver) {
					this.showMessageGameOver=false;
				}else {
					this.showMessageGameOver=true;
				}
			}
			if(restartGame) {
				this.restartGame=false;
				this.gameState="NORMAL";
				cur_level=1;
				String newWord="level"+cur_level+".png";
				world.restartGame(newWord);
			}
		}else if(gameState=="MENU") {
			menu.tick();
		}
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, largura, altura);
		world.render(g);
		for (int i = 0; i < entities.size(); i++) {
			Entity e =entities.get(i);
			e.render(g);
		}
		for(int i=0;i<bullets.size();i++) {
			bullets.get(i).render(g);
		}
		ui.render(g);
		g.dispose();// metodo de otimização da imagem que sera alocada no jogo
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, largura * escala, altura * escala, null);
		g.setFont(new Font("arial",Font.BOLD,20));
		g.setColor(Color.white);
		g.drawString("Munição: "+Player.municao, 600, 20);
		if(gameState=="GAME_OVER") {
			Graphics2D g2= (Graphics2D)g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, largura*escala,altura*escala);
			g.setFont(new Font("arial",Font.BOLD,50));
			g.setColor(Color.white);
			g.drawString("GAME OVER", (largura*escala)/2-150, (altura*escala)/2-5);
			g.setFont(new Font("arial",Font.BOLD,40));
			if(this.showMessageGameOver) {
			g.drawString("> Pressione enter para continuar <", (largura*escala)/2-323, (altura*escala)/2+100);
			}
		}else if(gameState=="MENU") {
			menu.render(g);
		}
		bs.show();
	}

	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer = System.currentTimeMillis();
			}
		}
		stop();
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
       if(e.getKeyCode()== KeyEvent.VK_RIGHT || e.getKeyCode()==KeyEvent.VK_D) {
    	 player.right=true;
       }else if (e.getKeyCode()== KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_A) {
    	   player.left=true;
       }
       if(e.getKeyCode()== KeyEvent.VK_UP || e.getKeyCode()==KeyEvent.VK_W) {
    	   player.up=true;
    	   if(gameState=="MENU") {
    		   menu.up=true;
    	   }
       }else if(e.getKeyCode()== KeyEvent.VK_DOWN || e.getKeyCode()==KeyEvent.VK_S) {
    	   player.down=true;
    	   if(gameState=="MENU") {
    		   menu.down=true;
    	   }
       }
       if(e.getKeyCode()==KeyEvent.VK_X ) {
    	  // Sound.flecha.play();
    	   player.shoot=true;
    	  
       }
       if(e.getKeyCode()==KeyEvent.VK_ENTER) {
    	   this.restartGame=true;
    	   if(gameState=="MENU") {
    		   menu.enter=true;
    	   }
       }
       if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
    	   gameState="MENU";
    	   menu.pause=true;
       }
		
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()== KeyEvent.VK_RIGHT || e.getKeyCode()==KeyEvent.VK_D) {
	    	 player.right=false;
	       }else if (e.getKeyCode()== KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_A) {
	    	   player.left=false;
	       }
	       if(e.getKeyCode()== KeyEvent.VK_UP || e.getKeyCode()==KeyEvent.VK_W) {
	    	   player.up=false;
	       }else if(e.getKeyCode()== KeyEvent.VK_DOWN || e.getKeyCode()==KeyEvent.VK_S) {
	    	   player.down=false;
	       }
			
		
	}

	
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void mousePressed(MouseEvent e) {
	//Sound.flecha.play();
	player.mouseShoot=true;
	player.mx=(e.getX()/3);
	player.my=(e.getY()/3);
	
		
	}

	
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}

