package example.com.asd4;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String name;
    private String img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // initdata();

        Button login = findViewById(R.id.qq_login);
        Button share = findViewById(R.id.qq_share);


        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //获取昵称   头像
                Intent intent = new Intent(MainActivity.this, ShowActivity.class);
                intent.putExtra("aaa",name);
                intent.putExtra("sss",img);
                startActivity(intent);
                Toast.makeText(MainActivity.this,name,Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this,img,Toast.LENGTH_LONG).show();

                //获得UMShareAPI实例
                UMShareAPI umShareAPI = UMShareAPI.get(MainActivity.this);

                //开始登录
                //第一个参数：上下文
                //第二个参数，登录哪种平台
                //第三个参数，添加回调
                umShareAPI.getPlatformInfo(MainActivity.this, SHARE_MEDIA.QQ, new UMAuthListener() {
                    /**
                     * 开始登录回调
                     * @param share_media
                     */
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        Log.i("dj", "UMAuthListener onStart");
                    }

                    /**
                     * 登录完成
                     * @param share_media
                     * @param i
                     * @param map
                     */
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        //头像，昵称，如果拿不到，自己debug看Key是啥，再问打死
                        Log.i("dj", "UMAuthListener onComplete");

                        //获取名字
                        name = map.get("screen_name");
                        //获取头像
                        img = map.get("profile_image_url");

                        Log.i("dj", "name is " + name);
                        Log.i("dj", "img is " + img);
                        Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    }

                    /**
                     * 登录失败
                     * @param share_media
                     * @param i
                     * @param throwable
                     */
                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        Log.i("dj", "UMAuthListener onError" + throwable.getLocalizedMessage());
                    }

                    /**
                     * 登录取消
                     * @param share_media
                     * @param i
                     */
                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        Log.i("dj", "UMAuthListener onCancel");
                    }
                });
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享用的图片
                UMImage image = new UMImage(MainActivity.this, R.drawable.umeng_socialize_qq);
                new ShareAction(MainActivity.this)
                        //分享的标题
                        .withText("hello")
                        //分享的图片
                        .withMedia(image)
                        //分享到哪，这里面只写了QQ，如有需要在后面添加
                        .setDisplayList(SHARE_MEDIA.QQ)
                        //设置回调
                        .setCallback(shareListener)
                        //打开分享面板
                        .open();
            }
        });

        checkPermission();
    }

//    private void initdata() {
//        mainPresenter = new MainPresenter();
//        mainPresenter.setView(this);
//
//    }

    //判断权限
    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {//QQ需要申请写入权限
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(MainActivity.this, mPermissionList, 123);
        } else {
//            UMShareAPI.get(MainActivity.this).getPlatformInfo(MainActivity.this, SHARE_MEDIA.QQ, authListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, authListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(MainActivity.this, "成功了", Toast.LENGTH_LONG).show();
//
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            Toast.makeText(MainActivity.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(MainActivity.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            Log.i("dj", "UMShareListener onStart");
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.i("dj", "UMShareListener onResult");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Log.i("dj", "UMShareListener onError");
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Log.i("dj", "UMShareListener onCancel");
        }
    };

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mainPresenter.DettchView();
//    }
//
}
