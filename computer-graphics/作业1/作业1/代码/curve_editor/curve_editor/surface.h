#pragma once
#include "spline.h"
#include "curve.h"
class Surface :public Spline {
	virtual void OutputBezier(FILE* file);
	virtual void OutputBSpline(FILE* file);
};

class SurfaceOfRevolution :public Surface {
public :
	SurfaceOfRevolution(Curve* c);
	virtual void Paint(ArgParser* args);

	// ����ʵ����������ת���� FOR CONVERTING BETWEEN SPLINE TYPES
	virtual void OutputBezier(FILE* file);
	virtual void OutputBSpline(FILE* file);
	// ���ڲ��������ε� FOR GENERATING TRIANGLES
	virtual TriangleMesh* OutputTriangles(ArgParser* args);

	// ���ڵõ����Ƶ�� FOR CONTROL POINT PICKING
	virtual int getNumVertices();
	virtual Vec3f getVertex(int i);

	// ���ڱ༭������ FOR EDITING OPERATIONS
	virtual void moveControlPoint(int selectedPoint, float x, float y);
	virtual void addControlPoint(int selectedPoint, float x, float y);
	virtual void deleteControlPoint(int selectedPoint);
protected:
	Curve* curve;
};

class BezierPatch :public Surface {
public :
	BezierPatch();
	virtual void Paint(ArgParser* args);
	/*void set(int i, Vec3f v);*/
	// ���ڲ��������ε� FOR GENERATING TRIANGLES
	virtual TriangleMesh* OutputTriangles(ArgParser* args);
	
protected:
	

};