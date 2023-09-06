package com.zn.apps.model.usecase

sealed class UseCaseException(cause: Throwable): Throwable(cause) {

    /**
     * If the task was not updated or added, throws an exception that will be used
     * to display what happened
     */
    class TaskNotUpdatedException(cause: Throwable): UseCaseException(cause)

    /**
     * If we tried to request a task and couldn't find, throws an exception to let the user
     * that task was not found
     */
    class TaskNotFoundException(cause: Throwable): UseCaseException(cause)

    /**
     * If the project was not updated or added, throws an exception that will be used
     * to display what happened
     */
    class ProjectNotUpdatedException(cause: Throwable): UseCaseException(cause)

    /**
     * If we tried to request a project and couldn't find, throws an exception to let the user
     * that project was not found
     */
    class ProjectNotFoundException(cause: Throwable): UseCaseException(cause)

    /**
     * If the tag was not updated or added, throws an exception that will be used
     * to display what happened
     */
    class TagNotFoundException(cause: Throwable): UseCaseException(cause)

    /**
     * If we tried to request a tag and couldn't find, throws an exception to let the user
     * that tag was not found
     */
    class TagNotUpdatedException(cause: Throwable): UseCaseException(cause)

    /**
     * If none of the above exception above occurred, throw an exception to let the user
     * know that something unexpected happened
     */
    class UnknownException(cause: Throwable): UseCaseException(cause)

    companion object {

        fun createFromThrowable(throwable: Throwable): UseCaseException {
            return if (throwable is UseCaseException) throwable else UnknownException(throwable)
        }
    }

}
