package com.example.xogame

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.xogame.database.entities.History
import java.time.format.DateTimeFormatter

object  DialogHistory {
    @RequiresApi(Build.VERSION_CODES.O)
    fun showHistoryDialog(context: Context, histories: List<History>) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_history, null)
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        val historyLayout = dialogView.findViewById<LinearLayout>(R.id.history_layout)

        for (history in histories) {
            val historyTextView = TextView(context)
            historyTextView.text = "turn: ${history.id}\n Result: ${history.detailGame}\n Date/Time: ${history.date.format(formatter)}\n ________________________________"
            historyLayout.addView(historyTextView)
        }

        AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("History")
            .setPositiveButton("OK", null)
            .show()
    }
}