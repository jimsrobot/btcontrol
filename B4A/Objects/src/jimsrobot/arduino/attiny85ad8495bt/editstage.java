package jimsrobot.arduino.attiny85ad8495bt;

import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class editstage extends Activity implements B4AActivity{
	public static editstage mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "jimsrobot.arduino.attiny85ad8495bt", "jimsrobot.arduino.attiny85ad8495bt.editstage");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (editstage).");
				p.finish();
			}
		}
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
		BA.handler.postDelayed(new WaitForLayout(), 5);

	}
	private static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "jimsrobot.arduino.attiny85ad8495bt", "jimsrobot.arduino.attiny85ad8495bt.editstage");
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (editstage) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (editstage) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
		return true;
	}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return editstage.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true)
				return true;
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
		this.setIntent(intent);
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (editstage) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (editstage) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}

public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btneditstageok = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btneditstagecancel = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _chkbeep = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _chkdonothing = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _chkincreasetemp = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtduration = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtrate = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txttargettemp = null;
public jimsrobot.arduino.attiny85ad8495bt.main _main = null;
public jimsrobot.arduino.attiny85ad8495bt.profilelist _profilelist = null;
public jimsrobot.arduino.attiny85ad8495bt.stagelist _stagelist = null;
public static String  _activity_create(boolean _firsttime) throws Exception{
		Debug.PushSubsStack("Activity_Create (editstage) ","editstage",2,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("FirstTime", _firsttime);
 BA.debugLineNum = 25;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
Debug.ShouldStop(16777216);
 BA.debugLineNum = 27;BA.debugLine="Activity.LoadLayout(\"EditStage\")";
Debug.ShouldStop(67108864);
mostCurrent._activity.LoadLayout("EditStage",mostCurrent.activityBA);
 BA.debugLineNum = 31;BA.debugLine="If StageList.SelectedStageID > -1 Then";
Debug.ShouldStop(1073741824);
if (mostCurrent._stagelist._selectedstageid>-1) { 
 BA.debugLineNum = 33;BA.debugLine="LoadStage";
Debug.ShouldStop(1);
_loadstage();
 }else {
 };
 BA.debugLineNum = 37;BA.debugLine="End Sub";
Debug.ShouldStop(16);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _activity_pause(boolean _userclosed) throws Exception{
		Debug.PushSubsStack("Activity_Pause (editstage) ","editstage",2,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("UserClosed", _userclosed);
 BA.debugLineNum = 43;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
Debug.ShouldStop(1024);
 BA.debugLineNum = 45;BA.debugLine="End Sub";
Debug.ShouldStop(4096);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _activity_resume() throws Exception{
		Debug.PushSubsStack("Activity_Resume (editstage) ","editstage",2,mostCurrent.activityBA,mostCurrent);
try {
 BA.debugLineNum = 39;BA.debugLine="Sub Activity_Resume";
Debug.ShouldStop(64);
 BA.debugLineNum = 41;BA.debugLine="End Sub";
Debug.ShouldStop(256);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _btneditstagecancel_click() throws Exception{
		Debug.PushSubsStack("btnEditStageCancel_Click (editstage) ","editstage",2,mostCurrent.activityBA,mostCurrent);
try {
 BA.debugLineNum = 53;BA.debugLine="Sub btnEditStageCancel_Click";
Debug.ShouldStop(1048576);
 BA.debugLineNum = 54;BA.debugLine="Activity.Finish";
Debug.ShouldStop(2097152);
mostCurrent._activity.Finish();
 BA.debugLineNum = 55;BA.debugLine="End Sub";
Debug.ShouldStop(4194304);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _btneditstageok_click() throws Exception{
		Debug.PushSubsStack("btnEditStageOK_Click (editstage) ","editstage",2,mostCurrent.activityBA,mostCurrent);
try {
 BA.debugLineNum = 47;BA.debugLine="Sub btnEditStageOK_Click";
Debug.ShouldStop(16384);
 BA.debugLineNum = 49;BA.debugLine="SaveStage";
Debug.ShouldStop(65536);
_savestage();
 BA.debugLineNum = 50;BA.debugLine="Activity.Finish";
Debug.ShouldStop(131072);
mostCurrent._activity.Finish();
 BA.debugLineNum = 51;BA.debugLine="End Sub";
Debug.ShouldStop(262144);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _chkbeep_checkedchange(boolean _checked) throws Exception{
		Debug.PushSubsStack("chkBeep_CheckedChange (editstage) ","editstage",2,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("Checked", _checked);
 BA.debugLineNum = 206;BA.debugLine="Sub chkBeep_CheckedChange(Checked As Boolean)";
Debug.ShouldStop(8192);
 BA.debugLineNum = 207;BA.debugLine="If Checked = True Then";
Debug.ShouldStop(16384);
if (_checked==anywheresoftware.b4a.keywords.Common.True) { 
 BA.debugLineNum = 208;BA.debugLine="txtTargetTemp.Text = \"0\"";
Debug.ShouldStop(32768);
mostCurrent._txttargettemp.setText((Object)("0"));
 BA.debugLineNum = 209;BA.debugLine="txtTargetTemp.Enabled = False";
Debug.ShouldStop(65536);
mostCurrent._txttargettemp.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 BA.debugLineNum = 210;BA.debugLine="txtRate.Text = \"0\"";
Debug.ShouldStop(131072);
mostCurrent._txtrate.setText((Object)("0"));
 BA.debugLineNum = 211;BA.debugLine="txtRate.Enabled = False";
Debug.ShouldStop(262144);
mostCurrent._txtrate.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 BA.debugLineNum = 212;BA.debugLine="txtDuration.Text = \"0\"";
Debug.ShouldStop(524288);
mostCurrent._txtduration.setText((Object)("0"));
 BA.debugLineNum = 213;BA.debugLine="txtDuration.Enabled = False";
Debug.ShouldStop(1048576);
mostCurrent._txtduration.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 BA.debugLineNum = 215;BA.debugLine="End Sub";
Debug.ShouldStop(4194304);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _chkdonothing_checkedchange(boolean _checked) throws Exception{
		Debug.PushSubsStack("chkDoNothing_CheckedChange (editstage) ","editstage",2,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("Checked", _checked);
 BA.debugLineNum = 196;BA.debugLine="Sub chkDoNothing_CheckedChange(Checked As Boolean)";
Debug.ShouldStop(8);
 BA.debugLineNum = 197;BA.debugLine="If Checked = True Then";
Debug.ShouldStop(16);
if (_checked==anywheresoftware.b4a.keywords.Common.True) { 
 BA.debugLineNum = 198;BA.debugLine="txtTargetTemp.Text = \"0\"";
Debug.ShouldStop(32);
mostCurrent._txttargettemp.setText((Object)("0"));
 BA.debugLineNum = 199;BA.debugLine="txtTargetTemp.Enabled = False";
Debug.ShouldStop(64);
mostCurrent._txttargettemp.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 BA.debugLineNum = 200;BA.debugLine="txtRate.Text = \"0\"";
Debug.ShouldStop(128);
mostCurrent._txtrate.setText((Object)("0"));
 BA.debugLineNum = 201;BA.debugLine="txtRate.Enabled = False";
Debug.ShouldStop(256);
mostCurrent._txtrate.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 BA.debugLineNum = 203;BA.debugLine="txtDuration.Enabled = True";
Debug.ShouldStop(1024);
mostCurrent._txtduration.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 BA.debugLineNum = 205;BA.debugLine="End Sub";
Debug.ShouldStop(4096);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _chkincreasetemp_checkedchange(boolean _checked) throws Exception{
		Debug.PushSubsStack("chkIncreaseTemp_CheckedChange (editstage) ","editstage",2,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("Checked", _checked);
 BA.debugLineNum = 186;BA.debugLine="Sub chkIncreaseTemp_CheckedChange(Checked As Boolean)";
Debug.ShouldStop(33554432);
 BA.debugLineNum = 187;BA.debugLine="If Checked = True Then";
Debug.ShouldStop(67108864);
if (_checked==anywheresoftware.b4a.keywords.Common.True) { 
 BA.debugLineNum = 189;BA.debugLine="txtTargetTemp.Enabled = True";
Debug.ShouldStop(268435456);
mostCurrent._txttargettemp.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 BA.debugLineNum = 191;BA.debugLine="txtRate.Enabled = True";
Debug.ShouldStop(1073741824);
mostCurrent._txtrate.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 BA.debugLineNum = 192;BA.debugLine="txtDuration.Text = \"0\"";
Debug.ShouldStop(-2147483648);
mostCurrent._txtduration.setText((Object)("0"));
 BA.debugLineNum = 193;BA.debugLine="txtDuration.Enabled = False";
Debug.ShouldStop(1);
mostCurrent._txtduration.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 BA.debugLineNum = 195;BA.debugLine="End Sub";
Debug.ShouldStop(4);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static long  _getmaxorder() throws Exception{
		Debug.PushSubsStack("GetMaxOrder (editstage) ","editstage",2,mostCurrent.activityBA,mostCurrent);
try {
Object _ret = null;
String _ssql = "";
 BA.debugLineNum = 140;BA.debugLine="Sub GetMaxOrder() As Long";
Debug.ShouldStop(2048);
 BA.debugLineNum = 141;BA.debugLine="Dim ret As Object";
Debug.ShouldStop(4096);
_ret = new Object();Debug.locals.put("ret", _ret);
 BA.debugLineNum = 142;BA.debugLine="If ProfileList.SelectedProfileID > -1 Then";
Debug.ShouldStop(8192);
if (mostCurrent._profilelist._selectedprofileid>-1) { 
 BA.debugLineNum = 143;BA.debugLine="Dim sSQL As String";
Debug.ShouldStop(16384);
_ssql = "";Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 144;BA.debugLine="sSQL = \"SELECT MAX(stage_sort_order) FROM stages WHERE profile_id=\" & ProfileList.SelectedProfileID";
Debug.ShouldStop(32768);
_ssql = "SELECT MAX(stage_sort_order) FROM stages WHERE profile_id="+BA.NumberToString(mostCurrent._profilelist._selectedprofileid);Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 145;BA.debugLine="ret = Main.SQL1.ExecQuerySingleResult(sSQL)";
Debug.ShouldStop(65536);
_ret = (Object)(mostCurrent._main._sql1.ExecQuerySingleResult(_ssql));Debug.locals.put("ret", _ret);
 BA.debugLineNum = 147;BA.debugLine="If ret <> Null Then";
Debug.ShouldStop(262144);
if (_ret!= null) { 
 BA.debugLineNum = 148;BA.debugLine="Return ret";
Debug.ShouldStop(524288);
if (true) return BA.ObjectToLongNumber(_ret);
 }else {
 BA.debugLineNum = 150;BA.debugLine="Return 0";
Debug.ShouldStop(2097152);
if (true) return (long)(0);
 };
 }else {
 BA.debugLineNum = 153;BA.debugLine="Msgbox(\"Error getting sort order\", \"Error\")";
Debug.ShouldStop(16777216);
anywheresoftware.b4a.keywords.Common.Msgbox("Error getting sort order","Error",mostCurrent.activityBA);
 BA.debugLineNum = 154;BA.debugLine="Return -1";
Debug.ShouldStop(33554432);
if (true) return (long)(-1);
 };
 BA.debugLineNum = 156;BA.debugLine="End Sub";
Debug.ShouldStop(134217728);
return 0L;
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 15;BA.debugLine="Dim btnEditStageOK As Button";
mostCurrent._btneditstageok = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Dim btnEditStageCancel As Button";
mostCurrent._btneditstagecancel = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Dim chkBeep As RadioButton";
mostCurrent._chkbeep = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Dim chkDoNothing As RadioButton";
mostCurrent._chkdonothing = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim chkIncreaseTemp As RadioButton";
mostCurrent._chkincreasetemp = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Dim txtDuration As EditText";
mostCurrent._txtduration = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Dim txtRate As EditText";
mostCurrent._txtrate = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Dim txtTargetTemp As EditText";
mostCurrent._txttargettemp = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}
public static String  _loadstage() throws Exception{
		Debug.PushSubsStack("LoadStage (editstage) ","editstage",2,mostCurrent.activityBA,mostCurrent);
try {
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
String _sstagetype = "";
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
 BA.debugLineNum = 102;BA.debugLine="Sub LoadStage";
Debug.ShouldStop(32);
 BA.debugLineNum = 103;BA.debugLine="Dim Cursor1 As Cursor";
Debug.ShouldStop(64);
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();Debug.locals.put("Cursor1", _cursor1);
 BA.debugLineNum = 104;BA.debugLine="Dim sStageType As String";
Debug.ShouldStop(128);
_sstagetype = "";Debug.locals.put("sStageType", _sstagetype);
 BA.debugLineNum = 106;BA.debugLine="Cursor1 = Main.SQL1.ExecQuery(\"SELECT stage_id, stage_type, stage_temp, stage_percent, stage_seconds, stage_sort_order FROM stages WHERE stage_id=\" & StageList.SelectedStageID)";
Debug.ShouldStop(512);
_cursor1.setObject((android.database.Cursor)(mostCurrent._main._sql1.ExecQuery("SELECT stage_id, stage_type, stage_temp, stage_percent, stage_seconds, stage_sort_order FROM stages WHERE stage_id="+BA.NumberToString(mostCurrent._stagelist._selectedstageid))));
 BA.debugLineNum = 108;BA.debugLine="If Cursor1.RowCount <> 1 Then";
Debug.ShouldStop(2048);
if (_cursor1.getRowCount()!=1) { 
 BA.debugLineNum = 109;BA.debugLine="Msgbox (\"Error - too many stages\", \"ERROR\")";
Debug.ShouldStop(4096);
anywheresoftware.b4a.keywords.Common.Msgbox("Error - too many stages","ERROR",mostCurrent.activityBA);
 }else {
 BA.debugLineNum = 113;BA.debugLine="Cursor1.Position = 0";
Debug.ShouldStop(65536);
_cursor1.setPosition((int)(0));
 BA.debugLineNum = 114;BA.debugLine="Dim lbl As Label";
Debug.ShouldStop(131072);
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();Debug.locals.put("lbl", _lbl);
 BA.debugLineNum = 115;BA.debugLine="lbl.Initialize(\"lbl\")";
Debug.ShouldStop(262144);
_lbl.Initialize(mostCurrent.activityBA,"lbl");
 BA.debugLineNum = 116;BA.debugLine="sStageType = StageTypeToString(Cursor1.Getstring(\"stage_type\"))";
Debug.ShouldStop(524288);
_sstagetype = _stagetypetostring((int)(Double.parseDouble(_cursor1.GetString("stage_type"))));Debug.locals.put("sStageType", _sstagetype);
 BA.debugLineNum = 117;BA.debugLine="txtDuration.Text = Cursor1.Getstring(\"stage_seconds\")";
Debug.ShouldStop(1048576);
mostCurrent._txtduration.setText((Object)(_cursor1.GetString("stage_seconds")));
 BA.debugLineNum = 118;BA.debugLine="txtRate.Text = Cursor1.Getstring(\"stage_percent\")";
Debug.ShouldStop(2097152);
mostCurrent._txtrate.setText((Object)(_cursor1.GetString("stage_percent")));
 BA.debugLineNum = 119;BA.debugLine="txtTargetTemp.Text = Cursor1.Getstring(\"stage_temp\")";
Debug.ShouldStop(4194304);
mostCurrent._txttargettemp.setText((Object)(_cursor1.GetString("stage_temp")));
 BA.debugLineNum = 121;BA.debugLine="Select Cursor1.Getstring(\"stage_type\")";
Debug.ShouldStop(16777216);
switch (BA.switchObjectToInt(_cursor1.GetString("stage_type"),BA.NumberToString(1),BA.NumberToString(2),BA.NumberToString(3))) {
case 0:
 BA.debugLineNum = 123;BA.debugLine="chkDoNothing.Checked = True";
Debug.ShouldStop(67108864);
mostCurrent._chkdonothing.setChecked(anywheresoftware.b4a.keywords.Common.True);
 BA.debugLineNum = 124;BA.debugLine="chkDoNothing_CheckedChange(True)";
Debug.ShouldStop(134217728);
_chkdonothing_checkedchange(anywheresoftware.b4a.keywords.Common.True);
 break;
case 1:
 BA.debugLineNum = 126;BA.debugLine="chkIncreaseTemp.Checked = True";
Debug.ShouldStop(536870912);
mostCurrent._chkincreasetemp.setChecked(anywheresoftware.b4a.keywords.Common.True);
 BA.debugLineNum = 127;BA.debugLine="chkIncreaseTemp_CheckedChange(True)";
Debug.ShouldStop(1073741824);
_chkincreasetemp_checkedchange(anywheresoftware.b4a.keywords.Common.True);
 break;
case 2:
 BA.debugLineNum = 129;BA.debugLine="chkBeep.Checked = True";
Debug.ShouldStop(1);
mostCurrent._chkbeep.setChecked(anywheresoftware.b4a.keywords.Common.True);
 BA.debugLineNum = 130;BA.debugLine="chkBeep_CheckedChange(True)";
Debug.ShouldStop(2);
_chkbeep_checkedchange(anywheresoftware.b4a.keywords.Common.True);
 break;
default:
 BA.debugLineNum = 132;BA.debugLine="Msgbox (\"Error - invalid stage type\", \"ERROR\")";
Debug.ShouldStop(8);
anywheresoftware.b4a.keywords.Common.Msgbox("Error - invalid stage type","ERROR",mostCurrent.activityBA);
 break;
}
;
 };
 BA.debugLineNum = 136;BA.debugLine="Cursor1.Close";
Debug.ShouldStop(128);
_cursor1.Close();
 BA.debugLineNum = 138;BA.debugLine="End Sub";
Debug.ShouldStop(512);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
public static String  _savestage() throws Exception{
		Debug.PushSubsStack("SaveStage (editstage) ","editstage",2,mostCurrent.activityBA,mostCurrent);
try {
String _ssql = "";
long _lmaxorder = 0L;
 BA.debugLineNum = 61;BA.debugLine="Sub SaveStage";
Debug.ShouldStop(268435456);
 BA.debugLineNum = 62;BA.debugLine="Dim sSQL As String";
Debug.ShouldStop(536870912);
_ssql = "";Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 63;BA.debugLine="Dim lMaxOrder As Long";
Debug.ShouldStop(1073741824);
_lmaxorder = 0L;Debug.locals.put("lMaxOrder", _lmaxorder);
 BA.debugLineNum = 64;BA.debugLine="If ProfileList.SelectedProfileID >-1 Then";
Debug.ShouldStop(-2147483648);
if (mostCurrent._profilelist._selectedprofileid>-1) { 
 BA.debugLineNum = 66;BA.debugLine="Log(StageList.SelectedStageID)";
Debug.ShouldStop(2);
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(mostCurrent._stagelist._selectedstageid));
 BA.debugLineNum = 68;BA.debugLine="If StageList.SelectedStageID < 0 Then";
Debug.ShouldStop(8);
if (mostCurrent._stagelist._selectedstageid<0) { 
 BA.debugLineNum = 69;BA.debugLine="sSQL = \"INSERT INTO stages (stage_id, profile_id, stage_type, stage_temp, stage_percent, stage_seconds, stage_sort_order) VALUES (Null,\"";
Debug.ShouldStop(16);
_ssql = "INSERT INTO stages (stage_id, profile_id, stage_type, stage_temp, stage_percent, stage_seconds, stage_sort_order) VALUES (Null,";Debug.locals.put("sSQL", _ssql);
 }else {
 BA.debugLineNum = 71;BA.debugLine="sSQL = \"INSERT INTO stages (stage_id, profile_id, stage_type, stage_temp, stage_percent, stage_seconds, stage_sort_order) VALUES (\"";
Debug.ShouldStop(64);
_ssql = "INSERT INTO stages (stage_id, profile_id, stage_type, stage_temp, stage_percent, stage_seconds, stage_sort_order) VALUES (";Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 72;BA.debugLine="sSQL = sSQL & StageList.SelectedStageID & \", \"";
Debug.ShouldStop(128);
_ssql = _ssql+BA.NumberToString(mostCurrent._stagelist._selectedstageid)+", ";Debug.locals.put("sSQL", _ssql);
 };
 BA.debugLineNum = 75;BA.debugLine="sSQL = sSQL & ProfileList.SelectedProfileID & \", \"";
Debug.ShouldStop(1024);
_ssql = _ssql+BA.NumberToString(mostCurrent._profilelist._selectedprofileid)+", ";Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 77;BA.debugLine="If chkBeep.Checked Then";
Debug.ShouldStop(4096);
if (mostCurrent._chkbeep.getChecked()) { 
 BA.debugLineNum = 78;BA.debugLine="sSQL = sSQL & \"3, \"";
Debug.ShouldStop(8192);
_ssql = _ssql+"3, ";Debug.locals.put("sSQL", _ssql);
 }else 
{ BA.debugLineNum = 79;BA.debugLine="Else If chkDoNothing.Checked Then";
Debug.ShouldStop(16384);
if (mostCurrent._chkdonothing.getChecked()) { 
 BA.debugLineNum = 80;BA.debugLine="sSQL = sSQL & \"1, \"";
Debug.ShouldStop(32768);
_ssql = _ssql+"1, ";Debug.locals.put("sSQL", _ssql);
 }else 
{ BA.debugLineNum = 81;BA.debugLine="Else If chkIncreaseTemp.Checked Then";
Debug.ShouldStop(65536);
if (mostCurrent._chkincreasetemp.getChecked()) { 
 BA.debugLineNum = 82;BA.debugLine="sSQL = sSQL & \"2, \"";
Debug.ShouldStop(131072);
_ssql = _ssql+"2, ";Debug.locals.put("sSQL", _ssql);
 }}};
 BA.debugLineNum = 85;BA.debugLine="sSQL = sSQL & txtTargetTemp.Text & \", \"";
Debug.ShouldStop(1048576);
_ssql = _ssql+mostCurrent._txttargettemp.getText()+", ";Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 86;BA.debugLine="sSQL = sSQL & txtRate.Text & \", \"";
Debug.ShouldStop(2097152);
_ssql = _ssql+mostCurrent._txtrate.getText()+", ";Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 87;BA.debugLine="sSQL = sSQL & txtDuration.Text & \", \"";
Debug.ShouldStop(4194304);
_ssql = _ssql+mostCurrent._txtduration.getText()+", ";Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 89;BA.debugLine="lMaxOrder = GetMaxOrder";
Debug.ShouldStop(16777216);
_lmaxorder = _getmaxorder();Debug.locals.put("lMaxOrder", _lmaxorder);
 BA.debugLineNum = 90;BA.debugLine="lMaxOrder = lMaxOrder + 1";
Debug.ShouldStop(33554432);
_lmaxorder = (long)(_lmaxorder+1);Debug.locals.put("lMaxOrder", _lmaxorder);
 BA.debugLineNum = 91;BA.debugLine="sSQL= sSQL & lMaxOrder & \")\"";
Debug.ShouldStop(67108864);
_ssql = _ssql+BA.NumberToString(_lmaxorder)+")";Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 93;BA.debugLine="Log(sSQL)";
Debug.ShouldStop(268435456);
anywheresoftware.b4a.keywords.Common.Log(_ssql);
 BA.debugLineNum = 95;BA.debugLine="Main.SQL1.ExecNonQuery(sSQL)";
Debug.ShouldStop(1073741824);
mostCurrent._main._sql1.ExecNonQuery(_ssql);
 }else {
 BA.debugLineNum = 97;BA.debugLine="Msgbox(\"Invalid Profile ID\", \"ERROR\")";
Debug.ShouldStop(1);
anywheresoftware.b4a.keywords.Common.Msgbox("Invalid Profile ID","ERROR",mostCurrent.activityBA);
 };
 BA.debugLineNum = 100;BA.debugLine="End Sub";
Debug.ShouldStop(8);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _stagetypetostring(int _istagetype) throws Exception{
		Debug.PushSubsStack("StageTypeToString (editstage) ","editstage",2,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("iStageType", _istagetype);
 BA.debugLineNum = 159;BA.debugLine="Sub StageTypeToString(iStageType As Int) As String";
Debug.ShouldStop(1073741824);
 BA.debugLineNum = 160;BA.debugLine="Select iStageType";
Debug.ShouldStop(-2147483648);
switch (_istagetype) {
case 1:
 BA.debugLineNum = 162;BA.debugLine="Return \"Do nothing\"";
Debug.ShouldStop(2);
if (true) return "Do nothing";
 break;
case 2:
 BA.debugLineNum = 164;BA.debugLine="Return \"Heat to\"";
Debug.ShouldStop(8);
if (true) return "Heat to";
 break;
case 3:
 BA.debugLineNum = 166;BA.debugLine="Return \"Beep\"";
Debug.ShouldStop(32);
if (true) return "Beep";
 break;
}
;
 BA.debugLineNum = 169;BA.debugLine="End Sub";
Debug.ShouldStop(256);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static int  _stringtostagetype(String _sstagetype) throws Exception{
		Debug.PushSubsStack("StringToStageType (editstage) ","editstage",2,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("sStageType", _sstagetype);
 BA.debugLineNum = 171;BA.debugLine="Sub StringToStageType(sStageType As String) As Int";
Debug.ShouldStop(1024);
 BA.debugLineNum = 172;BA.debugLine="Select sStageType";
Debug.ShouldStop(2048);
switch (BA.switchObjectToInt(_sstagetype,"Do nothing","Heat to","Beep")) {
case 0:
 BA.debugLineNum = 174;BA.debugLine="Return 1";
Debug.ShouldStop(8192);
if (true) return (int)(1);
 break;
case 1:
 BA.debugLineNum = 176;BA.debugLine="Return 2";
Debug.ShouldStop(32768);
if (true) return (int)(2);
 break;
case 2:
 BA.debugLineNum = 178;BA.debugLine="Return 3";
Debug.ShouldStop(131072);
if (true) return (int)(3);
 break;
}
;
 BA.debugLineNum = 181;BA.debugLine="End Sub";
Debug.ShouldStop(1048576);
return 0;
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
  public Object[] GetGlobals() {
		return new Object[] {"Activity",_activity,"btnEditStageOK",_btneditstageok,"btnEditStageCancel",_btneditstagecancel,"chkBeep",_chkbeep,"chkDoNothing",_chkdonothing,"chkIncreaseTemp",_chkincreasetemp,"txtDuration",_txtduration,"txtRate",_txtrate,"txtTargetTemp",_txttargettemp,"Main",Debug.moduleToString(jimsrobot.arduino.attiny85ad8495bt.main.class),"ProfileList",Debug.moduleToString(jimsrobot.arduino.attiny85ad8495bt.profilelist.class),"StageList",Debug.moduleToString(jimsrobot.arduino.attiny85ad8495bt.stagelist.class)};
}
}
