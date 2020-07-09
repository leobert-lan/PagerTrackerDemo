package osp.leobert.android.pagertrackerdemo.index.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _notifications = MutableLiveData<List<Notification>>().apply {
        value = arrayListOf(
            Notification("李白", "李白的id", "准备点菜，我马上找你来喝酒"),
            Notification("杜甫", "杜甫的id", "我和李白一起来"),
            Notification("李白","李白的id","多准备点，小杜想要一起来")
        )
    }

    val notifications: LiveData<List<Notification>> = _notifications

}

data class Notification(val userName: String, val userId: String, val message: String)