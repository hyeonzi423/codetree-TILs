import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;


public class Main {
    static int N, M, K, ans = 0; // 격자 크기, 사람의 수, 기간
    static int map[][], tmpMap[][];
    static Point[] people;
    static Point exit;
    static int startX, startY, squareSize;
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        
        map = new int[N][N];
        tmpMap = new int[N][N];
        for(int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        
        people = new Point[M+1];
        for(int i = 1; i <= M; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken()) -1;
            int y = Integer.parseInt(st.nextToken()) -1;
            people[i] = new Point(x, y);
        }
        
        st = new StringTokenizer(br.readLine());
        int x = Integer.parseInt(st.nextToken()) -1;
        int y = Integer.parseInt(st.nextToken()) -1;
        exit = new Point(x, y);
        
        for(int i = 0; i < K; i++) {
        	move();
        	
        	boolean flag = true;
        	for(int p = 1; p <= M; p++) {
        		if(people[p].x != exit.x || people[p].y != exit.y) {
        			flag = false;
        		}
        	}
        	
        	if(flag) break;
        	
        	findSquare();
        	rotate();
        	rotate2();
        	//print();
        }
        System.out.println(ans);
        System.out.println((exit.x +1)+ " " + (exit.y+1));
    }

    
    public static void move() {
        for(int i = 1; i <= M; i++) {
            Point p = people[i];
            if(p.x == exit.x && p.y == exit.y) continue;
            
            if(p.x != exit.x) {
            	int nx = p.x;
            	int ny = p.y;
            	
            	if(exit.x > nx) nx++;
            	else nx--;
            	
            	if(map[nx][ny] == 0) {
            		p.x = nx;
            		p.y = ny;
            		ans++;
            		continue;
            	}
            }
            
            if(p.y != exit.y) {
            	int nx = p.x;
            	int ny = p.y;
            	
            	if(exit.y > ny) ny++;
            	else ny--;
            	
            	if(map[nx][ny] == 0) {
            		p.x = nx;
            		p.y = ny;
            		ans++;
            		continue;
            	}
            }
        }
    }
    
    public static void findSquare() {
    	for(int size = 2; size <= N; size++) {
    		for(int sx = 0; sx < N-size+1; sx++) {
    			for(int sy = 0; sy < N-size+1; sy++) {
    				int ex = sx + size -1;
    				int ey = sy + size -1;
    				
    				if(!(sx <= exit.x && exit.x <= ex && sy <= exit.y && exit.y <= ey))
    					continue;
    				
    				boolean flag = false;
    				for(int p = 1; p <= M; p++) {
    					if(sx <= people[p].x && people[p].x <= ex && sy <= people[p].y && people[p].y <= ey) {
                            if(!(people[p].x == exit.x && people[p].y == exit.y)) {
                                flag = true;
                            }
                        }
    				}
    				
    				if(flag) {
    					startX = sx;
    					startY = sy;
    					squareSize = size;
    					return;
    				}
    			}
    		}
    	}
    }
    
    public static void rotate() {
    	for(int x = startX; x < startX + squareSize; x++) {
            for(int y = startY; y < startY + squareSize; y++) {
                if(map[x][y] > 0) map[x][y]--;
            }
    	}
    	for(int x = startX; x < startX + squareSize; x++) {
            for(int y = startY; y < startY + squareSize; y++) {
                int ox = x - startX, oy = y - startY;
                int rx = oy, ry = squareSize - ox - 1;
                tmpMap[rx + startX][ry + startY] = map[x][y];
            }
    	}
    
        for(int x = startX; x < startX + squareSize; x++) {
            for(int y = startY; y < startY + squareSize; y++) {
                map[x][y] = tmpMap[x][y];
            }
        }
    }
    
    public static void rotate2() {
    	for(int i = 1; i <= M; i++) {
    		int x = people[i].x;
    		int y = people[i].y;
    		
    		if(startX <= x && x < startX + squareSize && startY <= y && y < startY + squareSize) {
    			int ox = x - startX, oy = y - startY;
                int rx = oy, ry = squareSize - ox - 1;
                people[i].x = rx + startX;
                people[i].y = ry + startY;
    		}
    	}
    	
    	int x = exit.x;
        int y = exit.y;
        if(startX <= x && x < startX + squareSize && startY <= y && y < startY + squareSize) {
            int ox = x - startX, oy = y - startY;
            int rx = oy, ry = squareSize - ox - 1;
            exit.x = rx + startX;
            exit.y = ry + startY;
        }
    }
    
    public static void print() {
    	for(int i = 0; i < N; i++) {
    		System.out.println(Arrays.toString(map[i]));
    	}
    	System.out.println(exit);
    	for(Point p : people) {
    		System.out.println(p);
    	}
    	    	
    }
}