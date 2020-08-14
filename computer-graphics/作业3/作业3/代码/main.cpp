#include <cmath>
#include <cstdlib>
#include <ctime>
#include <iostream>
#include <vector>

#ifdef _WIN32
#include "GL/freeglut.h"
#else
#include <GL/glut.h>
#endif
#include <vecmath.h>
#include "camera.h"

///TODO: include more headers if necessary

#include "TimeStepper.hpp"
#include "simpleSystem.h"
#include"pendulumSystem.h"
#include"ClothSystem.h"
using namespace std;

// Globals here.
namespace
{

    ParticleSystem *system;
    TimeStepper * timeStepper;

	bool isDrawSpring = false;
	bool isAddwind = false;
	bool isSwing = false;
vector< particle> createParticles(int s,float r) {
		vector< particle> particles;

		int e1 = 50;
		int e2 = 30;
		int e3 = 30;
		for (int i = 0; i < s * s; i++) {
			particle p2;
			//p2.m_Elastic_coefficient = 10; //弹性系数
			p2.m_mass = 0.2;//质量为1
			//p2.r = 1;	//想连接的弹簧的静态长度为1
			p2.m_Capacitance_coefficient = 10;

			////这里加入结构弹簧
			if (i - s >= 0) {
				//加入上面的粒子
				p2.m_link_particles.push_back(i - s);
				p2.m_Elastic_coefficient.push_back(e1);	//添加弹性系数
				p2.r.push_back(r); //添加静态长度
			}

			if (i + s < s * s) {
				//加入左面的粒子
				p2.m_link_particles.push_back(i + s);
				p2.m_Elastic_coefficient.push_back(e1);	//添加弹性系数
				p2.r.push_back(r); //添加静态长度
			}

			if (i - 1 >= 0 && i % s != 0) {
				//加入上面的粒子
				p2.m_link_particles.push_back(i - 1);
				p2.m_Elastic_coefficient.push_back(e1);	//添加弹性系数
				p2.r.push_back(r); //添加静态长度
			}

			if (i + 1 < s * s && (i + 1) % s != 0) {
				//加入右面的粒子
				p2.m_link_particles.push_back(i + 1);
				p2.m_Elastic_coefficient.push_back(e1);	//添加弹性系数
				p2.r.push_back(r); //添加静态长度
			}


			////加入抗剪弹簧
			if ((i - s - 1) >= 0 && i % s != 0) {
				//左上
				p2.m_link_particles.push_back(i - s - 1);
				p2.m_Elastic_coefficient.push_back(e2);	//添加弹性系数
				p2.r.push_back(sqrt(2.0)*r); //添加静态长度
			}
			if ((i - s + 1) >= 0 && (i + 1) % s != 0) {//右上
				p2.m_link_particles.push_back(i - s + 1);
				p2.m_Elastic_coefficient.push_back(e2);	//添加弹性系数
				p2.r.push_back(sqrt(2.0)*r); //添加静态长度
			}
			if ((i + s - 1) < s * s && i % s != 0) {//左下
				p2.m_link_particles.push_back(i + s - 1);
				p2.m_Elastic_coefficient.push_back(e2);	//添加弹性系数
				p2.r.push_back(sqrt(2.0)*r); //添加静态长度
			}
			if ((i + s + 1) < s * s && (i + 1) % s != 0) {//右下
				p2.m_link_particles.push_back(i + s + 1);
				p2.m_Elastic_coefficient.push_back(e2);	//添加弹性系数
				p2.r.push_back(sqrt(2.0)*r); //添加静态长度
			}

			//加入抗弯弹簧
			if (i - 2 * s >= 0) {//加入上面的粒子
				p2.m_link_particles.push_back(i - 2 * s);
				p2.m_Elastic_coefficient.push_back(e3);	//添加弹性系数
				p2.r.push_back(2*r); //添加静态长度
			}
			if (i + 2 * s < s * s) {//加入下面的粒子
				p2.m_link_particles.push_back(i + 2 * s);
				p2.m_Elastic_coefficient.push_back(e3);	//添加弹性系数
				p2.r.push_back(2*r); //添加静态长度
			}
			if (i - 2 >= 0 && (i - 1) % s != 0 && i % s != 0) {	//加入左面的粒子
				p2.m_link_particles.push_back(i - 2);
				p2.m_Elastic_coefficient.push_back(e3);	//添加弹性系数
				p2.r.push_back(2*r); //添加静态长度
			}
			if (i + 2 < s * s && (i + 2) % s != 0 && (i + 1) % s != 0) {//加入右面的粒子
				p2.m_link_particles.push_back(i + 2);
				p2.m_Elastic_coefficient.push_back(e3);	//添加弹性系数
				p2.r.push_back(2*r); //添加静态长度
			}

			////设置静止的粒子
			if (i % s == 0) {
				p2.isStill = true;
				p2.m_Elastic_coefficient[0] = 100;
				p2.m_Elastic_coefficient[1] = 100;
				p2.m_Elastic_coefficient[2] = 100;
				p2.m_Elastic_coefficient[3] = 100;
				p2.m_Elastic_coefficient[4] = 100;
			}
				

			particles.push_back(p2);
		}
		particles[0].isStill = true;
		particles[0].m_Elastic_coefficient[0] = 100;
		particles[0].m_Elastic_coefficient[1] = 100;
		particles[0].m_Elastic_coefficient[2] = 100;
		particles[0].m_Elastic_coefficient[3] = 100;
		particles[0].m_Elastic_coefficient[4] = 100;
		particles[s * (s - 1)].isStill = true;
		particles[s * (s - 1)].m_Elastic_coefficient[0] = 100;
		particles[s * (s - 1)].m_Elastic_coefficient[1] = 100;
		particles[s * (s - 1)].m_Elastic_coefficient[2] = 100;
		particles[s * (s - 1)].m_Elastic_coefficient[3] = 100;
		particles[s * (s - 1)].m_Elastic_coefficient[4] = 100;
		return particles;
	}


  // initialize your particle systems
  ///TODO: read argv here. set timestepper , step size etc
  void initSystem(int argc, char * argv[])
  {
    // seed the random number generator with the current time

    srand( time( NULL ) );
	int s = 15;
	float x = 0.2;
	vector< particle> particles = createParticles(s,x);
    system = new ClothSystem(s, particles);	//在这里改变系统

	vector<Vector3f> state;
	for (int i = 0; i < s; i++) {
		for (int j = 0; j < s; j++) {
			state.push_back(Vector3f(x*i,3,j*x));	//设置初始位置		
			state.push_back(Vector3f(0., 0.0, 0.));	//设置初始速度变量
		}
	}
	

	system->setState(state);
	

    timeStepper = new RK4();		//在这里改变事件积分器	
  }

  // Take a step forward for the particle shower
  ///TODO: Optional. modify this function to display various particle systems
  ///and switch between different timeSteppers
  void stepSystem()
  {
      ///TODO The stepsize should change according to commandline arguments
    const float h = 0.04f;
    if(timeStepper!=0){
      timeStepper->takeStep(system,h);
    }
  }

  //画出弹簧的函数,参数i为第几个弹簧
  void drawSpring(int i) {
	  glColor3f(0.0, 1.0, 1.0);
	  glLineWidth(5);
	  glEnable(GL_LINE_SMOOTH);
	  glBegin(GL_LINES);
	  glVertex3f(system->getState()[(i-1)*2].x(), system->getState()[(i - 1) * 2].y(), system->getState()[(i - 1) * 2].z());
	  glVertex3f(system->getState()[i*2].x(), system->getState()[i * 2].y(), system->getState()[i * 2].z());
	  glEnd();
  }



  // Draw the current particle positions
  void drawSystem()
  {
    
    // Base material colors (they don't change)
    GLfloat particleColor[] = {0.4f, 0.7f, 1.0f, 1.0f};
    GLfloat floorColor[] = {1.0f, 0.0f, 0.0f, 1.0f};
    
    glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, particleColor);
    
    //glutSolidSphere(0.1f,10.0f,10.0f); //最开始的球
    
    system->draw();
	if (isDrawSpring) {
		drawSpring(1);
		drawSpring(2);
		drawSpring(3);
	}
	

    glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, floorColor);
    glPushMatrix();
    glTranslatef(0.0f,-5.0f,0.0f);
    glScaled(50.0f,0.01f,50.0f);
    glutSolidCube(1);
    glPopMatrix();
    
  }
        

    //-------------------------------------------------------------------
    
        
    // This is the camera
    Camera camera;

    // These are state variables for the UI
    bool g_mousePressed = false;

    // Declarations of functions whose implementations occur later.
    void arcballRotation(int endX, int endY);
    void keyboardFunc( unsigned char key, int x, int y);
    void specialFunc( int key, int x, int y );
    void mouseFunc(int button, int state, int x, int y);
    void motionFunc(int x, int y);
    void reshapeFunc(int w, int h);
    void drawScene(void);
    void initRendering();

    // This function is called whenever a "Normal" key press is
    // received.

    void keyboardFunc( unsigned char key, int x, int y )
    {	
		isDrawSpring = false;
        switch ( key )
        {
        case 27: // Escape key
            exit(0);
            break;
        case 'r':
        {	
            Matrix4f eye = Matrix4f::identity();
            camera.SetRotation( eye );
            camera.SetCenter( Vector3f::ZERO );


            break;
        }
		case 'l':
		{
			Matrix4f eye = Matrix4f::identity();
			camera.SetRotation(eye);
			camera.SetCenter(Vector3f::ZERO);

			system = new SimpleSystem();
			cout << "现在是SimpleSystem" << endl;
			break;
		}
		case 'p':
		{
			Matrix4f eye = Matrix4f::identity();
			camera.SetRotation(eye);
			camera.SetCenter(Vector3f::ZERO);

			isDrawSpring = true;
			particle p1;	//先做单摆写1个简单的粒子
			particle p2;	//先做单摆写1个简单的粒子
			particle p3;	//先做单摆写1个简单的粒子
			particle p4;	//先做单摆写1个简单的粒子
			

			p1.isStill = true;

			p2.m_Elastic_coefficient.push_back(10); //弹性系数
			p2.m_Elastic_coefficient.push_back(10); //弹性系数
			p2.m_mass = 1;//质量为1
			p2.r.push_back(1);	//想连接的弹簧的静态长度为1
			p2.r.push_back(1);	//想连接的弹簧的静态长度为1
			p2.m_Capacitance_coefficient = 1;

			p3.m_Elastic_coefficient.push_back(10); //弹性系数
			p3.m_Elastic_coefficient.push_back(10); //弹性系数
			p3.m_mass = 1;//质量为1
			p3.r.push_back(1);	//想连接的弹簧的静态长度为1
			p3.r.push_back(1);	//想连接的弹簧的静态长度为1
			p3.m_Capacitance_coefficient = 1;

			p4.m_Elastic_coefficient.push_back(10); //弹性系数
			p4.m_mass = 1;//质量为1
			p4.r.push_back(1);	//想连接的弹簧的静态长度为1
			p4.m_Capacitance_coefficient = 1;	//摩檫力系数

			vector< particle> particles;
			p1.m_link_particles.push_back(1);

			p2.m_link_particles.push_back(0);
			p2.m_link_particles.push_back(2);

			p3.m_link_particles.push_back(1);
			p3.m_link_particles.push_back(3);

			p4.m_link_particles.push_back(2);

			particles.push_back(p1);
			particles.push_back(p2);
			particles.push_back(p3);
			particles.push_back(p4);
			vector<Vector3f> state;
			////设置p1
			state.push_back(Vector3f(0.0,5,0.));	//设置初始位置		
			state.push_back(Vector3f(0.,0.0,0.));	//设置初始速度变量
			//设置p2
			state.push_back(Vector3f(0.,4.5, 0.));	//设置初始位置		
			state.push_back(Vector3f(4.0, 0.0, 0.));	//设置初始速度变量
			////设置p3
			state.push_back(Vector3f(0.0,4.0, 0.));	//设置初始位置		
			state.push_back(Vector3f(-4.0, 0.0, 0.));	//设置初始速度变量
			////设置p4
			state.push_back(Vector3f(0., 3.5, 0.));	//设置初始位置		
			state.push_back(Vector3f(0.0, 0.0, 0.));	//设置初始速度变量

			system = new PendulumSystem(4, particles);
			system->setState(state);
			
			cout << "现在是PendulumSystem" << endl;
			break;
		}
		case 'c':
		{
			Matrix4f eye = Matrix4f::identity();
			camera.SetRotation(eye);
			camera.SetCenter(Vector3f::ZERO);
			int s = 15;
			float x = 0.2;
			vector< particle> particles = createParticles(s, x);
			system = new ClothSystem(s, particles);	//在这里改变系统

			vector<Vector3f> state;
			for (int i = 0; i < s; i++) {
				for (int j = 0; j < s; j++) {
					state.push_back(Vector3f(x * i, 3, j * x));	//设置初始位置		
					state.push_back(Vector3f(0., 0.0, 0.));	//设置初始速度变量
				}
			}
			system->setState(state);
			cout << "现在是ClothSystem" << endl;
			break;
		}
		case 'e':
		{
			timeStepper = new ForwardEuler();
			cout << "现在时间积分器是ForwardEuler" << endl;
			break;
		}
		case 't':
		{
			timeStepper = new Trapzoidal();
			cout << "现在时间积分器是Trapzoidal" << endl;
			break;
		}
		case 'k':
		{

			timeStepper = new RK4();
			cout << "现在时间积分器是RK4" << endl;
			break;
		}
		case 'd': 
		{	
			isAddwind = !isAddwind;
			if(isAddwind)
				system->setWind(Vector3f(0, 0, 3));
			else
			{
				system->setWind(Vector3f());
			}
			
			printf("按下了d");
			break;
			
		}
		case 'w':
		{
			system->setDraw();
			break;
		}
		case 's':
		{	
			isSwing =!isSwing;
			break;
		}
        default:
            cout << "Unhandled key press " << key << "." << endl;        
        }

        glutPostRedisplay();
    }

    // This function is called whenever a "Special" key press is
    // received.  Right now, it's handling the arrow keys.
    void specialFunc( int key, int x, int y )
    {
        switch ( key )
        {

        }
        //glutPostRedisplay();
    }

    //  Called when mouse button is pressed.
    void mouseFunc(int button, int state, int x, int y)
    {
        if (state == GLUT_DOWN)
        {
            g_mousePressed = true;
            
            switch (button)
            {
            case GLUT_LEFT_BUTTON:
                camera.MouseClick(Camera::LEFT, x, y);
                break;
            case GLUT_MIDDLE_BUTTON:
                camera.MouseClick(Camera::MIDDLE, x, y);
                break;
            case GLUT_RIGHT_BUTTON:
                camera.MouseClick(Camera::RIGHT, x,y);
            default:
                break;
            }                       
        }
        else
        {
            camera.MouseRelease(x,y);
            g_mousePressed = false;
        }
        glutPostRedisplay();
    }

    // Called when mouse is moved while button pressed.
    void motionFunc(int x, int y)
    {
        camera.MouseDrag(x,y);        
    
        glutPostRedisplay();
    }

    // Called when the window is resized
    // w, h - width and height of the window in pixels.
    void reshapeFunc(int w, int h)
    {
        camera.SetDimensions(w,h);

        camera.SetViewport(0,0,w,h);
        camera.ApplyViewport();

        // Set up a perspective view, with square aspect ratio
        glMatrixMode(GL_PROJECTION);

        camera.SetPerspective(50);
        glLoadMatrixf( camera.projectionMatrix() );
    }

    // Initialize OpenGL's rendering modes
    void initRendering()
    {
        glEnable(GL_DEPTH_TEST);   // Depth testing must be turned on
        glEnable(GL_LIGHTING);     // Enable lighting calculations
        glEnable(GL_LIGHT0);       // Turn on light #0.

        glEnable(GL_NORMALIZE);

        // Setup polygon drawing
        glShadeModel(GL_SMOOTH);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        
	glEnable(GL_CULL_FACE);
	glCullFace(GL_BACK);

        // Clear to black
        glClearColor(0,0,0,1);
    }

    // This function is responsible for displaying the object.
    void drawScene(void)
    {
        // Clear the rendering window
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glMatrixMode( GL_MODELVIEW );  
        glLoadIdentity();              

        // Light color (RGBA)
        GLfloat Lt0diff[] = {1.0,1.0,1.0,1.0};
        GLfloat Lt0pos[] = {3.0,3.0,5.0,1.0};
        glLightfv(GL_LIGHT0, GL_DIFFUSE, Lt0diff);
        glLightfv(GL_LIGHT0, GL_POSITION, Lt0pos);

        glLoadMatrixf( camera.viewMatrix() );

        // THIS IS WHERE THE DRAW CODE GOES.

        drawSystem();

        // This draws the coordinate axes when you're rotating, to
        // keep yourself oriented.
        if( g_mousePressed )
        {
            glPushMatrix();
            Vector3f eye = camera.GetCenter();
            glTranslatef( eye[0], eye[1], eye[2] );

            // Save current state of OpenGL
            glPushAttrib(GL_ALL_ATTRIB_BITS);

            // This is to draw the axes when the mouse button is down
            glDisable(GL_LIGHTING);
            glLineWidth(3);
            glPushMatrix();
            glScaled(5.0,5.0,5.0);
            glBegin(GL_LINES);
            glColor4f(1,0.5,0.5,1); glVertex3f(0,0,0); glVertex3f(1,0,0);
            glColor4f(0.5,1,0.5,1); glVertex3f(0,0,0); glVertex3f(0,1,0);
            glColor4f(0.5,0.5,1,1); glVertex3f(0,0,0); glVertex3f(0,0,1);

            glColor4f(0.5,0.5,0.5,1);
            glVertex3f(0,0,0); glVertex3f(-1,0,0);
            glVertex3f(0,0,0); glVertex3f(0,-1,0);
            glVertex3f(0,0,0); glVertex3f(0,0,-1);

            glEnd();
            glPopMatrix();

            glPopAttrib();
            glPopMatrix();
        }
                 
        // Dump the image to the screen.
        glutSwapBuffers();
    }

    void timerFunc(int t)
    {
        stepSystem();

		if (isSwing) {
			vector<Vector3f>state = system->getState();
			Vector3f v(0, 0, -0.5);
			if (state[0].z() > 3) {
				v = (0, 0, 0.5);
			}
			else if (state[0].z() < -3)
			{
				v = (0, 0, -0.5);
			}

			for (int i = 0; i < state.size(); i++) {
				if (i % 2 != 0) {
					state[i] = Vector3f(v);
				}
			}
			system->setState(state);
		}
		

        glutPostRedisplay();

        glutTimerFunc(t, &timerFunc, t);
    }

    

    
    
}

// Main routine.
// Set up OpenGL, define the callbacks and start the main loop
int main( int argc, char* argv[] )
{
    glutInit( &argc, argv );

    // We're going to animate it, so double buffer 
    glutInitDisplayMode( GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH );

    // Initial parameters for window position and size
    glutInitWindowPosition( 60, 60 );
    glutInitWindowSize( 600, 600 );
    
    camera.SetDimensions( 600, 600 );

    camera.SetDistance( 10 );
    camera.SetCenter( Vector3f::ZERO );
    
    glutCreateWindow("Assignment 4");

    // Initialize OpenGL parameters.
    initRendering();

    // Setup particle system
    initSystem(argc,argv);

    // Set up callback functions for key presses
    glutKeyboardFunc(keyboardFunc); // Handles "normal" ascii symbols
    glutSpecialFunc(specialFunc);   // Handles "special" keyboard keys

    // Set up callback functions for mouse
    glutMouseFunc(mouseFunc);
    glutMotionFunc(motionFunc);

    // Set up the callback function for resizing windows
    glutReshapeFunc( reshapeFunc );

    // Call this whenever window needs redrawing
    glutDisplayFunc( drawScene );

    // Trigger timerFunc every 20 msec
    glutTimerFunc(20, timerFunc, 20);

        
    // Start the main loop.  glutMainLoop never returns.
    glutMainLoop();

    return 0;	// This line is never reached.
}
