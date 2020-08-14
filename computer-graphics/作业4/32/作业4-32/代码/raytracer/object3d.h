#pragma once
#include"hit.h"
#include"material.h"
#include"ray.h"

class Object3D
{
public:
	Object3D() {};
	~Object3D() {};
	//相交的类
	virtual bool intersect(const Ray& r, Hit& h, float tmin) = 0;
protected:
	Material *m_material;	//材质
};


