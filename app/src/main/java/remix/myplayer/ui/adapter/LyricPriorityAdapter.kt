package remix.myplayer.ui.adapter

import android.content.Context
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.item_lyric_priority.view.*
import remix.myplayer.bean.misc.LyricPriority
import remix.myplayer.ui.adapter.holder.BaseViewHolder
import remix.myplayer.util.SPUtil


class LyricPriorityAdapter(context: Context?, layoutId: Int) : BaseAdapter<LyricPriority, LyricPriorityAdapter.LyricPriorityHolder>(layoutId) {
    init {
        mDatas = Gson().fromJson(SPUtil.getValue(context, SPUtil.LYRIC_KEY.NAME, SPUtil.LYRIC_KEY.PRIORITY_LYRIC, SPUtil.LYRIC_KEY.DEFAULT_PRIORITY),
                object : TypeToken<List<LyricPriority>>() {}.type)
    }

    override fun convert(holder: LyricPriorityHolder?, d: LyricPriority?, position: Int) {
        holder?.view?.item_title?.text = d?.desc
        holder?.view?.setOnClickListener {

        }
    }

    class LyricPriorityHolder(val view: View) : BaseViewHolder(view)
}
