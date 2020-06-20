package osp.leobert.android.tracker.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

/**
 * <p><b>Package:</b> osp.leobert.android.tracker.pager </p>
 * <p><b>Classname:</b> FragmentFactoryWrapper </p>
 * Created by leobert on 2020/5/19.
 */
class FragmentFactoryWrapper(val wrapper: FragmentFactory) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return wrapper.instantiate(classLoader, className).apply {
            this.lifecycle.addObserver(FragmentTrackerImpl())
        }
    }
}