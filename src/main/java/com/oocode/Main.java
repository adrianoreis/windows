package com.oocode;

public class Main {
    public static void main(String[] args) throws Exception {
        Windows windows = new Windows(args);
        windows.invoke();
        System.out.println(windows.getServerMessage());
    }
}
