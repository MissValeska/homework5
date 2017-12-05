// Note that that Comparator<E> interface used below is in the Java library in java.util,
//    so import java.util.*; when you use the Comparator
// ALSO, the Iterator<E> interface you'll use is in java.util
//--------------------------------------------------------------------------------------
// HashTable interface definition:

import java.util.*;

public abstract class HashTable<E> {
    protected int numCollisions; // how many collisions since instantiating or rehashing
    protected int longestCollisionRun;// longest run for ONE entry or longest linked list
    protected Hasher<E> hasher;
    protected Comparator<E> comparator;

    public HashTable(Hasher<E> h, Comparator<E> c)
    {
        hasher = h;
        comparator = c;
    }

    public abstract E getEntry(E target);
    public abstract boolean contains( E x);
    public abstract void makeEmpty();
    public abstract boolean insert( E x);
    public abstract boolean remove( E x);
    public abstract int size();
    public abstract boolean setMaxLambda( double lam );
    public abstract Iterator<E> iterator();
    public abstract void displayTable();
    public abstract void displayStatistics();
}

