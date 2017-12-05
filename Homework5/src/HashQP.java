// HashQP Class Definition
import java.util.*;

// HashQP class ------------------------
public class HashQP<E> extends HashTable<E> // CHANGE TO MAKE THIS A SUBCLASS OF HashTable for HW#5!!!!!!!!!!
{
    protected static final int ACTIVE = 0;
    protected static final int EMPTY = 1;
    protected static final int DELETED = 2;

    static final int INIT_TABLE_SIZE = 97;
    static final double INIT_MAX_LAMBDA = 0.49;

    protected HashEntry<E>[] mArray;
    protected int mSize;
    protected int mLoadSize;
    protected int mTableSize;
    protected double mMaxLambda;

    // public methods ---------------------------------
    public HashQP(int tableSize, Hasher<E> hash, Comparator<E> cmp) // ADD Comparator<E> and Hasher<E> parameters for HW#5!!!!!!!!
    {
        // PASS corresponding parameters to Comparator<E> and Hasher<E> parameters of the superclass constructor!!!!!
        super(hash, cmp);

        mLoadSize = mSize = 0;
        if (tableSize < INIT_TABLE_SIZE)
            mTableSize = INIT_TABLE_SIZE;
        else
            mTableSize = nextPrime(tableSize);

        allocateArray();  // uses mTableSize;
        mMaxLambda = INIT_MAX_LAMBDA;
    }

    public HashQP(Hasher<E> hash, Comparator<E> cmp)// ADD Comparator<E> and Hasher<E> parameters for HW#5!!!!!!!!
    {
        this(INIT_TABLE_SIZE, hash, cmp); // FIX THIS (also pass Comparator<E> and Hasher<E>)
    }

    public boolean insert( E x)
    {
        int bucket = findPos(x);

        if ( mArray[bucket].state == ACTIVE )
            return false;

        mArray[bucket].data = x;
        mArray[bucket].state = ACTIVE;
        mSize++;

        // check load factor
        if( ++mLoadSize > mMaxLambda * mTableSize )
            rehash();

        return true;
    }

    public boolean remove( E x )
    {
        int bucket = findPos(x);

        if ( mArray[bucket].state != ACTIVE )
            return false;

        mArray[bucket].state = DELETED;
        mSize--; // mLoadSize not dec'd because it counts any non-EMP location
        return true;
    }

    public boolean contains(E x )
    {
        return mArray[findPos(x)].state == ACTIVE;
    }

    public int size()  { return mSize; }

    public void makeEmpty()
    {
        int k, size = mArray.length;

        for(k = 0; k < size; k++)
            mArray[k].state = EMPTY;
        mSize = mLoadSize = 0;
    }

    public boolean setMaxLambda( double lam )
    {
        if (lam < .1 || lam > INIT_MAX_LAMBDA )
            return false;
        mMaxLambda = lam;
        return true;
    }

    public void displayStatistics() // NEW WITH HW#5 (you'll call this in main)
    {
        System.out.println("\nIn the HashQP class:\n");
        System.out.println( "Table Size = " +  mTableSize );;
        System.out.println( "Number of entries = " + mSize);
        System.out.println( "Load factor = " + (double)mSize/mTableSize);
        System.out.println( "Number of collisions = " + this.numCollisions );
        System.out.println( "Longest Collision Run = " + this.longestCollisionRun );
    }

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// DON'T FORGET TO OVERRIDE iterator() (YOU WRITE FOR HW#5)
//    just return a new instance of the HashQPIterator class below
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    @Override
    public Iterator<E> iterator() {
        return new HashQPIterator<>(mArray);
    }

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// DON'T FORGET TO OVERRIDE displayTable() (YOU WRITE FOR HW#5)
// FOR EACH ARRAY ELEMENT...
//      if the element isn't ACTIVE, don't display anything
//      if the element IS ACTIVE, display the ARRAY INDEX and the
//			 data of the entry (toString)
//      (see the test runs for examples)
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    @Override
    public void displayTable() {

        for(int i = 0; i < size(); i++) {
            if(mArray[i].state == ACTIVE) {
                System.out.println(i + " " + mArray[i].data.toString());
            }
        }

    }

    public E getEntry(E target)
    {
        // FINISH THIS (should be like remove, but return
        //   the data from the ACTIVE found entry OR null if not found
        int bucket = findPos(target);

        if ( mArray[bucket].state == ACTIVE )
            return mArray[bucket].data;

        return null;
    }

    // protected methods of class ----------------------

    protected int findPos( E x )
    {
        int kthOddNum = 1;
        int index = myHash(x);
        int oldIndex = index;
        int tmp = 0;

        while ( mArray[index].state != EMPTY
                && comparator.compare(mArray[index].data, x) != 0) // CHANGE TO USE Comparator's compare for HW#5!!!!!!!!!!!
        {
            index += kthOddNum; // k squared = (k-1) squared + kth odd #
            kthOddNum += 2;     // compute next odd #
            if ( index >= mTableSize )
                index -= mTableSize;
            ++numCollisions; // **************** FOR EX. 8.2 **********************
            // ADD HERE: update local counter variable for HW#5!!!
            tmp++;
        }
        // ADD HERE: maybe update longestCollisionRun variable for HW#5!!!!!!!
        if(tmp > longestCollisionRun) {
            longestCollisionRun = tmp;
        }

        return index;
    }

    protected void rehash()
    {
        numCollisions = 0; // **************** FOR EX. 8.2 **********************
        // ADD CODE HERE TO RESET THE HashTable longestCollisionRun TO 0 for HW#5!!!!
        longestCollisionRun = 0;

        // we save old list and size then we can reallocate freely
        HashEntry<E>[] oldArray = mArray;
        int k, oldTableSize = mTableSize;;

        mTableSize = nextPrime(2*oldTableSize);

        // allocate a larger, empty array
        allocateArray();  // uses mTableSize;

        // use the insert() algorithm to re-enter old data
        mSize = mLoadSize = 0;
        for(k = 0; k < oldTableSize; k++)
            if (oldArray[k].state == ACTIVE)
                insert( oldArray[k].data );
    }

    protected int getNumCollisions(){ return numCollisions; }// **************** FOR EX. 8.2 **********************

    protected int myHash(E x)
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

    void allocateArray()
    {
        int k;

        mArray = new HashEntry[mTableSize];
        for (k = 0; k < mTableSize; k++)
            mArray[k] = new HashEntry<E>();
    }

    // INNER CLASS: HashEntry, used ONLY internally in HashQP---------------------
    class HashEntry<E>
    {
        public E data;
        public int state;

        public HashEntry( E x, int st )
        {
            data = x;
            state = st;
        }

        public HashEntry()
        {
            this(null, HashQP.EMPTY);
        }

    }
//--------------------------------------------------------------------------------------
// WRITE THE HashQPIterator<E> class (inner class in HashQP, so it WON'T be public)
//     This class MUST implement Iterator<E>, AND include the following:
// 			private instance variable for HashEntry<E>[] , and an int (for current index)
//			constructor with a HashEntry<E>[] parameter in which you assign the parameter
//				the corresponding instance variable
// Override methods:
//     	boolean hasNext()
//			find the next current index (inside the array) where the HashEntry's state
//				is ACTIVE (loop)
//			if the current index is < array's length return true
//			otherwise return false
//		E next()
//			if hasNext() returns true, return the data at the current index's HashEntry
//				(and increment the current index before returning)
//			else return false

    class HashQPIterator<E> implements Iterator<E> {

        private HashEntry<E>[] hEntry;
        private int index = 0;

        public HashQPIterator(HashEntry<E>[] hEntry) {
            this.hEntry = hEntry;
        }

        @Override
        public boolean hasNext() {

            for(int i = index; i < hEntry.length; i++) {
                if(hEntry[i].state == ACTIVE) {
                    if(i < hEntry.length) {
                        index = i;
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            }
            return false;
        }

        @Override
        public E next() {
            if(hasNext()) {
                E tmp = hEntry[index].data;
                index++;
                return tmp;
            }
            else {

                return null;
            }
        }
    }

} // end HashQP class
