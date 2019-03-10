package com.example.lab2

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_new.*

class NewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        textView2.text = intent.extras["MESSAGE"] as CharSequence?

    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK, Intent(this, MainActivity::class.java)
            .putExtra(MainActivity.MESSAGE, "TEST"))
        finish()
    }
}
