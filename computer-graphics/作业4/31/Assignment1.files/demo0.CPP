//
// originally implemented by Justin Legakis
//
#include "matrix.h"
#include "vectors.h"

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <string.h>


int main( int argc, const char** argv )
{
char *input_file = NULL;
int width = 100;
int height = 100;
char *output_file = NULL;
float depth_min = 0;
float depth_max = 1;
char *depth_file = NULL;

// sample command line:
// raytracer -input scene1_1.txt -size 200 200 -output output1_1.tga -depth 9 10 depth1_1.tga

for (int i = 1; i < argc; i++) {
  if (!strcmp(argv[i],"-input")) {
    i++; assert (i < argc); 
    input_file = argv[i];
  } else if (!strcmp(argv[i],"-size")) {
    i++; assert (i < argc); 
    width = atoi(argv[i]);
    i++; assert (i < argc); 
    height = atoi(argv[i]);
  } else if (!strcmp(argv[i],"-output")) {
    i++; assert (i < argc); 
    output_file = argv[i];
  } else if (!strcmp(argv[i],"-depth")) {
    i++; assert (i < argc); 
    depth_min = atof(argv[i]);
    i++; assert (i < argc); 
    depth_max = atof(argv[i]);
    i++; assert (i < argc); 
    depth_file = argv[i];
  } else {
    printf ("whoops error with command line argument %d: '%s'\n",i,argv[i]);
    assert(0);
  }

}
    FILE* f = fopen( input_file, "r" );
    if ( f == 0 )
    {
        printf( "can't open input_file);
        return false;
    }
    
    // Read each line.
    fscanf(f,"%d",&n);
}