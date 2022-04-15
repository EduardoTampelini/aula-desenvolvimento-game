package pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Ball {
	public double x,y;
	public int largura, altura;
	public double dx,dy;
	public double speed=1.7;
	public Ball(int x,int y) {
		this.x = x;
		this.y = y;
		this.largura=4;
		this.altura=4;
		
		int angulo= new Random().nextInt(120 -45)+45+1;;
		dx= Math.cos(Math.toRadians(angulo));
		dy= Math.sin(Math.toRadians(angulo));
		
	}
public void tick() {
	if(x+(dx*speed)+largura>=Game.largura) {
		dx*=-1;
	}else if(x+(dx*speed)<0) {
		dx*=-1;
	}
	if(y>=Game.altura) {
		//ponto do inimigo
		System.out.println("Ponto do inimigo");
		new Game();
		return;
	}else if(y<0) {
		//ponto jogador
		System.out.println("Ponto do jogador");
		new Game();
		return;
	}
	Rectangle baunds = new Rectangle((int)(x+(dx*speed)),(int)(y+(dy*speed)),largura, altura);
	Rectangle baundsPlayer = new Rectangle(Game.player.x,Game.player.y,Game.player.largura,Game.player.altura);
	Rectangle baundsenemy = new Rectangle((int)Game.enemy.x,(int)Game.enemy.y,Game.enemy.largura,Game.enemy.altura);
	
	if(baunds.intersects(baundsPlayer)) {
		int angulo= new Random().nextInt(120 -45)+45+1;;
		dx= Math.cos(Math.toRadians(angulo));
		dy= Math.sin(Math.toRadians(angulo));
		if(dy>0)
		dy*=-1;
	}else if(baunds.intersects(baundsenemy)) {
		int angulo= new Random().nextInt(120 -45)+45+1;;
		dx= Math.cos(Math.toRadians(angulo));
		dy= Math.sin(Math.toRadians(angulo));
		if(dy<0)
		dy*=-1;
	}
	
	x+=dx*speed;
	y+=dy*speed;
}
public void render(Graphics g) {
	g.setColor(Color.YELLOW);
	g.fillRect((int)x,(int)y, largura, altura);
	
}
}
