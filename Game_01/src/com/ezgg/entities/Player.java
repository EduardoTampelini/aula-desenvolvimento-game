package com.ezgg.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.ezgg.graficos.SpriteSheet;
import com.ezgg.main.Game;
import com.ezgg.word.Camera;
import com.ezgg.word.World;

public class Player extends Entity {
   public boolean right,left,up,down;
   public int right_dir=0,left_dir=1,up_dir=2,down_dir=3;
   public int dir=right_dir;
   public double speed =1;
   private int frames=0,maxframes=9,index=0,maxindex=3;
   private boolean moved=false;
   private BufferedImage[] rightPlayer;
   private BufferedImage[] leftPlayer;
   private BufferedImage[] upPlayer;
   private BufferedImage[] downPlayer;
   private BufferedImage playerDano;
   private BufferedImage playerDanoDown;
   private BufferedImage playerDanoLeft;
   private BufferedImage playerDanoUP;
   private boolean hasGun=false;
   public static int municao=0;
   public boolean isDano=false;
   private int danoFrames=0;
   public boolean shoot=false,mouseShoot=false;
   public double life=100,maxLife=100;
   public int mx , my;
	public Player(int x, int y, int largura, int altura, BufferedImage sprite) {
		super(x, y, largura, altura, sprite);
		
		rightPlayer=new BufferedImage[4];
		leftPlayer=new BufferedImage[4];
		upPlayer=new BufferedImage[4];
		downPlayer=new BufferedImage[4];
		playerDano= Game.spritesheet.getSprite(0, 32, 16, 16); 
		playerDanoDown=Game.spritesheet.getSprite(16, 32, 16, 16);
		playerDanoUP=Game.spritesheet.getSprite(0, 48, 16, 16);
		playerDanoLeft=Game.spritesheet.getSprite(16, 48, 16, 16);
		
        for(int i = 0;i<4;i++) {
		rightPlayer[i]= Game.spritesheet.getSprite(32 +(i*16),0,16,16);
        }
        for(int i = 0;i<4;i++) {
    		leftPlayer[i]= Game.spritesheet.getSprite(32 +(i*16),16,16,16);
            }
        for(int i = 0;i<4;i++) {
    		upPlayer[i]= Game.spritesheet.getSprite(32 +(i*16),32,16,16);
            }
        for(int i = 0;i<4;i++) {
    		downPlayer[i]= Game.spritesheet.getSprite(32 +(i*16),48,16,16);
            }
	}
	public void tick(){
		moved=false;
		if(right && World.isfree((int)(x+speed),this.getY())) {
			moved=true;
			dir=right_dir;
			x+=speed;
		}else if(left && World.isfree((int)(x-speed),this.getY())) {
			moved=true;
			dir=left_dir;
			x-=speed;
		}
		if(up && World.isfree(this.getX(),(int)(y-speed))) {
			moved=true;
			dir=up_dir;
			y-=speed;
		}else if(down && World.isfree(this.getX(),(int)(y+speed))) {
			moved=true;
			dir=down_dir;
			y+=speed;
		}
		if(moved) {
			frames++;
			if(frames==maxframes) {
				frames=0;
				index++;
				if(index>maxindex) {
					index=0;
				}
			}
		}
		if(life<=0) {
			life=0;
			Game.gameState="GAME_OVER";

		}
		this.checkColisionLife();
		this.checkColisionMunicao();
		this.checkColisionArma();
		if(isDano) {
			danoFrames++;
			if(danoFrames==8) {
				danoFrames=0;
				isDano=false;
			}
		}
		if(shoot ) {
			shoot=false;
		//System.out.println("ATIRAR");
		if(hasGun && municao>0) {
			municao--;
			int dx=0;
			int px=0;
			int py=5;
			if(dir==right_dir) {
				
				px=18;
				dx= 1;
			}else {
				 px=-8;
				 dx= -1;
			}
			BulletShoot bullet=new BulletShoot(this.getX()+px,this.getY()+py,4,1,null,dx,0);
			Game.bullets.add(bullet);
		}
		}
		if(mouseShoot) {
			mouseShoot=false;
			double angle=Math.atan2(my-(this.getY()+8-Camera.y), mx-(this.getX()+8-Camera.x));
			//System.out.println(angle);
			if(hasGun && municao>0) {
				municao--;
				
				double dx=Math.cos(angle);
				double dy=Math.sin(angle);
				int px=0;
				int py=5;
				if(dir==right_dir) {
					px=18;
					 dx= 1;
				}else {
					 px=-8;
					 dx= -1;
				}
				
				BulletShoot bullet=new BulletShoot(this.getX()+px,this.getY()+py,4,1,null,dx,dy);
				Game.bullets.add(bullet);
			}
		}
		updateCamera();
		
	}
	public void updateCamera() {
		Camera.x=Camera.clamp(this.getX()-(Game.largura/2),0,World.largura*16-Game.largura);
		Camera.y=Camera.clamp(this.getY()-(Game.altura/2),0,World.altura*16-Game.altura);
	}
	public void checkColisionLife() {
		for(int i=0;i<Game.entities.size();i++) {
			Entity atual=Game.entities.get(i);
			if(atual instanceof Life) {
				if(Entity.isColliding(this, atual)) {
					life+=10;
					if(life>100)
						life=100;
					Game.entities.remove(atual);
				}
			}
		}
	}
	public void checkColisionArma() {
		for(int i=0;i<Game.entities.size();i++) {
			Entity atual=Game.entities.get(i);
			if(atual instanceof Gun) {
				if(Entity.isColliding(this, atual)) {
					hasGun=true;
					
					Game.entities.remove(atual);
				}
			}
		}
	}
	public void checkColisionMunicao() {
		for(int i=0;i<Game.entities.size();i++) {
			Entity atual=Game.entities.get(i);
			if(atual instanceof Bullet) {
				if(Entity.isColliding(this, atual)) {
					municao+=3;
					//System.out.println("Munição atual"+municao);
					Game.entities.remove(atual);
				}
			}
		}
	}
	public void render(Graphics g){
		if(!isDano) {
		if(dir==right_dir) {
		g.drawImage(rightPlayer[index],this.getX()-Camera.x,this.getY()-Camera.y, null);
		if(hasGun) {
			g.drawImage(Entity.Arma_rigth,this.getX()+6-Camera.x,this.getY()-Camera.y, null);
		}
		}else if(dir==left_dir) {
		g.drawImage(leftPlayer[index],this.getX()-Camera.x,this.getY()-Camera.y, null);
        if(hasGun) {
        	g.drawImage(Entity.Arma_left,this.getX()-8-Camera.x,this.getY()-Camera.y, null);
        	
        	
		}
        
		}
		if(dir==up_dir) {
		g.drawImage(upPlayer[index],this.getX()-Camera.x,this.getY()-Camera.y, null);
		if(hasGun) {
			
		}
		}else if(dir==down_dir) {
			g.drawImage(downPlayer[index],this.getX()-Camera.x,this.getY()-Camera.y, null);
			if(hasGun) {
				g.drawImage(Entity.Arma_left,this.getX()+-1-Camera.x,this.getY()+3-Camera.y, null);
			}
			}
	}else if(isDano && dir==right_dir){
		g.drawImage(playerDano, this.getX()-Camera.x, this.getY()-Camera.y, null);
	}
		if(isDano && dir==down_dir) {
		g.drawImage(this.playerDanoDown, this.getX()-Camera.x, this.getY()-Camera.y, null);
		}if(isDano && dir==up_dir){
		g.drawImage(this.playerDanoUP, this.getX()-Camera.x, this.getY()-Camera.y, null);
		}if(isDano && dir==left_dir) {
		g.drawImage(this.playerDanoLeft, this.getX()-Camera.x, this.getY()-Camera.y, null);
		
		}
		if(isDano && dir==right_dir && hasGun){
			g.drawImage(Entity.ArmaDano_right, this.getX()+8-Camera.x, this.getY()-Camera.y, null);
		}
			if(isDano && dir==down_dir && hasGun) {
			g.drawImage(Entity.ArmaDano_left, this.getX()+-1-Camera.x,this.getY()+3-Camera.y, null);
			}
			if(isDano && dir==left_dir && hasGun) {
				g.drawImage(Entity.ArmaDano_left, this.getX()-8-Camera.x,this.getY()-Camera.y, null);
			}
		
		
	}
	
	}


