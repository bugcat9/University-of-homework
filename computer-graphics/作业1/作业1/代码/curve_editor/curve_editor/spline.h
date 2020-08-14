#pragma once
#include "triangle_mesh.h"
#include"vectors.h"
#include"arg_parser.h"
#include<vector>
using namespace std;
class Spline {
public:
	virtual void Paint(ArgParser* args)=0;
	// ����ʵ����������ת���� FOR CONVERTING BETWEEN SPLINE TYPES
	virtual void OutputBezier(FILE* file)=0;
	virtual void OutputBSpline(FILE* file)=0;

	// ���ڵõ����Ƶ�� FOR CONTROL POINT PICKING
	virtual int getNumVertices();
	virtual Vec3f getVertex(int i);

	// ���ڱ༭������ FOR EDITING OPERATIONS
	virtual void moveControlPoint(int selectedPoint, float x, float y);
	virtual void addControlPoint(int selectedPoint, float x, float y);
	virtual void deleteControlPoint(int selectedPoint);

	// ���ڲ��������ε� FOR GENERATING TRIANGLES
	virtual TriangleMesh* OutputTriangles(ArgParser* args)=0;

	virtual void  set(int i, Vec3f v);		//���õ�

protected:

	int num_vertices;		//���Ƶ����
	vector<Vec3f>vertexs;	//���Ƶ����飬��ȡvector�洢
};
