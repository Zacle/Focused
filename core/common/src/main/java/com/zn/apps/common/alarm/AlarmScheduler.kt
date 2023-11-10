package com.zn.apps.common.alarm

import com.zn.apps.model.data.alarm.AlarmItem

interface AlarmScheduler {
    fun scheduleAlarm(alarmItem: AlarmItem)
    fun cancelAlarm(alarmItem: AlarmItem)
}