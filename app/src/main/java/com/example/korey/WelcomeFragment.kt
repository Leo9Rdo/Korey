package com.example.korey

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

// Константы для аргументов фрагмента
private const val WELCOME_FRAGMENT_ARG_PARAM1 = "param1"
private const val WELCOME_FRAGMENT_ARG_PARAM2 = "param2"

class WelcomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var onStartQuizListener: OnStartQuizListener? = null
    private var onCreateCardListener: OnCreateCardListener? = null
    private lateinit var startQuizButton: Button // Переменная для кнопки
    private lateinit var createASetButton: Button
    private lateinit var rulesButton: Button


    interface OnCreateCardListener {
        fun onCreateCard()
    }

    override fun onResume() {
        super.onResume()
        // Снятие блокировки при возвращении на WelcomeFragment
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }


    // Интерфейс для обратного вызова при нажатии на кнопку "Start Quiz"
    interface OnStartQuizListener {
        fun onStartQuiz()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Проверяем, реализует ли активити интерфейс OnCreateCardListener
        if (context is OnCreateCardListener) {
            onCreateCardListener = context
        } else {
            throw RuntimeException("$context must implement OnCreateCardListener")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Загружаем макет фрагмента
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация кнопки "Create A Set" и установка слушателя нажатия
        createASetButton = view.findViewById(R.id.createASetButton)
        createASetButton.setOnClickListener {
            onCreateCardListener?.onCreateCard()
        }

        // Инициализация кнопки "Rules" и установка слушателя нажатия
        rulesButton = view.findViewById(R.id.rulesButton)
        rulesButton.setOnClickListener{ showRules() }

        // Проверяем, реализует ли активити интерфейс OnStartQuizListener
        if (activity is OnStartQuizListener) {
            onStartQuizListener = activity as OnStartQuizListener
        } else {
            throw RuntimeException("$activity must implement OnStartQuizListener")
        }

        // Инициализация кнопки "Start Quiz" и установка слушателя нажатия
        startQuizButton = view.findViewById(R.id.startQuizButton)
        startQuizButton.setOnClickListener {
            onStartQuizListener?.onStartQuiz()
        }
    }

//    interface OnCreateCardListener {
//        fun onCreateCard()
//
//        private var onCreateCardListener: OnCreateCardListener? = null
//
//    }



    private fun showRules() {
        // Блокировка остального экрана от нажатий
        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        val message = """
        Title: "Korey"

        Service Objective: To attract attention to the modern, rapidly growing, and popular genre - k-pop.

        How to Use:
        1. Click "Start" to begin the game.
        2. Press "Create a set" to make your own card.
        3. Korean individuals' cards are shown to you.
        4. If you name correctly, press "+" to score a point.
        5. If you don't know, press "-", no points will be awarded.
        6. The game continues until you name all Koreans.
        7. At the end of the game, the result is displayed.
    """.trimIndent()

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setMessage(message)
            setPositiveButton("Ok") { dialog, _ ->
                // Снятие блокировки после закрытия диалогового окна
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                dialog.dismiss()
            }
            setCancelable(false)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }



    companion object {
        // Фабричный метод для создания экземпляра WelcomeFragment с передачей параметров
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WelcomeFragment().apply {
                arguments = Bundle().apply {
                    putString(WELCOME_FRAGMENT_ARG_PARAM1, param1)
                    putString(WELCOME_FRAGMENT_ARG_PARAM2, param2)
                }
            }
    }
}
