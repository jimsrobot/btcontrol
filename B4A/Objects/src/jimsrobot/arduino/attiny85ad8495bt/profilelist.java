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

public class profilelist extends Activity implements B4AActivity{
	public static profilelist mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "jimsrobot.arduino.attiny85ad8495bt", "jimsrobot.arduino.attiny85ad8495bt.profilelist");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (profilelist).");
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
		activityBA = new BA(this, layout, processBA, "jimsrobot.arduino.attiny85ad8495bt", "jimsrobot.arduino.attiny85ad8495bt.profilelist");
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (profilelist) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (profilelist) Resume **");
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
		return profilelist.class;
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
        BA.LogInfo("** Activity (profilelist) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (profilelist) Resume **");
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
public static long _selectedprofileid = 0L;
public static String _selectedprofilename = "";
public static anywheresoftware.b4a.randomaccessfile.AsyncStreams _astream = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scvprofiles = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnnewprofile = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnrename = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btndeleteprofile = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtlog = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbltemperature = null;
public static String _smsgbuild = "";
public ADR.stringdemo.stringfunctions _sf = null;
public jimsrobot.arduino.attiny85ad8495bt.main _main = null;
public jimsrobot.arduino.attiny85ad8495bt.editstage _editstage = null;
public jimsrobot.arduino.attiny85ad8495bt.stagelist _stagelist = null;
public static String  _activity_create(boolean _firsttime) throws Exception{
		Debug.PushSubsStack("Activity_Create (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("FirstTime", _firsttime);
 BA.debugLineNum = 41;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
Debug.ShouldStop(256);
 BA.debugLineNum = 43;BA.debugLine="Activity.LoadLayout(\"ProfileList\")";
Debug.ShouldStop(1024);
mostCurrent._activity.LoadLayout("ProfileList",mostCurrent.activityBA);
 BA.debugLineNum = 48;BA.debugLine="If AStream.IsInitialized = False AND Main.SkipBT = False Then";
Debug.ShouldStop(32768);
if (_astream.IsInitialized()==anywheresoftware.b4a.keywords.Common.False && mostCurrent._main._skipbt==anywheresoftware.b4a.keywords.Common.False) { 
 BA.debugLineNum = 49;BA.debugLine="AStream.Initialize(Main.serial1.InputStream, Main.serial1.OutputStream, \"AStream\")";
Debug.ShouldStop(65536);
_astream.Initialize(processBA,mostCurrent._main._serial1.getInputStream(),mostCurrent._main._serial1.getOutputStream(),"AStream");
 };
 BA.debugLineNum = 53;BA.debugLine="If FirstTime Then";
Debug.ShouldStop(1048576);
if (_firsttime) { 
 BA.debugLineNum = 54;BA.debugLine="sf.Initialize";
Debug.ShouldStop(2097152);
mostCurrent._sf._vvv1(processBA);
 };
 BA.debugLineNum = 57;BA.debugLine="LoadProfiles(-1)";
Debug.ShouldStop(16777216);
_loadprofiles((long)(-1));
 BA.debugLineNum = 58;BA.debugLine="End Sub";
Debug.ShouldStop(33554432);
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
		Debug.PushSubsStack("Activity_Pause (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("UserClosed", _userclosed);
 BA.debugLineNum = 65;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
Debug.ShouldStop(1);
 BA.debugLineNum = 67;BA.debugLine="End Sub";
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
public static String  _activity_resume() throws Exception{
		Debug.PushSubsStack("Activity_Resume (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
 BA.debugLineNum = 60;BA.debugLine="Sub Activity_Resume";
Debug.ShouldStop(134217728);
 BA.debugLineNum = 61;BA.debugLine="SelectedProfileID = -1";
Debug.ShouldStop(268435456);
_selectedprofileid = (long)(-1);
 BA.debugLineNum = 62;BA.debugLine="LoadProfiles (-1)";
Debug.ShouldStop(536870912);
_loadprofiles((long)(-1));
 BA.debugLineNum = 63;BA.debugLine="End Sub";
Debug.ShouldStop(1073741824);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _astream_error() throws Exception{
		Debug.PushSubsStack("AStream_Error (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
 BA.debugLineNum = 78;BA.debugLine="Sub AStream_Error";
Debug.ShouldStop(8192);
 BA.debugLineNum = 79;BA.debugLine="ToastMessageShow(\"Connection is broken.\", True)";
Debug.ShouldStop(16384);
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Connection is broken.",anywheresoftware.b4a.keywords.Common.True);
 BA.debugLineNum = 80;BA.debugLine="End Sub";
Debug.ShouldStop(32768);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _astream_newdata(byte[] _buffer) throws Exception{
		Debug.PushSubsStack("AStream_NewData (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("Buffer", _buffer);
 BA.debugLineNum = 70;BA.debugLine="Sub AStream_NewData (Buffer() As Byte)";
Debug.ShouldStop(32);
 BA.debugLineNum = 71;BA.debugLine="sMsgBuild = sMsgBuild & BytesToString(Buffer, 0, Buffer.Length, \"UTF8\")";
Debug.ShouldStop(64);
mostCurrent._smsgbuild = mostCurrent._smsgbuild+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int)(0),_buffer.length,"UTF8");
 BA.debugLineNum = 72;BA.debugLine="If sMsgBuild.EndsWith(CRLF) Then";
Debug.ShouldStop(128);
if (mostCurrent._smsgbuild.endsWith(anywheresoftware.b4a.keywords.Common.CRLF)) { 
 BA.debugLineNum = 73;BA.debugLine="ProcessMessages(sMsgBuild)";
Debug.ShouldStop(256);
_processmessages(mostCurrent._smsgbuild);
 BA.debugLineNum = 74;BA.debugLine="sMsgBuild = \"\"";
Debug.ShouldStop(512);
mostCurrent._smsgbuild = "";
 };
 BA.debugLineNum = 76;BA.debugLine="End Sub";
Debug.ShouldStop(2048);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _astream_terminated() throws Exception{
		Debug.PushSubsStack("AStream_Terminated (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
 BA.debugLineNum = 82;BA.debugLine="Sub AStream_Terminated";
Debug.ShouldStop(131072);
 BA.debugLineNum = 83;BA.debugLine="AStream_Error";
Debug.ShouldStop(262144);
_astream_error();
 BA.debugLineNum = 84;BA.debugLine="End Sub";
Debug.ShouldStop(524288);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _btndeleteprofile_click() throws Exception{
		Debug.PushSubsStack("btnDeleteProfile_Click (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
int _ret = 0;
 BA.debugLineNum = 393;BA.debugLine="Sub btnDeleteProfile_Click";
Debug.ShouldStop(256);
 BA.debugLineNum = 395;BA.debugLine="Dim ret As Int";
Debug.ShouldStop(1024);
_ret = 0;Debug.locals.put("ret", _ret);
 BA.debugLineNum = 397;BA.debugLine="If SelectedProfileID <> -1 Then";
Debug.ShouldStop(4096);
if (_selectedprofileid!=-1) { 
 BA.debugLineNum = 398;BA.debugLine="ret = Msgbox2(\"Delete selected profile AND stages?\", \"Delete\", \"Delete\", \"Cancel\",\"\", Null)";
Debug.ShouldStop(8192);
_ret = anywheresoftware.b4a.keywords.Common.Msgbox2("Delete selected profile AND stages?","Delete","Delete","Cancel","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);Debug.locals.put("ret", _ret);
 BA.debugLineNum = 399;BA.debugLine="If ret = DialogResponse.POSITIVE Then";
Debug.ShouldStop(16384);
if (_ret==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 BA.debugLineNum = 400;BA.debugLine="Main.SQL1.ExecNonQuery(\"DELETE FROM stages WHERE profile_id =\" & SelectedProfileID)";
Debug.ShouldStop(32768);
mostCurrent._main._sql1.ExecNonQuery("DELETE FROM stages WHERE profile_id ="+BA.NumberToString(_selectedprofileid));
 BA.debugLineNum = 401;BA.debugLine="Main.SQL1.ExecNonQuery(\"DELETE FROM profiles WHERE profile_id =\" & SelectedProfileID)";
Debug.ShouldStop(65536);
mostCurrent._main._sql1.ExecNonQuery("DELETE FROM profiles WHERE profile_id ="+BA.NumberToString(_selectedprofileid));
 BA.debugLineNum = 402;BA.debugLine="LoadProfiles(-1)";
Debug.ShouldStop(131072);
_loadprofiles((long)(-1));
 };
 }else {
 BA.debugLineNum = 405;BA.debugLine="Msgbox(\"Select a profile to DELETE\", \"No selection\")";
Debug.ShouldStop(1048576);
anywheresoftware.b4a.keywords.Common.Msgbox("Select a profile to DELETE","No selection",mostCurrent.activityBA);
 };
 BA.debugLineNum = 407;BA.debugLine="End Sub";
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
public static String  _btneditstages_click() throws Exception{
		Debug.PushSubsStack("btnEditStages_Click (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
 BA.debugLineNum = 364;BA.debugLine="Sub btnEditStages_Click()";
Debug.ShouldStop(2048);
 BA.debugLineNum = 365;BA.debugLine="If SelectedProfileID <> -1 Then";
Debug.ShouldStop(4096);
if (_selectedprofileid!=-1) { 
 BA.debugLineNum = 366;BA.debugLine="StartActivity(StageList)";
Debug.ShouldStop(8192);
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._stagelist.getObject()));
 }else {
 BA.debugLineNum = 368;BA.debugLine="Msgbox(\"Select a profile to edit stages\", \"No selection\")";
Debug.ShouldStop(32768);
anywheresoftware.b4a.keywords.Common.Msgbox("Select a profile to edit stages","No selection",mostCurrent.activityBA);
 };
 BA.debugLineNum = 370;BA.debugLine="End Sub";
Debug.ShouldStop(131072);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _btngo_click() throws Exception{
		Debug.PushSubsStack("btnGO_Click (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
 BA.debugLineNum = 413;BA.debugLine="Sub btnGO_Click";
Debug.ShouldStop(268435456);
 BA.debugLineNum = 414;BA.debugLine="SendData(\"GO\" & CRLF)";
Debug.ShouldStop(536870912);
_senddata("GO"+anywheresoftware.b4a.keywords.Common.CRLF);
 BA.debugLineNum = 415;BA.debugLine="End Sub";
Debug.ShouldStop(1073741824);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _btnnewprofile_click() throws Exception{
		Debug.PushSubsStack("btnNewProfile_Click (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
anywheresoftware.b4a.agraham.dialogs.InputDialog _id = null;
String _ret = "";
 BA.debugLineNum = 349;BA.debugLine="Sub btnNewProfile_Click";
Debug.ShouldStop(268435456);
 BA.debugLineNum = 350;BA.debugLine="Dim Id As InputDialog";
Debug.ShouldStop(536870912);
_id = new anywheresoftware.b4a.agraham.dialogs.InputDialog();Debug.locals.put("Id", _id);
 BA.debugLineNum = 351;BA.debugLine="Dim ret As String";
Debug.ShouldStop(1073741824);
_ret = "";Debug.locals.put("ret", _ret);
 BA.debugLineNum = 352;BA.debugLine="Id.InputType = Id.INPUT_TYPE_TEXT";
Debug.ShouldStop(-2147483648);
_id.setInputType(_id.INPUT_TYPE_TEXT);
 BA.debugLineNum = 353;BA.debugLine="Id.Input = \"\"";
Debug.ShouldStop(1);
_id.setInput("");
 BA.debugLineNum = 354;BA.debugLine="Id.Hint = \"Enter the name of the profile\"";
Debug.ShouldStop(2);
_id.setHint("Enter the name of the profile");
 BA.debugLineNum = 355;BA.debugLine="Id.HintColor = Colors.ARGB(196, 255, 140, 0)";
Debug.ShouldStop(4);
_id.setHintColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int)(196),(int)(255),(int)(140),(int)(0)));
 BA.debugLineNum = 356;BA.debugLine="ret = DialogResponse.CANCEL";
Debug.ShouldStop(8);
_ret = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL);Debug.locals.put("ret", _ret);
 BA.debugLineNum = 357;BA.debugLine="ret = Id.Show(\"Input the name of the profile\", \"New Profile\", \"OK\", \"Cancel\", \"\", Null)";
Debug.ShouldStop(16);
_ret = BA.NumberToString(_id.Show("Input the name of the profile","New Profile","OK","Cancel","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)));Debug.locals.put("ret", _ret);
 BA.debugLineNum = 358;BA.debugLine="If Id.Response <> DialogResponse.CANCEL Then";
Debug.ShouldStop(32);
if (_id.getResponse()!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
 BA.debugLineNum = 359;BA.debugLine="Main.SQL1.ExecNonQuery(\"INSERT INTO profiles VALUES (Null,'\" & Id.Input & \"')\")";
Debug.ShouldStop(64);
mostCurrent._main._sql1.ExecNonQuery("INSERT INTO profiles VALUES (Null,'"+_id.getInput()+"')");
 BA.debugLineNum = 360;BA.debugLine="LoadProfiles(-1)";
Debug.ShouldStop(128);
_loadprofiles((long)(-1));
 };
 BA.debugLineNum = 362;BA.debugLine="End Sub";
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
public static String  _btnrename_click() throws Exception{
		Debug.PushSubsStack("btnRename_Click (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
anywheresoftware.b4a.agraham.dialogs.InputDialog _id = null;
String _ret = "";
 BA.debugLineNum = 372;BA.debugLine="Sub btnRename_Click";
Debug.ShouldStop(524288);
 BA.debugLineNum = 373;BA.debugLine="Dim Id As InputDialog";
Debug.ShouldStop(1048576);
_id = new anywheresoftware.b4a.agraham.dialogs.InputDialog();Debug.locals.put("Id", _id);
 BA.debugLineNum = 374;BA.debugLine="Dim ret As String";
Debug.ShouldStop(2097152);
_ret = "";Debug.locals.put("ret", _ret);
 BA.debugLineNum = 376;BA.debugLine="If SelectedProfileID <> -1 Then";
Debug.ShouldStop(8388608);
if (_selectedprofileid!=-1) { 
 BA.debugLineNum = 378;BA.debugLine="Id.InputType = Id.INPUT_TYPE_TEXT";
Debug.ShouldStop(33554432);
_id.setInputType(_id.INPUT_TYPE_TEXT);
 BA.debugLineNum = 379;BA.debugLine="Id.Input = \"\"";
Debug.ShouldStop(67108864);
_id.setInput("");
 BA.debugLineNum = 380;BA.debugLine="Id.Hint = \"Enter the NEW name of the profile\"";
Debug.ShouldStop(134217728);
_id.setHint("Enter the NEW name of the profile");
 BA.debugLineNum = 381;BA.debugLine="Id.HintColor = Colors.ARGB(196, 255, 140, 0)";
Debug.ShouldStop(268435456);
_id.setHintColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int)(196),(int)(255),(int)(140),(int)(0)));
 BA.debugLineNum = 382;BA.debugLine="ret = DialogResponse.CANCEL";
Debug.ShouldStop(536870912);
_ret = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL);Debug.locals.put("ret", _ret);
 BA.debugLineNum = 383;BA.debugLine="ret = Id.Show(\"Input the name of the profile\", \"Rename Profile\", \"OK\", \"Cancel\", \"\", Null)";
Debug.ShouldStop(1073741824);
_ret = BA.NumberToString(_id.Show("Input the name of the profile","Rename Profile","OK","Cancel","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)));Debug.locals.put("ret", _ret);
 BA.debugLineNum = 384;BA.debugLine="If Id.Response <> DialogResponse.CANCEL Then";
Debug.ShouldStop(-2147483648);
if (_id.getResponse()!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
 BA.debugLineNum = 385;BA.debugLine="Main.SQL1.ExecNonQuery(\"UPDATE profiles SET profile_name ='\" & Id.Input.SubString2(0, 48)  & \"' WHERE profile_id = \" & SelectedProfileID)";
Debug.ShouldStop(1);
mostCurrent._main._sql1.ExecNonQuery("UPDATE profiles SET profile_name ='"+_id.getInput().substring((int)(0),(int)(48))+"' WHERE profile_id = "+BA.NumberToString(_selectedprofileid));
 BA.debugLineNum = 386;BA.debugLine="LoadProfiles(-1)";
Debug.ShouldStop(2);
_loadprofiles((long)(-1));
 };
 }else {
 BA.debugLineNum = 389;BA.debugLine="Msgbox(\"Select a profile to rename\", \"No selection\")";
Debug.ShouldStop(16);
anywheresoftware.b4a.keywords.Common.Msgbox("Select a profile to rename","No selection",mostCurrent.activityBA);
 };
 BA.debugLineNum = 391;BA.debugLine="End Sub";
Debug.ShouldStop(64);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _btnupload_click() throws Exception{
		Debug.PushSubsStack("btnUpload_Click (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
 BA.debugLineNum = 409;BA.debugLine="Sub btnUpload_Click";
Debug.ShouldStop(16777216);
 BA.debugLineNum = 410;BA.debugLine="SendProfile";
Debug.ShouldStop(33554432);
_sendprofile();
 BA.debugLineNum = 411;BA.debugLine="End Sub";
Debug.ShouldStop(67108864);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _clearlist() throws Exception{
		Debug.PushSubsStack("ClearList (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
int _i = 0;
 BA.debugLineNum = 313;BA.debugLine="Sub ClearList";
Debug.ShouldStop(16777216);
 BA.debugLineNum = 315;BA.debugLine="Dim i As Int";
Debug.ShouldStop(67108864);
_i = 0;Debug.locals.put("i", _i);
 BA.debugLineNum = 316;BA.debugLine="For i = scvProfiles.Panel.NumberOfViews -1 To 0 Step -1";
Debug.ShouldStop(134217728);
{
final double step178 = -1;
final double limit178 = (int)(0);
for (_i = (int)(mostCurrent._scvprofiles.getPanel().getNumberOfViews()-1); (step178 > 0 && _i <= limit178) || (step178 < 0 && _i >= limit178); _i += step178) {
Debug.locals.put("i", _i);
 BA.debugLineNum = 317;BA.debugLine="scvProfiles.Panel.RemoveViewAt(i)";
Debug.ShouldStop(268435456);
mostCurrent._scvprofiles.getPanel().RemoveViewAt(_i);
 }
}Debug.locals.put("i", _i);
;
 BA.debugLineNum = 320;BA.debugLine="SelectedProfileID = -1	'Nothing selected";
Debug.ShouldStop(-2147483648);
_selectedprofileid = (long)(-1);
 BA.debugLineNum = 321;BA.debugLine="SelectedProfileName = \"\" 'No name";
Debug.ShouldStop(1);
_selectedprofilename = "";
 BA.debugLineNum = 323;BA.debugLine="End Sub";
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

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 24;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 28;BA.debugLine="Dim scvProfiles As ScrollView";
mostCurrent._scvprofiles = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Dim btnNewProfile As Button";
mostCurrent._btnnewprofile = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Dim btnRename As Button";
mostCurrent._btnrename = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Dim btnDeleteProfile As Button";
mostCurrent._btndeleteprofile = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Dim txtLog As EditText";
mostCurrent._txtlog = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Dim lblTemperature As Label";
mostCurrent._lbltemperature = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Dim sMsgBuild As String";
mostCurrent._smsgbuild = "";
 //BA.debugLineNum = 37;BA.debugLine="Dim sf As StringFunctions";
mostCurrent._sf = new ADR.stringdemo.stringfunctions();
 //BA.debugLineNum = 39;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_click() throws Exception{
		Debug.PushSubsStack("lbl_Click (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
anywheresoftware.b4a.objects.LabelWrapper _send = null;
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
 BA.debugLineNum = 337;BA.debugLine="Sub lbl_Click";
Debug.ShouldStop(65536);
 BA.debugLineNum = 338;BA.debugLine="Dim Send As Label";
Debug.ShouldStop(131072);
_send = new anywheresoftware.b4a.objects.LabelWrapper();Debug.locals.put("Send", _send);
 BA.debugLineNum = 339;BA.debugLine="Dim pnl As Panel";
Debug.ShouldStop(262144);
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();Debug.locals.put("pnl", _pnl);
 BA.debugLineNum = 340;BA.debugLine="Send=Sender";
Debug.ShouldStop(524288);
_send.setObject((android.widget.TextView)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 BA.debugLineNum = 341;BA.debugLine="pnl = Send.tag";
Debug.ShouldStop(1048576);
_pnl.setObject((android.view.ViewGroup)(_send.getTag()));
 BA.debugLineNum = 342;BA.debugLine="LoadProfiles(pnl.Tag)";
Debug.ShouldStop(2097152);
_loadprofiles(BA.ObjectToLongNumber(_pnl.getTag()));
 BA.debugLineNum = 344;BA.debugLine="End Sub";
Debug.ShouldStop(8388608);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _loadprofiles(long _lselectid) throws Exception{
		Debug.PushSubsStack("LoadProfiles (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
int _i = 0;
anywheresoftware.b4a.objects.PanelWrapper _pnladd = null;
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
Debug.locals.put("lSelectID", _lselectid);
 BA.debugLineNum = 257;BA.debugLine="Sub LoadProfiles(lSelectID As Long)";
Debug.ShouldStop(1);
 BA.debugLineNum = 260;BA.debugLine="Main.SQL1.ExecNonQuery(\"CREATE TABLE IF NOT EXISTS profiles (profile_id INTEGER PRIMARY KEY AUTOINCREMENT, profile_name TEXT)\")";
Debug.ShouldStop(8);
mostCurrent._main._sql1.ExecNonQuery("CREATE TABLE IF NOT EXISTS profiles (profile_id INTEGER PRIMARY KEY AUTOINCREMENT, profile_name TEXT)");
 BA.debugLineNum = 263;BA.debugLine="Main.SQL1.ExecNonQuery(\"CREATE TABLE IF NOT EXISTS stages  	(stage_id INTEGER PRIMARY KEY AUTOINCREMENT, \" & _ 																															\"profile_id INTEGER, \" & _ 																															\"stage_type INTEGER,  \" & _ 																															\"stage_temp INTEGER,  \" & _ 																															\"stage_percent INTEGER,  \" & _ 																															\"stage_seconds INTEGER,  \" & _ 																															\"stage_sort_order INTEGER)\")";
Debug.ShouldStop(64);
mostCurrent._main._sql1.ExecNonQuery("CREATE TABLE IF NOT EXISTS stages  	(stage_id INTEGER PRIMARY KEY AUTOINCREMENT, "+"profile_id INTEGER, "+"stage_type INTEGER,  "+"stage_temp INTEGER,  "+"stage_percent INTEGER,  "+"stage_seconds INTEGER,  "+"stage_sort_order INTEGER)");
 BA.debugLineNum = 272;BA.debugLine="ClearList";
Debug.ShouldStop(32768);
_clearlist();
 BA.debugLineNum = 274;BA.debugLine="Dim Cursor1 As Cursor";
Debug.ShouldStop(131072);
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();Debug.locals.put("Cursor1", _cursor1);
 BA.debugLineNum = 275;BA.debugLine="Cursor1 = Main.SQL1.ExecQuery(\"SELECT profile_id, profile_name FROM profiles\")";
Debug.ShouldStop(262144);
_cursor1.setObject((android.database.Cursor)(mostCurrent._main._sql1.ExecQuery("SELECT profile_id, profile_name FROM profiles")));
 BA.debugLineNum = 276;BA.debugLine="For i = 0 To Cursor1.RowCount - 1";
Debug.ShouldStop(524288);
{
final double step149 = 1;
final double limit149 = (int)(_cursor1.getRowCount()-1);
for (_i = (int)(0); (step149 > 0 && _i <= limit149) || (step149 < 0 && _i >= limit149); _i += step149) {
Debug.locals.put("i", _i);
 BA.debugLineNum = 277;BA.debugLine="Dim pnlAdd As Panel";
Debug.ShouldStop(1048576);
_pnladd = new anywheresoftware.b4a.objects.PanelWrapper();Debug.locals.put("pnlAdd", _pnladd);
 BA.debugLineNum = 278;BA.debugLine="Dim lbl As Label";
Debug.ShouldStop(2097152);
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();Debug.locals.put("lbl", _lbl);
 BA.debugLineNum = 279;BA.debugLine="pnlAdd.Initialize(\"pnlAdd\")";
Debug.ShouldStop(4194304);
_pnladd.Initialize(mostCurrent.activityBA,"pnlAdd");
 BA.debugLineNum = 280;BA.debugLine="scvProfiles.Panel.AddView(pnlAdd,0,5dip+i*50dip,100%x,50dip)";
Debug.ShouldStop(8388608);
mostCurrent._scvprofiles.getPanel().AddView((android.view.View)(_pnladd.getObject()),(int)(0),(int)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int)(5))+_i*anywheresoftware.b4a.keywords.Common.DipToCurrent((int)(50))),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float)(100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int)(50)));
 BA.debugLineNum = 282;BA.debugLine="Cursor1.Position = i";
Debug.ShouldStop(33554432);
_cursor1.setPosition(_i);
 BA.debugLineNum = 285;BA.debugLine="Dim lbl As Label";
Debug.ShouldStop(268435456);
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();Debug.locals.put("lbl", _lbl);
 BA.debugLineNum = 286;BA.debugLine="lbl.Initialize(\"lbl\")";
Debug.ShouldStop(536870912);
_lbl.Initialize(mostCurrent.activityBA,"lbl");
 BA.debugLineNum = 287;BA.debugLine="lbl.Text = Cursor1.Getstring(\"profile_name\")";
Debug.ShouldStop(1073741824);
_lbl.setText((Object)(_cursor1.GetString("profile_name")));
 BA.debugLineNum = 289;BA.debugLine="pnlAdd.Color = Colors.White";
Debug.ShouldStop(1);
_pnladd.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 BA.debugLineNum = 290;BA.debugLine="pnlAdd.AddView(lbl,10dip, 5dip, 80dip, 40dip)";
Debug.ShouldStop(2);
_pnladd.AddView((android.view.View)(_lbl.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int)(10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int)(5)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int)(80)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int)(40)));
 BA.debugLineNum = 291;BA.debugLine="lbl.TextSize=20";
Debug.ShouldStop(4);
_lbl.setTextSize((float)(20));
 BA.debugLineNum = 292;BA.debugLine="lbl.TextColor = Colors.DarkGray";
Debug.ShouldStop(8);
_lbl.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 BA.debugLineNum = 293;BA.debugLine="If Cursor1.GetInt(\"profile_id\") = lSelectID Then";
Debug.ShouldStop(16);
if (_cursor1.GetInt("profile_id")==_lselectid) { 
 BA.debugLineNum = 294;BA.debugLine="lbl.TextColor = Colors.Black";
Debug.ShouldStop(32);
_lbl.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 BA.debugLineNum = 295;BA.debugLine="pnlAdd.Color = Colors.LightGray";
Debug.ShouldStop(64);
_pnladd.setColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 BA.debugLineNum = 296;BA.debugLine="SelectedProfileID  = lSelectID";
Debug.ShouldStop(128);
_selectedprofileid = _lselectid;
 BA.debugLineNum = 297;BA.debugLine="SelectedProfileName = Cursor1.Getstring(\"profile_name\")";
Debug.ShouldStop(256);
_selectedprofilename = _cursor1.GetString("profile_name");
 };
 BA.debugLineNum = 299;BA.debugLine="pnlAdd.Tag = Cursor1.GetInt(\"profile_id\")";
Debug.ShouldStop(1024);
_pnladd.setTag((Object)(_cursor1.GetInt("profile_id")));
 BA.debugLineNum = 300;BA.debugLine="lbl.Tag = pnlAdd";
Debug.ShouldStop(2048);
_lbl.setTag((Object)(_pnladd.getObject()));
 }
}Debug.locals.put("i", _i);
;
 BA.debugLineNum = 303;BA.debugLine="Cursor1.Close";
Debug.ShouldStop(16384);
_cursor1.Close();
 BA.debugLineNum = 305;BA.debugLine="scvProfiles.Panel.Height = 50dip * i";
Debug.ShouldStop(65536);
mostCurrent._scvprofiles.getPanel().setHeight((int)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int)(50))*_i));
 BA.debugLineNum = 306;BA.debugLine="DoEvents";
Debug.ShouldStop(131072);
anywheresoftware.b4a.keywords.Common.DoEvents();
 BA.debugLineNum = 309;BA.debugLine="scvProfiles.Panel.Height=50%y";
Debug.ShouldStop(1048576);
mostCurrent._scvprofiles.getPanel().setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float)(50),mostCurrent.activityBA));
 BA.debugLineNum = 311;BA.debugLine="End Sub";
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
public static String  _padstringto2(String _instring) throws Exception{
		Debug.PushSubsStack("PadStringTo2 (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
int _ivalue = 0;
String _retstr = "";
Debug.locals.put("inString", _instring);
 BA.debugLineNum = 219;BA.debugLine="Sub PadStringTo2(inString As String) As String";
Debug.ShouldStop(67108864);
 BA.debugLineNum = 220;BA.debugLine="Dim iValue As Int";
Debug.ShouldStop(134217728);
_ivalue = 0;Debug.locals.put("iValue", _ivalue);
 BA.debugLineNum = 221;BA.debugLine="Dim retStr As String";
Debug.ShouldStop(268435456);
_retstr = "";Debug.locals.put("retStr", _retstr);
 BA.debugLineNum = 222;BA.debugLine="iValue = inString";
Debug.ShouldStop(536870912);
_ivalue = (int)(Double.parseDouble(_instring));Debug.locals.put("iValue", _ivalue);
 BA.debugLineNum = 224;BA.debugLine="If iValue < 10 Then";
Debug.ShouldStop(-2147483648);
if (_ivalue<10) { 
 BA.debugLineNum = 225;BA.debugLine="retStr = \"0\" & inString";
Debug.ShouldStop(1);
_retstr = "0"+_instring;Debug.locals.put("retStr", _retstr);
 }else {
 BA.debugLineNum = 227;BA.debugLine="retStr = inString";
Debug.ShouldStop(4);
_retstr = _instring;Debug.locals.put("retStr", _retstr);
 };
 BA.debugLineNum = 230;BA.debugLine="Return retStr";
Debug.ShouldStop(32);
if (true) return _retstr;
 BA.debugLineNum = 231;BA.debugLine="End Sub";
Debug.ShouldStop(64);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _padstringto3(String _instring) throws Exception{
		Debug.PushSubsStack("PadStringTo3 (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
int _ivalue = 0;
String _retstr = "";
Debug.locals.put("inString", _instring);
 BA.debugLineNum = 233;BA.debugLine="Sub PadStringTo3(inString As String) As String";
Debug.ShouldStop(256);
 BA.debugLineNum = 234;BA.debugLine="Dim iValue As Int";
Debug.ShouldStop(512);
_ivalue = 0;Debug.locals.put("iValue", _ivalue);
 BA.debugLineNum = 235;BA.debugLine="Dim retStr As String";
Debug.ShouldStop(1024);
_retstr = "";Debug.locals.put("retStr", _retstr);
 BA.debugLineNum = 236;BA.debugLine="iValue = inString";
Debug.ShouldStop(2048);
_ivalue = (int)(Double.parseDouble(_instring));Debug.locals.put("iValue", _ivalue);
 BA.debugLineNum = 238;BA.debugLine="If iValue < 10 Then";
Debug.ShouldStop(8192);
if (_ivalue<10) { 
 BA.debugLineNum = 239;BA.debugLine="retStr = \"00\" & inString";
Debug.ShouldStop(16384);
_retstr = "00"+_instring;Debug.locals.put("retStr", _retstr);
 }else 
{ BA.debugLineNum = 240;BA.debugLine="Else If iValue < 100 Then";
Debug.ShouldStop(32768);
if (_ivalue<100) { 
 BA.debugLineNum = 241;BA.debugLine="retStr = \"0\" & inString";
Debug.ShouldStop(65536);
_retstr = "0"+_instring;Debug.locals.put("retStr", _retstr);
 }else {
 BA.debugLineNum = 243;BA.debugLine="retStr = inString";
Debug.ShouldStop(262144);
_retstr = _instring;Debug.locals.put("retStr", _retstr);
 }};
 BA.debugLineNum = 246;BA.debugLine="Return retStr";
Debug.ShouldStop(2097152);
if (true) return _retstr;
 BA.debugLineNum = 247;BA.debugLine="End Sub";
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
public static String  _pnladd_click() throws Exception{
		Debug.PushSubsStack("pnlAdd_Click (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
anywheresoftware.b4a.objects.PanelWrapper _send = null;
 BA.debugLineNum = 328;BA.debugLine="Sub pnlAdd_Click";
Debug.ShouldStop(128);
 BA.debugLineNum = 330;BA.debugLine="Dim Send As Panel";
Debug.ShouldStop(512);
_send = new anywheresoftware.b4a.objects.PanelWrapper();Debug.locals.put("Send", _send);
 BA.debugLineNum = 331;BA.debugLine="Send = Sender";
Debug.ShouldStop(1024);
_send.setObject((android.view.ViewGroup)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 BA.debugLineNum = 333;BA.debugLine="LoadProfiles(Send.Tag)";
Debug.ShouldStop(4096);
_loadprofiles(BA.ObjectToLongNumber(_send.getTag()));
 BA.debugLineNum = 335;BA.debugLine="End Sub";
Debug.ShouldStop(16384);
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
 //BA.debugLineNum = 16;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 19;BA.debugLine="Dim SelectedProfileID As Long : SelectedProfileID = -1";
_selectedprofileid = 0L;
 //BA.debugLineNum = 19;BA.debugLine="Dim SelectedProfileID As Long : SelectedProfileID = -1";
_selectedprofileid = (long)(-1);
 //BA.debugLineNum = 20;BA.debugLine="Dim SelectedProfileName As String";
_selectedprofilename = "";
 //BA.debugLineNum = 21;BA.debugLine="Dim AStream As AsyncStreams			'USed for Bluetooth";
_astream = new anywheresoftware.b4a.randomaccessfile.AsyncStreams();
 //BA.debugLineNum = 22;BA.debugLine="End Sub";
return "";
}
public static String  _processmessage(String _msg) throws Exception{
		Debug.PushSubsStack("ProcessMessage (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
String _stype = "";
String _sworking = "";
anywheresoftware.b4a.audio.Beeper _b = null;
Debug.locals.put("Msg", _msg);
 BA.debugLineNum = 105;BA.debugLine="Sub ProcessMessage (Msg As String)";
Debug.ShouldStop(256);
 BA.debugLineNum = 118;BA.debugLine="If Msg <> \"\" AND Msg.Length > 1 Then";
Debug.ShouldStop(2097152);
if ((_msg).equals("") == false && _msg.length()>1) { 
 BA.debugLineNum = 120;BA.debugLine="Dim sType As String";
Debug.ShouldStop(8388608);
_stype = "";Debug.locals.put("sType", _stype);
 BA.debugLineNum = 121;BA.debugLine="Dim sWorking As String 'Dummy to build strings";
Debug.ShouldStop(16777216);
_sworking = "";Debug.locals.put("sWorking", _sworking);
 BA.debugLineNum = 126;BA.debugLine="sType = Msg.SubString2(0,2)";
Debug.ShouldStop(536870912);
_stype = _msg.substring((int)(0),(int)(2));Debug.locals.put("sType", _stype);
 BA.debugLineNum = 128;BA.debugLine="Select sType";
Debug.ShouldStop(-2147483648);
switch (BA.switchObjectToInt(_stype,"C:","B:","P:","S:","D:","E:")) {
case 0:
 BA.debugLineNum = 130;BA.debugLine="sWorking = Msg.Replace(\"C:\",\"\")";
Debug.ShouldStop(2);
_sworking = _msg.replace("C:","");Debug.locals.put("sWorking", _sworking);
 BA.debugLineNum = 131;BA.debugLine="sWorking = sWorking.Replace(CRLF, \"\")";
Debug.ShouldStop(4);
_sworking = _sworking.replace(anywheresoftware.b4a.keywords.Common.CRLF,"");Debug.locals.put("sWorking", _sworking);
 BA.debugLineNum = 132;BA.debugLine="lblTemperature.Text = sWorking & \" \" & Chr(0x00B0) & \"C\"";
Debug.ShouldStop(8);
mostCurrent._lbltemperature.setText((Object)(_sworking+" "+String.valueOf(anywheresoftware.b4a.keywords.Common.Chr((int)(0x00b0)))+"C"));
 BA.debugLineNum = 133;BA.debugLine="sWorking = \"\"";
Debug.ShouldStop(16);
_sworking = "";Debug.locals.put("sWorking", _sworking);
 break;
case 1:
 BA.debugLineNum = 136;BA.debugLine="Dim b As Beeper";
Debug.ShouldStop(128);
_b = new anywheresoftware.b4a.audio.Beeper();Debug.locals.put("b", _b);
 BA.debugLineNum = 137;BA.debugLine="b.Initialize(600, 500) '600 milliseconds, 500 hz";
Debug.ShouldStop(256);
_b.Initialize((int)(600),(int)(500));
 BA.debugLineNum = 138;BA.debugLine="b.Beep";
Debug.ShouldStop(512);
_b.Beep();
 BA.debugLineNum = 139;BA.debugLine="txtLog.Text = \"BEEP !!\" & CRLF & txtLog.Text";
Debug.ShouldStop(1024);
mostCurrent._txtlog.setText((Object)("BEEP !!"+anywheresoftware.b4a.keywords.Common.CRLF+mostCurrent._txtlog.getText()));
 break;
case 2:
 BA.debugLineNum = 142;BA.debugLine="txtLog.Text = \"Profile: \" & Msg.SubString(2) & CRLF & txtLog.Text";
Debug.ShouldStop(8192);
mostCurrent._txtlog.setText((Object)("Profile: "+_msg.substring((int)(2))+anywheresoftware.b4a.keywords.Common.CRLF+mostCurrent._txtlog.getText()));
 break;
case 3:
 BA.debugLineNum = 145;BA.debugLine="txtLog.Text = \"Stage: \" & Msg.SubString(2) & CRLF & txtLog.Text";
Debug.ShouldStop(65536);
mostCurrent._txtlog.setText((Object)("Stage: "+_msg.substring((int)(2))+anywheresoftware.b4a.keywords.Common.CRLF+mostCurrent._txtlog.getText()));
 break;
case 4:
 BA.debugLineNum = 148;BA.debugLine="txtLog.Text = \"Complete!\" & CRLF & txtLog.Text";
Debug.ShouldStop(524288);
mostCurrent._txtlog.setText((Object)("Complete!"+anywheresoftware.b4a.keywords.Common.CRLF+mostCurrent._txtlog.getText()));
 break;
case 5:
 BA.debugLineNum = 151;BA.debugLine="txtLog.Text = \"Uploaded:\" & Msg.SubString(2) & CRLF & txtLog.Text";
Debug.ShouldStop(4194304);
mostCurrent._txtlog.setText((Object)("Uploaded:"+_msg.substring((int)(2))+anywheresoftware.b4a.keywords.Common.CRLF+mostCurrent._txtlog.getText()));
 break;
default:
 BA.debugLineNum = 154;BA.debugLine="Log(Msg)";
Debug.ShouldStop(33554432);
anywheresoftware.b4a.keywords.Common.Log(_msg);
 BA.debugLineNum = 155;BA.debugLine="txtLog.Text = Msg & CRLF & txtLog.Text";
Debug.ShouldStop(67108864);
mostCurrent._txtlog.setText((Object)(_msg+anywheresoftware.b4a.keywords.Common.CRLF+mostCurrent._txtlog.getText()));
 break;
}
;
 BA.debugLineNum = 161;BA.debugLine="If txtLog.Text.Length > 2000 Then";
Debug.ShouldStop(1);
if (mostCurrent._txtlog.getText().length()>2000) { 
 BA.debugLineNum = 162;BA.debugLine="txtLog.Text = txtLog.text.SubString2(0,2000)";
Debug.ShouldStop(2);
mostCurrent._txtlog.setText((Object)(mostCurrent._txtlog.getText().substring((int)(0),(int)(2000))));
 };
 };
 BA.debugLineNum = 167;BA.debugLine="End Sub";
Debug.ShouldStop(64);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _processmessages(String _msg) throws Exception{
		Debug.PushSubsStack("ProcessMessages (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
anywheresoftware.b4a.objects.collections.List _msglist = null;
int _i = 0;
Debug.locals.put("Msg", _msg);
 BA.debugLineNum = 86;BA.debugLine="Sub ProcessMessages (Msg As String)";
Debug.ShouldStop(2097152);
 BA.debugLineNum = 89;BA.debugLine="Dim MsgList As List";
Debug.ShouldStop(16777216);
_msglist = new anywheresoftware.b4a.objects.collections.List();Debug.locals.put("MsgList", _msglist);
 BA.debugLineNum = 90;BA.debugLine="Dim i As Int";
Debug.ShouldStop(33554432);
_i = 0;Debug.locals.put("i", _i);
 BA.debugLineNum = 93;BA.debugLine="If Msg.IndexOf(CRLF) = Msg.Length - 1 Then";
Debug.ShouldStop(268435456);
if (_msg.indexOf(anywheresoftware.b4a.keywords.Common.CRLF)==_msg.length()-1) { 
 BA.debugLineNum = 94;BA.debugLine="ProcessMessage(Msg)";
Debug.ShouldStop(536870912);
_processmessage(_msg);
 }else {
 BA.debugLineNum = 96;BA.debugLine="MsgList = sf.Split(Msg, CRLF)";
Debug.ShouldStop(-2147483648);
_msglist = mostCurrent._sf._vvvvv3(_msg,anywheresoftware.b4a.keywords.Common.CRLF);Debug.locals.put("MsgList", _msglist);
 BA.debugLineNum = 98;BA.debugLine="For i = 0 To MsgList.Size - 1";
Debug.ShouldStop(2);
{
final double step52 = 1;
final double limit52 = (int)(_msglist.getSize()-1);
for (_i = (int)(0); (step52 > 0 && _i <= limit52) || (step52 < 0 && _i >= limit52); _i += step52) {
Debug.locals.put("i", _i);
 BA.debugLineNum = 99;BA.debugLine="ProcessMessage(MsgList.Get(i))";
Debug.ShouldStop(4);
_processmessage(String.valueOf(_msglist.Get(_i)));
 }
}Debug.locals.put("i", _i);
;
 };
 BA.debugLineNum = 103;BA.debugLine="End Sub";
Debug.ShouldStop(64);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _senddata(String _sendstring) throws Exception{
		Debug.PushSubsStack("SendData (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("SendString", _sendstring);
 BA.debugLineNum = 249;BA.debugLine="Sub SendData(SendString As String)";
Debug.ShouldStop(16777216);
 BA.debugLineNum = 250;BA.debugLine="AStream.Write(SendString.GetBytes(\"UTF8\"))";
Debug.ShouldStop(33554432);
_astream.Write(_sendstring.getBytes("UTF8"));
 BA.debugLineNum = 251;BA.debugLine="End Sub";
Debug.ShouldStop(67108864);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _sendprofile() throws Exception{
		Debug.PushSubsStack("SendProfile (profilelist) ","profilelist",1,mostCurrent.activityBA,mostCurrent);
try {
String _ret = "";
String _sprofile = "";
int _i = 0;
int _icount = 0;
String _scount = "";
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
 BA.debugLineNum = 170;BA.debugLine="Sub SendProfile";
Debug.ShouldStop(512);
 BA.debugLineNum = 176;BA.debugLine="Dim ret As String";
Debug.ShouldStop(32768);
_ret = "";Debug.locals.put("ret", _ret);
 BA.debugLineNum = 177;BA.debugLine="Dim sProfile As String";
Debug.ShouldStop(65536);
_sprofile = "";Debug.locals.put("sProfile", _sprofile);
 BA.debugLineNum = 178;BA.debugLine="Dim i As Int";
Debug.ShouldStop(131072);
_i = 0;Debug.locals.put("i", _i);
 BA.debugLineNum = 180;BA.debugLine="If SelectedProfileID > -1 Then";
Debug.ShouldStop(524288);
if (_selectedprofileid>-1) { 
 BA.debugLineNum = 183;BA.debugLine="Dim iCount As Int";
Debug.ShouldStop(4194304);
_icount = 0;Debug.locals.put("iCount", _icount);
 BA.debugLineNum = 184;BA.debugLine="Dim sCount As String";
Debug.ShouldStop(8388608);
_scount = "";Debug.locals.put("sCount", _scount);
 BA.debugLineNum = 186;BA.debugLine="iCount = Main.SQL1.ExecQuerySingleResult(\"SELECT count(*) FROM stages WHERE profile_id = \" & SelectedProfileID)";
Debug.ShouldStop(33554432);
_icount = (int)(Double.parseDouble(mostCurrent._main._sql1.ExecQuerySingleResult("SELECT count(*) FROM stages WHERE profile_id = "+BA.NumberToString(_selectedprofileid))));Debug.locals.put("iCount", _icount);
 BA.debugLineNum = 187;BA.debugLine="sCount = iCount";
Debug.ShouldStop(67108864);
_scount = BA.NumberToString(_icount);Debug.locals.put("sCount", _scount);
 BA.debugLineNum = 190;BA.debugLine="sProfile = SelectedProfileName";
Debug.ShouldStop(536870912);
_sprofile = _selectedprofilename;Debug.locals.put("sProfile", _sprofile);
 BA.debugLineNum = 191;BA.debugLine="If sProfile.Length > 20 Then sProfile = sProfile.SubString2(0, 19) 'max 20 characters";
Debug.ShouldStop(1073741824);
if (_sprofile.length()>20) { 
_sprofile = _sprofile.substring((int)(0),(int)(19));Debug.locals.put("sProfile", _sprofile);};
 BA.debugLineNum = 192;BA.debugLine="sProfile = sProfile & \"[\" & PadStringTo2(sCount)							     'Prepare the start of the profile";
Debug.ShouldStop(-2147483648);
_sprofile = _sprofile+"["+_padstringto2(_scount);Debug.locals.put("sProfile", _sprofile);
 BA.debugLineNum = 194;BA.debugLine="Dim Cursor1 As Cursor";
Debug.ShouldStop(2);
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();Debug.locals.put("Cursor1", _cursor1);
 BA.debugLineNum = 195;BA.debugLine="Cursor1 = Main.SQL1.ExecQuery(\"SELECT stage_id, stage_type, stage_temp, stage_percent, stage_seconds, stage_sort_order FROM stages WHERE profile_id=\" _ 																	& SelectedProfileID & \" ORDER BY stage_sort_order\")";
Debug.ShouldStop(4);
_cursor1.setObject((android.database.Cursor)(mostCurrent._main._sql1.ExecQuery("SELECT stage_id, stage_type, stage_temp, stage_percent, stage_seconds, stage_sort_order FROM stages WHERE profile_id="+BA.NumberToString(_selectedprofileid)+" ORDER BY stage_sort_order")));
 BA.debugLineNum = 197;BA.debugLine="For i = 0 To Cursor1.RowCount - 1";
Debug.ShouldStop(16);
{
final double step104 = 1;
final double limit104 = (int)(_cursor1.getRowCount()-1);
for (_i = (int)(0); (step104 > 0 && _i <= limit104) || (step104 < 0 && _i >= limit104); _i += step104) {
Debug.locals.put("i", _i);
 BA.debugLineNum = 198;BA.debugLine="Cursor1.Position = i";
Debug.ShouldStop(32);
_cursor1.setPosition(_i);
 BA.debugLineNum = 199;BA.debugLine="sProfile = sProfile & \"[\"";
Debug.ShouldStop(64);
_sprofile = _sprofile+"[";Debug.locals.put("sProfile", _sprofile);
 BA.debugLineNum = 200;BA.debugLine="sProfile = sProfile & Cursor1.GetString(\"stage_type\") _ 						 & PadStringTo3(Cursor1.GetString(\"stage_temp\")) _ 						 & PadStringTo3(Cursor1.GetString(\"stage_percent\")) _  						 & PadStringTo3(Cursor1.GetString(\"stage_seconds\"))";
Debug.ShouldStop(128);
_sprofile = _sprofile+_cursor1.GetString("stage_type")+_padstringto3(_cursor1.GetString("stage_temp"))+_padstringto3(_cursor1.GetString("stage_percent"))+_padstringto3(_cursor1.GetString("stage_seconds"));Debug.locals.put("sProfile", _sprofile);
 }
}Debug.locals.put("i", _i);
;
 BA.debugLineNum = 205;BA.debugLine="Cursor1.Close";
Debug.ShouldStop(4096);
_cursor1.Close();
 BA.debugLineNum = 210;BA.debugLine="Log(sProfile)";
Debug.ShouldStop(131072);
anywheresoftware.b4a.keywords.Common.Log(_sprofile);
 BA.debugLineNum = 213;BA.debugLine="SendData(sProfile & CRLF)";
Debug.ShouldStop(1048576);
_senddata(_sprofile+anywheresoftware.b4a.keywords.Common.CRLF);
 }else {
 BA.debugLineNum = 215;BA.debugLine="Msgbox(\"Select a profile to upload\", \"No Profile\")";
Debug.ShouldStop(4194304);
anywheresoftware.b4a.keywords.Common.Msgbox("Select a profile to upload","No Profile",mostCurrent.activityBA);
 };
 BA.debugLineNum = 217;BA.debugLine="End Sub";
Debug.ShouldStop(16777216);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
  public Object[] GetGlobals() {
		return new Object[] {"Activity",_activity,"SelectedProfileID",_selectedprofileid,"SelectedProfileName",_selectedprofilename,"AStream",_astream,"scvProfiles",_scvprofiles,"btnNewProfile",_btnnewprofile,"btnRename",_btnrename,"btnDeleteProfile",_btndeleteprofile,"txtLog",_txtlog,"lblTemperature",_lbltemperature,"sMsgBuild",_smsgbuild,"sf",_sf,"Main",Debug.moduleToString(jimsrobot.arduino.attiny85ad8495bt.main.class),"EditStage",Debug.moduleToString(jimsrobot.arduino.attiny85ad8495bt.editstage.class),"StageList",Debug.moduleToString(jimsrobot.arduino.attiny85ad8495bt.stagelist.class)};
}
}
