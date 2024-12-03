import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.awt.Point;

public class Main {
	static int N, M, K, goal, total;
	static int[][] maze;
	static Point exit;
	static int[][] pMap;
	static ArrayList<Point> people;
	static int[] dx = { -1, 1, 0, 0 };
	static int[] dy = { 0, 0, -1, 1 };

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken()); // 미로의 크기
		M = Integer.parseInt(st.nextToken()); // 참가자 수
		K = Integer.parseInt(st.nextToken()); // 게임 시간
		goal = 0;
		total = 0;

		maze = new int[N][N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				maze[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		pMap = new int[N][N];
		people = new ArrayList<>();
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			pMap[x][y] += 1;
		}

		st = new StringTokenizer(br.readLine());
		int x = Integer.parseInt(st.nextToken()) - 1;
		int y = Integer.parseInt(st.nextToken()) - 1;
		exit = new Point(x, y);

		for (int i = 0; i < K; i++) {
			move();
			if (goal == M)
				break;
			int[] ret = choice();
			rotate(ret);
		}
		System.out.println(total);
		System.out.println((exit.x + 1) + " " + (exit.y + 1));
	}

	public static void move() {
		people.clear();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (pMap[i][j] >= 1) {
					for (int k = 0; k < pMap[i][j]; k++) {
						people.add(new Point(i, j));
					}
				}
			}
		}
		for (int i = 0; i < people.size(); i++) {
			Point now = people.get(i);
			if (now.x == -1 && now.y == -1)
				continue;
			int dis = Math.abs(now.x - exit.x) + Math.abs(now.y - exit.y);
			int cx = -1, cy = -1;
			for (int j = 0; j < 4; j++) {
				int nx = now.x + dx[j];
				int ny = now.y + dy[j];
				if (!isRange(nx, ny)) {
					continue;
				}
				if (maze[nx][ny] > 0) {
					continue;
				}
				int tmpDis = Math.abs(nx - exit.x) + Math.abs(ny - exit.y);
				if (tmpDis < dis) {
					cx = nx;
					cy = ny;
					dis = tmpDis;
				}
			}

			if (cx == -1 && cy == -1)
				continue;
			else {
				total++;
				if (exit.x == cx && exit.y == cy) {
					pMap[now.x][now.y] -= 1;
					people.get(i).x = -1;
					people.get(i).y = -1;
					goal++;
				} else {
					pMap[now.x][now.y] -= 1;
					people.get(i).x = cx;
					people.get(i).y = cy;
					pMap[cx][cy] += 1;
				}
			}
		}
	}

	public static int[] choice() {
		for (int s = 2; s < N; s++) {
			for (int sx = 0; sx <= N - s; sx++) {
				for (int sy = 0; sy <= N - s; sy++) {
					if (sx <= exit.x && exit.x < sx + s && sy <= exit.y && exit.y < sy + s) {
						for (int i = sx; i < sx + s; i++) {
							for (int j = sy; j < sy + s; j++) {
								if (pMap[i][j] >= 1) {
									return new int[] { sx, sy, s };
								}
							}
						}
					}
				}
			}
		}
		return new int[] { 0, 0, N };
	}

	public static void rotate(int[] pos) {
		int sx = pos[0], sy = pos[1], s = pos[2];
		int[][] copyMap = new int[s][s];
		int[][] pCopyMap = new int[s][s];

		for (int i = 0; i < s; i++) {
			for (int j = 0; j < s; j++) {
				copyMap[j][s - 1 - i] = maze[sx + i][sy + j];
				pCopyMap[j][s - 1 - i] = pMap[sx + i][sy + j];
			}
		}

		for (int i = 0; i < s; i++) {
			for (int j = 0; j < s; j++) {
				maze[sx + i][sy + j] = copyMap[i][j] - 1;
				if (maze[i][j] < 0)
					maze[i][j] = 0;
				pMap[sx + i][sy + j] = pCopyMap[i][j];
			}
		}

		int localExitX = exit.x - sx;
		int localExitY = exit.y - sy;
		int rotatedExitX = localExitY;
		int rotatedExitY = s - 1 - localExitX;
		exit.x = sx + rotatedExitX;
		exit.y = sy + rotatedExitY;

	}

	public static boolean isRange(int x, int y) {
		return 0 <= x && x < N && 0 <= y && y < N;
	}
}