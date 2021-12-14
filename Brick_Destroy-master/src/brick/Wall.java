/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2017  Filippo Ranza
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package brick;

import java.awt.*;
import java.awt.geom.Point2D;


import ball.Ball;
import ball.RubberBall;
import player.Player;


public class Wall 
{

   

    
    private Rectangle area;

    private Brick[] bricks;
    private Ball ball;
    private Player player;

    private Brick[][] levels;
    private int level;
    

    private Point startPoint;
    private int brickCount;
    private int ballCount;
    private boolean ballLost;
    
    private int speedX;
    private int speedY;

    public Wall(Rectangle drawArea, Point ballPos)
    {
    	
        this.startPoint = new Point(ballPos);

        ballCount = 3;
        ballLost = false;
       
        
        speedX = 4;
        speedY = -4;
        

        makeBall(ballPos);
        

        ball.setSpeed(speedX,speedY);

        player = new Player((Point) ballPos.clone(),150,10, drawArea);

        area = drawArea;


    }


    private void makeBall(Point2D ballPos)
    {
        ball = new RubberBall(ballPos);
    }


    public void move()
    {
        player.move();
        ball.move();
    }

    public void findImpacts()
    {
        if(player.impact(ball))
        {
            ball.reverseY();
        }
        else if(impactWall())
        {
            /*for efficiency reverse is done into method impactWall
            * because for every brick program checks for horizontal and vertical impacts
            */
            brickCount--;
        }
        else if(impactBorder()) 
        {
            ball.reverseX();
        }
        else if(ball.getPosition().getY() < area.getY())
        {
            ball.reverseY();
        }
        else if(ball.getPosition().getY() > area.getY() + area.getHeight())
        {
            ballCount--;
            ballLost = true;
        }
    }

    private boolean impactWall()
    {
        for(Brick b : bricks){
            switch(b.findImpact(ball)) 
            {
                //Vertical Impact
                case Brick.UP_IMPACT:
                    ball.reverseY();
                    return b.setImpact(ball.getDown(), Crack.UP);
                case Brick.DOWN_IMPACT:
                    ball.reverseY();
                    return b.setImpact(ball.getUp(),Crack.DOWN);

                //Horizontal Impact
                case Brick.LEFT_IMPACT:
                    ball.reverseX();
                    return b.setImpact(ball.getRight(),Crack.RIGHT);
                case Brick.RIGHT_IMPACT:
                    ball.reverseX();
                    return b.setImpact(ball.getLeft(),Crack.LEFT);
            }
        }
        return false;
    }

    private boolean impactBorder()
    {
        Point2D p = ball.getPosition();
        return ((p.getX() < area.getX()) ||(p.getX() > (area.getX() + area.getWidth())));
    }

    public int getBrickCount()
    {
        return brickCount;
    }

    public int getBallCount()
    {
        return ballCount;
    }

    public boolean isBallLost()
    {
        return ballLost;
    }

    public void ballReset()
    {
        player.moveTo(startPoint);
        ball.moveTo(startPoint);
        ball.setSpeed(speedX,speedY);
        ballLost = false;
    }

    public void wallReset()
    {
        for(Brick b : bricks)
            b.repair();
        brickCount = bricks.length;
        ballCount = 3;
    }

    public boolean ballEnd()
    {
        return ballCount == 0;
    }

    public boolean isDone()
    {
        return brickCount == 0;
    }

    public void nextLevel()
    {
        bricks = levels[level++];
        this.brickCount = bricks.length;
    }

    public boolean hasLevel()
    {
        return level < levels.length;
    }

    public void setBallXSpeed(int s)
    {
        ball.setXSpeed(s);
    }

    public void setBallYSpeed(int s)
    {
        ball.setYSpeed(s);
    }

    public void resetBallCount()
    {
        ballCount = 3;
    }

    
    public Brick[] getBricks() 
    {
        return bricks;
    }

    public void setBricks(Brick[] bricks) 
    {
        this.bricks = bricks;
    }

    public Ball getBall() 
    {
        return ball;
    }

    public void setBall(Ball ball) 
    {
        this.ball = ball;
    }

    public Player getPlayer() 
    {
        return player;
    }

    public void setPlayer(Player player) 
    {
        this.player = player;
    }

    public void setBrickCount(int brickCount)
    {
        this.brickCount = brickCount;
    }

}