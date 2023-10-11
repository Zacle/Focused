package com.zn.apps.filter

import com.zn.apps.model.R


sealed interface Grouping {
    /** Group by completed time **/
    data object DeadlineCompletedTimeGrouping: Grouping

    /** Group by [DeadlineTime] */
    data object DeadlineTimeGrouping: Grouping
    /**
     * Group by [DeadlineType]
     */
    data object DeadlineTypeGrouping: Grouping

    /* Group by priority */
    data object PriorityGrouping: Grouping

    /* Group by tags */
    data object TagGrouping: Grouping

    companion object {
        /**
         * Return resId of [Grouping] that will be used to convert to String and display on
         * the screen
         */
        fun getGroupingResId(grouping: Grouping): Int {
            return when(grouping) {
                is DeadlineTimeGrouping -> R.string.due_date
                is PriorityGrouping -> R.string.priority
                is TagGrouping -> R.string.tag
                else -> 0
            }
        }
    }

}