#ifndef PARTICLESYSTEM_H
#define PARTICLESYSTEM_H

#include <vector>
#include <vecmath.h>

using namespace std;

class ParticleSystem
{
public:

	ParticleSystem(int numParticles=0);

	int m_numParticles;
	
	// for a given state, evaluate derivative f(X,t)
	virtual vector<Vector3f> evalF(vector<Vector3f> state) = 0;
	
	// getter method for the system's state
	vector<Vector3f> getState(){ return m_vVecState; };
	
	// setter method for the system's state
	void setState(const vector<Vector3f>  & newState) { m_vVecState = newState; };
	
	void setWind(Vector3f wind) { windForce = wind; };

	void setDraw() { this->isDraw = !isDraw; };

	virtual void draw() = 0;
	
protected:

	Vector3f windForce;
	bool isDraw;
	vector<Vector3f> m_vVecState;
	
};

#endif
