import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
	static int N, M, K, C;
	static int ans = 0;
	static int[][] tree, tmpTree, herbicide;
	static int[] dx = { -1, 0, 1, 0 };
	static int[] dy = { 0, 1, 0, -1 };
	static int[] hx = { -1, -1, 1, 1 };
	static int[] hy = { -1, 1, 1, -1 };

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken()); // 크기
		M = Integer.parseInt(st.nextToken()); // 박멸이 진행되는 수
		K = Integer.parseInt(st.nextToken()); // 제초제의 확산 범위
		C = Integer.parseInt(st.nextToken()); // 남이있는 년 수

		tree = new int[N][N];
		herbicide = new int[N][N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				tree[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		for (int i = 0; i < M; i++) {
			grow();
			spread();
			Point c = choice();
			if(c.x == -1 && c.y == -1) break;
			kill(c);
			time();
		}
		System.out.println(ans);
	}

	public static void grow() {
		tmpTree = new int[N][N];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (tree[i][j] > 0) {
					for (int k = 0; k < 4; k++) {
						int nx = i + dx[k];
						int ny = j + dy[k];
						if (isRange(nx, ny) && tree[nx][ny] > 0) {
							tmpTree[i][j]++;
						}
					}
				}
			}
		}

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				tree[i][j] += tmpTree[i][j];
			}
		}
	}

	public static void spread() {
		tmpTree = new int[N][N];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (tree[i][j] > 0) {
					int cnt = 0;
					ArrayList<Point> points = new ArrayList<>();
					for (int k = 0; k < 4; k++) {
						int nx = i + dx[k];
						int ny = j + dy[k];
						if (isRange(nx, ny) && tree[nx][ny] == 0 && herbicide[nx][ny] == 0) {
							cnt++;
							points.add(new Point(nx, ny));
						}
					}
					for (Point p : points) {
						tmpTree[p.x][p.y] += tree[i][j] / cnt;
					}
				}
			}
		}

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				tree[i][j] += tmpTree[i][j];
			}
		}
	}

	public static Point choice() {
		int max = 0, cx = -1, cy = -1;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (tree[i][j] > 0) {
					int tmp = tree[i][j];
					for (int k = 0; k < 4; k++) {
						for (int s = 1; s <= K; s++) {
							int nx = i + s * hx[k];
							int ny = j + s * hy[k];
							if (!isRange(nx, ny) || tree[nx][ny] <= 0)
								break;
							tmp += tree[nx][ny];
						}
					}
					if (max < tmp) {
						max = tmp;
						cx = i;
						cy = j;
					}
				}
			}
		}
		ans += max;
		return new Point(cx, cy);
	}

	public static void kill(Point c) {
		tree[c.x][c.y] = 0;
		herbicide[c.x][c.y] = C+1;
		for (int k = 0; k < 4; k++) {
			for (int s = 1; s <= K; s++) {
				int nx = c.x + s * hx[k];
				int ny = c.y + s * hy[k];
				if (!isRange(nx, ny))
					break;
				if (tree[nx][ny] == 0 || tree[nx][ny] == -1) {
					herbicide[nx][ny] = C+1;
					break;
				}
				if (tree[nx][ny] != -1) {
					tree[nx][ny] = 0;
					herbicide[nx][ny] = C+1;
				}
			}
		}
	}

	public static void time() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (herbicide[i][j] > 0) {
					herbicide[i][j]--;
				}
			}
		}
	}
	
	public static boolean isRange(int x, int y) {
		return 0 <= x && x < N && 0 <= y && y < N;
	}
}