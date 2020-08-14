#include "Mesh.h"

using namespace std;

void Mesh::load( const char* filename )
{
	// 2.1.1. load() should populate bindVertices, currentVertices, and faces

	// Add your code here.
	//cout << filename;
	FILE* file = fopen(filename, "rb");
	char c;
	float x = 0., y = 0., z = 0.;	//点的坐标
	int v1 = 0, v2 = 0, v3 = 0;		//面三个点的下标
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
	
	cout << "结点数量为：" << bindVertices.size()<<endl;
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
		
		//得到点的三个面
		Vector3f v1 = currentVertices[faces[i][0]-1];
		Vector3f v2 = currentVertices[faces[i][1]-1];
		Vector3f v3 = currentVertices[faces[i][2]-1];
		//计算法向量

		Vector3f Normal = Vector3f::cross(v1 - v2, v1 - v3).normalized();

		//设置法向量
		glNormal3f(Normal.x(), Normal.y(), Normal.z());	

		glVertex3f(v1.x(), v1.y(), v1.z());
		glVertex3f(v2.x(), v2.y(), v2.z());
		glVertex3f(v3.x(), v3.y(), v3.z());
		
	}
	glEnd();
	glFlush();

	//cout << "调用了这个画图函数"<<endl;
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
		vector< float > a;		//存储权重数组
		a.push_back(0);			//根关节权重为0
		
		for (int i = 0; i < numJoints-1;++i) {
			in >> weight;
			a.push_back(weight);
		}
		this->attachments.push_back(a);
		
	}
	cout << "外部向量的大小：" << attachments.size() << endl;
	in.close();
}
