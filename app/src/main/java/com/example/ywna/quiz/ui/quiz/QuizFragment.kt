package com.example.ywna.quiz.ui.quiz

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.ywna.quiz.R
import com.example.ywna.quiz.ui.result.ResultFragment
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_quiz.*
import java.util.*
import kotlin.collections.ArrayList


class QuizFragment : Fragment(), View.OnClickListener, IOnBackPressed {


    private var viewModel: QuizViewModel = QuizViewModel()
    private var countDownTimer: CountDownTimer? = null
    private var textQuizCount: TextView? = null
    private var textTotalQuiz: TextView? = null
    private var textQuestion: TextView? = null
    private var optionAButton: Button? = null
    private var optionBButton: Button? = null
    private var optionCButton: Button? = null
    private var optionDButton: Button? = null
    private var rightAnswer: String? = null
    private var rightAnswerCount = 0
    private var quizCount = 1
    private var totalQuiz = 0
    private var time: Long = 10

    private var questionArray = ArrayList<ArrayList<String>>()
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_quiz, container, false)

        databaseReference = FirebaseDatabase.getInstance().reference.child("questionBank")

        initView(root)
        loadQuizTime()
        loadQuestions()
        return root
    }

    private fun initView(root: View) {
        textQuizCount = root.findViewById(R.id.quiz_count)
        textTotalQuiz = root.findViewById(R.id.total_quiz)
        textQuestion = root.findViewById(R.id.txt_question)
        optionAButton = root.findViewById(R.id.option_a)
        optionBButton = root.findViewById(R.id.option_b)
        optionCButton = root.findViewById(R.id.option_c)
        optionDButton = root.findViewById(R.id.option_d)

        optionAButton?.setOnClickListener(this)
        optionBButton?.setOnClickListener(this)
        optionCButton?.setOnClickListener(this)
        optionDButton?.setOnClickListener(this)
    }

    private fun loadQuestions() {
        viewModel.getQuestions().observe(requireActivity(), Observer { questions ->
            questionArray = questions
            totalQuiz = questionArray.size
            showNextQuiz()
        })
    }

    private fun loadQuizTime() {
        viewModel.getQuestionTime().observe(requireActivity(), androidx.lifecycle.Observer { quizTime ->
            time = quizTime
            countDownTimer()
        })
    }

    private fun countDownTimer() {
        countDownTimer = object : CountDownTimer(time * 1000, 1000) {

            @TargetApi(Build.VERSION_CODES.M)
            override fun onTick(millisUntilFinished: Long) {
                timer.text = ((millisUntilFinished / 1000).toString())
                if (millisUntilFinished <= 20000) {
                    context?.let { ContextCompat.getColor(it, R.color.colorPrimary) }?.let {
                        timer.setTextColor(
                            it
                        )
                        timer.compoundDrawableTintList =
                            ContextCompat.getColorStateList(context!!, R.color.colorPrimary)
                    }
                }
            }

            override fun onFinish() {
                moveToResultActivity()
            }
        }
        countDownTimer?.start()
    }

    override fun onClick(view: View?) {
        when (val id = view?.id) {
            R.id.option_a -> {
                checkAnswer(id)
            }
            R.id.option_b -> {
                checkAnswer(id)
            }
            R.id.option_c -> {
                checkAnswer(id)
            }
            R.id.option_d -> {
                checkAnswer(id)
            }
        }
    }

    private fun showNextQuiz() {
        textQuizCount?.text = quizCount.toString()
        textTotalQuiz?.text = getString(R.string.total_quiz, totalQuiz)

        val random = Random()
        val randomNum = random.nextInt(questionArray.size)

        val quiz = questionArray[randomNum]

        textQuestion?.text = quiz[0]
        rightAnswer = quiz[1]

        quiz.removeAt(0)
        quiz.shuffle()

        optionAButton?.text = (quiz[0])
        optionBButton?.text = (quiz[1])
        optionCButton?.text = (quiz[2])
        optionDButton?.text = (quiz[3])

        questionArray.removeAt(randomNum)
    }

    private fun checkAnswer(id: Int) {
        val answerBtn = activity?.findViewById<Button>(id)
        val btnText = answerBtn?.text

        if (btnText == rightAnswer) {
            answerBtn?.background =
                context?.let { ContextCompat.getDrawable(it, R.drawable.correct_option_bg) }
            rightAnswerCount++
        } else {
            answerBtn?.background =
                context?.let { ContextCompat.getDrawable(it, R.drawable.wrong_option_bg) }
        }
        Handler().postDelayed({
            when (quizCount) {
                totalQuiz -> {
                    moveToResultActivity()
                }
                else -> {
                    quizCount++
                    showNextQuiz()
                    setupDefaultButton()
                }
            }
        }, 2000)
    }

    private fun moveToResultActivity() {
        countDownTimer?.cancel()
        val bundle = Bundle()
        bundle.putInt(ResultFragment.CORRECT_ANSWER, rightAnswerCount)
        bundle.putInt(ResultFragment.TOTAL_QUESTION, totalQuiz)
        view?.findNavController()?.navigate(R.id.navigation_result, bundle)
    }

    private fun setupDefaultButton() {
        optionAButton?.background =
            context?.let { ContextCompat.getDrawable(it, R.drawable.option_button_bg) }
        optionBButton?.background =
            context?.let { ContextCompat.getDrawable(it, R.drawable.option_button_bg) }
        optionCButton?.background =
            context?.let { ContextCompat.getDrawable(it, R.drawable.option_button_bg) }
        optionDButton?.background =
            context?.let { ContextCompat.getDrawable(it, R.drawable.option_button_bg) }
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}

interface IOnBackPressed {
    fun onBackPressed(): Boolean
}