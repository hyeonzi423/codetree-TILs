import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
    static int L, N, Q; // 격자의 크기, 기사의 정보 수, 왕의 명령 수
    static int[][] map, playerMap;
    static Player[] players;
    static int[][] orders;
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};
    static boolean[] together;
    
    static class Player{
        int r, c, h, w, k, damege;

        public Player(int r, int c, int h, int w, int k) {
            this.r = r;
            this.c = c;
            this.h = h;
            this.w = w;
            this.k = k;
        }

		@Override
		public String toString() {
			return "Player [r=" + r + ", c=" + c + ", h=" + h + ", w=" + w + ", k=" + k + ", damege=" + damege + "]";
		}
        
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        L = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());
        
        map = new int[L][L]; // 0 : 빈칸 / 1 : 함정 / 2 : 벽  
        for(int i = 0; i < L; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < L; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        
        players = new Player[N+1];
        for(int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            players[i+1] = new Player(r-1, c-1, h, w, k);
        }
        
        orders = new int[Q][2];
        for(int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            int num = Integer.parseInt(st.nextToken());
            int dir = Integer.parseInt(st.nextToken());
            orders[i][0] = num;
            orders[i][1] = dir;
        }
        
        for(int i = 0; i < Q; i++) {
        	if(check(i)) {
        		move(i);
        	}
//        	for(Player p : players) {
//            	System.out.println(p);
//            }
        }
        
        int ans = 0;
        for(Player p : players) {
        	//System.out.println(p);
        	if(p!=null && p.k > 0) {
        		ans += p.damege;
        	}
        }
        System.out.println(ans);
    }
    
    public static boolean check(int turn) {
    	int num = orders[turn][0];
    	int dir = orders[turn][1];
    	
    	if(players[num].k <= 0) return false;
    	
    	playerMap = new int[L][L];
    	for(int a = 1; a <= N; a++) {
    		Player p = players[a];
    		if(p.k <= 0) continue;
    		for(int i = p.r; i < p.r + p.h; i++) {
        		for(int j = p.c; j < p.c + p.w; j++) {
        			playerMap[i][j] = a;
        		}
        	}
    	}
    	together = new boolean[N+1];
    	Queue<Point> q = new LinkedList<>();
    	boolean[][] visited = new boolean[L][L];
    	Player p = players[num];
    	
    	together[num] = true;
    	for(int i = p.r; i < p.r + p.h; i++) {
    		for(int j = p.c; j < p.c + p.w; j++) {
    			q.add(new Point(i, j));
    			visited[i][j] = true;
    		}
    	}
    	
    	while(!q.isEmpty()) {
    		Point now = q.poll();
    		
    		int nx = now.x + dx[dir];
    		int ny = now.y + dy[dir];
    		
    		if(0 > nx || nx >= L || 0> ny || ny >= L || map[nx][ny] == 2) return false;
    		else if(playerMap[nx][ny] != 0 && playerMap[nx][ny] != num && !visited[nx][ny]){
    			Player tmp = players[playerMap[nx][ny]];
    			for(int r = tmp.r; r < tmp.r + tmp.h; r++) {
    				for(int c = tmp.c; c < tmp.c + tmp.w; c++) {
    					q.add(new Point(r, c));
    					visited[r][c] = true;
    					together[playerMap[r][c]] = true;
    				}
    			}
    			
    		}
    	}
		return true;
    }
    
    public static void move(int turn) {
    	int num = orders[turn][0];
    	int dir = orders[turn][1];
    	
    	for(int i = 1; i <=N; i++) {
    		if(together[i]) {
    			Player p = players[i];
    			p.r += dx[dir];
    			p.c += dy[dir];
    			
    			if(i == num) continue;
    			for(int r = p.r; r < p.r + p.h; r++) {
    				for(int c = p.c; c < p.c + p.w; c++) {
    					if(map[r][c] == 1) {
    						p.damege++;
    					}
    				}
    			}
    		}
    	}
    	
    	for(int i = 1; i <=N; i++) {
    		Player p = players[i];
    		if(i==num) continue;
    		if(p.k - p.damege <= 0) p.k = 0; 
    	}
    }
}