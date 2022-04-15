package pong;

import java.awt.Color;
import java.awt.Graphics;

public class Player {
 
	public boolean right,left;
	public int x,y;
	public int largura, altura;
	public Player(int x,int y) {
		this.x = x ;
		this.y = y ;
		this.largura=40;
		this.altura=5;
		
	}
	public void tick() {
		if(right==true) {
			x++;
		}
		else if(left==true) {
			x--;
		}
		
		if(x+largura> Game.largura) {
			x=Game.largura-largura;
		}else if(x<0) {
			x=0;
		}
		
	}
	public void render(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(x, y, largura, altura);
	}
}
