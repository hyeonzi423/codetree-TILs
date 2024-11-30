import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
	static int N, M, K;
	static int[] dx = { -1, 0, 1, 0 };
	static int[] dy = { 0, 1, 0, -1 };
	static Person[] people; // 사람 정보
	static PriorityQueue<Integer>[][] gunMap; // 총 정보
	static int[][] peopleMap; // 사람 위치 확인용
	static int[] point; // 얻은 포인트

	public static class Person {
		int idx, x, y, d, s, g; // 인덱스, 위치, 방향, 초기 능력치, 총

		public Person(int idx, int x, int y, int d, int s, int g) {
			this.idx = idx;
			this.x = x;
			this.y = y;
			this.d = d;
			this.s = s;
			this.g = g;
		}

	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken()); // 격자의 크기
		M = Integer.parseInt(st.nextToken()); // 사람의 수
		K = Integer.parseInt(st.nextToken()); // 라운드 수

		gunMap = new PriorityQueue[N][N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				gunMap[i][j] = new PriorityQueue<>((a, b) -> b - a); // 큰 순으로 정렬되도록
				gunMap[i][j].add(Integer.parseInt(st.nextToken()));
			}
		}

		people = new Person[M + 1];
		peopleMap = new int[N][N];
		point = new int[M + 1];

		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			int d = Integer.parseInt(st.nextToken());
			int s = Integer.parseInt(st.nextToken());
			people[i + 1] = new Person(i + 1, x, y, d, s, gunMap[x][y].poll());
			peopleMap[x][y] = i + 1;
		}

		for (int i = 0; i < K; i++) {
			move();
		}

		for (int i = 1; i <= M; i++) {
			System.out.print(point[i] + " ");
		}

	}

	public static void move() {
		for (Person p : people) {
			if (p == null)
				continue;

			int nx = p.x + dx[p.d];
			int ny = p.y + dy[p.d];
			int nd = p.d;
			if (!isRange(nx, ny)) {
				nd = (p.d + 2) % 4;
				nx = p.x + dx[nd];
				ny = p.y + dy[nd];
			}

			if (peopleMap[nx][ny] == 0) {
				gunMap[nx][ny].add(p.g);
				peopleMap[p.x][p.y] = 0;
				peopleMap[nx][ny] = p.idx;
				people[p.idx] = new Person(p.idx, nx, ny, nd, p.s, gunMap[nx][ny].poll());
			}

			else {
				peopleMap[p.x][p.y] = 0;
				Person p1 = people[peopleMap[nx][ny]];
				Person p2 = new Person(p.idx, nx, ny, nd, p.s, p.g);
				int p1Score = p1.s + p1.g;
				int p2Score = p2.s + p2.g;

				if (p1Score > p2Score || ((p1Score == p2Score) && (p1.s > p2.s))) {
					gunMap[nx][ny].add(p2.g);
					for (int i = 0; i < 4; i++) {
						nd = (nd + i) % 4;
						nx += dx[nd];
						ny += dy[nd];
						if (isRange(nx, ny) && peopleMap[nx][ny] == 0) {
							break;
						}
					}
					peopleMap[nx][ny] = p.idx;
					people[p.idx] = new Person(p.idx, nx, ny, nd, p.s,
							gunMap[nx][ny].size() != 0 ? gunMap[nx][ny].poll() : 0);

					gunMap[nx][ny].add(p1.g);
					people[p1.idx].g = gunMap[nx][ny].poll();
					point[p1.idx] += p1Score - p2Score;

				}

				else {
					gunMap[nx][ny].add(p1.g);

					int px = nx, py = ny, pd = p1.d;
					for (int i = 0; i < 4; i++) {
						pd = (p1.d + i) % 4;
						px = nx + dx[pd];
						py = ny + dy[pd];
						if (isRange(px, py) && peopleMap[px][py] == 0) {
							break;
						}
					}

					peopleMap[px][py] = p1.idx;
					people[p1.idx] = new Person(p1.idx, px, py, pd, p1.s,
							gunMap[px][py].size() != 0 ? gunMap[px][py].poll() : 0);

					peopleMap[nx][ny] = p2.idx;
					gunMap[nx][ny].add(p2.g);
					people[p2.idx] = new Person(p2.idx, nx, ny, nd, p2.s,
							gunMap[nx][ny].size() != 0 ? gunMap[nx][ny].poll() : 0);
					point[p2.idx] += p2Score - p1Score;
				}

			}
		}
	}

	public static void print() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				System.out.print(peopleMap[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public static boolean isRange(int x, int y) {
		return 0 <= x && x < N && 0 <= y && y < N;
	}
}