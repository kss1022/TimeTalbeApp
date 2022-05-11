package net.suwon.plus.ui.main.memo.folder.editmemo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import net.suwon.plus.R
import net.suwon.plus.data.entity.memo.MemoEntity
import net.suwon.plus.databinding.FragmentEditMemoBinding
import net.suwon.plus.extensions.toReadableDateString
import net.suwon.plus.extensions.toReadableTimeString
import net.suwon.plus.model.MemoModel
import net.suwon.plus.ui.base.BaseFragment
import net.suwon.plus.ui.main.memo.folder.editmemo.gallery.GalleryActivity
import java.util.*
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

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){granted->
        if(granted){
            startActivity(GalleryActivity.newIntent(requireContext()))
        }else{
            showSystemSettingDialog(requireActivity())
        }
    }

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

        if(memo.time != 0L ){
            val date = Date(memo.time)
            lastUpdatedTextView.visibility = View.VISIBLE
            lastUpdatedTextView.text = getString(R.string.editTime , date.toReadableDateString() , date.toReadableTimeString())
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

        galleryButton.setOnClickListener {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
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
        lp.height = ConstraintLayout.LayoutParams.MATCH_PARENT
        titleEditText.layoutParams = lp
    }


    private fun changeHeightMinTitleTextView() = with(binding){
        val lp = titleEditText.layoutParams
        lp.height =ConstraintLayout.LayoutParams.WRAP_CONTENT
        titleEditText.layoutParams = lp
    }


    private fun showSystemSettingDialog(activity: Activity) {
        AlertDialog.Builder(activity)
            .setMessage(getString(R.string.please_check_storage_permission))
            .setPositiveButton(getString(R.string.detail_setting)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivity(intent)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }


}