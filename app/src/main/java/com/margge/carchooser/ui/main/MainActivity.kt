package com.margge.carchooser.ui.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.margge.carchooser.R
import com.margge.carchooser.databinding.ActivityMainBinding
import com.margge.carchooser.ui.main.injection.DaggerMainFComponent
import com.margge.carchooser.ui.main.injection.MainFModule
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerMainFComponent.builder()
                .mainFModule(MainFModule(this@MainActivity))
                .build()
                .inject(this@MainActivity)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.mainViewModel = mainViewModel
    }
}
