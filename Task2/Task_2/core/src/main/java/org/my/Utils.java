package org.my;


import jarutils.StringUtils;

public class Utils {
   public static boolean  isAllPositiveNumbers(String... str) {
      boolean res = true;
      for (String s : str) {
         if(!StringUtils.isPositiveNumber(s))
            res = false;
      }
      return res;
   }
   }