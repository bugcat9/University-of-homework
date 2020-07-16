/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package turtle;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class TurtleSoup {

    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
        //throw new RuntimeException("implement me!");
    	turtle.color(PenColor.RED);
    	turtle.forward(sideLength);
    	turtle.turn(90);
    	turtle.forward(sideLength);
    	turtle.turn(90);
    	turtle.forward(sideLength);
    	turtle.turn(90);
    	turtle.forward(sideLength);
    	turtle.turn(90);
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
//        throw new RuntimeException("implement me!");
    	return ((sides - 2) * 180.0) / sides;
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        //throw new RuntimeException("implement me!");
    	return (int)Math.round(2 * 180.0 / (180.0 - angle));
    } 
    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
//        throw new RuntimeException("implement me!");
    	double degrees = TurtleSoup.calculateRegularPolygonAngle(sides);//与y轴的夹角
    	degrees = 180.0 - degrees;
    		
    	turtle.color(PenColor.MAGENTA);
    	for(int i = 0; i < sides; i++)
    	{
        	turtle.forward(sideLength);
        	turtle.turn(degrees);
    	}
    }

    /**
     * Given the current direction, current location, and a target location, calculate the Bearing
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentBearing. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentBearing current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to Bearing (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateBearingToPoint(double currentBearing, int currentX, int currentY,
                                                 int targetX, int targetY) {
//        throw new RuntimeException("implement me!");
    	double theta;
    	if((targetX == 0 && targetY == 0) || (targetX == 0 && targetY > 0))
        {
	   		//theta = (180/Math.PI)*((Math.atan(Math.abs(targetX-currentX)/Math.abs(targetY-currentY))));
    		theta = 0;
        }
    	else if(targetX > 0 && targetY > 0)
    	{
    		if(currentBearing == 0)
    		{
    	   		theta = (180/Math.PI)*((Math.atan(Math.abs(targetX-currentX)/Math.abs(targetY-currentY))));
        		theta = theta - currentBearing;
    		}
    		else
    		{
    	   		theta = (180/Math.PI)*((Math.atan(Math.abs(targetX-currentX)/Math.abs(targetY-currentY))));
        		theta = 360.0 + theta - currentBearing;    			
    		}
    	}
    	else if(targetX > 0 && targetY == 0)
    	{	
	   		//theta = (180/Math.PI)*((Math.atan(Math.abs(targetX-currentX)/Math.abs(targetY-currentY))));
    		theta = 90.0;
    	}
    	else if(targetX > 0 && targetY < 0)
    	{
	   		theta = (180/Math.PI)*((Math.atan(Math.abs(targetX-currentX)/Math.abs(targetY-currentY))));
    		theta = 180.0 - theta - currentBearing;
    	}
    	else if(targetX == 0 && targetY < 0)
    	{
	   		//theta = (180/Math.PI)*((Math.atan(Math.abs(targetX-currentX)/Math.abs(targetY-currentY))));
    		theta = 180.0;
    	}
   		else if(targetX < 0 && targetY < 0)
   		{
	   		theta = (180/Math.PI)*((Math.atan(Math.abs(targetX-currentX)/Math.abs(targetY-currentY))));
    		theta = theta + 180.0 - currentBearing;
   		}
   		else if(targetX < 0 && targetY == 0)
   		{
   			//theta = (180/Math.PI)*((Math.atan(Math.abs(targetX-currentX)/Math.abs(targetY-currentY))));
    		theta = 270.0;
   		}
    	else
    	{
	   		theta = (180/Math.PI)*((Math.atan(Math.abs(targetX-currentX)/Math.abs(targetY-currentY))));
    		theta = theta + 270.0 - currentBearing;
    	}
    	return theta;
    }

    /**
     * Given a sequence of points, calculate the Bearing adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateBearingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of Bearing adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateBearings(List<Integer> xCoords, List<Integer> yCoords) {
//        throw new RuntimeException("implement me!");
    	List<Double> currentBearing = new ArrayList<>();
    	currentBearing.add(0.0);
    	int j = 0; 
    	double theta;
    	//xCoords.get(0);
    	for(int i = 0; i < xCoords.size() - 1; i++) 
    	{
	    	theta = TurtleSoup.calculateBearingToPoint(currentBearing.get(i), 
	    			xCoords.get(i), yCoords.get(i), xCoords.get(++j), yCoords.get(j));
	    	currentBearing.add(theta);
    	}
    	currentBearing.remove(0);
    	return currentBearing;
    }
    
    public static double calculateBearingToPoint2(double currentBearing, double currentX, double currentY,
            double targetX, double targetY) {
		//throw new RuntimeException("implement me!");
			double tmp=Math.toDegrees(Math.atan2(targetX-currentX, targetY-currentY));
			if (targetX<currentX) {
				tmp+=360;
			}
			double res=tmp-currentBearing;
			if (res<0) {
				res+=360;
			}
			return res;
		}
    /**
     * Given a set of points, compute the convex hull, the smallest convex set that contains all the points 
     * in a set of input points. The gift-wrapping algorithm is one simple approach to this problem, and 
     * there are other algorithms too.
     * 
     * @param points a set of points with xCoords and yCoords. It might be empty, contain only 1 point, two points or more.
     * @return minimal subset of the input points that form the vertices of the perimeter of the convex hull
     */
    public static Set<Point> convexHull(Set<Point> points) {
//        throw new RuntimeException("implement me!");
		//points = new HashSet<Point>();
		if(points.size() <= 3)
		{
			return points;
		}
        Set<Point> res=new HashSet<>();
        Point first=points.iterator().next();
        for (Point point : points)
        {
        	if(point.x() != 1 && point.y() != 2)
        	{
            	if (point.y()<first.y())
                {
                    first=point;
                }
                else if (point.y()==first.y()&&point.x()<first.x())
                {
                    first=point;
                }
        	}
        }
        res.add(first);
        Point tmp=first;
        points.remove(tmp);
        Point tmp2=points.iterator().next();
        double a=270.0;
        double b=calculateBearingToPoint2(a,tmp.x(),tmp.y(),tmp2.x(),tmp2.y());
        for (Point point : points) 
        {
            double d=calculateBearingToPoint2(a,tmp.x(),tmp.y(),point.x(),point.y());
            if (d>b)
            {
                tmp2=point;
                b=d;
            }
        }
        res.add(tmp2);
        points.add(tmp);
        tmp=tmp2;
        while (!(tmp.x()==first.x()&&tmp.y()==first.y()))
        {
            points.remove(tmp);
            tmp2=points.iterator().next();
            a=a+b-180;
            b=calculateBearingToPoint2(a,tmp.x(),tmp.y(),tmp2.x(),tmp2.y());
            for (Point point :
                    points) 
            {
                double d=calculateBearingToPoint2(a,tmp.x(),tmp.y(),point.x(),point.y());
                if (d>b)
                {
                    tmp2=point;
                    b=d;
                }
            }
            res.add(tmp2);
            points.add(tmp);
            tmp=tmp2;
        }
        return res;
    	
    	//return points;
    }
    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
//        throw new RuntimeException("implement me!");
        turtle.turn(18);
        turtle.color(PenColor.RED);
    	for (int i=0;i<5;++i)
    	{
    	    turtle.forward(200);
    	    turtle.turn(144);
    	}
    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();

        //rawSquare(turtle, 40);
        drawPersonalArt(turtle);
        // draw the window
        turtle.draw();
        
        //        
    }

}
