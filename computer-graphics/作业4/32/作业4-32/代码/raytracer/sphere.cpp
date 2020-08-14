#include"sphere.h"

Sphere::Sphere(Vec3f center, float radius, Material* current_material)
{
	m_center = center;
	m_radius = radius;
	m_material = current_material;
}

bool Sphere::intersect(const Ray& r, Hit& h, float tmin)
{	
	//需要求光线和球的交点
	//可以参考ppt光线投射求交算法
	//Rd·Rdt2 + 2Rd·Rot + Ro·Ro - r2 = 0
	//a=1,b = 2Rd·Ro,c = Ro·Ro – r2

	//转化为以球为原点的 direction、origin
	Vec3f direction = r.getDirection();
	Vec3f origin = r.getOrigin()-this->m_center;
	

	/*if (r.getOrigin().x() == 0 && r.getOrigin().y() == 0) {
		origin.Write();
	}*/


	//求出b和c，然后使用判别式
	double b = 2*direction.Dot3(origin);
	double c = origin.Dot3(origin) - m_radius * m_radius;

	//判断有没有根 b^2 -4ac ，其中a=1
	double d = b * b - 4 * c;
	if (d > 0.00001) {
		d = sqrt(d);	//开根号
		//direction.Write();
		//origin.Write();
		//说明存在根，求解较小的根
		double t = -(b + d) / 2;
		//说明离碰撞点更近，需要修改hit的值
		if (t >=tmin && t < h.getT()) {
			//cout << "这里被调用" << endl;
			h.set(t, m_material, r);
			//说明相交了
			return true;
		}
	}

	return false;
}
