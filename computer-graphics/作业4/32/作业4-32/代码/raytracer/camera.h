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

	Vec3f m_center;		//�������
	Vec3f m_direction;	//����ָ�۾�����ķ���
	Vec3f m_horizontal;	//��ֱ����
	Vec3f m_up;			//��ֱ����
	float m_size;		//��Ļ��С
	float m_tmin;		//�����Сֵ
};
#endif // CAMERA_H