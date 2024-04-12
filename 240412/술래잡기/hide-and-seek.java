import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class Main {
	static int N, M, H, K;
	static Player[] runners; // 도망자들의 현재 정보
	static Player catcher; // 술래의 정보
	static boolean[][] tree; // 나무가 있는지 확인
	static boolean[] isCatch; // 이미 술래에게 잡혔는지
	static ArrayList<Integer>[][] map; // 술래가 잡을때 확인하기 위한 map
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	static ArrayList<Player> track;
	static int turn, catchNum = 0;
	
	static class Player{
		int x, y, d;

		public Player(int x, int y, int d) {
			this.x = x;
			this.y = y;
			this.d = d;
		}

		@Override
		public String toString() {
			return "Player [x=" + x + ", y=" + y + ", d=" + d + "]";
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken()); // 격자의 크기
		M = Integer.parseInt(st.nextToken()); // 도망자의 수
		H = Integer.parseInt(st.nextToken()); // 나무의 수
		K = Integer.parseInt(st.nextToken()); // 턴의 수
		StringBuilder sb= new StringBuilder();
		
		map = new ArrayList[N][N];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				map[i][j] = new ArrayList<>();
			}
		}
		
		runners = new Player[M];
		for(int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken())-1;
			int y = Integer.parseInt(st.nextToken())-1;
			int d = Integer.parseInt(st.nextToken());
			runners[i] = new Player(x, y, d);
		}
		
		tree = new boolean[N][N];
		for(int i = 0; i <H; i++) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken())-1;
			int y = Integer.parseInt(st.nextToken())-1;
			tree[x][y] = true; // 나무가 존재함을 의미
		}
		
		isCatch = new boolean[M];
		catcher = new Player(N/2, N/2, 0);
		track = new ArrayList<>();
		for(int i = 0; i < K/(N*N) + 1; i++) {
			catcherTrack();
			catcher = new Player(N/2, N/2, 0);
			track.add(catcher);
		}
		
//		for(Player p : track) {
//			System.out.println(p);
//		}
		
		
		int ans = 0;
		for(turn = 1; turn <= K; turn++) {
			moveRunner();
			catcher = track.get(turn-1);
			//System.out.println("catch " + catcher);
			int tmp = catchRunner();
			ans += tmp*turn;
		}
		System.out.println(ans);
	}
	
	public static void moveRunner() {
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				map[i][j].clear();
			}
		}
		
		for(int i = 0; i < M; i++) {
			if(isCatch[i]) continue; // 이미 잡혔다면 넘어감
			
			Player p = runners[i];
			int calDis = Math.abs(catcher.x - p.x) +  Math.abs(catcher.y - p.y);
			if(calDis > 3) {
				map[p.x][p.y].add(i);
				//System.out.println(map[p.y][p.y].get(0));
				continue; // 술래와의 거리가 3 초과면 넘어감
			}
			
			int nx = p.x + dx[p.d];
			int ny = p.y + dy[p.d];
			if(!inRange(nx, ny)) {
				p.d = (p.d + 2) % 4;
				nx = p.x + dx[p.d];
				ny = p.y + dy[p.d];
			}
			if(nx == catcher.x && ny == catcher.y) {
				map[p.x][p.y].add(i);
				continue; // 이동하려는 위치에 술래가 있으면 이동 X
			}
			map[nx][ny].add(i);
			p.x = nx;
			p.y = ny;
			
			//System.out.println(map[p.y][p.y].get(0));
		}
		
//		for(Player p : runners) {
//			System.out.println(p);
//		}
//		for(int i = 0; i < N; i++) {
//			for(int j =0; j < N; j++) {
//				System.out.println( i + " " + j + " " + map[i][j].size());
//			}
//		}
	}
	
	public static boolean inRange(int x, int y) {
		return 0<= x && x < N && 0 <= y && y < N;
	}

	public static void catcherTrack() {
		int cnt = 0;
		boolean reverse = false, flag = false;
		int x = catcher.x;
		int y = catcher.y;
		int d = catcher.d;
		
		while(cnt < K) {
			int rep = cnt / 2 + 1;
			for(int i = 0; i < rep; i++) {
				int nx = x + dx[d]; 
				int ny = y + dy[d]; 
				if(nx == 0 && ny == 0) {
					reverse = true;
					cnt = K;
					break;
				}
				if(i == rep - 1) {
					d = (d+1)%4;
					track.add(new Player(nx, ny, d));
				}else {
					track.add(new Player(nx, ny, d));
				}
				x = nx;
				y = ny;
				if(track.size() > K) cnt = K;
			}cnt += 1;
		}
		if(!reverse) {
			return;
		}
		
		x = 0;
		y = 0;
		d = 2;
		track.add(new Player(x, y, d));
		boolean[][] visited = new boolean[N][N];
		visited[0][0] = true;
		while(true) {
			int nx = x + dx[d];
			int ny = y + dy[d];
			if(!inRange(nx, ny) || visited[nx][ny]) {
				int s = track.size();
				d = (d-1+4)%4;
				track.get(s-1).d = d;
				continue;
			}
			if(nx == catcher.x && ny == catcher.y) break;
			track.add(new Player(nx, ny, d));
			visited[nx][ny] = true;
			x = nx;
			y = ny;
		}
		
//		for(Player p : track) {
//			System.out.println(p);
//		}
	}
	
	public static int catchRunner() {
//		for(int i = 0; i < N; i++) {
//			for(int j =0; j < N; j++) {
//				System.out.println( i + " " + j + " " + map[i][j].size());
//			}
//		}
		
		int tmpCnt = 0;
		catcher = track.get(turn-1);
		int x = catcher.x;
		int y = catcher.y;
		if(!tree[x][y]) {
			for(int idx : map[x][y]) {
				isCatch[idx] = true;
				catchNum++;
				tmpCnt++;
			}
		}
		
		for(int i = 0; i < 2; i++) {
			int nx = x + dx[catcher.d];
			int ny = y + dy[catcher.d];
			if (!inRange(nx, ny)) break;
			x = nx;
			y = ny;
			//System.out.println(nx +" " + ny);
			if(tree[nx][ny]) continue;
			for(int idx : map[nx][ny]) {
				isCatch[idx] = true;
				catchNum++;
				tmpCnt++;
			}
		}
		return tmpCnt;
	}
}