package com.awkitsune.tictactoegame

import kotlinx.coroutines.*
import kotlin.coroutines.*
import kotlin.random.Random


class GameField: CoroutineScope {
    companion object{
        private const val freeCellWeight = 0.7f
        private const val winCellWeight = 0.5f
        private const val suppressCellWeight = 0.3f
        private const val neutralWeight = 0.0f

        private const val playerMark = -1
        private const val aiMark = 1
        private const val neutralMark = 0
    }

    private val job = Job()

    private var field = Array(3) { Array(3) { neutralMark } }
    private var weights = Array(3) { Array(3) { neutralWeight } }

    var isWon = true
    var isPlayerWon = false

    fun flushField() {
        field = Array(3) { Array(3) { neutralMark } }
        flushWeights()
    }
    private fun flushWeights() { weights = Array(3) { Array(3) { neutralWeight } } }

    private fun aiDraw(coordinates: Pair<Int, Int>){
        if (field[coordinates.first][coordinates.second] == neutralMark){
            field[coordinates.first][coordinates.second] = aiMark
        }
    }

    fun playerDraw(x: Int, y: Int){
        if (field[x][y] == neutralMark){
            field[x][y] = playerMark

            setWinStates(playerMark)
        }
    }

    private fun aiMove() {
        var coordinatesForMove = Pair(0, 0)
        var maximumWeight = 0.1F

        findPosition()

        for (i in 0..2) {
            for (j in 0..2) {
                if (weights[i][j] > maximumWeight) {
                    maximumWeight = weights[i][j]
                    coordinatesForMove = Pair(i, j)
                }
            }
        }

        aiDraw(coordinatesForMove)

        setWinStates(aiMark)
        flushWeights()
    }

    private fun findPosition() {
        initializeWeights()

        searchInLines(playerMark)
        searchInLines(aiMark)

        randomizeWeights()
        normalizeWeights()
    }

    private fun normalizeWeights() {
        var min = Float.MAX_VALUE
        var max = Float.MIN_VALUE

        //finding max and min value
        for (i in 0..2) {
            for (j in 0..2) {
                if (weights[i][j] < min) min = weights[i][j]
                if (weights[i][j] > max) max = weights[i][j]
            }
        }

        //normalizing
        for (i in 0..2) {
            for (j in 0..2) {
                weights[i][j] = Utilities.normalize(weights[i][j], min, max)
            }
        }
    }

    private fun randomizeWeights() {
        for (i in 0..2) {
            for (j in 0..2) {
                if (field[i][j] == neutralMark){
                    weights[i][j] += Random.nextFloat() / 10.0f
                }
            }
        }
    }

    private fun searchInLines(playerType: Int) {
        //search in horizontal
        for (i in 0..2) {
            for (j in 0..2) {
                if (weights[i][j] >= 0.2 &&
                    field[i][0] + field[i][1] + field[i][2] == playerType * 2) {
                    for (k in 0..2) {
                        if (field[i][j] == neutralMark) when (playerType) {
                                playerMark -> weights[i][k] += suppressCellWeight
                                aiMark -> weights[i][k] += winCellWeight
                        }
                    }
                }
            }
        }

        //search in vertical
        for (i in 0..2) {
            for (j in 0..2) {
                if (weights[i][j] >= 0.2 &&
                    field[0][i] + field[1][i] + field[2][i] == playerType * 2) {
                    for (k in 0..2) {
                        if (field[i][j] == neutralMark) when (playerType) {
                            playerMark -> weights[k][i] += suppressCellWeight
                            aiMark -> weights[k][i] += winCellWeight
                        }
                    }
                }
            }
        }

        //search in main diagonal
        if (field[0][0] + field[0][0] + field[0][0] == playerType * 2){
            for (k in 0..2) {
                if (field[k][k] == neutralMark) when (playerType) {
                    playerMark -> weights[k][k] += suppressCellWeight
                    aiMark -> weights[k][k] += winCellWeight
                }
            }
        }

        //search in additional diagonal
        if (field[2][0] + field[0][0] + field[2][2] == playerType * 2){
            for (k in 0..2) {
                if (field[2 - k][k] == neutralMark) when (playerType) {
                    playerMark -> weights[2 - k][k] += suppressCellWeight
                    aiMark -> weights[2 - k][k] += winCellWeight
                }
            }
        }
    }

    private fun initializeWeights() {
        for (i in 0..2){
            for (j in 0..2){
                if (field[i][j] == neutralMark) weights[i][j] = freeCellWeight
            }
        }
    }

    private fun setWinStates(playerType: Int){
        if (checkForWin(playerType)){
            isWon = true
            when (playerType){
                aiMark -> isPlayerWon = false
                playerMark -> isPlayerWon = true
            }
        } else {
            isWon = false
            isPlayerWon = false
            when (playerType){
                playerMark -> aiMove()
            }
        }
    }

    private fun checkForWin(playerType: Int): Boolean {
        if ((field[0][0] == playerType && field[1][0] == playerType && field[2][0] == playerType) ||
            (field[0][1] == playerType && field[1][1] == playerType && field[2][1] == playerType) ||
            (field[0][2] == playerType && field[1][2] == playerType && field[2][2] == playerType))
        {
            return true
        }

        if ((field[0][0] == playerType && field[0][1] == playerType && field[0][2] == playerType) ||
            (field[1][0] == playerType && field[1][1] == playerType && field[1][2] == playerType) ||
            (field[2][0] == playerType && field[2][1] == playerType && field[2][2] == playerType))
        {
            return true
        }

        if ((field[0][0] == playerType && field[1][1] == playerType && field[2][2] == playerType) ||
            (field[2][0] == playerType && field[1][1] == playerType && field[0][2] == playerType))
        {
            return true
        }

        return false
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job
}