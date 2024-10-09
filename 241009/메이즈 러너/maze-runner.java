import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.awt.Point;

public class Main {

	static int N, M, K, ans;
	static int[][] wall, tmpWall;
	static ArrayList<Point> person, tmpPerson;
	static Point exit;
	static int[] dx = { -1, 1, 0, 0 };
	static int[] dy = { 0, 0, -1, 1 };

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		ans = 0;

		wall = new int[N][N];
		tmpWall = new int[N][N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				wall[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		person = new ArrayList<>();
		tmpPerson = new ArrayList<>();
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			person.add(new Point(x, y));
		}

		st = new StringTokenizer(br.readLine());
		int x = Integer.parseInt(st.nextToken()) - 1;
		int y = Integer.parseInt(st.nextToken()) - 1;
		exit = new Point(x, y);

		for (int i = 0; i < K; i++) {
			move();
			if (person.size() == 0) {
				break;
			}
			int[] ret = find();
			//System.out.println("find " + ret[0] + " " + ret[1] + " " + ret[2]);
			rotate(ret[0], ret[1], ret[2]);
			//System.out.println("exit " + exit.x + " " + exit.y);
			//print(wall);
			//System.out.println("-------------------------");
		}
		System.out.println(ans);
		System.out.println((exit.x + 1) + " " + (exit.y + 1));
	}

	public static void move() {
		tmpPerson.clear();

		for (Point p : person) {
			int dis = Math.abs(p.x - exit.x) + Math.abs(p.y - exit.y);
			int dir = -1;
			for (int i = 0; i < 4; i++) {
				int nx = p.x + dx[i];
				int ny = p.y + dy[i];
				if (isRange(nx, ny) && wall[nx][ny] == 0) {
					int tmpDis = Math.abs(nx - exit.x) + Math.abs(ny - exit.y);
					if (tmpDis == 0) {
						dis = 0;
						break;
					}
					if (dis > tmpDis) {
						dis = tmpDis;
						dir = i;
					}
				}
			}
			if (dis == 0) {
				ans++;
				continue;
			}
			if (dir != -1) {
				int nx = p.x + dx[dir];
				int ny = p.y + dy[dir];
				tmpPerson.add(new Point(nx, ny));
				ans++;
			} else {
				tmpPerson.add(p);
			}
		}
		person.clear();
		for (Point p : tmpPerson) {
			person.add(p);
//			System.out.println(p.x + " " + p.y);
		}
//		System.out.println();
	}

	public static int[] find() {
		for (int s = 1; s < N; s++) {
			for (int i = 0; i < N - s; i++) {
				for (int j = 0; j < N - s; j++) {
					boolean eFlag = false;
					boolean pFlag = false;
					if (i <= exit.x && exit.x < i + s && j <= exit.y && exit.y < j + s) {
						eFlag = true;
					}
					if (!eFlag)
						continue;
					for (Point p : person) {
						if (i <= p.x && p.x < i + s && j <= p.y && p.y < j + s) {
							pFlag = true;
							break;
						}
					}
					if (pFlag) {
						return new int[] { i, j, s };
					}
				}
			}
		}
		return new int[] { 0, 0, N };
	}

	public static void rotate(int i, int j, int s) {
		// 선택한 부분 영역을 tmpWall에 복사한 후 시계방향으로 회전
		for (int a = 0; a < s; a++) {
			for (int b = 0; b < s; b++) {
				tmpWall[i + b][j + s - 1 - a] = wall[i + a][j + b];
				if (tmpWall[i + b][j + s - 1 - a] >= 1) {
					tmpWall[i + b][j + s - 1 - a]--;
				}
			}
		}

		// 회전한 부분 영역을 다시 wall로 복사
		for (int a = 0; a < s; a++) {
			for (int b = 0; b < s; b++) {
				wall[i + a][j + b] = tmpWall[i + a][j + b];
			}
		}

		// 출구 좌표가 회전할 범위에 있을 경우 회전
		if (i <= exit.x && exit.x < i + s && j <= exit.y && exit.y < j + s) {
			int newX = exit.y - j;
			int newY = s - 1 - (exit.x - i);
			exit.x = i + newX;
			exit.y = j + newY;
		}

		// 사람 좌표가 회전할 범위에 있을 경우 회전
		for (Point p : person) {
			if (i <= p.x && p.x < i + s && j <= p.y && p.y < j + s) {
				int newX = p.y - j;
				int newY = s - 1 - (p.x - i);
				p.x = i + newX;
				p.y = j + newY;
			}
		}

//		for (Point p : person) {
//			System.out.println(p.x + " " + p.y);
//		}
	}

	public static boolean isRange(int x, int y) {
		return 0 <= x && x < N && 0 <= y && y < N;
	}

	public static void print(int[][] map) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();

	}
}