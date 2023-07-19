package com.example.worktimemanagement.service

import com.example.worktimemanagement.entity.WorkLog
import com.example.worktimemanagement.repository.WorkLogRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime
import kotlin.collections.ArrayList

const val INSERT_HYPHEN_POSITION1 = 4
const val INSERT_HYPHEN_POSITION2 = 7
const val SECOND_OF_DAY = 60 * 60 * 24
const val TIME_ZERO_HHMMSS = "00:00:00"

interface WorkLogService {
    fun findByBetweenYearAndMonth(userId: Int, fromDate: String, toDate: String): List<WorkLog>

    fun save(workLog: WorkLog): List<WorkLog>
}

@Service
class WorkLogServiceImpl(val workLogRepository: WorkLogRepository): WorkLogService {

    override fun findByBetweenYearAndMonth(userId: Int, fromDate: String, toDate: String): List<WorkLog> {
        val modifiedFromDate = addHyphensToDate(fromDate)
        val modifiedToDate = addHyphensToDate(toDate)
        val workLogList = workLogRepository.findByBetweenYearAndMonth(userId, modifiedFromDate, modifiedToDate)
        return workLogList.map { workLog ->
                    if (workLog.workLogEndTime.substring(workLog.workLogEndTime.length - 8) == TIME_ZERO_HHMMSS)  {
                        WorkLog(
                            workLog.workLogId,
                            workLog.workLogUserId,
                            workLog.workLogDate,
                            workLog.workLogStartTime,
                            "${workLog.workLogDate} 24:00:00",
                            workLog.workLogSeconds
                        )
                    } else {
                        workLog
                    }
                }
    }

    override fun save(workLog: WorkLog): List<WorkLog> {
        val workLogStartDate = workLog.workLogStartTime.substring(0, 10)
        val workLogEndDate = workLog.workLogEndTime.substring(0, 10)
        var workLogList = ArrayList<WorkLog>()
        if (workLogStartDate != workLogEndDate) {
            workLogList.addAll(toListOfDayToDayWorkLog(workLogStartDate, workLogEndDate, workLog))
        } else {
            workLogList.add(workLog)
        }

        workLogRepository.saveAll(workLogList)
        return workLogList
    }

    private fun addHyphensToDate(date: String): String {
        return StringBuilder(date)
            .apply {
                insert(INSERT_HYPHEN_POSITION1, "-")
                insert(INSERT_HYPHEN_POSITION2, "-")
            }.toString()
    }

    private fun convertTimeToSeconds(timeString: String): Int {
        val time = LocalTime.parse(timeString)
        return time.toSecondOfDay()
    }

    private fun toListOfDayToDayWorkLog(workLogStartDate: String, workLogEndDate: String, workLog: WorkLog): ArrayList<WorkLog> {
        val workLogDateList = ArrayList<LocalDate>()
        var workLogList = ArrayList<WorkLog>()
        val startDate = LocalDate.parse(workLogStartDate)
        val endDate = LocalDate.parse(workLogEndDate)
        var currentDate = startDate

        while (!currentDate.isAfter(endDate)) {
            workLogDateList.add(currentDate)
            currentDate = currentDate.plusDays(1)
        }

        workLogDateList.forEachIndexed { index, workLogDate ->
            var workLogStartTime: String
            var workLogEndTime: String
            var workLogSeconds: Int
            if (index == 0) {
                workLogStartTime = workLog.workLogStartTime
                workLogEndTime = "${workLogDate.plusDays(1)} $TIME_ZERO_HHMMSS"
                workLogSeconds =
                    SECOND_OF_DAY - convertTimeToSeconds(workLogStartTime.substring(workLogStartTime.length - 8))
            } else if (index == workLogDateList.size - 1) {
                workLogStartTime = "$workLogDate $TIME_ZERO_HHMMSS"
                workLogEndTime = workLog.workLogEndTime
                workLogSeconds = convertTimeToSeconds(workLogEndTime.substring(workLogEndTime.length - 8))
            } else {
                workLogStartTime = "$workLogDate $TIME_ZERO_HHMMSS"
                workLogEndTime = "${workLogDate.plusDays(1)} $TIME_ZERO_HHMMSS"
                workLogSeconds = SECOND_OF_DAY
            }
            workLogList.add(
                WorkLog(
                    0,
                    workLog.workLogUserId,
                    workLogDate.toString(),
                    workLogStartTime,
                    workLogEndTime,
                    workLogSeconds
                )
            )
        }
        return workLogList
    }
}