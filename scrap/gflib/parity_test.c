/*
Procedures and Programs for Galois-Field Arithmetic and Reed-Solomon Coding.  
Copyright (C) 2003 James S. Plank

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

---------------------------------------------------------------------------
Please see http://www.cs.utk.edu/~plank/plank/gflib
for instruction on how to use this library.

Jim Plank
plank@cs.utk.edu
http://www.cs.utk.edu/~plank

Associate Professor
Department of Computer Science
University of Tennessee
203 Claxton Complex
1122 Volunteer Blvd.
Knoxville, TN 37996-3450

     865-974-4397
Fax: 865-974-4404

$Revision: 1.1 $
*/


#include <stdio.h>
#include "gflib.h"
#include <sys/time.h>


main(int argc, char **argv)
{
  char *y;
  char *x;
  char *z;
  int i, size, iterations, check_end;
  struct timeval tv1, tv2;
  struct timezone tz1, tz2;

  if (argc != 4) {
    fprintf(stderr, "usage: parity_test buffer-size iterations check-at-end(Y/N)\n");
    exit(1);
  }

  size = atoi(argv[1]);
  iterations = atoi(argv[2]);
  check_end = (argv[3][0] == 'Y' || argv[3][0] == 'y');

  srand48();

  y = (char *) malloc(size);
  x = (char *) malloc(size);
  z = (char *) malloc(size);

  for (i = 0; i < size; i++) {
    y[i] = lrand48()%256;
    x[i] = lrand48()%256;
    z[i] = y[i];
  }
  
  gettimeofday(&tv1, &tz1);
  for (i = 0; i < iterations; i++) {
    gf_add_parity(x, y, size);
  }
  gettimeofday(&tv2, &tz2);

  tv2.tv_sec -= tv1.tv_sec;
  tv2.tv_usec -= tv1.tv_usec;
  if (tv2.tv_usec < 0) {
    tv2.tv_sec--;
    tv2.tv_usec += 1000000;
  }
  printf("%6d.%06d\n", tv2.tv_sec, tv2.tv_usec);
  if (check_end) {
    for (i = 0; i < size; i++) {
      if (z[i] != y[i]) printf("Error in byte %d\n", i);
    }
  }

}
  
