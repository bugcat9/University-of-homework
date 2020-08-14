#include "SkeletalModel.h"
#include <FL/Fl.H>

using namespace std;

void SkeletalModel::load(const char *skeletonFile, const char *meshFile, const char *attachmentsFile)
{
	loadSkeleton(skeletonFile);

	m_mesh.load(meshFile);
	m_mesh.loadAttachments(attachmentsFile, m_joints.size());

	computeBindWorldToJointTransforms();
	updateCurrentJointToWorldTransforms();
}

void SkeletalModel::draw(Matrix4f cameraMatrix, bool skeletonVisible)
{
	// draw() gets called whenever a redraw is required
	// (after an update() occurs, when the camera moves, the window is resized, etc)

	m_matrixStack.clear();
	m_matrixStack.push(cameraMatrix);
	//skeletonVisible = false;
	if(skeletonVisible)
	{
		drawJoints();

		drawSkeleton();
	}
	else
	{

		// Clear out any weird matrix we may have been using for drawing the bones and revert to the camera matrix.
		//������ǿ���һֱ���ڻ��ƹ������κι������Ȼ��ԭΪ�������

		glLoadMatrixf(m_matrixStack.top());
		// Tell the mesh to draw itself.
		m_mesh.draw();
	}
}

void SkeletalModel::loadSkeleton( const char* filename )
{
	// Load the skeleton from file here.
	//cout << filename<<endl;

	FILE* file = fopen(filename, "rb");
	float x=0, y=0, z=0;
	int index = 0;
	while (!feof(file)) {
		//���ж�ȡ������Ҫ֮����иĽ�
		if (m_joints.size() == 18)
			break;

		fscanf(file, "%f %f %f %i", &x, &y, &z, &index);
		//cout << x << "	" << y << "	" << z << "	" << index << endl;
		Joint* joint = new Joint();

		joint->transform = Matrix4f::translation(x, y, z);		//�����仯�����ʼ��

		if (index == -1)		//˵���Ǹ��ڵ�
			this->m_rootJoint = joint;
		else {
			//˵�����Ǹ��ڵ㣬��ô��ҪѰ���丸�ڵ�,�����ڵ���Ӻ��ӽڵ�
			 this->m_joints[index]->children.push_back(joint);
		}
		this->m_joints.push_back(joint);
			
	}
	fclose(file);
	cout << "�ؽ�����" << m_joints.size()<<endl;
}

void SkeletalModel::drawJoints( )
{
	// Draw a sphere at each joint. You will need to add a recursive helper function to traverse the joint hierarchy.
	//
	// We recommend using glutSolidSphere( 0.025f, 12, 12 )
	// to draw a sphere of reasonable size.
	//
	// You are *not* permitted to use the OpenGL matrix stack commands
	// (glPushMatrix, glPopMatrix, glMultMatrix).
	// You should use your MatrixStack class
	// and use glLoadMatrix() before your drawing call.

	//����Ҫclear��Ϊ��draw()�����Ѿ�clear()��

	
	drawJoints(m_rootJoint);
	
}

//�ݹ黭�Ǽ�
void SkeletalModel::drawJoints(Joint* joint)
{	
	m_matrixStack.push(joint->transform);	//�ȼ����Լ��ľ���
	for (int i = 0; i < joint->children.size(); i++) {
		Joint* child = joint->children[i];
		drawJoints(child);	//�ݹ黭�ؽ�
	}

	glLoadMatrixf(m_matrixStack.top());

	glutSolidSphere(0.025f, 12, 12);
	m_matrixStack.pop();
}

//������
void SkeletalModel::drawSkeleton(Joint* joint)
{	
	m_matrixStack.push(joint->transform);	//�ȼ����Լ��ľ���
	for (int i = 0; i < joint->children.size(); i++) {
		Joint* child = joint->children[i];
		drawSkeleton(child);	//�ݹ黭����
	}

	m_matrixStack.pop();	//Ϊʲô�����Ҫpop
	if (joint == m_rootJoint)
		return;

	Matrix4f m1 = Matrix4f::translation(0, 0, 0.5);		//����ƽ��

	Vector3f z = Vector3f(joint->transform.getCol(3)[0], joint->transform.getCol(3)[1], joint->transform.getCol(3)[2]);
	float L = z.abs();

	Matrix4f m2 = Matrix4f::scaling(0.025, 0.025, L);	//��������

	Vector3f rnd = Vector3f(0, 0, 1);

	z.normalize();
	Vector3f y = Vector3f::cross(z, rnd).normalized();
	Vector3f x = Vector3f::cross(y, z).normalized();

	Matrix4f m3 = Matrix4f(
		x[0], y[0], z[0], 0.0f,
		x[1], y[1], z[1], 0.0f,
		x[2], y[2], z[2], 0.0f,
		0.0f, 0.0f, 0.0f, 1.0f);	//������ת

	this->m_matrixStack.push(m3);
	this->m_matrixStack.push(m2);
	this->m_matrixStack.push(m1);

	glLoadMatrixf(m_matrixStack.top());
	glutSolidCube(1.0f);			//���л���

	this->m_matrixStack.pop();
	this->m_matrixStack.pop();
	this->m_matrixStack.pop();

}


void SkeletalModel::drawSkeleton( )
{
	// Draw boxes between the joints. You will need to add a recursive helper function to traverse the joint hierarchy.
	drawSkeleton(this->m_rootJoint);
}

void SkeletalModel::setJointTransform(int jointIndex, float rX, float rY, float rZ)
{
	// Set the rotation part of the joint's transformation matrix based on the passed in Euler angles.
	Matrix4f x = Matrix4f::rotateX(rX);
	Matrix4f y = Matrix4f::rotateY(rY);
	Matrix4f z = Matrix4f::rotateZ(rZ);
	Matrix4f m = x * y * z;
	
	m_joints[jointIndex]->transform.setSubmatrix3x3(0,0,m.getSubmatrix3x3(0,0));
}


void SkeletalModel::computeBindWorldToJointTransforms()
{
	// 2.3.1. Implement this method to compute a per-joint transform from
	//ʵ�ִ˷����Դ��м���ÿ���ؽڵı任
	// world-space to joint space in the BIND POSE.
	//�������е�����ռ䵽���Ͽռ䡣
	// Note that this needs to be computed only once since there is only
	// a single bind pose.
	//
	// This method should update each joint's bindWorldToJointTransform.
	// You will need to add a recursive helper function to traverse the joint hierarchy.
	//������Ҫ���һ���ݹ�������������������ϲ�νṹ��

	//�������ת�����ؽ�����ϵ
	cout << "computeBindWorldToJointTransforms���������" << endl;
	m_matrixStack.clear();
	computeBindWorldToJointTransforms(this->m_rootJoint);
}

//�����������תΪ�ֲ�����ת���ĵݹ麯��
void SkeletalModel:: computeBindWorldToJointTransforms(Joint* joint) {
	m_matrixStack.push(joint->transform);	//�ȼ����Լ��ľ���
	for (int i = 0; i < joint->children.size(); i++) {
		Joint* child = joint->children[i];
		computeBindWorldToJointTransforms(child);	//�ݹ黭�ؽ�
	}
	
	joint->bindWorldToJointTransform = m_matrixStack.top().inverse();
	m_matrixStack.pop();

}

void SkeletalModel::updateCurrentJointToWorldTransforms()
{
	// 2.3.2. Implement this method to compute a per-joint transform from joint space to world space in the CURRENT POSE.
	//
	// The current pose is defined by the rotations you've applied to the joints and 
	//hence needs to be *updated* every time the joint angles change.
	//
	// This method should update each joint's bindWorldToJointTransform.
	// You will need to add a recursive helper function to traverse the joint hierarchy.
	//������Ҫ���һ���ݹ�������������������ϲ�νṹ
	cout << "updateCurrentJointToWorldTransforms���������"<<endl;
	m_matrixStack.clear();
	updateCurrentJointToWorldTransforms(m_rootJoint);

}

void SkeletalModel::updateCurrentJointToWorldTransforms(Joint* joint)
{
	m_matrixStack.push(joint->transform);	//�ȼ����Լ��ľ���
	for (int i = 0; i < joint->children.size(); i++) {
		Joint* child = joint->children[i];
		updateCurrentJointToWorldTransforms(child);	//�ݹ�
	}
	
	joint->currentJointToWorldTransform = m_matrixStack.top();
	m_matrixStack.pop();
}

void SkeletalModel::updateMesh()
{
	// 2.3.2. This is the core of SSD.
	// Implement this method to update the vertices of the mesh
	// given the current state of the skeleton.
	//�����Ǽܵĵ�ǰ״̬
	// You will need both the bind pose world --> joint transforms.
	// and the current joint --> world transforms.

	for (int i = 1; i < m_mesh.bindVertices.size(); i++) {
		vector<float>wegihts = m_mesh.attachments[i];		//�õ�Ȩ�ؾ���
		Vector4f pi (m_mesh.bindVertices[i], 1);			//�õ��󶨵ĵ�
		Vector4f pij;
		for (int j = 0; j < wegihts.size(); j++) {
			if (wegihts[j] == 0)
				continue;
			/*pij = pij+
				wegihts[j] * 
				m_joints[j]->currentJointToWorldTransform * 
				m_joints[j]->bindWorldToJointTransform * pi;*/

			Vector4f temp = m_joints[j]->bindWorldToJointTransform * pi;
			temp = m_joints[j]->currentJointToWorldTransform * temp;
			pij = pij + wegihts[j] * temp;
		}

		m_mesh.currentVertices[i] = pij.xyz();
	}
}


