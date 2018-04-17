package com.tufer.mylove.frags.search;


import com.tufer.common.app.Fragment;
import com.tufer.mylove.R;
import com.tufer.mylove.activities.SearchActivity;

/**
 * 搜索群的界面实现
 */
public class SearchGroupFragment extends Fragment
        implements SearchActivity.SearchFragment {


    public SearchGroupFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void search(String content) {

    }
}
