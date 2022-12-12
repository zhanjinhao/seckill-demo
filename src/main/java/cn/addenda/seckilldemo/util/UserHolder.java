package cn.addenda.seckilldemo.util;

public class UserHolder {

    private UserHolder() {

    }

    private static final ThreadLocal<String> tl = new ThreadLocal<>();

    public static void saveUser(String user) {
        tl.set(user);
    }

    public static String getUserId() {
        return tl.get();
    }

    public static void removeUserId() {
        tl.remove();
    }
}
