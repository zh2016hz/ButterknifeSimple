package com.xiaoniu.finance.butterknifesimple;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.content.ContentValues.TAG;

/**
 * 文件描述：   代码注入工具类
 * Created by  xn069392
 * 创建时间    2018/8/2
 */

public class InjectViewUtils {

    /**
     * 代码注入方法，相当于Butterknife的 bind()
     *
     * @param activity
     */
    public static void inject(final Activity activity) {
        //反射拿到类对象
        Class<? extends Activity> clazz = activity.getClass();
        //这里我们需要的是所有的成员变量的参数
        Field[] Fileds = clazz.getDeclaredFields();
        //遍历集合
        for (int i = 0; i < Fileds.length; i++) {
            //获取每一个成员变量
            Field filed = Fileds[i];
            // 如果私有 暴力反射
            filed.setAccessible(true);
            //获取到成员变量的注解，传入参数就是之前我们定义的接口的类对象
            InjectView inject = filed.getAnnotation(InjectView.class);
            if (inject != null) {
                //我们的接口中定义了一个int  类型的value 值  ，获取到value 也就是我们成员变量的id
                int id = inject.value();
                //通过这个ID，也是需要在传入Activity中去找到对应view的
                View view = activity.findViewById(id);
                //成员变量找到了，这个要去设置
                try {
                    filed.set(activity, view);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, "inject: 设置成员变量失败！");
                    e.printStackTrace();
                }

            }
        }

        //处理点击事件
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (int i = 0; i < declaredMethods.length; i++) {
            final Method method = declaredMethods[i];
            method.setAccessible(true);
            OnClick onClick = method.getAnnotation(OnClick.class);
            //为什么是一个数组呢，因为我们的点击事件 有事后就写一个方法，多个id .
            //这里有的方法没有注解，必须做空判断
            if(onClick !=null){
                int[] value = onClick.value();
                for (int j = 0; j < value.length; j++) {
                    int idValue = value[j];
                    final View viewById = activity.findViewById(idValue);
                    viewById.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                method.invoke(activity, viewById);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        }
    }
}
