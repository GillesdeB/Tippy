// From Android App Development Tutorial for Beginners - Your First App
// Latest update: 2022-01-10
//        https://www.youtube.com/watch?v=FjrKMcnKahY
// Using: https://emojipedia.org/red-heart/
//        https://color.adobe.com/create/color-wheel
// ToDo: save/restore state when app goes to or returnd gtom bsckgtound
// ToDo: allow palindrome
// ToDo: enter amount paid and show the money to get back

package com.smartbumpersticker.tippy

import android.animation.ArgbEvaluator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
private const val INITIAL_NUMBER_OF_PARTICIPANTS = 1

class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView
    private lateinit var etNumberOfParticipants: EditText
    private lateinit var tvTotalPerParticipant: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.sbTip)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)
        etNumberOfParticipants = findViewById(R.id.etNumberOfParticipants)
        tvTotalPerParticipant = findViewById(R.id.tvTotalPerParticipant)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        etNumberOfParticipants.setText("$INITIAL_NUMBER_OF_PARTICIPANTS")

        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // Log.i(TAG, msg: "onProgressChanged $progress")
                tvTipPercentLabel.text = "$progress%"
                computeTipAndTotals()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
        etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Log.i(TAG, msg"afterTextChanged $s")
                computeTipAndTotals()
            }

        })
        etNumberOfParticipants.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Log.i(TAG, msg"afterTextChanged $s")
                computeTipAndTotals()
            }

        })
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription
        // Update the color based on the tip %
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip),
        ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotals() {
        if (etBaseAmount.text.isEmpty() or etNumberOfParticipants.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            tvTotalPerParticipant.text = ""
            return
        }
        // 1. Get the value of the base and tip percent
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        // 2. Compute the tip and total
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        // 3. Update the UI
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
        // 4. Compute the total per participant
        val numberOfParticipants = etNumberOfParticipants.text.toString().toDouble()
        val totalAmountPerParticipant = totalAmount / numberOfParticipants
        // 5. Update the UI
        tvTotalPerParticipant.text = "%.2f".format(totalAmountPerParticipant)
    }

}
