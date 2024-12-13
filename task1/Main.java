package HW4.task1;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class Main {
    private static BigInteger factorial(int n) {
        if (n <= 1) return BigInteger.ONE;
        return IntStream.rangeClosed(1, n)
                .mapToObj(BigInteger::valueOf)
                .reduce(BigInteger.ONE, BigInteger::multiply);
    }
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        CompletableFuture<int[]> createArrayFuture = CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            int[] array = new int[10];
            for (int i = 0; i < 10; i++) {
                array[i] = (int) (Math.random() * 100);
            }
            long end = System.currentTimeMillis();
            System.out.println("Початковий масив: " + Arrays.toString(array));
            System.out.println("Час для створення масиву: " + (end - start) + " ms");
            return array;
        });

        CompletableFuture<int[]> incrementedArrayFuture = createArrayFuture.thenApplyAsync(array -> {
            long start = System.currentTimeMillis();
            int[] incrementedArray = Arrays.stream(array)
                    .map(x -> x + 5)
                    .toArray();
            long end = System.currentTimeMillis();
            System.out.println("Збільшений масив: " + Arrays.toString(incrementedArray));
            System.out.println("Час для збільшення елементів масиву: " + (end - start) + " ms");
            return incrementedArray;
        });

        CompletableFuture<Integer> sumInitialFuture = createArrayFuture.thenApplyAsync(array -> {
            long start = System.currentTimeMillis();
            int sum = Arrays.stream(array).sum();
            long end = System.currentTimeMillis();
            System.out.println("Сума початкового масиву: " + sum);
            System.out.println("Час для обчислення суми початкового масиву: " + (end - start) + " ms");
            return sum;
        });


        CompletableFuture<Integer> sumIncrementedFuture = incrementedArrayFuture.thenApplyAsync(array -> {
            long start = System.currentTimeMillis();
            int sum = Arrays.stream(array).sum();
            long end = System.currentTimeMillis();
            System.out.println("Сума збільшеного масиву: " + sum);
            System.out.println("Час для обчислення суми збільшеного масиву: " + (end - start) + " ms");
            return sum;
        });


        CompletableFuture<Integer> sumFuture = sumInitialFuture.thenCombineAsync(
                sumIncrementedFuture,
                (sumInitial, sumIncremented) -> {
                    long start = System.currentTimeMillis();
                    int totalSum = sumInitial + sumIncremented;
                    long end = System.currentTimeMillis();
                    System.out.println("Загальна сума масивів: " + totalSum);
                    System.out.println("Час комбінувати суми: " + (end - start) + " ms");
                    return totalSum;
                }
        );

        CompletableFuture<Void> factorialFuture = sumFuture.thenAcceptAsync(sum -> {
            long start = System.currentTimeMillis();
            BigInteger factorialResult = factorial(sum);
            long end = System.currentTimeMillis();
            System.out.println("Факторіал суми " + factorialResult);
            System.out.println("Час для обчислення факторіалу " + (end - start) + " ms");
        });

        factorialFuture.thenRunAsync(() -> {
            System.out.println("Всі завдання виконано.");
        });

        factorialFuture.join();

        long endTime = System.currentTimeMillis();
        System.out.println("Загальний час виконання: " + (endTime - startTime) + " ms");
    }
}
