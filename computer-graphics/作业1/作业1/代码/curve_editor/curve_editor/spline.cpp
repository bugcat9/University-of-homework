#include "spline.h"
#include"matrix.h"

int Spline::getNumVertices()
{
	return num_vertices;
}

Vec3f Spline::getVertex(int i)
{	
	//防止越界
	assert(i < num_vertices && i>=0);
	return this->vertexs[i];
}

void Spline::moveControlPoint(int selectedPoint, float x, float y)
{
	cout << "移动点";
	this->vertexs[selectedPoint].Set(x, y, 0);
}

void Spline::addControlPoint(int selectedPoint, float x, float y)
{
	cout << "加点";
	
	Vec3f v(x, y, 0);
	vertexs.insert(vertexs.begin() + selectedPoint, v);
	num_vertices++;

}

void Spline::deleteControlPoint(int selectedPoint)
{	
	cout << "删除点";
	if (num_vertices > 4) {
		num_vertices--;
		vertexs.erase(vertexs.begin()+selectedPoint);
	}
	
}

void Spline::set(int i, Vec3f v)
{
	vertexs[i] = v;
}
