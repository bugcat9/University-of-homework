#pragma once
#include"object3d.h"

class Sphere :public Object3D
{
public:
	Sphere(Vec3f center, float radius, Material* current_material);
	~Sphere() {};
	virtual bool intersect(const Ray& r, Hit& h, float tmin);
protected:

	Vec3f m_center;	//����λ��
	float m_radius;	//�뾶
};
