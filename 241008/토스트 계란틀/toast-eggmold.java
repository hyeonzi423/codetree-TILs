import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;
import java.awt.Point;

public class Main {
	static int N, L, R, ans;
	static int[][] map;
	static boolean[][] visited;
	static boolean flag;
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, -1, 0, 1};
	
   public static void main(String[] args) throws IOException {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      StringTokenizer st = new StringTokenizer(br.readLine());
      N = Integer.parseInt(st.nextToken());
      L = Integer.parseInt(st.nextToken());
      R = Integer.parseInt(st.nextToken());
      ans = 0;
      
      map = new int[N][N];
      for(int i = 0; i < N; i++) {
    	  st = new StringTokenizer(br.readLine());
    	  for(int j = 0; j < N; j++) {
    		  map[i][j] = Integer.parseInt(st.nextToken());
    	  }
      }
      
      simul();
   }
   
   public static void simul() {
	   while(true) {
		   flag = false;
		   visited = new boolean[N][N];
		   
		   for(int i = 0; i < N; i++) {
			   for(int j = 0; j < N; j++) {
				   if(!visited[i][j]) {
					   bfs(i, j);
				   }
			   }
		   }
		   if(flag) {
			   ans++;
		   }else {
			   System.out.println(ans);
			   break;
		   }
	   }
   }
   
   public static void bfs(int x, int y) {
	   Queue<Point> q = new LinkedList<>();
	   q.add(new Point(x, y));
	   visited[x][y] = true;
	   int cnt = 1;
	   int total = map[x][y];
	   ArrayList<Point> list = new ArrayList<>();
	   list.add(new Point(x, y));
	   
	   while(!q.isEmpty()) {
		   Point now = q.poll();
		   for(int k = 0; k < 4; k++) {
			   int nx = now.x + dx[k];
			   int ny = now.y + dy[k];
			   if(inRange(nx, ny) && !visited[nx][ny] && L <= Math.abs(map[nx][ny] - map[now.x][now.y]) && R >= Math.abs(map[nx][ny] - map[now.x][now.y])) {
				   q.add(new Point(nx, ny));
				   cnt++;
				   total += map[nx][ny];
				   visited[nx][ny] = true;
				   list.add(new Point(nx, ny));
			   }
		   }
	   }
	   
	   for (Point p: list) {
		   map[p.x][p.y] = total/cnt;
	   }
	   
	   if(list.size() > 1) {
		   flag = true;
	   }
   }
   
   public static boolean inRange(int x, int y) {
	   return 0 <= x && x < N && 0 <= y && y < N; 
   }
}