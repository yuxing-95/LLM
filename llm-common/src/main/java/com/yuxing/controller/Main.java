package com.yuxing.controller;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] heights = new int[n];
        for (int i = 0; i < n; i++) {
            heights[i] = sc.nextInt();
        }

        Deque<Integer> stack = new ArrayDeque<>();
        long total = 0;

        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && heights[stack.peek()] < heights[i]) {
                stack.pop();
            }

            if (stack.isEmpty()) {
                total += (n - 1 - i);
            } else {
                total += (stack.peek() - i);
            }

            stack.push(i);
        }

        System.out.println(total);
    }
}