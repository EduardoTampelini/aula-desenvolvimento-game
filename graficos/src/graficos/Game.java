package graficos;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning=true;
	private final int largura=240;
	private final int altura=160;
	private final int escala=3;
	private BufferedImage image;
	private Spritesheet spritesheet;
	private BufferedImage[] player;
	private int frames=0;
	private int maxframes=2;
	private int curAnimation=0,maxAnimation=8;
	
	public Game() {
		spritesheet=new Spritesheet("/sprit2.png");
		player= new BufferedImage[9];
		player[0]=spritesheet.getSprite(0, 0, 25, 25);
		player[1]=spritesheet.getSprite(25, 0, 25, 25);
		player[2]=spritesheet.getSprite(50, 0, 25, 25);	
		player[3]=spritesheet.getSprite(75, 0, 25, 25);	
		player[4]=spritesheet.getSprite(100, 0, 25, 25);	
		player[5]=spritesheet.getSprite(125, 0, 25, 25);	
		player[6]=spritesheet.getSprite(150, 0, 25, 25);	
		player[7]=spritesheet.getSprite(175, 0, 25, 25);	
		player[8]=spritesheet.getSprite(200, 0, 25, 25);	
		this.setPreferredSize(new Dimension(largura*escala,altura*escala));
		initFrame();
		image=new BufferedImage(largura,altura,BufferedImage.TYPE_INT_RGB);
		
	}
	public void initFrame() {
		frame= new JFrame("game #1");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	public synchronized void start() {
		thread = new Thread(this);
		isRunning=true;
		thread.start();
	}
	public synchronized void stop() {
		isRunning=false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
public static void main(String[]args) {
	Game game=new Game();
	game.start();
	//game.stop();
}
public void tick() {
	frames++;
	if(frames>maxframes) {
		frames=0;
		curAnimation++;
		if(curAnimation>maxAnimation) {
			curAnimation=0;
		}
	}
}
public void render() {
	BufferStrategy bs =this.getBufferStrategy();
	if (bs==null) {
		this.createBufferStrategy(3);
		return;
	}
	Graphics g =image.getGraphics();
	g.setColor(new Color(0,0,255));
	g.fillRect(0, 0, largura, altura);
	//renderização do jogo
	Graphics2D g2 =(Graphics2D) g;
	g2.drawImage(player[curAnimation], 90, 90, null);
	//---------------------
	g.dispose();//metodo de otimização da imagem que sera alocada no jogo
	g=bs.getDrawGraphics();
	g.drawImage(image,0,0,largura*escala,altura*escala,null);
	bs.show();
}
	public void run() {
		long lastTime=System.nanoTime();
		double amountOfTicks=60;
		double ns = 1000000000/amountOfTicks;
		double delta=0;
		int frames=0;
		double timer=System.currentTimeMillis();
	while (isRunning) {
		long now=System.nanoTime();
		delta+=(now-lastTime)/ns;
		lastTime=now;
		if(delta>=1) {
			tick();
			render();
			frames++;
			delta--;
		}
		if(System.currentTimeMillis()-timer>=1000) {
			System.out.println("FPS: "+frames);
			frames=0;
			timer=System.currentTimeMillis();
		}
	}
	stop();	
	}

}
