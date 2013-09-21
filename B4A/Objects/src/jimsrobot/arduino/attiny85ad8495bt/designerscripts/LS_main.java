package jimsrobot.arduino.attiny85ad8495bt.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_main{

public static void LS_general(java.util.HashMap<String, anywheresoftware.b4a.objects.ViewWrapper<?>> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
views.get("btnprofiles").setWidth((int)((20d / 100 * width)));
views.get("btnprofiles").setHeight((int)((views.get("btnprofiles").getWidth())));
views.get("btnprofiles").setLeft((int)((80d / 100 * width)));
views.get("btnprofiles").setTop((int)(0d));
views.get("lblcurrentprofile").setWidth((int)((80d / 100 * width)));
views.get("lblcurrentprofile").setLeft((int)(0d));
views.get("lblcurrentprofile").setHeight((int)((views.get("btnprofiles").getWidth())));
views.get("btnstartstop").setWidth((int)((20d / 100 * width)));
views.get("btnstartstop").setHeight((int)((views.get("btnstartstop").getWidth())));
views.get("btnstartstop").setLeft((int)((80d / 100 * width) - (views.get("btnstartstop").getWidth())));
views.get("btnstartstop").setTop((int)((100d / 100 * height) - (views.get("btnstartstop").getHeight())));
views.get("btnquit").setWidth((int)((20d / 100 * width)));
views.get("btnquit").setHeight((int)((views.get("btnstartstop").getWidth())));
views.get("btnquit").setLeft((int)((100d / 100 * width) - (views.get("btnquit").getWidth())));
views.get("btnquit").setTop((int)((100d / 100 * height) - (views.get("btnquit").getHeight())));
views.get("lbltemp").setLeft((int)(0d));
views.get("lbltemp").setWidth((int)((80d / 100 * width)));
views.get("lbltemp").setHeight((int)((views.get("btnstartstop").getHeight())));
views.get("lbltemp").setTop((int)((100d / 100 * height) - (views.get("lbltemp").getHeight())));
views.get("txtlog").setTop((int)((views.get("btnstartstop").getHeight())));
views.get("txtlog").setWidth((int)((100d / 100 * width)));
views.get("txtlog").setHeight((int)((100d / 100 * height)-(views.get("lbltemp").getHeight())-(views.get("btnstartstop").getHeight())));

}
}