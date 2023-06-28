package cn.cai.receivingCode.utils;

/**
 * 随机工具类
 */
public class RandomUtils {
    /**
     * 所有的选择
     */
    private static final String[] ALL_SELECT = {"1","2","3","4","5","6","7","8","9","0","a","b","c","d","e","f","g","h","i","g","k","l","m","n","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","G","K","L","M","N","U","V","W","X","Y","Z"};

    /**
     * 随机生成指定位数的字符串
     * @param len 指定的位数
     * @return 随机生成的位数
     */
    public static String randomNum(Integer len){
        StringBuilder strRandom = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int index = (int) (Math.random() * ALL_SELECT.length);
            strRandom.append(ALL_SELECT[index]);
        }
        return strRandom.toString();
    }
}
