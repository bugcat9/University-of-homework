#pragma once
#include<vector>
using namespace std;
//�Զ����������� particle�����ӣ�
//����������洢�������ӵ����ӣ�����ϵ���;�̬���ȡ�
class particle {

public:
	particle() {
		//m_Elastic_coefficient = 0.0;
		m_Capacitance_coefficient = 0.0;
		//r = 0.0;
		m_mass = 1.0;
		isStill = false;	//����״̬��Ϊ����ֹ
	}

	vector< int> m_link_particles;	//�����ӵ�����,�洢���±꼴��
	
	vector<float> m_Elastic_coefficient;	//����ϵ����

	float m_Capacitance_coefficient;	//Ħ������ϵ��

	vector<float> r;		//��̬����

	float m_mass;	//����

	bool isStill;	//�����Ƿ�ֹ�������ø������Ƿ�һֱΪ��ֹ״̬

	//Vector3f m_gravity; //����

	//Vector3f m_friction; //����

	//Vector3f m_f;//����

	//Vector3f m_totalForce;	//����

	

};