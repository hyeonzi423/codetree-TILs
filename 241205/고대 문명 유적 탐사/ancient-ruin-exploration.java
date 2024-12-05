import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	static int K, M, N = 5;
	static int[][] map, tmpMap;
	static ArrayList<Integer> ans;
	static Queue<Integer> wall;
	static PriorityQueue<Pos> pq;
	static PriorityQueue<Point> points;
	static int[] fdx = { -1, 0, 1, 0 };
	static int[] fdy = { 0, 1, 0, -1 };
	static int[] dx = { -1, -1, 0, 1, 1, 1, 0, -1 };
	static int[] dy = { 0, 1, 1, 1, 0, -1, -1, -1 };

	static class Pos implements Comparable<Pos> {
		int c, d, y, x;

		public Pos(int c, int d, int y, int x) {
			this.c = c;
			this.d = d;
			this.y = y;
			this.x = x;
		}

		@Override
		public int compareTo(Pos o) {
			if (this.c != o.c)
				return Integer.compare(o.c, this.c);
			if (this.d != o.d)
				return Integer.compare(this.d, o.d);
			if (this.y != o.y)
				return Integer.compare(this.y, o.y);
			return Integer.compare(this.x, o.x);
		}

		@Override
		public String toString() {
			return "Pos [c=" + c + ", d=" + d + ", y=" + y + ", x=" + x + "]";
		}
		
		
	}

	static class Point implements Comparable<Point> {
		int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Point o) {
			if (this.y != o.y) return Integer.compare(this.y, o.y);
			return Integer.compare(o.x, this.x);
		}

		@Override
		public String toString() {
			return "Point [x=" + x + ", y=" + y + "]";
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		K = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		map = new int[5][5];
		tmpMap = new int[5][5];
		ans = new ArrayList<>();
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		st = new StringTokenizer(br.readLine());
		wall = new LinkedList<>();
		for (int i = 0; i < M; i++) {
			wall.add(Integer.parseInt(st.nextToken()));
		}

		int[] deg = new int[] { 0, 2, 4, 6 };

		for (int t = 0; t < K; t++) {
			pq = new PriorityQueue<>();
			int flag = -1;
			for (int i = 1; i < N - 1; i++) {
				for (int j = 1; j < N - 1; j++) {
					for (int d = 0; d < 4; d++) {
						rotate(i, j, deg[d]);
						int ret = get(tmpMap);
						if(ret > 0) flag = ret;
						pq.add(new Pos(ret, deg[d], j, i));
					}
				}
			}
			if(flag == -1) break;
			int tmpCnt = 0;
			Pos best = pq.poll();
			int ret = change(best);
			tmpCnt += ret;
			while (true) {
				if (get(map) > 0) {
					ret = change(new Pos(0, 0, 1, 1));
					tmpCnt += ret;
				} else {
					break;
				}
			}
			ans.add(tmpCnt);
		}
		
		for(int n : ans) {
			System.out.print(n + " ");
		}
	}

	public static void rotate(int cx, int cy, int degree) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				tmpMap[i][j] = map[i][j];
			}
		}

		for (int i = 0; i < 8; i++) {
			int dir = (i - degree + 8) % 8;
			tmpMap[cx + dx[i]][cy + dy[i]] = map[cx + dx[dir]][cy + dy[dir]];
		}

	}

	public static int get(int[][] tmpMap) {
		boolean[][] visited = new boolean[N][N];
		Queue<Point> q = new LinkedList<>();
		int totalCnt = 0;

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (!visited[i][j]) {
					int cnt = 1;
					q.add(new Point(i, j));
					visited[i][j] = true;

					while (!q.isEmpty()) {
						Point now = q.poll();
						for (int k = 0; k < 4; k++) {
							int nx = now.x + fdx[k];
							int ny = now.y + fdy[k];
							if (!isRange(nx, ny))
								continue;
							if (visited[nx][ny])
								continue;
							if (tmpMap[nx][ny] != tmpMap[i][j])
								continue;
							visited[nx][ny] = true;
							cnt++;
							q.add(new Point(nx, ny));
						}
					}
					if (cnt >= 3) {
						totalCnt += cnt;
					}
				}
			}
		}
		return totalCnt;
	}

	public static int change(Pos pos) {
		rotate(pos.x, pos.y, pos.d);

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				map[i][j] = tmpMap[i][j];
			}
		}

		points = new PriorityQueue<>();
		boolean[][] visited = new boolean[N][N];
		Queue<Point> q = new LinkedList<>();

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (!visited[i][j]) {
					q.add(new Point(i, j));
					visited[i][j] = true;
					ArrayList<Point> tmpList = new ArrayList<>();
					tmpList.add(new Point(i, j));

					while (!q.isEmpty()) {
						Point now = q.poll();
						for (int k = 0; k < 4; k++) {
							int nx = now.x + fdx[k];
							int ny = now.y + fdy[k];
							if (!isRange(nx, ny))
								continue;
							if (visited[nx][ny])
								continue;
							if (map[nx][ny] != map[i][j])
								continue;
							visited[nx][ny] = true;
							q.add(new Point(nx, ny));
							tmpList.add(new Point(nx, ny));
						}
					}
					if (tmpList.size() >= 3) {
						for (Point p : tmpList) {
							points.add(p);
						}
					}
				}
			}
		}

		int r= points.size();
		while (!points.isEmpty()) {
			Point p = points.poll();
			map[p.x][p.y] = wall.poll();
		}
		return r;
	}

	public static boolean isRange(int x, int y) {
		return 0 <= x && x < N && 0 <= y && y < N;
	}
}