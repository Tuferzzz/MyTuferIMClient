package com.tufer.common.widget.recycler;

/**
 * Created by Administrator on 2018/3/26 0026.
 */
public interface AdapterCallback<Data> {
    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);
}
