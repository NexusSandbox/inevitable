package eli.inevitable.templates

/**
 * A generic builder pattern used to configure objects with complex state.
 */
abstract class AbstractBuilderTemplate<out T>(protected val buildable: T) {

    private var finished = false

    /**
     * Verifies that the builder is not in a finished state. Only the builder can modify the base
     * buildable object, so that once [finish()][finish] is called, it can no longer be modified.
     *
     * This method should be called at the top of any mutator method.
     */
    protected fun checkFinalizedStatus() =
        require(!finished) { "Unable to modify a finished builder" }

    /**
     * @return `true` if the builder has already been finished, otherwise
     * `false`.
     */
    fun isFinished(): Boolean {
        return finished
    }

    /**
     * Ensures the builder's fields are in a valid state. This will only be called when
     * [#finish()][finish] is called.
     *
     * This method should be overridden to fail on all invalid states.
     */
    protected abstract fun validate(): AbstractBuilderTemplate<T>

    /**
     * Perform any additional finalization steps. This will only be called when
     * [#finish()][finish] is called.
     *
     * This method should be overridden to handle any additional finishing tasks that can only be
     * completed after the builder's criteria has been set.
     */
    protected abstract fun build(): T

    /**
     * Finalizes the builder object and returns the configured buildable.
     *
     * @return The finished configured [buildable object][T].
     */
    fun finish(): T {
        finished = true
        return validate().build()
    }
}
