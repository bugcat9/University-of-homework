
#include "simpleSystem.h"

using namespace std;

SimpleSystem::SimpleSystem()
{	
	Vector3f v(1,0,0);
	this->m_vVecState.push_back(v);
}

// TODO: implement evalF
// for a given state, evaluate f(X,t)
vector<Vector3f> SimpleSystem::evalF(vector<Vector3f> state)
{
	vector<Vector3f> f;
	// YOUR CODE HERE
	for (int i = 0; i < state.size(); i++) {
		Vector3f v(-state[i].y(), state[i].x(), 0.);
		f.push_back(v);
	}
	return f;
}

// render the system (ie draw the particles)
void SimpleSystem::draw()
{		
		//
	//printf("%s", "这里被调用");
	 
	
	for (int i = 0; i < this->m_vVecState.size(); i++) {
		Vector3f pos = this->m_vVecState[i];	//YOUR PARTICLE POSITION
		glPushMatrix();
		glTranslatef(pos[0], pos[1], pos[2]);
		glutSolidSphere(0.075f, 10.0f, 10.0f);
		glPopMatrix();
		//pos.print();
	}
	
}
