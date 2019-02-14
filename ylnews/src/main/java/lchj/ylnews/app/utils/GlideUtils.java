package lchj.ylnews.app.utils;

import com.bumptech.glide.request.RequestOptions;

import lchj.ylnews.R;

public class GlideUtils {

    /**RequestOptions
     * @param placeholder
     * @param error
     * @return
     */
    public static RequestOptions GlideOptions(int placeholder,int error){
        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)//图片加载出来前，显示的图片
                .error(error);//图片加载失败后，显示的图片
        return options;

    }
    /**RequestOptions
     * @param placeholder
     * @param fallback
     * @param error
     * @return
     */
    public static RequestOptions GlideOptions(int placeholder,int fallback,int error){
        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)//图片加载出来前，显示的图片
                .fallback(fallback) //url为空的时候,显示的图片
                .error(error);//图片加载失败后，显示的图片
        return options;

    }
}
