import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	static int R, C, K, ans;
	static int[][] map, number;
	static int[] dx = { -1, 0, 1, 0 };
	static int[] dy = { 0, 1, 0, -1 };

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		R = Integer.parseInt(st.nextToken()) + 3;
		C = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		ans = 0;

		map = new int[R][C];
		number = new int[R][C];
		for (int i = 1; i <= K; i++) {
			st = new StringTokenizer(br.readLine());
			int y = Integer.parseInt(st.nextToken()) - 1;
			int dir = Integer.parseInt(st.nextToken());
			int x = 0;
			boolean flag;

			while (true) {
				flag = false;
				if (isRange(y - 1, y + 1, x, x + 2) && map[x + 2][y] == 0 && map[x + 1][y + 1] == 0
						&& map[x + 1][y - 1] == 0) {
					flag = true;
					x++;
				} else if (isRange(y - 2, y + 1, x - 1, x + 2) && map[x + 1][y - 1] == 0 && map[x][y - 2] == 0
						&& map[x - 1][y - 1] == 0 && map[x + 1][y - 2] == 0 && map[x + 2][y - 1] == 0) {
					flag = true;
					x++;
					y--;
					dir = (dir + 3) % 4;
				} else if (isRange(y - 1, y + 2, x - 1, x + 2) && map[x][y + 2] == 0 && map[x - 1][y + 1] == 0
						&& map[x + 1][y + 1] == 0 && map[x + 2][y + 1] == 0 && map[x + 1][y + 2] == 0) {
					flag = true;
					x++;
					y++;
					dir = (dir + 1) % 4;
				}
				if (!flag || x + 1 == R - 1) {
					break;
				}
			}

			map[x][y] = i;
			map[x - 1][y] = i;
			map[x + 1][y] = i;
			map[x][y - 1] = i;
			map[x][y + 1] = i;
			number[x][y] = i;
			number[x - 1][y] = i;
			number[x + 1][y] = i;
			number[x][y - 1] = i;
			number[x][y + 1] = i;

			if (dir == 0) {
				map[x - 1][y] = -1;
			} else if (dir == 1) {
				map[x][y + 1] = -1;
			} else if (dir == 2) {
				map[x + 1][y] = -1;
			} else if (dir == 3) {
				map[x][y - 1] = -1;
			}

			if (x - 1 < 3) {
				for (int a = 0; a < R; a++) {
					for (int b = 0; b < C; b++) {
						map[a][b] = 0;
						number[a][b] = 0;
					}
				}
			} else {
				bfs(x, y);
			}
		}
		System.out.println(ans);
	}

	public static void bfs(int x, int y) {
		Queue<Point> q = new LinkedList<>();
		q.add(new Point(x, y));
		boolean[][] visited = new boolean[R][C];
		visited[x][y] = true;
		int max = x;

		while (!q.isEmpty()) {
			Point now = q.poll();
			max = Math.max(max, now.x);
			for (int i = 0; i < 4; i++) {
				int nx = now.x + dx[i];
				int ny = now.y + dy[i];
				if (0 > nx || nx >= R || 0 > ny || ny >= C)
					continue;
				if (visited[nx][ny])
					continue;
				if ((map[now.x][now.y] == -1 && map[nx][ny] != 0) || (map[now.x][now.y] == map[nx][ny])
						|| (map[now.x][now.y] != -1 && map[nx][ny] == -1 && number[now.x][now.y] == number[nx][ny])) {
					q.add(new Point(nx, ny));
					visited[nx][ny] = true;
				}
			}
		}
		ans += max - 2;
	}

	public static boolean isRange(int left, int right, int up, int down) {
		return 0 <= left && right < C && 0 <= up && down < R;
	}
}