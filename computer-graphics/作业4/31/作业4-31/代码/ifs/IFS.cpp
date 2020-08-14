#include"IFS.h"

void IFS::ReadDescription(char* input_file)
{	
	FILE* f = fopen(input_file, "r");

	if (f == 0)
	{
		printf("can't open input_file");
		return;
	}

	fscanf(f, "%d", &n);	//读入变换的数量
	m_transformMatrixs = new Matrix[n];
	m_probability = new float[n];

	float p = 0.0;
	//循环读取文件
	for (int i =0; i < n; ++i) {
		fscanf(f, "%f", &m_probability[i]);	//读入概率
		cout << m_probability[i] << endl;
		Matrix m;
		m.Read3x3(f);	//读入
		m.Write();
		m_transformMatrixs[i] = m;
	} 
}

void IFS::Render(Image* image, int numOfPoints, int numOfiters)
{
	int width = image->Width();

	int height = image->Height(); 
	assert(height > 0 && width > 0);	//需要判断宽和高大于0

	//设置颜色
	Vec3f black(0.0f, 0.0f, 0.0f), white(255.0f, 255.0f, 255.0f);
	image->SetAllPixels(white);//先将底色设置成白色


	vector<Vec2f> points;	//随机点
	for (int i = 0; i < numOfPoints; ++i) {
		//srand((unsigned)time(NULL));
		float x = rand() % width;
		float y = rand() % height;
		points.push_back(Vec2f(x, y));
	}

	

	//进行核心的渲染算法
	//将迭代次数写在外面可以减少迭代为0的时候的重复
	for ( int times = 0; times < numOfiters; ++times)
	{	
		//对点进行迭代
		for ( int j = 0; j < numOfPoints; ++j)
		{	
			int index = 0;
			//进行概率选择
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
			transMatrix.Transform(t);//有直接对vec2f进行转化的函数所以不需要三个点
			points[j].Set(t.x() * width, t.y() * height);
		}
	}
	

	//对点进行设置
	for (int i = 0; i < numOfPoints; ++i) {
		if(!(points[i].x() >=0 && points[i].x() < width))
			continue;
		if(!(points[i].y() >=0 && points[i].y() < height))
			continue;
		image->SetPixel(points[i].x(), points[i].y(), black);
	}

}
