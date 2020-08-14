#include "TimeStepper.hpp"

///TODO: implement Explicit Euler time integrator here
void ForwardEuler::takeStep(ParticleSystem* particleSystem, float stepSize)
{
	/*printf("%s", "这里被调用");*/
	//得到状态
	vector<Vector3f> state = particleSystem->getState();
	vector<Vector3f> f = particleSystem->evalF(state);
	for (int i = 0; i < state.size(); i++)
	{	
		//X(t + h)=X + hf(X,t) 
		state[i] = state[i] + stepSize * f[i];
	}
	particleSystem->setState(state);
}

///TODO: implement Trapzoidal rule here
void Trapzoidal::takeStep(ParticleSystem* particleSystem, float stepSize)
{

	//得到状态
	vector<Vector3f> state = particleSystem->getState();
	vector<Vector3f> state1 = particleSystem->getState();
	vector<Vector3f> f0 = particleSystem->evalF(state);
	for (int i = 0; i < state.size(); i++)
	{
		//X(t + h)=X + hf(X,t) 
		state1[i] = state1[i] + stepSize * f0[i];
	}
	vector<Vector3f> f1 = particleSystem->evalF(state1);

	for (int i = 0; i < state.size(); i++)
	{
		//X(t + h)=X + hf(X,t) 
		state[i] = state[i] + stepSize * (f0[i]+f1[i])/2;
	}

	particleSystem->setState(state);
}
