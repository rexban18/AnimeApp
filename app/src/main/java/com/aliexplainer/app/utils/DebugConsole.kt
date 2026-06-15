package com.aliexplainer.app.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

object DebugConsole {
    private var logView: TextView? = null
    private var container: FrameLayout? = null
    private var isVisible = false
    private val logs = mutableListOf<String>()
    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    fun show(context: Context, parent: FrameLayout) {
        container = parent
        val scroll = ScrollView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                300
            ).apply {
                gravity = Gravity.BOTTOM
                bottomMargin = 0
            }
            setBackgroundColor(0xCC000000.toInt())
        }

        logView = TextView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            textSize = 10f
            text = "⚡ Debug Console\n"
            setTextColor(android.graphics.Color.parseColor("#00FF00"))
            setPadding(12, 12, 12, 12)
        }

        scroll.addView(logView)
        parent.addView(scroll)
        isVisible = true

        // Add a small toggle button
        val toggle = TextView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                100, 36
            ).apply {
                gravity = Gravity.BOTTOM or Gravity.END
                bottomMargin = 310
                rightMargin = 8
            }
            text = "CONSOLE"
            textSize = 8f
            gravity = Gravity.CENTER
            setTextColor(android.graphics.Color.WHITE)
            setBackgroundColor(0xAA000000.toInt())
            setOnClickListener {
                scroll.visibility = if (scroll.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
        }
        parent.addView(toggle)
    }

    fun log(tag: String, message: String) {
        val time = dateFormat.format(Date())
        val line = "[$time] [$tag] $message"
        logs.add(line)
        if (logs.size > 100) logs.removeAt(0)

        logView?.post {
            val sb = StringBuilder()
            for (l in logs) {
                sb.append(l).append("\n")
            }
            logView?.text = "⚡ Debug Console\n$sb"
        }
    }

    fun apiRequest(url: String) {
        log("API", "→ $url")
    }

    fun apiSuccess(url: String) {
        log("API", "✓ $url")
    }

    fun apiError(url: String, error: String) {
        log("ERROR", "✗ $url - $error")
    }
}
