#pragma once
#ifndef GROUP_H
#define GROUP_H

#include"object3d.h"
class Group :public Object3D 
{
public:
	Group(int num_objects) {
		m_num_objects = num_objects;
		m_objects = new Object3D* [num_objects];
	};

	~Group() {
		delete []m_objects;
	};
	virtual bool intersect(const Ray& r, Hit& h, float tmin);
	void addObject(int index, Object3D* obj);
private:
	int m_num_objects;
	Object3D **m_objects;
};
#endif // !GROUP_H