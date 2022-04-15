package com.ezgg.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;



public class Menu {
	
	public String[] options= {"Novo Jogo","Carregar Jogo","Sair"};
	public int currentOption=0;
	public int maxOption=options.length - 1;
	public boolean down,up,enter;
	public boolean pause=false;
	
public  void tick() {
	if(up) {
		up=false;
		currentOption--;
		if(currentOption<0) {
			currentOption=maxOption;
		}
	}
	if(down) {
		down=false;
		currentOption++;
		if(currentOption>maxOption) {
			currentOption=0;
		}
	}
	if(enter) {
		//Sound.music.loop();
		enter=false;
		if(options[currentOption]=="Novo Jogo"||options[currentOption]=="continuar") {
			Game.gameState="NORMAL";
			pause=false;
			
		}else if(options[currentOption]=="Sair") {
			System.exit(1);
		}
	}
	
}
public void render(Graphics g) {
	
	Graphics2D g2=(Graphics2D)g;
	g2.setColor(new Color(0,0,0,100));
	g2.fillRect(0, 0, Game.largura*Game.escala, Game.altura*Game.escala);
	g.setColor(Color.ORANGE);
	g.setFont(new Font("arial",Font.BOLD,40));
	g.drawString("GAME 01",(Game.largura*Game.escala)/2-85, (Game.altura*Game.escala)/2-150);
	//opções jogo
	g.setColor(Color.white);
	g.setFont(new Font("arial",Font.BOLD,40));
	if(pause==false) {
	g.drawString("Novo Jogo",(Game.largura*Game.escala)/2-100, 200);
	}else {
	g.drawString("Continuar",(Game.largura*Game.escala)/2-90, 200);
	}
	g.drawString("Carregar Jogo",(Game.largura*Game.escala)/2-130, 260);
	g.drawString("Sair",(Game.largura*Game.escala)/2-30, 320);
	
	if(options[currentOption]=="Novo Jogo") {
		if(pause==false) {
		g.setColor(Color.RED);
		g.drawString(">",(Game.largura*Game.escala)/2-140, 200);
		}else {
			g.setColor(Color.RED);
			g.drawString(">",(Game.largura*Game.escala)/2-130, 200);
		}
		
	}else if(options[currentOption]=="Carregar Jogo") {
		g.setColor(Color.RED);
		g.drawString(">",(Game.largura*Game.escala)/2-170, 260);
		
	}else if(options[currentOption]=="Sair") {
		g.setColor(Color.RED);
		g.drawString(">",(Game.largura*Game.escala)/2-70, 320);
		
	}
	
	
}
}
