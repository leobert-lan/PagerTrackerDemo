package osp.leobert.android.pagertrackerdemo.index.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "IndexFragment\n假装这里是feed流，点击后进入了一个文章详情，模拟id是321"
    }
    val text: LiveData<String> = _text
}
