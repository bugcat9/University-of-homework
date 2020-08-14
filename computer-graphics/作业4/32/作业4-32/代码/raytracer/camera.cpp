#include"camera.h"

OrthographicCamera::OrthographicCamera(Vec3f center, Vec3f direction, Vec3f up, float size)
{
	m_center = center;
	m_direction = direction;
	m_direction.Normalize();

	if (direction.Dot3(up) == 0) {
		m_up = up;
		Vec3f::Cross3(m_horizontal, direction, up);//�õ�m_horizontal����
	}
	else
	{	
		Vec3f::Cross3(m_horizontal, direction, up);//�õ�m_horizontal����
		Vec3f::Cross3(m_up, m_horizontal, direction);
	}
	m_up.Normalize();
	m_horizontal.Normalize();
	m_size = size;
	m_tmin = -FLT_MAX;	//���ֵ�����Լ�����һ��������
}

Ray OrthographicCamera::generateRay(Vec2f point)
{	
	//��ʽ��origin = center + (x-0.5)*size*horizontal + (y-0.5)*size*up
	//�õ���ʼ��
	Vec3f orig = m_center + (point.x() - 0.5) * m_size * m_horizontal + (point.y() - 0.5) * m_size * m_up;
	//orig.Write();
	return Ray(orig,m_direction);
}

float OrthographicCamera::getTMin() const
{
	return m_tmin;
}
