package Rainfall;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import java.util.*;
import java.awt.event.*;

@SuppressWarnings("serial")

public class Rainfall_Runner extends PApplet implements KeyListener
{
	//importing visuals
	PImage bucket = loadImage("waterbucket.png");
	PImage waterDrop = loadImage("waterDrop.png");
	PImage badRain = loadImage("badRain.png");
	PImage rain = loadImage("ring.png");
	PImage Stage1 = loadImage("Stage1.png");
	PImage Stage2 = loadImage("Stage2.png");
	PImage Stage3 = loadImage("Stage3.png");
	PImage Stage4 = loadImage("Stage4.png");
	PImage Stage5 = loadImage("Stage5.png");
	PImage Stage6 = loadImage("Stage6.png");
	
	//variables
	int bucketX = 800;
	int bucketY = 400;
	int bucketSpeed = 50;
	int numOfLives = 3;
	int numOfRaindrops = 0;
	int numOfRaindropsPartTwo = 0;
	
	int badRainMax = 3;
	int[] badRainX = new int[badRainMax];
	int[] badRainY = new int[badRainMax];
	
	int waterDropMax = 5;
	int[] waterDropX = new int[waterDropMax];
	int[] waterDropY = new int[waterDropMax];
	int waterDropIterator = 0;
	
	int circleMax = 100;
	int[] circleX = new int[circleMax];
	int[] circleY = new int[circleMax];
	int circleIterator = 0;
	
	boolean endPartOne;
	boolean levelTwo;
	int whatTime;
	boolean goDrop;
	Random randomNumberGenerator = new Random();
	
	PFont myFont = createFont("Arial", 44);
	
	public void setup()
	{
		frameRate(60);
		size(1000, 500);
		//initializes necessary variables
		endPartOne = false;
		levelTwo = false;
		whatTime = 0;
		goDrop = false;
		background(154, 255, 247);
		//starting locations of the rain drops for the first level
		for(int i = 0; i < badRainMax; i++) {
			badRainX[i] = randomNumberGenerator.nextInt(1000);
			badRainY[i] = -randomNumberGenerator.nextInt(100);
		}
		for(int a = 0; a < waterDropMax; a++) {
			waterDropX[a] = randomNumberGenerator.nextInt(1000);
			waterDropY[a] = -randomNumberGenerator.nextInt(100);
		}
		
	}
	
	public void draw()
	{
		background(154, 255, 247);
		
		if(endPartOne == false && levelTwo == false) {
				for(int i=0;i < circleMax; i++)
				{
					for(int j=0;j < badRainMax; j++)
					{
						//checks for collision between black raindrop and ring
						if((circleX[i] > (badRainX[j] - 50)) && (circleX[i] < (badRainX[j] + 50))
							&& (circleY[i] > (badRainY[j] - 50)) && (circleY[i] < (badRainY[j] + 50)))
						{
							//when ring and black raindrop collide
							numOfRaindrops++;
					
							//move the black raindrop
							badRainX[j] = randomNumberGenerator.nextInt(900);
							badRainY[j] = -20;
					
							//deletes the ring
							circleX[i]=-50;
							circleY[i]=-50;
						}
					}
			 
					if(circleX[i] < 0)//ring is off the screen. delete it.
					{
						//this code deletes the ring
						circleX[i]=-50;
						circleY[i]=-50;
					}
					else//draws the ring
					{
						drawRing(circleX[i], circleY[i]);
						
						//moves the ring
						circleY[i]-=10;
					}
				}
				for(int a=0;a < badRainMax; a++)
				{
					//checks for collision between black raindrop and watering can
					if((bucketX > (badRainX[a] - 80)) && (bucketX < (badRainX[a] + 80))
						&& (bucketY > (badRainY[a] - 80)) && (bucketY < (badRainY[a] + 80)))
					{
						//if collision between black raindrop and watering can
						badRainX[a] = randomNumberGenerator.nextInt(900);
						badRainY[a] = -20;
						numOfLives--;
					}
					
					//draws the black raindrop
					drawBadRain(badRainX[a], badRainY[a]);
				
					//move black raindrop
					badRainY[a]+= 3;
				
					//if black raindrop moves past bottom of screen, regenerate it at top of screen w/ random location
					if(badRainY[a] > 600) {
						badRainY[a] = -20;
						badRainX[a] = randomNumberGenerator.nextInt(900);}
				}
			
				for(int a=0;a < waterDropMax; a++)
				{
					//when blue drop is caught or collision between blue drop and watering can
					if((bucketX > (waterDropX[a] - 80)) && (bucketX < (waterDropX[a] + 80))
						&& (bucketY > (waterDropY[a] - 80)) && (bucketY < (waterDropY[a] + 80)))
					{
						//add 1 to score and also regenerate
						waterDropX[a] = randomNumberGenerator.nextInt(900);
						waterDropY[a] = -20;
						numOfRaindrops++;
					}
					
					//draw the blue raindrop
					drawWaterDrop(waterDropX[a], waterDropY[a]);
				
					//move blue drop
					waterDropY[a]+= 3;
				
					//if blue drop moves past bottom of screen, regenerate it at top of screen w/ a random location
					if(waterDropY[a] > 600) {
						waterDropY[a] = -20;
						waterDropX[a] = randomNumberGenerator.nextInt(900);}
				}
				
				//draws the bucket
				drawBucket(bucketX, bucketY);
				
				//displays the score
				textFont(myFont);
				text("Score: " + numOfRaindrops, 20, 450, 500, 500);
				
				//displays the lives
				for(int b=0;b<numOfLives;b++)
				{
					image(bucket, 900-b*50, 450, 14*3, 13*3);
				}
				
				if(numOfRaindrops >= 20 || numOfLives == 0) {
					endPartOne = true;
				}
			}
			//switches to second part
			else if(endPartOne == true && levelTwo == false) {
				levelTwo = true;
			}
			//second part: growing the plant
			else if(endPartOne == true && levelTwo == true ) {
				whatTime++;
				//initial setup of the screen and variables
				if(whatTime == 1) {
					startPartTwo();
					delay(1000); 
					background(154, 255, 247);
				}
				else{
					wateringTree();
					text("Remaining drops: " + (numOfRaindrops - numOfRaindropsPartTwo), 20, 450, 500, 500);
					//when mouse is clicked
					if(goDrop == true) {
						frameRate(5);
						drawWaterDrop(500,80);
						goDrop = false;
					}
				}
			}
		}
		
		//chooses which image to display
		public void wateringTree() {
			if(numOfRaindropsPartTwo < 3 && numOfRaindropsPartTwo > 0) {
				drawOne(360,15);
//				System.out.println("3");
			}
			else if(numOfRaindropsPartTwo >= 3 && numOfRaindropsPartTwo < 6) {
				drawTwo(290,15);
//				System.out.println("3+");
			}
			else if(numOfRaindropsPartTwo >= 6 && numOfRaindropsPartTwo < 9) {
				drawThree(200,15);
//				System.out.println("6+");
			}
			else if(numOfRaindropsPartTwo >= 9 && numOfRaindropsPartTwo < 12) {
				drawFour(110,15);
//				System.out.println("9+");
			}
			else if(numOfRaindropsPartTwo >= 12 && numOfRaindropsPartTwo < 15) {
				drawFive(10,15);
//				System.out.println("12+");
			}
			else if(numOfRaindropsPartTwo >= 15){
				drawSix(-120,15);
//				System.out.println("15+");
			}
//			System.out.println("num of raindrops caught" + numOfRaindropsPartTwo);
		}
		

	////////////////////////////////////////////////////

	public void mousePressed(MouseEvent e)
	{
		if ( levelTwo == true && numOfRaindropsPartTwo != numOfRaindrops) {
//			System.out.println("drawn!");
			goDrop = true;
			numOfRaindropsPartTwo++;
		}
	}
	
	public void keyReleased (KeyEvent e)
	{		
		int key = e.getKeyCode();
		if (key != KeyEvent.VK_UNDEFINED)
		{
			//bucket moves right
			if (key == KeyEvent.VK_RIGHT)
			{
				//System.out.println("Right Arrow pressed!");
				bucketX += bucketSpeed;
			}
			//bucket moves left
			if (key == KeyEvent.VK_LEFT)
			{
				//System.out.println("Left Arrow pressed!");
				bucketX -= bucketSpeed;
			}
			//ring shooter
			if (key == KeyEvent.VK_SPACE)
			{
				if(circleIterator >= 100)
				{
					circleIterator = 0;
				}

				circleX[circleIterator] = bucketX;
				circleY[circleIterator] = bucketY;
				circleIterator++;
				//System.out.println("SpaceBar pressed!");
			}
		}
	}

	////////////////////////////////////////////////////
	
	public void drawBucket(int x, int y)
	{
		image(bucket, x-70, y-80, 200, 150);
	}

	public void drawBadRain(int x, int y)
	{
		image(badRain, x,y,70,65);
	}
		
	public void drawWaterDrop(float x, float y)
	{
		image(waterDrop,x,y,40,60);
	}
	
	public void drawRing(int x, int y) {
		image(rain, x,y,50,50);
	}
	
	//the different stages of the flower
	public void drawOne(int x, int y) {
		image(Stage1, x, y, 700, 500);
	}
	public void drawTwo(int x, int y) {
		image(Stage2, x, y, 700, 500);
	}
	public void drawThree(int x, int y) {
		image(Stage3, x, y, 700, 500);
	}
	public void drawFour(int x, int y) {
		image(Stage4, x, y, 700, 500);
	}
	public void drawFive(int x, int y) {
		image(Stage5, x, y, 700, 500);
	}
	public void drawSix(int x, int y) {
		image(Stage6, x, y, 700, 500);
	}
	//starts part two
	public void startPartTwo() {
		endPartOne = true;
	}
	
}
