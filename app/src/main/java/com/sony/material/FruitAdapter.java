package com.sony.material;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sony.www.demo.R;

import java.util.List;

/**
 * @author:nsh
 * @data:2018/2/8. 上午1:06
 */

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.FruitViewHolder> {

    private List<Fruit> mFruitList;
    private Context mContext;

    /**
     * 通过构造方法获取到内容资源
     *
     * @param mFruitList
     */
    public FruitAdapter(List<Fruit> mFruitList) {
        this.mFruitList = mFruitList;
    }

    /**
     * 通过此方法加载一个RecyclerView列表的item资源文件 填充一个ViewHolder
     * 传给自定义的继承了RecyclerView.ViewHolder的类
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public FruitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View fruitItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item_md, parent, false);
        FruitViewHolder fruitViewHolder = new FruitViewHolder(fruitItemView);
        return fruitViewHolder;
    }

    /**
     * 获取到内容对应的属性值 分别绑定给对应的属性
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final FruitViewHolder holder, int position) {
        final Fruit fruit = mFruitList.get(position);
//        使用第三方库加载大图
        Glide.with(mContext).load(fruit.getImageId()).into(holder.fruitImage);
//        holder.fruitImage.setImageResource(fruit.getImageId());
        holder.fruitName.setText(fruit.getFruitName());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "You clicked " + fruit.getFruitName(), Toast.LENGTH_SHORT).show();

                Intent detailsIntent = new Intent(mContext, FruitActivity.class);
                detailsIntent.putExtra(FruitActivity.FRUIT_NAME, fruit.getFruitName());
                detailsIntent.putExtra(FruitActivity.FRUIT_IMAGE_ID, fruit.getImageId());
                mContext.startActivity(detailsIntent);
            }
        });
    }

    /**
     * 返回一共有多少条item
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mFruitList.size();
    }

    public void addData(int position) {
        mFruitList.add(mFruitList.get(position));
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mFruitList.remove(mFruitList.get(position));
        notifyItemRemoved(position);
    }

    /**
     * 得到ViewHolder之后 分别得到item资源中的每个属性
     */
    static class FruitViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView fruitName;
        ImageView fruitImage;

        public FruitViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            fruitImage = (ImageView) itemView.findViewById(R.id.fruit_image_md);
            fruitName = (TextView) itemView.findViewById(R.id.fruit_name_md);
        }
    }

}
