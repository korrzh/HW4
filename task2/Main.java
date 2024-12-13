package HW4.task2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException{
        CompletableFuture<Void> runAsyncFuture = CompletableFuture.runAsync(() -> {
            System.out.println("Старт обробки масиву в асинхронному режимі...");
        });
        long startTime = System.currentTimeMillis();
        CompletableFuture<int[]> createArrayFuture = CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            int[] array = new int[20];
            for (int i = 0; i < 20; i++) {
                array[i] = (int) (Math.random() * 1000);
            }
            long end = System.currentTimeMillis();
            System.out.println("Початковий масив: " + Arrays.toString(array));
            System.out.println("Час для створення масиву: " + (end - start) + " ms");
            return array;
        });

        CompletableFuture<List<Integer>> adjacentSumsFuture = createArrayFuture.thenApplyAsync(array -> {
            long start = System.currentTimeMillis();
            List<Integer> adjacentSums = new ArrayList<>();
            for (int i = 0; i < array.length - 1; i++) {
                adjacentSums.add(array[i] + array[i + 1]);
            }
            long end = System.currentTimeMillis();
            System.out.println("Суміжні суми: " + adjacentSums);
            System.out.println("Час для підрахунку суми суміжних елементів: " + (end - start) + " ms");
            return adjacentSums;
        });

        CompletableFuture<Void> findMinSumFuture = adjacentSumsFuture.thenApplyAsync(sums -> {
            long start = System.currentTimeMillis();
            int minSum = Integer.MAX_VALUE;
            for (int sum : sums) {
                minSum = Math.min(minSum, sum);
            }
            long end = System.currentTimeMillis();
            System.out.println("Мінімальна сума суміжних елементів: " + minSum);
            System.out.println("Час для підрахунку мінімальної суми: " + (end - start) + " ms");
            return minSum;
        }).thenRunAsync(() -> {
            System.out.println("Обробка масиву завершена.");
        });

        runAsyncFuture.get();
        findMinSumFuture.get();

        CompletableFuture.runAsync(() -> {
            long totalTime = System.currentTimeMillis() - startTime;
            System.out.println("Загальний час виконання: " + totalTime + " ms");
        });
    }
}
