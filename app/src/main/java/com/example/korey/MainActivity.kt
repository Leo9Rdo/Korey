package com.example.korey

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.animation.ObjectAnimator
import android.animation.Animator
import com.example.korey.R
import com.example.korey.QuizFragment
import com.example.korey.CountdownFragment
import com.example.korey.WelcomeFragment

// Главная активность приложения
class MainActivity : AppCompatActivity(), CountdownFragment.CountdownListener, WelcomeFragment.OnStartQuizListener, WelcomeFragment.OnCreateCardListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Первоначально отображаем фрагмент приветствия
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, WelcomeFragment.newInstance("param1", "param2"))
            .commit()
    }


    override fun onCreateCard() {
        // Замена текущего фрагмента на фрагмент создания карточки
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, CreateCardFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }


    override fun onStartQuiz() {
        // Замена текущего фрагмента на фрагмент с обратным отсчетом
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, CountdownFragment.newInstance("param1", "param2"))
            .addToBackStack(null)
            .commit()
    }

    // Метод для обработки завершения обратного отсчета
    override fun onCountdownFinished() {
        // Замена текущего фрагмента на фрагмент с квизом
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, QuizFragment.newInstance())
            .commit()
    }
}
