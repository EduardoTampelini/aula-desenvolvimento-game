package com.ezgg.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.ezgg.main.Game;
//import com.ezgg.main.Sound;
import com.ezgg.word.Camera;
import com.ezgg.word.World;

public class Enemy extends Entity {
private double speed=0.7;
private int maskx=7,masky=5,maskl=5,maska=7;
private int frames=0,maxframes=15,index=0,maxindex=2;
private BufferedImage[] sprites;
private int life=2;
private boolean isDamaged=false;
private int damageFrame=10,damageCurrent=0;

	public Enemy(int x, int y, int largura, int altura, BufferedImage sprite) {
		super(x, y, largura, altura, null);
		sprites=new BufferedImage[3];
		sprites[0]=Game.spritesheet.getSprite(112, 16, 16, 16);
		sprites[1]=Game.spritesheet.getSprite(128, 16, 16, 16);
		sprites[2]=Game.spritesheet.getSprite(144, 16, 16, 16);

	}
	public void tick() {
		/*maskx=8;
		masky=8;
		maskl=5;
		maska=5;
		if(Game.rand.nextInt(100)<30) {*/
		if(this.iscollidingwithPlayer()==false) {
		if((int)x<Game.player.getX() && World.isfree((int)(x+speed), this.getY())
				&& !iscoliding((int)(x+speed), this.getY())) {
			x+=speed;
		}else if((int)x>Game.player.getX() && World.isfree((int)(x-speed), this.getY())
				&& !iscoliding((int)(x-speed), this.getY())) {
			x-=speed;
		}
		if((int)y<Game.player.getY() && World.isfree(this.getX(), (int)(y+speed))
				&& !iscoliding(this.getX(), (int)(y+speed))){
			y+=speed;
		}else if((int)y>Game.player.getY() && World.isfree(this.getX(), (int)(y-speed))
				&& !iscoliding(this.getX(), (int)(y-speed))) {
			y-=speed;
		}
		}else {
			if(Game.rand.nextInt(100)<10) {
			Game.player.life-=10;
			Game.player.isDano=true;
			
			
			}
			
		}
		
			frames++;
			if(frames==maxframes) {
				frames=0;
				index++;
				if(index>maxindex) {
					index=0;
				}	
			}
			collidingBullet();
			
			if(life<=0) {
				destroySelf();
				return;
			}
			if(isDamaged) {
				this.damageCurrent++;
				if(this.damageCurrent==this.damageFrame) {
					this.damageCurrent=0;
					this.isDamaged=false;
				}
			}
			
			
		}
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
			
	
	}	
	
	public void collidingBullet() {
		for(int i = 0;i < Game.bullets.size();i++) {
			Entity e = Game.bullets.get(i);
			if(Entity.isColliding(this, e)) {
				//Sound.atingir.play();
				isDamaged=true;
				life--;
				//System.out.println("Colidindo");
				Game.bullets.remove(i);
				return;
			
			}
		
		}
		
		
	
	}
	public boolean iscollidingwithPlayer() {
		 Rectangle enemyCurrent=new Rectangle(this.getX()+maskx,this.getY()+masky,maskl,maska);
		Rectangle player = new Rectangle(Game.player.getX(),Game.player.getY(),16,16);
		
		return enemyCurrent.intersects(player);
	}
   public boolean iscoliding(int xnext,int ynext){
	   Rectangle enemyCurrent=new Rectangle(xnext+maskx,ynext+masky,maskl,maska);
	   
	   for(int i=0;i<Game.enemies.size();i++) {
		   Enemy e = Game.enemies.get(i);
		  if(e==this) {
			  continue;
		  }
		  Rectangle targetEnemy=new Rectangle(e.getX()+maskx,e.getY()+masky,maskl,maska);
		  if(enemyCurrent.intersects(targetEnemy)) {
			  return true;
		  }
	   }
	   return false;
   }
   public void render(Graphics g) {
	   if(!isDamaged) {
	   g.drawImage(sprites[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
	   }else {
	   g.drawImage(Entity.Inimigo_dano, this.getX()-Camera.x, this.getY()-Camera.y, null);
	   }
	//   g.setColor(Color.BLUE);
	//   g.fillRect(this.getX()+maskx-Camera.x, this.getY()+masky-Camera.y, maskl, maska);

   }
   }

