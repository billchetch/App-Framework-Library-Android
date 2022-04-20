package net.chetch.appframework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.util.List;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewFragmentAdapter<F extends RecyclerViewFragmentAdapter.IRecylcerViewFragment> extends RecyclerView.Adapter<RecyclerViewFragmentAdapter.ViewHolder> {

    public interface IRecylcerViewFragment{
        public void onBindData(Object data);
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        IRecylcerViewFragment itemFragment;
        public ViewHolder(View v, IRecylcerViewFragment itemFragment) {
            super(v);
            this.itemFragment = itemFragment;
        }
    }

    List dataset;
    Class<F> itemFragmentClass;

    public RecyclerViewFragmentAdapter(Class<F> itemFragmentClass){
        this.itemFragmentClass = itemFragmentClass;
    }

    public void setDataset(List dataset){
        this.dataset = dataset;
        notifyDataSetChanged();
    }

    public List getDataset(){
        return dataset;
    }

    public void clear(){
        if(dataset != null)dataset.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            IRecylcerViewFragment itemFragment = itemFragmentClass.getDeclaredConstructor().newInstance();
            View v = itemFragment.onCreateView(LayoutInflater.from(parent.getContext()), parent, null);

            ViewHolder vh = new ViewHolder(v, itemFragment);
            return vh;
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(dataset != null) {
            Object data = dataset.get(position);
            holder.itemFragment.onBindData(data);
        }
    }

    @Override
    public int getItemCount() {
        return this.dataset == null ? 0 : this.dataset.size();
    }

}
