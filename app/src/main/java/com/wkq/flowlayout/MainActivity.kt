package com.wkq.flowlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.wkq.flow.FlowLayout

class MainActivity : AppCompatActivity() {
    var lists = arrayListOf<String>("1", "拖更了1年", "拖更了1年", "拖更了1年")
//    var lists= arrayListOf<String>("1","拖更了1年","拖更了1年","拖更了1年","拖更了1年","拖更了1年",
//        "拖更了1年","拖更了1年","拖更了1年","拖更了1年","拖更了1年",
//        "拖更了1年","拖更了2年","拖更了1年","666666666")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       var flowLayout= findViewById<FlowLayout>(R.id.fl)
        flowLayout.addLabels(lists)
        flowLayout.addLabelsListener(object :FlowLayout.FlowLayoutLabelslListener{
            override fun onLabelsClick(content: Any) {
                Toast.makeText(this@MainActivity,content.toString(),Toast.LENGTH_LONG).show()
            }

        })
    }
}