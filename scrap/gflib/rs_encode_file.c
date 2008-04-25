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
#include <sys/types.h>
#include <sys/stat.h>

/* This one is going to be in-core */

main(int argc, char **argv)
{
  int i, j, *vdm, *inv, *prod, cache_size;
  int rows, cols, blocksize, orig_size;
  int n, m, sz, *factors, tmp, factor;
  char *stem, *filename; 
  char **buffer, *buf_file, *block;
  struct stat buf;
  FILE *f;

  if (argc != 5) {
    fprintf(stderr, "usage: rs_encode_file filename n m stem\n");
    exit(1);
  }
  
  n = atoi(argv[2]);
  m = atoi(argv[3]);
  stem = argv[4];
  filename = argv[1];

  rows = n+m;
  cols = n;

  if (stat(filename, &buf) != 0) {
    perror(filename);
    exit(1);
  }

  sz = buf.st_size;
  orig_size = buf.st_size;
  if (sz % (n*sizeof(unit)) != 0) {
    sz += (n*sizeof(unit) - (sz % (n*sizeof(unit))));
  }
  blocksize = sz/n;

  buffer = (char **) malloc(sizeof(char *)*n);
  for (i = 0; i < n; i++) {
    buffer[i] = (char *) malloc(blocksize);
    if (buffer[i] == NULL) {
      perror("Allocating buffer to store the whole file");
      exit(1);
    }
  }

  f = fopen(filename, "r");
  if (f == NULL) { perror(filename); }
  cache_size = orig_size;

  for (i = 0; i < n; i++) {
    if (cache_size < blocksize) memset(buffer[i], 0, blocksize);
    if (cache_size > 0) {
      if (fread(buffer[i], 1, (cache_size > blocksize) ? blocksize : cache_size, f) <= 0) {
        fprintf(stderr, "Couldn't read the right bytes into the buffer\n");
        exit(1);
      }
    }
    cache_size -= blocksize;
  }
  fclose(f);

  buf_file = (char *) malloc(sizeof(char)*(strlen(stem)+30));
  if (buf_file == NULL) { perror("malloc - buf_file"); exit(1); }
  block = (char *) malloc(sizeof(char)*blocksize);
  if (block == NULL) { perror("malloc - block"); exit(1); }
  for (i = 0; i < n; i++) {
    sprintf(buf_file, "%s-%04d.rs", stem, i);
    printf("Writing %s ...", buf_file); fflush(stdout);
    f = fopen(buf_file, "w");
    if (f == NULL) { perror(buf_file); exit(1); }
    fwrite(buffer[i], 1, blocksize, f);
    fclose(f);
    printf(" Done\n");
  }

  factors = (int *) malloc(sizeof(int)*n);
  if (factors == NULL) { perror("malloc - factors"); exit(1); }

  for (i = 0; i < n; i++) factors[i] = 1;
  
  vdm = gf_make_dispersal_matrix(rows, cols);

  for (i = cols; i < rows; i++) {
    sprintf(buf_file, "%s-%04d.rs", stem, i);
    printf("Calculating  %s ...", buf_file); fflush(stdout);
    memset(block, 0, blocksize); 
    for (j = 0; j < cols; j++) {
      tmp = vdm[i*cols+j]; 
      if (tmp != 0) {
        factor = gf_single_divide(tmp, factors[j]);
/*        printf("M[%02d,%02d] = %3d.  Factors[%02d] = %3d.  Factor = %3d.\n",
                i, j, tmp, j, factors[j], factor); */
        factors[j] = tmp;
/*        printf("     Block %2d Bef: %3d.  ", j, buffer[j][0]); */
        gf_mult_region(buffer[j], blocksize, factor);
/*        printf("Block %2d Aft: %3d.  ", j, buffer[j][0]); */
/*         printf("Block %2d Bef: %3d.  ", i, block[0]); */
        gf_add_parity(buffer[j], block, blocksize);
       /*  printf("Block %2d Aft: %3d.\n", i, block[0]); */
      }
    }
    printf(" writing  ...", buf_file); fflush(stdout);
    f = fopen(buf_file, "w");
    if (f == NULL) { perror(buf_file); exit(1); }
    fwrite(block, 1, blocksize, f);
    printf(" Done\n");
    fclose(f);
  }

  sprintf(buf_file, "%s-info.txt", stem, i);
  f = fopen(buf_file, "w");
  if (f == NULL) { perror(buf_file); exit(1); }
  fprintf(f, "%d\n", orig_size);
  fprintf(f, "%d\n", sz);
  fprintf(f, "%d\n", blocksize);
  fprintf(f, "%d\n", n);
  fprintf(f, "%d\n", m);
  gf_write_matrix(f, vdm, rows, cols);
}
