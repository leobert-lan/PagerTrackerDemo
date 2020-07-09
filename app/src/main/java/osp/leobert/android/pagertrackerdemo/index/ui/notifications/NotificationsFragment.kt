package osp.leobert.android.pagertrackerdemo.index.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_demo_notify.*
import osp.leobert.android.pagertrackerdemo.BaseFragment
import osp.leobert.android.pagertrackerdemo.R
import osp.leobert.android.pagertrackerdemo.bio.BioActivity
import osp.leobert.android.tracker.pager.ITrackedPager
import osp.leobert.android.tracker.pager.TrackedPager


@TrackedPager(pagerPoint = "P_NOTIFY_FG", whenFragment = TrackedPager.FragmentStrategy.REPLACE_ACTIVITY)
class NotificationsFragment : BaseFragment(), ITrackedPager.FragmentInViewPager {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_demo_notify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notification_rv.layoutManager = LinearLayoutManager(notification_rv.context)
        notificationsViewModel.notifications.observe(viewLifecycleOwner, Observer {
            notification_rv.adapter = NotificationsAdapter(it)
        })
    }
}

class NotificationVH(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.vh_notification, parent, false)) {

    val tvName = itemView.findViewById<TextView>(R.id.tv_user)
    val tvMsg = itemView.findViewById<TextView>(R.id.tv_msg)
    var notification: Notification? = null

    init {

        itemView.setOnClickListener { v ->
            notification?.let {
                BioActivity.launch(v.context, it.userId)
            }
        }
    }

    fun bind(notification: Notification) {
        this.notification = notification
        tvName.text = notification.userName
        tvMsg.text = notification.message
    }

}

class NotificationsAdapter(val list: List<Notification>) : RecyclerView.Adapter<NotificationVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationVH = NotificationVH(parent)

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NotificationVH, position: Int) {
        holder.bind(list[position])
    }

}