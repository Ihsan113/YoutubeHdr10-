package com.danzku.youtubehdr;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.*;

public class MainActivity extends Activity {
    private TextView status;
    private boolean grantedUsage() {
        AppOpsManager ops=(AppOpsManager)getSystemService(Context.APP_OPS_SERVICE);
        return ops!=null && ops.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), getPackageName())==AppOpsManager.MODE_ALLOWED;
    }
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout root=new LinearLayout(this); root.setOrientation(1); root.setPadding(36,40,36,24); root.setBackgroundColor(0xFFF5F7F8);
        TextView title=new TextView(this); title.setText("DanzKu YouTube HDR-like"); title.setTextSize(24); title.setTextColor(0xFF17212B); title.setGravity(Gravity.CENTER_VERTICAL); root.addView(title);
        TextView desc=new TextView(this); desc.setText("Overlay warna eksperimental untuk YouTube. Efek ini bukan HDR10+ asli dan hanya berupa tint transparan di atas layar."); desc.setTextSize(14); desc.setTextColor(0xFF53616D); desc.setPadding(0,16,0,20); root.addView(desc);
        Button usage=new Button(this); usage.setText("1. Izinkan akses penggunaan aplikasi"); root.addView(usage); usage.setOnClickListener(v->startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)));
        Button overlay=new Button(this); overlay.setText("2. Izinkan tampil di atas aplikasi lain"); root.addView(overlay); overlay.setOnClickListener(v->{Intent i=new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName())); startActivity(i);});
        SeekBar intensity=new SeekBar(this); intensity.setMax(18); intensity.setProgress(5); root.addView(intensity);
        TextView level=new TextView(this); level.setText("Intensitas: 5/18"); level.setTextColor(0xFF263238); root.addView(level); intensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){public void onProgressChanged(SeekBar s,int p,boolean u){level.setText("Intensitas: "+p+"/18");} public void onStartTrackingTouch(SeekBar s){} public void onStopTrackingTouch(SeekBar s){}});
        Button start=new Button(this); start.setText("Aktifkan overlay YouTube"); root.addView(start); start.setOnClickListener(v->{if(!Settings.canDrawOverlays(this)){Toast.makeText(this,"Izinkan overlay dulu",Toast.LENGTH_SHORT).show();return;} if(!grantedUsage()){Toast.makeText(this,"Izinkan akses penggunaan dulu",Toast.LENGTH_SHORT).show();return;} Intent i=new Intent(this,OverlayService.class); i.putExtra("alpha", intensity.getProgress()); startService(i); status.setText("Overlay aktif saat YouTube terdeteksi di depan.");});
        Button stop=new Button(this); stop.setText("Matikan overlay"); root.addView(stop); stop.setOnClickListener(v->{stopService(new Intent(this,OverlayService.class));status.setText("Overlay dimatikan.");});
        status=new TextView(this); status.setText("Status: belum aktif"); status.setTextColor(0xFF263238); status.setPadding(0,18,0,0); root.addView(status);
        setContentView(root);
    }
}
