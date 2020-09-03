package remix.myplayer.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import remix.myplayer.R;
import remix.myplayer.bean.mp3.Song;
import remix.myplayer.misc.asynctask.WrappedAsyncTaskLoader;
import remix.myplayer.misc.interfaces.LoaderIds;
import remix.myplayer.misc.interfaces.OnItemClickListener;
import remix.myplayer.service.Command;
import remix.myplayer.ui.adapter.SongAdapter;
import remix.myplayer.ui.widget.fastcroll_recyclerview.LocationRecyclerView;
import remix.myplayer.util.MediaStoreUtil;

import static remix.myplayer.helper.MusicServiceRemote.setPlayQueue;
import static remix.myplayer.service.MusicService.EXTRA_POSITION;
import static remix.myplayer.util.MusicUtil.makeCmdIntent;

/**
 * Created by Remix on 2015/11/30.
 */

/**
 * 全部歌曲的Fragment
 */
public class SongFragment extends LibraryFragment<Song, SongAdapter> {

    public static final String TAG = SongFragment.class.getSimpleName();
    @BindView(R.id.location_recyclerView)
    LocationRecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageName = TAG;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_song;
    }

    @Override
    protected void initAdapter() {
        mAdapter = new SongAdapter(R.layout.item_song_recycle, mChoice, mRecyclerView);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (getUserVisibleHint() && !mChoice.click(position, mAdapter.getDatas().get(position))) {
                    //设置正在播放列表
                    final List<Song> songs = mAdapter.getDatas();
                    if (songs == null || songs.isEmpty()) {
                        return;
                    }
                    setPlayQueue(songs, makeCmdIntent(Command.PLAYSELECTEDSONG)
                            .putExtra(EXTRA_POSITION, position));

                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (getUserVisibleHint()) {
                    mChoice.longClick(position, mAdapter.getDatas().get(position));
                }
            }
        });

    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected Loader<List<Song>> getLoader() {
        return new AsyncSongLoader(mContext);
    }

    @Override
    protected int getLoaderId() {
        return LoaderIds.SONG_FRAGMENT;
    }


    @Override
    public SongAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onMetaChanged() {
        super.onMetaChanged();
        if (mAdapter != null) {
            mAdapter.updatePlayingSong();
        }
    }

    public void scrollToCurrent() {
        mRecyclerView.smoothScrollToCurrentSong(mAdapter.getDatas());
    }

    private static class AsyncSongLoader extends WrappedAsyncTaskLoader<List<Song>> {

        private AsyncSongLoader(Context context) {
            super(context);
        }

        @Override
        public List<Song> loadInBackground() {
            return MediaStoreUtil.getAllSong();
        }
    }
}
