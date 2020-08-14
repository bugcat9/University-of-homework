#include "group.h"

bool Group::intersect(const Ray& r, Hit& h, float tmin)
{	
	//循环调用算法
	bool res = false;
	for (int i = 0; i < m_num_objects; i++) {
		if (m_objects[i]->intersect(r, h, tmin)) {
			/*cout << "改变的" << i << endl;*/
			res= true;
		}
		
	}
	return res;
}

void Group::addObject(int index, Object3D* obj)
{
	this->m_objects[index] = obj;
}
