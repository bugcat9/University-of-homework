#include "ClothSystem.h"

//TODO: Initialize here
ClothSystem::ClothSystem(int num, vector<particle> particles):PendulumSystem(num* num, particles)
{
	this->m_num = num;
	this->isDraw = true;
}


// TODO: implement evalF
// for a given state, evaluate f(X,t)
vector<Vector3f> ClothSystem::evalF(vector<Vector3f> state)
{	
	vector<Vector3f> f = PendulumSystem::evalF(state);
	//for (int i = m_num-3; i < this->m_numParticles; i+= m_num) {
	//	if (!m_particles[i].isStill)
	//		f[2 * i + 1] = f[2 * i + 1] + windForce / m_particles[i].m_mass;
	//}

	for (int i = 0; i < this->m_numParticles; i++) {
		if (!m_particles[i].isStill)
			f[2 * i + 1] = f[2 * i + 1] + windForce / m_particles[i].m_mass;
	}

	return f;
	//return state;
}

///TODO: render the system (ie draw the particles)
void ClothSystem::draw()
{	
	////先画出点
	//for (int i = 0; i < m_numParticles; i++) {
	//	Vector3f pos = this->m_vVecState[2 * i];
	//	glPushMatrix();
	//	glTranslatef(pos[0], pos[1], pos[2]);
	//	glutSolidSphere(0.075f, 10.0f, 10.0f);
	//	glPopMatrix();
	//}
	glPushMatrix();
	GLfloat sphereColor[] = { 1.0f, 1.0f, 0.0f, 1.0f };
	GLfloat Color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

	glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, sphereColor);
	glutSolidSphere(1.0f, 50.0f, 50.0f);
	glPopMatrix();

	if (isDraw) {
		//画出光滑的曲面
		glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, Color);
		glBegin(GL_TRIANGLES);
		for (int i = 0; i < m_num - 1; i++) {

			for (int j = 0; j < m_num - 1; j++) {

				Vector3f v1 = this->m_vVecState[2 * (i * m_num + j)];
				Vector3f v2 = this->m_vVecState[2 * (i * m_num + 1 + j)];
				Vector3f v3 = this->m_vVecState[2 * ((i + 1) * m_num + j)];
				Vector3f v4 = this->m_vVecState[2 * ((i + 1) * m_num + 1 + j)];

				if (v1.abs() - 1.0f <= 0.1) {
					m_particles[(i * m_num + j)].isStill = true;	//粒子静止
					m_vVecState[2 * (i * m_num + j)+1] = 0;	//速度为0
				}

				if (v2.abs() - 1.0f <= 0.1) {
					m_particles[ (i * m_num + 1 + j)].isStill = true;
					m_vVecState[2 * (i * m_num + 1 + j)+1] = 0;
				}

				if (v3.abs() - 1.0f <= 0.1) {
					m_particles[ ((i + 1) * m_num + j)].isStill = true;
					m_vVecState[2 * ((i + 1) * m_num + j)+1] = 0;
				}

				if (v4.abs() - 1.0f <= 0.1) {
					m_particles[((i + 1) * m_num + 1 + j)].isStill = true;
					m_vVecState[2 * ((i + 1) * m_num + 1 + j)+1] = 0;
				}
			


				Vector3f n1 = Vector3f::cross(v1 - v2, v1 - v3);
				Vector3f n2 = Vector3f::cross(v4 - v3, v3 - v2);
				//画两个三角形
				glNormal3f(n1.x(), n1.y(), n1.z());

				glVertex3f(v1.x(), v1.y(), v1.z());
				glVertex3f(v2.x(), v2.y(), v2.z());
				glVertex3f(v3.x(), v3.y(), v3.z());

				glNormal3f(n2.x(), n2.y(), n2.z());
				glVertex3f(v4.x(), v4.y(), v4.z());
				glVertex3f(v3.x(), v3.y(), v3.z());
				glVertex3f(v2.x(), v2.y(), v2.z());


			}
		}
		glEnd();

	}
	else {
		//画出线
		glEnable(GL_LINE_SMOOTH);
		glBegin(GL_LINES);
		for (int i = 0; i < this->m_particles.size(); i++) {

			for (int j = 0; j < 4; j++) {
				if (m_particles[i].r[0] != m_particles[i].r[j])
					continue;
				int index = m_particles[i].m_link_particles[j];
				Vector3f x = m_vVecState[2 * i];

				//认为在表面了
				
				Vector3f y = m_vVecState[2 * index];

				if (x.abs() - 1.0f <= 0.1) {
					m_particles[i].isStill = true;//粒子静止
					m_vVecState[2 * i + 1] = 0;//速度为0
				}



				if (y.abs() - 1.0f <= 0.1) {
					m_particles[index].isStill = true;
					m_vVecState[2 * index + 1] = 0;
				}

				glVertex3f(x.x(), x.y(), x.z());
				glVertex3f(y.x(), y.y(), y.z());
			}

		}
		glEnd();
	}
	

	

	
}

