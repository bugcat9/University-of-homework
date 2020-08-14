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

	// 用于实现样条类型转换的 FOR CONVERTING BETWEEN SPLINE TYPES
	virtual void OutputBezier(FILE* file);
	virtual void OutputBSpline(FILE* file);
	// 用于产生三角形的 FOR GENERATING TRIANGLES
	virtual TriangleMesh* OutputTriangles(ArgParser* args);

	// 用于得到控制点的 FOR CONTROL POINT PICKING
	virtual int getNumVertices();
	virtual Vec3f getVertex(int i);

	// 用于编辑操作的 FOR EDITING OPERATIONS
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
	// 用于产生三角形的 FOR GENERATING TRIANGLES
	virtual TriangleMesh* OutputTriangles(ArgParser* args);
	
protected:
	

};