#pragma once
#include<iostream>
#include<vector>
#include"matrix.h"
#include"image.h"
#include <time.h>
using namespace std;
//��������ϵͳ
class IFS
{
public:
	//���캯��
	IFS(char* input_file) {
		this->ReadDescription(input_file);
	};
	//��������
	~IFS() {
		//�ͷ��ڴ�
		if(m_transformMatrixs!=NULL)
			delete[] m_transformMatrixs;
		m_transformMatrixs = NULL;
		if(m_probability!=NULL)
			delete[] m_probability;
		m_probability = NULL;
	};


	//��Ⱦ
	void Render(Image* image, int numOfPoints, int numOfiters);

protected:
	//��ȡ�ļ��ĺ���
	void ReadDescription(char* input_file);

	int n;	//�任������
	Matrix * m_transformMatrixs;	//�任����
	float * m_probability;//��������

};


