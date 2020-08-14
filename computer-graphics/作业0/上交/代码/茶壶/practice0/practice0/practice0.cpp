#include <stdlib.h>
//#include <iostream>
//using namespace std;
#include <GL/glut.h>
#include <iostream>
using namespace std;


/*
 * Initialize depth buffer, projection matrix, light source, and lighting
 * model.  Do not specify a material property here.
 */

GLfloat mat[4] = {0.2,0.2,0.9,0.0};		//全局变量记录颜色
//光源位置
GLfloat position[] = { 1.0, 1.0, 5.0, 0.0 };
void init(void)
{
	GLfloat ambient[] = { 0.0, 0.0, 0.0, 1.0 };
	GLfloat diffuse[] = { 1.0, 1.0, 1.0, 1.0 };
	GLfloat specular[] = { 1.0, 1.0, 1.0, 1.0 };
	GLfloat lmodel_ambient[] = { 0.2, 0.2, 0.2, 1.0 };
	GLfloat local_view[] = { 0.0 };

	glLightfv(GL_LIGHT0, GL_AMBIENT, ambient);
	glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuse);
	glLightfv(GL_LIGHT0, GL_POSITION, position);
	glLightModelfv(GL_LIGHT_MODEL_AMBIENT, lmodel_ambient);
	glLightModelfv(GL_LIGHT_MODEL_LOCAL_VIEWER, local_view);

	glFrontFace(GL_CW);
	glEnable(GL_LIGHTING);
	glEnable(GL_LIGHT0);
	glEnable(GL_AUTO_NORMAL);
	glEnable(GL_NORMALIZE);
	glEnable(GL_DEPTH_TEST);
}


void display(void)
{
	

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glPushMatrix();
	glTranslatef(4, 8, 0.0);
	//teapot的颜色
	//mat[0] = 0.2; mat[1] = 0.2; mat[2] = 0.9;  //颜色

	glMaterialfv(GL_FRONT, GL_DIFFUSE, mat);

	glutSolidTeapot(3.0);

	glPopMatrix();

	glFlush();
}

void reshape(int w, int h)
{
	glViewport(0, 0, (GLsizei)w, (GLsizei)h);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	if (w <= h)
		glOrtho(0.0, 16.0, 0.0, 16.0 * (GLfloat)h / (GLfloat)w,
			-10.0, 10.0);
	else
		glOrtho(0.0, 16.0 * (GLfloat)w / (GLfloat)h, 0.0, 16.0,
			-10.0, 10.0);
	glMatrixMode(GL_MODELVIEW);
}


void keyboard(unsigned char key, int x, int y)
{
	switch (key) {
	case 27:
		exit(0);
		break;
	case 'c':
		cout << key;
		mat[0] +=0.1 ; mat[1] += 0.1; mat[2] += 0.1;  //颜色
		break;
	default:
		//		  cout<<key;
		break;
	}
	glutPostRedisplay();
}

void keySpecail(int  key, int x, int y) {
	switch (key) {
	case GLUT_KEY_UP:	//箭头向上
		position[1] -= 0.5;
		glLightfv(GL_LIGHT0, GL_POSITION, position);
		break;
	case GLUT_KEY_DOWN:	//箭头向下
		position[1] += 0.5;
		glLightfv(GL_LIGHT0, GL_POSITION, position);
		break;
	case GLUT_KEY_LEFT:	//箭头向左
		position[0] += 0.5;
		glLightfv(GL_LIGHT0, GL_POSITION, position);
		break;
	case GLUT_KEY_RIGHT:	//箭头向右
		position[0] -= 0.5;
		glLightfv(GL_LIGHT0, GL_POSITION, position);
		break;
	}
	glutPostRedisplay();
}

/*
 * Main Loop
 */
int main(int argc, char** argv)
{
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB | GLUT_DEPTH);
	glutInitWindowSize(400, 400);
	glutInitWindowPosition(150, 150);
	glutCreateWindow("hello world");
	init();
	glutReshapeFunc(reshape);
	glutDisplayFunc(display);
	glutKeyboardFunc(keyboard);
	glutSpecialFunc(keySpecail);
	glutMainLoop();
	return 0;
}
