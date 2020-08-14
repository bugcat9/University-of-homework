#ifndef PENDULUMSYSTEM_H
#define PENDULUMSYSTEM_H

#include <vecmath.h>
#include <vector>
#ifdef _WIN32
#include "GL/freeglut.h"
#else
#include <GL/glut.h>
#endif

#include "particleSystem.h"
#include"Particle.h"
class PendulumSystem: public ParticleSystem
{
public:
	PendulumSystem(int numParticles , vector<particle> particles);	//对构造函数需要进行修改
	
	vector<Vector3f> evalF(vector<Vector3f> state);
	

	void draw();

protected:
	vector<particle> m_particles;
	
};

#endif
