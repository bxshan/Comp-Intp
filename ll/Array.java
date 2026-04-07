package ll;

/**
 * AST node representing an array type declaration expression.
 * <p>
 * {@code Array} describes the extent of a fixed-size array in the source
 * language using an inclusive integer range {@code [start..end]}.  At runtime
 * the evaluator uses these bounds to allocate storage for the array.
 * </p>
 * <p>
 * Example source: {@code array[1..10]}
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Expression
 * @see ArrayElement
 * @see ArrayAssignment
 */
public class Array extends Expression
{
    /** The inclusive lower bound of the array index range. */
    private int start;

    /** The inclusive upper bound of the array index range. */
    private int end;

    /**
     * Constructs an {@code Array} node with the given index bounds.
     *
     * @param start the inclusive lower bound of the array index range
     * @param end   the inclusive upper bound of the array index range;
     *              must be &ge; {@code start}
     */
    public Array(int start, int end)
    {
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the inclusive lower bound of the array index range.
     *
     * @return the start index
     */
    public int getStart()
    {
        return this.start;
    }

    /**
     * Returns the inclusive upper bound of the array index range.
     *
     * @return the end index
     */
    public int getEnd()
    {
        return this.end;
    }

    /**
     * Returns a Pascal-style range notation for this array type.
     *
     * @return a string of the form {@code array[start..end]}
     */
    @Override
    public String toString()
    {
        return "array[" + start + ".." + end + "]";
    }
}
