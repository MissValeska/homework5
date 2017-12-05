// Use the following data class:
public class Color
{
    public static final int MAX_COLOR_CODE = 0xFFFFFF;

    private int code=0;
    private String name="";

    public Color(){}

    public Color(int c, String n)
    {
        setCode(c);
        setName(n);
    }

    public boolean setCode(int c)
    {
        if( c < 0 || c > MAX_COLOR_CODE )
            return false;
        code = c;
        return true;
    }

    public boolean setName( String s )
    {
        if( s==null || s.length() == 0 )
            return false;
        name = s;
        return true;
    }

    public int getCode(){ return code; }

    public String getName(){ return name; }

    public String toString()
    {
        String decFormat = String.format("%06X", code);
        return "Color: " + name + " = " + decFormat;
    }

} // end class Color
