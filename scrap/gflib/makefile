# Procedures and Programs for Galois-Field Arithmetic and Reed-Solomon Coding.  
# Copyright (C) 2003 James S. Plank
# 
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
# 
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
# 
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
# 
# ---------------------------------------------------------------------------
# Please see http://www.cs.utk.edu/~plank/plank/gflib
# for instruction on how to use this library.
# 
# Jim Plank
# plank@cs.utk.edu
# http://www.cs.utk.edu/~plank
# 
# Associate Professor
# Department of Computer Science
# University of Tennessee
# 203 Claxton Complex
# 1122 Volunteer Blvd.
# Knoxville, TN 37996-3450
# 
#      865-974-4397
# Fax: 865-974-4404
# 
# $Revision: 1.1 $

CC = gcc 
CFLAGS = -O

ALL =	gf_mult gf_div parity_test \
        xor rs_encode_file rs_decode_file

all:
	@ echo "use one of the following targets: w8, w16"

w8:
	make "CFLAGS=$(CFLAGS) -DW_8 -DTABLE" $(ALL)

w16:
	make "CFLAGS=$(CFLAGS) -DW_16 -DTABLE" $(ALL)

# w32:
# 	make "CFLAGS=$(CFLAGS) -DW_32 -DXOR_N_SHIFT" gfm gfd

# +mkmake+ -- Everything after this line is automatically generated


clean:
	rm -f core *.o $(ALL) a.out

.SUFFIXES: .c .o
.c.o:
	$(CC) $(CFLAGS) -c $*.c


gflib.o: gflib.h

parity_test.o: gflib.h gflib.o
parity_test: parity_test.o gflib.o
	$(CC) $(CFLAGS) -o parity_test parity_test.o gflib.o

create_rs_matrix.o: gflib.h gflib.o
create_rs_matrix: create_rs_matrix.o gflib.o
	$(CC) $(CFLAGS) -o create_rs_matrix create_rs_matrix.o gflib.o

gf_mult_test.o: gflib.h gflib.o
gf_mult_test: gf_mult_test.o gflib.o
	$(CC) $(CFLAGS) -o gf_mult_test gf_mult_test.o gflib.o

gf_mult.o: gflib.h gflib.o
gf_mult: gf_mult.o gflib.o
	$(CC) $(CFLAGS) -o gf_mult gf_mult.o gflib.o


rs_encode_file.o: gflib.h gflib.o
rs_encode_file: rs_encode_file.o gflib.o
	$(CC) $(CFLAGS) -o rs_encode_file rs_encode_file.o gflib.o

rs_decode_file.o: gflib.h gflib.o
rs_decode_file: rs_decode_file.o gflib.o
	$(CC) $(CFLAGS) -o rs_decode_file rs_decode_file.o gflib.o

gf_div.o: gflib.h gflib.o
gf_div: gf_div.o gflib.o
	$(CC) $(CFLAGS) -o gf_div gf_div.o gflib.o

xor: xor.o gflib.o
	$(CC) $(CFLAGS) -o xor xor.o

