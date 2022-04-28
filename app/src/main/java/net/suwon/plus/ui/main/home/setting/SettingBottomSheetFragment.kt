package net.suwon.plus.ui.main.home.setting

import android.app.UiModeManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.suwon.plus.R
import net.suwon.plus.databinding.SettingBottomSheetBinding

class SettingBottomSheetFragment : BottomSheetDialogFragment() {


    private var _binding: SettingBottomSheetBinding? = null
    val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SettingBottomSheetBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViews()
    }


    private fun initViews() = with(binding) {
        when (arguments?.getInt(THEME)) {
            UiModeManager.MODE_NIGHT_NO -> {
                lightModeTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorOnPrimary
                    )
                )

            }
            UiModeManager.MODE_NIGHT_YES -> {
                darkModeTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorOnPrimary
                    )
                )
            }

            else -> {
                systemModeTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorOnPrimary
                    )
                )
            }
        }
    }


    private fun bindViews() = with(binding) {
        systemModeTextView.setOnClickListener {
            itemClickListener(Int.MIN_VALUE)
            this@SettingBottomSheetFragment.dismiss()
        }
        lightModeTextView.setOnClickListener {
            itemClickListener(UiModeManager.MODE_NIGHT_NO)
            this@SettingBottomSheetFragment.dismiss()
        }
        darkModeTextView.setOnClickListener {
            itemClickListener(UiModeManager.MODE_NIGHT_YES)
            this@SettingBottomSheetFragment.dismiss()
        }
        cancelTextView.setOnClickListener {
            this@SettingBottomSheetFragment.dismiss()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        fun newInstance(
            themeId: Int,
            itemClickListener: (Int) -> Unit
        ): SettingBottomSheetFragment {
            this.itemClickListener = itemClickListener

            return SettingBottomSheetFragment().apply {
                arguments = bundleOf(
                    THEME to themeId
                )
            }
        }


        lateinit var itemClickListener: (Int) -> Unit


        private const val THEME = "Theme"
        const val TAG = "SettingBottomSheetFragment"
    }

}