package com.ezgg.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.ezgg.main.Game;
import com.ezgg.word.Camera;

public class Entity {
public static BufferedImage LIFEPACK_EN=Game.spritesheet.getSprite(96, 0, 16, 16);
public static BufferedImage GUN_EN=Game.spritesheet.getSprite(112, 0, 16, 16);
public static BufferedImage BULLETPACK_EN=Game.spritesheet.getSprite(96, 16, 16, 16);
public static BufferedImage ENEMY_EN=Game.spritesheet.getSprite(112, 16, 16, 16);
public static BufferedImage Arma_rigth=Game.spritesheet.getSprite(128, 0, 16, 16);
public static BufferedImage Arma_left=Game.spritesheet.getSprite(144, 0, 16, 16);
public static BufferedImage Arma_down=Game.spritesheet.getSprite(144, 32, 16, 16);
public static BufferedImage Inimigo_dano=Game.spritesheet.getSprite(112, 32, 16, 16);
public static BufferedImage ArmaDano_right=Game.spritesheet.getSprite(128, 32, 16, 16);
public static BufferedImage ArmaDano_left=Game.spritesheet.getSprite(144, 32, 16, 16);

protected double x;
protected double y;
protected int largura;
protected int altura;
private BufferedImage sprite;
private int maskx,masky,mAltura,mLargura;

public Entity(int x,int y, int largura,int altura, BufferedImage sprite) {
	this.x = x;
	this.y = y;
	this.largura = largura;
	this.altura = altura;
	this.sprite = sprite;
	
	this.maskx=0;
	this.masky=0;
	this.mAltura=altura;
	this.mLargura=largura;
	
}
public void setMask(int maskx,int masky,int mAltura,int mLargura) {
	this.maskx=maskx;
	this.masky=masky;
	this.mAltura=mAltura;
	this.mLargura=mLargura;
}
public void setX(int newX) {
	this.x=newX;
}
public void setY(int newY) {
	this.y=newY;
}
public int getX() {
	return (int)this.x;
}
public int getY() {
	return (int)this.y;
}
public int getLargura() {
	return this.largura;
}

public int getAltura() {
	return this.altura;
}


public void tick() {
	
	
}
public static boolean isColliding(Entity e1, Entity e2) {
	Rectangle e1mask=new Rectangle (e1.getX()+e1.maskx,e1.getY()+e1.masky,e1.mLargura,e1.mAltura);
	Rectangle e2mask=new Rectangle (e2.getX()+e2.maskx,e2.getY()+e2.masky,e2.mLargura,e2.mAltura);
	 return e1mask.intersects(e2mask);
}
public void render(Graphics g) {
	g.drawImage(sprite, this.getX()-Camera.x,this.getY()-Camera.y, null);
	
   // g.setColor(Color.red);
    //g.fillRect(this.getX()+this.maskx-Camera.x,this.getY()+this.masky-Camera.y, mLargura, mAltura);
}

}
