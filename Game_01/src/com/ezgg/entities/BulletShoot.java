package com.ezgg.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.ezgg.main.Game;
import com.ezgg.word.Camera;

public class BulletShoot extends Entity{
	private double dx;
	private double dy;
	private double spd=4;//Speed
	private int life=25,curLife=0;
	public BulletShoot(int x, int y, int largura, int altura, BufferedImage sprite,double dx,double dy) {
		super(x, y, largura, altura, sprite);
		
this.dx=dx;
this.dy=dy;
	}
	
public void tick() {
	y+=dy*spd;
	x+=dx*spd;
	curLife++;
	if(curLife==life) {
		Game.bullets.remove(this);
		return;
	}
}
public void render(Graphics g) {
	
	g.setColor(Color.GRAY);
	g.fillRect(this.getX()-Camera.x,this.getY()-Camera.y,largura ,altura);
	
	
}
}
