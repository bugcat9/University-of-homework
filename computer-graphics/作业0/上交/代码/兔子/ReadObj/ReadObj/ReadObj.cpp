// ReadObj.cpp : 此文件包含 "main" 函数。程序执行将在此处开始并结束。
//

#include <iostream>
#include<string>
#include<gl/glut.h>
#include<fstream>
#include<sstream>   
#include<vector>
#include<math.h>
using namespace std;
//顶点的数据结构
struct vertex
{
	GLfloat p1;
	GLfloat p2;
	GLfloat p3;

	vertex() {
		p1 = 0;
		p2 = 0;
		p3 = 0;
	}

	vertex(GLfloat p1, GLfloat p2, GLfloat p3) {
		this->p1 = p1;
		this->p2 = p2;
		this->p3 = p3;
	}

}; 

//面的数据结构
struct face
{
	vertex v1;
	vertex v2;
	vertex v3;
	face(vertex v1, vertex v2, vertex v3){
		this->v1 = v1;
		this->v2 = v2;
		this->v3 = v3;
	}
};

vector<face>faces;	//储存面
vector<vertex>vertexs;	//储存点
int fcount = 0;	//面的数量
int vcount = 0;	//点的数量
GLfloat p[3] = { 0.0f,0.0f,2.0f };	//眼睛的位置
int px = 0;
int py = 0;
float angleMove[3] = { 0.0,0.0,0.0 };//角度

float color[] = {0.5, 0.0, 0.0, 0.0};//颜色
//光源位置
GLfloat position[] = { 0.0, 1.0,-1.0, 0.0 };
float angle = 0.0;
// 创建一个显示列表对象  
GLuint list;

//将字符串转化为数字
template<typename T>
T stringToNum(const string& str)
{
	istringstream iss(str);
	T num;
	iss >> num;
	return num;
}


///读取obj文件
void Readobj(string filename) {
	string s, str, s1, s2, s3, s4;
	ifstream inf;
	inf.open(filename);
	int vn = 0;
	int vnum = 0;
	int fnum = 0;
	int vindex = 0;

	while (getline(inf, s))
	{
		//cout << s<<endl;
		istringstream in(s);
		in >> s1 >> s2 >> s3 >> s4;
		//cout << s1 << "	" << s2 << "	" << s3 << "	" << s4 << endl;

		if (s1 == "v") {
			GLfloat p1 = stringToNum<float>(s2);
			GLfloat p2 = stringToNum<float>(s3);
			GLfloat p3 = stringToNum<float>(s4);
			vertex v(p1, p2, p3);
			vertexs.push_back(v);	//存点
			vcount++;
		}
		else if (s1 == "f")
		{
			int v1= stringToNum<int>(s2);
			int v2 = stringToNum<int>(s3);
			int v3 = stringToNum<int>(s4);

			face f(vertexs[v1-1],vertexs[v2-1],vertexs[v3-1]);
			faces.push_back(f);//存面
			fcount++;
		}
	}
}

//求法向量
GLvoid CalculateVectorNormal(face f, float* fNormalX,
	float* fNormalY, float* fNormalZ)
{
	float Qx, Qy, Qz, Px, Py, Pz;

	Qx = f.v2.p1 - f.v1.p1;//fVert2[0] - fVert1[0];
	Qy = f.v2.p2 - f.v1.p2;//fVert2[1] - fVert1[1];
	Qz = f.v2.p3 - f.v1.p3;//fVert2[2] - fVert1[2];
	Px = f.v3.p1 - f.v1.p1;//fVert3[0] - fVert1[0];
	Py = f.v3.p2 - f.v1.p2; //fVert3[1] - fVert1[1];
	Pz = f.v3.p3 - f.v1.p3;//fVert3[2] - fVert1[2];

	*fNormalX = Py * Qz - Pz * Qy;
	*fNormalY = Pz * Qx - Px * Qz;
	*fNormalZ = Px * Qy - Py * Qx;

	float D = sqrt(pow(*fNormalX, 2) + pow(*fNormalY, 2) + pow(*fNormalZ, 2));
	*fNormalX = *fNormalX / D;
	*fNormalY = *fNormalY / D;
	*fNormalZ = *fNormalZ / D;
}

//通过显示列表画图形
void  draw_obj() {
	list= glGenLists(1);
	glNewList(list, GL_COMPILE);
	for (int i = 0; i < fcount; i++) {
		face f = faces[i];
		glBegin(GL_TRIANGLES);
		glColor3f(color[0], color[1],color[2]);
		float  fNormalX, fNormalY, fNormalZ;
		CalculateVectorNormal(f, &fNormalX, &fNormalY, &fNormalZ);
		glNormal3f(fNormalX, fNormalY, fNormalZ);	//加法向量
		glVertex3f(f.v1.p1, f.v1.p2, f.v1.p3);	//画每个面
		glVertex3f(f.v2.p1, f.v2.p2, f.v2.p3);
		glVertex3f(f.v3.p1, f.v3.p2, f.v3.p3);
		glEnd();
	}
	glEndList();
}

void display(void)
{	
	
	glClear(GL_COLOR_BUFFER_BIT);
	//glBegin(GL_TRIANGLES);
	//for (int i = 0; i < fcount; i++) {
	//	face f = faces[i];
	//	glBegin(GL_TRIANGLES);
	//	glColor3f(color[0], color[1],color[2]);
	//	float  fNormalX, fNormalY, fNormalZ;
	//	//CalculateVectorNormal(f, &fNormalX, &fNormalY, &fNormalZ);
	//	//glNormal3f(fNormalX, fNormalY, fNormalZ);	//加法向量
	//	glVertex3f(f.v1.p1, f.v1.p2, f.v1.p3);	//画每个面
	//	glVertex3f(f.v2.p1, f.v2.p2, f.v2.p3);
	//	glVertex3f(f.v3.p1, f.v3.p2, f.v3.p3);
	//	
	//}
	//glEnd();
	//glFlush();
	glCallList(list);
	glFlush();	
	glLoadIdentity();
	gluLookAt(p[0], p[1], p[2], 0.0, 0.0, 0.0, 0.0f, 1.0f, 0.0f);
}

void changeSize(int w, int h) {


	// 防止除数即高度为0
	// (你可以设置窗口宽度为0).
	if (h == 0)
		h = 1;

	float ratio = 1.0 * w / h;

	// 单位化投影矩阵。
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();

	// 设置视口大小为增个窗口大小
	glViewport(0, 0, w, h);

	// 设置正确的投影矩阵
	gluPerspective(45, ratio, 1, 1000);
	//下面是设置模型视图矩阵
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	gluLookAt(p[0], p[1], p[2], 0.0, 0.0, 0.0, 0.0f, 1.0f, 0.0f);

}

void init(void)
{


	glLightfv(GL_LIGHT0, GL_POSITION, position);
	glEnable(GL_LIGHTING);
	glEnable(GL_LIGHT0);
	glEnable(GL_COLOR_MATERIAL);
	glLightModeli(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
	
	glClearColor(0.0, 0, 0, 0.0);
	glMatrixMode(GL_PROJECTION);

	glLoadIdentity();

	//glOrtho(0.0, 1.0, 0.0, 1.0, -1.0, 1.0);
}

//定时函数
void time_Rotate_Func(int id) {
	angle +=25;
	p[2] = cos((angle / 180) * 3.14);
	p[0] = sin((angle / 180) * 3.14);
	cout << angle;
	glutPostRedisplay();
	glutTimerFunc(30, time_Rotate_Func, 1);//需要在函数中再调用一次，才能保证循环
	
}

void time_Color_Func(int id) {
	if (color[0] < 1)
		color[0] += 0.3;
	if (color[1] < 1)
		color[1] += 0.3;
	if (color[2] < 1)
		color[2] += 0.3;
	glutTimerFunc(30, time_Color_Func, 2);//需要在函数中再调用一次，才能保证循环
}


//鼠标点击
void mouse_Click(int button,int state, int x,int y) {
	if (button == GLUT_LEFT_BUTTON) {
		px = x;
		py = y;
	}
}
//鼠标移动
void mouse_Move(int x, int y) {
	
	angleMove[0] += (px - x)*0.3;
	p[2] = cos((angleMove[0] / 180) * 3.14)*2;
	p[0] = sin((angleMove[0] / 180) * 3.14)*2;	//用于旋转
	px = x;
	py = y;
	
	glutPostRedisplay();
}

void keyboard(unsigned char key, int x, int y)
{
	angle = 0;
	switch (key) {
	case 27:
		exit(0);
		break;
	case 'r':
		glutTimerFunc(30, time_Rotate_Func, 1);
		break;
	case 'c':
		glutTimerFunc(30, time_Color_Func, 2);
		break;
	case 'w':
		p[2] -= 0.2;//改变距离实现缩放
		break;
	case's':
		p[2] += 0.2;
		break;
	default:
		//		  cout<<key;
		break;
	}
	glutPostRedisplay();
}

//用来调光源
void keySpecail(int  key, int x, int y) {
	switch (key) {
	case GLUT_KEY_UP:	//箭头向上
		position[2] += 0.2;
		break;
	case GLUT_KEY_DOWN:	//箭头向下
		position[2] -= 0.2;
		break;
	case GLUT_KEY_LEFT:	//箭头向左
		position[0] += 0.2;
		glLightfv(GL_LIGHT0, GL_POSITION, position);
		break;
	case GLUT_KEY_RIGHT:	//箭头向右
		position[0] -= 0.2;
		glLightfv(GL_LIGHT0, GL_POSITION, position);
		cout << position[2]<<"	";
		break;
	}
	glutPostRedisplay();
}

int main(int argc, char** argv)
{
	string filename = "bunny_1k.obj";
	Readobj(filename);
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DEPTH | GLUT_SINGLE | GLUT_RGBA);
	glutInitWindowSize(500, 500);
	glutInitWindowPosition(0, 0);
	glutCreateWindow("图形太难了");
	init();
	draw_obj();
	glutDisplayFunc(display);
	glutReshapeFunc(changeSize);
	glutKeyboardFunc(keyboard);
	glutSpecialFunc(keySpecail);
	glutMouseFunc(mouse_Click);
	glutMotionFunc(mouse_Move);
	glutMainLoop();
	return 0;
}

// 运行程序: Ctrl + F5 或调试 >“开始执行(不调试)”菜单
// 调试程序: F5 或调试 >“开始调试”菜单

// 入门提示: 
//   1. 使用解决方案资源管理器窗口添加/管理文件
//   2. 使用团队资源管理器窗口连接到源代码管理
//   3. 使用输出窗口查看生成输出和其他消息
//   4. 使用错误列表窗口查看错误
//   5. 转到“项目”>“添加新项”以创建新的代码文件，或转到“项目”>“添加现有项”以将现有代码文件添加到项目
//   6. 将来，若要再次打开此项目，请转到“文件”>“打开”>“项目”并选择 .sln 文件
