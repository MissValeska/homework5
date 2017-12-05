public class ColorNameHasher2 implements Hasher<Color> {
    @Override
    public int hash(Color elem) {
        int hash = 0;
        String str = elem.getName();
        for(int i = 0; i < str.length(); i++) {
            hash *= 37;
            hash += str.charAt(i) * Math.pow(37, i);
        }
        return -hash;
    }
}
