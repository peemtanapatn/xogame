package com.example.xogame

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.xogame.database.entities.History
import com.example.xogame.database.viewmodel.HistoryViewModel
import com.google.android.material.textfield.TextInputEditText
import java.lang.Math.random
import java.time.LocalDateTime


class MainActivity : AppCompatActivity() {
    private var numEdit = 3
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var btnHistory: Button
    private lateinit var btnEdit: Button
    private lateinit var btnRestart: Button
    private lateinit var txtNum: TextInputEditText
    private lateinit var tvSize: TextView
    private lateinit var board: Array<Array<String>>
    private lateinit var historyList : List<History>
    private var activePlayer = "x"
    private var statusGame = true



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        historyViewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        historyViewModel.allEvents.observe(this) { histories ->
            historyList = histories
        }
        btnHistory = findViewById(R.id.btnHistory)
        btnRestart = findViewById(R.id.btnRestart)
        btnEdit = findViewById(R.id.btnEdit)
        txtNum = findViewById(R.id.numtxt)
        tvSize = findViewById(R.id.tvSize)
        txtNum.visibility = View.GONE
        setBoard()


        btnEdit.setOnClickListener {
            setEditNum()
        }
        btnRestart.setOnClickListener {
            setBoard()
        }
        btnHistory.setOnClickListener{
            DialogHistory.showHistoryDialog(this, historyList)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setEditNum() {
        if (txtNum.isVisible){
            if(txtNum.text.toString().isNotEmpty()){
                numEdit = txtNum.text.toString().toInt()
                if(numEdit in 2..10){
                    setBoard()
                    tvSize.text = "Size : $numEdit x $numEdit"
                }else{
                    val toast = Toast.makeText(this, "The board must be sized from 2-10.", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
            btnEdit.text = "edit"
            btnEdit.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500))
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(txtNum.windowToken, 0)
            txtNum.visibility = View.GONE
        }else{
            txtNum.setText("")
            btnEdit.text = "save"
            btnEdit.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            txtNum.visibility = View.VISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setBoard(){
        var row = 0
        var col = 0
        var numButtons = numEdit*numEdit
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        gridLayout.removeAllViews()
        gridLayout.rowCount = numEdit
        gridLayout.columnCount = numEdit
        board = Array(numEdit) { Array(numEdit) { " " } }
        statusGame = true
        activePlayer = "x"

        for (i in 0 until numButtons) {
            var button = Button(this)
            button.tag = "$row,$col"
            button.setOnClickListener {
                if(button.text.isEmpty() && statusGame){
                    play(button)
                    if(activePlayer=="x"){
                        activePlayer = "o"
                    }else{
                        activePlayer = "x"
                    }
                }
            }
            val layoutParams = GridLayout.LayoutParams()
            layoutParams.width = 0
            layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT
            val rowSpec = GridLayout.spec(i / numEdit, 1f)
            val colSpec = GridLayout.spec(i % numEdit, 1f)
            layoutParams.rowSpec = rowSpec
            layoutParams.columnSpec = colSpec
            button.layoutParams = layoutParams
            gridLayout.addView(button)
            if(col < numEdit-1){
                col+=1
            }else{
                col = 0
                row+=1
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun play(button: Button){
        val index: List<String> = button.tag.toString().split(",")
        val row = index[0].toInt()
        val col = index[1].toInt()
        button.text = activePlayer
        board[row][col] = activePlayer
        if(!checkWin()){
            checkDraw()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkWin(): Boolean{
        // Check rows
        for (i in board.indices) {
            var rowWin = true
            for (j in 1 until board[i].size) {
                if (board[i][j] != board[i][0] || board[i][0] == " ") {
                    rowWin = false
                    break
                }
            }
            if (rowWin) {
                endGame("Player $activePlayer is winner!")
                return true
            }
        }

        // Check columns
        for (i in board.indices) {
            var colWin = true
            for (j in 1 until board[i].size) {
                if (board[j][i] != board[0][i] || board[0][i] == " ") {
                    colWin = false
                    break
                }
            }
            if (colWin) {
                endGame("Player $activePlayer is winner!")
                return true
            }
        }

        // Check diagonals
        var diagonalWin = true
        for (i in 1 until board.size) {
            if (board[i][i] != board[0][0] || board[0][0] == " ") {
                diagonalWin = false
                break
            }
        }
        if (diagonalWin) {
            endGame("Player $activePlayer is winner!")
            return true
        }

        diagonalWin = true
        for (i in 1 until board.size) {
            if (board[i][board.size - i - 1] != board[0][board.size - 1] || board[0][board.size - 1] == " ") {
                diagonalWin = false
                break
            }
        }
        if (diagonalWin) {
            endGame("Player $activePlayer is winner!")
            return true
        }
        return false

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkDraw(){
        var draw = true
        for (i in board.indices) {
            for (j in board.indices) {
                if(board[i][j] == " "){
                    draw = false
                    break
                }
            }
        }
        if (draw) {
            endGame("Draw!")
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun endGame(msg:String){
        statusGame = false
        val builder = AlertDialog.Builder(this)
        builder.setTitle("End game!")
        builder.setMessage(msg)
        builder.setPositiveButton("OK") { _, _ ->
            val history = History(0, msg,  LocalDateTime.now())
            historyViewModel.insertEvent(history)
            setBoard()

        }
        val dialog = builder.create()
        dialog.show()

    }

}