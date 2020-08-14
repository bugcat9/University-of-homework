#ifndef JOINT_H
#define JOINT_H

#include <vector>
#include <vecmath.h>

struct Joint
{
	Matrix4f transform; // transform relative to its parent
	std::vector< Joint* > children; // list of children

	// This matrix transforms world space into joint space for the initial ("bind") configuration of the joints.
	//该矩阵将世界空间转换为关节空间，以实现关节的初始（“绑定”）配置。
	Matrix4f bindWorldToJointTransform;


	// This matrix maps joint space into world space for the *current* configuration of the joints.
	//此矩阵将关节的“当前”配置映射到世界空间。
	Matrix4f currentJointToWorldTransform;
};

#endif
