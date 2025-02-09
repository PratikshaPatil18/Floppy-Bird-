import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class FloppyBird extends JPanel implements ActionListener , KeyListener {
 
    //frame size

    int boardWidth=360;
    int boardHeight=640;

	//Image
				
	Image backgroundImg;
	Image birdImg;
	Image topPipeImage;
	Image bottomPipeImage;

    //bird class

    int birdX=boardWidth/8;
    int birdY=boardHeight/2;
    int birdWidth=34;
    int birdHeight=24;

    class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;
        
        Bird(Image img)
        {
            this.img=img;
        }
    }
    //pipe class
	Pipe pipe;
	int pipeX = boardWidth;
	int pipeY = 0;
	int pipeWidth = 64; //scaled by 1/4
	int pipeHeight = 512;
	
	public class Pipe 
	{
	            int x = pipeX;
				int y = pipeY;
				int width = pipeWidth;
				int height = pipeHeight;
				Image img;
				boolean passed = false;
						
						Pipe(Image img)
						{
							this.img=img;
						}
					}

				  //game logic
					Bird bird;
					int velocityX = -4; //move pips to the left speed(simulates bird moving right)
					int velocityY = 0;   //move bird up/down speed
					int gravity = 1;
				
					ArrayList<Pipe> pipes;
					Random random = new Random ();
					
					
				  //game timer
					Timer gameLoop;
					Timer placePipeTimer;
					
					boolean gameOver = false;
					double score= 0;
					
					
					
				
				   
					 FloppyBird()
					{
						setPreferredSize(new Dimension(boardWidth,boardHeight));
						//setBackground(Color.BLUE);
						
						setFocusable(true);
						addKeyListener(this);          //for moving bird
						
				
					  //load image
					backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
					birdImg = new ImageIcon( getClass().getResource("./flappybird.png")).getImage();
					topPipeImage = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
					bottomPipeImage = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
				
					//bird
					bird = new Bird(birdImg);
				
					pipes = new ArrayList<Pipe>();
					
					//place pipes timer
					placePipeTimer = new Timer(1500,new ActionListener()//1000/60=16.6
					{
						 
						 public void actionPerformed (ActionEvent e){
							 placePipes();
						 }
					});
					placePipeTimer.start();
					//timer
				
					 gameLoop = new Timer(1000/60,this);       //1000/60=16.6
					gameLoop.start();
					}
					
				
					void placePipes()
					{
						//(0-1) * pipeHeight/2 ->(0-256)
						//128
						//0-128 -(0-256) --> pipeHeight/4 -->3/4 pipeHeight
						
						
						int randomPipeY =(int) (pipeY -pipeHeight / 4 - Math.random() * (pipeHeight/2));
						int openingSpace = boardHeight/4;
						
						 Pipe topPipe= new Pipe (topPipeImage);
						topPipe.y=randomPipeY;
						pipes.add(topPipe);
						
						Pipe bottomPipe = new Pipe(bottomPipeImage);
						bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
						pipes.add(bottomPipe);
						
					}
					public void paintComponent(Graphics g)
					{
						super.paintComponent(g);
						draw (g);
				
					}
			
					public void draw(Graphics g)  
					{

						//background
						g.drawImage(backgroundImg,0,0,boardWidth,boardHeight,null); //top left 00
				
				
						//bird
						g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height,null); 
						
						//draw the pipes
						for(int i=0;i<pipes.size();i++)
						{
							Pipe pipe = pipes.get(i);
							
							g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
						}
						
						//score
						g.setColor(Color.white);
						g.setFont(new Font("Arial",Font.PLAIN,32));
						if(gameOver)
						{
							g.drawString("Game Over : " + String.valueOf((int) score),10,35);
						}else{
							g.drawString(String.valueOf((int) score),10,35);
				;		}
					}
				
					public void move()
					{	//bird
						velocityY += gravity;
						bird.y += velocityY;
						bird.y= Math.max(bird.y,0);
						
						//pipes
						for(int i=0;i<pipes.size();i++)
						{
							Pipe pipe = pipes.get(i);
							pipe.x = velocityX;
							//pipe.y = velocityY;
						}

		if(! pipe.passed && bird.x > pipe.x + pipe.width)
		{
			score += 0.5;     //bcz there are 2 pipes so 0.5 * 2= 1 , 1 for eact set of pipe
			pipe.passed = true;
			
		}
		
		if(collision(bird, pipe )){
			gameOver=true;
		}
		if(bird.y >boardHeight)
		{
			gameOver = true;
		}
    }

	public boolean collision(Bird a, Pipe b)
	{
		return a.x < b.x + b.width && // a's top left corner not reach to b's top right corner
				a.x + a.width > b.x && //a's top right pass b's top left corner
				a.y < b.y + b.height && //a's top left corner not reach b's bottom left corner
				a.y + a.height > b.y; //a's bottom left pass to b's top left corner
	}
    public void actionPerformed(ActionEvent e) {
        move(); //60 time/sec.
        repaint();
		
		if(gameOver)
		{
			placePipeTimer.stop();
			gameLoop.stop();
		}
    }

//pressing key to move bird

	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			//JUMP
			velocityY=-9;
			
			//restart the game
			if(gameOver)
			{
				bird.y =birdY;
				velocityY= 0;
				pipes.clear();
				score=0;
				gameOver = false;
				gameLoop.start();
				placePipeTimer.start();
			}
		}

	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
	}
	


}
