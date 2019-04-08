package com.tufer.mylove.frags.panel;

import android.view.View;

import com.tufer.common.widget.recycler.RecyclerAdapter;
import com.tufer.face.Face;
import com.tufer.mylove.R;

import java.util.List;

/**
 * @author Tufer Email:1126179195@qq.com
 * @version 1.0.0
 */
public class FaceAdapter extends RecyclerAdapter<Face.Bean> {

    public FaceAdapter(List<Face.Bean> beans, AdapterListener<Face.Bean> listener) {
        super(beans, listener);
    }

    @Override
    protected int getItemViewType(int position, Face.Bean bean) {
        return R.layout.cell_face;
    }

    @Override
    protected ViewHolder<Face.Bean> onCreateViewHolder(View root, int viewType) {
        return new FaceHolder(root);
    }
}
