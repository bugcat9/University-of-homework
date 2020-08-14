#include "surface.h"
#include<gl/glut.h>

void Surface::OutputBezier(FILE* file)
{
}

void Surface::OutputBSpline(FILE* file)
{
}


SurfaceOfRevolution::SurfaceOfRevolution(Curve* c)
{
	this->curve = c;
}

void SurfaceOfRevolution::Paint(ArgParser* args)
{
	this->curve->Paint(args);
}

void SurfaceOfRevolution::OutputBezier(FILE* file)
{
	fprintf(file, "%s \n", "surface_of_revolution");
	this->curve->OutputBezier(file);
}

void SurfaceOfRevolution::OutputBSpline(FILE* file)
{	
	fprintf(file, "%s \n", "surface_of_revolution");
	this->curve->OutputBSpline(file);
}

TriangleMesh* SurfaceOfRevolution::OutputTriangles(ArgParser* args)
{
	 return  this->curve->OutputTriangles(args);
}

int SurfaceOfRevolution::getNumVertices()
{
	return curve->getNumVertices();
}

Vec3f SurfaceOfRevolution::getVertex(int i)
{
	
	return curve->getVertex(i);
}

void SurfaceOfRevolution::moveControlPoint(int selectedPoint, float x, float y)
{
	this->curve->moveControlPoint(selectedPoint, x, y);
}

void SurfaceOfRevolution::addControlPoint(int selectedPoint, float x, float y)
{
	this->curve->addControlPoint(selectedPoint, x, y);
}

void SurfaceOfRevolution::deleteControlPoint(int selectedPoint)
{
	this->curve->deleteControlPoint(selectedPoint);
}

BezierPatch::BezierPatch()
{
	this->num_vertices = 16;
	this->vertexs.resize(16);
}

void BezierPatch::Paint(ArgParser* args)
{

	glPointSize(5);
	//绘制控制点
	glBegin(GL_POINTS);

	glColor3f(1, 0, 0);
	for (int i = 0; i < num_vertices; i++) {
		glVertex3f(vertexs[i].x(), vertexs[i].y(), vertexs[i].z());
	}

	glEnd();
	glFlush();
	////将控制点连起来
	glColor3f(0, 0, 1);
	glBegin(GL_LINES);

	for (int i = 0; i < 4; i++) {
		Vec3f v0 = vertexs[i*4];
		for (int j = 1; j < 4; j++) {
			glVertex3f(v0.x(), v0.y(), v0.z());
			glVertex3f(vertexs[i*4+j].x(), vertexs[i * 4 + j].y(), vertexs[i * 4 + j].z());
			v0 = vertexs[i * 4 + j];
		}
	}
	glEnd();
	glFlush();
}


TriangleMesh* BezierPatch::OutputTriangles(ArgParser* args)
{	
	int u_tess = args->patch_tessellation;		//有多少个面
	int v_tess = args->patch_tessellation;		//多少个段	

	double offset1 = 1.0 /args->patch_tessellation;	//画多少段
	double offset2 = 1.0 / v_tess;	//画多少段
	TriangleNet* m = new TriangleNet(u_tess,v_tess);
	
	for (double i = 0,t=0.0; t<=1.0; i++, t += offset1) {
		//遍历面

		BezierCurve* beziercurve = new BezierCurve(4);
		//得到4个控制点
		Vec3f p0= beziercurve->getBezier(vertexs[0], vertexs[1], vertexs[2], vertexs[3],t);
		Vec3f p1 = beziercurve->getBezier(vertexs[4], vertexs[5], vertexs[6], vertexs[7], t);
		Vec3f p2 = beziercurve->getBezier(vertexs[8], vertexs[9], vertexs[10], vertexs[11], t);
		Vec3f p3 = beziercurve->getBezier(vertexs[12], vertexs[13], vertexs[14], vertexs[15], t);
		
		
		for (double j = 0,s=0.0; s<=1.0; j++,s+=offset2) {
			//遍历点
			Vec3f v= beziercurve->getBezier(p0,p1,p2,p3,s);
			m->SetVertex(i, j, v);
			//cout << j;
		}
	}
	return m;
}



