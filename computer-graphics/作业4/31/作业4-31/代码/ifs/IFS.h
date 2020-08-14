#pragma once
#include<iostream>
#include<vector>
#include"matrix.h"
#include"image.h"
#include <time.h>
using namespace std;
//迭代函数系统
class IFS
{
public:
	//构造函数
	IFS(char* input_file) {
		this->ReadDescription(input_file);
	};
	//析构函数
	~IFS() {
		//释放内存
		if(m_transformMatrixs!=NULL)
			delete[] m_transformMatrixs;
		m_transformMatrixs = NULL;
		if(m_probability!=NULL)
			delete[] m_probability;
		m_probability = NULL;
	};


	//渲染
	void Render(Image* image, int numOfPoints, int numOfiters);

protected:
	//读取文件的函数
	void ReadDescription(char* input_file);

	int n;	//变换的数量
	Matrix * m_transformMatrixs;	//变换数组
	float * m_probability;//概率数组

};


