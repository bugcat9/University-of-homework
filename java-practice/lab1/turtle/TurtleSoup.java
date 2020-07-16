/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package turtle;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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
        //throw new RuntimeException("implement me!");
   
    	for(int i=0;i<4;i++)
    	{
    		turtle.forward(sideLength);
            turtle.turn(90.00);
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
        //throw new RuntimeException("implement me!");
    	if(sides<2)
    	{
    		//抛出错误
    		
    	}
    		
    	double res= (sides-2)*180.0/sides;
    	 return res;
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
    	double d=360/(180-angle);
    	int sildes=(int) Math.round(d);
    	return  sildes;
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
       // throw new RuntimeException("implement me!");
    	double angles=180.0- calculateRegularPolygonAngle(sides);
    	for(int i=0;i<sides;i++)
    	{
    		turtle.forward(sideLength);
        	turtle.turn(angles);
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
        //throw new RuntimeException("implement me!");
    	double x1=Math.sin(currentBearing/180.0*Math.PI);
    	double  y1=Math.cos(currentBearing/180.0*Math.PI);
    	double x2=targetX-currentX;
    	double y2=targetY-currentY;
    	double l=Math.sqrt(Math.pow(x2, 2)+Math.pow(y2, 2));
    	double a=Math.acos((x1*x2+y1*y2)/l)/Math.PI*180.0;
    	
    	if(x1*y2-x2*y1>0)
    		return 360.0-a;
    	else
    		return a;
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
        //throw new RuntimeException("implement me!");
    	List<Double>anglesList=new ArrayList<>();
    	int x1=xCoords.get(0);
    	int x2=xCoords.get(1);
    	int y1=yCoords.get(0);
    	int y2=yCoords.get(1);
    	double curangles= calculateBearingToPoint(0.0,x1,y1,x2,y2);
    	anglesList.add(curangles);
    	for(int i=2;i<xCoords.size();i++)
    	{
    		 x1=xCoords.get(i-1);
        	 x2=xCoords.get(i);
        	 y1=yCoords.get(i-1);
        	 y2=yCoords.get(i);
        	 curangles= calculateBearingToPoint(anglesList.get(i-2),x1,y1,x2,y2);
        	 anglesList.add(curangles);
    	}
    	return anglesList;
    }
    
 // 向量ca与ba的叉积
    static double cross(Point c, Point a, Point b) {
        return (c.x() - a.x()) * (a.y() - b.y()) - (c.y() - a.y()) * (a.x() - b.x());
    }
    
 // 求距离,主要是为了求极点
    public static double distance(Point p1, Point p2) {
        return (Math.hypot((p1.x() - p2.x()), (p1.y() - p2.y())));
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
    	if(points.isEmpty())
    		return points;
    	Set<Point> res=new HashSet<Point>();
    	int n= points.size();
    	boolean []visit=new boolean[n];
    	Point[] mypoints=(Point[]) points.toArray(new Point[n]);
    	//进行排序
    	for(int i=0;i<n;i++)
    	{
    		Point minpoint=mypoints[i];
    		int minindex=i;
    		for(int j=i+1;j<n;j++)
    		{
    			if( minpoint.x()>mypoints[j].x()  ||   ( ( minpoint.x()==mypoints[j].x() ) && ( minpoint.y()>mypoints[j].y() )))
    			{
    				minpoint=mypoints[j];
    				minindex=j;
    			}
    		}
    		mypoints[minindex]=mypoints[i];
    		mypoints[i]=minpoint;
    	}
    	
    	
    	visit[0]=true;
    	 int in = 0;//在凸包上的点
    	 res.add(mypoints[0]);
    	 while (true) {
             int not = -1;
             for (int i = 0; i < n; i++) {
                 if (!visit[i]) {// 找一个不在凸包上的点
                     not = i;
                     break;
                 }
             }
             if (not == -1)
                 break;// 找不到,结束
             for (int i = 0; i < n; i++) {
                 /*
                  *  遍历所有点, 每个点都和现有最外侧的点比较,得到新的最外侧的点
                  *  第二个条件是找到极点，不包括共线点
                  */
            	 if ((cross(mypoints[in], mypoints[i], mypoints[not]) > 0)
                         || (cross(mypoints[in], mypoints[i], mypoints[not]) == 0)
                         && (distance(mypoints[in], mypoints[i]) > distance(
                        		 mypoints[in], mypoints[not])))
                     not = i;
             }
             if (visit[not])
                 break;// 找不到最外侧的点了
             res.add(mypoints[not]);// 最外侧的点进凸包
             visit[not] = true;// 标记这点已放进凸包了
             in = not;//in始终表示一个必定在凸包里的点
         }
    	return res;
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
        //throw new RuntimeException("implement me!");
    	turtle.color(PenColor.ORANGE);
    	for(int i=0;i<180;i++)
    	{
    		drawRegularPolygon(turtle,3,i*1);
        	turtle.turn(2.0*i);
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
        //drawRegularPolygon(turtle,12,30);
        //drawSquare(turtle, 40);
        drawPersonalArt(turtle);
        // draw the window
        turtle.draw();
    }

}
