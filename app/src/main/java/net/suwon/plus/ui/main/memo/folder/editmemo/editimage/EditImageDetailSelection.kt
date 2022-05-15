package net.suwon.plus.ui.main.memo.folder.editmemo.editimage

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.parcelize.Parcelize

@Parcelize
class EditImageDetailSelection(private val selectedItems: LinkedHashMap<Int, String> = LinkedHashMap()) :
    Parcelable {

    private val count = MutableLiveData(selectedItems.size)

    fun select(id: Int, item: String) {
        selectedItems[id] = item
        count.value = selectedItems.size
    }

    private fun deselect(id: Int) {
        selectedItems.remove(id)
        count.value = selectedItems.size
    }

    fun toggle(id: Int, item: String) {
        if (isSelected(id)) {
            deselect(id)
        } else {
            select(id, item)
        }
    }

    fun isSelected(id: Int): Boolean {
        return selectedItems.containsKey(id)
    }

    fun toList(): List<String> {
        return selectedItems.values.toList()
    }

    fun isEmpty() = selectedItems.isEmpty()

    fun getCount(): LiveData<Int> = count

}