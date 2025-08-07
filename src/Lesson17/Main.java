package Lesson17;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int[][] map = generateRandomMap(70,70,0.33);
        printMap(map, "Исходная карта");
        int[][] mapForDFS = copyMap(map);
        int[][] mapForBFS = copyMap(map);
        DfsVoyager dfsVoyager = new DfsVoyager();
        BfsVoyager bfsVoyager = new BfsVoyager();
        int dfsIslands = dfsVoyager.lookupIslands(mapForDFS);
        int bfsIslands = bfsVoyager.lookupIslands(mapForBFS);
        System.out.println("Количество островов найденных алгоритмом DFS: " + dfsIslands);
        System.out.println("Количество островов найденных алгоритмом BFS: " + bfsIslands);
    }

    private static int[][] copyMap(int[][] original) {
        if (original == null) return null;
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    private static void printMap(int[][] map, String title) {
        System.out.println("\n" + title + ":");
        for (int[] row : map) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    public static int[][] generateRandomMap(int m, int n, double p) {
        Random random = new Random();
        int[][] map = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                double r = random.nextDouble();
                map[i][j] = (r < p) ? 1 : 2;
            }
        }
        return map;
    }
}