package jimsrobot.arduino.attiny85ad8495bt.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_stagelist{

public static void LS_general(java.util.HashMap<String, anywheresoftware.b4a.objects.ViewWrapper<?>> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
views.get("btnstageadd").setLeft((int)((0d / 100 * width)));
views.get("btnstageadd").setWidth((int)((20d / 100 * width)));
views.get("btnstageadd").setHeight((int)((views.get("btnstageadd").getWidth())));
views.get("btnstageadd").setTop((int)((100d / 100 * height) - (views.get("btnstageadd").getHeight())));
views.get("btnstagedelete").setLeft((int)((40d / 100 * width)));
views.get("btnstagedelete").setWidth((int)((20d / 100 * width)));
views.get("btnstagedelete").setHeight((int)((views.get("btnstagedelete").getWidth())));
views.get("btnstagedelete").setTop((int)((100d / 100 * height) - (views.get("btnstagedelete").getHeight())));
views.get("btnstageup").setLeft((int)((60d / 100 * width)));
views.get("btnstageup").setWidth((int)((20d / 100 * width)));
views.get("btnstageup").setHeight((int)((views.get("btnstageup").getWidth())));
views.get("btnstageup").setTop((int)((100d / 100 * height) - (views.get("btnstageup").getHeight())));
views.get("btnstageedit").setLeft((int)((20d / 100 * width)));
views.get("btnstageedit").setWidth((int)((20d / 100 * width)));
views.get("btnstageedit").setHeight((int)((views.get("btnstageup").getWidth())));
views.get("btnstageedit").setTop((int)((100d / 100 * height) - (views.get("btnstageedit").getHeight())));
views.get("btnstagedown").setLeft((int)((80d / 100 * width)));
views.get("btnstagedown").setWidth((int)((20d / 100 * width)));
views.get("btnstagedown").setHeight((int)((views.get("btnstagedown").getWidth())));
views.get("btnstagedown").setTop((int)((100d / 100 * height) - (views.get("btnstagedown").getHeight())));
views.get("scvstages").setWidth((int)((100d / 100 * width)));
views.get("scvstages").setHeight((int)(((100d / 100 * height)-(views.get("btnstagedown").getHeight()))));

}
}