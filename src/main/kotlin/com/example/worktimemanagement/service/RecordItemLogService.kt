package com.example.worktimemanagement.service

import com.example.worktimemanagement.entity.RecordItemLog
import com.example.worktimemanagement.repository.RecordItemLogRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.collections.ArrayList

const val INSERT_HYPHEN_POSITION1 = 4
const val INSERT_HYPHEN_POSITION2 = 7
const val SECOND_OF_DAY = 60 * 60 * 24
const val TIME_ZERO_HHMMSS = "00:00:00"

interface RecordItemLogService {

    fun getSelectedMonthlyRecordItemLog(recordItemId: Int, fromDate: String, toDate: String): List<RecordItemLog>

    fun createRecordItemLog(recordItemLog: RecordItemLog): List<RecordItemLog>

    fun deleteRecordItemLog(recordItemLogId: Int)
}

@Service
class RecordItemLogServiceImpl(val recordItemLogRepository: RecordItemLogRepository): RecordItemLogService {

    override fun getSelectedMonthlyRecordItemLog(recordItemId: Int, fromDate: String, toDate: String): List<RecordItemLog> {
        val modifiedFromDate = addHyphensToDate(fromDate)
        val modifiedToDate = addHyphensToDate(toDate)
        val recordItemLogList = recordItemLogRepository.findByBetweenYearAndMonth(recordItemId, modifiedFromDate, modifiedToDate)
        return recordItemLogList.map { recordItemLog ->
                    if (recordItemLog.recordItemLogEndTime.substring(recordItemLog.recordItemLogEndTime.length - 8) == TIME_ZERO_HHMMSS)  {
                        RecordItemLog(
                            recordItemLog.recordItemLogId,
                            recordItemLog.recordItemId,
                            recordItemLog.recordItemLogDate,
                            recordItemLog.recordItemLogStartTime,
                            "${recordItemLog.recordItemLogDate} 24:00:00",
                            recordItemLog.recordItemLogSeconds
                        )
                    } else {
                        recordItemLog
                    }
                }
    }

    override fun createRecordItemLog(recordItemLog: RecordItemLog): List<RecordItemLog> {
        val recordItemLogEndDate = recordItemLog.recordItemLogEndTime.substring(recordItemLog.recordItemLogEndTime.length - 8, recordItemLog.recordItemLogEndTime.length)
        var recordItemLogList = ArrayList<RecordItemLog>()
        var returnRecordItemLogList = ArrayList<RecordItemLog>()
        returnRecordItemLogList.add(recordItemLog)
        if (recordItemLogEndDate == "24:00:00") {
            recordItemLogList.add(
                RecordItemLog(
                    0,
                    recordItemLog.recordItemId,
                    recordItemLog.recordItemLogDate,
                    recordItemLog.recordItemLogStartTime,
            "${LocalDate.parse(recordItemLog.recordItemLogDate).plusDays(1)} $TIME_ZERO_HHMMSS",
                    recordItemLog.recordItemLogSeconds
                ))
        } else {
            recordItemLogList.add(recordItemLog)
        }
        recordItemLogRepository.saveAll(recordItemLogList)
        return returnRecordItemLogList
    }

    override fun deleteRecordItemLog(recordItemLogId: Int) {
        recordItemLogRepository.deleteByRecordItemLogId(recordItemLogId)
    }

    private fun addHyphensToDate(date: String): String {
        return StringBuilder(date)
            .apply {
                insert(INSERT_HYPHEN_POSITION1, "-")
                insert(INSERT_HYPHEN_POSITION2, "-")
            }.toString()
    }
}