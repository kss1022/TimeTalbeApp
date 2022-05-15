package net.suwon.plus.ui.main.memo.folder.editmemo.editimage

import androidx.lifecycle.MutableLiveData
import net.suwon.plus.ui.base.BaseViewModel
import net.suwon.plus.util.lifecycle.SingleLiveEvent
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class EditImageDetailViewModel @Inject constructor(
) : BaseViewModel() {

    val selection: EditImageDetailSelection =  EditImageDetailSelection()
    var itemCount : Int = 0
    val bindingItemAdapterPosition = AtomicInteger(-1)

    val checkBoxEnabled = MutableLiveData(true)
    val isChecked = MutableLiveData<Boolean>()
    var currentMediaItem: Pair< Int, String?> = -1 to null
    val checkBoxClickEvent = SingleLiveEvent< Pair< Int,String?>>()

    fun onCheckBoxClick() {
        checkBoxClickEvent.value = currentMediaItem
    }


    fun getSelectedMediaList(): List<String> {
        return selection.toList()
    }

}