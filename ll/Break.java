package ll;

/**
 * AST node representing a {@code BREAK} (or {@code EXIT}) statement.
 * <p>
 * When the evaluator encounters a {@code Break} node inside a loop body it
 * immediately terminates the enclosing {@link While}, {@link For}, or
 * {@link RepeatUntil} loop.  The parser also maps the {@code EXIT} keyword to
 * this node type, treating it as a synonym for {@code BREAK}.
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Statement
 * @see Continue
 */
public class Break extends Statement
{}
