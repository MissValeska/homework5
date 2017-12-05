public class ColorCodeHasher implements Hasher<Color> {
    @Override
    public int hash(Color elem) {
        return elem.getCode();
    }
}
