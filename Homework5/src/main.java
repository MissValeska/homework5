import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class main {

    // Include the following methods in the main class:
//
// Remember to import the correct package for Scanner and Files

    public static Scanner userScanner = new Scanner(System.in);

    // opens a text file for input, returns a Scanner:
    public static Scanner openInputFile() {
        String filename;
        Scanner scanner = null;

        System.out.print("Enter the input filename: ");
        filename = userScanner.nextLine();
        File file = new File(filename);

        try {
            scanner = new Scanner(file);
        }// end try
        catch (FileNotFoundException fe) {
            System.out.println("Can't open input file\n");
            return null; // array of 0 elements
        } // end catch
        return scanner;
    }

    public static Color testContains(HashTable<Color> tableQP,
                                     HashTable<Color> tableSC,
                                     Color tempColor) {
        // YOU FINISH SO IT CREATES A COPY OF tempColor (new Color,
        //    NOT assigning the same reference, but another Color with
        //    the same code and name), then call contains for tableQP
        //    passing the copy Color and display the return value as
        //    shown in the test run.
        // Now call contains for tableSC (pass the same copy Color) and
        //    display the return value as shown in the test run.
        // Return the copy Color

        Color tmp = new Color(tempColor.getCode(), tempColor.getName());
        System.out.println("Result of calling contains for " + tmp.getName() + " in HashQP = " + tableQP.contains(tmp));
        System.out.println("Result of calling contains for " + tmp.getName() + " in HashSC = " + tableSC.contains(tmp));

        return tmp;

    }

// Call the following in main for both HashTables (in one call):

    public static void testHashTables(HashTable<Color> tableQP,
                                      HashTable<Color> tableSC) {
        Color tempColorQP, tempColorSC;
        Color targetColor1 = null, targetColor2 = null;


        Iterator<Color> iter = tableQP.iterator(); // get Color from HashQP
        if (iter.hasNext()) {
            tempColorQP = iter.next();
            targetColor1 = testContains(tableQP, tableSC, tempColorQP);// YOU WRITE
        }

        iter = tableSC.iterator(); // get Color from HashSC
        if (iter.hasNext()) {
            tempColorSC = iter.next();
            targetColor2 = testContains(tableQP, tableSC, tempColorSC);// YOU WRITE
        }
        // found a Color in table1 in table2, now test getEntry and remove methods

        tempColorQP = tableQP.getEntry(targetColor1);
        if (tempColorQP != null) {
            System.out.println("Retrieved in HashQP, Color: " + tempColorQP.getName() + ", now trying to delete it");
            // now delete it
            if (tableQP.remove(targetColor1))
                System.out.println("Successfully removed from HashQP: " + targetColor1.getName());
            else
                System.out.println("Unsuccessful attempt to remove from HashQP: " + targetColor1.getName());
        } else
            System.out.println("Error in HashQP: can't retrieve " + targetColor1.getName());

        tempColorSC = tableSC.getEntry(targetColor2);
        if (tempColorSC != null) {
            System.out.println("Retrieved in HashSC, Color: " + tempColorSC.getName() + ", now trying to delete it");
            // now delete it
            if (tableSC.remove(targetColor2))
                System.out.println("Successfully removed from HashSC: " + targetColor2.getName());
            else
                System.out.println("Unsuccessful attempt to remove from HashSC: " + targetColor2.getName());
        } else
            System.out.println("Error in HashSC: can't retrieve " + targetColor2.getName());

    } // testHashTables

    public static boolean fillHash(HashTable<Color> colorQP, HashTable<Color> colorSC1, HashTable<Color> colorSC2) {

        Scanner scn = openInputFile();

        if(scn == null) {
            return false;
        }

        while(scn.hasNext()) {

            Color clr = new Color(Integer.parseInt(scn.next(), 16), scn.nextLine().trim());
            colorQP.insert(clr);
            colorSC1.insert(clr);
            colorSC2.insert(clr);
        }

        scn.close();
        return true;

    }

    public static void displayHashes(HashTable<Color> hColor) {
        Iterator<Color> itr = hColor.iterator();
        while(itr.hasNext()) {
            Color tmp = itr.next();
            System.out.println(tmp.toString());
        }
        hColor.displayStatistics();

    }

    public static void main(String[] args) {
        HashTable<Color> colorQP = new HashQP<Color>(new ColorCodeHasher(), new ColorCodeComparator());
        HashTable<Color> colorSC1 = new HashSC<Color>(new ColorNameHasher1(), new ColorNameComparator());
        HashTable<Color> colorSC2 = new HashSC<Color>(new ColorNameHasher2(), new ColorNameComparator());

        if(!fillHash(colorQP, colorSC1, colorSC2)) {
            System.out.println("Ending program");
            System.exit(1);
        }

        System.out.println("HashQP with the int key has");
        displayHashes(colorQP);
        System.out.println("HashSC #1 with the String key has:");
        displayHashes(colorSC1);
        System.out.println("HashSC #2 with the String key has:");
        displayHashes(colorSC2);

        testHashTables(colorQP, colorSC1);
        System.out.println("HashQP with the int key now has");
        colorQP.displayTable();
        System.out.println("HashSC #1 with the String key now has:");
        colorSC1.displayTable();
        System.out.println("HashSC #2 with the String key now has:");
        colorSC2.displayTable();


    }


}
