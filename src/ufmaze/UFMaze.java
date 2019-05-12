// Ryan Draper, TTh 4-5pm
package ufmaze;

import java.util.*;

public class UFMaze {

    private static int N;
    private static int M;
    private static int[][] maze;
    private static int[] onedarr;
    private static ArrayList walls = new ArrayList<Wall>();
    private static int count;

    public UFMaze(int n, int m) {
        N = n;
        M = m;
        maze = new int[N][M];
        onedarr = new int[N * M];
    }

    class Wall {

        int room1;
        int room2;

        public Wall(int r1, int r2) {
            room1 = r1;
            room2 = r2;
        }

        public int getRoom1() {
            return room1;
        }

        public int getRoom2() {
            return room2;
        }

        public String toString() {
            return room1 + " -> " + room2;
        }

        public boolean contains(int num) {
            return num == room1 || num == room2;
        }

        public boolean both(int num) {
            return num == room1 && num == room2;
        }
    }

    public void fillArrays() {
        int counter = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                maze[i][j] = counter;
                counter++;
            }
        }
        for (int i = 0; i < N * M; i++) {
            onedarr[i] = i;
        }
    }

    // precondition: N >= 2 and M >= 2
    public void makeWalls() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M - 1; j++) {
                walls.add(new Wall(maze[i][j], maze[i][j + 1]));
            }
        }
        for (int i = 0; i < N - 1; i++) {
            for (int j = 0; j < M; j++) {
                walls.add(new Wall(maze[i][j], maze[i + 1][j]));
            }
        }
    }

    public Wall removeWall(int one, int two) {
        for (int i = 0; i < walls.size(); i++) {
            if (((Wall) (walls.get(i))).getRoom1() == one && ((Wall) (walls.get(i))).getRoom2() == two) {
                return (Wall) walls.remove(i);
            }
        }
        return null;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    public int find(int p) {
        return onedarr[p];
    }

    public void union(int p, int q) {
        int pID = find(p);
        int qID = find(q);
        if (pID == qID) {
            return;
        }
        for (int i = 0; i < onedarr.length; i++) {
            if (onedarr[i] == pID) {
                onedarr[i] = qID;
            }
            count--;
        }
    }

    public static boolean isThereWall(int c) {
        for (int i = 0; i < walls.size(); i++) {
            if (((Wall) walls.get(i)).contains(c)
                    && onedarr[((Wall) walls.get(i)).getRoom1()] != onedarr[((Wall) walls.get(i)).getRoom2()]) {
                return true;
            }
        }
        return false;
    }

    public int next(int c) {
        while (true) {
            int rand = (int) (Math.random() * walls.size());
            if (((Wall) walls.get(rand)).contains(c)) {
                if (((Wall) walls.get(rand)).getRoom1() == c) {
                    return ((Wall) walls.get(rand)).getRoom2();
                } else {
                    return ((Wall) walls.get(rand)).getRoom1();
                }
            }
        }

    }

    public boolean allSame() {
        int num = onedarr[0];
        for (int i = 0; i < onedarr.length; i++) {
            if (onedarr[i] != num) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter a value for N: ");
        int n = s.nextInt();
        System.out.println("Enter a value for M: ");
        int m = s.nextInt();
        count = n * m - 1;
        UFMaze maze = new UFMaze(n, m);
        maze.fillArrays();
        maze.makeWalls();
        ArrayList pastCurrents = new ArrayList<>();
        int current = onedarr[0];
        while (!maze.allSame()) {
            int sub = 0;
            while (!maze.isThereWall(current)) {
                sub++;
                current = (int) pastCurrents.get(pastCurrents.size() - sub);
            }
            int p = current;
            int q = maze.next(p);
            if (maze.connected(p, q)) {
                continue;
            }
            maze.union(p, q);
            for (int i = 0; i < walls.size(); i++) {
                if (((Wall) walls.get(i)).contains(p)
                        && ((Wall) walls.get(i)).contains(q)) {
                    System.out.println("removing: " + walls.remove(i));
                }
            }
            pastCurrents.add(current);
            current = q;
        }
        for (int i = 0; i < onedarr.length; i++) {
            System.out.print(onedarr[i]);
        }
    }
}

/* SAMPLE OUTPUT (as seen on paper maze):
Enter a value for N: 
15
Enter a value for M: 
15
removing: 0 -> 1
removing: 1 -> 16
removing: 16 -> 17
removing: 2 -> 17
removing: 2 -> 3
removing: 3 -> 4
removing: 4 -> 19
removing: 19 -> 20
removing: 5 -> 20
removing: 5 -> 6
removing: 6 -> 7
removing: 7 -> 22
removing: 22 -> 37
removing: 36 -> 37
removing: 36 -> 51
removing: 50 -> 51
removing: 50 -> 65
removing: 65 -> 66
removing: 66 -> 81
removing: 81 -> 82
removing: 82 -> 83
removing: 83 -> 84
removing: 69 -> 84
removing: 54 -> 69
removing: 53 -> 54
removing: 52 -> 53
removing: 52 -> 67
removing: 67 -> 68
removing: 38 -> 53
removing: 38 -> 39
removing: 24 -> 39
removing: 24 -> 25
removing: 25 -> 26
removing: 26 -> 41
removing: 40 -> 41
removing: 40 -> 55
removing: 55 -> 56
removing: 56 -> 57
removing: 42 -> 57
removing: 42 -> 43
removing: 43 -> 58
removing: 58 -> 59
removing: 44 -> 59
removing: 29 -> 44
removing: 28 -> 29
removing: 27 -> 28
removing: 12 -> 27
removing: 12 -> 13
removing: 13 -> 14
removing: 11 -> 12
removing: 10 -> 11
removing: 9 -> 10
removing: 8 -> 9
removing: 8 -> 23
removing: 59 -> 74
removing: 74 -> 89
removing: 88 -> 89
removing: 87 -> 88
removing: 86 -> 87
removing: 85 -> 86
removing: 85 -> 100
removing: 99 -> 100
removing: 99 -> 114
removing: 113 -> 114
removing: 98 -> 113
removing: 97 -> 98
removing: 96 -> 97
removing: 95 -> 96
removing: 80 -> 95
removing: 79 -> 80
removing: 79 -> 94
removing: 93 -> 94
removing: 93 -> 108
removing: 108 -> 123
removing: 123 -> 124
removing: 109 -> 124
removing: 109 -> 110
removing: 110 -> 125
removing: 125 -> 140
removing: 139 -> 140
removing: 138 -> 139
removing: 137 -> 138
removing: 137 -> 152
removing: 152 -> 167
removing: 167 -> 182
removing: 182 -> 183
removing: 183 -> 198
removing: 198 -> 199
removing: 184 -> 199
removing: 184 -> 185
removing: 185 -> 200
removing: 200 -> 215
removing: 215 -> 216
removing: 201 -> 216
removing: 186 -> 201
removing: 171 -> 186
removing: 156 -> 171
removing: 155 -> 156
removing: 154 -> 155
removing: 154 -> 169
removing: 169 -> 170
removing: 168 -> 169
removing: 153 -> 168
removing: 141 -> 156
removing: 126 -> 141
removing: 111 -> 126
removing: 111 -> 112
removing: 112 -> 127
removing: 127 -> 142
removing: 142 -> 157
removing: 157 -> 172
removing: 172 -> 173
removing: 173 -> 174
removing: 159 -> 174
removing: 144 -> 159
removing: 129 -> 144
removing: 128 -> 129
removing: 128 -> 143
removing: 143 -> 158
removing: 129 -> 130
removing: 115 -> 130
removing: 115 -> 116
removing: 116 -> 117
removing: 117 -> 132
removing: 131 -> 132
removing: 131 -> 146
removing: 146 -> 161
removing: 161 -> 162
removing: 147 -> 162
removing: 147 -> 148
removing: 148 -> 149
removing: 134 -> 149
removing: 119 -> 134
removing: 118 -> 119
removing: 118 -> 133
removing: 103 -> 118
removing: 102 -> 103
removing: 101 -> 102
removing: 103 -> 104
removing: 149 -> 164
removing: 163 -> 164
removing: 163 -> 178
removing: 177 -> 178
removing: 176 -> 177
removing: 176 -> 191
removing: 190 -> 191
removing: 175 -> 190
removing: 160 -> 175
removing: 145 -> 160
removing: 190 -> 205
removing: 205 -> 220
removing: 219 -> 220
removing: 218 -> 219
removing: 217 -> 218
removing: 202 -> 217
removing: 202 -> 203
removing: 188 -> 203
removing: 187 -> 188
removing: 188 -> 189
removing: 189 -> 204
removing: 220 -> 221
removing: 206 -> 221
removing: 206 -> 207
removing: 207 -> 222
removing: 222 -> 223
removing: 208 -> 223
removing: 193 -> 208
removing: 193 -> 194
removing: 194 -> 209
removing: 209 -> 224
removing: 179 -> 194
removing: 192 -> 193
removing: 214 -> 215
removing: 213 -> 214
removing: 212 -> 213
removing: 211 -> 212
removing: 210 -> 211
removing: 195 -> 210
removing: 195 -> 196
removing: 181 -> 196
removing: 166 -> 181
removing: 151 -> 166
removing: 136 -> 151
removing: 121 -> 136
removing: 106 -> 121
removing: 105 -> 106
removing: 105 -> 120
removing: 120 -> 135
removing: 135 -> 150
removing: 150 -> 165
removing: 165 -> 180
removing: 90 -> 105
removing: 75 -> 90
removing: 60 -> 75
removing: 45 -> 60
removing: 30 -> 45
removing: 30 -> 31
removing: 31 -> 32
removing: 32 -> 47
removing: 46 -> 47
removing: 46 -> 61
removing: 61 -> 76
removing: 76 -> 77
removing: 77 -> 78
removing: 63 -> 78
removing: 62 -> 63
removing: 48 -> 63
removing: 48 -> 49
removing: 34 -> 49
removing: 33 -> 34
removing: 18 -> 33
removing: 34 -> 35
removing: 49 -> 64
removing: 77 -> 92
removing: 91 -> 92
removing: 92 -> 107
removing: 107 -> 122
removing: 15 -> 30
removing: 196 -> 197
removing: 70 -> 85
removing: 70 -> 71
removing: 71 -> 72
removing: 72 -> 73
removing: 21 -> 36
*/
