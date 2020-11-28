package alaiz.hashim.taskstodo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class Task(@PrimaryKey val id: UUID = UUID.randomUUID(),
                var title: String = "",
                var expireDate: Date = Date(),
                var detail: String = "",
                var flag: Int = 0      )