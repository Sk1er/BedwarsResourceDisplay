package club.sk1er.mods.bedwars;

/**
 * Created by Mitchell Katz on 5/25/2017.
 */
public class NumberUtil {
    public static double round(double in, double places) {
//        return in;
        return Math.round(in* 10.0*places)/ (10*places);
    }

}
