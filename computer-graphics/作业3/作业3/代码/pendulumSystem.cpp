
#include "pendulumSystem.h"

PendulumSystem::PendulumSystem(int numParticles,vector<particle> particles):ParticleSystem(numParticles)
{
	m_numParticles = numParticles;
	this->m_particles = particles;
	// fill in code for initializing the state based on the number of particles
	for (int i = 0; i < m_numParticles; i++) {
		
		// for this system, we care about the position and the velocity
		this->m_vVecState.push_back(Vector3f());
		this->m_vVecState.push_back(Vector3f(0, 0, 0));
	}
}


// TODO: implement evalF
// for a given state, evaluate f(X,t)
vector<Vector3f> PendulumSystem::evalF(vector<Vector3f> state)
{
	vector<Vector3f> f;

	// YOUR CODE HERE
	for (int i = 0; i < this->m_numParticles; i++) {
		f.push_back(state[2*i+1]);	//存入速度
		Vector3f totalForce;	//合力

		if (!m_particles[i].isStill) {
			float g = this->m_particles[i].m_mass * 10;	//得出重力
			Vector3f gravity(0, -g, 0);	//重力的向量

			Vector3f t;
			//得到弹力向量
			for (int j = 0; j < m_particles[i].m_link_particles.size(); j++) {
				int index = m_particles[i].m_link_particles[j];

				Vector3f d = this->m_vVecState[2 * i] - m_vVecState[2 * index];	//先假设弹簧另外一边
				//printf("d的值");
				//d.print();
				/*if (d.abs() == 0)
					continue;*/
				//这个公式非常的精妙，并且d不能为0
				t += -m_particles[i].m_Elastic_coefficient[j] * (d.abs() - m_particles[i].r[j]) * (d / d.abs());

			}

			//设置摩檫力
			Vector3f friction = -m_particles[i].m_Capacitance_coefficient * m_vVecState[2 * i + 1];
			/*Vector3f friction ;*/

			//得到合力(重力+弹力+摩檫力)
 			totalForce = gravity + t + friction;
			//printf("%d %s",i,"总力量");
			//totalForce.print();
		}
	
		f.push_back(totalForce / m_particles[i].m_mass);	//存储加速度
		
	}

	return f;
}

// render the system (ie draw the particles)
void PendulumSystem::draw()
{
	for (int i = 0; i < m_numParticles; i++) {
		Vector3f pos = this->m_vVecState[2 * i] ;//  position of particle i. YOUR CODE HERE
		glPushMatrix();
		glTranslatef(pos[0], pos[1], pos[2] );
		glutSolidSphere(0.075f,10.0f,10.0f);
		glPopMatrix();
	}
}


