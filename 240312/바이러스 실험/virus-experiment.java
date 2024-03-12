import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
	static int N, M, K;
	static PriorityQueue<Integer>[][] virus, tmpVirus; 
	static int[][] map;
	static int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
	static int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};
	
	static class Dead{
		int x, y, age;

		public Dead(int x, int y, int age) {
			this.x = x;
			this.y = y;
			this.age = age;
		}
	}
	
	static void init() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken()); // N 배지의 크기
		M = Integer.parseInt(st.nextToken()); // M 바이러스의 개수
		K = Integer.parseInt(st.nextToken()); // K 사이클 수
		
		map = new int[N][N]; // 양분 저장
		for(int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j < N; j++) {
				map[i][j] =Integer.parseInt(st.nextToken());
			}
		}
		
		virus = new PriorityQueue[N][N]; // 바이러스 저장
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				virus[i][j] = new PriorityQueue<>();
			}
		}
		
		for(int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			int a = Integer.parseInt(st.nextToken());
			virus[r-1][c-1].add(a);
		}
	}
	
	static void eatNutrient(){
		tmpVirus = new PriorityQueue[N][N];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				tmpVirus[i][j] = new PriorityQueue<>();
			}
		}
		List<Dead> deadVirus = new LinkedList<>();
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(virus[i][j].size() == 0) continue;
				while(!virus[i][j].isEmpty()){
					int now = virus[i][j].poll();
					if(map[i][j] >= now) {
						map[i][j] -= now;
						tmpVirus[i][j].add(now+1);
					}
					else {
						deadVirus.add(new Dead(i, j, now));
					}
				}
			}
		}
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				virus[i][j].clear();
				if(tmpVirus[i][j].size() == 0) continue;
				while(!tmpVirus[i][j].isEmpty()) {
					virus[i][j].add(tmpVirus[i][j].poll());
				}
			}
		}
		
		for(Dead dv : deadVirus) {
			map[dv.x][dv.y] += dv.age/2;
		}
	}
	
	static void spreadVirus() {
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(virus[i][j].size() == 0) continue;
				for(int v : virus[i][j]) {
					if(v % 5 == 0) {
						for(int k = 0; k < 8; k++) {
							int nx = i + dx[k];
							int ny = j + dy[k];
							if(isRange(nx, ny)) {
								virus[nx][ny].add(1);
							}
						}
					}
				}
			}
		}
	}
	
	static void addNutrient() {
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				map[i][j] += 1;
			}
		}
	}
	
	static int remainVirus() {
		int cnt = 0;
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				cnt += virus[i][j].size();
			}
		}
		return cnt;
	}
	
	static boolean isRange(int x, int y) {
		return 0 <= x && x < N && 0 <= y && y < N;
	}
	
	public static void main(String[] args) throws IOException {
		init();
		
		for(int t = 0; t < K; t++) {
			eatNutrient();
			spreadVirus();
			addNutrient();
		}
		
		int ans = remainVirus();
		System.out.println(ans);
	}

}