import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
    static int N, M, K, attackPoint[][];
    static Turret[][] map;
    static ArrayList<Turret> remain;
    static Turret attacker, victim;
    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {1, 0, -1, 0};
    static int[] bdx = {-1, -1, 0, 1, 1, 1, 0, -1};
    static int[] bdy = {0, 1, 1, 1, 0, -1, -1, -1};

    static class Turret implements Comparable<Turret>{
        int x, y, p, r;

        public Turret(int x, int y, int p, int r) {
            this.x = x; // 행
            this.y = y; // 열
            this.p = p; // 공격력
            this.r = r; // 최근 공격 시간
        }

        @Override
        public int compareTo(Turret o) {
            if(this.p != o.p) {
                return this.p - o.p;
            }else if(this.r != o.r) {
                return this.r - o.r;
            }else if((this.x + this.y) != (o.x + o.y)) {
                return (o.x + o.y) - (this.x + this.y);
            }else {
                return o.y - this.y;
            }
        }

        @Override
        public String toString() {
            return "Turret [x=" + x + ", y=" + y + ", p=" + p + ", r=" + r + "]";
        }
        
    }
    
    public static class Point{
    	int x, y, t;

		public Point(int x, int y, int t) {
			this.x = x;
			this.y = y;
			this.t = t;
		}

		@Override
		public String toString() {
			return "Point [x=" + x + ", y=" + y + ", t=" + t + "]";
		}
    	
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        
        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        
        map = new Turret[N][N];
        for(int i = 0; i <N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < M; j++) {
                int power = Integer.parseInt(st.nextToken());
                map[i][j] = new Turret(i, j, power, 0);
            }
        }
        
        for(int i = 0; i < K; i++) {
        	choice();
        	maintain();
        }
        int ans = 0;
        for(int i = 0; i < N; i++) {
        	for(int j = 0; j < M; j++) {
        		ans = Math.max(ans, map[i][j].p);
        	}
        }System.out.println(ans);
    }
    
    public static void choice() {
        remain = new ArrayList<>();
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < M; j++) {
                if(map[i][j].p > 0) {
                	remain.add(map[i][j]);
                }
            }
        }
        Collections.sort(remain);
        attacker = remain.get(0);
        victim = remain.get(remain.size()-1);
        
        map[attacker.x][attacker.y].p += N + M;
//        System.out.println(attacker);
//        System.out.println(victim);
        
        attackPoint = new int[N][M];
        attackPoint[attacker.x][attacker.y] = 1;
        laser();
    }
    
    public static void laser() {
    	boolean flag = false;
    	Queue<Point> q = new LinkedList<>();
    	q.add(new Point(attacker.x, attacker.y, 0));
    	int[][] visited = new int[N][M];
    	visited[attacker.x][attacker.y] = 0;
    	Point[][] track = new Point[N][M];
    	
    	while(!q.isEmpty()) {
    		Point now = q.poll();
    		//System.out.println(now);
    		if(now.x == victim.x && now.y == victim.y) {
    			flag = true;
    			break;
    		}
    		for(int i = 0; i < 4; i++) {
    			int nx = (now.x + dx[i] + N)%N;
    			int ny = (now.y + dy[i] + M)%M;
    			if(map[nx][ny].p <= 0) continue;
    			if(visited[nx][ny] == 0) {
    				visited[nx][ny] = now.t +1;
    				track[nx][ny] = new Point(now.x, now.y, now.t + 1);
    				q.add(new Point(nx, ny, now.t + 1));
    			}else if(track[nx][ny] != null && visited[nx][ny] > now.t +1) {
    				visited[nx][ny] = now.t +1;
    				track[nx][ny] = new Point(now.x, now.y, now.t + 1);
    				q.add(new Point(nx, ny, now.t + 1));
    			}
    		}
    	}
//    	for(int i = 0; i < N; i++) {
//    		for(int j = 0; j < M; j++) {
//    			System.out.println(i + " " + j + " " + track[i][j]);
//    		}
//    	}
    	if(!flag) {
    		bomb();//포탄 공격 함수 받아오기
    	}
    	else {
    		int attackValue = map[attacker.x][attacker.y].p;
    		map[victim.x][victim.y].p -= attackValue;
    		attackPoint[victim.x][victim.y] = 1;
    		int x = victim.x;
    		int y = victim.y;
    		while(true) {
    			
    			int nx = track[x][y].x;
    			int ny = track[x][y].y;
    			if(nx == attacker.x && ny == attacker.y) {
    				break;
    			}
    			map[nx][ny].p -= attackValue / 2;
    			attackPoint[nx][ny] = 1;
    			x = nx;
    			y = ny;
    		}
    		
//    		for(int i = 0; i < N; i++) {
//        		for(int j = 0; j < M; j++) {
//        			System.out.println(i + " " + j + " " + map[i][j]);
//        		}
//        	}
    	}
    }
    
    public static void bomb() {
    	int x = victim.x;
    	int y = victim.y;
    	int attackValue = map[attacker.x][attacker.y].p;
    	map[x][y].p -= attackValue;
    	attackPoint[victim.x][victim.y] = 1;
    	
    	for(int i = 0; i < 8; i++) {
    		int nx = (x + bdx[i] + N)%N;
			int ny = (y + bdy[i] + M)%M;
			if(nx == attacker.x && ny == attacker.y)continue;
			if(map[nx][ny].p <= 0) continue;
			else {
				map[nx][ny].p -= attackValue / 2;
				attackPoint[nx][ny] = 1;
			}
    	}
    }
    
    public static void maintain() {
    	for(int i = 0; i < N; i++) {
    		for(int j = 0; j < M; j++) {
    			if(map[i][j].p <= 0) {
    				map[i][j].p = 0;
    			}
    		}
    	}
    	
    	for(int i = 0; i < N; i++) {
    		for(int j = 0; j < M; j++) {
    			if(map[i][j].p > 0 && attackPoint[i][j] == 0) {
    				map[i][j].p += 1;
    			}
    		}
    	}
    }

}