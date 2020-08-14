#pragma once
#include "spline.h"
#include "matrix.h"
class Curve :public Spline {
public:
	Curve(int num_vertices);
	virtual void Paint(ArgParser* args);
	
	
protected:
	

};


class BezierCurve :public Curve {
public :
	BezierCurve(int num_vertices);

	Vec3f getBezier(Vec3f v1, Vec3f v2, Vec3f v3, Vec3f v4, double t);		//进行点的转化的函数

	virtual void Paint(ArgParser* args);		//绘画函数
	// 用于实现样条类型转换的 FOR CONVERTING BETWEEN SPLINE TYPES
	virtual void OutputBezier(FILE* file);
	virtual void OutputBSpline(FILE* file);
	// 用于产生三角形的 FOR GENERATING TRIANGLES
	virtual TriangleMesh* OutputTriangles(ArgParser* args);

	virtual void addControlPoint(int selectedPoint, float x, float y);
	virtual void deleteControlPoint(int selectedPoint);
private:

};

class BSplineCurve :public Curve
{
public:
	BSplineCurve(int num_vertices);
	virtual void Paint(ArgParser* args);
	Vec3f getBSpline(Vec3f v1, Vec3f v2, Vec3f v3, Vec3f v4, double t);

	// 用于实现样条类型转换的 FOR CONVERTING BETWEEN SPLINE TYPES
	virtual void OutputBezier(FILE* file);
	virtual void OutputBSpline(FILE* file);
	// 用于产生三角形的 FOR GENERATING TRIANGLES
	virtual TriangleMesh* OutputTriangles(ArgParser* args);
private:

};
