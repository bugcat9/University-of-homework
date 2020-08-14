#include "Mesh.h"

using namespace std;

void Mesh::load( const char* filename )
{
	// 2.1.1. load() should populate bindVertices, currentVertices, and faces

	// Add your code here.
	//cout << filename;
	FILE* file = fopen(filename, "rb");
	char c;
	float x = 0., y = 0., z = 0.;	//�������
	int v1 = 0, v2 = 0, v3 = 0;		//����������±�
	while (!feof(file)) {
		fscanf(file, "%c ",&c);

		if (c == 'v') {
			fscanf(file, "%f %f %f", &x, &y, &z);
			this->bindVertices.push_back(Vector3f(x, y, z));
		}
		else if(c=='f')
		{
			fscanf(file, "%d %d %d", &v1, &v2, &v3);
			this->faces.push_back(Tuple3u(v1, v2, v3));
		}
		
		//cout << c << "	" << x << "	" << y << "	" << z << endl;
	}

	fclose(file);
	
	cout << "�������Ϊ��" << bindVertices.size()<<endl;
	// make a copy of the bind vertices as the current vertices
	currentVertices = bindVertices;
}

void Mesh::draw()
{
	// Since these meshes don't have normals
	// be sure to generate a normal per triangle.
	// Notice that since we have per-triangle normals
	// rather than the analytical normals from
	// assignment 1, the appearance is "faceted".

	glBegin(GL_TRIANGLES);
	for (int i = 0; i < faces.size(); i++){
		
		//�õ����������
		Vector3f v1 = currentVertices[faces[i][0]-1];
		Vector3f v2 = currentVertices[faces[i][1]-1];
		Vector3f v3 = currentVertices[faces[i][2]-1];
		//���㷨����

		Vector3f Normal = Vector3f::cross(v1 - v2, v1 - v3).normalized();

		//���÷�����
		glNormal3f(Normal.x(), Normal.y(), Normal.z());	

		glVertex3f(v1.x(), v1.y(), v1.z());
		glVertex3f(v2.x(), v2.y(), v2.z());
		glVertex3f(v3.x(), v3.y(), v3.z());
		
	}
	glEnd();
	glFlush();

	//cout << "�����������ͼ����"<<endl;
}

void Mesh::loadAttachments( const char* filename, int numJoints )
{
	// 2.2. Implement this method to load the per-vertex attachment weights
	// this method should update m_mesh.attachments

	cout << filename<<endl;
	cout << numJoints<<endl;

	ifstream in(filename, ios::in);

	float weight=0;
	while (attachments.size()<=bindVertices.size()) {
		vector< float > a;		//�洢Ȩ������
		a.push_back(0);			//���ؽ�Ȩ��Ϊ0
		
		for (int i = 0; i < numJoints-1;++i) {
			in >> weight;
			a.push_back(weight);
		}
		this->attachments.push_back(a);
		
	}
	cout << "�ⲿ�����Ĵ�С��" << attachments.size() << endl;
	in.close();
}
