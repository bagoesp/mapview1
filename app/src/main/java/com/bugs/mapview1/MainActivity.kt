package com.bugs.mapview1

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.annotation.ColorRes

class MainActivity : AppCompatActivity() {

    lateinit var mCanvas: Canvas
    lateinit var ivMap: ImageView
    private val mPaint = Paint()
    lateinit var mBitmap : Bitmap
    private val mRect = RectF()

    lateinit var spinAwal: Spinner
    lateinit var spinTujuan: Spinner
    lateinit var btnOk : Button
    lateinit var btnChange: Button

    private lateinit var selSel: Array<Array<Sel>>
    var selSize = 0F

    companion object {
        const val COLS = 3
        const val ROWS = 2
        const val WALL = 4F
        const val INSET = 15F
    }

    init {
        createMap()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivMap = findViewById(R.id.iv_map)
        spinAwal = findViewById(R.id.spin_awal)
        spinTujuan = findViewById(R.id.spin_tujuan)
        btnOk = findViewById(R.id.btn_ok)
        btnChange = findViewById(R.id.btn_change)

        val lokasi = arrayOf<String>("A", "B", "C", "D")
        val ad = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lokasi)

        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinAwal.adapter = ad
        spinTujuan.adapter = ad

        ivMap.post { showMap() }

        btnOk.setOnClickListener {
            showMap()
        }

        btnChange.setOnClickListener {
            showAB()
        }
    }

    // OK
    private fun createMap() {
        selSel = Array(COLS) {
            Array(ROWS) {
                Sel()
            }
        }

        for (x in 0 until COLS){
            for (y in 0 until ROWS) {
                selSel[x][y].x = x
                selSel[x][y].y = y
            }
        }

        //top wall
        for (x in 0 until COLS) {
            val y = 0
            selSel[x][y].top = true
        }

        //left wall
        for (y in 0 until ROWS) {
            val x = 0
            selSel[x][y].left = true
        }

        //right wall
        for (y in 0 until ROWS){
            val x = 2
            selSel[x][y].right = true
        }

        //bottom wall
        for (x in 0 until COLS) {
            val y = 1
            selSel[x][y].bottom = true
        }

        selSel[0][0].bottom = true
        selSel[0][1].top = true

        selSel[2][0].bottom = true
        selSel[2][1].top = true
    }

    // OK
    private fun showMap(){
        mBitmap = Bitmap.createBitmap(ivMap.width, ivMap.height, Bitmap.Config.ARGB_8888)
        ivMap.setImageBitmap(mBitmap)
        mCanvas = Canvas(mBitmap)
        mCanvas.drawColor(Color.WHITE)
        mPaint.color = Color.BLACK
        mPaint.strokeWidth = WALL

        selSize = (ivMap.width/(COLS+1)).toFloat()
        val hMargin = (ivMap.width - (selSize* COLS))/2
        val vMargin = (ivMap.height - (selSize* ROWS))/2

        mCanvas.translate(hMargin, vMargin)

        for (x in 0 until COLS) {
            for (y in 0 until ROWS) {
                if (selSel[x][y].top)
                    mCanvas.drawLine(x*selSize, y*selSize, (x+1)*selSize, y*selSize, mPaint)
                if (selSel[x][y].left)
                    mCanvas.drawLine(x*selSize, y*selSize, x*selSize, (y+1)*selSize, mPaint)
                if (selSel[x][y].right)
                    mCanvas.drawLine((x+1)*selSize, y*selSize, (x+1)*selSize, (y+1)*selSize, mPaint)
                if (selSel[x][y].bottom)
                    mCanvas.drawLine(x*selSize, (y+1)*selSize, (x+1)*selSize, (y+1)*selSize, mPaint)
            }
        }

        ivMap.invalidate()
    }

    // working on it ...
    private fun showAB(){

        showMap()

        val awal = initSel(spinAwal.selectedItem.toString())
        val tujuan = initSel(spinTujuan.selectedItem.toString())

        drawAwal(awal)
        drawTujuan(tujuan)
    }

    private fun drawAwal(awal: Sel) {
        mCanvas.save()
        val left = INSET
        val top = INSET
        val right = selSize - INSET
        val bottom = selSize - INSET
        mPaint.color = Color.BLUE
        mCanvas.translate(awal.x * selSize, awal.y * selSize)
        mRect.set(left, top, right, bottom)
        mCanvas.drawRect(mRect, mPaint)
        ivMap.invalidate()
        mCanvas.restore()
    }

    private fun drawTujuan(tujuan: Sel) {
        mCanvas.save()
        val left = INSET
        val top = INSET
        val right = selSize - INSET
        val bottom = selSize - INSET
        mPaint.color = Color.RED
        mCanvas.translate(tujuan.x*selSize, tujuan.y*selSize)
        mRect.set(left, top, right, bottom)
        mCanvas.drawRect(mRect, mPaint)
        ivMap.invalidate()
        mCanvas.restore()
    }

    private fun initSel(lokasi: String) : Sel {
        when(lokasi) {
            "A" -> {
                return selSel[0][0]
            }
            "B" -> {
                return selSel[2][0]
            }
            "C" -> {
                return selSel[0][1]
            }
            "D" -> {
                return selSel[2][1]
            }
        }
        return selSel[0][0]
    }
}