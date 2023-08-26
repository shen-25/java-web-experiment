package com.jingdong.manager.common;


import java.util.Arrays;
import java.util.List;

/**
 * @author word
 */
public class Constant {
    public static final String CURRENT_USER = "current_user";

    public static final String DEFAULT_PASSWORD = "123456";

    public static Integer DEFAULT_VALUE = 100;
    public static final List<String> STATIC_RESOURCES =
            Arrays.asList("css", "png", "jpg", "js", "woff2?v=256",
                    "ico", "ttf", "woff", "woff2");

    public static Integer tightProductNumber = 100;

    public interface UserState {
        public static int ONTHEJOB = 1;
        int QUITE = 2;
        int PROBATION = 3;
        int ALL = 0;
    }

        public interface MenuState {
            public static int All = -1;
            public static int NORMAL = 0;
            public static   int STOP = 1;
        }

        public interface ProductState {
            public static int All = -1;
            public static int NORMAL = 0;
            int STOP = 1;
        }

        public interface MenuType {
            public static int All = -1;
            public static int MENU = 0;
            int BUTTON = 1;
        }

    public interface ReplyStatus {
        public  static  int ALL = -1;
        public static int REPLIED = 0;
        public static int NO_REPLY = 1;
    }


    public interface  CommentStatus {
        public  static  int ALL = -1;
        public static int NORMAL = 0;
        public static int STOP = 1;
    }


    public interface  OrderStatus {
        public  static  int CANCELED = 0;

        public static int NOT_PAID = 1;
        public static  int PAID = 2;
        public static int DELIVERED = 3;
        public static int FINISHED = 4;
    }

    public interface OrderPaymentState {
        public static int UP_LINE = 0;
        public static int ON_LINE = 1;


    }

    public interface CartState {
        public static int NO_SELECTED = 0;

        public static int SELECTED =  1;


    }


}
