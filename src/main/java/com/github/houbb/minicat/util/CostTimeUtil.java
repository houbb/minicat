package com.github.houbb.minicat.util;

public class CostTimeUtil {

   public static long costTimeMock() {
       long start = System.currentTimeMillis();
        // 避免是 sleep 影响判断
        String text = "111111111111111111111111111111111111111";

        for(int i = 0; i < 10000000; i++) {
            String text1 = text+i;
            text1.matches("123[0-9]");
        }

        return System.currentTimeMillis() - start;
    }

    public static void main(String[] args) {
        System.out.println(costTimeMock());;
    }

}
