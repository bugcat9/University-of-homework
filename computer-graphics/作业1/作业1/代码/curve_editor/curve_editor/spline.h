#pragma once
#include "triangle_mesh.h"
#include"vectors.h"
#include"arg_parser.h"
#include<vector>
using namespace std;
class Spline {
public:
	virtual void Paint(ArgParser* args)=0;
	// 用于实现样条类型转换的 FOR CONVERTING BETWEEN SPLINE TYPES
	virtual void OutputBezier(FILE* file)=0;
	virtual void OutputBSpline(FILE* file)=0;

	// 用于得到控制点的 FOR CONTROL POINT PICKING
	virtual int getNumVertices();
	virtual Vec3f getVertex(int i);

	// 用于编辑操作的 FOR EDITING OPERATIONS
	virtual void moveControlPoint(int selectedPoint, float x, float y);
	virtual void addControlPoint(int selectedPoint, float x, float y);
	virtual void deleteControlPoint(int selectedPoint);

	// 用于产生三角形的 FOR GENERATING TRIANGLES
	virtual TriangleMesh* OutputTriangles(ArgParser* args)=0;

	virtual void  set(int i, Vec3f v);		//设置点

protected:

	int num_vertices;		//控制点个数
	vector<Vec3f>vertexs;	//控制点数组，采取vector存储
};
