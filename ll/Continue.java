package ll;

/**
 * AST node representing a {@code CONTINUE} statement.
 * <p>
 * When the evaluator encounters a {@code Continue} node inside a loop body it
 * skips the remaining statements in the current iteration and proceeds to the
 * next condition check (for {@link While}) or iteration step (for {@link For}
 * and {@link RepeatUntil}).
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Statement
 * @see Break
 */
public class Continue extends Statement
{
}
