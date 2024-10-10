import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

	static int K, M, N = 5;
	static int[] ans;
	static int[][] map, tmpMap;
	static Queue<Integer> block;
	static int[] dx = { -1, 0, 1, 0 };
	static int[] dy = { 0, 1, 0, -1 };
	static PriorityQueue<Point> points;

	static class Point implements Comparable<Point> {
		int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Point p) {
			if (this.y == p.y) {
				return p.x - this.x;
			}
			return this.y - p.y;
		}
	}

	static class Node implements Comparable<Node> {
		int x, y, count, rotate;

		public Node(int x, int y, int count, int rotate) {
			this.x = x;
			this.y = y;
			this.count = count;
			this.rotate = rotate;
		}

		@Override
		public int compareTo(Node n) {

			if (this.count == n.count) {
				if (this.rotate == n.rotate) {
					if (this.x == n.x) {
						return this.y - n.y;
					}
					return this.x - n.x;
				}
				return this.rotate - n.rotate;
			}
			return n.count - this.count;
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		K = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		ans = new int[K];
		map = new int[N][N];
		tmpMap = new int[N][N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		block = new LinkedList<>();
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < M; i++) {
			block.add(Integer.parseInt(st.nextToken()));
		}

		for (int t = 0; t < K; t++) {
			ArrayList<Node> candidate = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					for (int d = 0; d < 4; d++) {
						rotate(i, j, d);
						int ret = bfs(tmpMap);
						if (ret > 0) {
							candidate.add(new Node(i, j, ret, d));
						}
					}
				}
			}
			if (candidate.size() == 0) {
				break;
			}

			Collections.sort(candidate);
			Node best = candidate.get(0);
			rotate(best.x, best.y, best.rotate);
			map = tmpMap;
			int tmpCount = 0;

			while (true) {
				int ret = bfs(map);
				tmpCount += ret;
				if (ret == 0)
					break;
				change();
			}
			ans[t] = tmpCount;
		}
		for (int i = 0; i < K; i++) {
			if (ans[i] != 0) {
				System.out.print(ans[i] + " ");
			}
		}
	}

	private static int bfs(int[][] arr) {
		boolean visited[][] = new boolean[N][N];
		Queue<Point> q = new LinkedList<>();
		points = new PriorityQueue<>();

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (!visited[i][j]) {
					q.add(new Point(i, j));
					visited[i][j] = true;
					ArrayList<Point> list = new ArrayList<>();
					list.add(new Point(i, j));

					while (!q.isEmpty()) {
						Point now = q.poll();
						for (int k = 0; k < 4; k++) {
							int nx = now.x + dx[k];
							int ny = now.y + dy[k];
							if (!isRange(nx, ny) || visited[nx][ny])
								continue;
							if (arr[nx][ny] == arr[i][j]) {
								q.add(new Point(nx, ny));
								list.add(new Point(nx, ny));
								visited[nx][ny] = true;
							}
						}
					}
					if (list.size() >= 3) {
						points.addAll(list);
					}
				}
			}
		}
		return points.size();
	}

	public static void rotate(int x, int y, int deg) {
		tmpMap = new int[N][N];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				tmpMap[i][j] = map[i][j];
			}
		}

		if (deg == 0)
			return;

		for (int i = x; i < x + 3; i++) {
			for (int j = y; j < y + 3; j++) {
				int ox = i - x;
				int oy = j - y;
				int rx, ry;

				if (deg == 1) {
					rx = oy;
					ry = 3 - 1 - ox;
				} else if (deg == 2) {
					rx = 3 - 1 - ox;
					ry = 3 - 1 - oy;
				} else {
					rx = 3 - 1 - oy;
					ry = ox;
				}
				tmpMap[x + rx][y + ry] = map[i][j];
			}
		}
	}

	public static void change() {
		while (!points.isEmpty()) {
			Point now = points.poll();
			map[now.x][now.y] = block.poll();
		}
	}

	public static boolean isRange(int x, int y) {
		return 0 <= x && x < N && 0 <= y && y < N;
	}

}