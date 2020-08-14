#ifndef JOINT_H
#define JOINT_H

#include <vector>
#include <vecmath.h>

struct Joint
{
	Matrix4f transform; // transform relative to its parent
	std::vector< Joint* > children; // list of children

	// This matrix transforms world space into joint space for the initial ("bind") configuration of the joints.
	//�þ�������ռ�ת��Ϊ�ؽڿռ䣬��ʵ�ֹؽڵĳ�ʼ�����󶨡������á�
	Matrix4f bindWorldToJointTransform;


	// This matrix maps joint space into world space for the *current* configuration of the joints.
	//�˾��󽫹ؽڵġ���ǰ������ӳ�䵽����ռ䡣
	Matrix4f currentJointToWorldTransform;
};

#endif
