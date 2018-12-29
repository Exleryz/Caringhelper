package com.weimore.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Werimore/ on 2017/11/2.
 *         Adapter基类，简化创建Adapter和Holder
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.BaseHolder<T>> {

    private List<T> mDataList;

    private AdapterClickListener mListener;

    public BaseAdapter() {
        mDataList = new ArrayList<>();
    }

    public BaseAdapter(List<T> dataList) {
        if (dataList != null) {
            mDataList = dataList;
        } else {
            mDataList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public BaseHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutResId(), parent, false);
        return getViewHolder(view);
    }

    protected abstract int getItemLayoutResId();

    protected abstract BaseHolder<T> getViewHolder(View view);

    public List<T> getDataList() {
        return mDataList;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public boolean isLast(int position) {
        return mDataList.size() - 1 == position;
    }


    public void addData(List<T> data) {
        mDataList.addAll(data);
        notifyItemRangeChanged(0, mDataList.size());
    }

    public void addData(T data) {
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    public void clear() {
        mDataList.clear();
    }

    public void setData(List<T> data) {
        mDataList.clear();
        mDataList.addAll(data);
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return mDataList.get(position);
    }

    public int getItemPosition(T item) {
        return mDataList.indexOf(item);
    }

    public void changeItem(int position, T data) {
        mDataList.set(position, data);
        notifyItemChanged(position);
    }

    public void addItem(int position, T data) {
        mDataList.add(position, data);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mDataList.size() - position);
    }

    public void removeItem(T data) {
        int position = mDataList.indexOf(data);
        removeItem(position);
    }

    public void removeItem(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataList.size() - position);
    }

    public void setAdapterListener(AdapterClickListener listener) {
        mListener = listener;
    }


    public abstract static class BaseHolder<T> extends RecyclerView.ViewHolder {


        protected BaseAdapter<T> mAdapter;

        public BaseHolder(View itemView, BaseAdapter<T> adapter) {
            super(itemView);
            mAdapter = adapter;
        }

        public View getRootView() {
            return itemView;
        }

        public BaseAdapter<T> getAdapter() {
            return mAdapter;
        }

        private void onBind(int position) {
            bind(mAdapter.getItem(position),position);
        }

        protected abstract void bind(T item,int position);
    }

    public interface AdapterClickListener {

        void onItemClickListener(BaseHolder holder, int position);

        void onLongItemClickListener(BaseHolder holder, int position);

    }

}
