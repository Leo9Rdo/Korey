package com.example.korey

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

// Ключи для передачи параметров в фрагмент
private const val COUNTDOWN_FRAGMENT_ARG_PARAM1 = "param1"
private const val COUNTDOWN_FRAGMENT_ARG_PARAM2 = "param2"

class CountdownFragment : Fragment() {

    // Интерфейс для обработки завершения обратного отсчета
    interface CountdownListener {
        fun onCountdownFinished()
    }

    // Слушатель для обработки завершения обратного отсчета
    private var countdownListener: CountdownListener? = null
    private lateinit var countdownTextView: TextView
    private var countdownTimer: CountDownTimer? = null

    // Переопределение метода onAttach() для привязки слушателя к активности
    override fun onAttach(context: Context) {
        super.onAttach(context)
        countdownListener = context as? CountdownListener
    }

    // Метод companion object для создания нового экземпляра фрагмента с передачей параметров
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String): CountdownFragment {
            val fragment = CountdownFragment()
            val args = Bundle().apply {
                putString(COUNTDOWN_FRAGMENT_ARG_PARAM1, param1)
                putString(COUNTDOWN_FRAGMENT_ARG_PARAM2, param2)
            }
            fragment.arguments = args
            return fragment
        }
    }

    // Метод для создания макета фрагмента
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_countdown, container, false)
    }

    // Метод вызывается после того, как фрагмент создан и связан с макетом
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countdownTextView = view.findViewById(R.id.countdownTextView)
        startCountdown() // Запуск обратного отсчета
    }

    // Метод для запуска обратного отсчета
    private fun startCountdown() {
        val countdownDuration = 4000L // Пример: 4 секунды

        countdownTimer = object : CountDownTimer(countdownDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                countdownTextView.text = "Beginning in: $secondsRemaining seconds"
            }

            override fun onFinish() {
                countdownTextView.text = "Let's go!"
                countdownListener?.onCountdownFinished() // Оповещение о завершении обратного отсчета
            }
        }

        countdownTimer?.start() // Запуск таймера
    }

    // Метод вызывается перед уничтожением фрагмента
    override fun onDestroy() {
        super.onDestroy()
        countdownTimer?.cancel() // Остановка таймера при уничтожении фрагмента
    }
}
