#include <assert.h>
#include <stdint.h>
#include <stdio.h>
#include <string.h>
#include <sys/time.h>

#include "sudoku.h"

int64_t now()
{
  struct timeval tv;
  gettimeofday(&tv, NULL);
  return tv.tv_sec * 1000000 + tv.tv_usec;
}

int main(int argc, char* argv[])
{
  init_neighbors();
  //输入文件名
  char str[20];
  scanf("%s", str);
  int len1 = strlen(str);
  char* t;
  if(len1 == 10)
  	{t ="test_group";}
  
  FILE* fp = fopen(t, "r");
  char filename[128];
  char* f;
  char puzzle[128];
  int total_solved = 0;
  int total = 0;
  
  bool (*solve)(int) = solve_sudoku_dancing_links;
  
  int64_t start = now();
  
  while (fgets(filename, sizeof filename, fp) != NULL) {

  int len = strlen(filename);
  if(len == 8)
  	f = "test1";
  else if(len == 11)
  	f = "test1000";
  else
  	f = "test10000";
  FILE* fp2 = fopen(f, "r");
  total_solved = 0;
  total = 0;
  
  while (fgets(puzzle, sizeof puzzle, fp2) != NULL) {
     if (strlen(puzzle) >= N) {
      ++total;
      input(puzzle);
      init_cache();

      if (solve(0)) {
        ++total_solved;
        if (!solved())
          assert(0);
      }
      else {
        printf("No: %s", puzzle);
      }
    }
    
    for(int i = 0 ; i<81 ; i++)
    	printf("%d",board[i]);
    printf("\n");
    
  }
  /*
  //时间
  int64_t end = now();
  double sec = (end-start)/1000000.0;
  printf("%f sec %f ms each %d\n", sec, 1000*sec/total, total_solved);
  */
  }
  
  return 0;
}

