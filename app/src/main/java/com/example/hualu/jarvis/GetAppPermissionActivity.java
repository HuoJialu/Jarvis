package com.example.hualu.jarvis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.hualu.jarvis.dao.AntivirusDao;
import com.example.hualu.jarvis.domain.AppInfo;
import com.example.hualu.jarvis.engine.APPInfoProvider;
import com.example.hualu.jarvis.util.MD5Utils;

import java.util.ArrayList;
import java.util.List;

public class GetAppPermissionActivity extends AppCompatActivity {
    protected static final int FINISH = 0;
    protected static final int SCAN = 1;
    protected static final int START = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_app_permission);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        anim();
    }

    //查看app申请的所有权限
    public String getAPPPermission(String packageName) {
        PackageManager pm = getPackageManager();
        String permissions = null;
        try {
            PackageInfo pack = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            String[] permissionStrings = pack.requestedPermissions;
            if(permissionStrings==null)
                return permissions;
            for (int i = 0; i < permissionStrings.length; i++) {
                System.out.println(permissionStrings[i]);
            }
            permissions = permissionStrings.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return permissions;
    }

    // 判断APP申请的权限有没有生效
    public boolean ifPermission(String applyPermission, String packageName) {
        PackageManager pm = getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission(applyPermission, packageName));
        if (permission) {
            System.out.println("权限生效了");
        } else {
            System.out.println("权限未生效");
        }
        return permission;
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SCAN:
                    GetAppPermissionActivity.AntiInfo ai = (GetAppPermissionActivity.AntiInfo) msg.obj;
                    Drawable icon = ai.getIcon();
                    String name = ai.getName();
                    break;
                case FINISH:
                    break;
                case START:
                    break;
            }

        }

        ;
    };

    public void anim() {
        final PackageManager pm = getPackageManager();
        //final AntivirusDao ad = new AntivirusDao();
        new Thread() {
            public void run() {
                List<AppInfo> infos = APPInfoProvider
                        .getAppInfo(GetAppPermissionActivity.this);
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                for (int x = 0; x < infos.size(); x++) {
                    try {
                        sleep(330);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    GetAppPermissionActivity.AntiInfo ai = new GetAppPermissionActivity.AntiInfo();
                    Drawable icon = infos.get(x).getIcon();
                    String name = infos.get(x).getName();
                    String packageName = infos.get(x).getPackageName();
                    System.out.print("Test"+ packageName);
                    System.out.print("permission:"+getAPPPermission(packageName));
                    if (x == 0) {
                        Message msg = handler.obtainMessage();
                        msg.obj = ai;
                        msg.what = START;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = handler.obtainMessage();
                        msg.obj = ai;
                        msg.what = SCAN;
                        handler.sendMessage(msg);
                    }
                }
                Message msg = handler.obtainMessage();
                msg.what = FINISH;
                handler.sendMessage(msg);
            }
        }.start();
    }


    class AntiInfo {
        private Drawable icon;
        private String name;
        private boolean isAniti;
        private String packageName;


        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isAniti() {
            return isAniti;
        }

        public void setAniti(boolean isAniti) {
            this.isAniti = isAniti;
        }

    }
}

