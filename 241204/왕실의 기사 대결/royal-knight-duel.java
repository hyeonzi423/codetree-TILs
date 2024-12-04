import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	static int N, M, Q;
	static Soldier[] soldiers;
	static int[][] map, sMap;
	static int[] dead, damage;
	static ArrayList<Integer> moveIdx;
	static int[] dx = { -1, 0, 1, 0 };
	static int[] dy = { 0, 1, 0, -1 };

	static class Soldier {
		int r, c, h, w, k;

		public Soldier(int r, int c, int h, int w, int k) {
			this.r = r;
			this.c = c;
			this.h = h;
			this.w = w;
			this.k = k;
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken()); // 체스판의 크기
		M = Integer.parseInt(st.nextToken()); // 기사의 수
		Q = Integer.parseInt(st.nextToken()); // 명령의 수

		damage = new int[M + 1];
		dead = new int[M + 1];
		map = new int[N][N];
		sMap = new int[N][N];
		soldiers = new Soldier[M + 1];

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		for (int i = 1; i <= M; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken()) - 1;
			int c = Integer.parseInt(st.nextToken()) - 1;
			int h = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int k = Integer.parseInt(st.nextToken());
			soldiers[i] = new Soldier(r, c, h, w, k);
		}

		for (int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());
			int idx = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken());
			if (canGo(idx, dir)) {
				move(idx, dir);
			}
		}
		
		int ans = 0;
		for(int i = 1; i <= M; i++) {
			if(dead[i] == 1) continue;
			ans += damage[i];
		}
		System.out.println(ans);
	}

	public static boolean canGo(int idx, int dir) {
		if (dead[idx] == 1)
			return false;

		sMap = new int[N][N];
		moveIdx = new ArrayList<>();
		moveIdx.add(idx);
		Queue<Point> q = new LinkedList<>();
		boolean[][] visited = new boolean[N][N];

		for (int i = 1; i <= M; i++) {
			if(dead[i] == 1) continue;
			Soldier s = soldiers[i];
			for (int a = s.r; a < s.r + s.h; a++) {
				for (int b = s.c; b < s.c + s.w; b++) {
					sMap[a][b] = i;
					if (i == idx) {
						q.add(new Point(a, b));
						visited[a][b] = true;
					}
				}
			}
		}
		
		while (!q.isEmpty()) {
			Point p = q.poll();
			int nx = p.x + dx[dir];
			int ny = p.y + dy[dir];
			if (!isRange(nx, ny))
				return false;
			if (map[nx][ny] == 2)
				return false;
			if (sMap[nx][ny] == idx || sMap[nx][ny] == 0)
				continue;
			if (!visited[nx][ny]) {
				int tmp = sMap[nx][ny];
				moveIdx.add(tmp);
				Soldier pNow = soldiers[tmp];
				for (int a = pNow.r; a < pNow.r + pNow.h; a++) {
					for (int b = pNow.c; b < pNow.c + pNow.w; b++) {
						q.add(new Point(a, b));
						visited[a][b] = true;
					}
				}
			}
		}
		return true;
	}

	public static void move(int orderIdx, int dir) {
		for (int idx : moveIdx) {
			Soldier s = soldiers[idx];
			s.r += dx[dir];
			s.c += dy[dir];

			if (orderIdx == idx)
				continue;
			for (int a = s.r; a < s.r + s.h; a++) {
				for (int b = s.c; b < s.c + s.w; b++) {
					if (map[a][b] == 1) {
						s.k -= 1;
						damage[idx]++;
					}
				}
			}
			if (s.k <= 0) {
				dead[idx] = 1;
			}
		}
	}

	public static boolean isRange(int x, int y) {
		return 0 <= x && x < N && 0 <= y && y < N;
	}
}