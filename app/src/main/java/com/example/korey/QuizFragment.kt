package com.example.korey

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.animation.Animator
import androidx.appcompat.app.AlertDialog
import android.view.WindowManager
import android.animation.AnimatorListenerAdapter
import android.view.animation.AccelerateDecelerateInterpolator


// Класс фрагмента для викторины
class QuizFragment : Fragment() {

    // Класс для представления информации об артистах
    data class Artist(val name: String, val position: String, val imageResourceId: Int)

    // Список корейцев
    private val artists = mutableListOf(
        Artist("Чхве Чон Хо ","(Ateez)", R.drawable.chve_chonhvo),
        Artist("Чхве Сан ","(Ateez)", R.drawable.chve_san),
        Artist("Ким Хон Джун ","(Ateez)", R.drawable.kim_honjun),
        Artist("Пак Сон Хва ","(Ateez)", R.drawable.pak_sonhva),
        Artist("Чон Юн Хо ","(Ateez)", R.drawable.yonxo_chon),
        Artist("Кан Ё Сан ","(Ateez)", R.drawable.kan_yosan),
        Artist("Сон Мин Ги ","(Ateez)", R.drawable.son_mingi),
        Artist("Чон У Ён ","(Ateez)", R.drawable.yeon_chon),

        Artist("Чхве Бом Гю ","(TXT)",R.drawable.chhve_bom_gu),
        Artist("Чхве Ён Джун ","(TXT)",R.drawable.chhve_en_jun),
        Artist("Чхве Су Бин  ","(TXT)",R.drawable.chve_su_bin),
        Artist("Кай Камал Хюнин ","(TXT)",R.drawable.kail_kamal_hunin),
        Artist("Кан Тхэ Хён ","(TXT)",R.drawable.kan_the_hen),

        Artist("Ким Дженни ","(Blackpink)",R.drawable.kim_djenni),
        Artist("Ким Джиcу ","(Blackpink)",R.drawable.kim_djesu),
        Artist("Пак Чхэён ","(Blackpink)",R.drawable.lalisa_monobin),
        Artist("Лалиса Манобан ","(Blackpink)",R.drawable.rose),
    )



    companion object {
        @JvmStatic
        fun newInstance(): QuizFragment {
            return QuizFragment()
        }
    }

    // Список оставшихся артистов для угадывания
    private val remainingArtists = mutableListOf<Artist>()

    // Счетчик угаданных и неугаданных артистов
    private var guessedCount = 0
    private var notGuessedCount = 0

    // Путь к сохраненному изображению
    private var savedImagePath: String? = null

    // Текущий кореец и визуальные компоненты
    private lateinit var currentArtist: Artist
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var buttonGuessed: Button
    private lateinit var buttonNotGuessed: Button
    private var isFirstClick = true

    // Создание фрагмента
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    // Инициализация фрагмента
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Помещение списка artists в Bundle
        val bundle = Bundle()
        bundle.putSerializable("artists", ArrayList(artists))


    // Инициализация визуальных компонентов
        imageView = view.findViewById(R.id.ImageView)
        textView = view.findViewById(R.id.TextView)
        buttonGuessed = view.findViewById(R.id.buttonGuessed)
        buttonNotGuessed = view.findViewById(R.id.buttonNotGuessed)

        // Загрузка списка артистов и начало игры
        remainingArtists.addAll(artists)
        showRandomArtist()

        // Установка обработчиков событий
        imageView.setOnClickListener {
            flipCard()
        }

        buttonGuessed.setOnClickListener {
            handleGuess(true)
        }

        buttonNotGuessed.setOnClickListener {
            handleGuess(false)
        }
    }


    // Поворот карточки
    private fun flipCard() {
        val rotateAnimator = ObjectAnimator.ofFloat(imageView, View.ROTATION_Y, 0f, 180f)
        rotateAnimator.duration = 1000 // Увеличение продолжительности анимации до 1000 миллисекунд
        rotateAnimator.interpolator = AccelerateDecelerateInterpolator() // Интерполятор для плавного изменения скорости

        val animatorListener = object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // После завершения анимации, меняется видимость текста
                if (isFirstClick) {
                    textView.visibility = View.VISIBLE
                    isFirstClick = false
                } else {
                    textView.visibility = View.GONE
                    isFirstClick = true
                }
            }
        }

        rotateAnimator.addListener(animatorListener)
        rotateAnimator.start()
    }



    // Показать случайного корейца
    private fun showRandomArtist() {
        if (remainingArtists.isEmpty()) {
            showResult()
            return
        }

        val randomIndex = (0 until remainingArtists.size).random()
        currentArtist = remainingArtists.removeAt(randomIndex)
        imageView.setImageResource(currentArtist.imageResourceId)
        textView.text = currentArtist.name
        textView.visibility = View.GONE
        isFirstClick = true
    }

    // Обработка угадывания артиста
    private fun handleGuess(correctGuess: Boolean) {
        if (correctGuess) {
            guessedCount++
        } else {
            notGuessedCount++
        }
        if (remainingArtists.isEmpty()) {
            showResult()
            return
        }
        showRandomArtist()
    }

    // Сброс игры
    private fun resetGame() {
        // сброс игры
        guessedCount = 0
        notGuessedCount = 0
        remainingArtists.clear()
        remainingArtists.addAll(artists)
        showRandomArtist()
    }

    // Показать результат игры
    private fun showResult() {
        // блокировка остального экрана от нажатий
        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        val message = "Correctly named $guessedCount \nIncorrect $notGuessedCount. Would you like to try again?"
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setMessage(message)
            setPositiveButton("Да") { dialog, _ ->
                resetGame()
                // снятие блокировки после закрытия диалогового окна
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                dialog.dismiss()
            }
            setNegativeButton("Нет") { dialog, _ ->
                //  Возврат к WelcomeFragment
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, WelcomeFragment.newInstance("param1", "param2"))
                    .commit()
                dialog.dismiss()
            }
            setCancelable(false)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}