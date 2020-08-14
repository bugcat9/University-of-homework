#include "MatrixStack.h"

MatrixStack::MatrixStack()
{
	// Initialize the matrix stack with the identity matrix.
	Matrix4f m = Matrix4f::identity();
	this->m_matrices.push_back(m);
}

void MatrixStack::clear()
{
	// Revert to just containing the identity matrix.
	this->m_matrices.clear();
	Matrix4f m = Matrix4f::identity();
	this->m_matrices.push_back(m);
}

Matrix4f MatrixStack::top()
{
	// Return the top of the stack

	return this->m_matrices.back();
}

void MatrixStack::push( const Matrix4f& m )
{
	// Push m onto the stack.
	// Your stack should have OpenGL semantics:
	// the new top should be the old top multiplied by m

	this->m_matrices.push_back(this->top()*m);
}

void MatrixStack::pop()
{
	// Remove the top element from the stack
	this->m_matrices.pop_back();
}
