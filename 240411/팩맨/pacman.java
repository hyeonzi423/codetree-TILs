import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Main {
	static int M, T; // 몬스터의 수, 턴 수
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, -1, 0, 1};
	static int[] mdx = {-1, -1, 0, 1, 1, 1, 0, -1};
	static int[] mdy = {0, -1, -1, -1, 0, 1, 1, 1};
	static ArrayList<Integer>[][] map, moveMap, deadMap; // 몬스터를 저장하는 맵
	static ArrayList<Egg> eggs; // 부화를 기다리는 알의 정보를 저장
	static Point pacman;
	static int eatMonster = 0;
	static int[] track = new int[3];
			
	static int[] target = { 0, 1, 2, 3 };
	static int[] result = new int[3];
	
	static class Egg{
		int x, y, d;

		public Egg(int x, int y, int d) {
			this.x = x;
			this.y = y;
			this.d = d;
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		M = Integer.parseInt(st.nextToken());
		T = Integer.parseInt(st.nextToken());
		
		st = new StringTokenizer(br.readLine());
		int x = Integer.parseInt(st.nextToken()) - 1;
		int y = Integer.parseInt(st.nextToken()) - 1; 
		pacman = new Point(x, y);
		
		map = new ArrayList[4][4];
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				map[i][j] = new ArrayList<>();
			}
		}
		
		deadMap = new ArrayList[4][4];
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				deadMap[i][j] = new ArrayList<>();
			}
		}
		
		for(int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			x = Integer.parseInt(st.nextToken()) - 1;
			y = Integer.parseInt(st.nextToken()) - 1; 
			int d = Integer.parseInt(st.nextToken()) - 1; 
			map[x][y].add(d);
		}
		
		for(int i = 0; i < T; i++) {
			copyMonster();
			moveMonster();
//			System.out.println("몬스터 이동 후");
//			print();
			eatMonster = -1;
			track = new int[3];
			perm(0);
//			System.out.println("팩맨 이동 경로");
//			System.out.println(Arrays.toString(track));
			realMovePM();
//			System.out.println("팩맨 이동 후 map");
//			print();
//			System.out.println(pacman);
			removeDead();
			bornMonster();
//			System.out.println("새로 부화한 후");
//			print();
		}
		
		int ans = 0;
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				ans += map[i][j].size();
			}
		}
		System.out.println(ans);
	}
	
	public static void copyMonster() { // 1. 몬스터 복제 시도
		eggs = new ArrayList<>();
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				if(map[i][j].size() == 0) continue;
				for(int d : map[i][j]) {
					eggs.add(new Egg(i, j, d));
				}
			}
		}
	}
	
	public static void moveMonster() { // 몬스터 이동
		moveMap = new ArrayList[4][4];
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				moveMap[i][j] = new ArrayList<>();
			}
		}
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				if(map[i][j].size() == 0) continue;
				for(int d : map[i][j]) {
					//System.out.println("origin " + i + " " + j + " " + d);
					int nx = i + mdx[d];
					int ny = j + mdy[d];
					if((pacman.x == nx && pacman.y == ny) || !inRange(nx, ny) || deadMap[nx][ny].size() != 0) {
						int cnt = 0;
						int originD = d;
						while(true) {
							d += 1;
							d = (d + 8)%8;
							cnt += 1;
							nx = i + mdx[d];
							ny = j + mdy[d];
							if((pacman.x != nx || pacman.y != ny) && inRange(nx, ny) && deadMap[nx][ny].size()== 0) {
								break;
							}
							if(cnt == 9) {
								d = originD;
								break;
							}
						}
					}
					
					nx = i + mdx[d];
					ny = j + mdy[d];
					if(!inRange(nx, ny)) continue;
					moveMap[nx][ny].add(d);
				}
			}
		}
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				map[i][j] = new ArrayList<>();
				map[i][j].addAll(moveMap[i][j]);
			}
		}
	}
	
	public static int movePacman() {
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				moveMap[i][j].clear();
				moveMap[i][j].addAll(map[i][j]);
			}
		}
		int eat = 0;
		Point tmpPM = new Point(pacman.x, pacman.y);
		for(int i = 0; i < 3; i++) {
			int nx = tmpPM.x + dx[result[i]];
			int ny = tmpPM.y + dy[result[i]];
			
			if(!inRange(nx, ny)) return -1;
			eat += moveMap[nx][ny].size();
			moveMap[nx][ny].clear();
			tmpPM.x = nx;
			tmpPM.y = ny;
			
		}
		return eat;
	}
	
	
	public static void perm(int cnt) {
		if (cnt == 3) {
			int ret = movePacman();
//			System.out.print(ret + " ");
//			System.out.println(Arrays.toString(result));
			if(eatMonster < ret) {
				for(int  i = 0; i < 3; i++) {
					track[i] = result[i];
				}
				eatMonster = ret;
			}
			return;
		}
		
		for (int i = 0; i < 4; i++) {
			result[cnt] = target[i];
			perm(cnt + 1);
		}
	}
	
	public static boolean inRange(int x, int y) {
		return 0 <= x && x < 4 && 0 <= y && y < 4;
	}
	
	public static void print() {
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				System.out.print(i + " " + j + " ");
				for(int d : map[i][j]) {
					System.out.print(d + " ");
				}
				System.out.println();
			}
		}
	}
	
	public static void realMovePM() {
		for(int i = 0; i < 3; i++) {
			int nx = pacman.x + dx[track[i]];
			int ny = pacman.y + dy[track[i]];
			pacman.x = nx;
			pacman.y = ny;
			for(int j = 0; j < map[pacman.x][pacman.y].size(); j++) {
				deadMap[pacman.x][pacman.y].add(3);
			}
			//System.out.println(pacman);
			map[pacman.x][pacman.y].clear();
		}
	}
	
	public static void removeDead() {
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				if(deadMap[i][j].size() == 0) continue;
				ArrayList<Integer> tmp = new ArrayList<>();
				for(int k = 0; k < deadMap[i][j].size(); k++) {
					if(deadMap[i][j].get(k) - 1 > 0) {
						tmp.add(deadMap[i][j].get(k) - 1);
					}
				}
				deadMap[i][j].clear();
				deadMap[i][j].addAll(tmp);
			}
		}
	}
	
	public static void bornMonster() {
		for(Egg e : eggs) {
			map[e.x][e.y].add(e.d);
		}
	}
}