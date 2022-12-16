/* SizelessCircularQueue.java
 * Written by: Nathan Todzy
 * Date: 2022/12/16
 */
import java.lang.reflect.Field;

/**
 * This class implements a circular queue using an array.
 * The queue is sizeless, meaning that it does not track the number of elements.
 * @param <T>
 *
 * @author Nathan Todzy
 */
public class SizelessCircularQueue<T> {

  private Object[] queue; // Array to hold the queue
  private int front; // Index of the front of the queue
  private int rear; // Index of the rear of the queue

  /**
   * Constructor for the CircularQueueADT class
   * @param size size of the queue
   * @throws IllegalArgumentException if size is less than 1
   */

  public SizelessCircularQueue(int size) {
    if (size < 1) {
      throw new IllegalArgumentException("Size must be greater than 0");
    }

    queue = new Object[size];
    front = -1;
    rear = -1;
  }

  /**
   * Checks if the queue is empty or not
   * @return true if the queue is empty, false otherwise
   */

  public boolean isEmpty() {
    return front == -1 && rear == -1;
  }

  /**
   * Checks if the queue is full or not
   */
  public boolean isFull() {
    // is there a better way to do this?
    for (Object o : queue) {
      if (o == null) {
        return false;
      }
    }

    return true;
  }
  /**
   * Adds an element to the queue
   * @param element The element to be added to be added to the queue
   * @throws IllegalStateException if the queue is full
   * @throws IllegalArgumentException if the element is null
   */

  public void enqueue(T element) {
    if (element == null) {
      throw new IllegalArgumentException("Element cannot be null");
    }

    if ( !isEmpty() && isFull()) {
      throw new IllegalStateException("Queue is full");
    }

    if (isEmpty()) {
      front = 0;
      rear = 0;
    } else {
      int next = (rear + 1) % queue.length;

      // dont overwrite the front
      if (next == front) {
        throw new IllegalStateException("Queue is full");
      }

      rear = next;
    }

    // add the element
    queue[rear] = element;
  }

  /**
   * Removes an element from the queue
   * @return The element removed from the queue, or null if the queue is empty
   */

  public T dequeue() {
    if (isEmpty()) {
      return null;
    }

    // get the element and remove it from the queue
    T element = (T) queue[front];
    queue[front] = null;

    if (front == rear) {
      front = -1;
      rear = -1;
    } else {
      front = (front + 1) % queue.length;
    }

    return element;
  }

  /**
   * Returns the element at the front of the queue
   */

  public T peek() {
    if (isEmpty()) {
      return null;
    }

    return (T) queue[front];
  }
}

/**
 * This class tests the SizelessCircularQueue class
 */
class Tester {

  private static boolean testConstructor() {
    boolean flag = true;

    // 1. test with a size of 0, should throw an IllegalArgumentException
    {
      try {
        SizelessCircularQueue<Integer> queue = new SizelessCircularQueue<>(0);
        System.err.println("(1) Should have thrown an IllegalArgumentException");
        flag = false;
      } catch (IllegalArgumentException e) {
        // expected
      } catch (Exception e) {
        System.err.println("(1) Unexpected exception: " + e);
        flag = false;
      }
    }

    // 2. test with a size of 1, should not throw an exception
    {
      try {
        SizelessCircularQueue<Integer> queue = new SizelessCircularQueue<>(1);
      } catch (Exception e) {
        System.err.println("(2) Unexpected exception: " + e);
        flag = false;
      }
    }

    // 3. Check that the queue is empty and not full
    {
      SizelessCircularQueue<Integer> queue = new SizelessCircularQueue<>(1);
      if (!queue.isEmpty()) {
        System.err.println("(3) Queue should be empty");
        flag = false;
      }

      if (queue.isFull()) {
        System.err.println("(3) Queue should not be full");
        flag = false;
      }
    }

    return flag;
  }

  /**
   * Uses reflection to get modify the front and rear fields of the queue
   *
   * @return true if the test passes, false otherwise
   */
  private static boolean testIsFullEmpty() {
    boolean flag = true;
    SizelessCircularQueue<Integer> queue = new SizelessCircularQueue(1);

    // 1. Check that the queue is empty and not full
    {
      if (!queue.isEmpty()) {
        System.err.println("(1) Queue should be empty");
        flag = false;
      }

      if (queue.isFull()) {
        System.err.println("(1) Queue should not be full");
        flag = false;
      }
    }

    // Set access modifers
    Field arrayField; Field frontField; Field rearField;

    try {
      arrayField = queue.getClass().getDeclaredField("queue");
      arrayField.setAccessible(true);

      frontField = queue.getClass().getDeclaredField("front");
      frontField.setAccessible(true);

      rearField = queue.getClass().getDeclaredField("rear");
      rearField.setAccessible(true);


      arrayField.set(queue, new Object[]{1});
      frontField.set(queue, 0);
      rearField.set(queue, 0);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      System.err.println("(2) Error setting fields");
      return false;
    }

    // 2. Check that the queue is not empty and is full
    {
      if (queue.isEmpty()) {
        System.err.println("(2) Queue should not be empty");
        flag = false;
      }

      if (!queue.isFull()) {
        System.err.println("(2) Queue should be full");
        flag = false;
      }
    }

    // Force Remove the element
    try {
      arrayField.set(queue, new Object[]{null});
      frontField.set(queue, -1);
      rearField.set(queue, -1);
    } catch (IllegalAccessException e) {
      System.err.println("(3) Error setting fields");
      return false;
    }


    // 3. Check that the queue is empty and not full
    {
      if (!queue.isEmpty()) {
        System.err.println("(3) Queue should be empty");
        flag = false;
      }

      if (queue.isFull()) {
        System.err.println("(3) Queue should not be full");
        flag = false;
      }
    }

    return flag;
  }

  /**
   * Uses reflection to test the peek method
   * @return true if the test passes, false otherwise
   */

  private static boolean testPeek() {
    boolean flag = true;
    SizelessCircularQueue<Integer> queue = new SizelessCircularQueue(1);

    // 1. Check that peek returns null when the queue is empty
    {
      if (queue.peek() != null) {
        System.err.println("(1) Peek should return null when the queue is empty");
        flag = false;
      }
    }


    // Set access modifers
    Field arrayField;
    Field frontField;
    Field rearField;

    try {
      arrayField = queue.getClass().getDeclaredField("queue");
      arrayField.setAccessible(true);

      frontField = queue.getClass().getDeclaredField("front");
      frontField.setAccessible(true);

      rearField = queue.getClass().getDeclaredField("rear");
      rearField.setAccessible(true);
    } catch (NoSuchFieldException e) {
      System.err.println("(1) Error setting fields");
      return false;
    }

    // 2. Check that peek returns the correct element when the queue is not empty
    {
      try {
        arrayField.set(queue, new Object[]{1});
        frontField.set(queue, 0);
        rearField.set(queue, 0);
      } catch (IllegalAccessException e) {
        System.err.println("(2) Error setting fields");
        return false;
      }

      if (queue.peek() != 1) {
        System.err.println("(2) Peek should return the correct element when the queue is not empty");
        flag = false;
      }
    }

    // 3. Check that peek does not remove the element from the queue
    {
      if (queue.peek() != 1) {
        System.err.println("(3) Peek should not remove the element from the queue");
        flag = false;
      }
    }

    return flag;
  }

  /**
   * Uses reflection to test the enqueue method
   * @return true if the test passes, false otherwise
   */
  private static boolean testEnqueue() {
    boolean flag = true;

    // 1. test with a size of 1, should not throw an exception
    {
      try {
        SizelessCircularQueue<Integer> queue = new SizelessCircularQueue<>(1);
        queue.enqueue(1);

        if (queue.isEmpty()) {
          System.err.println("(1) Queue should not be empty");
          flag = false;
        }

        if (!queue.isFull()) {
          System.err.println("(1) Queue should be full");
          flag = false;
        }


      } catch (Exception e) {
        System.err.println("(1) Unexpected exception: " + e);
        flag = false;
      }

    }

    // 2. test enqueuing null
    {
      // enqueue null
      try {
        SizelessCircularQueue<Integer> queue = new SizelessCircularQueue<>(1);
        queue.enqueue(null);
        System.err.println("(2) Should have thrown an IllegalArgumentException");
        flag = false;
      } catch (IllegalArgumentException e) {
        // expected
      } catch (Exception e) {
        System.err.println("(2) Unexpected exception: " + e);
        flag = false;
      }
    }

    // 3. test enqueuing into a full queue
    {
      try {
        SizelessCircularQueue<Integer> queue = new SizelessCircularQueue<>(1);
        queue.enqueue(1);
        queue.enqueue(2);
        System.err.println("(3) Should have thrown an IllegalStateException");
        flag = false;
      } catch (IllegalStateException e) {
        // expected
      } catch (Exception e) {
        System.err.println("(3) Unexpected exception: " + e);
        flag = false;
      }
    }

    // test wrapping the queue around using reflection
    {
      SizelessCircularQueue<Integer> queue = new SizelessCircularQueue<>(2);
      Field arrayField;
      Field frontField;
      Field rearField;

      try {
        arrayField = queue.getClass().getDeclaredField("queue");
        arrayField.setAccessible(true);

        frontField = queue.getClass().getDeclaredField("front");
        frontField.setAccessible(true);

        rearField = queue.getClass().getDeclaredField("rear");
        rearField.setAccessible(true);
      } catch (NoSuchFieldException e) {
        System.err.println("(4) Error getting fields");
        return false;
      }

      try {
        arrayField.set(queue, new Object[]{null, 2});
        frontField.set(queue, 1);
        rearField.set(queue, 1);
      } catch (IllegalAccessException e) {
        System.err.println("(4) Error setting fields");
        return false;
      }

      try {
        queue.enqueue(3);
      } catch (Exception e) {
        System.err.println("(4) Unexpected exception: " + e);
        flag = false;
      }

      // check state of array
      try {
        Object[] array = (Object[]) arrayField.get(queue);
        if (array[0].equals(new Object[]{3, 2})) {
          System.err.println("(4) Array should have been updated");
          flag = false;
        }
      } catch (IllegalAccessException e) {
        System.err.println("(4) Error getting fields");
        return false;
      } catch (Exception e) {
        System.err.println("(4) Unexpected exception: " + e);
        flag = false;
      }
    }

    return flag;
  }

  /**
   * Uses reflection to test the dequeue method
   * @return true if the test passes, false otherwise
   */

  private static boolean testDequeue() {
    boolean flag = true;

    // 1. test with a size of 1, should not throw an exception
    {
      try {
        SizelessCircularQueue<Integer> queue = new SizelessCircularQueue<>(1);

        // use reflection to set the queue to a non-empty state
        Field arrayField;
        Field frontField;
        Field rearField;

        try {
          arrayField = queue.getClass().getDeclaredField("queue");
          arrayField.setAccessible(true);

          frontField = queue.getClass().getDeclaredField("front");
          frontField.setAccessible(true);

          rearField = queue.getClass().getDeclaredField("rear");
          rearField.setAccessible(true);
        } catch (NoSuchFieldException e) {
          System.err.println("(1) Error getting fields");
          return false;
        }

        // set the queue to a non-empty state
        try {
          arrayField.set(queue, new Object[]{1});
          frontField.set(queue, 0);
          rearField.set(queue, 0);
        } catch (IllegalAccessException e) {
          System.err.println("(1) Error setting fields");
          return false;
        }

        // dequeue and check the result
        queue.dequeue();

        if (!queue.isEmpty()) {
          System.err.println("(1) Queue should be empty");
          flag = false;
        }

        if (queue.isFull()) {
          System.err.println("(1) Queue should not be full");
          flag = false;
        }

        // use reflection to check the state of the queue
        try {
          Object[] array = (Object[]) arrayField.get(queue);
          if (array[0] != null) {
            System.err.println("(1) Array should have been updated");
            flag = false;
          }
        } catch (IllegalAccessException e) {
          System.err.println("(1) Error getting fields");
          return false;
        } catch (Exception e) {
          System.err.println("(1) Unexpected exception: " + e);
          flag = false;
        }


      } catch (Exception e) {
        System.err.println("(1) Unexpected exception: " + e);
        flag = false;
      }
    }

    // 2. test wrapping the queue around using reflection
    {
      SizelessCircularQueue<Integer> queue = new SizelessCircularQueue<>(2);
      Field arrayField;
      Field frontField;
      Field rearField;

      try {
        arrayField = queue.getClass().getDeclaredField("queue");
        arrayField.setAccessible(true);

        frontField = queue.getClass().getDeclaredField("front");
        frontField.setAccessible(true);

        rearField = queue.getClass().getDeclaredField("rear");
        rearField.setAccessible(true);
      } catch (NoSuchFieldException e) {
        System.err.println("(2) Error getting fields");
        return false;
      }

      try {
        arrayField.set(queue, new Object[]{1, 2});
        frontField.set(queue, 1);
        rearField.set(queue, 0);
      } catch (IllegalAccessException e) {
        System.err.println("(2) Error setting fields");
        return false;
      }

      try {
        queue.dequeue();
      } catch (Exception e) {
        System.err.println("(2) Unexpected exception: " + e);
        flag = false;
      }

      // check state of array
      try {
        Object[] array = (Object[]) arrayField.get(queue);
        if (array[0].equals(new Object[]{1, null})) {
          System.err.println("(2) Array should have been updated");
          flag = false;
        }

        if (frontField.getInt(queue) != 0) {
          System.err.println("(2) Front should have been updated to 0");
          flag = false;
        }

        if (rearField.getInt(queue) != 0) {
          System.err.println("(2) Rear should have shouldn't have changed");
          flag = false;
        }
      } catch (IllegalAccessException e) {
        System.err.println("(2) Error getting fields");
        return false;
      } catch (Exception e) {
        System.err.println("(2) Unexpected exception: " + e);
        flag = false;
      }
    }
    return flag;
  }

  /**
   * Main method to run the tests
   * @param args command line arguments (not used)
   */

  public static void main(String[] args) {
    if (!testConstructor()) {
       return;
    }

    if (testIsFullEmpty()) {
      System.out.println("testIsFullEmpty: true");
      if (testPeek()) {
        System.out.println("testIsFullEmpty: passed");
      } else {
        System.err.println("testPeek: failed");
      }
    } else {
      System.err.println("testIsFullEmpty: failed");
    }

    if (testEnqueue()) {
      System.out.println("testEnqueue: passed");
    } else {
      System.err.println("testEnqueue: failed");
    }

    if (testDequeue()) {
      System.out.println("testDequeue: passed");
    } else {
      System.err.println("testDequeue: failed");
    }
  }
}
