#include"sphere.h"

Sphere::Sphere(Vec3f center, float radius, Material* current_material)
{
	m_center = center;
	m_radius = radius;
	m_material = current_material;
}

bool Sphere::intersect(const Ray& r, Hit& h, float tmin)
{	
	//��Ҫ����ߺ���Ľ���
	//���Բο�ppt����Ͷ�����㷨
	//Rd��Rdt2 + 2Rd��Rot + Ro��Ro - r2 = 0
	//a=1,b = 2Rd��Ro,c = Ro��Ro �C r2

	//ת��Ϊ����Ϊԭ��� direction��origin
	Vec3f direction = r.getDirection();
	Vec3f origin = r.getOrigin()-this->m_center;
	

	/*if (r.getOrigin().x() == 0 && r.getOrigin().y() == 0) {
		origin.Write();
	}*/


	//���b��c��Ȼ��ʹ���б�ʽ
	double b = 2*direction.Dot3(origin);
	double c = origin.Dot3(origin) - m_radius * m_radius;

	//�ж���û�и� b^2 -4ac ������a=1
	double d = b * b - 4 * c;
	if (d > 0.00001) {
		d = sqrt(d);	//������
		//direction.Write();
		//origin.Write();
		//˵�����ڸ�������С�ĸ�
		double t = -(b + d) / 2;
		//˵������ײ���������Ҫ�޸�hit��ֵ
		if (t >=tmin && t < h.getT()) {
			//cout << "���ﱻ����" << endl;
			h.set(t, m_material, r);
			//˵���ཻ��
			return true;
		}
	}

	return false;
}
