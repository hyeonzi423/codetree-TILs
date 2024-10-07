import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

   public static void main(String[] args) throws IOException {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

      StringTokenizer st = new StringTokenizer(br.readLine());
      int N = Integer.parseInt(st.nextToken());
      int L = Integer.parseInt(st.nextToken());
      int R = Integer.parseInt(st.nextToken());

      int ans = 0;
      boolean flag = true;

      int[][] map = new int[N][N];
      for (int i = 0; i < N; i++) {
         st = new StringTokenizer(br.readLine());
         for (int j = 0; j < N; j++) {
            map[i][j] = Integer.parseInt(st.nextToken());
         }
      }

      int[] dx = { -1, 0, 1, 0 };
      int[] dy = { 0, 1, 0, -1 };
      boolean[][] visited = new boolean[N][N];

      while (true) {
         if (!flag) {
            System.out.println(ans-1);
            break;
         }
         if(flag) {
        	 ans++;
         }
         flag = false;
         for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
               if (!visited[i][j]) {
                  for (int k = 0; k < 4; k++) {
                     int nx = i + dx[k];
                     int ny = j + dy[k];
                     if (0 <= nx && nx < N && 0 <= ny && ny < N && !visited[nx][ny]) {
                        if (L <= Math.abs(map[i][j] - map[nx][ny]) && Math.abs(map[i][j] - map[nx][ny]) < R) {
                           Queue<int[]> q = new LinkedList<>();
                           flag = true;
                           q.add(new int[] { i, j });
                           int cnt = 1;
                           int total = map[i][j];
                           visited[i][j] = true;
                           ArrayList<int[]> list = new ArrayList<>();
                           while (!q.isEmpty()) {
                              int[] now = q.poll();
                              for (int a = 0; a < 4; a++) {
                                 int nnx = now[0] + dx[a];
                                 int nny = now[1] + dy[a];
                                 if (0 <= nnx && nnx < N && 0 <= nny && nny < N && !visited[nnx][nny]) {
                                    if (L <= Math.abs(map[i][j] - map[nnx][nny])
                                          && Math.abs(map[i][j] - map[nnx][nny]) < R) {
                                       q.add(new int[] { nnx, nny });
                                       visited[nnx][nny] = true;
                                       cnt++;
                                       total += map[nnx][nny];
                                       list.add(new int[] { i, j });
                                    }
                                 }
                              }
                           }
                           for (int[] l : list) {
                              int val = total / cnt;
                              map[l[0]][l[1]] = val;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }
}