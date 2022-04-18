package com.example.suwon_university_community.ui.main.memo.folder.editmemo

import android.content.Context
import android.view.KeyEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.suwon_university_community.data.entity.memo.MemoEntity
import com.example.suwon_university_community.databinding.FragmentEditMemoBinding
import com.example.suwon_university_community.model.MemoModel
import com.example.suwon_university_community.ui.base.BaseFragment
import javax.inject.Inject

//todo 뒤로 가기 했을떄 바로 저장할것인지 물어본다.
// 설정에서 Auto Save 할 것인지 설정가능하게 만든다.


class EditMemoFragment : BaseFragment<EditMemoViewModel, FragmentEditMemoBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: EditMemoViewModel by viewModels { viewModelFactory }

    override fun getViewBinding(): FragmentEditMemoBinding =
        FragmentEditMemoBinding.inflate(layoutInflater)


    private lateinit var callback: OnBackPressedCallback

    private val argument: EditMemoFragmentArgs by navArgs()

    private lateinit var memo: MemoModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }





    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }


    override fun observeData() {
    }

    override fun onStop() {
        saveMemo()
        super.onStop()
    }


    override fun initViews() {
        memo = argument.memo

        initMemo()
        bindViews()
    }

    private fun initMemo() = with(binding) {
        if (memo.title.isEmpty().not()) {
            titleEditText.setText(memo.title)
        }

        if (memo.memo.isEmpty().not()) {
            changeHeightMinTitleTextView()
            memoEditText.visibility = View.VISIBLE
            memoEditText.setText(memo.memo)
            memoEditText.setSelection(memoEditText.length())
        } else {
            memoEditText.isGone = true
        }

    }

    private fun bindViews() = with(binding) {
        titleEditText.setOnKeyListener { _, keyCode, event ->
            if ( (titleEditText.selectionStart == titleEditText.length() || titleEditText.selectionEnd == titleEditText.length()) &&
                keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                changeHeightMinTitleTextView()

                memoEditText.visibility = View.VISIBLE
                memoEditText.requestFocus()
                memoEditText.setSelection(0)
                true
            } else {
                false
            }
        }

        memoEditText.setOnKeyListener { _, keyCode, event ->
            if ( (memoEditText.selectionStart == 0 || memoEditText.selectionEnd == 0)&&
                keyCode == KeyEvent.KEYCODE_DEL &&
                event.action == KeyEvent.ACTION_DOWN) {
                titleEditText.requestFocus()
                titleEditText.setSelection(titleEditText.length())

                if(memoEditText.length() == 0){
                    changeHeightMaxTitleTextView()
                }
                true
            } else {
                false
            }
        }


    }


    private fun saveMemo() {
        val titleStr = binding.titleEditText.text.toString()
        val memoStr  =binding.memoEditText.text.toString()

        if (memo.id == -1L) {
            if( (titleStr.isEmpty() && memoStr.isEmpty()).not() ){
                viewModel.insertMemo(
                    MemoEntity(
                        title = titleStr,
                        memo = memoStr,
                        time = System.currentTimeMillis(),
                        memoFolderId = memo.memoFolderId,
                    )
                )
            }
        } else {
            if( (titleStr==memo.title && memoStr == memo.memo).not()){
                viewModel.updateMemo(
                    MemoEntity(
                        memoId = memo.id,
                        title = titleStr,
                        memo = memoStr,
                        time = System.currentTimeMillis(),
                        memoFolderId = memo.memoFolderId,
                        timeTableCellId = memo.timeTableCellId
                    )
                )
            }
        }
    }



    private fun changeHeightMaxTitleTextView() = with(binding){
        val lp = titleEditText.layoutParams
        lp.height =ConstraintLayout.LayoutParams.MATCH_PARENT
        titleEditText.layoutParams = lp
    }


    private fun changeHeightMinTitleTextView() = with(binding){
        val lp = titleEditText.layoutParams
        lp.height =ConstraintLayout.LayoutParams.WRAP_CONTENT
        titleEditText.layoutParams = lp
    }


}