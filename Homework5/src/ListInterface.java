/** An interface for the ADT list.
    Entries in a list have positions that begin with 1.

    @author Frank M. Carrano
    @author Timothy M. Henry
    @version 4.0

    Updated by C. Lee-Klawender 2015
*/
public interface ListInterface<T>
{
   /** Adds a new entry to the end of this list.
       Entries currently in the list are unaffected.
       The list's size is increased by 1.
       @param newEntry  The object to be added as a new entry. */
   public void add(T newEntry);

   /** Adds a new entry at a specified position within this list.
       Entries originally at and above the specified position
       are at the next higher position within the list.
       The list's size is increased by 1.
       @param newPosition  An integer that specifies the desired
                           position of the new entry.
       @param newEntry     The object to be added as a new entry.
       @return  true if newPosition is in range, false if
                newPosition < 1 or newPosition > getLength() + 1. */
   public boolean add(int newPosition, T newEntry);

   /** Removes the first occurrence of the parameter in this list.
       @param anEntry   The object to be removed from the list,
                        which is "found" if equals returns true
       @return      true if the parameter is found and removed, false otherwise
       @does nothing if either
                givenPosition < 1 or givenPosition > getLength(). */
   public boolean remove(T anEntry);

   /** Removes the entry at a given position from this list.
       Entries originally at positions higher than the given
       position are at the next lower position within the list,
       and the list's size is decreased by 1.
       @param givenPosition  An integer that indicates the position of
                             the entry to be removed.
       @return  A reference to the removed entry,
                OR null if givenPosition < 1 or givenPosition > getLength().
    */
   public T remove(int givenPosition);

   /** Removes all entries from this list. */
   public void clear();

   /** Retrieves the entry at a given position in this list.
       @param givenPosition  An integer that indicates the position of
                             the desired entry.
       @return  A reference to the indicated entry.
       @returns null if either
                givenPosition < 1 or givenPosition > getLength(). */
   public T getEntry(int givenPosition);

   /** Sees whether this list contains a given entry.
       @param anEntry  The object that is the desired entry.
       @return  True if the list contains anEntry, or false if not. */
   public boolean contains(T anEntry);

   /** Gets the length of this list.
       @return  The integer number of entries currently in the list. */
   public int getLength();

   /** Sees whether this list is empty.
       @return  True if the list is empty, or false if not. */
   public boolean isEmpty();

    /** Displays the list.
        */
   public void display();
} // end ListInterface
