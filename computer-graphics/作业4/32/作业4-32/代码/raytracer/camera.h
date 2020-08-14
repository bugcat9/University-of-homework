#pragma once
#ifndef CAMERA_H
#define CAMERA_H


#include"ray.h"
#include"vectors.h"
class Camera
{
public:
	Camera() {};
	~Camera() {};
	virtual Ray generateRay(Vec2f point) = 0;
	virtual float getTMin() const = 0;
private:

};

class OrthographicCamera :public Camera
{
public:
	OrthographicCamera(Vec3f center, Vec3f direction, Vec3f up, float size);
	~OrthographicCamera() {};

	virtual Ray generateRay(Vec2f point) ;
	virtual float getTMin() const ;
protected:

	Vec3f m_center;		//相机中心
	Vec3f m_direction;	//方向，指眼睛看向的方向
	Vec3f m_horizontal;	//垂直方向
	Vec3f m_up;			//竖直方向
	float m_size;		//屏幕大小
	float m_tmin;		//相机最小值
};
#endif // CAMERA_H