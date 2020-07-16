/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package lab1_code.turtle;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;


public class TurtleSoup {

    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
    	for(int i=0;i<4;i++) {
    	turtle.forward(sideLength);
    	turtle.turn(180-calculateRegularPolygonAngle(4));
    	}
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
    	double angle;
    	angle=(sides-2)*180.00/sides;
    	System.out.print("angle="+angle+"\n");
    	return angle;
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
//        throw new RuntimeException("implement me!");
    	int sides;
    	double remainder=(double)(360/(180-angle))-(int)(360/(180-angle));
    	if(remainder>=0.5)
    	 {sides=(int) (360/(180-angle))+1;}
    	else
    	 {sides=(int) (360/(180-angle));}	
    	System.out.print("sides="+sides+"\n");
    	return sides;
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
    	for(int i=0;i<sides;i++)
    	{
    		turtle.forward(sideLength);
    		turtle.turn(180-calculateRegularPolygonAngle(sides));
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
    	double angle=Math.toDegrees(Math.atan2((targetX-currentX),(targetY-currentY)));
    	double azimuth=(360+angle-currentBearing)%360; 
    	//System.out.print("azimuth="+azimuth+"\n");
    	return azimuth;
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
    	double currentBearing=0;
    	List<Double> azimuths = new ArrayList<Double>();
    	for(int i=0;i<xCoords.size()-1;i++)
    	{
    		double azimuth=calculateBearingToPoint(currentBearing,xCoords.get(i),yCoords.get(i),xCoords.get(i+1),yCoords.get(i+1));
    		azimuths.add(azimuth);
    		currentBearing+=azimuth;
    	}
    	return azimuths;
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
        //throw new RuntimeException("implement me!");
//    	import java.util.*; 
//    	class point implements Comparable {//平面上的一个点 
//    	double x; 
//    	double y; 
//    	public int compareTo(Object o) {//按y升序排列,y相同按x升序 
//    	point b=(point)o; 
//    	if(this.y>b.y) return 1; 
//    	else if(this.y==b.y ){ 
//    	if(this.x>b.x) return 1; 
//    	else if(this.x==b.x) return 0; 
//    	else return -1; 
//    	} 
//    	else return -1; 
//    	} 
//    	}
    	Set<Point> Hull = new HashSet<Point>();
       	List<Point> lists = new ArrayList<Point>(points);
       	List<Point> convexHull = new ArrayList<Point>();
    	int n=points.size();
    	if (n<3) return Hull;
    	else if (n==3) return points;
    	int l = 0; 
        for (int i = 1; i < n; i++) 
            {
        	if (lists.get(i).x() < lists.get(i+1).x()) 
                l = i;
        	else if(lists.get(i).x() == lists.get(i+1).x()) 
        	{
        		if(lists.get(i).y() > lists.get(i+1).y())
        			l = i;
        	}
        		}
        int p=l;
        int q; 
        do
        { 
        	convexHull.add(lists.get(p)); 
        	q = (p+1)%n;
            for (int i = 0; i < n; i++) 
            { 
            	double val = (lists.get(i).y() - lists.get(p).y()) * (lists.get(q).x() - lists.get(i).x()) - 
            	                  (lists.get(i).x() -lists.get(p).x()) * (lists.get(q).y() - lists.get(i).y()); 
            	 int shu;
            	if (val == 0) shu=0;
                if ((shu=(val>0)?1:2)==2) 
                   q = i; 
            } 
            p = q; 
        } while (p != l);
        Set<Point> BHull = new HashSet<Point>(convexHull);
        return BHull;
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
//    	turtle.color(colors);
//    	turtle.turn(18);
//    	turtle.forward(200);
//    	for(int i=1;i<5;i++) {
//    		turtle.turn(144);
//        	turtle.forward(200);
//        	}
    	turtle.color(PenColor.BLUE);
    	for(int i=0;i<360;i++) {
        	turtle.forward(2);
        	turtle.turn(1);
        	}
    	turtle.color(PenColor.GREEN);
    	turtle.turn(30);
    	turtle.forward(114.3);
    	for(int i=1;i<6;i++) {
    		turtle.turn(60);
        	turtle.forward(114.3);
        	}
    	turtle.color(PenColor.RED);
    	turtle.turn(90);
    	turtle.forward(198);
    	for(int i=1;i<3;i++) {
    		turtle.turn(120);
        	turtle.forward(198);
        	}
    	turtle.color(PenColor.GREEN);
    	turtle.turn(90);
    	turtle.forward(114.3);
    	turtle.color(PenColor.RED);
    	turtle.turn(90);
    	turtle.forward(198);
    	for(int i=1;i<3;i++) {
    		turtle.turn(120);
        	turtle.forward(198);
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
//        drawSquare(turtle, 40);
//        drawRegularPolygon(turtle,5,100);
        drawPersonalArt(turtle);
        // draw the window
        turtle.draw();
    }

}
