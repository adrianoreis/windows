package com.oocode;

public class Main {
    public static void main(String[] args) throws Exception {
        if(args.length < 4){
            System.out.println("4 parameters: width height quantity model");
            System.exit(0);
        }
        Windows windows = new Windows(args);
        windows.invoke();
        System.out.println(windows.getServerMessage());
    }
}
