package net.suwon.plus.ui.main.memo.folder.editmemo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import net.suwon.plus.R
import net.suwon.plus.data.entity.media.Media
import net.suwon.plus.data.entity.memo.MemoEntity
import net.suwon.plus.databinding.FragmentEditMemoBinding
import net.suwon.plus.extensions.toReadableDateString
import net.suwon.plus.extensions.toReadableTimeString
import net.suwon.plus.model.MemoModel
import net.suwon.plus.ui.base.BaseFragment
import net.suwon.plus.ui.main.memo.folder.editmemo.editimage.EditImageDetailActivity
import net.suwon.plus.ui.main.memo.folder.editmemo.gallery.GalleryActivity
import net.suwon.plus.util.PagingConstants
import net.suwon.plus.widget.adapter.mediaadpater.MediaImageClickListener
import net.suwon.plus.widget.adapter.mediaadpater.MemoImageAdapter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
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


    private lateinit var backPressCallback: OnBackPressedCallback

    private val argument: EditMemoFragmentArgs by navArgs()

    private lateinit var memo: MemoModel
    private var needSave = true

    private val imageUrlList by lazy {
        memo.imageUrlList.toMutableList()
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                needSave = false
                galleryLauncher.launch(GalleryActivity.newIntent(requireContext()))
            } else {
                showSystemSettingDialog(requireActivity())
            }
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { intent ->
                    needSave = true
                    val mediaArray: ArrayList<Media> =
                        intent.getParcelableArrayListExtra(GET_IMAGE) ?: ArrayList()
                    saveFile(mediaArray)
                }
            }
        }

    private val editImageDetailLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode == Activity.RESULT_OK){
                result.data?.let { intent->
                    needSave = true
                    val mediaArray: ArrayList<String> =
                        intent.getStringArrayListExtra(DELETE_IMAGE) ?: ArrayList()
                    deleteFile(mediaArray)
                }
            }
        }




    override fun onAttach(context: Context) {
        super.onAttach(context)
        backPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressCallback)
    }


    override fun onDetach() {
        super.onDetach()
        backPressCallback.remove()
    }


    override fun observeData() {
    }

    override fun onStop() {
        if (needSave) saveMemo()
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

        if (memo.time != 0L) {
            val date = Date(memo.time)
            lastUpdatedTextView.visibility = View.VISIBLE
            lastUpdatedTextView.text = getString(
                R.string.editTime,
                date.toReadableDateString(),
                date.toReadableTimeString()
            )
        }

    }

    private fun bindViews() = with(binding) {
        titleEditText.setOnKeyListener { _, keyCode, event ->
            if ((titleEditText.selectionStart == titleEditText.length() || titleEditText.selectionEnd == titleEditText.length()) &&
                keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN
            ) {
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
            if ((memoEditText.selectionStart == 0 || memoEditText.selectionEnd == 0) &&
                keyCode == KeyEvent.KEYCODE_DEL &&
                event.action == KeyEvent.ACTION_DOWN
            ) {
                titleEditText.requestFocus()
                titleEditText.setSelection(titleEditText.length())

                if (memoEditText.length() == 0) {
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

        showRecyclerViewButton.setOnClickListener {
            if (recyclerView.isVisible) {
                hideRecyclerView()
            } else {
                showRecyclerView()

            }
        }
    }


    private fun saveMemo() {
        val titleStr = binding.titleEditText.text.toString()
        val memoStr = binding.memoEditText.text.toString()

        if (memo.id == -1L) {
            if ((titleStr.isEmpty() && memoStr.isEmpty()).not() || imageUrlList.isNullOrEmpty().not()) {
                viewModel.insertMemo(
                    MemoEntity(
                        title = titleStr,
                        memo = memoStr,
                        time = System.currentTimeMillis(),
                        memoFolderId = memo.memoFolderId,
                        imageUrlList = imageUrlList
                    )
                )
            }
        } else {
            if ((titleStr == memo.title && memoStr == memo.memo).not()) {
                viewModel.updateMemo(
                    MemoEntity(
                        memoId = memo.id,
                        title = titleStr,
                        memo = memoStr,
                        time = System.currentTimeMillis(),
                        memoFolderId = memo.memoFolderId,
                        timeTableCellId = memo.timeTableCellId,
                        imageUrlList = imageUrlList
                    )
                )
            }
        }
    }


    private fun changeHeightMaxTitleTextView() = with(binding) {
        val lp = titleEditText.layoutParams
        lp.height = ConstraintLayout.LayoutParams.MATCH_PARENT
        titleEditText.layoutParams = lp
    }


    private fun changeHeightMinTitleTextView() = with(binding) {
        val lp = titleEditText.layoutParams
        lp.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        titleEditText.layoutParams = lp
    }


    private fun showRecyclerView() = with(binding) {
        recyclerView.visibility = View.VISIBLE
        recyclerView.layoutManager = GridLayoutManager(context, PagingConstants.DEFAULT_SPAN_COUNT)

        val adapter = MemoImageAdapter(object : MediaImageClickListener {
            override fun itemClick(position: Int) {
                editImageDetailLauncher.launch(EditImageDetailActivity.newIntent(requireContext()).apply {
                    putExtra(EXTRA_IMAGE_LIST , ArrayList(imageUrlList))
                    putExtra(EXTRA_IMAGE_POSITION, position)
                })
            }
        })

        adapter.setUrlList(imageUrlList)
        recyclerView.adapter = adapter

        showRecyclerViewButton.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_baseline_keyboard_arrow_down_24
            )
        )
    }

    private fun hideRecyclerView() = with(binding) {
        recyclerView.isGone = true
        showRecyclerViewButton.setImageDrawable(
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_up_24)
        )
    }


    private fun saveFile(mediaArray: ArrayList<Media>) {
        mediaArray.forEach { media ->
            saveFile(media.getUri(), media.name)
        }


        val titleStr = binding.titleEditText.text.toString()
        val memoStr = binding.memoEditText.text.toString()

        if (memo.id != -1L) {
            viewModel.updateMemo(
                MemoEntity(
                    memoId = memo.id,
                    title = titleStr,
                    memo = memoStr,
                    time = System.currentTimeMillis(),
                    memoFolderId = memo.memoFolderId,
                    timeTableCellId = memo.timeTableCellId,
                    imageUrlList = imageUrlList
                )
            )
        }



        showRecyclerView()
    }

    private fun saveFile(uri: Uri, name: String) {
        val bimMap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    requireActivity().contentResolver,
                    uri
                )
            )
        } else {
            MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
        }


        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {

            //Create folder if is not exists
            val file = createExternalFolder()
            try {
                val imageFile = File(file, "${name}.${System.currentTimeMillis()}.png")

                val stream = FileOutputStream(imageFile)
                bimMap.compress(Bitmap.CompressFormat.PNG, 100, stream)

                stream.flush()
                stream.close()

                imageUrlList.add(imageFile.absolutePath)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Save Failed", Toast.LENGTH_SHORT).show()
                return
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Save Failed", Toast.LENGTH_SHORT).show()
                return
            }

        } else {
            Toast.makeText(requireContext(), "Directory can't use", Toast.LENGTH_SHORT).show()
        }

    }


    private fun deleteFile(mediaArray: ArrayList<String>) {
        mediaArray.forEach { url->
            imageUrlList.remove(url)
            File(url).delete()
        }

        showRecyclerView()
    }


    private fun createExternalFolder(): File {
        val file =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "/memo")

        if (!file.exists()) {
            file.mkdirs();
        }

        return file
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

    companion object {
        const val GET_IMAGE = "get_image"
        const val DELETE_IMAGE = "delete_image"

        const val EXTRA_IMAGE_LIST ="image_list"
        const val EXTRA_IMAGE_POSITION ="image_position"
    }
}