package com.aliexplainer.app.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aliexplainer.app.data.api.RetrofitClient
import com.aliexplainer.app.databinding.ActivitySettingsBinding
import com.aliexplainer.app.utils.SettingsManager

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }
        binding.urlInput.setText(SettingsManager.getBaseUrl())

        binding.saveButton.setOnClickListener {
            val url = binding.urlInput.text.toString().trim()
            if (url.isEmpty()) {
                Toast.makeText(this, "URL cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            SettingsManager.setBaseUrl(url)
            RetrofitClient.rebuildClient()
            Toast.makeText(this, "API URL saved! Restart app to apply.", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
