public class ColorNameHasher1 implements Hasher<Color> {
    @Override
    public int hash(Color elem) {
        return elem.getName().hashCode();
    }
}
