package pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Enemy {
	public double x,y;
	public int largura, altura;
	public Enemy(int x,int y) {
		this.x = x;
		this.y = y;
		this.largura=40;
		this.altura=5;
		
	}
public void tick() {
	x+=(Game.ball.x-x-6)*0.1;
	if(x+largura> Game.largura) {
		x=Game.largura-largura;
	}else if(x<0) {
		x=0;
	}
	
}
public void render(Graphics g) {
	g.setColor(Color.RED);
	g.fillRect((int)x,(int)y, largura, altura);
	
}
}
