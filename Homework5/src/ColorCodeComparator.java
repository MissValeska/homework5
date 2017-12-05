import java.util.Comparator;

public class ColorCodeComparator implements Comparator<Color> {
    @Override
    public int compare(Color o1, Color o2) {
        return o1.getCode() - o2.getCode();
    }
}
