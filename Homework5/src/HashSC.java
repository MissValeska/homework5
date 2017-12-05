import java.util.*;

public class HashSC<E> extends HashTable<E> // CHANGE TO MAKE THIS A SUBCLASS OF HashTable for HW#5!!!!!!!!!!
{
    static final int INIT_TABLE_SIZE = 97;
    static final double INIT_MAX_LAMBDA = 1.5;

    protected LList<E>[] mLists;
    protected int mSize;
    protected int mTableSize;
    protected double mMaxLambda;


    public HashSC(int tableSize, Hasher<E> hash, Comparator<E> cmp) // ADD Comparator<E> and Hasher<E> parameters for HW#5!!!!!!!!
    {
        // Pass Comparator<E> and Hasher<E> parameters to the SUPERCLASS constructor for HW#5!!!!!!!!!!
        super(hash, cmp);

        if (tableSize < INIT_TABLE_SIZE)
            mTableSize = INIT_TABLE_SIZE;
        else
            mTableSize = nextPrime(tableSize);

        allocateMListArray();  // uses mTableSize;
        mMaxLambda = INIT_MAX_LAMBDA;
    }

    public HashSC(Hasher<E> hash, Comparator<E> cmp)// ADD Comparator<E> and Hasher<E> parameters for HW#5!!!!!!!!
    {
        this(INIT_TABLE_SIZE, hash, cmp); // FIX THIS (also pass Comparator<E> and Hasher<E>)
    }

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// DON'T FORGET TO OVERRIDE iterator() (YOU WRITE FOR HW#5)
//    just return a new instance of the HashQPIterator class below
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    @Override
    public Iterator<E> iterator() {
        return new HashSCIterator<E>(mLists);
    }

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// DON'T FORGET TO OVERRIDE displayTable() (YOU WRITE FOR HW#5)
// FOR EACH ARRAY ELEMENT...
//   if the linked list at that element is empty, don't display anything
//   otherwise, display the ARRAY INDEX AND the
//		data in each linked list node all on ONE line, BUT
//     	YOU MUST USE THE ITERATOR RETURNED FROM EACH LINKED LIST
//     	to retrieve each Node's data (YOU ARE NOT ALLOWED TO CALL getEntry)
//     	(see the test runs for examples)
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    @Override
    public void displayTable() {

        for(int i = 0; i < mLists.length; i++) {
            if(mLists[i].isEmpty()) {
                break;
            }
            else {
                System.out.print(i);
                Iterator<E> itr = mLists[i].iterator();
                while (itr.hasNext()) {
                    System.out.print(" " + itr.next().toString());
                }
                System.out.println();
            }
        }

    }

    public E getEntry(E target)
    {
        // FINISH THIS: should be like remove, (SO YOU USE A LinkedList's iterator
        //   AND Comparator), but return what the iterator returned if the comparator's compare
        //   method returns 0  OR null if not found

        LList<E> theList = mLists[myHash(target)];
        Iterator<E> iter = theList.iterator();
        E currElem;

        for(int i=0; iter.hasNext(); ++i )
        {
            currElem = iter.next();
            if(comparator.compare(currElem, target)==0)
            {
                return currElem;
            }
        }

        // not found
        return null;

    }

    public boolean contains( E x)
    {
        LList<E> theList = mLists[myHash(x)];
        Iterator<E> iter = theList.iterator();
        E currElem;

        for(int i=0; iter.hasNext(); ++i )
        {
            currElem = iter.next();
            if(comparator.compare(currElem, x)==0)
            {
                return true;
            }
        } // CHANGE AS INDICATED ON HW#5!!!!!!!!!!!!!!!!
        // replace with SEVERAL LINES (USE ITERATOR AND COMPARATOR)
        return false;
    }

    public void makeEmpty()
    {
        int k, size = mLists.length;

        for(k = 0; k < size; k++)
            mLists[k].clear();
        mSize = 0;
    }

    public boolean insert( E x)
    {
        LList<E> theList = mLists[myHash(x)];
        Iterator<E> iter = theList.iterator();
        E currElem;

        for(int i = 0; iter.hasNext(); i++) { // CHANGE AS INDICATED ON HW#5!!!!!!!!!
            // replace with SEVERAL LINES (USE ITERATOR AND COMPARATOR)
            currElem = iter.next();
            if(comparator.compare(currElem, x)==0)
            {
                return false;
            }
        }

        // not found so we insert
        theList.add(x);

        // ADD HERE: check and maybe UPDATE member counter variable
        if(theList.getLength() > 1) {
            System.out.println("Incremented!");
            numCollisions++;
        }

        // ADD HERE: possibly update longestCollisionRun variable
        //    which should be counting the longest linked list
        if(theList.getLength() > longestCollisionRun) {
            longestCollisionRun = theList.getLength();
        }

        // check load factor
        if( ++mSize > mMaxLambda * mTableSize )
            rehash();

        return true;
    }

    public boolean remove( E x)
    {
        LList<E> theList = mLists[myHash(x)];
        Iterator<E> iter = theList.iterator();
        E currElem;

        for(int i=0; iter.hasNext(); ++i )
        {
            currElem = iter.next();
            if(comparator.compare(currElem, x)==0)
            {
                theList.remove(i+1);
                --mSize;
                return true;
            }
        }

        // not found
        return false;
    }

    public int size()  { return mSize; }

    public boolean setMaxLambda( double lam )
    {
        if (lam < .1 || lam > 100.)
            return false;
        mMaxLambda = lam;
        return true;
    }

    public void displayStatistics()
    {
        System.out.println("\nIn the HashSC class:\n");
        System.out.println( "Table Size = " +  mTableSize );;
        System.out.println( "Number of entries = " + mSize);
        System.out.println( "Load factor = " + (double)mSize/mTableSize);
        System.out.println( "Number of collisions = " + this.numCollisions );
        System.out.println( "Longest Linked List = " + this.longestCollisionRun );
    }

    // protected methods of class ----------------------
    protected void rehash()
    {
        // ADD CODE HERE TO RESET THE HashTable COUNTERS TO 0 for HW#5!!!!!!!!!!!!!!!!
        numCollisions = 0;
        longestCollisionRun = 0;

        // we save old list and size then we can reallocate freely
        LList<E>[] oldLists = mLists;
        int k, oldTableSize = mTableSize;
        Iterator<E> iter;

        mTableSize = nextPrime(2*oldTableSize);

        // allocate a larger, empty array
        allocateMListArray();  // uses mTableSize;

        // use the insert() algorithm to re-enter old data
        mSize = 0;
        for(k = 0; k < oldTableSize; k++)
            for(iter = oldLists[k].iterator(); iter.hasNext() ; )
                insert( iter.next());
    }

    protected int myHash( E x)
    {
        int hashVal;

        hashVal = hasher.hash(x) % mTableSize; // CHANGE TO USE Hasher's hash method for HW#5!!!!!!!!!!!
        if(hashVal < 0)
            hashVal += mTableSize;

        return hashVal;
    }

    protected static int nextPrime(int n)
    {
        int k, candidate, loopLim;

        // loop doesn't work for 2 or 3
        if (n <= 2 )
            return 2;
        else if (n == 3)
            return 3;

        for (candidate = (n%2 == 0)? n+1 : n ; true ; candidate += 2)
        {
            // all primes > 3 are of the form 6k +/- 1
            loopLim = (int)( (Math.sqrt((double)candidate) + 1)/6 );

            // we know it is odd.  check for divisibility by 3
            if (candidate%3 == 0)
                continue;

            // now we can check for divisibility of 6k +/- 1 up to sqrt
            for (k = 1; k <= loopLim; k++)
            {
                if (candidate % (6*k - 1) == 0)
                    break;
                if (candidate % (6*k + 1) == 0)
                    break;
            }
            if (k > loopLim)
                return candidate;
        }
    }

    private  void allocateMListArray()
    {
        int k;

        mLists = new LList[mTableSize];
        for (k = 0; k < mTableSize; k++)
            mLists[k] = new LList<E>();
    }

//--------------------------------------------------------------------------------------
// WRITE THE HashSCIterator<E> class (inner class in HashSC, so it WON'T be public)
//     This class MUST implement Iterator<E>, AND include the following:
// 			private instance variables for LList<E>[] , an int (for current index),
//				AND an Iterator<E> (for the current linked list's iterator)
//			constructor with a LList<E>[] parameter in which you assign the parameter
//				the corresponding instance variable,
//				and the element[0]'s iterator to the Iterator instance variable
// Override methods:
//     	boolean hasNext()
//			This method MUST determine if the array has an LList that hasn't been
//				completely iterated AT or AFTER the current index
//				 (MUST advance the current index and assign Iterator
//				instance variable to the current index's Iterator if the current iterator
//				doesn't have any more (HINT for this will be given in class)
//		E next()
//			if hasNext() returns true, return the next() of the instance var. Iterator
//			else return false

   class HashSCIterator<E> implements Iterator<E> {

        private LList<E>[] mList;
        private int index;
        private Iterator<E> itr;

        public HashSCIterator(LList<E>[] list) {
            mList = list;
            itr = list[0].iterator();
        }

       @Override
       public boolean hasNext() {
           if(index < mList.length && itr.hasNext())
               return true;
           while(++index < mList.length) {
               if(mList[index].getLength() > 0) {
                   itr = mList[index].iterator();
                   return true;
               }
           }
           return false;
       }

       @Override
       public E next() {
            if(hasNext()) {
                System.out.println("index:" + index);
                return itr.next();
            }
           return null;
       }
   }

}
