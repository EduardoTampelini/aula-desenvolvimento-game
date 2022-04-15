package com.ezgg.word;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.ezgg.entities.*;
import com.ezgg.graficos.SpriteSheet;
import com.ezgg.main.Game;

public class World {
	private static Tile[] tiles;
	public static int largura,altura;
	public static final int tile_size=16;
	
public World(String path) {
	try {
		BufferedImage map=ImageIO.read(getClass().getResource(path));
		int[] pixels=new int[map.getWidth()*map.getHeight()];
		largura=map.getWidth();
		altura=map.getHeight();
		tiles=new Tile[map.getWidth()*map.getHeight()];
		map.getRGB(0, 0,map.getWidth(), map.getHeight(),pixels, 0, map.getWidth());
		for(int xx=0;xx<map.getWidth();xx++) {
			for(int yy=0;yy<map.getHeight();yy++) {
				int pixelAtual=pixels[xx+ (yy*map.getWidth())];
				tiles[xx + (yy*largura)]= new ChaoTile(xx*16,yy*16,Tile.TILE_CHAO);
				if(pixelAtual==0xFF000000) {
					//chão
					tiles[xx + (yy*largura)]= new ChaoTile(xx*16,yy*16,Tile.TILE_CHAO);
				}else if(pixelAtual==0xFFFFFFFF) {
					//parede
					tiles[xx + (yy*largura)]= new ParedeTile(xx*16,yy*16,Tile.TILE_PAREDE);
				}else if(pixelAtual==0xFFB200FF) {
					//pedra
					tiles[xx + (yy*largura)]= new ChaoTile(xx*16,yy*16,Tile.TILE_PEDRA);
				}else if(pixelAtual==0xFF0026FF) {
					//pedra
					tiles[xx + (yy*largura)]= new ChaoTile(xx*16,yy*16,Tile.TILE_PEDRA2);
				}else if(pixelAtual==0xFFFF0000) {
					//jogador
				Game.player.setX(xx*16);
				Game.player.setY(yy*16);
					
				}else if(pixelAtual==0xFF4CFF00){
					//inimigo
					Enemy en=new Enemy(xx*16,yy*16,16,16,Entity.ENEMY_EN);
					Game.entities.add(en);
					Game.enemies.add(en);
				}else if(pixelAtual==0xFFFF6A00) {
					//arma
					Game.entities.add(new Gun(xx*16,yy*16,16,16,Entity.GUN_EN));
				}else if(pixelAtual==0xFFFF7F7F) {
					//pacote de vida
					Game.entities.add(new Life(xx*16,yy*16,16,16,Entity.LIFEPACK_EN));
				}else if (pixelAtual==0xFFFFD800) {
					//munição
					Game.entities.add(new Bullet(xx*16,yy*16,16,16,Entity.BULLETPACK_EN));
				}
			}
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
	}
	public static boolean isfree (int xnext,int ynext){
		int x1=xnext / tile_size;
		int y1=ynext / tile_size;
		
		int x2=(xnext+tile_size-1) / tile_size;
		int y2=ynext / tile_size;
		
		int x3=xnext / tile_size;
		int y3=(ynext + tile_size-1) / tile_size;
		
		int x4=(xnext + tile_size-1) / tile_size;
		int y4=(ynext + tile_size-1)  / tile_size;

		return !((tiles[x1+(y1*World.largura)] instanceof ParedeTile)||
				 (tiles[x2+(y2*World.largura)] instanceof ParedeTile)||
				 (tiles[x3+(y3*World.largura)] instanceof ParedeTile)||
			     (tiles[x4+(y4*World.largura)] instanceof ParedeTile));
	
}
	public static void restartGame(String level) {
		Game.entities.clear();
		Game.enemies.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet=new SpriteSheet("/spritesheet.png");
		Game.player = new Player(0,0,16,16,Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world=new World("/"+level);
		return;
	}
public void render(Graphics g) {
	int xStart=Camera.x/16;
	int yStart=Camera.y/16;
	
	int xFinal=xStart+(Game.largura/16);
	int yFinal=yStart+(Game.altura/16);
	
for(int xx=xStart;xx<=xFinal;xx++) {
	for(int yy=yStart;yy<=yFinal;yy++) {
		if(xx <0 || yy < 0 || xx>=largura|| yy>=altura) {
			continue;
		}
		Tile tile= tiles[xx + (yy*largura)];
		tile.render(g);
		
	}
}
}
}
