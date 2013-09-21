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

public class stagelist extends Activity implements B4AActivity{
	public static stagelist mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "jimsrobot.arduino.attiny85ad8495bt", "jimsrobot.arduino.attiny85ad8495bt.stagelist");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (stagelist).");
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
		activityBA = new BA(this, layout, processBA, "jimsrobot.arduino.attiny85ad8495bt", "jimsrobot.arduino.attiny85ad8495bt.stagelist");
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (stagelist) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (stagelist) Resume **");
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
		return stagelist.class;
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
        BA.LogInfo("** Activity (stagelist) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (stagelist) Resume **");
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

public static class _sortitem{
public boolean IsInitialized;
public long StageID;
public long SwapStageID;
public long CurrentSortOrder;
public long NewSortOrder;
public void Initialize() {
IsInitialized = true;
StageID = 0L;
SwapStageID = 0L;
CurrentSortOrder = 0L;
NewSortOrder = 0L;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public anywheresoftware.b4a.keywords.Common __c = null;
public static long _selectedstageid = 0L;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scvstages = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnstageadd = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnstagedelete = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnstageup = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnstagedown = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnstageedit = null;
public jimsrobot.arduino.attiny85ad8495bt.main _main = null;
public jimsrobot.arduino.attiny85ad8495bt.profilelist _profilelist = null;
public jimsrobot.arduino.attiny85ad8495bt.editstage _editstage = null;
public static String  _activity_create(boolean _firsttime) throws Exception{
		Debug.PushSubsStack("Activity_Create (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("FirstTime", _firsttime);
 BA.debugLineNum = 26;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
Debug.ShouldStop(33554432);
 BA.debugLineNum = 29;BA.debugLine="Activity.LoadLayout(\"StageList\")";
Debug.ShouldStop(268435456);
mostCurrent._activity.LoadLayout("StageList",mostCurrent.activityBA);
 BA.debugLineNum = 33;BA.debugLine="If FirstTime Then";
Debug.ShouldStop(1);
if (_firsttime) { 
 };
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
public static String  _activity_pause(boolean _userclosed) throws Exception{
		Debug.PushSubsStack("Activity_Pause (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("UserClosed", _userclosed);
 BA.debugLineNum = 48;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
Debug.ShouldStop(32768);
 BA.debugLineNum = 50;BA.debugLine="End Sub";
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
public static String  _activity_resume() throws Exception{
		Debug.PushSubsStack("Activity_Resume (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
 BA.debugLineNum = 43;BA.debugLine="Sub Activity_Resume";
Debug.ShouldStop(1024);
 BA.debugLineNum = 44;BA.debugLine="SelectedStageID = -99";
Debug.ShouldStop(2048);
_selectedstageid = (long)(-99);
 BA.debugLineNum = 45;BA.debugLine="LoadSQLTables";
Debug.ShouldStop(4096);
_loadsqltables();
 BA.debugLineNum = 46;BA.debugLine="End Sub";
Debug.ShouldStop(8192);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _btnstageadd_click() throws Exception{
		Debug.PushSubsStack("btnStageAdd_Click (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
 BA.debugLineNum = 337;BA.debugLine="Sub btnStageAdd_Click";
Debug.ShouldStop(65536);
 BA.debugLineNum = 339;BA.debugLine="SelectedStageID = -98";
Debug.ShouldStop(262144);
_selectedstageid = (long)(-98);
 BA.debugLineNum = 340;BA.debugLine="StartActivity(EditStage)";
Debug.ShouldStop(524288);
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._editstage.getObject()));
 BA.debugLineNum = 341;BA.debugLine="End Sub";
Debug.ShouldStop(1048576);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _btnstagedelete_click() throws Exception{
		Debug.PushSubsStack("btnStageDelete_Click (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
int _ret = 0;
 BA.debugLineNum = 287;BA.debugLine="Sub btnStageDelete_Click()";
Debug.ShouldStop(1073741824);
 BA.debugLineNum = 289;BA.debugLine="Dim ret As Int";
Debug.ShouldStop(1);
_ret = 0;Debug.locals.put("ret", _ret);
 BA.debugLineNum = 291;BA.debugLine="If SelectedStageID <> -99 Then";
Debug.ShouldStop(4);
if (_selectedstageid!=-99) { 
 BA.debugLineNum = 292;BA.debugLine="ret = Msgbox2(\"Delete selected stage?\", \"Delete\", \"Delete\", \"Cancel\",\"\", Null)";
Debug.ShouldStop(8);
_ret = anywheresoftware.b4a.keywords.Common.Msgbox2("Delete selected stage?","Delete","Delete","Cancel","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);Debug.locals.put("ret", _ret);
 BA.debugLineNum = 293;BA.debugLine="If ret = DialogResponse.POSITIVE Then";
Debug.ShouldStop(16);
if (_ret==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 BA.debugLineNum = 294;BA.debugLine="Main.SQL1.ExecNonQuery(\"DELETE FROM stages WHERE stage_id =\" & SelectedStageID)";
Debug.ShouldStop(32);
mostCurrent._main._sql1.ExecNonQuery("DELETE FROM stages WHERE stage_id ="+BA.NumberToString(_selectedstageid));
 BA.debugLineNum = 295;BA.debugLine="LoadSQLTables";
Debug.ShouldStop(64);
_loadsqltables();
 };
 }else {
 BA.debugLineNum = 298;BA.debugLine="Msgbox(\"Select a stage to delete\", \"No selection\")";
Debug.ShouldStop(512);
anywheresoftware.b4a.keywords.Common.Msgbox("Select a stage to delete","No selection",mostCurrent.activityBA);
 };
 BA.debugLineNum = 300;BA.debugLine="End Sub";
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
public static String  _btnstagedown_click() throws Exception{
		Debug.PushSubsStack("btnStageDown_Click (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
jimsrobot.arduino.attiny85ad8495bt.stagelist._sortitem _tcurrentsort = null;
 BA.debugLineNum = 351;BA.debugLine="Sub btnStageDown_Click";
Debug.ShouldStop(1073741824);
 BA.debugLineNum = 354;BA.debugLine="Dim tCurrentSort As SortItem 'Item selected";
Debug.ShouldStop(2);
_tcurrentsort = new jimsrobot.arduino.attiny85ad8495bt.stagelist._sortitem();Debug.locals.put("tCurrentSort", _tcurrentsort);
 BA.debugLineNum = 356;BA.debugLine="tCurrentSort.StageID = SelectedStageID 		'";
Debug.ShouldStop(8);
_tcurrentsort.StageID = _selectedstageid;Debug.locals.put("tCurrentSort", _tcurrentsort);
 BA.debugLineNum = 357;BA.debugLine="tCurrentSort.SwapStageID = -1							' the stage that we're swapping into";
Debug.ShouldStop(16);
_tcurrentsort.SwapStageID = (long)(-1);Debug.locals.put("tCurrentSort", _tcurrentsort);
 BA.debugLineNum = 358;BA.debugLine="tCurrentSort.CurrentSortOrder = -1				' sort order of the selected stage";
Debug.ShouldStop(32);
_tcurrentsort.CurrentSortOrder = (long)(-1);Debug.locals.put("tCurrentSort", _tcurrentsort);
 BA.debugLineNum = 359;BA.debugLine="tCurrentSort.NewSortOrder = 1							' our new sort order";
Debug.ShouldStop(64);
_tcurrentsort.NewSortOrder = (long)(1);Debug.locals.put("tCurrentSort", _tcurrentsort);
 BA.debugLineNum = 361;BA.debugLine="If SelectedStageID > -95 Then";
Debug.ShouldStop(256);
if (_selectedstageid>-95) { 
 BA.debugLineNum = 363;BA.debugLine="If GetCurrentOrder(tCurrentSort) > 0 Then";
Debug.ShouldStop(1024);
if (_getcurrentorder(_tcurrentsort)>0) { 
 BA.debugLineNum = 365;BA.debugLine="If GetMaxOrder > tCurrentSort.CurrentSortOrder  Then";
Debug.ShouldStop(4096);
if (_getmaxorder()>_tcurrentsort.CurrentSortOrder) { 
 BA.debugLineNum = 367;BA.debugLine="GetHigherSortOrder(tCurrentSort)";
Debug.ShouldStop(16384);
_gethighersortorder(_tcurrentsort);
 BA.debugLineNum = 370;BA.debugLine="SwapSortOrder(tCurrentSort)";
Debug.ShouldStop(131072);
_swapsortorder(_tcurrentsort);
 BA.debugLineNum = 373;BA.debugLine="LoadSQLTables";
Debug.ShouldStop(1048576);
_loadsqltables();
 }else {
 BA.debugLineNum = 375;BA.debugLine="Msgbox(\"Already at the end\", \"End\")";
Debug.ShouldStop(4194304);
anywheresoftware.b4a.keywords.Common.Msgbox("Already at the end","End",mostCurrent.activityBA);
 };
 }else {
 BA.debugLineNum = 378;BA.debugLine="Msgbox(\"No sort order found\", \"Error\")";
Debug.ShouldStop(33554432);
anywheresoftware.b4a.keywords.Common.Msgbox("No sort order found","Error",mostCurrent.activityBA);
 };
 }else {
 BA.debugLineNum = 381;BA.debugLine="Msgbox(\"Select a stage\", \"No Selection\")";
Debug.ShouldStop(268435456);
anywheresoftware.b4a.keywords.Common.Msgbox("Select a stage","No Selection",mostCurrent.activityBA);
 };
 BA.debugLineNum = 383;BA.debugLine="End Sub";
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
public static String  _btnstageedit_click() throws Exception{
		Debug.PushSubsStack("btnStageEdit_Click (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
 BA.debugLineNum = 343;BA.debugLine="Sub btnStageEdit_Click";
Debug.ShouldStop(4194304);
 BA.debugLineNum = 344;BA.debugLine="If SelectedStageID < -95 Then";
Debug.ShouldStop(8388608);
if (_selectedstageid<-95) { 
 BA.debugLineNum = 345;BA.debugLine="Msgbox(\"Please select a stage to edit\", \"Select\")";
Debug.ShouldStop(16777216);
anywheresoftware.b4a.keywords.Common.Msgbox("Please select a stage to edit","Select",mostCurrent.activityBA);
 }else {
 BA.debugLineNum = 347;BA.debugLine="StartActivity(EditStage)";
Debug.ShouldStop(67108864);
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._editstage.getObject()));
 };
 BA.debugLineNum = 349;BA.debugLine="End Sub";
Debug.ShouldStop(268435456);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _btnstageup_click() throws Exception{
		Debug.PushSubsStack("btnStageUp_Click (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
jimsrobot.arduino.attiny85ad8495bt.stagelist._sortitem _tcurrentsort = null;
 BA.debugLineNum = 385;BA.debugLine="Sub btnStageUp_Click";
Debug.ShouldStop(1);
 BA.debugLineNum = 387;BA.debugLine="Dim tCurrentSort As SortItem 'Item selected";
Debug.ShouldStop(4);
_tcurrentsort = new jimsrobot.arduino.attiny85ad8495bt.stagelist._sortitem();Debug.locals.put("tCurrentSort", _tcurrentsort);
 BA.debugLineNum = 389;BA.debugLine="tCurrentSort.StageID = SelectedStageID 		'";
Debug.ShouldStop(16);
_tcurrentsort.StageID = _selectedstageid;Debug.locals.put("tCurrentSort", _tcurrentsort);
 BA.debugLineNum = 390;BA.debugLine="tCurrentSort.SwapStageID = -1							' the stage that we're swapping into";
Debug.ShouldStop(32);
_tcurrentsort.SwapStageID = (long)(-1);Debug.locals.put("tCurrentSort", _tcurrentsort);
 BA.debugLineNum = 391;BA.debugLine="tCurrentSort.CurrentSortOrder = -1				' sort order of the selected stage";
Debug.ShouldStop(64);
_tcurrentsort.CurrentSortOrder = (long)(-1);Debug.locals.put("tCurrentSort", _tcurrentsort);
 BA.debugLineNum = 392;BA.debugLine="tCurrentSort.NewSortOrder = 1							' our new sort order";
Debug.ShouldStop(128);
_tcurrentsort.NewSortOrder = (long)(1);Debug.locals.put("tCurrentSort", _tcurrentsort);
 BA.debugLineNum = 394;BA.debugLine="If SelectedStageID > -95 Then";
Debug.ShouldStop(512);
if (_selectedstageid>-95) { 
 BA.debugLineNum = 396;BA.debugLine="If GetCurrentOrder(tCurrentSort) > 0 Then";
Debug.ShouldStop(2048);
if (_getcurrentorder(_tcurrentsort)>0) { 
 BA.debugLineNum = 398;BA.debugLine="If GetMinOrder < tCurrentSort.CurrentSortOrder  Then";
Debug.ShouldStop(8192);
if (_getminorder()<_tcurrentsort.CurrentSortOrder) { 
 BA.debugLineNum = 400;BA.debugLine="GetLowerSortOrder(tCurrentSort)";
Debug.ShouldStop(32768);
_getlowersortorder(_tcurrentsort);
 BA.debugLineNum = 403;BA.debugLine="SwapSortOrder(tCurrentSort)";
Debug.ShouldStop(262144);
_swapsortorder(_tcurrentsort);
 BA.debugLineNum = 406;BA.debugLine="LoadSQLTables";
Debug.ShouldStop(2097152);
_loadsqltables();
 }else {
 BA.debugLineNum = 408;BA.debugLine="Msgbox(\"Already at the start\", \"End\")";
Debug.ShouldStop(8388608);
anywheresoftware.b4a.keywords.Common.Msgbox("Already at the start","End",mostCurrent.activityBA);
 };
 }else {
 BA.debugLineNum = 411;BA.debugLine="Msgbox(\"No sort order found\", \"Error\")";
Debug.ShouldStop(67108864);
anywheresoftware.b4a.keywords.Common.Msgbox("No sort order found","Error",mostCurrent.activityBA);
 };
 }else {
 BA.debugLineNum = 414;BA.debugLine="Msgbox(\"Select a stage\", \"No Selection\")";
Debug.ShouldStop(536870912);
anywheresoftware.b4a.keywords.Common.Msgbox("Select a stage","No Selection",mostCurrent.activityBA);
 };
 BA.debugLineNum = 416;BA.debugLine="End Sub";
Debug.ShouldStop(-2147483648);
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
		Debug.PushSubsStack("ClearList (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
int _i = 0;
 BA.debugLineNum = 101;BA.debugLine="Sub ClearList";
Debug.ShouldStop(16);
 BA.debugLineNum = 103;BA.debugLine="Dim i As Int";
Debug.ShouldStop(64);
_i = 0;Debug.locals.put("i", _i);
 BA.debugLineNum = 104;BA.debugLine="For i = scvStages.Panel.NumberOfViews -1 To 0 Step -1";
Debug.ShouldStop(128);
{
final double step52 = -1;
final double limit52 = (int)(0);
for (_i = (int)(mostCurrent._scvstages.getPanel().getNumberOfViews()-1); (step52 > 0 && _i <= limit52) || (step52 < 0 && _i >= limit52); _i += step52) {
Debug.locals.put("i", _i);
 BA.debugLineNum = 105;BA.debugLine="scvStages.Panel.RemoveViewAt(i)";
Debug.ShouldStop(256);
mostCurrent._scvstages.getPanel().RemoveViewAt(_i);
 }
}Debug.locals.put("i", _i);
;
 BA.debugLineNum = 108;BA.debugLine="End Sub";
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
public static long  _getcurrentorder(jimsrobot.arduino.attiny85ad8495bt.stagelist._sortitem _tsortitem) throws Exception{
		Debug.PushSubsStack("GetCurrentOrder (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
Object _ret = null;
String _ssql = "";
Debug.locals.put("tSortItem", _tsortitem);
 BA.debugLineNum = 125;BA.debugLine="Sub GetCurrentOrder(tSortItem As SortItem) As Long";
Debug.ShouldStop(268435456);
 BA.debugLineNum = 128;BA.debugLine="Dim ret As Object";
Debug.ShouldStop(-2147483648);
_ret = new Object();Debug.locals.put("ret", _ret);
 BA.debugLineNum = 129;BA.debugLine="If tSortItem.StageID > -1 Then";
Debug.ShouldStop(1);
if (_tsortitem.StageID>-1) { 
 BA.debugLineNum = 130;BA.debugLine="Dim sSQL As String";
Debug.ShouldStop(2);
_ssql = "";Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 131;BA.debugLine="sSQL = \"SELECT stage_sort_order FROM stages WHERE stage_id=\" & tSortItem.StageID";
Debug.ShouldStop(4);
_ssql = "SELECT stage_sort_order FROM stages WHERE stage_id="+BA.NumberToString(_tsortitem.StageID);Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 132;BA.debugLine="ret = Main.SQL1.ExecQuerySingleResult(sSQL)";
Debug.ShouldStop(8);
_ret = (Object)(mostCurrent._main._sql1.ExecQuerySingleResult(_ssql));Debug.locals.put("ret", _ret);
 BA.debugLineNum = 134;BA.debugLine="If ret <> Null Then";
Debug.ShouldStop(32);
if (_ret!= null) { 
 BA.debugLineNum = 135;BA.debugLine="tSortItem.CurrentSortOrder = ret";
Debug.ShouldStop(64);
_tsortitem.CurrentSortOrder = BA.ObjectToLongNumber(_ret);Debug.locals.put("tSortItem", _tsortitem);
 };
 BA.debugLineNum = 138;BA.debugLine="Return ret";
Debug.ShouldStop(512);
if (true) return BA.ObjectToLongNumber(_ret);
 }else {
 BA.debugLineNum = 140;BA.debugLine="Msgbox(\"Error getting current sort order\", \"2:Error\")";
Debug.ShouldStop(2048);
anywheresoftware.b4a.keywords.Common.Msgbox("Error getting current sort order","2:Error",mostCurrent.activityBA);
 BA.debugLineNum = 141;BA.debugLine="Return -1";
Debug.ShouldStop(4096);
if (true) return (long)(-1);
 };
 BA.debugLineNum = 143;BA.debugLine="End Sub";
Debug.ShouldStop(16384);
return 0L;
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static long  _gethighersortorder(jimsrobot.arduino.attiny85ad8495bt.stagelist._sortitem _tsortitem) throws Exception{
		Debug.PushSubsStack("GetHigherSortOrder (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
long _ret = 0L;
String _ssql = "";
Debug.locals.put("tSortItem", _tsortitem);
 BA.debugLineNum = 145;BA.debugLine="Sub GetHigherSortOrder(tSortItem As SortItem) As Long";
Debug.ShouldStop(65536);
 BA.debugLineNum = 146;BA.debugLine="Dim ret As Long";
Debug.ShouldStop(131072);
_ret = 0L;Debug.locals.put("ret", _ret);
 BA.debugLineNum = 147;BA.debugLine="If tSortItem.CurrentSortOrder > -1 Then";
Debug.ShouldStop(262144);
if (_tsortitem.CurrentSortOrder>-1) { 
 BA.debugLineNum = 148;BA.debugLine="Dim sSQL As String";
Debug.ShouldStop(524288);
_ssql = "";Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 149;BA.debugLine="sSQL = \"SELECT MIN(stage_sort_order) FROM stages WHERE profile_id=\" & ProfileList.SelectedProfileID & \" AND stage_sort_order > \" & tSortItem.CurrentSortOrder";
Debug.ShouldStop(1048576);
_ssql = "SELECT MIN(stage_sort_order) FROM stages WHERE profile_id="+BA.NumberToString(mostCurrent._profilelist._selectedprofileid)+" AND stage_sort_order > "+BA.NumberToString(_tsortitem.CurrentSortOrder);Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 150;BA.debugLine="ret = Main.SQL1.ExecQuerySingleResult(sSQL)";
Debug.ShouldStop(2097152);
_ret = (long)(Double.parseDouble(mostCurrent._main._sql1.ExecQuerySingleResult(_ssql)));Debug.locals.put("ret", _ret);
 BA.debugLineNum = 151;BA.debugLine="tSortItem.NewSortOrder = ret";
Debug.ShouldStop(4194304);
_tsortitem.NewSortOrder = _ret;Debug.locals.put("tSortItem", _tsortitem);
 BA.debugLineNum = 154;BA.debugLine="sSQL = \"SELECT stage_id FROM stages WHERE profile_id=\" & ProfileList.SelectedProfileID & \" AND stage_sort_order = \" & tSortItem.NewSortOrder";
Debug.ShouldStop(33554432);
_ssql = "SELECT stage_id FROM stages WHERE profile_id="+BA.NumberToString(mostCurrent._profilelist._selectedprofileid)+" AND stage_sort_order = "+BA.NumberToString(_tsortitem.NewSortOrder);Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 155;BA.debugLine="ret = Main.SQL1.ExecQuerySingleResult(sSQL)";
Debug.ShouldStop(67108864);
_ret = (long)(Double.parseDouble(mostCurrent._main._sql1.ExecQuerySingleResult(_ssql)));Debug.locals.put("ret", _ret);
 BA.debugLineNum = 156;BA.debugLine="tSortItem.SwapStageID = ret";
Debug.ShouldStop(134217728);
_tsortitem.SwapStageID = _ret;Debug.locals.put("tSortItem", _tsortitem);
 BA.debugLineNum = 158;BA.debugLine="Return ret";
Debug.ShouldStop(536870912);
if (true) return _ret;
 }else {
 BA.debugLineNum = 161;BA.debugLine="Msgbox(\"Error getting current sort order\", \"Error\")";
Debug.ShouldStop(1);
anywheresoftware.b4a.keywords.Common.Msgbox("Error getting current sort order","Error",mostCurrent.activityBA);
 BA.debugLineNum = 162;BA.debugLine="Return -1";
Debug.ShouldStop(2);
if (true) return (long)(-1);
 };
 BA.debugLineNum = 164;BA.debugLine="End Sub";
Debug.ShouldStop(8);
return 0L;
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static long  _getlowersortorder(jimsrobot.arduino.attiny85ad8495bt.stagelist._sortitem _tsortitem) throws Exception{
		Debug.PushSubsStack("GetLowerSortOrder (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
long _ret = 0L;
String _ssql = "";
Debug.locals.put("tSortItem", _tsortitem);
 BA.debugLineNum = 167;BA.debugLine="Sub GetLowerSortOrder(tSortItem As SortItem) As Long";
Debug.ShouldStop(64);
 BA.debugLineNum = 168;BA.debugLine="Dim ret As Long";
Debug.ShouldStop(128);
_ret = 0L;Debug.locals.put("ret", _ret);
 BA.debugLineNum = 169;BA.debugLine="If tSortItem.CurrentSortOrder > -1 Then";
Debug.ShouldStop(256);
if (_tsortitem.CurrentSortOrder>-1) { 
 BA.debugLineNum = 170;BA.debugLine="Dim sSQL As String";
Debug.ShouldStop(512);
_ssql = "";Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 171;BA.debugLine="sSQL = \"SELECT MAX(stage_sort_order) FROM stages WHERE profile_id=\" & ProfileList.SelectedProfileID & \" AND stage_sort_order < \" & tSortItem.CurrentSortOrder";
Debug.ShouldStop(1024);
_ssql = "SELECT MAX(stage_sort_order) FROM stages WHERE profile_id="+BA.NumberToString(mostCurrent._profilelist._selectedprofileid)+" AND stage_sort_order < "+BA.NumberToString(_tsortitem.CurrentSortOrder);Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 172;BA.debugLine="ret = Main.SQL1.ExecQuerySingleResult(sSQL)";
Debug.ShouldStop(2048);
_ret = (long)(Double.parseDouble(mostCurrent._main._sql1.ExecQuerySingleResult(_ssql)));Debug.locals.put("ret", _ret);
 BA.debugLineNum = 173;BA.debugLine="tSortItem.NewSortOrder = ret";
Debug.ShouldStop(4096);
_tsortitem.NewSortOrder = _ret;Debug.locals.put("tSortItem", _tsortitem);
 BA.debugLineNum = 176;BA.debugLine="sSQL = \"SELECT stage_id FROM stages WHERE profile_id=\" & ProfileList.SelectedProfileID & \" AND stage_sort_order = \" & tSortItem.NewSortOrder";
Debug.ShouldStop(32768);
_ssql = "SELECT stage_id FROM stages WHERE profile_id="+BA.NumberToString(mostCurrent._profilelist._selectedprofileid)+" AND stage_sort_order = "+BA.NumberToString(_tsortitem.NewSortOrder);Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 177;BA.debugLine="ret = Main.SQL1.ExecQuerySingleResult(sSQL)";
Debug.ShouldStop(65536);
_ret = (long)(Double.parseDouble(mostCurrent._main._sql1.ExecQuerySingleResult(_ssql)));Debug.locals.put("ret", _ret);
 BA.debugLineNum = 178;BA.debugLine="tSortItem.SwapStageID = ret";
Debug.ShouldStop(131072);
_tsortitem.SwapStageID = _ret;Debug.locals.put("tSortItem", _tsortitem);
 BA.debugLineNum = 180;BA.debugLine="Return ret";
Debug.ShouldStop(524288);
if (true) return _ret;
 }else {
 BA.debugLineNum = 183;BA.debugLine="Msgbox(\"Error getting current sort order\", \"Error\")";
Debug.ShouldStop(4194304);
anywheresoftware.b4a.keywords.Common.Msgbox("Error getting current sort order","Error",mostCurrent.activityBA);
 BA.debugLineNum = 184;BA.debugLine="Return -1";
Debug.ShouldStop(8388608);
if (true) return (long)(-1);
 };
 BA.debugLineNum = 186;BA.debugLine="End Sub";
Debug.ShouldStop(33554432);
return 0L;
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static long  _getmaxorder() throws Exception{
		Debug.PushSubsStack("GetMaxOrder (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
long _ret = 0L;
String _ssql = "";
 BA.debugLineNum = 210;BA.debugLine="Sub GetMaxOrder() As Long";
Debug.ShouldStop(131072);
 BA.debugLineNum = 211;BA.debugLine="Dim ret As Long";
Debug.ShouldStop(262144);
_ret = 0L;Debug.locals.put("ret", _ret);
 BA.debugLineNum = 212;BA.debugLine="If ProfileList.SelectedProfileID > -1 Then";
Debug.ShouldStop(524288);
if (mostCurrent._profilelist._selectedprofileid>-1) { 
 BA.debugLineNum = 213;BA.debugLine="Dim sSQL As String";
Debug.ShouldStop(1048576);
_ssql = "";Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 214;BA.debugLine="sSQL = \"SELECT MAX(stage_sort_order) FROM stages WHERE profile_id=\" & ProfileList.SelectedProfileID";
Debug.ShouldStop(2097152);
_ssql = "SELECT MAX(stage_sort_order) FROM stages WHERE profile_id="+BA.NumberToString(mostCurrent._profilelist._selectedprofileid);Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 215;BA.debugLine="ret = Main.SQL1.ExecQuerySingleResult(sSQL)";
Debug.ShouldStop(4194304);
_ret = (long)(Double.parseDouble(mostCurrent._main._sql1.ExecQuerySingleResult(_ssql)));Debug.locals.put("ret", _ret);
 BA.debugLineNum = 218;BA.debugLine="Return ret";
Debug.ShouldStop(33554432);
if (true) return _ret;
 }else {
 BA.debugLineNum = 223;BA.debugLine="Msgbox(\"Error getting sort order\", \"Error\")";
Debug.ShouldStop(1073741824);
anywheresoftware.b4a.keywords.Common.Msgbox("Error getting sort order","Error",mostCurrent.activityBA);
 BA.debugLineNum = 224;BA.debugLine="Return -1";
Debug.ShouldStop(-2147483648);
if (true) return (long)(-1);
 };
 BA.debugLineNum = 226;BA.debugLine="End Sub";
Debug.ShouldStop(2);
return 0L;
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static long  _getminorder() throws Exception{
		Debug.PushSubsStack("GetMinOrder (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
long _ret = 0L;
String _ssql = "";
 BA.debugLineNum = 228;BA.debugLine="Sub GetMinOrder() As Long";
Debug.ShouldStop(8);
 BA.debugLineNum = 229;BA.debugLine="Dim ret As Long";
Debug.ShouldStop(16);
_ret = 0L;Debug.locals.put("ret", _ret);
 BA.debugLineNum = 230;BA.debugLine="If ProfileList.SelectedProfileID > -1 Then";
Debug.ShouldStop(32);
if (mostCurrent._profilelist._selectedprofileid>-1) { 
 BA.debugLineNum = 231;BA.debugLine="Dim sSQL As String";
Debug.ShouldStop(64);
_ssql = "";Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 232;BA.debugLine="sSQL = \"SELECT MIN(stage_sort_order) FROM stages WHERE profile_id=\" & ProfileList.SelectedProfileID";
Debug.ShouldStop(128);
_ssql = "SELECT MIN(stage_sort_order) FROM stages WHERE profile_id="+BA.NumberToString(mostCurrent._profilelist._selectedprofileid);Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 233;BA.debugLine="ret = Main.SQL1.ExecQuerySingleResult(sSQL)";
Debug.ShouldStop(256);
_ret = (long)(Double.parseDouble(mostCurrent._main._sql1.ExecQuerySingleResult(_ssql)));Debug.locals.put("ret", _ret);
 BA.debugLineNum = 236;BA.debugLine="Return ret";
Debug.ShouldStop(2048);
if (true) return _ret;
 }else {
 BA.debugLineNum = 241;BA.debugLine="Msgbox(\"Error getting sort order\", \"Error\")";
Debug.ShouldStop(65536);
anywheresoftware.b4a.keywords.Common.Msgbox("Error getting sort order","Error",mostCurrent.activityBA);
 BA.debugLineNum = 242;BA.debugLine="Return -1";
Debug.ShouldStop(131072);
if (true) return (long)(-1);
 };
 BA.debugLineNum = 244;BA.debugLine="End Sub";
Debug.ShouldStop(524288);
return 0L;
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static long  _getstageidfromorder(jimsrobot.arduino.attiny85ad8495bt.stagelist._sortitem _tsortitem) throws Exception{
		Debug.PushSubsStack("GetStageIDFromOrder (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
long _ret = 0L;
String _ssql = "";
Debug.locals.put("tSortItem", _tsortitem);
 BA.debugLineNum = 110;BA.debugLine="Sub GetStageIDFromOrder(tSortItem As SortItem) As Long";
Debug.ShouldStop(8192);
 BA.debugLineNum = 112;BA.debugLine="Dim ret As Long";
Debug.ShouldStop(32768);
_ret = 0L;Debug.locals.put("ret", _ret);
 BA.debugLineNum = 113;BA.debugLine="If tSortItem.CurrentSortOrder > -1 Then";
Debug.ShouldStop(65536);
if (_tsortitem.CurrentSortOrder>-1) { 
 BA.debugLineNum = 114;BA.debugLine="Dim sSQL As String";
Debug.ShouldStop(131072);
_ssql = "";Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 115;BA.debugLine="sSQL = \"SELECT stage_id FROM stages WHERE sort_order=\" & tSortItem.CurrentSortOrder";
Debug.ShouldStop(262144);
_ssql = "SELECT stage_id FROM stages WHERE sort_order="+BA.NumberToString(_tsortitem.CurrentSortOrder);Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 116;BA.debugLine="ret = Main.SQL1.ExecQuerySingleResult(sSQL)";
Debug.ShouldStop(524288);
_ret = (long)(Double.parseDouble(mostCurrent._main._sql1.ExecQuerySingleResult(_ssql)));Debug.locals.put("ret", _ret);
 BA.debugLineNum = 117;BA.debugLine="tSortItem.StageID = ret";
Debug.ShouldStop(1048576);
_tsortitem.StageID = _ret;Debug.locals.put("tSortItem", _tsortitem);
 BA.debugLineNum = 118;BA.debugLine="Return ret";
Debug.ShouldStop(2097152);
if (true) return _ret;
 }else {
 BA.debugLineNum = 120;BA.debugLine="Msgbox(\"Error getting current sort order\", \"1:Error\")";
Debug.ShouldStop(8388608);
anywheresoftware.b4a.keywords.Common.Msgbox("Error getting current sort order","1:Error",mostCurrent.activityBA);
 BA.debugLineNum = 121;BA.debugLine="Return -1";
Debug.ShouldStop(16777216);
if (true) return (long)(-1);
 };
 BA.debugLineNum = 123;BA.debugLine="End Sub";
Debug.ShouldStop(67108864);
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
 //BA.debugLineNum = 15;BA.debugLine="Dim scvStages As ScrollView";
mostCurrent._scvstages = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Dim btnStageAdd As Button";
mostCurrent._btnstageadd = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Dim btnStageDelete As Button";
mostCurrent._btnstagedelete = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Dim btnStageUp As Button";
mostCurrent._btnstageup = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim btnStageDown As Button";
mostCurrent._btnstagedown = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Dim btnStageEdit As Button";
mostCurrent._btnstageedit = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Type SortItem(StageID As Long, SwapStageID As Long, CurrentSortOrder As Long, NewSortOrder As Long)";
;
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_click() throws Exception{
		Debug.PushSubsStack("lbl_Click (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
anywheresoftware.b4a.objects.LabelWrapper _send = null;
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
 BA.debugLineNum = 329;BA.debugLine="Sub lbl_Click";
Debug.ShouldStop(256);
 BA.debugLineNum = 330;BA.debugLine="Dim Send As Label";
Debug.ShouldStop(512);
_send = new anywheresoftware.b4a.objects.LabelWrapper();Debug.locals.put("Send", _send);
 BA.debugLineNum = 331;BA.debugLine="Dim pnl As Panel";
Debug.ShouldStop(1024);
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();Debug.locals.put("pnl", _pnl);
 BA.debugLineNum = 332;BA.debugLine="Send=Sender";
Debug.ShouldStop(2048);
_send.setObject((android.widget.TextView)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 BA.debugLineNum = 333;BA.debugLine="pnl = Send.tag";
Debug.ShouldStop(4096);
_pnl.setObject((android.view.ViewGroup)(_send.getTag()));
 BA.debugLineNum = 334;BA.debugLine="SelectPanel(pnl)";
Debug.ShouldStop(8192);
_selectpanel(_pnl);
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
public static String  _loadsqltables() throws Exception{
		Debug.PushSubsStack("LoadSQLTables (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
String _sstagetype = "";
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
int _i = 0;
anywheresoftware.b4a.objects.PanelWrapper _pnladd = null;
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
 BA.debugLineNum = 54;BA.debugLine="Sub LoadSQLTables";
Debug.ShouldStop(2097152);
 BA.debugLineNum = 56;BA.debugLine="Dim sStageType As String";
Debug.ShouldStop(8388608);
_sstagetype = "";Debug.locals.put("sStageType", _sstagetype);
 BA.debugLineNum = 58;BA.debugLine="Main.SQL1.ExecNonQuery(\"CREATE TABLE IF NOT EXISTS stages  	(stage_id INTEGER PRIMARY KEY AUTOINCREMENT, \" & _ 																															\"profile_id INTEGER, \" & _ 																															\"stage_type INTEGER,  \" & _ 																															\"stage_temp INTEGER,  \" & _ 																															\"stage_percent INTEGER,  \" & _ 																															\"stage_seconds INTEGER,  \" & _ 																															\"stage_sort_order INTEGER)\")";
Debug.ShouldStop(33554432);
mostCurrent._main._sql1.ExecNonQuery("CREATE TABLE IF NOT EXISTS stages  	(stage_id INTEGER PRIMARY KEY AUTOINCREMENT, "+"profile_id INTEGER, "+"stage_type INTEGER,  "+"stage_temp INTEGER,  "+"stage_percent INTEGER,  "+"stage_seconds INTEGER,  "+"stage_sort_order INTEGER)");
 BA.debugLineNum = 67;BA.debugLine="ClearList";
Debug.ShouldStop(4);
_clearlist();
 BA.debugLineNum = 69;BA.debugLine="Dim Cursor1 As Cursor";
Debug.ShouldStop(16);
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();Debug.locals.put("Cursor1", _cursor1);
 BA.debugLineNum = 71;BA.debugLine="If ProfileList.SelectedProfileID <> -1 Then	'do we have a profile selected";
Debug.ShouldStop(64);
if (mostCurrent._profilelist._selectedprofileid!=-1) { 
 BA.debugLineNum = 72;BA.debugLine="Cursor1 = Main.SQL1.ExecQuery(\"SELECT stage_id, stage_type, stage_temp, stage_percent, stage_seconds, stage_sort_order FROM stages WHERE profile_id=\" & ProfileList.SelectedProfileID &  \" ORDER BY stage_sort_order\")";
Debug.ShouldStop(128);
_cursor1.setObject((android.database.Cursor)(mostCurrent._main._sql1.ExecQuery("SELECT stage_id, stage_type, stage_temp, stage_percent, stage_seconds, stage_sort_order FROM stages WHERE profile_id="+BA.NumberToString(mostCurrent._profilelist._selectedprofileid)+" ORDER BY stage_sort_order")));
 BA.debugLineNum = 73;BA.debugLine="For i = 0 To Cursor1.RowCount - 1";
Debug.ShouldStop(256);
{
final double step30 = 1;
final double limit30 = (int)(_cursor1.getRowCount()-1);
for (_i = (int)(0); (step30 > 0 && _i <= limit30) || (step30 < 0 && _i >= limit30); _i += step30) {
Debug.locals.put("i", _i);
 BA.debugLineNum = 74;BA.debugLine="Dim pnlAdd As Panel";
Debug.ShouldStop(512);
_pnladd = new anywheresoftware.b4a.objects.PanelWrapper();Debug.locals.put("pnlAdd", _pnladd);
 BA.debugLineNum = 75;BA.debugLine="Dim lbl As Label";
Debug.ShouldStop(1024);
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();Debug.locals.put("lbl", _lbl);
 BA.debugLineNum = 76;BA.debugLine="pnlAdd.Initialize(\"pnlAdd\")";
Debug.ShouldStop(2048);
_pnladd.Initialize(mostCurrent.activityBA,"pnlAdd");
 BA.debugLineNum = 77;BA.debugLine="scvStages.Panel.AddView(pnlAdd,0,5dip+i*50dip,100%x,50dip)";
Debug.ShouldStop(4096);
mostCurrent._scvstages.getPanel().AddView((android.view.View)(_pnladd.getObject()),(int)(0),(int)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int)(5))+_i*anywheresoftware.b4a.keywords.Common.DipToCurrent((int)(50))),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float)(100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int)(50)));
 BA.debugLineNum = 79;BA.debugLine="Cursor1.Position = i";
Debug.ShouldStop(16384);
_cursor1.setPosition(_i);
 BA.debugLineNum = 80;BA.debugLine="Dim lbl As Label";
Debug.ShouldStop(32768);
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();Debug.locals.put("lbl", _lbl);
 BA.debugLineNum = 81;BA.debugLine="lbl.Initialize(\"lbl\")";
Debug.ShouldStop(65536);
_lbl.Initialize(mostCurrent.activityBA,"lbl");
 BA.debugLineNum = 82;BA.debugLine="sStageType = StageTypeToString(Cursor1.Getstring(\"stage_type\"))";
Debug.ShouldStop(131072);
_sstagetype = _stagetypetostring((int)(Double.parseDouble(_cursor1.GetString("stage_type"))));Debug.locals.put("sStageType", _sstagetype);
 BA.debugLineNum = 83;BA.debugLine="lbl.Text = 	Cursor1.Getstring(\"stage_sort_order\") & \" : \"  & sStageType & \" : \" & _ 										Cursor1.Getstring(\"stage_temp\") & \" C : \" & _ 										Cursor1.Getstring(\"stage_percent\") & \" % : \" & _ 										Cursor1.Getstring(\"stage_seconds\") & \" sec\"";
Debug.ShouldStop(262144);
_lbl.setText((Object)(_cursor1.GetString("stage_sort_order")+" : "+_sstagetype+" : "+_cursor1.GetString("stage_temp")+" C : "+_cursor1.GetString("stage_percent")+" % : "+_cursor1.GetString("stage_seconds")+" sec"));
 BA.debugLineNum = 88;BA.debugLine="pnlAdd.AddView(lbl, 0%x, 5dip, 100%x, 40dip)";
Debug.ShouldStop(8388608);
_pnladd.AddView((android.view.View)(_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float)(0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int)(5)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float)(100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int)(40)));
 BA.debugLineNum = 89;BA.debugLine="lbl.TextSize=15";
Debug.ShouldStop(16777216);
_lbl.setTextSize((float)(15));
 BA.debugLineNum = 90;BA.debugLine="lbl.TextColor = Colors.Black";
Debug.ShouldStop(33554432);
_lbl.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 BA.debugLineNum = 91;BA.debugLine="pnlAdd.Tag = Cursor1.GetInt(\"stage_id\")";
Debug.ShouldStop(67108864);
_pnladd.setTag((Object)(_cursor1.GetInt("stage_id")));
 BA.debugLineNum = 92;BA.debugLine="lbl.Tag = pnlAdd";
Debug.ShouldStop(134217728);
_lbl.setTag((Object)(_pnladd.getObject()));
 }
}Debug.locals.put("i", _i);
;
 BA.debugLineNum = 95;BA.debugLine="Cursor1.Close";
Debug.ShouldStop(1073741824);
_cursor1.Close();
 };
 BA.debugLineNum = 97;BA.debugLine="scvStages.Panel.Height=50%y";
Debug.ShouldStop(1);
mostCurrent._scvstages.getPanel().setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float)(50),mostCurrent.activityBA));
 BA.debugLineNum = 99;BA.debugLine="End Sub";
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
public static String  _pnladd_click() throws Exception{
		Debug.PushSubsStack("pnlAdd_Click (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
anywheresoftware.b4a.objects.PanelWrapper _send = null;
 BA.debugLineNum = 321;BA.debugLine="Sub pnlAdd_Click";
Debug.ShouldStop(1);
 BA.debugLineNum = 323;BA.debugLine="Dim send As Panel";
Debug.ShouldStop(4);
_send = new anywheresoftware.b4a.objects.PanelWrapper();Debug.locals.put("send", _send);
 BA.debugLineNum = 324;BA.debugLine="send = Sender";
Debug.ShouldStop(8);
_send.setObject((android.view.ViewGroup)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 BA.debugLineNum = 325;BA.debugLine="SelectPanel(send)";
Debug.ShouldStop(16);
_selectpanel(_send);
 BA.debugLineNum = 327;BA.debugLine="End Sub";
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
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim SelectedStageID As Long";
_selectedstageid = 0L;
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
public static String  _selectpanel(anywheresoftware.b4a.objects.PanelWrapper _pnl) throws Exception{
		Debug.PushSubsStack("SelectPanel (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
anywheresoftware.b4a.objects.ConcreteViewWrapper _v = null;
int _i = 0;
Debug.locals.put("pnl", _pnl);
 BA.debugLineNum = 302;BA.debugLine="Sub SelectPanel(pnl As Panel)";
Debug.ShouldStop(8192);
 BA.debugLineNum = 303;BA.debugLine="Dim v As View";
Debug.ShouldStop(16384);
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();Debug.locals.put("v", _v);
 BA.debugLineNum = 304;BA.debugLine="Dim i As Int";
Debug.ShouldStop(32768);
_i = 0;Debug.locals.put("i", _i);
 BA.debugLineNum = 306;BA.debugLine="For i = scvStages.Panel.NumberOfViews -1 To 0 Step -1";
Debug.ShouldStop(131072);
{
final double step199 = -1;
final double limit199 = (int)(0);
for (_i = (int)(mostCurrent._scvstages.getPanel().getNumberOfViews()-1); (step199 > 0 && _i <= limit199) || (step199 < 0 && _i >= limit199); _i += step199) {
Debug.locals.put("i", _i);
 BA.debugLineNum = 307;BA.debugLine="v = scvStages.Panel.GetView (i)";
Debug.ShouldStop(262144);
_v = mostCurrent._scvstages.getPanel().GetView(_i);Debug.locals.put("v", _v);
 BA.debugLineNum = 308;BA.debugLine="If v Is Panel Then";
Debug.ShouldStop(524288);
if (_v.getObjectOrNull() instanceof android.view.ViewGroup) { 
 BA.debugLineNum = 309;BA.debugLine="v.Color = Colors.white";
Debug.ShouldStop(1048576);
_v.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 };
 }
}Debug.locals.put("i", _i);
;
 BA.debugLineNum = 315;BA.debugLine="pnl.Color = Colors.LightGray";
Debug.ShouldStop(67108864);
_pnl.setColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 BA.debugLineNum = 318;BA.debugLine="SelectedStageID  = pnl.Tag";
Debug.ShouldStop(536870912);
_selectedstageid = BA.ObjectToLongNumber(_pnl.getTag());
 BA.debugLineNum = 319;BA.debugLine="End Sub";
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
public static String  _setorder(long _lorderid,long _lstageid) throws Exception{
		Debug.PushSubsStack("SetOrder (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
String _ssql = "";
Debug.locals.put("lOrderID", _lorderid);
Debug.locals.put("lStageID", _lstageid);
 BA.debugLineNum = 246;BA.debugLine="Sub SetOrder(lOrderID As Long, lStageID As Long)";
Debug.ShouldStop(2097152);
 BA.debugLineNum = 248;BA.debugLine="If lStageID = -1 Then";
Debug.ShouldStop(8388608);
if (_lstageid==-1) { 
 BA.debugLineNum = 249;BA.debugLine="Dim sSQL As String";
Debug.ShouldStop(16777216);
_ssql = "";Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 250;BA.debugLine="sSQL = \"SELECT MIN(stage_sort_order) FROM stages WHERE profile_id=\" & ProfileList.SelectedProfileID";
Debug.ShouldStop(33554432);
_ssql = "SELECT MIN(stage_sort_order) FROM stages WHERE profile_id="+BA.NumberToString(mostCurrent._profilelist._selectedprofileid);Debug.locals.put("sSQL", _ssql);
 }else {
 BA.debugLineNum = 253;BA.debugLine="Msgbox(\"Error getting sort order\", \"Error\")";
Debug.ShouldStop(268435456);
anywheresoftware.b4a.keywords.Common.Msgbox("Error getting sort order","Error",mostCurrent.activityBA);
 BA.debugLineNum = 254;BA.debugLine="Return -1";
Debug.ShouldStop(536870912);
if (true) return BA.NumberToString(-1);
 };
 BA.debugLineNum = 256;BA.debugLine="End Sub";
Debug.ShouldStop(-2147483648);
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
		Debug.PushSubsStack("StageTypeToString (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("iStageType", _istagetype);
 BA.debugLineNum = 260;BA.debugLine="Sub StageTypeToString(iStageType As Int) As String";
Debug.ShouldStop(8);
 BA.debugLineNum = 261;BA.debugLine="Select iStageType";
Debug.ShouldStop(16);
switch (_istagetype) {
case 1:
 BA.debugLineNum = 263;BA.debugLine="Return \"Do nothing\"";
Debug.ShouldStop(64);
if (true) return "Do nothing";
 break;
case 2:
 BA.debugLineNum = 265;BA.debugLine="Return \"Heat to\"";
Debug.ShouldStop(256);
if (true) return "Heat to";
 break;
case 3:
 BA.debugLineNum = 267;BA.debugLine="Return \"Beep\"";
Debug.ShouldStop(1024);
if (true) return "Beep";
 break;
}
;
 BA.debugLineNum = 270;BA.debugLine="End Sub";
Debug.ShouldStop(8192);
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
		Debug.PushSubsStack("StringToStageType (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("sStageType", _sstagetype);
 BA.debugLineNum = 272;BA.debugLine="Sub StringToStageType(sStageType As String) As Int";
Debug.ShouldStop(32768);
 BA.debugLineNum = 273;BA.debugLine="Select sStageType";
Debug.ShouldStop(65536);
switch (BA.switchObjectToInt(_sstagetype,"Do nothing","Heat to","Beep")) {
case 0:
 BA.debugLineNum = 275;BA.debugLine="Return 1";
Debug.ShouldStop(262144);
if (true) return (int)(1);
 break;
case 1:
 BA.debugLineNum = 277;BA.debugLine="Return 2";
Debug.ShouldStop(1048576);
if (true) return (int)(2);
 break;
case 2:
 BA.debugLineNum = 279;BA.debugLine="Return 3";
Debug.ShouldStop(4194304);
if (true) return (int)(3);
 break;
}
;
 BA.debugLineNum = 282;BA.debugLine="End Sub";
Debug.ShouldStop(33554432);
return 0;
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _swapsortorder(jimsrobot.arduino.attiny85ad8495bt.stagelist._sortitem _tsortitem) throws Exception{
		Debug.PushSubsStack("SwapSortOrder (stagelist) ","stagelist",3,mostCurrent.activityBA,mostCurrent);
try {
String _ssql = "";
Debug.locals.put("tSortItem", _tsortitem);
 BA.debugLineNum = 188;BA.debugLine="Sub SwapSortOrder(tSortItem As SortItem)";
Debug.ShouldStop(134217728);
 BA.debugLineNum = 190;BA.debugLine="If tSortItem.StageID  > -1 AND tSortItem.SwapStageID > -1 Then";
Debug.ShouldStop(536870912);
if (_tsortitem.StageID>-1 && _tsortitem.SwapStageID>-1) { 
 BA.debugLineNum = 191;BA.debugLine="Main.SQL1.BeginTransaction";
Debug.ShouldStop(1073741824);
mostCurrent._main._sql1.BeginTransaction();
 BA.debugLineNum = 193;BA.debugLine="Dim sSQL As String";
Debug.ShouldStop(1);
_ssql = "";Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 194;BA.debugLine="sSQL = \"UPDATE stages SET stage_sort_order = \" & tSortItem.NewSortOrder & \" WHERE stage_id = \" & tSortItem.StageID";
Debug.ShouldStop(2);
_ssql = "UPDATE stages SET stage_sort_order = "+BA.NumberToString(_tsortitem.NewSortOrder)+" WHERE stage_id = "+BA.NumberToString(_tsortitem.StageID);Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 195;BA.debugLine="Main.SQL1.ExecNonQuery(sSQL)";
Debug.ShouldStop(4);
mostCurrent._main._sql1.ExecNonQuery(_ssql);
 BA.debugLineNum = 198;BA.debugLine="sSQL = \"UPDATE stages SET stage_sort_order = \" & tSortItem.CurrentSortOrder & \" WHERE stage_id = \" & tSortItem.SwapStageID";
Debug.ShouldStop(32);
_ssql = "UPDATE stages SET stage_sort_order = "+BA.NumberToString(_tsortitem.CurrentSortOrder)+" WHERE stage_id = "+BA.NumberToString(_tsortitem.SwapStageID);Debug.locals.put("sSQL", _ssql);
 BA.debugLineNum = 199;BA.debugLine="Main.SQL1.ExecNonQuery(sSQL)";
Debug.ShouldStop(64);
mostCurrent._main._sql1.ExecNonQuery(_ssql);
 BA.debugLineNum = 201;BA.debugLine="Main.SQL1.TransactionSuccessful";
Debug.ShouldStop(256);
mostCurrent._main._sql1.TransactionSuccessful();
 BA.debugLineNum = 202;BA.debugLine="Main.SQL1.EndTransaction";
Debug.ShouldStop(512);
mostCurrent._main._sql1.EndTransaction();
 }else {
 BA.debugLineNum = 205;BA.debugLine="Msgbox(\"Error getting current sort orders and stages\", \"Error\")";
Debug.ShouldStop(4096);
anywheresoftware.b4a.keywords.Common.Msgbox("Error getting current sort orders and stages","Error",mostCurrent.activityBA);
 BA.debugLineNum = 206;BA.debugLine="Return -1";
Debug.ShouldStop(8192);
if (true) return BA.NumberToString(-1);
 };
 BA.debugLineNum = 208;BA.debugLine="End Sub";
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
  public Object[] GetGlobals() {
		return new Object[] {"Activity",_activity,"SelectedStageID",_selectedstageid,"scvStages",_scvstages,"btnStageAdd",_btnstageadd,"btnStageDelete",_btnstagedelete,"btnStageUp",_btnstageup,"btnStageDown",_btnstagedown,"btnStageEdit",_btnstageedit,"Main",Debug.moduleToString(jimsrobot.arduino.attiny85ad8495bt.main.class),"ProfileList",Debug.moduleToString(jimsrobot.arduino.attiny85ad8495bt.profilelist.class),"EditStage",Debug.moduleToString(jimsrobot.arduino.attiny85ad8495bt.editstage.class)};
}
}
