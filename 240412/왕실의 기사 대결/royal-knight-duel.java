import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
    static int L, N, Q;
    static int[][] map, ground;
    static boolean[] dead;
    static int[] totalDamage;
    static Soldier[] soldiers;
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};
    static ArrayList<Integer> near;
    
    static class Soldier{
        int r, c, h, w, k;

        public Soldier(int r, int c, int h, int w, int k) {
            this.r = r;
            this.c = c;
            this.h = h;
            this.w = w;
            this.k = k;
        }

		@Override
		public String toString() {
			return "Soldier [r=" + r + ", c=" + c + ", h=" + h + ", w=" + w + ", k=" + k + "]";
		}
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        L = Integer.parseInt(st.nextToken()); // 체스판 크기
        N = Integer.parseInt(st.nextToken()); // 기사의 수
        Q = Integer.parseInt(st.nextToken()); // 명령의 수
        
        map = new int[L][L]; 
        ground = new int[L][L];
        for(int i = 0; i < L; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < L; j++) {
            	ground[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        
        soldiers = new Soldier[N+1];
        for(int i = 1; i <= N; i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken())-1;
            int c = Integer.parseInt(st.nextToken())-1;
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            soldiers[i] = new Soldier(r, c, h, w, k);
        }
        
        dead = new boolean[N+1];
        totalDamage = new int[N+1];
        for(int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            boolean ret = canMove(n, d);
            if(ret)
            	move(n, d);
//            for(int a = 1; a <= N; a++) {
//            	System.out.println(soldiers[a]);
//            }
//            System.out.println();
        }
        
        int ans = 0;
        for(int i = 1; i <= N; i++) {
        	if(!dead[i])
        		ans += totalDamage[i];
        }
        System.out.println(ans);
    }
    
    public static boolean canMove(int idx, int dir) {
    	Queue<Point> q = new LinkedList<>();
    	boolean[][] visited = new boolean[L][L];
    	map = new int[L][L];
    	near = new ArrayList<>();
    	near.add(idx);
    	
    	for(int i = 1; i <= N; i++) {
    		if(dead[i]) continue;
    		Soldier s = soldiers[i];
    		for(int a = s.r; a < s.r + s.h; a++) {
    			for(int b = s.c; b <s.c + s.w; b++) {
    				map[a][b] = i;
    				if(i == idx) {
    					q.add(new Point(a, b));
    					visited[a][b] = true;
    	    		}
    			}
    		}
    	}
    	
    	while(!q.isEmpty()) {
    		Point now = q.poll();
    		int nx = now.x + dx[dir];
    		int ny = now.y + dy[dir];
    		if(!inRange(nx, ny)) return false;
    		if(ground[nx][ny] == 2) return false;
    		if(map[nx][ny] == idx || map[nx][ny] == 0) continue;
    		if(!visited[nx][ny]) {
    			int tmp = map[nx][ny];
    			near.add(tmp);
    			Soldier s = soldiers[tmp];
    			for(int a = s.r; a < s.r + s.h; a++) {
        			for(int b = s.c; b <s.c + s.w; b++) {
        				q.add(new Point(a, b));
    					visited[a][b] = true;
        			}
        		}
    		}
    	}
    	return true;
    }
    
    public static void move(int nowIdx, int dir) {
    	 for(int idx : near) {
    		 Soldier s = soldiers[idx];
    		 s.r += dx[dir];
    		 s.c += dy[dir];
    		 
    		 if(nowIdx == idx) continue;
    		 int damage = 0;
    		 for(int a = s.r; a < s.r + s.h; a++) {
    			 for(int b = s.c; b < s.c + s.w; b++) {
    				 if(ground[a][b] == 1)
    					 damage++;
    			 }
    		 }
    		 s.k -= damage;
    		 if(s.k <= 0) {
    			 dead[idx] = true;
    		 }
    		 totalDamage[idx] += damage;
    	 }
    }
    
    public static boolean inRange(int x, int y) {
    	return 0 <= x && x < L && 0 <= y && y < L;
    }
}