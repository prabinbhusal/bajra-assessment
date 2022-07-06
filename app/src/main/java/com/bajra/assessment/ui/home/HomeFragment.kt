package com.bajra.assessment.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Size
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import android.widget.TextView.OnEditorActionListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bajra.assessment.R
import com.bajra.assessment.databinding.FragmentHomeBinding
import com.bajra.assessment.utils.hide
import com.bajra.assessment.utils.show
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var isMenuShown = true

    private var popupWindow: PopupWindow? = null

    private var mXDelta = 0
    private var mYDelta = 0
    private var mRootWidth = 0
    private var mRootHeight = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViews()
    }

    private fun setViews() {
        binding.measure.setOnClickListener {
            val orientation = this.resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                // code for portrait mode
                if (isMenuShown) {
                    showPopupWindowPortrait(binding.measure)
                    binding.imageViewMeasure.setColorFilter(
                        ContextCompat.getColor(
                            activity!!,
                            R.color.yellow
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.textViewMeasure.setTextColor(
                        ContextCompat.getColor(
                            activity!!,
                            R.color.yellow
                        )
                    )
                } else {
                    binding.imageViewMeasure.setColorFilter(
                        ContextCompat.getColor(
                            activity!!,
                            R.color.white_60
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.textViewMeasure.setTextColor(
                        ContextCompat.getColor(
                            activity!!,
                            R.color.white_60
                        )
                    )
                    popupWindow!!.dismiss()
                    isMenuShown = true
                }

            } else {
                // code for landscape mode
                if (isMenuShown) {
                    showPopupWindowLandscape(binding.measure)
                    binding.imageViewMeasure.setColorFilter(
                        ContextCompat.getColor(
                            activity!!,
                            R.color.yellow
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.textViewMeasure.setTextColor(
                        ContextCompat.getColor(
                            activity!!,
                            R.color.yellow
                        )
                    )
                } else {
                    binding.imageViewMeasure.setColorFilter(
                        ContextCompat.getColor(
                            activity!!,
                            R.color.white_60
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.textViewMeasure.setTextColor(
                        ContextCompat.getColor(
                            activity!!,
                            R.color.white_60
                        )
                    )
                    popupWindow!!.dismiss()
                    isMenuShown = true
                }
            }

        }

        binding.imageViewFullscreen.setOnClickListener {
            binding.fab.hide()
            binding.layoutBottom.hide()
            binding.imageViewExitFullscreen.show()
        }

        binding.imageViewExitFullscreen.setOnClickListener {
            binding.fab.show()
            binding.layoutBottom.show()
            binding.imageViewFullscreen.show()
            binding.imageViewExitFullscreen.hide()
        }

        binding.annotate.setOnClickListener {
            binding.editTextComment.show()
            binding.editTextComment.requestFocus()
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.showSoftInput(binding.editTextComment, InputMethodManager.SHOW_IMPLICIT)
            requireActivity().window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

            setEventListener(
                requireActivity(),
                KeyboardVisibilityEventListener {
                    if (it) {
                        if (activity != null) {
                            binding.editTextComment.show()
                            binding.textViewComment.hide()
                        }
                    }
                })

        }

        setEventListener(
            requireActivity(),
            KeyboardVisibilityEventListener {
                if (!it) {
                    if (activity != null) {
                        binding.editTextComment.hide()
                        binding.textViewComment.show()
                        binding.textViewComment.text =
                            binding.editTextComment.text.toString().trim()
                    }
                }
            })

        binding.editTextComment.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.editTextComment.hide()
                binding.textViewComment.show()
                binding.textViewComment.text = binding.editTextComment.text.toString().trim()


                val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                true
            } else false
        })


        binding.layoutTop.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    binding.layoutTop.viewTreeObserver
                        .removeOnGlobalLayoutListener(this)
                }
                mRootWidth = binding.layoutTop.width
                mRootHeight = binding.layoutTop.height
            }
        })

        binding.textViewComment.setOnTouchListener(mOnTouchListener);


    }

    @SuppressLint("ClickableViewAccessibility")
    var mOnTouchListener: View.OnTouchListener = View.OnTouchListener { view, event ->
        val xScreenTouch = event.rawX.toInt()
        val yScreenTouch = event.rawY.toInt()
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                val lParams = view.layoutParams as ConstraintLayout.LayoutParams
                mXDelta = xScreenTouch - lParams.leftMargin
                mYDelta = yScreenTouch - lParams.topMargin
            }
            MotionEvent.ACTION_MOVE -> {
                val layoutParams = view
                    .layoutParams as ConstraintLayout.LayoutParams
                layoutParams.leftMargin =
                    Math.max(0, Math.min(mRootWidth - view.width, xScreenTouch - mXDelta))
                layoutParams.topMargin =
                    Math.max(0, Math.min(mRootHeight - view.height, yScreenTouch - mYDelta))
                view.layoutParams = layoutParams
            }
        }
        true
    }

    private fun showPopupWindowPortrait(anchor: View) {
        isMenuShown = false
        popupWindow = PopupWindow(anchor.context).apply {
            val inflater = LayoutInflater.from(anchor.context)
            contentView = inflater.inflate(R.layout.layout_measure_menu, null).apply {
                measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
            }
        }.also { popupWindow ->
            val location = IntArray(2).apply {
                anchor.getLocationOnScreen(this)
            }
            val size = Size(
                popupWindow.contentView.measuredWidth,
                popupWindow.contentView.measuredHeight
            )
            popupWindow.showAtLocation(
                anchor,
                Gravity.TOP or Gravity.START,
                location[0] - (size.width - anchor.width) / 2,
                location[1] - size.height
            )

        }
        popupWindow!!.showAsDropDown(binding.measure)
    }

    private fun showPopupWindowLandscape(anchor: View) {
        isMenuShown = false
        popupWindow = PopupWindow(anchor.context).apply {
            val inflater = LayoutInflater.from(anchor.context)
            contentView = inflater.inflate(R.layout.layout_measure_menu, null).apply {
                measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
            }
        }.also { popupWindow ->
            val location = IntArray(2).apply {
                anchor.getLocationOnScreen(this)
            }
            val size = Size(
                popupWindow.contentView.measuredWidth,
                popupWindow.contentView.measuredHeight
            )
            popupWindow.showAtLocation(
                anchor,
                Gravity.END or Gravity.START,
                location[0] - size.height,
                location[1] -  (size.width - anchor.width) / 2

                )

        }
        popupWindow!!.showAsDropDown(binding.measure)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}