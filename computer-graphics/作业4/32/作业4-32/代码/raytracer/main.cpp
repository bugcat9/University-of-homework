//
// originally implemented by Justin Legakis
//
#include "vectors.h"
#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <string.h>
#include"scene_parser.h"
#include"image.h"
#include"ray.h"
#include "camera.h"
#include"hit.h"
#include"material.h"
#include"group.h"
int main(int argc, char** argv)
{
	char* input_file = NULL;
	int width = 100;
	int height = 100;
	char* output_file = NULL;
	float depth_min = 0;
	float depth_max = 1;
	char* depth_file = NULL;

	// sample command line:
	// raytracer -input scene1_1.txt -size 200 200 -output output1_1.tga -depth 9 10 depth1_1.tga

	for (int i = 1; i < argc; i++) {
		if (!strcmp(argv[i], "-input")) {
			i++; assert(i < argc);
			input_file = argv[i];
		}
		else if (!strcmp(argv[i], "-size")) {
			i++; assert(i < argc);
			width = atoi(argv[i]);
			 i++; assert (i < argc); 
			height = atoi(argv[i]);
		}
		else if (!strcmp(argv[i], "-output")) {
			i++; assert(i < argc);
			output_file = argv[i];
		}
		else if (!strcmp(argv[i], "-depth")) {
			i++; assert(i < argc);
			depth_min = atof(argv[i]);
			i++; assert(i < argc);
			depth_max = atof(argv[i]);
			i++; assert(i < argc);
			depth_file = argv[i];
		}
		else {
			printf("whoops error with command line argument %d: '%s'\n", i, argv[i]);
			assert(0);
		}
	}
	
	SceneParser sceneParser(input_file);
	Image* image = new Image(width,height);	//普通照片
	Image* depthImage = new Image(width, height);	//深度照片

	Group * group = sceneParser.getGroup();
	Vec3f background_color = sceneParser.getBackgroundColor();
	Camera* camera =sceneParser.getCamera();

	Vec3f black(0, 0, 0);
	Vec3f white(1.0, 1.0, 1.0);

	image->SetAllPixels(background_color);
	depthImage->SetAllPixels(black);

	float x = 0, y = 0;
	
	for (int i = 0; i < width; ++i,x+=1.0/ width) {
		y = 0;
		for (int j = 0; j < height; ++j,y+=1.0/ height) {
		
			//生成光线和碰撞
			Ray	ray = camera->generateRay(Vec2f(x,y));
			Hit hit(FLT_MAX,NULL);

			if (group->intersect(ray,hit,camera->getTMin())) {
				Material* m = hit.getMaterial();
				image->SetPixel(i, j,m->getDiffuseColor());
				float t = hit.getT();

				//进行深度图显示显示
				if (depth_min <=t && t<=depth_max) {
					Vec3f  color = (depth_max-t) / (depth_max - depth_min) * white;//采用网上方法进行深度设置

					depthImage->SetPixel(i, j, color);
				}
			}
		}
	}
	image->SaveTGA(output_file);
	depthImage->SaveTGA(depth_file);
}