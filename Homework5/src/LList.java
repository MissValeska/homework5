import java.util.Iterator;

/**
   A class that implements the ADT list by using a chain of
   linked nodes that has a head reference. This version has an iterator.

   @author Frank M. Carrano
   @author Timothy M. Henry
   @version 4.0
   Updated by C. Lee-Klawender
*/
public class LList<T> implements ListInterface<T>
{
	private Node firstNode;            // Reference to first node of chain
	private int  numberOfEntries;

	public LList()
	{
		initializeDataFields();
	} // end default constructor

	public void clear()
	{
		initializeDataFields();
	} // end clear

	public void add(T newEntry) 	      // OutOfMemoryError possible
	{
		Node newNode = new Node(newEntry);

		if (isEmpty())
			firstNode = newNode;
		else                              // Add to end of non-empty list
		{
			Node lastNode = getNodeAt(numberOfEntries);
			lastNode.setNextNode(newNode); // Make last node reference new node
		} // end if

		numberOfEntries++;
	}  // end add

   public boolean add(int newPosition, T newEntry)
	{
 		if ((newPosition >= 1) && (newPosition <= numberOfEntries + 1))
		{
			Node newNode = new Node(newEntry);

			if (newPosition == 1)                  // Case 1
			{
				newNode.setNextNode(firstNode);
				firstNode = newNode;
			}
			else									         // Case 2: list is not empty
			{                                      // and newPosition > 1
            Node nodeBefore = getNodeAt(newPosition - 1);
            Node nodeAfter = nodeBefore.getNextNode();
				newNode.setNextNode(nodeAfter);
				nodeBefore.setNextNode(newNode);
			} // end if

			numberOfEntries++;
			return true;
		}
      else
            return false;
   } // end add

	public boolean remove(T anEntry)// YOU WRITE for Exercise 1.2
	{
		boolean found = false;
		Node currentNode = firstNode;
		Node prevNode = null;

		while (!found && (currentNode != null))
		{
			if (anEntry.equals(currentNode.getData()))
				found = true;
			else
			{
				prevNode = currentNode;
				currentNode = currentNode.getNextNode();
			}
		} // end while
		if( !found )
			return false;
		// must be found here, is it the first node?
		if( prevNode == null )
			firstNode = currentNode.getNextNode();
		else
			prevNode.setNextNode(currentNode.getNextNode());
		return true;
	}


	public T remove(int givenPosition)
	{
      T result = null;                           // Return value

      if ((givenPosition >= 1) && (givenPosition <= numberOfEntries))
      {
         if (givenPosition == 1)                 // Case 1: Remove first entry
         {
            result = firstNode.getData();        // Save entry to be removed
            firstNode = firstNode.getNextNode(); // Remove entry
         }
         else                                    // Case 2: Not first entry
         {
            Node nodeBefore = getNodeAt(givenPosition - 1);
            Node nodeToRemove = nodeBefore.getNextNode();
            result = nodeToRemove.getData();     // Save entry to be removed
            Node nodeAfter = nodeToRemove.getNextNode();
            nodeBefore.setNextNode(nodeAfter);   // Remove entry
         } // end if

         numberOfEntries--;                      // Update count
         return result;                          // Return removed entry
      }
      else
          return null;
	} // end remove


   public T getEntry(int givenPosition)
   {
		if ((givenPosition >= 1) && (givenPosition <= numberOfEntries))
		{
            return getNodeAt(givenPosition).getData();
     	}
      else
          return null;
   } // end getEntry


	public boolean contains(T anEntry)
	{
		boolean found = false;
		Node currentNode = firstNode;

		while (!found && (currentNode != null))
		{
			if (anEntry.equals(currentNode.getData()))
				found = true;
			else
				currentNode = currentNode.getNextNode();
		} // end while

		return found;
	} // end contains

   public int getLength()
   {
      return numberOfEntries;
   } // end getLength

   public boolean isEmpty()
   {
        return numberOfEntries==0;

   } // end isEmpty

   public void display()
   {
        Node currNode;

        currNode = firstNode;
        while( currNode != null )
        {
            System.out.println(currNode.getData());
            currNode = currNode.getNextNode();
        }
    } // end display
   
   public Iterator<T> iterator(){ return new LinkedListIterator(firstNode); }


   // Initializes the class's data fields to indicate an empty list.
   private void initializeDataFields()
   {
		firstNode = null;
		numberOfEntries = 0;
   } // end initializeDataFields

   // Returns a reference to the node at a given position.
   // Precondition: The chain is not empty;
   //               1 <= givenPosition <= numberOfEntries.
	private Node getNodeAt(int givenPosition)
	{
		if((1 <= givenPosition) && (givenPosition <= numberOfEntries))
		{
            Node currentNode = firstNode;

            // Traverse the chain to locate the desired node
            // (skipped if givenPosition is 1)
            for (int counter = 1; counter < givenPosition; counter++)
                currentNode = currentNode.getNextNode();

            return currentNode;
        }
        else
            return null;
	} // end getNodeAt

	private class Node
	{
      private T    data; // Entry in list
      private Node next; // Link to next node

      private Node(T dataPortion)
      {
         data = dataPortion;
         next = null;
      } // end constructor

      private Node(T dataPortion, Node nextNode)
      {
         data = dataPortion;
         next = nextNode;
      } // end constructor

      private T getData()
      {
         return data;
      } // end getData

      private void setData(T newData)
      {
         data = newData;
      } // end setData

      private Node getNextNode()
      {
         return next;
      } // end getNextNode

      private void setNextNode(Node nextNode)
      {
         next = nextNode;
      } // end setNextNode
	} // end Node
	
	
	class LinkedListIterator implements Iterator<T>
	{
		private Node currentNode;
		
		LinkedListIterator(Node first)
		{
			currentNode = first;
		}
		
		
		@Override
		public boolean hasNext() {
			return currentNode!=null;
		}

		@Override
		public T next() {
			if( currentNode!=null )
			{
				T currData = currentNode.getData();
				currentNode = currentNode.getNextNode();
				return currData;
			}
			return null;
		}

	} // end LinkedListIterator
	
} // end LList



