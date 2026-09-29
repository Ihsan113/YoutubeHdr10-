package com.danzku.youtubehdr;

import android.app.*;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.*;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.*;
import android.view.*;
import android.widget.FrameLayout;
import java.util.*;

public class OverlayService extends Service {
    private WindowManager wm; private View overlay; private Handler handler; private int alpha=5;
    private final Runnable poll=new Runnable(){public void run(){try { if(isYoutubeForeground()) showOverlay(); else hideOverlay(); } catch(Exception ignored){} handler.postDelayed(this,1000); }};
    @Override public void onCreate(){super.onCreate();wm=(WindowManager)getSystemService(WINDOW_SERVICE);handler=new Handler(Looper.getMainLooper());}
    @Override public int onStartCommand(Intent i,int flags,int id){if(i!=null)alpha=i.getIntExtra("alpha",5); handler.removeCallbacks(poll);handler.post(poll);return START_STICKY;}
    private boolean isYoutubeForeground(){UsageStatsManager us=(UsageStatsManager)getSystemService(USAGE_STATS_SERVICE);if(us==null)return false;long now=System.currentTimeMillis();List<UsageStats> list=us.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,now-15000,now);if(list==null)return false;UsageStats best=null;for(UsageStats s:list)if(best==null||s.getLastTimeUsed()>best.getLastTimeUsed())best=s;return best!=null&&"com.google.android.youtube".equals(best.getPackageName());}
    private void showOverlay(){if(overlay!=null)return; overlay=new FrameLayout(this); // Very subtle cool/warm tint; does not process underlying pixels.
        int a=Math.min(22,Math.max(0,alpha)); overlay.setBackgroundColor(Color.argb(a,255,196,96));
        int type=Build.VERSION.SDK_INT>=26?WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY:WindowManager.LayoutParams.TYPE_PHONE;
        WindowManager.LayoutParams p=new WindowManager.LayoutParams(-1,-1,type,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN|WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,PixelFormat.TRANSLUCENT);
        p.gravity=Gravity.TOP|Gravity.START;wm.addView(overlay,p);
    }
    private void hideOverlay(){if(overlay!=null){try{wm.removeView(overlay);}catch(Exception ignored){}overlay=null;}}
    @Override public void onDestroy(){handler.removeCallbacks(poll);hideOverlay();super.onDestroy();}
    @Override public android.os.IBinder onBind(Intent i){return null;}
}
