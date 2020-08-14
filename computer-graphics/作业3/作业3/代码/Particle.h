#pragma once
#include<vector>
using namespace std;
//自定义数据类型 particle（粒子）
//在粒子里面存储其所连接的粒子，弹性系数和静态长度。
class particle {

public:
	particle() {
		//m_Elastic_coefficient = 0.0;
		m_Capacitance_coefficient = 0.0;
		//r = 0.0;
		m_mass = 1.0;
		isStill = false;	//正常状态下为不静止
	}

	vector< int> m_link_particles;	//所连接的粒子,存储器下标即可
	
	vector<float> m_Elastic_coefficient;	//弹性系数；

	float m_Capacitance_coefficient;	//摩檫力的系数

	vector<float> r;		//静态长度

	float m_mass;	//质量

	bool isStill;	//设置是否静止，即设置该粒子是否一直为静止状态

	//Vector3f m_gravity; //重力

	//Vector3f m_friction; //阻力

	//Vector3f m_f;//弹力

	//Vector3f m_totalForce;	//合力

	

};