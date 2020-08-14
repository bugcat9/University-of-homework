#include "curve.h"
#include <GL/glut.h>
#include <GL/gl.h>
#include<iostream>
#include <cassert>
using namespace std;
Curve::Curve(int num_vertices)
{
	this->num_vertices = num_vertices;
	vertexs.resize(num_vertices);
}
void Curve::Paint(ArgParser* args)
{
	int n = args->curve_tessellation;
	glPointSize(5);
	//绘制控制点
	glBegin(GL_POINTS);
	
	glColor3f(1, 0, 0);
	for (int i = 0; i < num_vertices; i++) {
		glVertex3f(vertexs[i].x(), vertexs[i].y(), vertexs[i].z());
	}

	glEnd();
	glFlush();
	//将控制点连起来
	glColor3f(0, 0, 1);
	glBegin(GL_LINES);
	Vec3f v0 = vertexs[0];
	for (int i = 1; i < this->num_vertices; i++) {

		glVertex3f(v0.x(), v0.y(), v0.z());
		glVertex3f(vertexs[i].x(), vertexs[i].y(), vertexs[i].z());
		v0 = vertexs[i];
	}
	glEnd();
	glFlush();
}


BezierCurve::BezierCurve(int num_vertices):Curve(num_vertices)
{
	assert((num_vertices - 1) % 3 == 0);
}

//计算出得到Bezier曲线上的点
//B(t)=v1*(1-t)^3+3* v2 *t*(1-t)^2+3*v3*t^2*(1-t)+v4*t^3
Vec3f BezierCurve::getBezier(Vec3f v1, Vec3f v2, Vec3f v3, Vec3f v4, double t)
{	
	
	//计算
	double a1 = pow((1 - t), 3);
	double a2 = pow((1 - t), 2) * 3 * t;
	double a3 = 3 * pow(t, 2) * (1 - t);
	double a4 = pow(t, 3);

	double x = a1 * v1.x() + a2 * v2.x() + a3 * v3.x() + a4 * v4.x();
	double y = a1 * v1.y() + a2 * v2.y() + a3 * v3.y() + a4 * v4.y();
	double z= a1 * v1.z() + a2 * v2.z() + a3 * v3.z() + a4 * v4.z();

	return Vec3f(x,y,z);
}


//Bezier画图函数
void BezierCurve::Paint(ArgParser* args)
{
	Curve::Paint(args);
	int n = args->curve_tessellation;	
	
	glBegin(GL_LINES);
	glColor3f(0, 1, 0);
	
	double offset = 1.0 / n;	//画Bezier曲线
	for (int i = 0; i < this->num_vertices-1; i=i+3) {
		//外层循环第几段Bezier曲线

		Vec3f v0 = vertexs[i];	//最开始的点
		for (double t = 0.0; t <= 1.0; t += offset) {
			//内层循环0到1负责画出相应的点
			Vec3f v1 = getBezier(vertexs[i], vertexs[i+1], vertexs[i+2], vertexs[i+3], t);
			glVertex3f(v0.x(), v0.y(), v0.z());
			glVertex3f(v1.x(), v1.y(), v1.z());
			v0 = v1;
		}
	}
	glEnd();
	glFlush();
	
	
}

//Bezier曲线生成Bezier曲线直接写即可
void BezierCurve::OutputBezier(FILE* file)
{
	fprintf(file, "%s\n", "bezier");
	fprintf(file, "%s %d \n", "num_vertices ",this->num_vertices);
	for (int i = 0; i < this->num_vertices; i++) {
		fprintf(file, "%f %f %f \n", this->vertexs[i].x(), this->vertexs[i].y(), this->vertexs[i].z());
	}
}

//由Bezier曲线生成BSplin曲线，需要进行计算
void BezierCurve::OutputBSpline(FILE* file)
{	
	fprintf(file, "%s\n", "bspline");
	int n = ((this->num_vertices - 1) / 3) * 4;
	fprintf(file, "%s %d \n", "num_vertices ", n);
	//Bezier的系数
	float f1[16] = { 
					-1,3,-3,1,
					3,-6,3,0,
					-3,3,0,0,
					1,0,0,0 
				};



	//BSpline的系数
	float f2[16] = {
		-1,3,-3,1,
		3,-6,0,4,
		-3,3,3,1,
		1,0,0,0
	};

	for (int i = 0; i < 16; i++) { f2 [i]= f2[i] / 6.0; }	//系数要除以6

	Matrix B1(f1);//Bezier的系数
	Matrix B2(f2);//BSpline的系数

	B2.Inverse();	//先将B2进行逆

	Matrix m = B1 * B2;
	/*float* t = m.glGet();*/
	float x = 0.0, y = 0.0, z = 0.0;
	//考虑到转化时点可能多余4个，所以取四个点一转化四个点一转化，
	//并且后一段的起点是前一段的末点，eg:0,1,2,3,4,5,6七个点
	//分成两段0，1，2，3 和3，4，5，6
	for (int i = 0; i < this->num_vertices-1; i += 3) {
		//第一段for循环是外层的for循环
		for (int j = 0; j <4; j++)
		{
			//第二层的for循环是指每一段的四个点
			x = 0.0; y = 0.0; z = 0.0;
			for (int k = 0; k < 4; k++) {
				//第三层for循环，是四个点相乘
				x += m.Get(j, k) * vertexs[i + k].x();
				y += m.Get(j, k) * vertexs[i + k].y();
				z += m.Get(j, k) * vertexs[i + k].z();
			}
			fprintf(file, "%f %f %f \n", x, y, z);
		}
	}



}

TriangleMesh* BezierCurve::OutputTriangles(ArgParser* args)
{
	int v_tess = args->curve_tessellation;		//有多少个点
	int u_tess = args->revolution_tessellation;		//旋转多少个面	//先不加1看看



	float theta = 0.0;	//旋转的角度
	TriangleNet* m = new  TriangleNet(u_tess,(v_tess+1)*((num_vertices-1)/3)-1);	//最终返回的网格

	Matrix a;
	float x = 0.0, y = 0.0, z = 0.0;
	double offset = 1.0 / v_tess;	//

	for (int i = 0; i < u_tess+1; i++) {
		//theta = ((360.0 / u_tess) * i) / 180.0 * 3.14; 简化一下
		theta = (2.0 / u_tess) * i * 3.14;
		//第一层循环，循环每个面
		Matrix b = a.MakeYRotation(theta);

		//第二层循环,循环Bezier曲线上对的点
		int k = 0;
		for (int j = 0; j < this->num_vertices - 1; j = j + 3) {
			for (double t = 0.0; t <= 1.0; t += offset,k++) {
					Vec3f v = getBezier(vertexs[j], vertexs[j+ 1], vertexs[j + 2], vertexs[j + 3], t);//得到曲线上的点

					x = b.Get(0, 0) * v.x() + b.Get(1, 0) * v.y() + b.Get(2, 0) * v.z() + b.Get(3, 0) * 1.0;//将点进行转化
					y = b.Get(0, 1) * v.x() + b.Get(1, 1) * v.y() + b.Get(2, 1) * v.z() + b.Get(3, 1) * 1.0;
					z = b.Get(0, 2) * v.x() + b.Get(1, 2) * v.y() + b.Get(2, 2) * v.z() + b.Get(3, 2) * 1.0;

					m->SetVertex(i, k, Vec3f(x, y, z));
					//cout << k << "	";
			}
		}
		
	}

	return m;
}

void BezierCurve::addControlPoint(int selectedPoint, float x, float y)
{
}

void BezierCurve::deleteControlPoint(int selectedPoint)
{
}



BSplineCurve::BSplineCurve(int num_vertices):Curve(num_vertices)
{

}

//BSpline曲线
void BSplineCurve::Paint(ArgParser* args)
{
	Curve::Paint(args);
	int n = args->curve_tessellation;

	glBegin(GL_LINES);
	glColor3f(0, 1, 0);
	
	//cout << num_vertices << endl;
	double offset = 1.0 / n;	//画Bezier曲线
	for (int i = 3; i < num_vertices; i++) {
		Vec3f v0 = getBSpline(vertexs[i - 3], vertexs[i - 2], vertexs[i - 1], vertexs[i], 0.0);	//最开始的点
		for (double t = offset; t <= 1.0; t += offset) {
			
			//cout << v0.x() << "	" << v0.y() << "	" << v0.z() << endl;
			Vec3f v1 = getBSpline(vertexs[i-3], vertexs[i-2], vertexs[i-1], vertexs[i], t);
			glVertex3f(v0.x(), v0.y(), v0.z());
			glVertex3f(v1.x(), v1.y(), v1.z());
			v0 = v1;
		}
	}
	
	glEnd();
	glFlush();

}

Vec3f BSplineCurve::getBSpline(Vec3f v1, Vec3f v2, Vec3f v3, Vec3f v4, double t)
{	
	//计算
	double a1 = pow((1 - t), 3)/6;
	double a2 = (3*pow(t,3)-6*pow(t,2)+4)/6;
	double a3 = (-3*pow(t,3)+3*pow(t,2)+3*t+1)/6;
	double a4 = pow(t, 3)/6;

	double x = a1 * v1.x() + a2 * v2.x() + a3 * v3.x() + a4 * v4.x();
	double y = a1 * v1.y() + a2 * v2.y() + a3 * v3.y() + a4 * v4.y();

	return Vec3f(x, y, 0);
}

//BSpline转Bezier
void BSplineCurve::OutputBezier(FILE* file)
{
	fprintf(file, "%s\n", "bezier");
	fprintf(file, "%s %d \n", "num_vertices ", this->num_vertices);

	float f1[] = {
					-1,3,-3,1,
					3,-6,3,0,
					-3,3,0,0,
					1,0,0,0
	};
	//BSpline的系数
	float f2[] = {
		-1,3,-3,1,
		3,-6,0,4,
		-3,3,3,1,
		1,0,0,0
	};

	for (int i = 0; i < 16; i++) { f2[i] = f2[i] / 6; }	//系数要除以6

	Matrix B1(f1);//Bezier的系数
	Matrix B2(f2);//BSpline的系数

	B1.Inverse();	//先将B2进行逆

	Matrix m = B2 * B1;

	float x = 0.0, y = 0.0, z = 0.0;	//有待填坑
	//第一段for循环是外层的for循环
	for (int j = 0; j < 4; j++)
	{
		//第二层的for循环是指每一段的四个点
		x = 0.0; y = 0.0; z = 0.0;
		for (int k = 0; k < 4; k++) {
			//第三层for循环，是四个点相乘
			x += m.Get(j, k) * vertexs[ k].x();
			y += m.Get(j, k) * vertexs[ k].y();
			z += m.Get(j, k) * vertexs[ k].z();
		}
		fprintf(file, "%f %f %f \n", x, y, z);
	}
}

//BSplineCurve转BSpline
void BSplineCurve::OutputBSpline(FILE* file)
{	
	fprintf(file, "%s\n", "bspline");
	fprintf(file, "%s %d \n", "num_vertices ", this->num_vertices);
	for (int i = 0; i < this->num_vertices; i++) {
		fprintf(file, "%f %f %f \n", this->vertexs[i].x(), this->vertexs[i].y(), this->vertexs[i].z());
	}
	
}

TriangleMesh* BSplineCurve::OutputTriangles(ArgParser* args)
{
	int v_tess = args->curve_tessellation;		//有多少个点
	int u_tess = args->revolution_tessellation;		//旋转多少个面	//先不加1看看



	float theta = 0.0;	//旋转的角度
	TriangleNet* m = new  TriangleNet(u_tess,(v_tess+1)*(num_vertices-3)-1);	//最终返回的网格

	Matrix a;
	float x = 0.0, y = 0.0, z = 0.0;
	double offset = 1.0 / v_tess;	//

	for (int i = 0; i < u_tess + 1; i++) {
		//theta = ((360.0 / u_tess) * i) / 180.0 * 3.14; 简化一下

		theta = (2.0 / u_tess) * i * 3.14;
		//第一层循环，循环每个面
		Matrix b = a.MakeYRotation(theta);

		//针对面里面的点
		int k = 0;
		for (int j = 3; j < num_vertices; j++) {
			
			for (double t = 0.0; t <= 1.0; t += offset) {

				Vec3f v = getBSpline(vertexs[j - 3], vertexs[j - 2], vertexs[j - 1], vertexs[j], t);
			
				x = b.Get(0, 0) * v.x() + b.Get(0, 1) * v.y() + b.Get(0,2) * v.z() + b.Get(0,3) * 1.0;//将点进行转化
				y = b.Get(0, 1) * v.x() + b.Get(1, 1) * v.y() + b.Get(2,1) * v.z() + b.Get(3,1) * 1.0;
				z = b.Get(0,2 ) * v.x() + b.Get(1, 2) * v.y() + b.Get(2,2) * v.z() + b.Get(3,2) * 1.0;
				
				//cout << k<<"	";
				m->SetVertex(i, k, Vec3f(x, y, z));
				
				k++;
			}
		}
		//cout << endl;
	}

	return m;
}




