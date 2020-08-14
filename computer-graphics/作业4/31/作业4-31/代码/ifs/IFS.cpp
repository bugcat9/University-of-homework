#include"IFS.h"

void IFS::ReadDescription(char* input_file)
{	
	FILE* f = fopen(input_file, "r");

	if (f == 0)
	{
		printf("can't open input_file");
		return;
	}

	fscanf(f, "%d", &n);	//����任������
	m_transformMatrixs = new Matrix[n];
	m_probability = new float[n];

	float p = 0.0;
	//ѭ����ȡ�ļ�
	for (int i =0; i < n; ++i) {
		fscanf(f, "%f", &m_probability[i]);	//�������
		cout << m_probability[i] << endl;
		Matrix m;
		m.Read3x3(f);	//����
		m.Write();
		m_transformMatrixs[i] = m;
	} 
}

void IFS::Render(Image* image, int numOfPoints, int numOfiters)
{
	int width = image->Width();

	int height = image->Height(); 
	assert(height > 0 && width > 0);	//��Ҫ�жϿ�͸ߴ���0

	//������ɫ
	Vec3f black(0.0f, 0.0f, 0.0f), white(255.0f, 255.0f, 255.0f);
	image->SetAllPixels(white);//�Ƚ���ɫ���óɰ�ɫ


	vector<Vec2f> points;	//�����
	for (int i = 0; i < numOfPoints; ++i) {
		//srand((unsigned)time(NULL));
		float x = rand() % width;
		float y = rand() % height;
		points.push_back(Vec2f(x, y));
	}

	

	//���к��ĵ���Ⱦ�㷨
	//����������д��������Լ��ٵ���Ϊ0��ʱ����ظ�
	for ( int times = 0; times < numOfiters; ++times)
	{	
		//�Ե���е���
		for ( int j = 0; j < numOfPoints; ++j)
		{	
			int index = 0;
			//���и���ѡ��
			double ran = (double)(rand()) / (double)(RAND_MAX);
			double	p = m_probability[0];
			
			while (p < ran && index<n) {
				/*if (index >= n-1) {
					cout << m_probability[0] << endl;
					cout << p<<endl;
					cout << ran << endl;
				}*/
				assert(index < n);
				p += m_probability[++index];
			}


			Matrix transMatrix = m_transformMatrixs[index];
			Vec2f t(points[j].x() / (float)(width), points[j].y() / (float)(height));
			transMatrix.Transform(t);//��ֱ�Ӷ�vec2f����ת���ĺ������Բ���Ҫ������
			points[j].Set(t.x() * width, t.y() * height);
		}
	}
	

	//�Ե��������
	for (int i = 0; i < numOfPoints; ++i) {
		if(!(points[i].x() >=0 && points[i].x() < width))
			continue;
		if(!(points[i].y() >=0 && points[i].y() < height))
			continue;
		image->SetPixel(points[i].x(), points[i].y(), black);
	}

}
