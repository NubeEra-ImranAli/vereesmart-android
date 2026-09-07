package com.vereesmart.controller;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import org.json.JSONObject;
import java.util.concurrent.*;

public class MainActivity extends Activity {
  private final Handler handler = new Handler(Looper.getMainLooper());
  private final ExecutorService io = Executors.newSingleThreadExecutor();
  private LinearLayout page; private JSONObject status = new JSONObject();
  private String screen = "HOME"; private int browsingGrade = 0;
  private boolean connected = false;
  private final Runnable poll = new Runnable() { public void run() { refresh(false); handler.postDelayed(this, 2000); } };
  private final int navy = Color.rgb(14, 40, 82), mint = Color.rgb(11, 158, 138), blue = Color.rgb(30, 105, 218), green = Color.rgb(9, 154, 56);

  @Override public void onCreate(Bundle state) { super.onCreate(state); showHome(); }
  @Override public void onResume() { super.onResume(); refresh(true); handler.post(poll); }
  @Override public void onPause() { handler.removeCallbacks(poll); super.onPause(); }
  @Override public void onDestroy() { io.shutdownNow(); super.onDestroy(); }

  private void refresh(boolean rerender) { io.execute(() -> { try { JSONObject s = Esp32Api.status(); runOnUiThread(() -> { status = s; connected = true; String remote = s.optString("screen", "HOME"); if (!"HOME".equals(remote) || browsingGrade == 0) screen = remote; if (rerender) render(); else updateTop(); }); } catch (Exception e) { runOnUiThread(() -> { connected = false; if (rerender) render(); else updateTop(); }); } }); }
  private void command(String command, int grade, int project) { io.execute(() -> { try { if ("home".equals(command)) Esp32Api.home(); else if ("back".equals(command)) Esp32Api.back(); else if ("execute".equals(command)) Esp32Api.execute(); else if ("stop".equals(command)) Esp32Api.stop(); else Esp32Api.select(grade, project); JSONObject s = Esp32Api.status(); runOnUiThread(() -> { status = s; connected = true; screen = s.optString("screen", "HOME"); browsingGrade = 0; render(); }); } catch (Exception e) { runOnUiThread(() -> { connected = false; toast("ESP32 not reachable. Check that Android Settings is connected to the VereeSmart Wi-Fi."); updateTop(); }); } }); }

  private void render() { ScrollView scroll = new ScrollView(this); page = new LinearLayout(this); page.setPadding(dp(20), dp(20), dp(20), dp(30)); page.setOrientation(LinearLayout.VERTICAL); page.setBackgroundColor(Color.WHITE); scroll.addView(page); setContentView(scroll); top(); if ("STEPS".equals(screen)) steps(); else if ("PROJECTS".equals(screen) || browsingGrade > 0) projects(); else home(); }
  private void top() { LinearLayout bar = row(); bar.setGravity(Gravity.CENTER_VERTICAL); TextView title = text("VereeSmart", 27, navy, true); bar.addView(title, new LinearLayout.LayoutParams(0, -2, 1)); TextView device = text(connected ? "●  ESP32-01\nConnected" : "●  ESP32\nDisconnected", 13, connected ? green : Color.rgb(180, 57, 57), true); device.setGravity(Gravity.RIGHT); bar.addView(device); page.addView(bar); TextView sub = text("ESP32 remote controller  •  192.168.4.1", 13, Color.rgb(82,106,133), false); page.addView(sub); View line = new View(this); line.setBackgroundColor(Color.rgb(213,232,230)); page.addView(line, lp(-1, 1, 0, 17, 0, 17)); if (!connected) { LinearLayout offline = box(Color.rgb(255,244,244), Color.rgb(255,205,205), 14); TextView warning = text("ESP32 not reachable\nConnect your phone to the VereeSmart ESP32 Wi-Fi in Android Settings, then retry.", 14, Color.rgb(151,48,48), false); offline.addView(warning, new LinearLayout.LayoutParams(0,-2,1)); Button retry = button("RETRY", Color.WHITE, Color.rgb(160,48,48)); retry.setOnClickListener(v -> refresh(true)); offline.addView(retry); page.addView(offline, lp(-1,-2,0,0,0,18)); } }
  private void updateTop() { render(); }
  private void home() { page.addView(text("Select Grade", 33, navy, true), lp(-1,-2,0,0,0,7)); page.addView(text("Choose a learning level to explore its projects.", 16, Color.rgb(82,106,133), false), lp(-1,-2,0,0,0,24)); LinearLayout grid = new LinearLayout(this); grid.setOrientation(LinearLayout.VERTICAL); int[] colors = {mint, blue, Color.rgb(231,125,22), Color.rgb(121,67,181)}; for (int r=0;r<2;r++) { LinearLayout row=row(); for(int c=0;c<2;c++){ int grade=r*2+c+1; Button b=button("Grade " + grade, Color.WHITE, colors[grade-1]); b.setTextSize(19); int g=grade; b.setOnClickListener(v->{ browsingGrade=g; screen="PROJECTS"; render();}); row.addView(b,new LinearLayout.LayoutParams(0,dp(110),1)); if(c==0) spacer(row,12); } grid.addView(row,lp(-1,-2,0,0,0,12)); } page.addView(grid); info(); }
  private void projects() { int grade = browsingGrade > 0 ? browsingGrade : status.optInt("grade", 1); page.addView(text("Grade " + grade + " Projects", 31, navy, true), lp(-1,-2,0,0,0,6)); page.addView(text("Select a project to view its steps and run it.", 16, Color.rgb(82,106,133), false), lp(-1,-2,0,0,0,19)); for (int p=0;p<6;p++){ Button item = button((p+1) + "     " + ProjectCatalog.NAMES[grade-1][p] + "    ›", Color.WHITE, navy); item.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT); item.setTextSize(16); int project=p+1; item.setOnClickListener(v -> command("select",grade,project)); page.addView(item,lp(-1,dp(62),0,0,0,10)); } Button home=button("⌂  HOME",Color.WHITE,blue); home.setOnClickListener(v->command("home",0,0)); page.addView(home,lp(-1,dp(50),0,12,0,0)); }
  private void steps() { int grade=status.optInt("grade",1), project=status.optInt("project",1); String name=ProjectCatalog.NAMES[grade-1][project-1]; page.addView(text(name, 29, navy, true), lp(-1,-2,0,0,0,5)); boolean running=status.optBoolean("running",false); page.addView(text(running ? "●  RUNNING" : "●  READY", 15, running?green:Color.rgb(92,112,134), true), lp(-1,-2,0,0,0,25)); page.addView(text("Steps", 23, navy, true),lp(-1,-2,0,0,0,8)); String[] steps=ProjectCatalog.STEPS[grade-1][project-1]; for(int i=0;i<steps.length;i++){ LinearLayout step=row(); TextView num=text(String.valueOf(i+1),17,blue,true); num.setGravity(Gravity.CENTER); step.addView(num,lp(dp(40),dp(40),0,0,0,6)); step.addView(text(steps[i],16,navy,true),new LinearLayout.LayoutParams(0,dp(40),1)); page.addView(step,lp(-1,dp(48),0,0,0,4)); } LinearLayout actions=row(); Button home=button("HOME",Color.WHITE,blue); home.setOnClickListener(v->command("home",0,0)); Button back=button("BACK",Color.WHITE,blue); back.setOnClickListener(v->command("back",0,0)); Button run=button(running?"STOP":"EXECUTE",Color.WHITE,running?green:blue); run.setOnClickListener(v->command(running?"stop":"execute",0,0)); actions.addView(home,new LinearLayout.LayoutParams(0,dp(54),1)); spacer(actions,8); actions.addView(back,new LinearLayout.LayoutParams(0,dp(54),1)); spacer(actions,8); actions.addView(run,new LinearLayout.LayoutParams(0,dp(54),1)); page.addView(actions,lp(-1,-2,0,20,0,0)); }
  private void info(){ LinearLayout card=box(Color.rgb(248,252,255),Color.rgb(204,218,232),16); card.setOrientation(LinearLayout.VERTICAL); card.addView(text("Device Information",21,navy,true)); card.addView(text("Device ID     " + status.optString("deviceId","ESP32") + "\nWi-Fi          " + status.optString("ssid","—") + "\nIP Address   192.168.4.1\nConnection  " + (connected?"Connected":"Disconnected"),15,navy,false),lp(-1,-2,0,13,0,0)); page.addView(card,lp(-1,-2,0,24,0,0)); }
  private LinearLayout row(){ LinearLayout x=new LinearLayout(this); x.setOrientation(LinearLayout.HORIZONTAL); return x; }
  private LinearLayout box(int bg,int border,int radius){ LinearLayout x=new LinearLayout(this); x.setPadding(dp(16),dp(16),dp(16),dp(16)); x.setBackground(round(bg,border,radius)); return x; }
  private Button button(String label,int fg,int bg){ Button b=new Button(this); b.setText(label); b.setTextColor(fg); b.setTextSize(14); b.setAllCaps(false); b.setTypeface(null,1); b.setBackground(round(bg,bg,13)); return b; }
  private TextView text(String value,int size,int color,boolean bold){ TextView t=new TextView(this); t.setText(value);t.setTextSize(size);t.setTextColor(color);t.setTypeface(null,bold?1:0);t.setGravity(Gravity.CENTER_VERTICAL);return t; }
  private GradientDrawable round(int bg,int border,int radius){ GradientDrawable d=new GradientDrawable();d.setColor(bg);d.setCornerRadius(dp(radius));d.setStroke(dp(1),border);return d; }
  private LinearLayout.LayoutParams lp(int w,int h,int l,int top,int r,int b){ LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(w,h);p.setMargins(dp(l),dp(top),dp(r),dp(b));return p; }
  private void spacer(LinearLayout parent,int width){ Space s=new Space(this);parent.addView(s,new LinearLayout.LayoutParams(dp(width),1)); }
  private int dp(int value){return (int)(value*getResources().getDisplayMetrics().density+.5f);}
  private void toast(String msg){Toast.makeText(this,msg,Toast.LENGTH_LONG).show();}
}
