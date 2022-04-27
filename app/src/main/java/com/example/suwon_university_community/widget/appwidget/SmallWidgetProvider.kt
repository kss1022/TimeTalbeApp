package com.example.suwon_university_community.widget.appwidget

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.suwon_university_community.R
import com.example.suwon_university_community.data.db.AppDataBase
import com.example.suwon_university_community.data.db.dao.TimeTableDao
import com.example.suwon_university_community.data.entity.timetable.DayOfTheWeek
import com.example.suwon_university_community.data.entity.timetable.TimeTableCellEntity
import com.example.suwon_university_community.data.preference.PreferenceManager
import com.example.suwon_university_community.extensions.toReadableDayString
import com.example.suwon_university_community.ui.start.StartActivity
import com.example.suwon_university_community.util.converter.RoomTypeConverter
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class SmallWidgetProvider : AppWidgetProvider() {


    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        ContextCompat.startForegroundService(
            context!!,
            Intent(context, UpdateWidgetService::class.java)
        )
    }

    class UpdateWidgetService : LifecycleService() {

        override fun onCreate() {
            super.onCreate()

            createChannelIfNeeded()
            startForeground(
                NOTIFICATION_ID,
                createNotification()
            )
        }


        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            val updateWidget = RemoteViews(packageName, R.layout.widget_small).apply {
                setTextViewText(R.id.dayTextView, "")
                setTextViewText(R.id.timeTextView, "")
                setViewVisibility(R.id.dayTextView, View.VISIBLE)
                setViewVisibility(R.id.timeTextView, View.VISIBLE)
                val i = Intent(Intent.ACTION_MAIN)
                i.addCategory(Intent.CATEGORY_LAUNCHER)
                i.component = ComponentName(this@UpdateWidgetService, StartActivity::class.java)

                setOnClickPendingIntent(
                    R.id.widgetLayout,
                    PendingIntent.getActivity(
                        this@UpdateWidgetService,
                        0,
                        i,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
            }

            updateWidget(updateWidget)

            lifecycleScope.launch {
                try {
                    getMainTableId()?.let { tableId ->
                        updateWidget.also {
                            it.setViewVisibility(R.id.dayTextView, View.VISIBLE)
                            it.setViewVisibility(R.id.lectureTitleTextView, View.VISIBLE)
                            it.setViewVisibility(R.id.lectureDetailTextView, View.VISIBLE)
                        }

                        val lectureDao = getTimeTableDao()
                        withContext(Dispatchers.IO) {
                            val cellList =
                                lectureDao.getTimeTableWithCell(tableId).timeTableCellList

                            val calendar = Calendar.getInstance()
                            val time = Date(System.currentTimeMillis())
                            calendar.time = time
                            val day = getDay(calendar.get(Calendar.DAY_OF_WEEK))


                            val lectureInfo = mutableSetOf<TimeTableCellEntity>()

                            for (i in cellList) {
                                i.locationAndTimeList.forEach {
                                    if (it.day == day) {
                                        lectureInfo.add(i)
                                    }
                                }
                            }


                            val testLecture = lectureInfo.first()

                            val lectureStr = if (lectureInfo.isEmpty()) {
                                "오늘은 강의가 없습니다."
                            } else {
                                var timeStr = ""
                                var locationStr = ""
                                val location = mutableSetOf<String>()

                                testLecture.locationAndTimeList.forEach {
                                    timeStr += "${it.getTimeString()}  "
                                    location.add(it.location)
                                }

                                location.forEach {
                                    locationStr += "$it  "
                                }

                                timeStr +"\n" + locationStr
                            }



                            withContext(Dispatchers.Main) {
                                updateWidget.also {
                                    it.setTextViewText(R.id.dayTextView, "${day.char} ${time.toReadableDayString()}")
                                    it.setTextViewText(R.id.lectureTitleTextView, testLecture.name)
                                    it.setTextViewText(R.id.lectureDetailTextView, lectureStr)
                                }
                            }
                        }

                    } ?: kotlin.run {
                        updateWidget.also {
                            //시간표가 없는경우
                            it.setViewVisibility(R.id.dayTextView, View.GONE)
                            it.setViewVisibility(R.id.lectureTitleTextView, View.GONE)
                            it.setViewVisibility(R.id.lectureDetailTextView, View.GONE)
                            it.setViewVisibility(R.id.errorTextView, View.VISIBLE)
                        }
                    }


                    updateWidget(updateWidget)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    stopSelf()
                }
            }

            return super.onStartCommand(intent, flags, startId)
        }


        override fun onDestroy() {
            super.onDestroy()
            stopForeground(true)
        }

        private fun createChannelIfNeeded() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                (getSystemService(NOTIFICATION_SERVICE) as? NotificationManager)
                    ?.createNotificationChannel(
                        NotificationChannel(
                            WIDGET_REFRESH_CHANNEL_ID,
                            "위젯 갱신 채널",
                            NotificationManager.IMPORTANCE_LOW
                        )
                    )
            }
        }

        private fun createNotification() =
            NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_baseline_refresh_24)
                .setChannelId(WIDGET_REFRESH_CHANNEL_ID)
                .build()

        private fun updateWidget(updateViews: RemoteViews) {
            val widgetProvider = ComponentName(this, SmallWidgetProvider::class.java)
            AppWidgetManager.getInstance(this).updateAppWidget(widgetProvider, updateViews)
        }


        private fun getMainTableId() = PreferenceManager(
            applicationContext!!.getSharedPreferences(
                PreferenceManager.PREFERENCES_NAME, MODE_PRIVATE
            )
        ).getMainTimeTableId()

        private fun getTimeTableDao(): TimeTableDao =
            Room.databaseBuilder(
                applicationContext,
                AppDataBase::class.java,
                AppDataBase.APP_DATABASE_NAME
            ).addTypeConverter(RoomTypeConverter(Gson())).build().getTimeTableDao()

        private fun getDay(day: Int): DayOfTheWeek {
            return when (day) {
                2 -> DayOfTheWeek.MON
                3 -> DayOfTheWeek.TUE
                4 -> DayOfTheWeek.WED
                5 -> DayOfTheWeek.THU
                6 -> DayOfTheWeek.FRI
                else -> DayOfTheWeek.DEFAULT
            }
        }

        companion object {
            private const val NOTIFICATION_ID = 10001
            private const val WIDGET_REFRESH_CHANNEL_ID = "WIDGET_REFRESH"
        }
    }
}

